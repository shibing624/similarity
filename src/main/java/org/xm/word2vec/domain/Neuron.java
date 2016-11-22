package org.xm.word2vec.domain;

/**
 * 神经元
 *
 * @author xuming
 */
public abstract class Neuron implements Comparable<Neuron> {
    public double freq;
    public Neuron parent;
    public int code;
    public int category = -1;

    @Override
    public int compareTo(Neuron o) {
        if (this.category == o.category) {
            if (this.freq > o.freq) {
                return 1;
            } else
                return -1;
        } else if (this.category > o.category) {
            return 1;
        } else {
            return 0;
        }
    }
}
