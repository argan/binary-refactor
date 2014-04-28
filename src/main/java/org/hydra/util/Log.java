package org.hydra.util;

import java.util.Formatter;

public class Log {
    /**
     * Cache Formatters in ThreadLocal variables for additional performance.
     */
    private static ThreadLocal<Formatter> formatterCache = new FormatterCache();

    private static boolean                debug, error;

    static {
        String logLevel = System.getProperty("Log.Level", "debug");
        debug = "debug".equalsIgnoreCase(logLevel);
        error = debug || "error".equalsIgnoreCase(logLevel);
    }

    public static void debug(String format, Object... args) {
        if (debug) {
            System.out.println("DEBUG " + sprintf(format, args));
        }
    }

    public static void error(String format, Object... args) {
        if (error) {
            System.out.println("ERROR " + sprintf(format, args));
        }
    }

    private static String sprintf(String format, Object... args) {

        Formatter f = getFormatter();
        f.format(format, args);

        StringBuilder sb = (StringBuilder) f.out();
        String message = sb.toString();
        sb.setLength(0);

        return message;

    }

    /**
     * Interface to cached formatters.
     */
    private static Formatter getFormatter() {
        return formatterCache.get();
    }

    /**
     * Cache Formatters for performance reasons.
     */
    static class FormatterCache extends ThreadLocal<Formatter> {

        protected synchronized Formatter initialValue() {
            return new Formatter();
        }

    }

}
