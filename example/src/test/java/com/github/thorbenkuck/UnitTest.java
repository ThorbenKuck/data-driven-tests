package com.github.thorbenkuck;

import com.github.thorbenkuck.domain.UserEntity;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class UnitTest {

    @Test
    public void test() {
        assertThat(new UserEntity("test", "test", "test", true)).isNotNull();
    }
}
