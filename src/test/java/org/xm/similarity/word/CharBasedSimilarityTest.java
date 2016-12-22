package org.xm.similarity.word;

import org.junit.Assert;
import org.junit.Test;


/**
 * @author xuming
 */
public class CharBasedSimilarityTest {
    @Test
    public void getSimilarity() throws Exception {
        CharBasedSimilarity sim = CharBasedSimilarity.getInstance();
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

        String word1 = "经贸";
        String word2 = "商贸";
        double wordpk = sim.getSimilarity(word1, word2);
        System.out.println(word1 + " 和 " + word2 + " 的字面量相似度分值：" + wordpk);
    }

}