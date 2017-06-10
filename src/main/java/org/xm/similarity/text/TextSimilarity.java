package org.xm.similarity.text;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xm.similarity.util.StringUtil;
import org.xm.tokenizer.Tokenizer;
import org.xm.tokenizer.Word;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 文本相似度
 *
 * @author xuming
 */
public abstract class TextSimilarity implements ITextSimilarity {

    protected static final Logger LOGGER = LoggerFactory.getLogger(TextSimilarity.class);

    @Override
    public double getSimilarity(String text1, String text2) {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("text1:" + text1);
            LOGGER.debug("text2:" + text2);
        }
        if (StringUtil.isBlank(text1) && StringUtil.isBlank(text2)) {
            return 1.0;
        }
        if (StringUtil.isBlank(text1) || StringUtil.isBlank(text2)) {
            return 0.0;
        }
        if (text1.equalsIgnoreCase(text2)) {
            return 1.0;
        }
        List<Word> words1 = Tokenizer.segment(text1);
        List<Word> words2 = Tokenizer.segment(text2);
        return getSimilarity(words1, words2);
    }

    @Override
    public double getSimilarity(List<Word> words1, List<Word> words2) {
        if (StringUtil.isBlank(words1) && StringUtil.isBlank(words2)) {
            return 1.0;
        }
        if (StringUtil.isBlank(words1) || StringUtil.isBlank(words2)) {
            return 0.0;
        }
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("词列表1：");
            LOGGER.debug("\t" + words1);
            LOGGER.debug("词列表2：");
            LOGGER.debug("\t" + words2);
        }

        double score = getSimilarityImpl(words1, words2);
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("score:" + score);
        }
        score = (int) (score * 1000000 + 0.5) / (double) 1000000;
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("分值，四舍五入：" + score);

        return score;
    }

    protected abstract double getSimilarityImpl(List<Word> words1, List<Word> words2);

    protected void taggingWeightByFrequency(List<Word> words1, List<Word> words2) {
        if (words1.get(0).getWeight() != null || words2.get(0).getWeight() != null) {
            return;
        }
        Map<String, AtomicInteger> frequency1 = getFrequency(words1);
        Map<String, AtomicInteger> frequency2 = getFrequency(words2);
        //输出词频统计信息
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("词频统计1：\n{}", getWordsFrequencyString(frequency1));
            LOGGER.debug("词频统计2：\n{}", getWordsFrequencyString(frequency2));
        }
        // 标注权重
        words1.parallelStream().forEach(word -> word.setWeight(frequency1.get(word.getName()).floatValue()));
        words2.parallelStream().forEach(word -> word.setWeight(frequency2.get(word.getName()).floatValue()));
    }

    /**
     * 统计词频
     *
     * @param words 词列表
     * @return 词频统计图
     */
    private Map<String, AtomicInteger> getFrequency(List<Word> words) {
        Map<String, AtomicInteger> freq = new HashMap<>();
        words.forEach(i -> freq.computeIfAbsent(i.getName(), k -> new AtomicInteger()).incrementAndGet());
        return freq;
    }

    /**
     * 词频统计信息
     *
     * @param frequency 词频
     * @return
     */
    private String getWordsFrequencyString(Map<String, AtomicInteger> frequency) {
        StringBuilder str = new StringBuilder();
        if (frequency != null && !frequency.isEmpty()) {
            AtomicInteger integer = new AtomicInteger();
            frequency.entrySet()
                    .stream()
                    .sorted((a, b) -> b.getValue().get() - a.getValue().get())
                    .forEach(i -> str.append("\t")
                            .append(integer.incrementAndGet())
                            .append("、")
                            .append(i.getKey())
                            .append("=")
                            .append(i.getValue())
                            .append("\n")
                    );
        }
        str.setLength(str.length() - 1);
        return str.toString();
    }

    /**
     * 构造权重快速搜索容器
     *
     * @param words
     * @return
     */
    protected Map<String, Float> getFastSearchMap(List<Word> words) {
        Map<String, Float> weightMap = new ConcurrentHashMap<>();
        if (words == null) return weightMap;
        words.parallelStream().forEach(i -> {
            if (i.getWeight() != null) {
                weightMap.put(i.getName(), i.getWeight());
            } else {
                LOGGER.error("no word weight info:" + i.getName());
            }
        });
        return weightMap;
    }
}
