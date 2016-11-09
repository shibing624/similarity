package org.xm.similarity.util;

public class MathUtil {

    public static int min(int... values) {
        int min = Integer.MAX_VALUE;
        for (int v : values) {
            min = (v < min) ? v : min;
        }
        return min;
    }

    public static int max(int a, int b) {
        return a > b ? a : b;
    }

}
