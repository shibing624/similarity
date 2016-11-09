package org.xm.similarity.phrase;

import org.junit.Test;

/**
 * @author xuming
 */
public class PhraseSimilarityTest {
    @Test
    public void getSimilarity() throws Exception {
        double score = PhraseSimilarity.getInstance().getSimilarity("继续发展", "努力发展");
        System.out.println(score+"");
    }

}