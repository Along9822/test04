package com.common.platform.base.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/*
 * MD5工具类
 */
public class MD5Util {

    public static String encrypt(String source) throws NoSuchAlgorithmException {
        return encodeMd5(source.getBytes());
    }

    private static String encodeMd5(byte[] source) throws NoSuchAlgorithmException {
        return encodeHex(MessageDigest.getInstance("MD5").digest(source));
    }

    private static String encodeHex(byte[] bytes) {
        StringBuilder buffer = new StringBuilder(bytes.length * 2);
        for (byte aByte : bytes) {
            if (((int) aByte & 0xff) < 0x10)
                buffer.append("0");
            buffer.append(Long.toString((int) aByte & 0xff, 16));
        }
        return buffer.toString();
    }

}

