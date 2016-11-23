package org.xm.word2vec;

import org.junit.Before;
import org.junit.Test;

import java.util.List;

/**
 * @author xuming
 */
public class Word2vecTest {
    String outputModelPath = "";

    @Before
    public void before() throws Exception {
        outputModelPath = Word2vec.trainModel("data/seg_result.txt");
        System.out.println("outputModelPath：" + outputModelPath);
    }

    @Test
    public void test() throws Exception {
        List<String> result = Word2vec.getHomoionym(outputModelPath, "贸易", 10);
        System.out.println("贸易 近似词：" + result);
    }

}