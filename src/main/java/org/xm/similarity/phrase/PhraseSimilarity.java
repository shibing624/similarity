package org.xm.similarity.phrase;

import org.xm.similarity.ISimilarity;

import java.util.ArrayList;
import java.util.List;

/**
 * 一种简单的短语相似度计算方法
 *
 * @author xuming
 */
public class PhraseSimilarity implements ISimilarity {
    private static PhraseSimilarity instance = null;

    public static PhraseSimilarity getInstance() {
        if (instance == null) {
            instance = new PhraseSimilarity();
        }
        return instance;
    }
    @Override
    public double getSimilarity(String phrase1, String phrase2) {
        return (getSC(phrase1, phrase2) + getSC(phrase2, phrase1)) / 2.0;
    }

    private List<Integer> getC(String first, String second, int pos) {
        List<Integer> results = new ArrayList<Integer>();
        char ch = first.charAt(pos);
        for (int i = 0; i < second.length(); i++) {
            if (ch == second.charAt(i)) {
                results.add(i);
            }
        }
        return results;
    }

    private int getDistance(String first, String second, int pos) {
        int d = second.length();
        for (int k : getC(first, second, pos)) {
            int value = Math.abs(k - pos);
            if (d > value) {
                d = value;
            }
        }
        return d;
    }

    private double getCC(String first, String second, int pos) {
        return (second.length() - getDistance(first, second, pos)) * 1.0 / second.length();
    }

    private double getSC(String first, String second) {
        double total = 0.0;
        for (int i = 0; i < first.length(); i++) {
            total = total + getCC(first, second, i);
        }
        return total / first.length();
    }
}
