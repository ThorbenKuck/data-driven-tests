package com.github.thorbenkuck.framework;

import com.github.thorbenkuck.api.UserEndpoint;
import com.github.thorbenkuck.domain.UserRepository;
import com.github.thorbenkuck.usecase.UserService;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class ApplicationContext {

    private final Map<Class<?>, Object> beans = new HashMap<>();

    private ApplicationContext() {
        UserRepository userRepository = new UserRepository();
        UserService userService = new UserService(userRepository);
        UserEndpoint userEndpoint = new UserEndpoint(userService);

        setBean(userRepository);
        setBean(userService);
        setBean(userEndpoint);
        setBean(new RestTemplate(userEndpoint));
        setBean(this);
    }

    public void setBean(Object o) {
        beans.put(o.getClass(), o);
    }

    public void injectInto(Object instance) {
        Arrays.stream(instance.getClass().getDeclaredFields())
                .filter(field -> field.isAnnotationPresent(Inject.class))
                .forEach(field -> {
                    field.setAccessible(true);
                    try {
                        field.set(instance, beans.get(field.getType()));
                    } catch (IllegalAccessException e) {
                        throw new IllegalStateException(e);
                    }
                });
    }

    public static ApplicationContext start() {
        return new ApplicationContext();
    }

    public <T> T getBean(Class<T> type) {
        return (T) beans.get(type);
    }
}
