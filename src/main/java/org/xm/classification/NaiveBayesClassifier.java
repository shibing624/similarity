package org.xm.classification;

import java.io.*;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class NaiveBayesClassifier {

    /**
     * 记录每个类别下出现的文档数量, 用于计算P(C)使用
     */
    Variable VARIABLE = new Variable();

    /**
     * 词语在所有类别中的总数量
     */
    Map<String, Integer> TERM_TOTAL_COUNT = new HashMap<>();

    /**
     * 训练一篇文档
     *
     * @param doc
     */
    public void training(Instance doc) {
        VARIABLE.addInstance(doc);
    }

    /**
     * 保存训练结果
     *
     * @throws IOException
     */
    void save(File file) throws IOException {
        DataOutput out = new DataOutputStream(new FileOutputStream(file));
        VARIABLE.write(out);
    }

    public void load(File file) throws IOException {
        DataInputStream in = new DataInputStream(new FileInputStream(file));
        VARIABLE = Variable.read(in);
    }

    /**
     * 计算P（C)
     *
     * @param category
     * @return
     */
    public double getCategoryProbability(String category) {
        return Math.log(VARIABLE.getDocCount(category) * 1.0f / VARIABLE.getDocCount());
    }

    /**
     * 计算P(feature|cateogry),返回的是取对数后的数值
     *
     * @param feature
     * @param category
     * @return
     */
    public double getFeatureProbability(String feature, String category) {
        int m = VARIABLE.getFeatureCount();
        return Math.log((VARIABLE.getDocCount(feature, category) + 1.0) / (VARIABLE.getDocCount(category) + m));
    }

    /**
     * 计算给定实例文档属于指定类别的概率
     *
     * @param category
     * @param doc
     * @return 返回的是取对数后的数值
     */
    public double getProbability(String category, Instance doc) {
        double result = getCategoryProbability(category);
        for (String feature : doc.getWords()) {
            if (VARIABLE.containFeature(feature)) {
                result += getFeatureProbability(feature, category);
            }
        }
        return result;
    }

    public String getCategory(Instance doc) {
        Collection<String> categories = VARIABLE.getCategories();
        double best = Double.NEGATIVE_INFINITY;
        String bestName = null;
        for (String c : categories) {
            double current = getProbability(c, doc);
            //			System.out.println(c + ":" + current);
            if (best < current) {
                best = current;
                bestName = c;
            }
        }
        return bestName;
    }

}
