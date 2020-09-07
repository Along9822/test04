package com.common.platform.sys.modular.system.service;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.io.IoUtil;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.common.platform.auth.context.LoginContextHolder;
import com.common.platform.auth.pojo.LoginUser;
import com.common.platform.base.config.sys.DefaultAvatar;
import com.common.platform.base.consts.ConstantsContext;
import com.common.platform.base.exception.BizExceptionEnum;
import com.common.platform.base.exception.CoreExceptionEnum;
import com.common.platform.base.exception.ServiceException;
import com.common.platform.base.utils.CoreUtil;
import com.common.platform.sys.modular.system.entity.FileInfo;
import com.common.platform.sys.modular.system.entity.User;
import com.common.platform.sys.modular.system.mapper.FileInfoMapper;
import com.common.platform.sys.modular.system.model.UploadResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.math.BigDecimal;

/**
 * 文件信息表
 */
@Service
@Slf4j
public class FileInfoService extends ServiceImpl<FileInfoMapper, FileInfo> {

    @Autowired
    private UserService userService;

    /**
     * 更新头像
     */
    @Transactional(rollbackFor = Exception.class)
    public void updateAvatar(String fileId) {
        LoginUser currentUser = LoginContextHolder.getContext().getUser();
        if (currentUser == null) {
            throw new ServiceException(CoreExceptionEnum.NO_CURRENT_USER);
        }

        User user = userService.getById(currentUser.getId());

        //更新用户的头像
        user.setAvatar(fileId);
        userService.updateById(user);
    }

    /**
     * 预览当前用户头像
     */
    public byte[] previewAvatar() {

        LoginUser currentUser = LoginContextHolder.getContext().getUser();
        if (currentUser == null) {
            throw new ServiceException(CoreExceptionEnum.NO_CURRENT_USER);
        }

        //获取当前用户的头像id
        User user = userService.getById(currentUser.getId());
        String avatar = user.getAvatar();

        //如果头像id为空就返回默认的
        if (CoreUtil.isEmpty(avatar)) {
            return Base64.decode(DefaultAvatar.BASE_64_AVATAR);
        } else {

            //文件id不为空就查询文件记录
            FileInfo fileInfo = this.getById(avatar);
            if (fileInfo == null) {
                return Base64.decode(DefaultAvatar.BASE_64_AVATAR);
            } else {
                try {
                    String filePath = fileInfo.getFilePath();
                    return IoUtil.readBytes(new FileInputStream(filePath));
                } catch (FileNotFoundException e) {
                    log.error("头像未找到！", e);
                    return Base64.decode(DefaultAvatar.BASE_64_AVATAR);
                }
            }
        }

    }

    /**
     * 上传文件（默认上传路径）
     */
    public UploadResult uploadFile(MultipartFile file) {
        String fileSavePath = ConstantsContext.getFileUploadPath();
        return this.uploadFile(file, fileSavePath);
    }

    /**
     * 上传文件（指定上传路径）
     */
    public UploadResult uploadFile(MultipartFile file, String fileSavePath) {

        UploadResult uploadResult = new UploadResult();

        //生成文件的唯一id
        String fileId = IdWorker.getIdStr();
        uploadResult.setFileId(fileId);

        //获取文件后缀
        String fileSuffix = CoreUtil.getFileSuffix(file.getOriginalFilename());
        uploadResult.setFileSuffix(fileSuffix);

        //获取文件原始名称
        String originalFilename = file.getOriginalFilename();
        uploadResult.setOriginalFilename(originalFilename);

        //生成文件的最终名称
        String finalName = fileId + "." + CoreUtil.getFileSuffix(originalFilename);
        uploadResult.setFinalName(finalName);
        uploadResult.setFileSavePath(fileSavePath + finalName);

        try {
            //保存文件到指定目录
            File newFile = new File(fileSavePath + finalName);
            file.transferTo(newFile);

            //保存文件信息
            FileInfo fileInfo = new FileInfo();
            fileInfo.setFileId(fileId);
            fileInfo.setFileName(originalFilename);
            fileInfo.setFileSuffix(fileSuffix);
            fileInfo.setFilePath(fileSavePath + finalName);
            fileInfo.setFinalName(finalName);

            //计算文件大小kb
            long kb = new BigDecimal(file.getSize())
                    .divide(BigDecimal.valueOf(1024))
                    .setScale(0, BigDecimal.ROUND_HALF_UP).longValue();
            fileInfo.setFileSizeKb(kb);
            this.save(fileInfo);
        } catch (Exception e) {
            log.error("上传文件错误！", e);
            throw new ServiceException(BizExceptionEnum.UPLOAD_ERROR);
        }

        return uploadResult;

    }
}