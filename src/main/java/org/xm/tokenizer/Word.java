package org.xm.tokenizer;

import com.hankcs.hanlp.HanLP;

import java.util.Objects;

/**
 * 封装分词结果
 *
 * @author XuMing
 */
public class Word implements Comparable {
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

    public String toString(String split) {
        if (HanLP.Config.ShowTermNature)
            return name + split + pos;
        return name;
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

