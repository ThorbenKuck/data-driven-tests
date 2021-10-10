package com.github.thorbenkuck;

import com.github.thorbenkuck.api.dto.LoginUserDto;
import com.github.thorbenkuck.domain.UserEntity;

import java.util.function.Consumer;

public class TestInputWithPrecondition {
    private UserEntity precondition;
    private LoginUserDto loginUserDto;

    public TestInputWithPrecondition() {
        // Jackson
    }

    public TestInputWithPrecondition(LoginUserDto loginUserDto, UserEntity precondition) {
        this.precondition = precondition;
        this.loginUserDto = loginUserDto;
    }

    public static TestInputWithPrecondition successfulLogin(UserEntity precondition) {
        return new TestInputWithPrecondition(new LoginUserDto(precondition.getEmail(), precondition.getPassword()), precondition);
    }

    public static TestInputWithPrecondition unsuccessfulLogin(UserEntity precondition) {
        return new TestInputWithPrecondition(new LoginUserDto(precondition.getEmail(), precondition.getPassword() + "_WRONG"), precondition);
    }

    public LoginUserDto getLoginUserDto() {
        return loginUserDto;
    }

    public void setLoginUserDto(LoginUserDto loginUserDto) {
        this.loginUserDto = loginUserDto;
    }

    public UserEntity getPrecondition() {
        return precondition;
    }

    public void setPrecondition(UserEntity precondition) {
        this.precondition = precondition;
    }

    public void ifPreconditionPresent(Consumer<UserEntity> consumer) {
        if (precondition != null) {
            consumer.accept(precondition);
        }
    }
}
