package org.xm.similarity.word.pinyin;

import org.xm.Similarity;
import org.xm.similarity.util.DicReader;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 查找汉字对应的拼音工具
 *
 * @author xuming
 */
public class PinyinDictionary {
    private Map<Character, Set<String>> pinyinDict;
    private static PinyinDictionary instance;
    private static final String path =  Similarity.Config.PinyinPath;

    private PinyinDictionary() throws IOException {
        BufferedReader br = DicReader.getReader(path);
        String line;
        Event event = new Event();
        while ((line = br.readLine()) != null) {
            event.visit(line);
        }
        br.close();
        this.pinyinDict = event.getPinyins();
    }

    public static PinyinDictionary getInstance() {
        if (instance == null) {
            try {
                instance = new PinyinDictionary();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return instance;
    }

    /**
     * 获取汉字的拼音, 由于汉字具有多音字，故返回一个集合
     *
     * @param c
     * @return
     */
    public Set<String> getPinyin(Character c) {
        Set<String> set = pinyinDict.get(c);
        if (set == null || set.size() == 0) {
            set = new HashSet<>();
            set.add(c.toString());
        }
        return set;
    }

    /**
     * 获取词语的拼音, 一个词语可能对应多个拼音，把所有可能的组合放到集合中返回
     *
     * @param word
     * @return
     */
    public Set<String> getPinyin(String word) {
        Set<String> set = new HashSet<>();
        for (int i = 0; i < word.length(); i++) {
            Set<String> pinyinSet = getPinyin(word.charAt(i));
            if (set == null || set.size() == 0) {
                set.addAll(pinyinSet);
                continue;
            }
            Set<String> tempSet = new HashSet<>();
            for (String s : set) {
                tempSet.addAll(pinyinSet.stream().map(p -> s + p).collect(Collectors.toList()));
            }
            set = tempSet;
        }
        return set;
    }

    /**
     * 获取拼音字符串，多音字只取一个
     *
     * @param word
     * @return
     */
    public String getPinyinSingle(String word) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < word.length(); i++) {
            sb.append(getPinyin(word.charAt(i)).iterator().next());
        }
        return sb.toString();
    }

    /**
     * 获取拼音串，对于多音字，给出所有拼音
     *
     * @param word
     * @return
     */
    public String getPinyinString(String word) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < word.length(); i++) {
            Set<String> pinyin = getPinyin(word.charAt(i));
            sb.append(pinyin.toString());
        }
        return sb.toString();
    }

    /**
     * 获取拼音首字母
     *
     * @param word
     * @return
     */
    public String getPinyinHead(String word) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < word.length(); i++) {
            sb.append(getPinyin(word.charAt(i)).iterator().next().charAt(0));
        }
        return sb.toString();
    }

    private static class Event {
        private Map<Character, Set<String>> pinyins;

        public Event() {
            this.pinyins = new HashMap<>();
        }

        public Map<Character, Set<String>> getPinyins() {
            return pinyins;
        }

        public boolean visit(String str) {
            if (str.startsWith("//")) {
                return true;
            }
            char c = str.charAt(0);
            String pinyin = str.substring(2, str.length());
            Set<String> set = pinyins.get(c);
            if (set == null) {
                set = new HashSet<>();
            }
            set.add(pinyin);
            pinyins.put(c, set);
            return true;
        }
    }
}
