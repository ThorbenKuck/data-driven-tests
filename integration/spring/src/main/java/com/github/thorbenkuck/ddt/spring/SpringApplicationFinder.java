package com.github.thorbenkuck.ddt.spring;

import com.github.thorbenkuck.ddt.collection.OrderedSet;
import io.github.classgraph.ClassGraph;
import io.github.classgraph.ClassInfoList;
import io.github.classgraph.ScanResult;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.util.Assert;

public class SpringApplicationFinder {

    public Class<?> findMainClass(Class<?> testClass) {
        Class<?> mainClass = scanPackage(testClass.getPackage().getName());

        if (mainClass == null) {
            throw new IllegalStateException("Could not find any main class. Test class: " + testClass);
        }

        return mainClass;
    }

    private OrderedSet<Class<?>> scan(String source) {
        try (ScanResult scan = new ClassGraph()
                .enableAnnotationInfo()
                .acceptPackages(source)
                .scan()) {
            ClassInfoList classesWithAnnotation = scan.getClassesWithAnnotation(SpringBootApplication.class);

            return OrderedSet.of(classesWithAnnotation.loadClasses());
        }
    }

    private Class<?> scanPackage(String source) {
        String currentSource = source;
        while (!currentSource.isEmpty()) {
            OrderedSet<Class<?>> components = scan(currentSource);
            if (!components.isEmpty()) {
                Assert.state(components.size() == 1, () -> "Found multiple @SpringBootApplication annotated classes " + components);
                return components.requireFirst();
            }
            currentSource = getParentPackage(currentSource);
        }
        return null;
    }

    private String getParentPackage(String sourcePackage) {
        int lastDot = sourcePackage.lastIndexOf('.');
        return (lastDot != -1) ? sourcePackage.substring(0, lastDot) : "";
    }

}
