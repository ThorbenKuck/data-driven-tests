package com.github.thorbenkuck.files;

import com.github.thorbenkuck.TestInputWithPrecondition;
import com.github.thorbenkuck.domain.UserEntity;
import com.github.thorbenkuck.domain.UserRepository;
import com.github.thorbenkuck.framework.*;
import com.github.thorbenkuck.ddt.api.annotations.TestScenario;
import com.github.thorbenkuck.ddt.api.annotations.marker.AfterCase;
import com.github.thorbenkuck.ddt.api.annotations.marker.BeforeCase;
import com.github.thorbenkuck.ddt.api.SuiteType;
import com.github.thorbenkuck.ddt.asserters.AssertJEquals;
import org.junit.jupiter.api.*;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@IntegrationTest
public class LoginProcessSuiteIntegrationTest {

    @Inject
    private RestTemplate restTemplate;

    @Inject
    private UserRepository userRepository;

    @BeforeAll
    public static void beforeAll() {
        System.out.println("Before All");
    }

    @BeforeCase
    public void beforeScenario() {
        System.out.println("Before Scenario");
    }

    @BeforeEach
    public void beforeEach() {
        System.out.println("Before Each");
    }

    @AfterEach
    public void afterEach() {
        System.out.println("After Each");
    }

    @AfterCase
    public void afterScenario() {
        System.out.println("After Scenario");
    }

    @AfterAll
    public static void afterAll() {
        System.out.println("After All");
    }

    @TestScenario(
            suite = "login",
            suiteType = SuiteType.CLASSPATH_RESOURCE
    )
    @AssertJEquals
    public UserEntity testLoginProcess(TestInputWithPrecondition testInputWithPrecondition) {
        // Arrange
        testInputWithPrecondition.ifPreconditionPresent(user -> userRepository.save(user));

        // Act
        restTemplate.postToLoginUser(testInputWithPrecondition.getLoginUserDto());
        System.out.println("Test-Case");

        // Assert
        Optional<UserEntity> byEmail = userRepository.findByEmail(testInputWithPrecondition.getLoginUserDto().getEmail());
        assertThat(byEmail).isPresent();
        return byEmail.get();
    }

    @TestScenario(suite = "login")
    @AssertJEquals
    public UserEntity testLoginProcess2(TestInputWithPrecondition testInputWithPrecondition) {
        return testLoginProcess(testInputWithPrecondition);
    }
}
