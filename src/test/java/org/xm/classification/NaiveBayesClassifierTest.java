package org.xm.classification;

import org.junit.Test;

import java.io.File;

/**
 * @author xuming
 */
public class NaiveBayesClassifierTest {
    @Test
    public void test() throws Exception {
        NaiveBayesClassifier classifier = new NaiveBayesClassifier();

        File samplePath = new File("data/test/testcorpus");
        for (File categoryPath : samplePath.listFiles()) {
            String category = categoryPath.getName();
            for (File f : categoryPath.listFiles()) {
                classifier.training(new Instance(category, f, "UTF-8"));
            }
        }
        classifier.save(new File("result.dat"));
        System.out.println("Finished!");

        classifier.load(new File("result.dat"));

        Instance doc = new Instance(null, new File("data/test/shanxiuniversity-part.txt"), "UTF-8");
        System.out.println("get category:" + classifier.getCategory(doc));

    }

}