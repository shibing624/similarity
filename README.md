[![jitpack](https://jitpack.io/v/shibing624/similarity.svg)](https://jitpack.io/#shibing624/similarity)
[![Downloads](https://pepy.tech/badge/similarity)](https://pepy.tech/project/similarity)
[![License Apache 2.0](https://img.shields.io/badge/license-Apache%202.0-blue.svg)](LICENSE)
[![GitHub issues](https://img.shields.io/github/issues/shibing624/similarity.svg)](https://github.com/shibing624/similarity/issues)
[![Wechat Group](http://vlog.sfyc.ltd/wechat_everyday/wxgroup_logo.png?imageView2/0/w/60/h/20)](#Contact)

# similarity

用于词语、短语、句子、词法分析、情感分析、语义分析等相关的相似度计算。

**similarity**是由一系列算法组成的Java版相似度计算工具包，目标是传播自然语言处理中相似度计算方法。**similarity**具备工具实用、性能高效、架构清晰、语料时新、可自定义的特点。

------
# Feature

**similarity**提供下列功能：

- 词语相似度计算
  * 词林编码法相似度
  * 汉语语义法相似度
  * 知网词语相似度
  * 字面编辑距离法
  
- 短语相似度计算
  * 简单短语相似度
    
- 句子相似度计算
  * 词性和词序结合法
  * 编辑距离算法
  * Gregor编辑距离法
  * 优化编辑距离法
  
- 文本相似度计算
  * 余弦相似度
  * 编辑距离算法
  * 欧几里得距离
  * Jaccard相似性系数
  * Jaro距离
  * Jaro–Winkler距离
  * 曼哈顿距离
  * SimHash + 汉明距离
  * Sørensen–Dice系数

- 词法分析
  * 中文分词
  * 分词词性标注
  * 词频统计

- 知网义原
  * 义原树

- 情感分析
  * 正面倾向程度
  * 负面倾向程度
  * 情感倾向性
  
- 近似词
  * word2vec



在提供丰富功能的同时，**similarity**内部模块坚持低耦合、模型坚持惰性加载、词典坚持明文发布，使用方便，帮助用户训练自己的语料。



# Usage
## jar包

### jitpack库

```xml
<repositories>
  <repository>
    <id>jitpack.io</id>
    <url>https://jitpack.io</url>
  </repository>
</repositories>
```

```xml
<dependency>
  <groupId>com.github.shibing624</groupId>
  <artifactId>similarity</artifactId>
  <version>1.1.5</version>
</dependency>
```


### 导入包，使用示例：
```java
import org.xm.Similarity;
import org.xm.tendency.word.HownetWordTendency;

public class demo {
    public static void main(String[] args) {
        double result = Similarity.cilinSimilarity("电动车", "自行车");
        System.out.println(result);

        String word = "混蛋";
        HownetWordTendency hownetWordTendency = new HownetWordTendency();
        result = hownetWordTendency.getTendency(word);
        System.out.println(word + "  词语情感趋势值：" + result);
    }
}
```

## 功能演示

### word similarity
demo code: [src/test/java/org.xm/WordSimilarityDemo.java](src/test/java/org/xm/WordSimilarityDemo.java)
```java
package org.xm;

public class WordSimilarityDemo {

    public static void main(String[] args) {
        String word1 = "教师";
        String word2 = "教授";
        double cilinSimilarityResult = Similarity.cilinSimilarity(word1, word2);
        double pinyinSimilarityResult = Similarity.pinyinSimilarity(word1, word2);
        double conceptSimilarityResult = Similarity.conceptSimilarity(word1, word2);
        double charBasedSimilarityResult = Similarity.charBasedSimilarity(word1, word2);

        System.out.println(word1 + " vs " + word2 + " 词林相似度值：" + cilinSimilarityResult);
        System.out.println(word1 + " vs " + word2 + " 拼音相似度值：" + pinyinSimilarityResult);
        System.out.println(word1 + " vs " + word2 + " 概念相似度值：" + conceptSimilarityResult);
        System.out.println(word1 + " vs " + word2 + " 字面相似度值：" + charBasedSimilarityResult);
    }
}
```

* result:

![word_sim result](./data/pic/word_sim.png)

### phrase similarity

demo code : [src/test/java/org.xm/PhraseSimilarityDemo.java](src/test/java/org/xm/PhraseSimilarityDemo.java)
```java
public static void main(String[] args) {
    String phrase1 = "继续努力";
    String phrase2 = "持续发展";
    double result = Similarity.phraseSimilarity(phrase1, phrase2);

    System.out.println(phrase1 + " vs " + phrase2 + " 短语相似度值：" + result);
}
```


* result:

![phrase sim result](./data/pic/phrase_sim.png)

### sentence similarity
demo code : [src/test/java/org.xm/SentenceSimilarityDemo.java](src/test/java/org/xm/SentenceSimilarityDemo.java)

```java
public static void main(String[] args) {
    String sentence1 = "中国人爱吃鱼";
    String sentence2 = "湖北佬最喜吃鱼";

    double morphoSimilarityResult = Similarity.morphoSimilarity(sentence1, sentence2);
    double editDistanceResult = Similarity.editDistanceSimilarity(sentence1, sentence2);
    double standEditDistanceResult = Similarity.standardEditDistanceSimilarity(sentence1,sentence2);
    double gregeorEditDistanceResult = Similarity.gregorEditDistanceSimilarity(sentence1,sentence2);

    System.out.println(sentence1 + " vs " + sentence2 + " 词形词序句子相似度值：" + morphoSimilarityResult);
    System.out.println(sentence1 + " vs " + sentence2 + " 优化的编辑距离句子相似度值：" + editDistanceResult);
    System.out.println(sentence1 + " vs " + sentence2 + " 标准编辑距离句子相似度值：" + standEditDistanceResult);
    System.out.println(sentence1 + " vs " + sentence2 + " gregeor编辑距离句子相似度值：" + gregeorEditDistanceResult);
}
```

* result:

![sentence sim result](./data/pic/sentence_sim.png)


### text similarity

demo code : [src/test/java/org.xm/similarity/text/CosineSimilarityTest.java](src/test/java/org/xm/similarity/text/CosineSimilarityTest.java)


```java
@Test
public void getSimilarityScore() throws Exception {
    String text1 = "我爱购物";
    String text2 = "我爱读书";
    String text3 = "他是黑客";
    TextSimilarity similarity = new CosineSimilarity();
    double score1pk2 = similarity.getSimilarity(text1, text2);
    double score1pk3 = similarity.getSimilarity(text1, text3);
    double score2pk2 = similarity.getSimilarity(text2, text2);
    double score2pk3 = similarity.getSimilarity(text2, text3);
    double score3pk3 = similarity.getSimilarity(text3, text3);
    System.out.println(text1 + " 和 " + text2 + " 的相似度分值：" + score1pk2);
    System.out.println(text1 + " 和 " + text3 + " 的相似度分值：" + score1pk3);
    System.out.println(text2 + " 和 " + text2 + " 的相似度分值：" + score2pk2);
    System.out.println(text2 + " 和 " + text3 + " 的相似度分值：" + score2pk3);
    System.out.println(text3 + " 和 " + text3 + " 的相似度分值：" + score3pk3);

}
```


* result:

![cos text result](./data/pic/cos_txt.png)

### word frequency statistics
demo code : [src/test/java/org/xm/tokenizer/WordFreqStatisticsTest.java](src/test/java/org/xm/tokenizer/WordFreqStatisticsTest.java)


* result:

![word freq result](./data/pic/freq.png)

分词及词性标注内置调用[HanLP](https://github.com/hankcs/HanLP)，也可以使用NLPchina的[ansj_seg](https://github.com/NLPchina/ansj_seg)分词工具。


### sentiment analysis based on words
demo code : [src/test/java/org/xm/tendency/word/HownetWordTendencyTest.java](src/test/java/org/xm/tendency/word/HownetWordTendencyTest.java)

```java
@Test
public void getTendency() throws Exception {
    HownetWordTendency hownet = new HownetWordTendency();
    String word = "美好";
    double sim = hownet.getTendency(word);
    System.out.println(word + ":" + sim);
    System.out.println("混蛋:" + hownet.getTendency("混蛋"));
}
```


* result:

![tendency result](./data/pic/tendency.png)

本例是基于义原树的词语粒度情感极性分析，关于文本情感分析有[text-classifier](https://github.com/shibing624/text-classifier)，利用深度神经网络模型、SVM分类算法实现的效果更好。

### homoionym(use word2vec)
demo code : [src/test/java/org/xm/word2vec/Word2vecTest.java](src/test/java/org/xm/word2vec/Word2vecTest.java)

```java
@Test
public void testHomoionym() throws Exception {
    List<String> result = Word2vec.getHomoionym(RAW_CORPUS_SPLIT_MODEL, "武功", 10);
    System.out.println("武功 近似词：" + result);
}

@Test
public void testHomoionymName() throws Exception {
    String model = RAW_CORPUS_SPLIT_MODEL;
    List<String> result = Word2vec.getHomoionym(model, "乔帮主", 10);
    System.out.println("乔帮主 近似词：" + result);

    List<String> result2 = Word2vec.getHomoionym(model, "阿朱", 10);
    System.out.println("阿朱 近似词：" + result2);

    List<String> result3 = Word2vec.getHomoionym(model, "少林寺", 10);
    System.out.println("少林寺 近似词：" + result3);
}
```

* train:

![word2vec train](./data/pic/word2v.png)

* result:

![word2vec result](./data/pic/word2v_ret.png)

训练词向量使用的是阿健实现的java版word2vec训练工具[Word2VEC_java](https://github.com/NLPchina/Word2VEC_java)，训练语料是小说天龙八部，通过词向量实现得到近义词。
用户可以训练自定义语料，也可以用中文维基百科训练通用词向量。

## Todo

文本相似性度量

- [x] 关键词匹配（TF-IDF、BM25）
- [x] 浅层语义匹配（WordEmbed隐语义模型，用word2vec或glove词向量直接累加构造的句向量）
- [ ] 深度语义匹配模型（DSSM、CLSM、DeepMatch、MatchingFeatures、ARC-II、DeepMind，具体依次参考下面的Reference）

欢迎大家贡献代码及思路，完善本项目

# Contact

- Issue(建议)：[![GitHub issues](https://img.shields.io/github/issues/shibing624/similarity.svg)](https://github.com/shibing624/similarity/issues)
- 邮件我：xuming: xuming624@qq.com
- 微信我：
  加我*微信号：xuming624, 备注：个人名称-公司-NLP* 进NLP交流群。

<img src="docs/wechat.jpeg" width="200" />


# Citation

如果你在研究中使用了*similarity*，请按如下格式引用：

```latex
@misc{similarity,
  title={similarity: A Tool for Compute Text Similarity Score},
  author={Ming Xu},
  howpublished={https://github.com/shibing624/similarity},
  year={2022}
}
```

# License


授权协议为 [The Apache License 2.0](/LICENSE)，可免费用做商业用途。请在产品说明中附加*similarity*的链接和授权协议。


# Contribute
项目代码还很粗糙，如果大家对代码有所改进，欢迎提交回本项目，在提交之前，注意以下两点：

- 在`test`添加相应的单元测试
- 运行所有单元测试，确保所有单测都是通过的

之后即可提交PR。

# Reference

* [DSSM] Po-Sen Huang, et al., 2013, Learning Deep Structured Semantic Models for Web Search using Clickthrough Data
* [CLSM] Yelong Shen, et al, 2014, A Latent Semantic Model with Convolutional-Pooling Structure for Information Retrieval 
* [DeepMatch] Zhengdong Lu & Hang Li, 2013, A Deep Architecture for Matching Short Texts
* [MatchingFeatures] Zongcheng Ji, et al., 2014, An Information Retrieval Approach to Short Text Conversation
* [ARC-II] Baotian Hu, et al., 2015, Convolutional Neural Network Architectures for Matching Natural Language Sentences
* [DeepMind] Aliaksei Severyn, et al., 2015, Learning to Rank Short Text Pairs with Convolutional Deep Neural Networks

