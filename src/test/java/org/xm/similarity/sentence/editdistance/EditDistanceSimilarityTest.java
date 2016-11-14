package org.xm.similarity.sentence.editdistance;

import org.junit.Test;

/**
 * @author xuming
 */
public class EditDistanceSimilarityTest {
    @Test
    public void getEditDistance() throws Exception {
        EditDistance ed = new EditDistanceSimilarity();
        String s1 = "abcxdef";
        String s2 = "def";
        System.out.println(ed.getEditDistance(SuperString.createCharSuperString(s1),
                SuperString.createCharSuperString(s2)));
    }

}