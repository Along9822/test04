package com.common.platform.base.utils;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.date.DateUtil;
import com.common.platform.base.config.app.AppNameProperties;
import com.common.platform.base.config.context.SpringContextHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URISyntaxException;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.Objects;
import java.util.Random;

public class CoreUtil extends ValidateUtil{
    /**
     * 默认密码盐长度
     */
    public static final int SALT_LENGTH = 6;

    /**
     * 获取随机字符,自定义长度
     */
    public static String getRandomString(int length) {
        String base = "abcdefghijklmnopqrstuvwxyz0123456789";
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            int number = random.nextInt(base.length());
            sb.append(base.charAt(number));
        }
        return sb.toString();
    }

    /**
     * md5加密(加盐)
     */
    public static String md5Hex(String password, String salt) throws NoSuchAlgorithmException {
        return MD5Util.encrypt(password + salt);
    }

    /**
     * 获取异常的具体信息
     */
    public static String getExceptionMsg(Throwable e) {
        StringWriter sw = new StringWriter();
        try {
            e.printStackTrace(new PrintWriter(sw));
        } finally {
            try {
                sw.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
        return sw.getBuffer().toString().replaceAll("\\$", "T");
    }

    /**
     * 获取应用名称
     */
    public static String getApplicationName() {
        try {
            AppNameProperties appNameProperties =
                    SpringContextHolder.getBean(AppNameProperties.class);
            return appNameProperties.getName();
        } catch (Exception e) {
            Logger logger = LoggerFactory.getLogger(CoreUtil.class);
            logger.error("获取应用名称错误！", e);
            return "";
        }
    }


    /**
     * 拷贝属性，为null的不拷贝
     */
    public static void copyProperties(Object source, Object target) {
        BeanUtil.copyProperties(source, target, CopyOptions.create().setIgnoreNullValue(true).ignoreError());
    }

    /**
     * 判断是否是windows操作系统
     */
    public static Boolean isWinOs() {
        String os = System.getProperty("os.name");
        return os.toLowerCase().startsWith("win");
    }

    /**
     * 获取临时目录
     */
    public static String getTempPath() {
        return System.getProperty("java.io.tmpdir");
    }

    /**
     * 获取项目路径
     */
    public static String getWebRootPath(String filePath) {
        try {
            String path = Objects.requireNonNull(CoreUtil.class.getClassLoader().getResource("")).toURI().getPath();
            path = path.replace("/WEB-INF/classes/", "");
            path = path.replace("/target/classes/", "");
            path = path.replace("file:/", "");
            if (isEmpty(filePath)) {
                return path;
            } else {
                return path + "/" + filePath;
            }
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 获取文件后缀名，不包含点
     */
    public static String getFileSuffix(String fileWholeName){
        if (isEmpty(fileWholeName)){
            return "none";
        }
        int lastIndexOf=fileWholeName.lastIndexOf(".");
        return fileWholeName.substring(lastIndexOf+1);
    }

    /**
     * 判断一个对象是否是时间类型
     */
    public static String dateType(Object o){
        if(o instanceof Date){
            return DateUtil.formatDate((Date) o);
        }else{
            return o.toString();
        }
    }

    /**
     * 当前时间
     */
    public static String currentTime(){
        return DateUtil.formatDateTime(new Date());
    }
}
