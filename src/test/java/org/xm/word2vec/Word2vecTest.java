package org.xm.word2vec;

import org.junit.Test;

import java.util.List;

/**
 * @author xuming
 */
public class Word2vecTest {

    @Test
    public void trainModel() throws Exception {
        String outputModelPath = Word2vec.trainModel("data/jinyong_novels_split.txt");
        System.out.println("outputModelPath：" + outputModelPath);
    }

    @Test
    public void testHomoionym() throws Exception {
        List<String> result = Word2vec.getHomoionym("data/jinyong_novels_split.txt.model", "道路", 10);
        System.out.println("道路 近似词：" + result);
    }

    @Test
    public void testHomoionymName() throws Exception {
        String model = "data/jinyong_novels_split.txt.model";
        List<String> result = Word2vec.getHomoionym(model, "乔峰", 10);
        System.out.println("乔峰 近似词：" + result);

        List<String> result2 = Word2vec.getHomoionym(model, "阿朱", 10);
        System.out.println("阿朱 近似词：" + result2);

        List<String> result3 = Word2vec.getHomoionym(model, "降龙十八掌", 10);
        System.out.println("降龙十八掌 近似词：" + result3);
    }

}