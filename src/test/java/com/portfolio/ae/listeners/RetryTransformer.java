package com.portfolio.ae.listeners;

import org.testng.IAnnotationTransformer;
import org.testng.annotations.ITestAnnotation;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

/**
 * Assigns {@link RetryAnalyzer} to all {@code @Test} methods in the project (Cucumber runners
 * and unit/integration tests) without having to annotate each method individually. Registered as
 * a listener in {@code testng*.xml}.
 */
public class RetryTransformer implements IAnnotationTransformer {

    @Override
    public void transform(ITestAnnotation annotation, Class testClass, Constructor testConstructor, Method testMethod) {
        annotation.setRetryAnalyzer(RetryAnalyzer.class);
    }
}
