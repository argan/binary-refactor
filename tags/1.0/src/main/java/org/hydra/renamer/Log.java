package org.hydra.renamer;

import java.util.Formatter;

public class Log {
	/**
	 * Cache Formatters in ThreadLocal variables for additional performance.
	 */
	private static ThreadLocal<Formatter> formatterCache = new FormatterCache();

	public static void debug(String format, Object... args) {
		System.out.println("DEBUG " + sprintf(format, args));
	}

    public static void error(String format, Object... args) {
        System.out.println("ERROR " + sprintf(format, args));
    }

	public static String sprintf(String format, Object... args) {

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
