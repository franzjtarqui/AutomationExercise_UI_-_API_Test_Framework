package com.portfolio.ae.listeners;

import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

/**
 * Retries a failed test once before reporting it as a definitive failure. Mitigation documented
 * in PLAN.md section 10 ("Unstable site/third-party ads... mitigated with robust waits and
 * retries, but some residual flakiness may remain"): with more scenarios running in parallel
 * (Component 14), a transient failure on the real site (a one-off timeout, a Cloudflare 5xx
 * error) shouldn't bring down the entire run.
 * <p>
 * Applied to all {@code @Test} methods automatically via {@link RetryTransformer}, without
 * annotating each method by hand. Each test method gets its own instance (a fresh
 * {@code retryCount}), so it's safe under parallel execution.
 */
public class RetryAnalyzer implements IRetryAnalyzer {

    private static final int MAX_RETRIES = 1;

    private int retryCount = 0;

    @Override
    public boolean retry(ITestResult result) {
        if (retryCount < MAX_RETRIES) {
            retryCount++;
            return true;
        }
        return false;
    }
}
