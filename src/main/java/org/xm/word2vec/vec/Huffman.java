package org.xm.word2vec.vec;

import org.xm.word2vec.domain.HiddenNeuron;
import org.xm.word2vec.domain.Neuron;

import java.util.Collection;
import java.util.TreeSet;

/**
 * huffman编码
 *
 * @author xuming
 */
public class Huffman {
    private int layerSize;

    public Huffman(int layerSize) {
        this.layerSize = layerSize;
    }

    private TreeSet<Neuron> set = new TreeSet<>();

    public void buildTree(Collection<Neuron> neurons) {
        set.addAll(neurons);
        while (set.size() > 1) {
            merge();
        }
    }

    private void merge() {
        HiddenNeuron hn = new HiddenNeuron(layerSize);
        Neuron neuron1 = set.pollFirst();
        Neuron neuron2 = set.pollFirst();
        hn.category = neuron2.category;
        hn.freq = neuron1.freq + neuron2.freq;
        neuron1.parent = hn;
        neuron2.parent = hn;
        neuron1.code = 0;
        neuron2.code = 1;
        set.add(hn);
    }
}
