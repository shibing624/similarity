package org.xm.similarity.sentence.morphology;

import org.junit.Test;

/**
 * @author xuming
 */
public class MorphoSimilarityTest {
    MorphoSimilarity morphoSimilarity = MorphoSimilarity.getInstance();
    @Test
    public void getSimilarity() throws Exception {
        String s1 = "一个伟大的国家有中国";
        String s2 = "中国是一个伟大的国家";

        double sim = morphoSimilarity.getSimilarity(s1, s2);
        System.out.println(sim + ":" + s1 + " , " + s2);
    }

    @Test
    public void test1() throws Exception {
        String s1 = "构建突发事件中的应急预案";
        String s2 = "面对突发事件应挺身而出";

        double sim = morphoSimilarity.getSimilarity(s1, s2);
        System.out.println(sim + ":" + s1 + " , " + s2);
    }

    @Test
    public void test2() throws Exception {
        String s1 = "祝贺两会胜利闭幕。。。！";
        String s2 = "撸起袖子好好干";

        double sim = morphoSimilarity.getSimilarity(s1, s2);
        System.out.println(sim + ":" + s1 + " , " + s2);
    }
}