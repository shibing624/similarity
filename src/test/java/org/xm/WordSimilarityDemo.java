package org.xm;

import java.util.ArrayList;

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

        getWords240Similarity();
    }


    public static void getWords240Similarity() {
        // 取Word2-240测试集前10条和后10条，具体测试集数据在data/test下
        ArrayList<String> expecteds = new ArrayList<>();
        expecteds.add("李白\t诗\t9.2");
        expecteds.add("日本\t南京大屠杀\t8.95");
        expecteds.add("浏览器\t网页\t8.93");
        expecteds.add("浏览器\t网页\t8.93");
        expecteds.add("医生\t责任\t8.85");
        expecteds.add("白天\t晚上\t8.8");
        expecteds.add("基因\t遗传\t8.775");
        expecteds.add("学校\t学生\t8.71");
        expecteds.add("法官\t法律\t8.65");
        expecteds.add("员工\t公司\t8.65");
        expecteds.add("股票\t光盘\t1.05");
        expecteds.add("皇帝\t白菜\t1.05");
        expecteds.add("周三\t新闻\t1");
        expecteds.add("能源\t秘书\t0.85");
        expecteds.add("七\t系列\t0.6");
        expecteds.add("中午\t字符串\t0.6");
        expecteds.add("收音机\t工作\t0.6");
        expecteds.add("教授\t黄瓜\t0.5");
        expecteds.add("自行车\t鸟\t0.5");
        expecteds.add("蛋白质\t文物\t0.15");
        for (int i = 0; i < expecteds.size(); i++) {
            String[] parts = expecteds.get(i).split("\t");
            String word1 = parts[0];
            String word2 = parts[1];
            // 0-10 转化为 0-1 的评分标准
            double expectScore = Double.valueOf(parts[2]) / 10;
            double cilinPredScore = Similarity.cilinSimilarity(word1, word2);
            double pinyinPredScore = Similarity.pinyinSimilarity(word1, word2);
            double conceptPredScore = Similarity.conceptSimilarity(word1, word2);
            double charBasedPredScore = Similarity.charBasedSimilarity(word1, word2);
            System.out.println(word1 + " vs " + word2 + " 人工标注相似度值:" + expectScore + " 词林相似度值:" + cilinPredScore);
            System.out.println(word1 + " vs " + word2 + " 人工标注相似度值:" + expectScore + " 拼音相似度值:" + pinyinPredScore);
            System.out.println(word1 + " vs " + word2 + " 人工标注相似度值:" + expectScore + " 概念相似度值:" + conceptPredScore);
            System.out.println(word1 + " vs " + word2 + " 人工标注相似度值:" + expectScore + " 字面相似度值:" + charBasedPredScore);
        }
    }
}
