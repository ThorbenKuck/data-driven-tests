package com.github.thorbenkuck.domain;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class UserRepository {

    private static final Map<String, UserEntity> entities = new HashMap<>();

    public Optional<UserEntity> findByEmail(String id) { return Optional.ofNullable(entities.get(id)); }

    public UserEntity save(UserEntity userEntity) {
        entities.put(userEntity.getEmail(), userEntity);
        return userEntity;
    }

    public void clear() {
        entities.clear();
    }
}
