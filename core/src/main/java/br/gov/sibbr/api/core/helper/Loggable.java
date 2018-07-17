package br.gov.sibbr.api.core.helper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Capture logger and trataments messages
 */
public interface Loggable {

    default Logger logger() {
        return LoggerFactory.getLogger(this.getClass().getName());
    }

    default void loggerError(Throwable exception) {
        logger().error("[" + exception.getClass().getSimpleName() + "] : " + exception.getMessage());
    }
}
