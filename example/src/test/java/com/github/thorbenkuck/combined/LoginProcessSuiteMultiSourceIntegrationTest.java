package com.github.thorbenkuck.combined;

import com.github.thorbenkuck.TestInputWithPrecondition;
import com.github.thorbenkuck.domain.UserEntity;
import com.github.thorbenkuck.domain.UserRepository;
import com.github.thorbenkuck.framework.Inject;
import com.github.thorbenkuck.framework.IntegrationTest;
import com.github.thorbenkuck.framework.RestTemplate;
import com.github.thorbenkuck.ddt.api.annotations.TestScenario;
import com.github.thorbenkuck.ddt.asserters.AssertJEquals;
import com.github.thorbenkuck.ddt.asserters.AssertJSame;
import com.github.thorbenkuck.ddt.asserters.json.DeepJsonEquals;
import com.github.thorbenkuck.ddt.asserters.json.IgnoreField;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@IntegrationTest
public class LoginProcessSuiteMultiSourceIntegrationTest {

    @Inject
    private RestTemplate restTemplate;

    @Inject
    private UserRepository userRepository;

    @TestScenario(suite = "login")
    @AssertJEquals
    @DeepJsonEquals
    public UserEntity testLoginProcess(TestInputWithPrecondition testInputWithPrecondition) {
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
