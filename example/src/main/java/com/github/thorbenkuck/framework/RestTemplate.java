package com.github.thorbenkuck.framework;

import com.github.thorbenkuck.api.UserEndpoint;
import com.github.thorbenkuck.api.dto.LoginUserDto;

public class RestTemplate {

    private final UserEndpoint userEndpoint;

    public RestTemplate(UserEndpoint userEndpoint) {
        this.userEndpoint = userEndpoint;
    }

    public ResponseEntity postToLoginUser(LoginUserDto loginUserDto) {
        return userEndpoint.loginUser(loginUserDto);
    }
}
