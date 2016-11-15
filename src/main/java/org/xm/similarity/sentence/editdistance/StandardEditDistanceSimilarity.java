package org.xm.similarity.sentence.editdistance;

/**
 * 基于编辑距离的汉语句子相似度计算
 *
 * @author xuming
 */
public class StandardEditDistanceSimilarity extends EditDistance {

    /**
     * 获取两个串的编辑距离
     */
    @Override
    public double getEditDistance(SuperString<? extends EditUnit> X, SuperString<? extends EditUnit> Y) {
        double[][] D; //编辑矩阵

        int m = X.length(); //字符串X的长度
        int n = Y.length(); //字符串Y的长度
        //char ch_x_i;       //字符串X的第i个词
        //char ch_y_j;       //字符串Y的第j个词

        if (m == 0) {
            double distance = 0.0;
            for (int j = 0; j < n; j++) {
                distance += Y.elementAt(j).getInsertionCost();
            }
            return distance;
        } else if (n == 0) {
            double distance = 0.0;
            for (int i = 0; i < m; i++) {
                distance += X.elementAt(i).getDeletionCost();
            }
            return distance;
        }

        D = new double[n + 1][m + 1];
        D[0][0] = 0.0; //第一个初始化为0

        // 初始化D[0][j]
        for (int j = 1; j <= m; j++) {
            D[0][j] = D[0][j - 1] + X.elementAt(j - 1).getDeletionCost();
        }

        // 初始化D[i][0]/
        for (int i = 1; i <= n; i++) {
            D[i][0] = D[i - 1][0] + Y.elementAt(i - 1).getInsertionCost();
        }

        for (int i = 1; i <= m; i++) {
            EditUnit unit_x_i = X.elementAt(i - 1);
            for (int j = 1; j <= n; j++) {
                EditUnit unit_y_j = Y.elementAt(j - 1);
                double cost = unit_x_i.getSubstitutionCost(unit_y_j);
                D[j][i] = Math.min(D[j - 1][i] + Y.elementAt(j - 1).getInsertionCost(), D[j][i - 1]
                        + X.elementAt(i - 1).getDeletionCost());
                D[j][i] = Math.min(D[j][i], D[j - 1][i - 1] + cost);
            }
        }

        return D[n][m];
    }

}
