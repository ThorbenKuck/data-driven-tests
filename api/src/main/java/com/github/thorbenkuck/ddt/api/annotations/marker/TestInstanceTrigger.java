package com.github.thorbenkuck.ddt.api.annotations.marker;

import org.junit.jupiter.api.TestInstance;

public enum TestInstanceTrigger {
    CLASS {
        @Override
        public boolean triggersFor(TestInstance.Lifecycle lifecycle) {
            return lifecycle == TestInstance.Lifecycle.PER_CLASS;
        }
    },
    METHOD {
        @Override
        public boolean triggersFor(TestInstance.Lifecycle lifecycle) {
            return lifecycle == TestInstance.Lifecycle.PER_METHOD;
        }
    };

    public abstract boolean triggersFor(TestInstance.Lifecycle lifecycle);
}