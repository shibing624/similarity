package org.xm.word2vec.vec;

import org.junit.Assert;
import org.junit.Test;

import java.io.File;

/**
 * @author xuming
 */
public class LearnTest {
    @Test
    public void learnFile() throws Exception {
        Learn learn = new Learn();
        long start = System.currentTimeMillis();
        learn.learnFile(new File("data/seg_result.txt"));
        System.out.println("use time " + (System.currentTimeMillis() - start));
        learn.saveModel(new File("data/seg_result_model"));
        Assert.assertTrue(learn != null);
    }

}