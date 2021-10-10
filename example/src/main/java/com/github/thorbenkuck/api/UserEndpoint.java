package com.github.thorbenkuck.api;

import com.github.thorbenkuck.api.dto.LoginUserDto;
import com.github.thorbenkuck.framework.ResponseEntity;
import com.github.thorbenkuck.usecase.UserService;

public class UserEndpoint {

    private final UserService userService;

    public UserEndpoint(UserService userService) {
        this.userService = userService;
    }

    public ResponseEntity loginUser(LoginUserDto loginUserDto) {
        return loginUserDto.transform((email, password) -> {
            if(userService.canLogin(email, password)) {
                userService.performLogin(email, password);
                return ResponseEntity.ok();
            } else {
                return ResponseEntity.unauthorized();
            }
        });
    }
}
