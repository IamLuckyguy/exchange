package kr.co.kwt.exchange.utils;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.event.Level;

import java.util.function.Supplier;

@Slf4j
@UtilityClass
public class LogUtils {

    public static void logApiRequest(String serviceName, String methodName, Object... params) {
        log.info("[{}] {} request - parameters: {}", serviceName, methodName, params);
    }

    public static void logApiResponse(String serviceName, String methodName, Object response) {
        log.debug("[{}] {} response: {}", serviceName, methodName, response);
    }

    public static void logSuccessOperation(String serviceName, String methodName, String message, Object... params) {
        log.info("[{}] {} success - {}", serviceName, methodName, String.format(message, params));
    }

    public static void logError(String serviceName, String methodName, Throwable error) {
        log.error("[{}] {} failed - error: {}", serviceName, methodName, error.getMessage(), error);
    }

    public static void logError(String serviceName, String methodName, String message, Object... params) {
        log.error("[{}] {} failed - {}", serviceName, methodName, String.format(message, params));
    }

    public static <T> T logAndRethrow(String serviceName, String methodName, Throwable error, Supplier<T> exceptionSupplier) {
        logError(serviceName, methodName, error);
        throw new RuntimeException(error);
    }

    public static void logDebug(String serviceName, String methodName, String message, Object... params) {
        if (log.isDebugEnabled()) {
            log.debug("[{}] {} - {}", serviceName, methodName, String.format(message, params));
        }
    }

    public static void logBusinessOperation(String serviceName, String operation, String target, String result) {
        log.info("[{}] {} {} - result: {}", serviceName, operation, target, result);
    }

    public static void logPerformance(String serviceName, String methodName, long startTime) {
        long executionTime = System.currentTimeMillis() - startTime;
        log.info("[{}] {} executed in {} ms", serviceName, methodName, executionTime);
    }
}