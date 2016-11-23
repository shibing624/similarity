package org.xm.word2vec;

import org.junit.Before;
import org.junit.Test;

import java.util.List;

/**
 * @author xuming
 */
public class Word2vecTest {
    String outputPath="";
    @Before
    public void before()throws Exception{
        outputPath = Word2vec.trainModel("data/seg_result.txt");
        System.out.println("outputPath："  +outputPath);
    }
    @Test
    public void test() throws Exception {
        List<String> result = Word2vec.getHomoionym(outputPath, "贸易", 10);
        System.out.println("贸易 近似词："  +result);
    }

}