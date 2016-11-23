package org.xm.word2vec.vec;

import org.junit.Test;

import java.util.Arrays;

/**
 * @author xuming
 */
public class ModelParserTest {
    @Test
    public void loadModel() throws Exception {
        ModelParser parser = new ModelParser();
        String path = "data/seg_result_model";
        parser.loadModel(path);
        float[] result = parser.getWordVector("贸易");
        System.out.println("贸易" + "\t" +Arrays.toString(result));
        System.out.println("食品" + "\t" +Arrays.toString(parser.getWordVector("食品")));
        System.out.println("贸易 近似词：" + "\t" +parser.distance("贸易"));

        /*
        List<String> list = new ArrayList<>();
        list.add("工贸");
        list.add("商贸");
        list.add("贸易");
        System.out.println("工贸,商贸,贸易: "+parser.distance(list));
        System.out.println(parser.analogy("工贸", "商贸", "贸易"));
        */
    }

}