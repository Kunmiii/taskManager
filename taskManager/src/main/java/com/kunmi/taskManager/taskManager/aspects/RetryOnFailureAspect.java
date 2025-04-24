package com.kunmi.taskManager.taskManager.aspects;

import com.kunmi.taskManager.taskManager.annotations.RetryOnFailure;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;


@Aspect
@Component
public class RetryOnFailureAspect {

    private static final Logger logger = LoggerFactory.getLogger(RetryOnFailureAspect.class);

    public Object retryMethod(ProceedingJoinPoint joinPoint, RetryOnFailure retryOnFailure) throws Throwable {
        int maxAttempts = retryOnFailure.attempts();
        long delay = retryOnFailure.delay();
        Class<? extends Throwable> [] retryExceptions = retryOnFailure.retryFor();

        int attempt = 1;
        while (attempt <= maxAttempts) {
            try {
                return joinPoint.proceed();
            } catch (Throwable ex) {
                boolean shouldRetry = false;
                for (Class<? extends Throwable> retryEx : retryExceptions) {
                    if (retryEx.isInstance(ex)) {
                        shouldRetry = true;
                        break;
                    }
                }

                if (!shouldRetry || attempt == maxAttempts) {
                    throw ex;
                }

                logger.warn("Attempt {} failed methods {}. Retrying after {} ms...",
                        attempt, joinPoint.getSignature(), delay,  ex);

                Thread.sleep(delay);
                attempt++;
            }
        }

        throw new IllegalStateException("Unexpected state in retry logic");
    }

}
