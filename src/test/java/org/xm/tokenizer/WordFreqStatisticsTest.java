package org.xm.tokenizer;

import com.hankcs.hanlp.tokenizer.NLPTokenizer;
import org.junit.Test;

/**
 * @author XuMing
 */
public class WordFreqStatisticsTest {
    //词频统计
    WordFreqStatistics statistic = new WordFreqStatistics(NLPTokenizer.SEGMENT);

    @Test
    public void seg() throws Exception {
        //开始分词
        statistic.seg("数据建模和算法是自然语言处理课的基础吧，写代码不算什么。下雨天，" +
                "明天有关于分子和原子的课程，下雨了也要去听自然语言处理课");
        //输出词频统计结果
        System.out.println(statistic.getStatisticsMap());
        //输出词频统计结果
        statistic.dump();
    }

    @Test
    public void clear() throws Exception {
        statistic.reset();
        //输出词频统计结果
        System.out.println(statistic.getStatisticsMap());
    }
}