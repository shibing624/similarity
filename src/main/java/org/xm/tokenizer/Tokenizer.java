package org.xm.tokenizer;

import com.hankcs.hanlp.HanLP;
import com.hankcs.hanlp.seg.common.Term;

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

    public static List<Word> segment(String sentence) {
        List<Word> results = new ArrayList<>();
        // ansj_seg
//        List<org.xm.ansj.domain.Term> termList = StandardSegmentation.parse(sentence).getTerms();//ansj
//        results.addAll(termList
//                .stream()
//                .map(term -> new Word(term.getName(), term.getNature().natureStr))
//                .collect(Collectors.toList())
//        );

        //Xmnlp
//        List<org.xm.xmnlp.seg.domain.Term> termList = Xmnlp.segment(sentence);
//        results.addAll(termList
//                .stream()
//                .map(term -> new Word(term.word, term.getNature().name()))
//                .collect(Collectors.toList())
//        );

        // HanLP
        List<Term> termList = HanLP.segment(sentence);
        results.addAll(termList
                .stream()
                .map(term -> new Word(term.word, term.nature.name()))
                .collect(Collectors.toList())
        );

        return results;
    }

}
