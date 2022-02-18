package org.xm;


import com.hankcs.hanlp.HanLP;
import com.hankcs.hanlp.seg.Segment;
import org.xm.tokenizer.Tokenizer;

import java.io.IOException;


/**
 * 对文件分词并统计
 *
 * @author xuming
 */
public class FileSegmentStatisticsDemo {
    private static final Segment SEGMENT = HanLP.newSegment().enableNameRecognize(true)
            .enablePlaceRecognize(true).enablePartOfSpeechTagging(true).enableMultithreading(4);

    public static void main(String[] args) throws IOException {
        String filePath = "corpus/tianlongbabu_head100.txt";
        Tokenizer.fileSegment(SEGMENT, filePath, null);
    }

}
