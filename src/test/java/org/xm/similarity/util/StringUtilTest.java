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

    String[] strs = {"hi", "yourname", "qingji", "dedao"};

    private String toString(String[] details) {
        StringBuilder buffer = new StringBuilder();
        for (int i = 0; i < details.length; i++) {
            buffer.append("  ");
            buffer.append(details[i]);
        }
        buffer.append("\n");

        return buffer.toString();
    }

    @Test
    public void getTest() {
        System.out.println(toString(strs));
    }
}