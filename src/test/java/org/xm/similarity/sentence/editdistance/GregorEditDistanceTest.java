package org.xm.similarity.sentence.editdistance;

import org.junit.Test;

/**
 * @author xuming
 */
public class GregorEditDistanceTest {
    @Test
    public void getEditDistance() throws Exception {
        String s1 = "abcxdef";
        String s2 = "defxabc";
        GregorEditDistance ed = new GregorEditDistance();
        System.out.println(ed.getEditDistance(SuperString.createCharSuperString(s1),
                SuperString.createCharSuperString(s2)));
    }

}