package com.github.thorbenkuck.combined;

import com.github.thorbenkuck.LoginTestCaseFactory;
import com.github.thorbenkuck.TestInputWithPrecondition;
import com.github.thorbenkuck.ddt.api.annotations.importer.TestContentFactory;
import com.github.thorbenkuck.ddt.api.annotations.marker.ConfigureTestData;
import com.github.thorbenkuck.ddt.api.domain.builder.TestSuiteBuilder;
import com.github.thorbenkuck.domain.UserEntity;
import com.github.thorbenkuck.domain.UserRepository;
import com.github.thorbenkuck.framework.Inject;
import com.github.thorbenkuck.framework.IntegrationTest;
import com.github.thorbenkuck.framework.RestTemplate;
import com.github.thorbenkuck.ddt.api.annotations.TestScenario;
import com.github.thorbenkuck.ddt.asserters.AssertJEquals;
import com.github.thorbenkuck.ddt.asserters.json.DeepJsonEquals;

import java.util.Optional;

import static com.github.thorbenkuck.LoginTestCaseFactory.LOGGED_IN_USER;
import static com.github.thorbenkuck.LoginTestCaseFactory.LOGGED_OUT_USER;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@IntegrationTest
public class LoginProcessSuiteMultiSourceIntegrationTest {

    @Inject
    private RestTemplate restTemplate;

    @Inject
    private UserRepository userRepository;

    @ConfigureTestData
    public static void configureTestData(TestSuiteBuilder<TestInputWithPrecondition, UserEntity> builder) {
        builder.addEntry("From the test-class itself!")
                .forInput(TestInputWithPrecondition.successfulLogin(LOGGED_OUT_USER))
                .expect(LOGGED_IN_USER);
    }

    @TestScenario(suite = "login")
    @AssertJEquals
    @DeepJsonEquals
    @TestContentFactory(LoginTestCaseFactory.class)
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
