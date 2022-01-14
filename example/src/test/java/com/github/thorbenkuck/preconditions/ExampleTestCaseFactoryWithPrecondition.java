package com.github.thorbenkuck.preconditions;

import com.github.thorbenkuck.api.dto.LoginUserDto;
import com.github.thorbenkuck.ddt.api.domain.TestCaseFactory;
import com.github.thorbenkuck.ddt.api.domain.builder.TestSuiteBuilder;
import com.github.thorbenkuck.domain.UserEntity;

import java.util.Optional;

public class ExampleTestCaseFactoryWithPrecondition implements TestCaseFactory<LoginUserDto, Optional<UserEntity>> {

    public static final UserEntity LOGGED_OUT_USER = new UserEntity("Tester1", "test@test.test", "test", false);
    public static final UserEntity LOGGED_IN_USER = new UserEntity("Tester1", "test@test.test", "test", true);

    @Override
    public void setup(TestSuiteBuilder<LoginUserDto, Optional<UserEntity>> builder) {
        builder.addEntry("Successful login")
                .withPrecondition(LOGGED_OUT_USER)
                .forInput(LoginUserDto.successfulLogin(LOGGED_OUT_USER))
                .expect(Optional.of(LOGGED_IN_USER));

        builder.addEntry("Unsuccessful login")
                .withPrecondition(LOGGED_OUT_USER)
                .forInput(LoginUserDto.unsuccessfulLogin(LOGGED_OUT_USER))
                .expect(Optional.of(LOGGED_OUT_USER));
    }

    @Override
    public String suite() {
        return "login-with-precondition";
    }
}
