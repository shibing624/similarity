package org.xm;

/**
 * @author xuming
 */
public class PhraseSimilarityDemo {
    public static void main(String[] args) {
        String phrase1 = "继续努力";
        String phrase2 = "持续发展";
        double result = Similarity.phraseSimilarity(phrase1, phrase2);

        System.out.println(phrase1 + " vs " + phrase2 + " 短语相似度值：" + result);
    }
}
