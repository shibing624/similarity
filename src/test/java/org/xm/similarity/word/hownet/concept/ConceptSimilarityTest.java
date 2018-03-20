package org.xm.similarity.word.hownet.concept;

import org.junit.Test;

import static junit.framework.TestCase.assertTrue;


/**
 * @author xuming
 */
public class ConceptSimilarityTest {

    @Test
    public void test() throws Exception {
        ConceptSimilarity conceptSimilarity = ConceptSimilarity.getInstance();
        String word3 = "出租车";
        String word4 = "自行车";
        System.out.println(conceptSimilarity.getSimilarity(word3, word4) + " :" + word3 + "," + word4);

        String s1 = "手机";
        String s2 = "电话";
        System.out.println(conceptSimilarity.getSimilarity(s1, s2) + " :" + s1 + "," + s2);

        String word1 = "电动车";
        String word2 = "自行车";
        double sim = conceptSimilarity.getSimilarity(word1, word2);
        System.out.println(sim + " :" + word1 + "," + word2);
        assertTrue(sim > 0.2);
    }

}