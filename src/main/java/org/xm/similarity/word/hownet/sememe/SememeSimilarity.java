package org.xm.similarity.word.hownet.sememe;

import org.xm.similarity.util.StringUtil;

import java.io.IOException;
import java.util.Collection;

/**
 * 义原相似度计算
 *
 * @author xuming
 */
public class SememeSimilarity extends SememeParser {

    public SememeSimilarity() throws IOException {
        super();
    }

    /**
     * 计算两个义原的相似度
     */
    double getSimilarityBySememeId(final String id1, final String id2) {

        int position = 0;
        String[] array1 = id1.split("-");
        String[] array2 = id2.split("-");
        for (position = 0; position < array1.length && position < array2.length; position++) {
            if (!array1[position].equals(array2[position])) {
                break;
            }
        }

        return 2.0 * position / (array1.length + array2.length);
    }

    /**
     * 根据汉语定义计算义原之间的相似度，由于可能多个义元有相同的汉语词语，故计算结果为其中相似度最大者
     *
     * @return
     */
    public double getMaxSimilarity(String sememeName1, String sememeName2) {
        double maxValue = 0.0;

        // 如果两个字符串相等，直接返回距离为0
        if (sememeName1.equals(sememeName2)) {
            return 1.0;
        }
        Collection<String> sememeIds1 = SEMEMES.get(sememeName1);
        Collection<String> sememeIds2 = SEMEMES.get(sememeName2);
        // 如果sememe1或者sememe2不是义元,则返回0
        if (sememeIds1.size() == 0 || sememeIds1.size() == 0) {
            return 0.0;
        }

        for (String id1 : sememeIds1) {
            for (String id2 : sememeIds2) {
                double value = getSimilarityBySememeId(id1, id2);
                if (value > maxValue) {
                    maxValue = value;
                }
            }
        }

        return maxValue;
    }

    /**
     * 计算两个义元之间的相似度，由于义元可能相同，计算结果为其中相似度最大者 similarity = alpha/(distance+alpha),
     * 如果两个字符串相同或都为空，直接返回1.0
     */
    @Override
    public double getSimilarity(String item1, String item2) {
        if (StringUtil.isBlankAll(item2, item2)) {
            return 1.0;
        } else if (StringUtil.isBlankAtLeastOne(item1, item2)) {
            return 0.0;
        } else if (item1.equals(item2)) {
            return 1.0;
        }

        String key1 = item1.trim();
        String key2 = item2.trim();

        // 去掉()符号
        if ((key1.charAt(0) == '(') && (key1.charAt(key1.length() - 1) == ')')) {

            if (key2.charAt(0) == '(' && key2.charAt(key2.length() - 1) == ')') {
                key1 = key1.substring(1, key1.length() - 1);
                key2 = key2.substring(1, key2.length() - 1);
            } else {
                return 0.0;
            }

        }

        // 处理关系义元,即x=y的情况
        int pos = key1.indexOf('=');
        if (pos > 0) {
            int pos2 = key2.indexOf('=');
            // 如果是关系义元，则判断前面部分是否相同，如果相同，则转为计算后面部分的相似度，否则为0
            if ((pos == pos2) && key1.substring(0, pos).equals(key2.substring(0, pos2))) {
                key1 = key1.substring(pos + 1);
                key2 = key2.substring(pos2 + 1);
            } else {
                return 0.0;
            }
        }

        // 处理符号义元,即前面有特殊符号的义元
        String symbol1 = key1.substring(0, 1);
        String symbol2 = key2.substring(0, 1);

        for (int i = 0; i < Symbol_Descriptions.length; i++) {
            if (symbol1.equals(Symbol_Descriptions[i][0])) {
                if (symbol1.equals(symbol2)) {
                    key1 = item1.substring(1);
                    key2 = item2.substring(1);
                    break;
                } else {
                    return 0.0; // 如果不是同一关系符号，则相似度直接返回0
                }
            }
        }

        if ((pos = key1.indexOf("|")) >= 0) {
            key1 = key1.substring(pos + 1);
        }
        if ((pos = key2.indexOf("|")) >= 0) {
            key2 = key2.substring(pos + 1);
        }

        // 如果两个字符串相等，直接返回距离为0
        if (key1.equals(key2)) {
            return 1.0;
        }

        return getMaxSimilarity(key1, key2);
    }
}
