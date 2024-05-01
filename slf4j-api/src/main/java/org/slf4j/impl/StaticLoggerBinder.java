package org.slf4j.impl;

import org.slf4j.ILoggerFactory;

/**
 * The binding of {@link org.slf4j.LoggerFactory} class with an actual instance of
 * {@link ILoggerFactory} is performed using information returned by this class.
 *
 * @author Ceki G&uuml;lc&uuml;
 */
public class StaticLoggerBinder {

    private static final StaticLoggerBinder SINGLETON = new StaticLoggerBinder();
    public static final StaticLoggerBinder getSingleton() {
        return SINGLETON;
    }

    /**
     * Declare the version of the SLF4J API this implementation is compiled against.
     * The value of this field is modified with each major release.
     */
    // to avoid constant folding by the compiler, this field must *not* be final
    public static String REQUESTED_API_VERSION = "1.6.99"; // !final

    private StaticLoggerBinder() {
        throw new UnsupportedOperationException("This code should have never made it into slf4j-api.jar");
    }

    public ILoggerFactory getLoggerFactory() {
        throw new UnsupportedOperationException("This code should never make it into slf4j-api.jar");
    }

    public String getLoggerFactoryClassStr() {
        throw new UnsupportedOperationException("This code should never make it into slf4j-api.jar");
    }

}
