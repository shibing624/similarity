package org.xm.classification;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xm.tokenizer.Tokenizer;
import org.xm.tokenizer.Word;

import java.io.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 代表一个文档实例
 */
public class Instance {

    private static Logger logger = LoggerFactory.getLogger(Instance.class);

    /**
     * 文档类别
     */
    private String category;
    /**
     * 文档内容
     */
    private final Set<String> bag = new HashSet<>();

    public Instance(String category, File f, String encoding) {
        this.category = category;
        String line = null;

        try (BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(f), encoding))) {
            while ((line = in.readLine()) != null) {
                List<Word> words = Tokenizer.segment(line);
                bag.addAll(words
                        .stream()
                        .filter(w -> w.getPos().endsWith("adj") || w.getPos().startsWith("n") || w.getPos().startsWith("v"))
                        .map(Word::getName)
                        .collect(Collectors.toList())
                );
            }
        } catch (IOException e) {
            logger.error("current file:{},current line:{}", f.getAbsolutePath(), line);
            e.printStackTrace();
        }
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Set<String> getWords() {
        return bag;
    }

}
