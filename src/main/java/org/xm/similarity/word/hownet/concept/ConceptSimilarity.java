package org.xm.similarity.word.hownet.concept;

import org.xm.similarity.util.StringUtil;
import org.xm.similarity.word.hownet.sememe.SememeSimilarity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

/**
 * 概念解析器的实现,用于获取概念、计算概念的相似度等
 * 加入了剪枝处理
 *
 * @author xuming
 */
public class ConceptSimilarity extends ConceptParser {
    private static final int MAX_COMBINED_COUNT = 12;
    private static ConceptSimilarity instance = null;

    public static ConceptSimilarity getInstance() {
        if (instance == null) {
            try {
                instance = new ConceptSimilarity();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return instance;
    }

    private ConceptSimilarity() throws IOException {
        super(new SememeSimilarity());
    }

    public ConceptSimilarity(SememeSimilarity sememeSimilarity) throws IOException {
        super(sememeSimilarity);
    }

    /**
     * 获取两个词语的相似度，如果一个词语对应多个概念，则返回相似度最大的
     *
     * @param word1
     * @param word2
     * @return
     */
    @Override
    public double getSimilarity(String word1, String word2) {
        double similarity = 0.0;
        if (word1.equals(word2)) {
            return 1.0;
        }
        Collection<Concept> concepts1 = getConcepts(word1);
        Collection<Concept> concepts2 = getConcepts(word2);
        // 未登录词需要计算组合概念
        if (StringUtil.isBlank(concepts1) && StringUtil.isNotBlank(concepts2)) {
            concepts1 = autoCombineConcepts(word1, concepts2);
        }
        if (StringUtil.isBlank(concepts2) && StringUtil.isNotBlank(concepts1)) {
            concepts2 = autoCombineConcepts(word2, concepts1);
        }
        if (StringUtil.isBlank(concepts1) && StringUtil.isBlank(concepts2)) {
            concepts1 = autoCombineConcepts(word1, concepts2);
            concepts2 = autoCombineConcepts(word2, concepts1);
            // 修正
            concepts1 = autoCombineConcepts(word1, concepts2);
            concepts2 = autoCombineConcepts(word2, concepts1);
        }

        // 处理所有可能组合的相似度
        for (Concept c1 : concepts1) {
            for (Concept c2 : concepts2) {
                double v = getSimilarity(c1, c2);
                if (v > similarity) {
                    similarity = v;
                }
                if (similarity == 1.0) {
                    break;
                }
            }
        }
        return similarity;
    }

    @Override
    protected double calculate(double sim_v1, double sim_v2, double sim_v3, double sim_v4) {
        return beta1 * sim_v1 + beta2 * sim_v1 * sim_v2 + beta3 * sim_v1 * sim_v3 + beta4 * sim_v1 * sim_v4;
    }

    public Collection<Concept> getConcepts(String key) {
        Collection<Concept> concepts = super.getConcepts(key);
        if (StringUtil.isBlank(concepts)) {
            concepts = autoCombineConcepts(key, null);
        }
        return concepts;
    }

    /**
     * 获取知网本身自带的概念，不组合处理
     *
     * @param key
     * @return
     */
    public Collection<Concept> getInnerConcepts(String key) {
        return super.getConcepts(key);
    }

    /**
     * 计算未登录词语
     *
     * @param newWord
     * @param refConcepts
     * @return
     */
    public Collection<Concept> autoCombineConcepts(String newWord, Collection<Concept> refConcepts) {
        ConceptLinkedList newConcepts = new ConceptLinkedList();
        if (StringUtil.isBlank(newWord)) {
            return newConcepts;
        }
        // 取最可能三个
        List<String> segmentNewWords = segmentNewWord(newWord, 3);
        // 过滤未登录单字
        if (segmentNewWords.size() == 1) {
            return newConcepts;
        }
        // 处理未登录组合词
        for (String conceptWord : segmentNewWords) {
            Collection<Concept> concepts = getConcepts(conceptWord);
            if (newConcepts.size() == 0) {
                newConcepts.addAll(concepts);
                continue;
            }
            ConceptLinkedList tempConcepts = new ConceptLinkedList();
            for (Concept head : concepts) {
                for (Concept tail : newConcepts) {
                    if (StringUtil.isNotBlank(refConcepts)) {
                        for (Concept ref : refConcepts) {
                            tempConcepts.addByDefine(autoCombineConcept(head, tail, ref));
                        }
                    } else {
                        tempConcepts.addByDefine(autoCombineConcept(head, tail, null));
                    }
                }
            }
            newConcepts = tempConcepts;
        }
        // 过滤删除最后的1/3
        if ((newConcepts.size() > MAX_COMBINED_COUNT)) {
            newConcepts.removeLast(MAX_COMBINED_COUNT / 3);
        }
        return newConcepts;
    }

    /**
     * 把未登录词进行概念切分, 形成多个概念的线性链表，并倒排组织
     * 如“娱乐场”切分完毕后存放成: 【场】 → 【娱乐】
     *
     * @param newWord
     * @param top
     * @return
     */
    private List<String> segmentNewWord(String newWord, int top) {
        List<String> results = new LinkedList<>();
        int count = 0;
        String word = newWord;
        while (word != null && !word.equals("")) {
            String token = word;
            while (token.length() > 1 && StringUtil.isBlank(super.getConcepts(token))) {
                token = token.substring(1);
            }
            results.add(token);
            count++;
            if (count >= top) break;
            word = word.substring(0, word.length() - token.length());
        }
        return results;

    }

    /**
     * 计算两个概念的组合概念
     * 计算过程中根据参照概念修正组合结果, 实际应用中的两个概念应具有一定的先后关系(体现汉语“重心后移”特点),
     * 如对于娱乐场，first="娱乐" second="场", 另外，
     * 还需要修正第一个概念中的符号义原对于第二个概念主义原的实际关系，当参照概念起作用时，
     * 即大于指定的阈值，则需要判断是否把当前义原并入组合概念中，对于第一个概念，还需要同时修正符号关系，
     * 符合关系与参照概念保持一致
     *
     * @param head 第一概念
     * @param tail 第二概念
     * @param ref  参考
     * @return
     */
    public Concept autoCombineConcept(Concept head, Concept tail, Concept ref) {
        // 一个null，一个非null，返回非null的新概念
        if (tail == null && head != null) {
            return new Concept(head.getWord(), head.getPos(), head.getDefine());
        }
        if (head == null && tail != null) {
            return new Concept(tail.getWord(), tail.getPos(), tail.getDefine());
        }

        // 第二个概念不是实词，直接返回第一个概念
        if (!tail.isbSubstantive()) {
            return new Concept(head.getWord() + tail.getWord(), head.getPos(), head.getDefine());
        }
        // 如无参照概念，或者参照概念是虚词，则直接相加
        if (ref == null || !ref.isbSubstantive()) {
            String define = tail.getDefine();
            List<String> sememeList = getAllSememes(head, true);
            for (String sememe : sememeList) {
                if (!define.contains(sememe)) {
                    define = define + "," + sememe;
                }
            }
            return new Concept(head.getWord() + tail.getWord(), tail.getPos(), define);
        }
        // 正常处理，实词概念，参考概念非空
        String define = tail.getMainSememe();
        List<String> refSememes = getAllSememes(ref, false);
        List<String> headSememes = getAllSememes(head, true);
        List<String> tailSememes = getAllSememes(tail, false);

        // 如果参照概念与第二个概念的主义原的义原相似度大于阈值THETA，
        // 则限制组合概念定义中与第二个概念相关的义原部分为:
        // 第二个概念的义原集合与参照概念义原集合的模糊交集
        double mainSimilarity = sememeParser.getSimilarity(tail.getMainSememe(), ref.getMainSememe());
        if (mainSimilarity >= PARAM_THETA) {
            // 交集
            for (String tailSememe : tailSememes) {
                double maxSimilarity = 0.0;
                String maxRefSememe = "";
                for (String refSememe : refSememes) {
                    double value = sememeParser.getSimilarity(tailSememe, refSememe);
                    if (value > maxSimilarity) {
                        maxSimilarity = value;
                        maxRefSememe = refSememe;
                    }
                }
                // 如果tail_sememe与参照概念中的相似度最大的义原经theta约束后超过阈值XI，则加入生成的组合概念定义中
                if (maxSimilarity * mainSimilarity >= PARAM_XI) {
                    define = define + "," + tailSememe;
                    refSememes.remove(maxRefSememe);
                }
            }
        } else {
            define = tail.getDefine();
        }
        // 合并第一个概念的义原到组合概念中
        for (String headSememe : headSememes) {
            double maxSimilarity = 0.0;
            String maxRefSememe = "";
            for (String refSememe : refSememes) {
                double value = sememeParser.getSimilarity(getPureSememe(headSememe), getPureSememe(refSememe));
                if (value > maxSimilarity) {
                    maxSimilarity = value;
                    maxRefSememe = refSememe;
                }
            }
            if (mainSimilarity * maxSimilarity >= PARAM_OMEGA) {
                // 调整符号关系, 用参照概念的符号关系替换原符号关系, 通过把参照概念的非符号部分替换成前面义原的非符号内容
                String sememe = maxRefSememe.replace(getPureSememe(maxRefSememe), getPureSememe(headSememe));
                if (!define.contains(sememe)) {
                    define = define + "," + sememe;
                }
            } else if (!define.contains(headSememe)) {
                define = define + "," + headSememe;
            }
        }
        return new Concept(head.getWord() + tail.getWord(), tail.getPos(), define);
    }

    /**
     * 获取概念的所有义原
     *
     * @param concept
     * @param includeMainSememe 是否包含主义原
     * @return
     */
    private List<String> getAllSememes(Concept concept, Boolean includeMainSememe) {
        List<String> results = new ArrayList<>();
        if (concept != null) {
            if (includeMainSememe) {
                results.add(concept.getMainSememe());
            }
            for (String sememe : concept.getSecondSememes()) {
                results.add(sememe);
            }
            for (String sememe : concept.getSymbolSememes()) {
                results.add(sememe);
            }
            for (String sememe : concept.getRelationSememes()) {
                results.add(sememe);
            }
        }
        return results;
    }

    /**
     * 去掉义原的符号和关系
     *
     * @param sememe
     * @return
     */
    private String getPureSememe(String sememe) {
        String line = sememe.trim();
        if ((line.charAt(0) == '(') && (line.charAt(line.length() - 1) == ')')) {
            line = line.substring(1, line.length() - 1);
        }
        // 符号
        String symbol = line.substring(0, 1);
        for (int i = 0; i < Symbol_Descriptions.length; i++) {
            if (symbol.equals(Symbol_Descriptions[i][0])) {
                return line.substring(1);
            }
        }

        // 关系义原、第二基本义原
        int pos = line.indexOf('=');
        if (pos > 0) {
            line = line.substring(pos + 1);
        }
        return line;
    }
}
