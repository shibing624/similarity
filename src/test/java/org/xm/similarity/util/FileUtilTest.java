package org.xm.similarity.util;

import org.junit.Test;

import java.io.File;

/**
 * @author xuming
 */
public class FileUtilTest {
    @Test
    public void fileTest() throws Exception {
        int count = 0;
        File dir = new File("C:/windows");
        for (File a : dir.listFiles()) {
            if (a != null && a.isFile())
                count++;
        }
        System.out.println(count);
    }

    @Test
    public void saveStringToFile() throws Exception {
        FileUtil.saveStringToFile("hi nihao .", "C:/temp/temp.txt");
    }

}