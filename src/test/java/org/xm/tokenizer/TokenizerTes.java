package org.xm.tokenizer;

import org.junit.Test;

/**
 * @author xuming
 */
public class TokenizerTes {
    @Test
    public void getSegmentResult() throws Exception {
        String sentence = "什么是球体和服装？一个伟大的国家，中国，电脑病毒会传染给人吗？";

        String segment = Tokenizer.getSegmentResult(sentence);
        System.out.println(segment);
    }

}