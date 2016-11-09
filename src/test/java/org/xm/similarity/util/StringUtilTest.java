package org.xm.similarity.util;

import org.junit.Assert;
import org.junit.Test;

import java.util.List;

/**
 * @author xuming
 */
public class StringUtilTest {
    @Test
    public void isBlank() throws Exception {
        Assert.assertTrue(StringUtil.isBlank("     ") == true);
    }

    @Test
    public void isNotBlank() throws Exception {
        Assert.assertTrue(StringUtil.isNotBlank(" sdf  ") == true);

    }

    @Test
    public void matcherAll() throws Exception {
        List<String> list = StringUtil.matcherAll("\\[.*?\\]", "i[liu.lisi]loveyo[,][name.liming]u");
        System.out.println(list);
        for (String s : list) {
            System.out.println(s);
        }
    }

}