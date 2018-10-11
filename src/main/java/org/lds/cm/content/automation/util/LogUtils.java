package org.lds.cm.content.automation.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.helpers.Util;

/**
 *
 * @author JiroDan
 */
public class LogUtils {
    public static Logger getLogger() {
        Logger result = LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME);
        Class<?> autoComputedCallingClass = Util.getCallingClass();
        if (autoComputedCallingClass != null) {
            result = LoggerFactory.getLogger(autoComputedCallingClass);
        } else {
            Util.report("Failed to detect logger name from caller.");
        }

        return result;
    }
}
