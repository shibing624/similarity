package org.xm.word2vec.vec;

import org.xm.word2vec.domain.WordEntry;

import java.io.*;
import java.util.*;

/**
 * word2vec 模型的使用
 *
 * @author xuming
 */
public class ModelParser {
    private int words;
    private int size;
    private int topNSize = 40;
    private HashMap<String, float[]> wordMap = new HashMap<>();
    private static final int MAX_SIZE = 50;

    public void loadModel(String path) throws IOException {
        try (DataInputStream dis = new DataInputStream(new BufferedInputStream(new FileInputStream(path)))) {
            words = dis.readInt();
            size = dis.readInt();
            float vector;
            String key;
            float[] value;
            for (int i = 0; i < words; i++) {
                double len = 0.0;
                key = dis.readUTF();
                value = new float[size];
                for (int j = 0; j < size; j++) {
                    vector = dis.readFloat();
                    len += vector * vector;
                    value[j] = vector;
                }
                len = Math.sqrt(len);
                for (int j = 0; j < size; j++) {
                    value[j] /= len;
                }
                wordMap.put(key, value);
            }
        }
    }

    /**
     * 近义词
     */
    public TreeSet<WordEntry> analogy(String word0, String word1, String word2) {
        float[] wv0 = getWordVector(word0);
        float[] wv1 = getWordVector(word1);
        float[] wv2 = getWordVector(word2);

        if (wv1 == null || wv2 == null || wv0 == null) {
            return null;
        }
        float[] wordVector = new float[size];
        for (int i = 0; i < size; i++) {
            wordVector[i] = wv1[i] - wv0[i] + wv2[i];
        }
        float[] tempVector;
        String name;
        List<WordEntry> wordEntrys = new ArrayList<WordEntry>(topNSize);
        for (Map.Entry<String, float[]> entry : wordMap.entrySet()) {
            name = entry.getKey();
            if (name.equals(word0) || name.equals(word1) || name.equals(word2)) {
                continue;
            }
            float dist = 0;
            tempVector = entry.getValue();
            for (int i = 0; i < wordVector.length; i++) {
                dist += wordVector[i] * tempVector[i];
            }
            insertTopN(name, dist, wordEntrys);
        }
        return new TreeSet<WordEntry>(wordEntrys);
    }

    private void insertTopN(String name, float score, List<WordEntry> wordsEntrys) {
        if (wordsEntrys.size() < topNSize) {
            wordsEntrys.add(new WordEntry(name, score));
            return;
        }
        float min = Float.MAX_VALUE;
        int minOffe = 0;
        for (int i = 0; i < topNSize; i++) {
            WordEntry wordEntry = wordsEntrys.get(i);
            if (min > wordEntry.score) {
                min = wordEntry.score;
                minOffe = i;
            }
        }

        if (score > min) {
            wordsEntrys.set(minOffe, new WordEntry(name, score));
        }

    }

    public Set<WordEntry> distance(String queryWord) {
        float[] center = wordMap.get(queryWord);
        if (center == null) {
            return Collections.emptySet();
        }

        int resultSize = wordMap.size() < topNSize ? wordMap.size() : topNSize;
        TreeSet<WordEntry> result = getWordEntries(center, resultSize);

        return result;
    }


    public Set<WordEntry> distance(List<String> words) {
        float[] center = null;
        for (String word : words) {
            center = sum(center, wordMap.get(word));
        }

        if (center == null) {
            return Collections.emptySet();
        }

        int resultSize = wordMap.size() < topNSize ? wordMap.size() : topNSize;
        TreeSet<WordEntry> result = getWordEntries(center, resultSize);

        return result;
    }

    private TreeSet<WordEntry> getWordEntries(float[] center, int resultSize) {
        TreeSet<WordEntry> result = new TreeSet<WordEntry>();

        double min = Float.MIN_VALUE;
        for (Map.Entry<String, float[]> entry : wordMap.entrySet()) {
            float[] vector = entry.getValue();
            float dist = 0;
            for (int i = 0; i < vector.length; i++) {
                dist += center[i] * vector[i];
            }

            if (dist > min) {
                result.add(new WordEntry(entry.getKey(), dist));
                if (resultSize < result.size()) {
                    result.pollLast();
                }
                min = result.last().score;
            }
        }
        result.pollFirst();
        return result;
    }

    private float[] sum(float[] center, float[] fs) {
        if (center == null && fs == null) {
            return null;
        }
        if (fs == null) {
            return center;
        }
        if (center == null) {
            return fs;
        }
        for (int i = 0; i < fs.length; i++) {
            center[i] += fs[i];
        }
        return center;
    }

    /**
     * 得到词向量
     *
     * @param word
     * @return
     */
    public float[] getWordVector(String word) {
        return wordMap.get(word);
    }

    public static float readFloat(InputStream is) throws IOException {
        byte[] bytes = new byte[4];
        is.read(bytes);
        return getFloat(bytes);
    }

    /**
     * 读取一个float
     *
     * @param b
     * @return
     */
    public static float getFloat(byte[] b) {
        int accum = 0;
        accum = accum | (b[0] & 0xff) << 0;
        accum = accum | (b[1] & 0xff) << 8;
        accum = accum | (b[2] & 0xff) << 16;
        accum = accum | (b[3] & 0xff) << 24;
        return Float.intBitsToFloat(accum);
    }

    /**
     * 读取一个字符串
     *
     * @param dis
     * @return
     * @throws IOException
     */
    private static String readString(DataInputStream dis) throws IOException {
        byte[] bytes = new byte[MAX_SIZE];
        byte b = dis.readByte();
        int i = -1;
        StringBuilder sb = new StringBuilder();
        while (b != 32 && b != 10) {
            i++;
            bytes[i] = b;
            b = dis.readByte();
            if (i == 49) {
                sb.append(new String(bytes));
                i = -1;
                bytes = new byte[MAX_SIZE];
            }
        }
        sb.append(new String(bytes, 0, i + 1));
        return sb.toString();
    }

    public int getTopNSize() {
        return topNSize;
    }

    public void setTopNSize(int topNSize) {
        this.topNSize = topNSize;
    }

    public HashMap<String, float[]> getWordMap() {
        return wordMap;
    }

    public int getWords() {
        return words;
    }

    public int getSize() {
        return size;
    }
}
