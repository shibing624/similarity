package org.xm.similarity.sentence;

import org.xm.xmnlp.seg.domain.Term;
import org.xm.xmnlp.tokenizer.StandardTokenizer;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 对中文分词的封装，封装了对Xmnlp（xm对hanlp改进版）的调用
 * 对Segment的调用采用了单例模式，实现需要时的延迟加载。
 *
 * @author xuming
 */
public class Segment {
    public static class Word {
        private String word;
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
        List<Term> termList = StandardTokenizer.segment(sentence);
        results.addAll(termList
                .stream()
                .map(term -> new Word(term.word, term.getNature().name()))
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
