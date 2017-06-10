package org.xm.similarity.text;

import org.xm.similarity.util.AtomicFloat;
import org.xm.tokenizer.Word;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 余弦相似度计算
 * 判定方式：余弦相似度，通过计算两个向量的夹角余弦值来评估他们的相似度
 * 余弦夹角原理：
 * 向量a=(x1,y1),向量b=(x2,y2)
 * similarity=a.b/|a|*|b|
 * a.b=x1x2+y1y2
 * |a|=根号[(x1)^2+(y1)^2],|b|=根号[(x2)^2+(y2)^2]
 *
 * @author xuming
 */
public class CosineSimilarity extends TextSimilarity {
    /**
     * 文本相似度计算
     * 判定方式：余弦相似度，通过计算两个向量的夹角余弦值来评估他们的相似度
     * 余弦夹角原理：
     * 向量a=(x1,y1),向量b=(x2,y2)
     * similarity=a.b/|a|*|b|
     * a.b=x1x2+y1y2
     * |a|=根号[(x1)^2+(y1)^2],|b|=根号[(x2)^2+(y2)^2]
     *
     * @param words1 词列表1
     * @param words2 词列表2
     * @return 分值
     */
    @Override
    public double getSimilarityImpl(List<Word> words1, List<Word> words2) {
        // 词频标注词的权重
        taggingWeightByFrequency(words1, words2);
        // 权重容器
        Map<String, Float> weightMap1 = getFastSearchMap(words1);
        Map<String, Float> weightMap2 = getFastSearchMap(words2);
        Set<Word> words = new HashSet<>();
        words.addAll(words1);
        words.addAll(words2);
        AtomicFloat ab = new AtomicFloat();// a.b
        AtomicFloat aa = new AtomicFloat();// |a|的平方
        AtomicFloat bb = new AtomicFloat();// |b|的平方
        // 计算
        words.parallelStream()
                .forEach(word -> {
                    Float x1 = weightMap1.get(word.getName());
                    Float x2 = weightMap2.get(word.getName());
                    if (x1 != null && x2 != null) {
                        //x1x2
                        float oneOfTheDimension = x1 * x2;
                        //+
                        ab.addAndGet(oneOfTheDimension);
                    }
                    if (x1 != null) {
                        //(x1)^2
                        float oneOfTheDimension = x1 * x1;
                        //+
                        aa.addAndGet(oneOfTheDimension);
                    }
                    if (x2 != null) {
                        //(x2)^2
                        float oneOfTheDimension = x2 * x2;
                        //+
                        bb.addAndGet(oneOfTheDimension);
                    }
                });
        //|a|
        double aaa = Math.sqrt(aa.doubleValue());
        //|b|
        double bbb = Math.sqrt(bb.doubleValue());
        //使用BigDecimal保证精确计算浮点数
        //|a|*|b|
        //double aabb = aaa * bbb;
        BigDecimal aabb = BigDecimal.valueOf(aaa).multiply(BigDecimal.valueOf(bbb));
        //similarity=a.b/|a|*|b|
        //double cos = ab.get() / aabb.doubleValue();
        double cos = BigDecimal.valueOf(ab.get()).divide(aabb, 9, BigDecimal.ROUND_HALF_UP).doubleValue();
        return cos;
    }


}
