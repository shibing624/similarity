package org.xm.tendency.word;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xm.similarity.word.hownet.concept.Concept;
import org.xm.similarity.word.hownet.concept.ConceptParser;
import org.xm.similarity.word.hownet.concept.ConceptSimilarity;
import org.xm.similarity.word.hownet.sememe.SememeParser;
import org.xm.similarity.word.hownet.sememe.SememeSimilarity;

import java.io.IOException;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * 知网词语倾向性
 *
 * @author xuming
 */
public class HownetWordTendency implements IWordTendency {
    private static final Logger logger = LoggerFactory.getLogger(HownetWordTendency.class);
    private ConceptParser conceptParser;
    private SememeParser sememeParser;
    public static String[] POSITIVE_SEMEMES = new String[]{"良", "喜悦", "夸奖", "满意", "期望", "注意", "致敬", "喜欢",
            "专", "敬佩", "同意", "爱惜", "愿意", "思念", "拥护", "祝贺", "福", "需求", "奖励", "致谢", "欢迎", "羡慕",
            "感激", "爱恋"};

    public static String[] NEGATIVE_SEMEMES = new String[]{"莠", "谴责", "害怕", "生气", "悲哀", "着急", "轻视", "羞愧",
            "烦恼", "灰心", "犹豫", "为难", "懊悔", "厌恶", "怀疑", "怜悯", "忧愁", "示怒", "不满", "仇恨", "埋怨",
            "失望", "坏"};

    public HownetWordTendency() {
        this.conceptParser = ConceptSimilarity.getInstance();
        try {
            this.sememeParser = new SememeSimilarity();
        } catch (IOException e) {
            logger.error("exception:{}", e.getMessage());
        }
    }

    @Override
    public double getTendency(String word) {
        double positive = getSentiment(word, POSITIVE_SEMEMES);
        double negative = getSentiment(word, NEGATIVE_SEMEMES);
        return positive - negative;
    }

    private double getSentiment(String word, String[] candidateSememes) {
        Collection<Concept> concepts = conceptParser.getConcepts(word);
        Set<String> sememes = new HashSet<>();
        for (Concept concept : concepts) sememes.addAll(concept.getAllSememeNames());

        double max = 0.0;
        for (String item : sememes) {
            double total = 0.0;
            for (String positiveSememe : candidateSememes) {
                double value = sememeParser.getSimilarity(item, positiveSememe);
                // 如果有特别接近的义原，直接返回该相似值
                if (value > 0.9) {
                    return value;
                }
                total += value;
            }
            double sim = total / candidateSememes.length;
            if (sim > max) {
                max = sim;
            }
        }
        return max;
    }
}
