package org.xm.similarity.sentence.editdistance;

import org.junit.Test;

/**
 * @author xuming
 */
public class NewEditDistanceSimilarityTest {

    String s1 = "什么是计算机病毒";
    String s2 = "我要换货怎么处理";
//    String s1 = "么在";
//    String s2 = "么";

    @Test
    public void getEditDistance() {
        EditDistance ed = new NewEditDistanceSimilarity();
        System.out.println(ed.getEditDistance(SuperString.createWordSuperString(s1),
                SuperString.createWordSuperString(s2)));
        System.out.println(ed.getEditDistance(SuperString.createCharSuperString(s1),
                SuperString.createCharSuperString(s2)));
    }

    @Test
    public void getEditDistanceSimilarity() {
        SuperString<CharEditUnit> ss1 = SuperString.createCharSuperString(s1);
        SuperString<CharEditUnit> ss2 = SuperString.createCharSuperString(s2);
        Split.LCS lcs = Split.LCS.parse(ss1, ss2);
        System.out.println(lcs);
        EditDistance ed = new NewEditDistanceSimilarity();
        System.out.println(ed.getSimilarity(s1, s2));
    }


}