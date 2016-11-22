package org.xm.similarity.sentence;

import org.xm.ansj.domain.Term;
import org.xm.ansj.segmentation.StandardSegmentation;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 对中文分词的封装，封装了对Xmnlp（xm对hanlp改进版）的调用
 * 对Segment的调用采用了单例模式，实现需要时的延迟加载。
 *
 * @author xuming
 */
public class SegmentTool {
    public static class Word {
        // 词名
        private String word;
        // 词性
        private String pos;

        public Word(String word, String pos) {
            this.word = word;
            this.pos = pos;
        }

        public String getWord() {
            return word;
        }

        public void setWord(String word) {
            this.word = word;
        }

        public String getPos() {
            return pos;
        }

        public void setPos(String pos) {
            this.pos = pos;
        }
    }

    public static List<Word> segment(String sentence) {
        List<Word> results = new ArrayList<>();
        List<Term> termList = StandardSegmentation.parse(sentence).getTerms();
        results.addAll(termList
                .stream()
                .map(term -> new Word(term.getName(), term.getNature().natureStr))
                .collect(Collectors.toList())
        );
        return results;
    }

    public static String getSegmentResult(String sentence){
        List<Word> words = segment(sentence);
        StringBuilder sb = new StringBuilder();
        for(Word word:words) sb.append(word.getWord() + "/" + word.getPos()).append(" ");
        return sb.toString();
    }
}
