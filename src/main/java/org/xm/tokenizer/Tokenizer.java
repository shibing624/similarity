package org.xm.tokenizer;

import org.xm.xmnlp.Xmnlp;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 对中文分词的封装，封装了对Xmnlp（xm对hanlp改进版）的调用
 * 对分词器的调用采用了单例模式，实现需要时的延迟加载。
 *
 * @author xuming
 */
public class Tokenizer {
    public static class Word implements Comparable {
        // 词名
        private String name;
        // 词性
        private String pos;
        // 权重，用于词向量分析
        private Float weight;
        private int frequency;

        public Word(String name) {
            this.name = name;
        }

        public Word(String name, String pos) {
            this.name = name;
            this.pos = pos;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getPos() {
            return pos;
        }

        public void setPos(String pos) {
            this.pos = pos;
        }

        public Float getWeight() {
            return weight;
        }

        public void setWeight(Float weight) {
            this.weight = weight;
        }

        public int getFrequency() {
            return frequency;
        }

        public void setFrequency(int frequency) {
            this.frequency = frequency;
        }

        @Override
        public int hashCode() {
            return Objects.hashCode(this.name);
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            final Word other = (Word) obj;
            return Objects.equals(this.name, other.name);
        }

        @Override
        public String toString() {
            StringBuilder str = new StringBuilder();
            if (name != null) {
                str.append(name);
            }
            if (pos != null) {
                str.append("/").append(pos);
            }
            if (frequency > 0) {
                str.append("/").append(frequency);
            }
            return str.toString();
        }

        @Override
        public int compareTo(Object o) {
            if (this == o) {
                return 0;
            }
            if (this.name == null) {
                return -1;
            }
            if (o == null) {
                return 1;
            }
            if (!(o instanceof Word)) {
                return 1;
            }
            String t = ((Word) o).getName();
            if (t == null) {
                return 1;
            }
            return this.name.compareTo(t);
        }
    }

    public static List<Word> segment(String sentence) {
        List<Word> results = new ArrayList<>();
//        List<org.xm.ansj.domain.Term> termList = StandardSegmentation.parse(sentence).getTerms();//ansj
//        results.addAll(termList
//                .stream()
//                .map(term -> new Word(term.getName(), term.getNature().natureStr))
//                .collect(Collectors.toList())
//        );

        //Xmnlp
        List<org.xm.xmnlp.seg.domain.Term> termList = Xmnlp.segment(sentence);
        results.addAll(termList
                .stream()
                .map(term -> new Word(term.word, term.getNature().name()))
                .collect(Collectors.toList())
        );

        return results;
    }

}
