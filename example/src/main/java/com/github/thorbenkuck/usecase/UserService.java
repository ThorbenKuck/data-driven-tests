package com.github.thorbenkuck.usecase;

import com.github.thorbenkuck.domain.UserRepository;

public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public boolean canLogin(String email, String password) {
        return userRepository.findByEmail(email)
                .map(user -> user.getPassword().equals(password))
                .orElse(false);
    }

    public void performLogin(String email, String password) {
        userRepository.findByEmail(email)
                .ifPresent(user -> {
                    if(user.getPassword().equals(password)) {
                        user.setLoginFlag(true);
                    }
                });
    }

}
