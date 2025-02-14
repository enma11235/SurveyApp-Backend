package com.quickpolls.dto.request;

import jakarta.validation.constraints.*;

public class AuthRequestBody {

    @NotBlank(message = "Nickname is required")
    private String nickname;

    @NotBlank(message = "Password is required")
    @Size(min = 8, message = "Password must be at least 8 characters long")
    private String password;

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
