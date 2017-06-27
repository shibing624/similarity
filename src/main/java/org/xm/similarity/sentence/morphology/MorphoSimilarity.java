package org.xm.similarity.sentence.morphology;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xm.similarity.sentence.ISentenceSimilarity;
import org.xm.similarity.word.IWordSimilarity;
import org.xm.similarity.word.hownet.concept.ConceptSimilarity;
import org.xm.tokenizer.Tokenizer;
import org.xm.tokenizer.Word;

import java.util.ArrayList;
import java.util.List;

/**
 * 基于词形和词序的句子相似度计算算法，考虑了语义因素
 *
 * @author xuming
 */
public class MorphoSimilarity implements ISentenceSimilarity {
    private static Logger logger = LoggerFactory.getLogger(MorphoSimilarity.class);

    // 词形相似度占总相似度比重
    private final double LAMBDA1 = 1.0;
    // 词序相似度占比
    private final double LAMBDA2 = 0.0;
    private IWordSimilarity wordSimilarity;
    private static final String FILTER_CHARS = " 　，。；？《》()｜！,.;?<>|_^…!";
    private static MorphoSimilarity instance;

    public static MorphoSimilarity getInstance() {
        if (instance == null) {
            instance = new MorphoSimilarity();
        }
        return instance;
    }

    private MorphoSimilarity() {
        this.wordSimilarity = ConceptSimilarity.getInstance();
        logger.debug("used hownet word similarity.");
    }

    private String[] filter(String[] words) {
        List<String> results = new ArrayList<>();
        for (String s : words) {
            if (!FILTER_CHARS.contains(s)) {
                results.add(s.toLowerCase());
            }
        }

        return results.toArray(new String[results.size()]);
    }

    @Override
    public double getSimilarity(String sentence1, String sentence2) {
        String[] list1 = filter(segment(sentence1));
        String[] list2 = filter(segment(sentence2));
        double wordSimilarity = getOccurrenceSimilarity(list1, list2);
        double orderSimilarity = getOrderSimilarity(list1, list2);
        return LAMBDA1 * wordSimilarity + LAMBDA2 * orderSimilarity;
    }

    /**
     * 获取两个集合的词形相似度, 同时获取相对于第一个句子中的词语顺序，第二个句子词语的顺序变化次数
     *
     * @param list1
     * @param list2
     * @return
     */
    private double getOccurrenceSimilarity(String[] list1, String[] list2) {
        int max = list1.length > list2.length ? list1.length : list2.length;
        if (max == 0) {
            return 0;
        }

        //首先计算出所有可能的组合
        double[][] scores = new double[max][max];
        for (int i = 0; i < list1.length; i++) {
            for (int j = 0; j < list2.length; j++) {
                scores[i][j] = wordSimilarity.getSimilarity(list1[i], list2[j]);
            }
        }

        double total_score = 0;

        //从scores[][]中挑选出最大的一个相似度，然后减去该元素，进一步求剩余元素中的最大相似度
        while (scores.length > 0) {
            double max_score = 0;
            int max_row = 0;
            int max_col = 0;

            //先挑出相似度最大的一对：<row, column, max_score>
            for (int i = 0; i < scores.length; i++) {
                for (int j = 0; j < scores.length; j++) {
                    if (max_score < scores[i][j]) {
                        max_row = i;
                        max_col = j;
                        max_score = scores[i][j];
                    }
                }
            }

            //从数组中去除最大的相似度，继续挑选
            double[][] tmp_scores = new double[scores.length - 1][scores.length - 1];
            for (int i = 0; i < scores.length; i++) {
                if (i == max_row)
                    continue;
                for (int j = 0; j < scores.length; j++) {
                    if (j == max_col)
                        continue;
                    int tmp_i = max_row > i ? i : i - 1;
                    int tmp_j = max_col > j ? j : j - 1;
                    tmp_scores[tmp_i][tmp_j] = scores[i][j];
                }
            }
            total_score += max_score;
            scores = tmp_scores;
        }

        return (2 * total_score) / (list1.length + list2.length);
    }

    /**
     * 获取两个集合的词序相似度
     */
    private double getOrderSimilarity(String[] list1, String[] list2) {
        double similarity = 0.0;
        return similarity;
    }

    public String[] segment(String sentence) {
        List<Word> list = Tokenizer.segment(sentence);
        String[] results = new String[list.size()];
        for (int i = 0; i < list.size(); i++) {
            results[i] = list.get(i).getName();
        }
        return results;
    }
}
