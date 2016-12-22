package org.xm.similarity.text;

import org.junit.Test;

/**
 * @author xuming
 */
public class CosineSimilarityTest {
    @Test
    public void getSimilarityScore() throws Exception {
        String text1 = "我爱购物";
        String text2 = "我爱读书";
        String text3 = "他是黑客";
        TextSimilarity similarity = new CosineSimilarity();
        double score1pk2 = similarity.getSimilarity(text1, text2);
        double score1pk3 = similarity.getSimilarity(text1, text3);
        double score2pk2 = similarity.getSimilarity(text2, text2);
        double score2pk3 = similarity.getSimilarity(text2, text3);
        double score3pk3 = similarity.getSimilarity(text3, text3);
        System.out.println(text1 + " 和 " + text2 + " 的相似度分值：" + score1pk2);
        System.out.println(text1 + " 和 " + text3 + " 的相似度分值：" + score1pk3);
        System.out.println(text2 + " 和 " + text2 + " 的相似度分值：" + score2pk2);
        System.out.println(text2 + " 和 " + text3 + " 的相似度分值：" + score2pk3);
        System.out.println(text3 + " 和 " + text3 + " 的相似度分值：" + score3pk3);

    }

}