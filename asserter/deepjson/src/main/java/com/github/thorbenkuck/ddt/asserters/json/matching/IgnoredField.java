package com.github.thorbenkuck.ddt.asserters.json.matching;

public class IgnoredField {

    private final String name;
    private final String reason;
    private final boolean temporary;

    public IgnoredField(String name, String reason, boolean temporary) {
        this.name = name;
        this.reason = reason;
        this.temporary = temporary;
    }

    public IgnoredField(String name, String reason) {
        this(name, reason, false);
    }

    public IgnoredField(String name) {
        this(name, null, false);
    }

    public String getName() {
        return name;
    }

    public String getReason() {
        return reason;
    }

    public boolean isTemporary() {
        return temporary;
    }
}
