package org.xm;

/**
 * @author xuming
 */
public class WordSimilarityDemo {
    public static void main(String[] args) {
        String word1 = "教师";
        String word2 = "教授";
        double cilinSimilarityResult = Similarity.cilinSimilarity(word1, word2);
        double pinyinSimilarityResult = Similarity.pinyinSimilarity(word1, word2);
        double conceptSimilarityResult = Similarity.conceptSimilarity(word1, word2);
        double charBasedSimilarityResult = Similarity.charBasedSimilarity(word1, word2);

        System.out.println(word1 + " vs " + word2 + " 词林相似度值：" + cilinSimilarityResult);
        System.out.println(word1 + " vs " + word2 + " 拼音相似度值：" + pinyinSimilarityResult);
        System.out.println(word1 + " vs " + word2 + " 概念相似度值：" + conceptSimilarityResult);
        System.out.println(word1 + " vs " + word2 + " 字面相似度值：" + charBasedSimilarityResult);
    }
}
