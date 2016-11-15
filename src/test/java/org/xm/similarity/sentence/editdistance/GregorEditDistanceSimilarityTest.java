package org.xm.similarity.sentence.editdistance;

import org.junit.Test;

/**
 * @author xuming
 */
public class GregorEditDistanceSimilarityTest {
    @Test
    public void getEditDistance() throws Exception {
        String s1 = "abcxdef";
        String s2 = "defxabc";
        s1 = "什么是计算机病毒";
        s2 = "电脑病毒会传染给人吗？";

        GregorEditDistanceSimilarity ed = new GregorEditDistanceSimilarity();
        System.out.println(ed.getEditDistance(SuperString.createCharSuperString(s1),
                SuperString.createCharSuperString(s2)));
    }

}