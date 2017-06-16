package org.xm.word2vec.vec;

import org.xm.word2vec.domain.HiddenNeuron;
import org.xm.word2vec.domain.Neuron;
import org.xm.word2vec.domain.WordNeuron;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 学习
 *
 * @author xuming
 */
public class Learn {
    private Map<String, Neuron> wordMap = new HashMap<>();
    private int layerSize = 200;// 特征数
    private int window = 5;
    private double sample = 1e-3;
    private double alpha = 0.025;
    private double startingAlpha = alpha;
    public int EXP_TABLE_SIZE = 1000;
    private boolean isCbow = false;
    private double[] expTable = new double[EXP_TABLE_SIZE];
    private int trainWordsCount = 0;
    private int MAX_EXP = 6;

    public Learn(Boolean isCbow, Integer layerSize, Integer window, Double alpha, Double sample) {
        createExpTable();
        this.isCbow = isCbow;
        this.layerSize = layerSize;
        this.window = window;
        this.alpha = alpha;
        this.sample = sample;
    }

    public Learn() {
        createExpTable();
    }

    /**
     * 预计算：exp() table f(x) = x / (x + 1)
     */
    private void createExpTable() {
        for (int i = 0; i < EXP_TABLE_SIZE; i++) {
            expTable[i] = Math.exp(((i / (double) EXP_TABLE_SIZE * 2 - 1) * MAX_EXP));
            expTable[i] = expTable[i] / (expTable[i] + 1);
        }
    }

    /**
     * 训练模型
     *
     * @param file
     * @throws IOException
     */
    private void trainModel(File file) throws IOException {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file)))) {
            String temp;
            long nextRandom = 5;
            int wordCount = 0;
            int lastWordCount = 0;
            int wordCountActual = 0;
            while ((temp = br.readLine()) != null) {
                if (wordCount - lastWordCount > 10000) {
                    System.out.println("alpha:" + alpha + "\tProgress: "
                            + (int) (wordCountActual / (double) (trainWordsCount + 1) * 100) + "%");
                    wordCountActual += wordCount - lastWordCount;
                    lastWordCount = wordCount;
                    alpha = startingAlpha * (1 - wordCountActual / (double) (trainWordsCount + 1));
                    if (alpha < startingAlpha * 0.0001) {
                        alpha = startingAlpha * 0.0001;
                    }
                }
                String[] strs = temp.split("[\t ]+");
                wordCount += strs.length;
                List<WordNeuron> sentence = new ArrayList<WordNeuron>();
                for (int i = 0; i < strs.length; i++) {
                    Neuron entry = wordMap.get(strs[i]);
                    if (entry == null) {
                        continue;
                    }
                    // The subsampling randomly discards frequent words while keeping the ranking same
                    if (sample > 0) {
                        double ran = (Math.sqrt(entry.freq / (sample * trainWordsCount)) + 1)
                                * (sample * trainWordsCount) / entry.freq;
                        nextRandom = nextRandom * 25214903917L + 11;
                        if (ran < (nextRandom & 0xFFFF) / (double) 65536) {
                            continue;
                        }
                    }
                    sentence.add((WordNeuron) entry);
                }

                for (int index = 0; index < sentence.size(); index++) {
                    nextRandom = nextRandom * 25214903917L + 11;
                    if (isCbow) {
                        cbowGram(index, sentence, (int) nextRandom % window);
                    } else {
                        skipGram(index, sentence, (int) nextRandom % window);
                    }
                }

            }
            System.out.println("Vocab size: " + wordMap.size());
            System.out.println("Words in train file: " + trainWordsCount);
            System.out.println("sucess train over!");
        }
    }

    /**
     * skip gram 模型训练
     */
    private void skipGram(int index, List<WordNeuron> sentence, int b) {
        WordNeuron word = sentence.get(index);
        int a, c = 0;
        for (a = b; a < window * 2 + 1 - b; a++) {
            if (a == window) {
                continue;
            }
            c = index - window + a;
            if (c < 0 || c >= sentence.size()) {
                continue;
            }

            double[] neu1e = new double[layerSize];// 误差项
            // HIERARCHICAL SOFTMAX
            List<Neuron> neurons = word.neurons;
            WordNeuron we = sentence.get(c);
            for (int i = 0; i < neurons.size(); i++) {
                HiddenNeuron out = (HiddenNeuron) neurons.get(i);
                double f = 0;
                // Propagate hidden -> output
                for (int j = 0; j < layerSize; j++) {
                    f += we.syn0[j] * out.syn1[j];
                }
                if (f <= -MAX_EXP || f >= MAX_EXP) {
                    continue;
                } else {
                    f = (f + MAX_EXP) * (EXP_TABLE_SIZE / MAX_EXP / 2);
                    f = expTable[(int) f];
                }
                // 'g' is the gradient multiplied by the learning rate
                double g = (1 - word.codeArray[i] - f) * alpha;
                // Propagate errors output -> hidden
                for (c = 0; c < layerSize; c++) {
                    neu1e[c] += g * out.syn1[c];
                }
                // Learn weights hidden -> output
                for (c = 0; c < layerSize; c++) {
                    out.syn1[c] += g * we.syn0[c];
                }
            }

            // Learn weights input -> hidden
            for (int j = 0; j < layerSize; j++) {
                we.syn0[j] += neu1e[j];
            }
        }

    }

    /**
     * 词袋模型
     *
     * @param index
     * @param sentence
     * @param b
     */
    private void cbowGram(int index, List<WordNeuron> sentence, int b) {
        WordNeuron word = sentence.get(index);
        int a, c;

        List<Neuron> neurons = word.neurons;
        double[] neu1e = new double[layerSize];// 误差项
        double[] neu1 = new double[layerSize];// 误差项
        WordNeuron last_word;

        for (a = b; a < window * 2 + 1 - b; a++)
            if (a != window) {
                c = index - window + a;
                if (c < 0)
                    continue;
                if (c >= sentence.size())
                    continue;
                last_word = sentence.get(c);
                if (last_word == null)
                    continue;
                for (c = 0; c < layerSize; c++)
                    neu1[c] += last_word.syn0[c];
            }

        // HIERARCHICAL SOFTMAX
        for (int d = 0; d < neurons.size(); d++) {
            HiddenNeuron out = (HiddenNeuron) neurons.get(d);
            double f = 0;
            // Propagate hidden -> output
            for (c = 0; c < layerSize; c++)
                f += neu1[c] * out.syn1[c];
            if (f <= -MAX_EXP)
                continue;
            else if (f >= MAX_EXP)
                continue;
            else
                f = expTable[(int) ((f + MAX_EXP) * (EXP_TABLE_SIZE / MAX_EXP / 2))];
            // 'g' is the gradient multiplied by the learning rate
            // double g = (1 - word.codeArray[d] - f) * alpha;
            // double g = f*(1-f)*( word.codeArray[i] - f) * alpha;
            double g = f * (1 - f) * (word.codeArray[d] - f) * alpha;
            //
            for (c = 0; c < layerSize; c++) {
                neu1e[c] += g * out.syn1[c];
            }
            // Learn weights hidden -> output
            for (c = 0; c < layerSize; c++) {
                out.syn1[c] += g * neu1[c];
            }
        }
        for (a = b; a < window * 2 + 1 - b; a++) {
            if (a != window) {
                c = index - window + a;
                if (c < 0)
                    continue;
                if (c >= sentence.size())
                    continue;
                last_word = sentence.get(c);
                if (last_word == null)
                    continue;
                for (c = 0; c < layerSize; c++)
                    last_word.syn0[c] += neu1e[c];
            }

        }
    }

    /**
     * 读入词语，并统计词频
     *
     * @param file
     * @throws IOException
     */
    private void readWord(File file) throws IOException {
        VecMap<String> map = new VecMap<>();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file)))) {
            String temp;
            while ((temp = br.readLine()) != null) {
                String[] split = temp.split("[\t ]+");
                trainWordsCount += split.length;
                for (String s : split) {
                    map.add(s);
                }
            }
        }
        for (Map.Entry<String, Integer> ele : map.getHm().entrySet()) {
            wordMap.put(ele.getKey(), new WordNeuron(ele.getKey(), (double) ele.getValue() / map.size(), layerSize));
        }
    }

    /**
     * 对文本进行预分类
     *
     * @param files
     * @throws IOException
     * @throws FileNotFoundException
     */
    private void readVocabWithSupervised(File[] files) throws IOException {
        for (int category = 0; category < files.length; category++) {
            // 对多个文件学习
            VecMap<String> map = new VecMap<>();
            try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(files[category])))) {
                String temp;
                while ((temp = br.readLine()) != null) {
                    String[] split = temp.split("[\t ]+");
                    trainWordsCount += split.length;
                    for (String string : split) map.add(string);
                }
            }
            for (Map.Entry<String, Integer> element : map.getHm().entrySet()) {
                double tarFreq = (double) element.getValue() / map.size();
                if (wordMap.get(element.getKey()) != null) {
                    double srcFreq = wordMap.get(element.getKey()).freq;
                    if (srcFreq >= tarFreq) {
                        continue;
                    }
                    Neuron wordNeuron = wordMap.get(element.getKey());
                    wordNeuron.category = category;
                    wordNeuron.freq = tarFreq;
                } else {
                    wordMap.put(element.getKey(), new WordNeuron(element.getKey(), tarFreq, category, layerSize));
                }
            }
        }
    }

    /**
     * 根据文件学习
     *
     * @param file
     * @throws IOException
     */
    public void learnFile(File file) throws IOException {
        readWord(file);
        new Huffman(layerSize).buildTree(wordMap.values());

        // 查找每个神经元
        for (Neuron neuron : wordMap.values()) {
            ((WordNeuron) neuron).makeNeurons();
        }

        trainModel(file);
    }

    /**
     * 根据预分类的文件学习
     *
     * @param summaryFile     合并文件
     * @param classifiedFiles 分类文件
     * @throws IOException
     */
    public void learnFile(File summaryFile, File[] classifiedFiles)
            throws IOException {
        readVocabWithSupervised(classifiedFiles);
        new Huffman(layerSize).buildTree(wordMap.values());
        // 查找每个神经元
        for (Neuron neuron : wordMap.values()) {
            ((WordNeuron) neuron).makeNeurons();
        }
        trainModel(summaryFile);
    }

    /**
     * 保存模型
     */
    public void saveModel(File file) {
        try (DataOutputStream dataOutputStream = new DataOutputStream(
                new BufferedOutputStream(new FileOutputStream(file)))) {
            dataOutputStream.writeInt(wordMap.size());
            dataOutputStream.writeInt(layerSize);
            double[] syn0 = null;
            for (Map.Entry<String, Neuron> element : wordMap.entrySet()) {
                dataOutputStream.writeUTF(element.getKey());
                syn0 = ((WordNeuron) element.getValue()).syn0;
                for (double d : syn0) {
                    dataOutputStream.writeFloat(((Double) d).floatValue());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public int getLayerSize() {
        return layerSize;
    }

    public void setLayerSize(int layerSize) {
        this.layerSize = layerSize;
    }

    public int getWindow() {
        return window;
    }

    public void setWindow(int window) {
        this.window = window;
    }

    public double getSample() {
        return sample;
    }

    public void setSample(double sample) {
        this.sample = sample;
    }

    public double getAlpha() {
        return alpha;
    }

    public void setAlpha(double alpha) {
        this.alpha = alpha;
        this.startingAlpha = alpha;
    }

    public boolean isCbow() {
        return isCbow;
    }

    public void setCbow(boolean cbow) {
        isCbow = cbow;
    }
}
