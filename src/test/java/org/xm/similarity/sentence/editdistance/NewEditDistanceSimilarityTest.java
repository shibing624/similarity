package org.xm.similarity.sentence.editdistance;

import org.junit.Test;

/**
 * @author xuming
 */
public class NewEditDistanceSimilarityTest {
    @Test
    public void getEditDistance() throws Exception {
        EditDistance ed = new NewEditDistanceSimilarity();
        String s1 = "abcxdef";
        String s2 = "def";
        s1 = "什么是计算机病毒";
        s2 = "电脑病毒会传染给人";
        System.out.println(ed.getEditDistance(SuperString.createCharSuperString(s1),
                SuperString.createCharSuperString(s2)));
    }

}