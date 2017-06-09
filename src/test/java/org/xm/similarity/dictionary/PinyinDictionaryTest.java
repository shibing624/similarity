package org.xm.similarity.dictionary;

import com.hankcs.hanlp.HanLP;
import com.hankcs.hanlp.dictionary.py.Pinyin;
import org.junit.Assert;
import org.junit.Test;
import org.xm.similarity.word.pinyin.PinyinDictionary;

import java.util.List;
import java.util.Set;

/**
 * @author xuming
 */
public class PinyinDictionaryTest {
    @Test
    public void getPinyin() throws Exception {
        Set<String> pinyinSet1 = PinyinDictionary.getInstance().getPinyin("教");
        Assert.assertTrue(pinyinSet1.size() > 1);
        pinyinSet1.forEach(System.out::println);
    }

    @Test
    public void getPinyin1() throws Exception {
        Set<String> pinyinSet1 = PinyinDictionary.getInstance().getPinyin("教师");
        Assert.assertTrue(pinyinSet1.size() > 1);
        pinyinSet1.forEach(System.out::println);
    }

    @Test
    public void getPinyinSingle() throws Exception {
        String pinyin = PinyinDictionary.getInstance().getPinyinSingle("教师");
        Assert.assertTrue(pinyin != null);
        System.out.println("教师：" + pinyin);

        // 胳臂
        String pinyin1 = PinyinDictionary.getInstance().getPinyinSingle("胳臂");
        System.out.println("胳臂:" + pinyin1);

        // 划船,计划
        System.out.println("划船:" + PinyinDictionary.getInstance().getPinyinSingle("划船"));
        System.out.println("计划:" + PinyinDictionary.getInstance().getPinyinSingle("计划"));
    }

    @Test
    public void getXmnlpPinyin() throws Exception {
        // 胳臂
        String pinyin1 = HanLP.convertToPinyinList("胳臂").toString();
        System.out.println("胳臂:" + pinyin1);

        // 划船,计划
        System.out.println("划船:" + HanLP.convertToPinyinList("划船").toString());
        List<Pinyin> pinyinList = HanLP.convertToPinyinList("计划");
        System.out.println("计划:" + pinyinList.toString());
    }

    @Test
    public void getPinyinString() throws Exception {
        String pinyin = PinyinDictionary.getInstance().getPinyinString("教师");
        System.out.println(pinyin);
    }

    @Test
    public void getPinyinHead() throws Exception {
        String pinyin = PinyinDictionary.getInstance().getPinyinHead("教师");
        System.out.println(pinyin);
    }

}