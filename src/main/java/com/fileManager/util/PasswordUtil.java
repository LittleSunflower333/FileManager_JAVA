package com.fileManager.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class PasswordUtil {

    // 使用 SHA-256 进行加密
    public static String encode(String rawPassword) {
        try {
            // 获取 SHA-256 MessageDigest 实例
            MessageDigest digest = MessageDigest.getInstance("SHA-256");

            // 对密码进行加密
            byte[] hashBytes = digest.digest(rawPassword.getBytes());

            // 转换为十六进制字符串
            StringBuilder hexString = new StringBuilder();
            for (byte b : hashBytes) {
                hexString.append(String.format("%02x", b));
            }

            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-256 algorithm not found", e);
        }
    }

    // 验证密码
    public static boolean matches(String rawPassword, String encodedPassword) {
        // 对输入的密码进行加密并与存储的加密密码进行比较
        return encode(rawPassword).equals(encodedPassword);
    }

    public static void main(String[] args) {
        String password = "myPassword";

        // 加密密码
        String encryptedPassword = encode(password);
        System.out.println("Encrypted password: " + encryptedPassword);

        // 比较密码
        boolean isMatch = matches(password, encryptedPassword);
        System.out.println("Password matches: " + isMatch);
    }
}
