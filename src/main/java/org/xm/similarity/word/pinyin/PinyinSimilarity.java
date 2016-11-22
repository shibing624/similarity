package org.xm.similarity.word.pinyin;

import org.xm.similarity.ISimilarity;
import org.xm.similarity.util.EditDistance;
import org.xm.similarity.util.MathUtil;

import java.util.Set;

/**
 * 拼音计算两个词相似度，拼音用编辑距离表示相似程度
 *
 * @author xuming
 */
public class PinyinSimilarity implements ISimilarity {
    @Override
    public double getSimilarity(String word1, String word2) {
        double max = 0.0;
        Set<String> pinyinSet1 = PinyinDictionary.getInstance().getPinyin(word1);
        Set<String> pinyinSet2 = PinyinDictionary.getInstance().getPinyin(word2);
        for (String pinyin1 : pinyinSet1) {
            for (String pinyin2 : pinyinSet2) {
                double distance = new EditDistance().getEditDistance(pinyin1, pinyin2);
                double similarity = 1 - distance / (MathUtil.max(pinyin1.length(), pinyin2.length()));
                max = (max > similarity) ? max : similarity;
                if (max == 1.0) {
                    return max;
                }
            }
        }

        return max;
    }
}
