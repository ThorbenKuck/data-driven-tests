package com.github.thorbenkuck.preconditions;

import com.github.thorbenkuck.LoginTestScenario;
import com.github.thorbenkuck.api.dto.LoginUserDto;
import com.github.thorbenkuck.ddt.api.SuiteType;
import com.github.thorbenkuck.ddt.api.annotations.marker.AfterCase;
import com.github.thorbenkuck.ddt.api.annotations.marker.ApplyPreconditions;
import com.github.thorbenkuck.ddt.api.annotations.TestScenario;
import com.github.thorbenkuck.ddt.asserters.AssertJEquals;
import com.github.thorbenkuck.domain.UserEntity;
import com.github.thorbenkuck.domain.UserRepository;
import com.github.thorbenkuck.framework.Inject;
import com.github.thorbenkuck.framework.IntegrationTest;
import com.github.thorbenkuck.framework.RestTemplate;

import java.util.Optional;

@IntegrationTest
public class LoginProcessFactoryWithPreconditionIntegrationTest {

    @Inject
    private RestTemplate restTemplate;

    @Inject
    private UserRepository userRepository;

    @ApplyPreconditions
    public void handlePrecondition(UserEntity user) {
        System.out.println("Creating test data");
        userRepository.save(user);
    }

    @LoginTestScenario
    public Optional<UserEntity> testLoginProcessWithFactory(LoginUserDto loginUserDto) {
        System.out.println("Running Test");
        // Act
        restTemplate.postToLoginUser(loginUserDto);
        return userRepository.findByEmail(loginUserDto.getEmail());
    }
}
