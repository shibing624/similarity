package org.xm.similarity.sentence.editdistance;

import org.junit.Test;

/**
 * @author xuming
 */
public class SplitTest {
    @Test
    public void split() throws Exception {
        String s1 = "abcdefghijkabc";
        String s2 = "cdefghijklabccc";
//        s2 = "fgabcdehijklkdslfkasdflak";
//        s1 = "abcdefgxyzoxyjasdkfjjjaldsfa";
        //		s1 = "I like the book";
        //		s2 = "the book I like";
        //        s1 = "什么是计算机病毒";
        //        s2 = "电脑病毒是什么";

        SuperString<WordEditUnit> ss1 = SuperString.createWordSuperString(s1);
        SuperString<WordEditUnit> ss2 = SuperString.createWordSuperString(s2);
//        Split.split(ss1, ss2);
//        Split.LCS lcs = Split.LCS.parse(ss1, ss2);
//        System.out.println(lcs);
    }

}