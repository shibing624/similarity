package org.xm.word2vec;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xm.word2vec.domain.WordEntry;
import org.xm.word2vec.vec.Learn;
import org.xm.word2vec.vec.ModelParser;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Word2vec java版 工具包
 *
 * @author xuming
 */
public class Word2vec {
    private static final Logger logger = LoggerFactory.getLogger(Word2vec.class);

    public static String trainModel(String inputfilePath, String modelPath) throws IOException {
        File inputfile = new File(inputfilePath);
        if (inputfile == null) {
            return "";
        }
        String out = inputfile.getPath() + ".model";
        if (modelPath != null && modelPath.trim().length() > 0) out = modelPath;
        File outFile = new File(out);
        Learn learn = new Learn();
        learn.learnFile(inputfile);
        learn.saveModel(outFile);
        return out;
    }

    public static float[] parser(String modelPath, String word) throws IOException {
        ModelParser parser = new ModelParser();
        parser.loadModel(modelPath);
        float[] result = parser.getWordVector(word);
        return result;
    }

    public static List<String> getHomoionym(String modelPath, String word, int topN) throws IOException {
        List<String> result = new ArrayList<>(topN);
        ModelParser parser = new ModelParser();
        parser.loadModel(modelPath);
        parser.setTopNSize(topN);
        Set<WordEntry> wordEntrySet = parser.distance(word);
        result.addAll(wordEntrySet.stream().map(wordEntry -> wordEntry.name).collect(Collectors.toList()));
        return result;
    }
}
