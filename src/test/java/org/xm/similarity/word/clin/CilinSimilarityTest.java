package org.xm.similarity.word.clin;

import org.junit.Test;


/**
 * @author xuming
 */
public class CilinSimilarityTest {
    @Test
    public void getSimilarity() throws Exception {
        String text = "词林:" + CilinSimilarity.getInstance().getSimilarity("电脑", "电椅");
        System.out.println("电脑 vs 电椅：" + text);

        String text1 = "词林:" + CilinSimilarity.getInstance().getSimilarity("盲人", "瞎子");
        System.out.println("盲人 vs 瞎子：" + text1);
    }

}