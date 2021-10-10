package com.github.thorbenkuck;

import com.github.thorbenkuck.domain.UserEntity;
import com.github.thorbenkuck.ddt.api.domain.factory.TestCaseEntry;
import com.github.thorbenkuck.ddt.api.domain.TestContentFactory;

import java.util.List;

public class ExampleTestContentFactory implements TestContentFactory<TestInputWithPrecondition, UserEntity> {

    private final UserEntity loggedOutUser = new UserEntity("Tester1", "test@test.test", "test", false);
    private final UserEntity loggedInUser = new UserEntity("Tester1", "test@test.test", "test", true);

    @Override
    public List<TestCaseEntry<TestInputWithPrecondition, UserEntity>> produce() {
        return builder()
                .append(
                        entry("Successful login")
                                .forInput(TestInputWithPrecondition.successfulLogin(loggedOutUser))
                                .expects(loggedInUser)
                )
                .append(
                        entry("Unsuccessful login")
                                .forInput(TestInputWithPrecondition.unsuccessfulLogin(loggedOutUser))
                                .expects(loggedOutUser)
                )
                .build();
    }

    @Override
    public String suite() {
        return "login";
    }
}
