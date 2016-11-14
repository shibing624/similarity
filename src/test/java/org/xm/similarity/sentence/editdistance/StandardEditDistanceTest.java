package org.xm.similarity.sentence.editdistance;

import org.junit.Test;

/**
 * @author xuming
 */
public class StandardEditDistanceTest {
    @Test
    public void getEditDistance() throws Exception {
        String s1 = "abcdefg";
        String s2 = "gcdefab";

        StandardEditDistance ed = new StandardEditDistance();
        s1 = "什么是计算机病毒";
        s2 = "什么是电脑病毒源";
        System.out.println(ed.getEditDistance(SuperString.createCharSuperString(s1),
                SuperString.createCharSuperString(s2)));
        System.out.println(ed.getEditDistance(SuperString.createWordSuperString(s1),
                SuperString.createWordSuperString(s2)));

    }

}