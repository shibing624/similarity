package org.xm.similarity.util;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 字符串处理工具类
 *
 * @author xuming
 */
public class StringUtil {
    private static final String EMPTY = "";
    private static final String NULL = "null";

    /**
     * 判断字符串是否为空
     *
     * @param cs
     * @return
     */
    public static boolean isBlank(CharSequence cs) {
        int strLen;
        if (cs == null || (strLen = cs.length()) == 0) {
            return true;
        }
        for (int i = 0; i < strLen; i++) {
            if (Character.isWhitespace(cs.charAt(i)) == false) {
                return false;
            }
        }
        return true;
    }

    /**
     * 判断字符串是否不为空
     *
     * @param cs
     * @return
     */
    public static boolean isNotBlank(CharSequence cs) {
        return !isBlank(cs);

    }

    /**
     * 判断字符串s是否是空串
     *
     * @return
     */
    public static boolean isBlank(String string) {
        return string == null || string.trim().equals("");
    }

    /**
     * 判断数组是否是空
     *
     * @param array
     * @return
     */
    public static boolean isBlank(Object[] array) {
        return array == null || array.length == 0;
    }

    /**
     * 判断集合是否是空
     *
     * @param array
     * @return
     */
    public static boolean isBlank(Collection<? extends Object> array) {
        return array == null || array.size() == 0;
    }

    /**
     * 判断集合
     *
     */
    public static boolean isNotBlank(Collection<? extends Object> array) {
        return !isBlank(array);

    }
    /**
     * 判断所有的集合是否都为空
     *
     * @param collections
     * @return
     */
    public static boolean isBlankAll(Collection<?>... collections) {
        for (Collection<?> c : collections) {
            if (!isBlank(c)) {
                return false;
            }
        }

        return true;
    }

    /**
     * 判断字符串strings中是否都是空串
     *
     * @param strings
     * @return
     */
    public static boolean isBlankAll(String... strings) {
        for (String s : strings) {
            if (!isBlank(s)) {
                return false;
            }
        }

        return true;
    }

    /**
     * 判断collections集合中是否至少有一个为空
     *
     * @param collections
     * @return
     */
    public static boolean isBlankAtLeastOne(Collection<?>... collections) {
        for (Collection<?> c : collections) {
            if (isBlank(c)) {
                return true;
            }
        }

        return false;
    }

    /**
     * 判断字符串strings中是否之首有一个为空
     *
     * @param strings
     * @return
     */
    public static boolean isBlankAtLeastOne(String... strings) {
        for (String s : strings) {
            if (isBlank(s)) {
                return true;
            }
        }

        return false;
    }

    /**
     * 将一个字符串.转换成排序后的字符数组
     *
     * @param str
     * @return
     */
    public static char[] sortCharArray(String str) {
        char[] chars = str.toCharArray();
        Arrays.sort(chars);
        return chars;
    }

    public static String joiner(int[] ints, String split) {

        if (ints.length == 0) {
            return EMPTY;
        }

        StringBuilder sb = new StringBuilder(String.valueOf(ints[0]));

        for (int i = 1; i < ints.length; i++) {
            sb.append(split);
            sb.append(ints[i]);
        }

        return sb.toString();
    }

    public static String joiner(double[] doubles, String split) {

        if (doubles.length == 0) {
            return EMPTY;
        }

        StringBuilder sb = new StringBuilder(String.valueOf(doubles[0]));

        for (int i = 1; i < doubles.length; i++) {
            sb.append(split);
            sb.append(doubles[i]);
        }

        return sb.toString();
    }

    public static String joiner(float[] floats, String split) {

        if (floats.length == 0) {
            return EMPTY;
        }

        StringBuilder sb = new StringBuilder(String.valueOf(floats[0]));

        for (int i = 1; i < floats.length; i++) {
            sb.append(split);
            sb.append(floats[i]);
        }

        return sb.toString();
    }

    public static String joiner(long[] longs, String split) {

        if (longs.length == 0) {
            return EMPTY;
        }

        StringBuilder sb = new StringBuilder(String.valueOf(longs[0]));

        for (int i = 1; i < longs.length; i++) {
            sb.append(split);
            sb.append(longs[i]);
        }

        return sb.toString();
    }

    public static String toString(Object obj) {
        if (obj == null) {
            return NULL;
        } else {
            return obj.toString();
        }
    }

    public static String joiner(Collection<?> c, String split) {

        Iterator<?> iterator = c.iterator();

        if (!iterator.hasNext()) {
            return EMPTY;
        }

        StringBuilder sb = new StringBuilder(iterator.next().toString());

        while (iterator.hasNext()) {
            sb.append(split);
            sb.append(toString(iterator.next()).toString());
        }

        return sb.toString();
    }

    public static boolean isBlank(char[] chars) {
        // TODO Auto-generated method stub
        int strLen;
        if (chars == null || (strLen = chars.length) == 0) {
            return true;
        }
        for (int i = 0; i < strLen; i++) {
            if (Character.isWhitespace(chars[i]) == false) {
                return false;
            }
        }
        return true;
    }

    /**
     * 正则匹配第一个
     *
     * @param regex
     * @param input
     * @return
     */
    public static String matcherFirst(String regex, String input) {
        Matcher matcher = Pattern.compile(regex).matcher(input); // 读取特征个数
        if (matcher.find()) {
            return input.substring(matcher.start(), matcher.end());
        } else {
            return null;
        }
    }

    /**
     * trim 一个字符串.扩展了string类原生的trim.对BOM和中文空格进行trim
     *
     * @return
     */
    public static String trim(String value) {

        if (value == null) {
            return value;
        }

        int len = value.length();

        int st = 0;

        while ((st < len) && (Character.isWhitespace(value.charAt(st)) || value.charAt(st) == 65279 || value.charAt(st) == 160 || value.charAt(st) == 12288)) {
            st++;
        }
        while ((st < len) && (Character.isWhitespace(value.charAt(len - 1)) || value.charAt(st) == 160 || value.charAt(st) == 12288)) {
            len--;
        }
        return ((st > 0) || (len < value.length())) ? value.substring(st, len) : value;
    }

    /**
     * 正则匹配全部
     *
     * @param regex
     * @param input
     * @return
     */
    public static List<String> matcherAll(String regex, String input) {
        List<String> result = new ArrayList<String>();
        Matcher matcher = Pattern.compile(regex).matcher(input); // 读取特征个数
        while (matcher.find()) {
            result.add(input.substring(matcher.start(), matcher.end()));
        }
        return result;
    }

    /**
     * 正则匹配全部结果
     *
     * @param regex
     * @param input
     * @return
     */
    public static Map<Integer, String> matcherAll2Map(String regex, String input) {
        Map<Integer, String> result = new HashMap<Integer, String>();
        Matcher matcher = Pattern.compile(regex).matcher(input);
        for (int i = 0; matcher.find(); i++) {
            result.put(i, matcher.group());
        }
        return result;
    }

    /**
     * 正则匹配最后
     *
     * @param regex
     * @param input
     * @return
     */
    public static String matcherLast(String regex, String input) {
        List<String> result = matcherAll(regex, input);
        if (result.size() == 0) {
            return null;
        } else {
            return result.get(result.size() - 1);
        }
    }


    public static String getLongString(String word1, String word2) {
        return word1.length() >= word2.length() ? word1 : word2;
    }

    public static String getShortString(String word1, String word2) {
        return word1.length() < word2.length() ? word1 : word2;
    }

}
