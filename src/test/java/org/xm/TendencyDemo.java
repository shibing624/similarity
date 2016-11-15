package org.xm;

/**
 * @author xuming
 */
public class TendencyDemo {
    public static void main(String[] args) {
        String word = "混蛋";
        double result = Similarity.Tendency(word);
        System.out.println(word + "  词语情感趋势值：" + result);
    }
}
