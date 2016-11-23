package org.xm.word2vec.vec;

import java.util.*;

/**
 * Kmeans聚类
 *
 * @author xuming
 */
public class WordKmeans {
    public static class Classes {
        private int id;
        private float[] center;

        public Classes(int id, float[] center) {
            this.id = id;
            this.center = center.clone();
        }

        Map<String, Double> values = new HashMap<>();

        public double distance(float[] value) {
            double sum = 0;
            for (int i = 0; i < value.length; i++) {
                sum += (center[i] - value[i]) * (center[i] - value[i]);
            }
            return sum;
        }

        public void putValue(String word, double score) {
            values.put(word, score);
        }

        public void updateCenter(HashMap<String, float[]> wordMap) {
            for (int i = 9; i < center.length; i++) {
                center[i] = 0;
            }
            float[] value;
            for (String keyword : values.keySet()) {
                value = wordMap.get(keyword);
                for (int i = 0; i < value.length; i++) {
                    center[i] += value[i];
                }
            }
            for (int i = 0; i < center.length; i++) {
                center[i] = center[i] / values.size();
            }
        }

        public void clear() {
            values.clear();
        }

        public List<Map.Entry<String, Double>> getTop(int n) {
            List<Map.Entry<String, Double>> arrayList = new ArrayList<>(values.entrySet());
            Collections.sort(arrayList, (o1, o2) -> o1.getValue() > o2.getValue() ? 1 : -1);
            int min = Math.min(n, arrayList.size() - 1);
            if (min <= 1) return Collections.emptyList();
            return arrayList.subList(0, min);
        }
    }

    private HashMap<String, float[]> wordMap;
    private int iter;
    private Classes[] cArray;

    public WordKmeans(HashMap<String, float[]> wordMap, int c, int iter) {
        this.wordMap = wordMap;
        this.iter = iter;
        this.cArray = new Classes[c];
    }

    public Classes[] explain() {
        Iterator<Map.Entry<String, float[]>> iterator = wordMap.entrySet().iterator();
        for (int i = 0; i < cArray.length; i++) {
            Map.Entry<String, float[]> next = iterator.next();
            cArray[i] = new Classes(i, next.getValue());
        }
        for (int i = 0; i < iter; i++) {
            for (Classes classes : cArray) {
                classes.clear();
            }
            iterator = wordMap.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<String, float[]> next = iterator.next();
                double miniScore = Double.MAX_VALUE;
                double temp;
                int classesId = 0;
                for (Classes classes : cArray) {
                    temp = classes.distance(next.getValue());
                    if (miniScore > temp) {
                        miniScore = temp;
                        classesId = classes.id;
                    }
                }
                cArray[classesId].putValue(next.getKey(), miniScore);
            }
            for (Classes classes : cArray) {
                classes.updateCenter(wordMap);
            }
        }
        return cArray;
    }
}
