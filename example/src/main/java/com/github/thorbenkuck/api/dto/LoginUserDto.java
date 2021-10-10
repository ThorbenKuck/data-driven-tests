package com.github.thorbenkuck.api.dto;

import java.util.function.BiFunction;

public class LoginUserDto {

    private String email;
    private String password;

    public LoginUserDto() {}

    public LoginUserDto(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public <T> T transform(BiFunction<String, String, T> transformer) {
        return transformer.apply(email, password);
    }
}
