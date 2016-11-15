package org.xm.similarity.sentence.editdistance;

/**
 * 由Gregor提出的考虑块交换(Block Transposition)的编辑距离改进算法
 * 时间复杂度为O(m3n3)
 * 具体实现请参考GregorLeusch，Nicola Ueffing的文章《A Novel String-to-String Distance Measure With
 * Application to Machine Translation Evaluation》
 * 问题：<br/>
 * 相似度计算的问题会影响句子相似度计算的直观结果，例如“什么是计算机病毒”，“电脑病毒是什么”
 * 直觉应该是2，即“什么是计算机病毒”首先变为“计算机病毒什么是”，再变为“计算机病毒是什么”，
 * 编辑代价为2，但实际上，当由“什么是计算机病毒”变为“计算机病毒什么是”后，由于"什么是"与“是什么”的替换代价只有0.2，
 * 因而不再进行交互，故总的编辑距离为1.2
 */
public class GregorEditDistanceSimilarity extends EditDistance {

    /**
     * 块交换代价
     */
    public static double swapCost = 0.5;

    private SuperString<? extends EditUnit> S, T;

    /**
     * 存放字符串从S(i0-i1)到T(j0-j1)的中间运算结果，避免多次运算，提高运算效率
     */
    private double[][][][] QArray;

    @Override
    public double getEditDistance(SuperString<? extends EditUnit> S, SuperString<? extends EditUnit> T) {
        this.S = S;
        this.T = T;
        QArray = new double[S.length()][S.length()][T.length()][T.length()];
        for (int i = 0; i < S.length(); i++) {
            for (int i2 = 0; i2 < S.length(); i2++)
                for (int j = 0; j < T.length(); j++)
                    for (int j2 = 0; j2 < T.length(); j2++) {
                        QArray[i][i2][j][j2] = Double.MAX_VALUE;
                    }
        }

        return Q(0, S.length() - 1, 0, T.length() - 1);
    }

    private double Q(int i0, int i1, int j0, int j1) {
        double cost = 0;

        if (i1 < i0) {
            for (int j = j0; j <= j1; j++) {
                cost += T.elementAt(j).getInsertionCost();
            }
            return cost;
        } else if (j1 < j0) {
            for (int i = i0; i <= i1; i++) {
                cost += S.elementAt(i).getDeletionCost();
            }
            return cost;
        } else if (i1 == i0 && j1 == j0) {
            cost = S.elementAt(i0).getSubstitutionCost(T.elementAt(j0));
            QArray[i0][i1][j0][j1] = cost;
            return cost;
        } else if (i1 == i0) {
            double minSubstituteValue = 1.0;
            int minPosJ = j0;
            for (int j = j0; j <= j1; j++) {
                double subsitituteValue = S.elementAt(i0).getSubstitutionCost(T.elementAt(j));
                if (minSubstituteValue > subsitituteValue) {
                    minSubstituteValue = subsitituteValue;
                    minPosJ = j;
                }
            }
            for (int j = j0; j <= j1; j++) {
                if (j == minPosJ) {
                    cost += minSubstituteValue;
                } else {
                    cost += T.elementAt(j).getInsertionCost();
                }
            }
        } else if (j1 == j0) {
            double minSubstituteValue = 1.0;
            int minPosI = i0;
            for (int i = i0; i <= i1; i++) {
                double subsitituteValue = S.elementAt(i).getSubstitutionCost(T.elementAt(j0));
                if (minSubstituteValue > subsitituteValue) {
                    minSubstituteValue = subsitituteValue;
                    minPosI = i;
                }
            }
            for (int i = i0; i <= i1; i++) {
                if (i == minPosI) {
                    cost += minSubstituteValue;
                } else {
                    cost += S.elementAt(i).getDeletionCost();
                }
            }
        } else {
            if (QArray[i0][i1][j0][j1] < Double.MAX_VALUE) {
                return QArray[i0][i1][j0][j1];
            }
            for (int i = i0; i < i1; i++) {
                for (int j = j0; j < j1; j++) {
                    double c = Math.min(Q(i0, i, j0, j) + Q(i + 1, i1, j + 1, j1),
                            Q(i0, i, j + 1, j1) + Q(i + 1, i1, j0, j) + swapCost);
                    if (c < QArray[i0][i1][j0][j1]) {
                        QArray[i0][i1][j0][j1] = c;
                    }
                }
            }
            return QArray[i0][i1][j0][j1];
        }
        QArray[i0][i1][j0][j1] = cost;
        return cost;
    }

}
