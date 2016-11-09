package org.xm.similarity.word;

import org.junit.Assert;
import org.junit.Test;


/**
 * @author xuming
 */
public class CharBasedSimilarityTest {
    @Test
    public void getSimilarity() throws Exception {
        CharBasedSimilarity sim = new CharBasedSimilarity();
        String s1 = "手机";
        String s2 = "飞机";
        System.out.println(sim.getSimilarity(s1, s2)+" :"+s1+","+s2);
        Assert.assertTrue(sim.getSimilarity(s1, s2) > 0);

        String s3 = "爱惜";
        String s4 = "喜爱";
        System.out.println(sim.getSimilarity(s3, s4)+" :"+s3+","+s4);

        String s5 = "手机";
        String s6 = "电话";
        System.out.println(sim.getSimilarity(s5, s6)+" :"+s5+","+s6);

    }

}