package com.github.thorbenkuck.domain;

import java.util.Objects;

public class UserEntity {

    private String username;
    private String email;
    private String password;
    private boolean loginFlag;

    protected UserEntity() {

    }

    public UserEntity(String username, String email, String password, boolean loginFlag) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.loginFlag = loginFlag;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public void setLoginFlag(boolean b) {
        loginFlag = b;
    }

    @Override
    public String toString() {
        return "UserEntity{" +
                "username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", loginFlag=" + loginFlag +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserEntity that = (UserEntity) o;
        return loginFlag == that.loginFlag && Objects.equals(username, that.username) && Objects.equals(email, that.email) && Objects.equals(password, that.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username, email, password, loginFlag);
    }
}
