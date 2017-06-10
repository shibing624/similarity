package org.xm.word2vec.vec;

import org.junit.Test;

import java.util.Arrays;

/**
 * @author xuming
 */
public class ModelParserTest {
    private static final String RAW_CORPUS_SPLIT_MODEL = "corpus/tianlongbabu.split.txt.model";

    @Test
    public void loadModel() throws Exception {
        ModelParser parser = new ModelParser();
        parser.loadModel(RAW_CORPUS_SPLIT_MODEL);
        float[] result = parser.getWordVector("内功");
        System.out.println("内功" + "\t" + Arrays.toString(result));
        System.out.println("武学" + "\t" + Arrays.toString(parser.getWordVector("武学")));
        System.out.println("内功 近似词：" + "\t" + parser.distance("内功"));

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