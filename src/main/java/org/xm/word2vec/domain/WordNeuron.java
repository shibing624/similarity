package org.xm.word2vec.domain;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

/**
 * 词语
 *
 * @author xuming
 */
public class WordNeuron extends Neuron {
    public String name;
    public double[] syn0;
    public List<Neuron> neurons;// 路径
    public int[] codeArray;

    public List<Neuron> makeNeurons() {
        if (neurons != null) return neurons;
        Neuron neuron = this;
        neurons = new LinkedList<>();
        while ((neuron = neuron.parent) != null) {
            neurons.add(neuron);
        }
        Collections.reverse(neurons);
        codeArray = new int[neurons.size()];
        for (int i = 1; i < neurons.size(); i++) {
            codeArray[i - 1] = neurons.get(i).code;
        }

        codeArray[codeArray.length - 1] = this.code;
        return neurons;
    }

    public WordNeuron(String name, double freq, int layerSize) {
        this.name = name;
        this.freq = freq;
        this.syn0 = new double[layerSize];
        Random random = new Random();
        for (int i = 0; i < syn0.length; i++) {
            syn0[i] = (random.nextDouble() - 0.5) / layerSize;
        }
    }

    /**
     * huffman tree
     *
     * @param name
     * @param freq
     * @param category
     * @param layerSize
     */
    public WordNeuron(String name, double freq, int category, int layerSize) {
        this.name = name;
        this.freq = freq;
        this.syn0 = new double[layerSize];
        this.category = category;
        Random random = new Random();
        for (int i = 0; i < syn0.length; i++) {
            syn0[i] = (random.nextDouble() - 0.5) / layerSize;
        }
    }
}
