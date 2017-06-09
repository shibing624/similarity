package org.xm.similarity.word.hownet.concept;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xm.Similarity;
import org.xm.similarity.util.DicReader;
import org.xm.similarity.util.MathUtil;
import org.xm.similarity.util.StringUtil;
import org.xm.similarity.word.IWordSimilarity;
import org.xm.similarity.word.hownet.IHownetMeta;
import org.xm.similarity.word.hownet.sememe.SememeParser;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.zip.GZIPInputStream;

/**
 * 概念解析器
 *
 * @author xuming
 */
public abstract class ConceptParser implements IHownetMeta, IWordSimilarity {
    private static final Logger logger = LoggerFactory.getLogger(ConceptParser.class);
    private static Multimap<String, Concept> CONCEPTS = null;
    private final static String path = Similarity.Config.ConceptXmlPath;
    protected SememeParser sememeParser = null;

    /**
     * 集合运算类型，目前支持均值运算和模糊集运算两种形式
     */
    public enum OPERATE_TYPE {
        AVERAGE,
        FUZZY
    }

    private OPERATE_TYPE currentOperateType = OPERATE_TYPE.AVERAGE;

    public ConceptParser(SememeParser sememeParser) throws IOException {
        this.sememeParser = sememeParser;
        synchronized (this) {
            if (CONCEPTS == null) {
                loadFile();
            }
        }
    }


    private static void loadFile() throws IOException {
        CONCEPTS = HashMultimap.create();
        InputStream inputStream = new GZIPInputStream(DicReader.getInputStream(path));
        load(inputStream);
    }

    /**
     * 用户自定义概念词典
     *
     * @param xmlFile
     * @throws IOException
     */
    public static void load(File xmlFile) throws IOException {
        if (CONCEPTS == null) {
            loadFile();
        }
        load(new FileInputStream(xmlFile));
    }

    private static void load(InputStream inputStream) throws IOException {
        long start = System.currentTimeMillis();
        int count = 0;
        try {
            XMLInputFactory inputFactory = XMLInputFactory.newInstance();
            XMLEventReader xmlEventReader = inputFactory.createXMLEventReader(inputStream);
            while (xmlEventReader.hasNext()) {
                XMLEvent event = xmlEventReader.nextEvent();
                if (event.isStartElement()) {
                    StartElement startElement = event.asStartElement();
                    if (startElement.getName().toString().equals("c")) {
                        String word = startElement.getAttributeByName(QName.valueOf("w")).getValue();
                        String define = startElement.getAttributeByName(QName.valueOf("d")).getValue();
                        String pos = startElement.getAttributeByName(QName.valueOf("p")).getValue();
                        CONCEPTS.put(word, new Concept(word, pos, define));
                        count++;
                    }
                }
            }
            inputStream.close();
        } catch (Exception e) {
            throw new IOException(e);
        }
        logger.info("complete! count num:" + count + "，time spend:" + (System.currentTimeMillis() - start) + "ms");
    }

    /**
     * 获取两个词语的相似度，如果一个词语对应多个概念，则返回相似度最大的一对
     *
     * @param word1
     * @param word2
     * @return
     */
    public abstract double getSimilarity(String word1, String word2);

    /**
     * 计算四个组成部分的相似度方式，不同的算法对这四个部分的处理或者说权重分配不同
     *
     * @param sim_v1 主义原的相似度
     * @param sim_v2 其他基本义原的相似度
     * @param sim_v3 关系义原的相似度
     * @param sim_v4 符号义原的相似度
     * @return
     */
    protected abstract double calculate(double sim_v1, double sim_v2, double sim_v3, double sim_v4);

    /**
     * 判断一个词语是否是一个概念
     *
     * @param word
     * @return
     */
    public boolean isConcept(String word) {
        return StringUtil.isNotBlank(CONCEPTS.get(word));
    }

    /**
     * 根据名称获取对应的概念定义信息，由于一个词语可能对应多个概念，因此返回一个集合
     *
     * @param key
     * @return
     */
    public Collection<Concept> getConcepts(String key) {
        return CONCEPTS.get(key);
    }

    public double getSimilarity(Concept concept1, Concept concept2) {
        double similarity = 0.0;
        if (concept1 == null || concept2 == null || !concept1.getPos().equals(concept2.getPos())) {
            return 0.0;
        }
        if (concept1.equals(concept2)) {
            return 1.0;
        }
        // 虚词和实词概念的相似度是0
        if (concept1.isbSubstantive() != concept2.isbSubstantive()) {
            return 0.0;
        }
        // 虚词
        if (concept1.isbSubstantive() == false) {
            similarity = sememeParser.getSimilarity(concept1.getMainSememe(), concept2.getMainSememe());
        } else {// 实词
            double sim1 = sememeParser.getSimilarity(concept1.getMainSememe(), concept2.getMainSememe());
            double sim2 = getSimilarity(concept1.getSecondSememes(), concept2.getSecondSememes());
            double sim3 = getSimilarity(concept1.getRelationSememes(), concept2.getRelationSememes());
            double sim4 = getSimilarity(concept1.getSymbolSememes(), concept2.getSymbolSememes());
            similarity = calculate(sim1, sim2, sim3, sim4);
        }
        return similarity;
    }

    /**
     * 计算两个义原集合的相似度
     * 每一个集合都是一个概念的某一类义原集合，如第二基本义原、符号义原、关系义原等
     *
     * @param sememes1
     * @param sememes2
     * @return
     */
    private double getSimilarity(String[] sememes1, String[] sememes2) {
        if (currentOperateType == OPERATE_TYPE.FUZZY) {
            return getSimilarityFuzzy(sememes1, sememes2);
        } else {
            return getSimilarityAVG(sememes1, sememes2);
        }
    }

    private double getSimilarityFuzzy(String[] sememes1, String[] sememes2) {
        // TODO
        return 0;
    }

    public void setOperateType(OPERATE_TYPE type) {
        this.currentOperateType = type;
    }

    private double getSimilarityAVG(String[] sememes1, String[] sememes2) {
        double similarity;
        double scoreArray[][];
        if (StringUtil.isBlank(sememes1) || StringUtil.isBlank(sememes2)) {
            if (StringUtil.isBlank(sememes1) && StringUtil.isBlank(sememes2)) {
                return 1.0;
            } else {
                return delta;// 一个非空值与空值的相似度为一个小的常数
            }
        }
        double score = 0.0;
        int arrayLen = MathUtil.max(sememes1.length, sememes2.length);
        scoreArray = new double[arrayLen][arrayLen];
        // calculate similarity of two set
        for (int i = 0; i < sememes1.length; i++) {
            for (int j = 0; j < sememes2.length; j++) {
                scoreArray[i][j] = sememeParser.getSimilarity(sememes1[i], sememes2[j]);
            }
        }

        // get max similarity score
        while (scoreArray.length > 0) {
            double[][] temp;
            int row = 0;
            int column = 0;
            double max = scoreArray[row][column];
            for (int i = 0; i < scoreArray.length; i++) {
                for (int j = 0; j < scoreArray[i].length; j++) {
                    if (scoreArray[i][j] > max) {
                        row = i;
                        column = j;
                        max = scoreArray[i][j];
                    }
                }
            }
            score += max;
            // 过滤掉该行该列，继续计算
            temp = new double[scoreArray.length - 1][scoreArray.length - 1];
            for (int i = 0; i < scoreArray.length; i++) {
                if (i == row) {
                    continue;
                }
                for (int j = 0; j < scoreArray[i].length; j++) {
                    if (j == column) {
                        continue;
                    }
                    int tempRow = i;
                    int tempColumn = j;
                    if (i > row) {
                        tempRow--;
                    }
                    if (j > column) {
                        tempColumn--;
                    }
                    temp[tempRow][tempColumn] = scoreArray[i][j];
                }
            }
            scoreArray = temp;
        }
        similarity = score / arrayLen;
        return similarity;
    }

}
