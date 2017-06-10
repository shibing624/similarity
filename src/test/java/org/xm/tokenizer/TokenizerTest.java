package org.xm.tokenizer;

import org.junit.Test;

import java.util.List;

/**
 * @author xuming
 */
public class TokenizerTest {
    @Test
    public void getSegmentResult() throws Exception {
        String sentence = "什么是球体和服装？一个伟大的国家，中国，电脑病毒会传染给人吗？";
        List<Word> lists = Tokenizer.segment(sentence);
        System.out.println(lists);
    }

}