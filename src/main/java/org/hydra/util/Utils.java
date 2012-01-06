package org.hydra.util;

import java.io.Closeable;
import java.util.StringTokenizer;

public class Utils {
    public static String[] tokens(String line) {
        if (line == null) {
            return new String[0];
        }
        StringTokenizer token = new StringTokenizer(line, " \t");
        String[] result = new String[token.countTokens()];
        int i = 0;
        while (token.hasMoreTokens()) {
            result[i++] = token.nextToken();
        }
        return result;
    }

    public static void close(Closeable os) {
        if (os != null) {
            try {
                os.close();
            } catch (Throwable t) {

            }
        }
    }
}