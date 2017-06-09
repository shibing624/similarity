package org.xm.similarity.word.clin;

import org.xm.similarity.ISimilarity;
import org.xm.similarity.util.StringUtil;

import java.util.Set;

/**
 * 词林编码的相似度计算
 *
 * @author xuming
 */
public class CilinSimilarity implements ISimilarity {
    private static CilinSimilarity instance = null;

    public static CilinSimilarity getInstance() {
        if (instance == null) {
            instance = new CilinSimilarity();
        }
        return instance;
    }

    private CilinSimilarity() {
    }

    @Override
    public double getSimilarity(String word1, String word2) {
        if (StringUtil.isBlank(word1) && StringUtil.isBlank(word2)) {
            return 1.0;
        }
        if (StringUtil.isBlank(word1) || StringUtil.isBlank(word2)) {
            return 0.0;
        }
        if (word1.equalsIgnoreCase(word2)) {
            return 1.0;
        }
        double sim = 0.0;
        Set<String> codeSet1 = CilinDictionary.getInstance().getCilinCodes(word1);
        Set<String> codeSet2 = CilinDictionary.getInstance().getCilinCodes(word2);
        if (codeSet1 == null || codeSet2 == null) {
            return 0.0;
        }
        for (String code1 : codeSet1) {
            for (String code2 : codeSet2) {
                double s = getSimilarityByCode(code1, code2);
                //System.out.println(code1 + ":" + code2 + ":" + CilinCode.calculateCommonWeight(code1, code2));
                if (sim < s) sim = s;
            }
        }

        return sim;
    }

    public double getSimilarityByCode(String code1, String code2) {
        return CilinCode.calculateCommonWeight(code1, code2) / CilinCode.TOTAL_WEIGHT;
    }

}
