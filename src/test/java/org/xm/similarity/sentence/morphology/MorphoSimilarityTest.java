package org.xm.similarity.sentence.morphology;

import org.junit.Test;

/**
 * @author xuming
 */
public class MorphoSimilarityTest {
    @Test
    public void getSimilarity() throws Exception {
        MorphoSimilarity morphoSimilarity = MorphoSimilarity.getInstance();
        String s1 = "一个伟大的国家有中国";
        String s2 = "中国是一个伟大的国家";

        double sim =  morphoSimilarity.getSimilarity(s1,s2);
        System.out.println(sim+":"+s1+" , "+s2);
    }

}