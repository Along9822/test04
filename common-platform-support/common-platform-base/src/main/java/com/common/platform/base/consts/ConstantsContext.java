package com.common.platform.base.consts;

import com.common.platform.base.enums.CommonStatus;
import com.common.platform.base.utils.CoreUtil;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;

import java.io.File;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static com.common.platform.base.consts.ConfigConstant.SYSTEM_CONSTANT_PREFIX;
import static com.common.platform.base.utils.CoreUtil.getTempPath;

@Slf4j
public class ConstantsContext {
    private static final String TIPS_END = "，若想忽略此提示，请在开发管理->系统配置->参数配置，设置相关参数！";

     static Logger log;

    /**
     * 所有的常量，可以增删改查
     */
    private static Map<String, Object> CONSTNTS_HOLDER = new ConcurrentHashMap<>();

    /**
     * 添加系统常量
     */
    public static void putConstant(String key, Object value) {
        if (CoreUtil.isOneEmpty(key, value)) {
            return;
        }
        CONSTNTS_HOLDER.put(key, value);
    }

    /**
     * 删除常量
     */
    public static void deleteConstant(String key) {
        if (CoreUtil.isOneEmpty(key)) {
            return;
        }

        //如果是系统常量
        if (!key.startsWith(SYSTEM_CONSTANT_PREFIX)) {
            CONSTNTS_HOLDER.remove(key);
        }
    }

    /**
     * 获取系统常量
     */
    public static Map<String, Object> getConstntsMap() {
        return CONSTNTS_HOLDER;
    }

    /**
     * 获取验证码开关
     */
    public static Boolean getKaptchaOpen() {
        String commonPlatformKaptchaOpen = (String) CONSTNTS_HOLDER.get("COMMON_PLATFORM_KAPTCHA_OPEN");
        if (CommonStatus.ENABLE.getCode().equalsIgnoreCase(commonPlatformKaptchaOpen)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 获取管理系统名称
     */
    public static String getSystemName() {
        String systemName = (String) CONSTNTS_HOLDER.get("COMMON_PLATFORM_SYSTEM_NAME");
        if (CoreUtil.isEmpty(systemName)) {
            log.error("系统常量存在空值！常量名称：COMMON_PLATFORM_SYSTEM_NAME，采用默认名称：通用快速开发平台" + TIPS_END);
            return "通用快速开发平台";
        } else {
            return systemName;
        }
    }

    /**
     * 获取管理系统默认密码
     */
    public static String getDefaultPassword() {
        String defaultPassword = (String) CONSTNTS_HOLDER.get("COMMON_PLATFORM_DEFAULT_PASSWORD");
        if (CoreUtil.isEmpty(defaultPassword)) {
            log.error("系统常量存在空值！常量名称：COMMON_PLATFORM_DEFAULT_PASSWORD，采用默认密码：111111" + TIPS_END);
            return "111111";
        } else {
            return defaultPassword;
        }
    }

    /**
     * 获取管理系统OAUTH2用户前缀
     */
    public static String getOAuth2UserPrefix() {
        String oauth2Prefix = (String) CONSTNTS_HOLDER.get("COMMON_PLATFORM_OAUTH2_PREFIX");
        if (CoreUtil.isEmpty(oauth2Prefix)) {
            log.error("系统常量存在空值！常量名称：COMMON_PLATFORM_OAUTH2_PREFIX，采用默认值：oauth2" + TIPS_END);
            return "oauth2";
        } else {
            return oauth2Prefix;
        }
    }

    /**
     * 获取系统发布的版本号（防止css和js的缓存）
     */
    public static String getReleaseVersion() {
        String systemReleaseVersion = (String) CONSTNTS_HOLDER.get("COMMON_PLATFORM_SYSTEM_RELEASE_VERSION");
        if (CoreUtil.isEmpty(systemReleaseVersion)) {
            log.error("系统常量存在空值！常量名称：COMMON_PLATFORM_SYSTEM_RELEASE_VERSION，采用默认值：commonPlatform" + TIPS_END);
            return CoreUtil.getRandomString(8);
        } else {
            return systemReleaseVersion;
        }
    }

    /**
     * 获取文件上传路径(用于头像和富文本编辑器)
     */
    public static String getFileUploadPath() {
        String commonPlatformFileUploadPath = (String) CONSTNTS_HOLDER.get("COMMON_PLATFORM_FILE_UPLOAD_PATH");
        if (CoreUtil.isEmpty(commonPlatformFileUploadPath)) {
            log.error("系统常量存在空值！常量名称：COMMON_PLATFORM_FILE_UPLOAD_PATH，采用默认值：系统tmp目录" + TIPS_END);
            return getTempPath();
        } else {
            //判断有没有结尾符
            if (!commonPlatformFileUploadPath.endsWith(File.separator)) {
                commonPlatformFileUploadPath = commonPlatformFileUploadPath + File.separator;
            }

            //判断目录存不存在
            File file = new File(commonPlatformFileUploadPath);
            if (!file.exists()) {
                boolean mkdirs = file.mkdirs();
                if (mkdirs) {
                    return commonPlatformFileUploadPath;
                } else {
                    log.error("系统常量存在空值！常量名称：COMMON_PLATFORM_FILE_UPLOAD_PATH，采用默认值：系统tmp目录" + TIPS_END);
                    return getTempPath();
                }
            } else {
                return commonPlatformFileUploadPath;
            }
        }
    }

    /**
     * 获取系统地密钥
     */
    public static String getJwtSecret() {
        String systemReleaseVersion = (String) CONSTNTS_HOLDER.get("COMMON_PLATFORM_JWT_SECRET");
        if (CoreUtil.isEmpty(systemReleaseVersion)) {
            String randomSecret = CoreUtil.getRandomString(32);
            CONSTNTS_HOLDER.put("COMMON_PLATFORM_JWT_SECRET", randomSecret);
            log.error("jwt密钥存在空值！常量名称：COMMON_PLATFORM_JWT_SECRET，采用默认值：随机字符串->" + randomSecret + TIPS_END);
            return randomSecret;
        } else {
            return systemReleaseVersion;
        }
    }

    /**
     * 获取系统地密钥过期时间（单位：秒）
     */
    public static Long getJwtSecretExpireSec() {
        Long defaultSecs = 86400L;
        String systemReleaseVersion = (String) CONSTNTS_HOLDER.get("COMMON_PLATFORM_JWT_SECRET_EXPIRE");
        if (CoreUtil.isEmpty(systemReleaseVersion)) {
            log.error("jwt密钥存在空值！常量名称：COMMON_PLATFORM_JWT_SECRET_EXPIRE，采用默认值：1天" + TIPS_END);
            CONSTNTS_HOLDER.put("COMMON_PLATFORM_JWT_SECRET_EXPIRE", String.valueOf(defaultSecs));
            return defaultSecs;
        } else {
            try {
                return Long.valueOf(systemReleaseVersion);
            } catch (NumberFormatException e) {
                log.error("jwt密钥过期时间不是数字！常量名称：COMMON_PLATFORM_JWT_SECRET_EXPIRE，采用默认值：1天" + TIPS_END);
                CONSTNTS_HOLDER.put("COMMON_PLATFORM_JWT_SECRET_EXPIRE", String.valueOf(defaultSecs));
                return defaultSecs;
            }
        }
    }

    /**
     * 获取token的header标识
     */
    public static String getTokenHeaderName() {
        String tokenHeaderName = (String) CONSTNTS_HOLDER.get("COMMON_PLATFORM_TOKEN_HEADER_NAME");
        if (CoreUtil.isEmpty(tokenHeaderName)) {
            String defaultName = "Authorization";
            CONSTNTS_HOLDER.put("COMMON_PLATFORM_TOKEN_HEADER_NAME", defaultName);
            log.error("获取token的header标识为空！常量名称：COMMON_PLATFORM_TOKEN_HEADER_NAME，采用默认值：" + defaultName + TIPS_END);
            return defaultName;
        } else {
            return tokenHeaderName;
        }
    }

}

