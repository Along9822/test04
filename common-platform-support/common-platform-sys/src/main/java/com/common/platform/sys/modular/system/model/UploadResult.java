package com.common.platform.sys.modular.system.model;

import lombok.Data;

/**
 * 文件上传结果
 */
@Data
public class UploadResult {

    /**
     * 文件唯一id
     */
    private String fileId;

    /**
     * 文件后缀
     */
    private String fileSuffix;

    /**
     * 文件原始名称（带后缀）
     */
    private String originalFilename;

    /**
     * 文件最终名称（带后缀）
     */
    private String finalName;

    /**
     * 最终文件存储路径
     */
    private String fileSavePath;
}