package com.github.thorbenkuck.factory;

import com.github.thorbenkuck.LoginTestCaseFactory;
import com.github.thorbenkuck.TestInputWithPrecondition;
import com.github.thorbenkuck.ddt.api.annotations.importer.TestCaseFactories;
import com.github.thorbenkuck.domain.UserEntity;
import com.github.thorbenkuck.domain.UserRepository;
import com.github.thorbenkuck.framework.Inject;
import com.github.thorbenkuck.framework.IntegrationTest;
import com.github.thorbenkuck.framework.RestTemplate;
import com.github.thorbenkuck.ddt.api.annotations.TestScenario;
import com.github.thorbenkuck.ddt.api.SuiteType;
import com.github.thorbenkuck.ddt.asserters.AssertJEquals;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@IntegrationTest
public class LoginProcessFactoryIntegrationTest {

    @Inject
    private RestTemplate restTemplate;

    @Inject
    private UserRepository userRepository;

    @TestScenario(suiteType = SuiteType.FACTORY)
    @AssertJEquals
    @TestCaseFactories(LoginTestCaseFactory.class)
    public UserEntity testLoginProcessWithFactory(TestInputWithPrecondition testInputWithPrecondition) {
        // Arrange
        testInputWithPrecondition.ifPreconditionPresent(user -> userRepository.save(user));

        // Act
        restTemplate.postToLoginUser(testInputWithPrecondition.getLoginUserDto());

        // Assert
        Optional<UserEntity> byEmail = userRepository.findByEmail(testInputWithPrecondition.getLoginUserDto().getEmail());
        assertThat(byEmail).isPresent();
        return byEmail.get();
    }
}
