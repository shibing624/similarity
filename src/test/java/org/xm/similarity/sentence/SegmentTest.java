package org.xm.similarity.sentence;

import org.junit.Test;

/**
 * @author xuming
 */
public class SegmentTest {
    String sentence = "什么是球体和服装？一个伟大的国家，中国";
    String s2 = "中国是一个伟大的国家";

    @Test
    public void getSegmentResult() throws Exception {
        String segment = Segment.getSegmentResult(sentence);
        System.out.println(segment);
    }

}