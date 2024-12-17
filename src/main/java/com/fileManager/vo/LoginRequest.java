package com.fileManager.vo;

public class LoginRequest {
    private String username;
    private String password;
    private boolean isSafe = true;  // 默认为 true
    public LoginRequest() {
    }

    public LoginRequest(String username, String password,boolean isSafe) {
        this.username = username;
        this.password = password;
        this.isSafe = isSafe;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    public boolean getIsSafe() {
        return isSafe;
    }

    public void setSafe(boolean isSafe) {
        this.isSafe = isSafe;
    }
}
