package org.xm.similarity.word.pinyin;


import org.junit.Test;

/**
 * @author xuming
 */
public class PinyinSimilarityTest {
    @Test
    public void getSimilarity() throws Exception {
        PinyinSimilarity pinyinSimilarity = new PinyinSimilarity();
        double result = pinyinSimilarity.getSimilarity("教授", "教师");
        System.out.println("教授" + " 教师 " + ":" + result);

    }

}