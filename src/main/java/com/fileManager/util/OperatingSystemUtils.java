package com.fileManager.util;

public class OperatingSystemUtils {

    /**
     * 获取当前操作系统的名称
     *
     * @return 操作系统名称
     */
    public static String getOperatingSystem() {
        return System.getProperty("os.name").toLowerCase();
    }

    /**
     * 判断当前操作系统是否为 Windows
     *
     * @return true 如果是 Windows 操作系统，false 否则
     */
    public static boolean isWindows() {
        return getOperatingSystem().contains("win");
    }

    /**
     * 判断当前操作系统是否为 Linux
     *
     * @return true 如果是 Linux 操作系统，false 否则
     */
    public static boolean isLinux() {
        return getOperatingSystem().contains("nix") || getOperatingSystem().contains("nux");
    }

    /**
     * 判断当前操作系统是否为 macOS
     *
     * @return true 如果是 macOS 操作系统，false 否则
     */
    public static boolean isMac() {
        return getOperatingSystem().contains("mac");
    }

    /**
     * 判断当前操作系统是否为 Windows、Linux 或 macOS
     *
     * @return 操作系统的类型
     */
    public static String getOsType() {
        if (isWindows()) {
            return "Windows";
        } else if (isLinux()) {
            return "Linux";
        } else if (isMac()) {
            return "macOS";
        } else {
            return "Unknown";
        }
    }

    public static void main(String[] args) {
        System.out.println("操作系统: " + getOsType());
    }
}

