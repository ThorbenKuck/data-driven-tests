package com.github.thorbenkuck.domain;

import com.github.thorbenkuck.framework.DAO;

import java.util.*;

public class UserRepository {

    private Map<String, UserEntity> getTable() {
        return DAO.getOrCreateTable(UserEntity.class);
    }

    public Optional<UserEntity> findByEmail(String id) { return Optional.ofNullable(getTable().get(id)); }

    public UserEntity save(UserEntity userEntity) {
        getTable().put(userEntity.getEmail(), userEntity);
        return userEntity;
    }

    public List<UserEntity> findAll() {
        return new ArrayList<>(getTable().values());
    }

    public void clear() {
        getTable().clear();
    }
}
