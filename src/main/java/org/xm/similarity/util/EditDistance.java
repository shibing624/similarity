package org.xm.similarity.util;

/**
 * This class computes the edit distance between two strings using dynamic
 * programming. The dynamic programming part is in the method
 * printEditDistance().
 */
public class EditDistance {

    /**
     * 获取删除代价
     *
     * @return
     */
    public int getDeletionCost() {
        return 1;
    }

    /**
     * 获取插入代价
     *
     * @return
     */
    public int getInsertionCost() {
        return 1;
    }

    /**
     * 获取替换代价
     *
     * @return
     */
    public int getSubstitutionCost(char a, char b) {
        return (a == b) ? 0 : 1;
    }

    public int getEditDistance(String S, String T) {
        int[][] D = null;
        if (S == null)
            S = "";
        if (T == null)
            T = "";

        char[] a = S.toCharArray();
        char[] b = T.toCharArray();

        int n = a.length; // 字符串S的长度
        int m = b.length; // 字符串T的长度

        if (a.length == 0) {
            return b.length;
        } else if (b.length == 0) {
            return a.length;
        }

        D = new int[a.length + 1][b.length + 1];

        /** 初始化D[i][0] */
        for (int i = 1; i <= n; i++) {
            D[i][0] = D[i - 1][0] + getDeletionCost();
        }

        /** 初始化D[0][j] */
        for (int j = 1; j <= m; j++) {
            D[0][j] = D[0][j - 1] + getInsertionCost();
        }

        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= m; j++) {
                D[i][j] = MathUtil.min(D[i - 1][j] + getDeletionCost(), D[i][j - 1] + getInsertionCost(),
                        D[i - 1][j - 1] + getSubstitutionCost(a[i - 1], b[j - 1]));
            }
        }

        return D[n][m];
    }

    /**
     * 应与getEditDistance(S, T)等同
     *
     * @param s
     * @param t
     * @return
     */
    public static int getLevenshteinDistance(String s, String t) {
        if (s == null || t == null) {
            throw new IllegalArgumentException("Strings must not be null");
        }
        int d[][]; // matrix
        int n; // length of s
        int m; // length of t
        int i; // iterates through s
        int j; // iterates through t
        char s_i; // ith character of s
        char t_j; // jth character of t
        int cost; // cost

        // Step 1
        n = s.length();
        m = t.length();
        if (n == 0) {
            return m;
        }
        if (m == 0) {
            return n;
        }
        d = new int[n + 1][m + 1];

        // Step 2
        for (i = 0; i <= n; i++) {
            d[i][0] = i;
        }
        for (j = 0; j <= m; j++) {
            d[0][j] = j;
        }

        // Step 3
        for (i = 1; i <= n; i++) {
            s_i = s.charAt(i - 1);

            // Step 4
            for (j = 1; j <= m; j++) {
                t_j = t.charAt(j - 1);

                // Step 5
                if (s_i == t_j) {
                    cost = 0;
                } else {
                    cost = 1;
                }

                // Step 6
                d[i][j] = MathUtil.min(d[i - 1][j] + 1, d[i][j - 1] + 1, d[i - 1][j - 1] + cost);
            }
        }

        // Step 7
        return d[n][m];
    }

}
