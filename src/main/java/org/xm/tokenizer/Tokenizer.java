package org.xm.tokenizer;

import com.hankcs.hanlp.HanLP;
import com.hankcs.hanlp.corpus.io.IOUtil;
import com.hankcs.hanlp.seg.Segment;
import com.hankcs.hanlp.seg.common.Term;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


/**
 * 对中文分词的封装，封装了对Xmnlp（xuming对HanLP改进版）的调用
 * 对分词器的调用采用了单例模式，实现需要时的延迟加载。
 *
 * @author xuming
 */
public class Tokenizer {
    private static final Logger logger = LoggerFactory.getLogger(Tokenizer.class);

    public static List<Word> segment(String sentence) {
        List<Word> results = new ArrayList<>();
        /*// ansj_seg
        List<org.xm.ansj.domain.Term> termList = StandardSegmentation.parse(sentence).getTerms();//ansj
        results.addAll(termList
                .stream()
                .map(term -> new Word(term.getName(), term.getNature().natureStr))
                .collect(Collectors.toList())
        );*/

        /*//Xmnlp
        List<org.xm.xmnlp.seg.domain.Term> termList = Xmnlp.segment(sentence);
        results.addAll(termList
                .stream()
                .map(term -> new Word(term.word, term.getNature().name()))
                .collect(Collectors.toList())
        );*/

        // HanLP
        List<Term> termList = HanLP.segment(sentence);
        results.addAll(termList
                .stream()
                .map(term -> new Word(term.word, term.nature.name()))
                .collect(Collectors.toList())
        );

        return results;
    }

    public static void fileSegment(String inputFilePath, String outputFilePath) {
        fileSegment(HanLP.newSegment(), inputFilePath, outputFilePath);
    }

    public static void fileSegment(Segment segment, String inputFilePath, String outputFilePath) {
        try {
            WordFreqStatistics.statistics(segment, inputFilePath);
            BufferedReader reader = IOUtil.newBufferedReader(inputFilePath);
            long allCount = 0;
            long lexCount = 0;
            long start = System.currentTimeMillis();
            String outPath = inputFilePath.replace(".txt", "") + "-Segment-Result.txt";
            if (outputFilePath != null && outputFilePath.trim().length() > 0) outPath = outputFilePath;
            FileOutputStream fos = new FileOutputStream(new File(outPath));
            String temp;
            while ((temp = reader.readLine()) != null) {
                List<Term> parse = segment.seg(temp);
                StringBuilder sb = new StringBuilder();
                for (Term term : parse) {
                    sb.append(term.toString() + "\t");
                    if (term.word.trim().length() > 0) {
                        allCount += term.length();
                        lexCount += 1;
                    }
                }
                fos.write(sb.toString().trim().getBytes());
                fos.write("\n".getBytes());
            }

            fos.flush();
            fos.close();
            reader.close();
            long end = System.currentTimeMillis();
            System.out.println("segment result save：" + outPath);
            System.out.println("共 " + allCount + " 个字符，共 " + lexCount + " 个词语，用时" + (end - start) + "毫秒，" +
                    "每秒处理了:" + (allCount * 1000 / (end - start)));
        } catch (IOException e) {
            logger.error("IO error: " + e.getLocalizedMessage());
        }
    }
}
