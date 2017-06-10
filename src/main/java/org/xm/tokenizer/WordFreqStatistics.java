package org.xm.tokenizer;


import com.hankcs.hanlp.HanLP;
import com.hankcs.hanlp.corpus.io.IOUtil;
import com.hankcs.hanlp.seg.Segment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xm.Similarity;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;


/**
 * 词频统计工具
 *
 * @author xuming
 */
public class WordFreqStatistics {
    protected static final Logger logger = LoggerFactory.getLogger(WordFreqStatistics.class);
    private String resultPath = Similarity.Config.StatisticsResultPath;
    private Map<String, AtomicInteger> statisticsMap = new ConcurrentHashMap<>();
    private Segment segment = HanLP.newSegment();

    /**
     * 获取词频统计结果保存路径
     *
     * @return 词频统计结果保存路径
     */
    public String getResultPath() {
        return resultPath;
    }

    /**
     * 设置词频统计结果保存路径
     *
     * @param resultPath 词频统计结果保存路径
     */
    public void setResultPath(String resultPath) {
        this.resultPath = resultPath;
    }

    public Segment getSegment() {
        return segment;
    }

    public void setSegment(Segment segment) {
        this.segment = segment;
    }

    public WordFreqStatistics() {
    }

    public WordFreqStatistics(String resultPath) {
        this.resultPath = resultPath;
    }

    public WordFreqStatistics(Segment segment) {
        this.segment = segment;
    }

    public WordFreqStatistics(String resultPath, Segment segment) {
        this.resultPath = resultPath;
        this.segment = segment;
    }

    private void statistics(String word, int times, Map<String, AtomicInteger> container) {
        container.putIfAbsent(word, new AtomicInteger());
        container.get(word).addAndGet(times);
    }

    private void dump(Map<String, AtomicInteger> map, String path) {
        try {
            //score rank
            List<String> list = map.entrySet()
                    .parallelStream()
                    .sorted((a, b) -> new Integer(b.getValue().get()).compareTo(a.getValue().intValue()))
                    .map(entry -> entry.getKey().toString() + "\t" + entry.getValue().get())
                    .collect(Collectors.toList());
            Files.write(Paths.get(path), list);
            if (list.size() < 100) {
                System.out.println("word statistics result:");
                AtomicInteger i = new AtomicInteger();
                list.forEach(item -> System.out.println("\t" + i.incrementAndGet() + "、" + item));
            }
            System.out.println("statistic result save：" + path);
        } catch (Exception e) {
            logger.error("dump error!");
        }
    }

    public void seg(String text) {
        segment.seg(text).parallelStream().forEach(i -> statistics(i.toString(), 1, statisticsMap));
    }

    /**
     * 将词频统计结果保存到文件
     *
     * @param resultPath 词频统计结果保存路径
     */
    public void dump(String resultPath) {
        this.resultPath = resultPath;
        dump();
    }

    /**
     * 将词频统计结果保存到文件
     */
    public void dump() {
        dump(this.statisticsMap, this.resultPath);
    }

    /**
     * 清除之前的统计结果
     */
    public void reset() {
        this.statisticsMap.clear();
    }

    public Map<String, AtomicInteger> getStatisticsMap() {
        return statisticsMap;
    }

    public void setStatisticsMap(Map<String, AtomicInteger> statisticsMap) {
        this.statisticsMap = statisticsMap;
    }

    public static void statistics(Segment segment, String inputFilePath) {
        try {
            //词频统计
            WordFreqStatistics statistic = new WordFreqStatistics(segment);
            BufferedReader reader = IOUtil.newBufferedReader(inputFilePath);
            String t;
            StringBuilder s = new StringBuilder();
            while ((t = reader.readLine()) != null) {
                s.append(t);
            }
            statistic.seg(s.toString());
            statistic.setResultPath(inputFilePath.replace(".txt", "") + "-WordFrequencyStatistics-Result.txt");
            statistic.dump();
            reader.close();
        } catch (IOException e) {
            logger.error("IO error: " + e.getLocalizedMessage());
        }
    }
}
