package org.xm.similarity.dictionary;

import org.junit.Before;
import org.junit.Test;
import org.xm.similarity.word.clin.CilinDictionary;

/**
 * @author xuming
 */
public class CilinDictionaryTest {
    String code = "";
    CilinDictionary dictionary;

    @Before
    public void init() throws Exception {
        dictionary = CilinDictionary.getInstance();
        code = dictionary.getCilinCodes("爱情").iterator().next();
    }

    @Test
    public void getCilinCodes() throws Exception {
        System.out.println("爱情 code:" + code);
    }

    @Test
    public void getCilinWords() throws Exception {
        System.out.println("爱情 code:" + code + "\t" + dictionary.getCilinWords(code));
    }

}