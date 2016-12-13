package org.xm;

import org.xm.tendency.word.HownetWordTendency;

/**
 * @author xuming
 */
public class TendencyDemo {
    public static void main(String[] args) {
        String word = "混蛋";
        HownetWordTendency hownetWordTendency = new HownetWordTendency();
        double result = hownetWordTendency.getTendency(word);
        System.out.println(word + "  词语情感趋势值：" + result);
    }
}
