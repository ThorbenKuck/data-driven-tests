package com.github.thorbenkuck;

import com.github.thorbenkuck.ddt.api.domain.TestCaseFactory;
import com.github.thorbenkuck.ddt.api.domain.builder.TestSuiteBuilder;
import com.github.thorbenkuck.domain.UserEntity;

public class LoginTestCaseFactory implements TestCaseFactory<TestInputWithPrecondition, UserEntity> {

    public static final UserEntity LOGGED_OUT_USER = new UserEntity("Tester1", "test@test.test", "test", false);
    public static final UserEntity LOGGED_IN_USER = new UserEntity("Tester1", "test@test.test", "test", true);

    @Override
    public void setup(TestSuiteBuilder<TestInputWithPrecondition, UserEntity> builder) {
        builder.addEntry("Successful login")
                .forInput(TestInputWithPrecondition.successfulLogin(LOGGED_OUT_USER))
                .expect(LOGGED_IN_USER);

        builder.addEntry("Unsuccessful login")
                .forInput(TestInputWithPrecondition.unsuccessfulLogin(LOGGED_OUT_USER))
                .expect(LOGGED_OUT_USER);
    }

    @Override
    public String suite() {
        return "login";
    }
}
