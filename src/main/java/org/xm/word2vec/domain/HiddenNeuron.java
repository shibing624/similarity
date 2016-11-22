package org.xm.word2vec.domain;

/**
 * 隐藏神经元
 *
 * @author xuming
 */
public class HiddenNeuron extends Neuron {
    public double[] syn1;// 隐藏层 -> 输出层

    public HiddenNeuron(int layerSize) {
        syn1 = new double[layerSize];
    }
}
