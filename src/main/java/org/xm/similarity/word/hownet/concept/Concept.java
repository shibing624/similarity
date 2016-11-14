package org.xm.similarity.word.hownet.concept;

import org.xm.similarity.word.hownet.IHownetMeta;

import java.util.*;

/**
 * 知网的概念表示类
 * <p>example和英文部分对于相似度的计算不起作用，考虑到内存开销， 在概念的表示中去掉了这部分数据的对应定义
 *
 * @author xuming
 */
public class Concept implements IHownetMeta {
    // 概念名称
    protected String word;
    // 词性 part of speech
    protected String pos;
    // 定义
    protected String define;
    // 实词(true),虚词(false)
    protected boolean bSubstantive;
    // 主基本义原
    protected String mainSememe;
    // 其他基本义原
    protected String[] secondSememes;
    // 关系义原
    protected String[] relationSememes;
    // 关系符号描述
    protected String[] symbolSememes;
    // 类型
    static String[][] concept_Type = {
            {"=", "事件"},
            {"aValue|属性值", "属性值"},
            {"qValue|数量值", "数量值"},
            {"attribute|属性", "属性"},
            {"quantity|数量", "数量"},
            {"unit|", "单位"},
            {"%", "部件"}
    };

    public Concept(String word, String pos, String define) {
        this.word = word;
        this.pos = pos;
        this.define = (define == null) ? "" : define.trim();

        // 虚词表示：{***}
        if (define.length() > 0 && define.charAt(0) == '{' && define.charAt(define.length() - 1) == '}') {
            this.bSubstantive = false;
        } else {
            this.bSubstantive = true;
        }
        initDefine();
    }

    private void initDefine() {
        List<String> secondList = new ArrayList<>();//求他基本义原
        List<String> relationList = new ArrayList<>();//关系义原
        List<String> symbolList = new ArrayList<>();//符号义原
        String tokenString = this.define;
        if (!this.bSubstantive) {//如果不是实词，则处理“{}”中的内容
            tokenString = define.substring(1, define.length() - 1);
        }
        StringTokenizer token = new StringTokenizer(tokenString, ",", false);

        if (token.hasMoreTokens()) {
            this.mainSememe = token.nextToken();
        }
        main_loop:
        while (token.hasMoreTokens()) {
            String item = token.nextToken();
            if (item.equals("")) continue;
            //判断符号义原
            String symbol = item.substring(0, 1);
            for (int i = 0; i < Symbol_Descriptions.length; i++) {
                if (symbol.equals(Symbol_Descriptions[i][0])) {
                    symbolList.add(item);
                    continue main_loop;
                }
            }
            //判断第二基本义原
            if (item.indexOf('=') > 0) {
                relationList.add(item);
            } else {
                secondList.add(item);
            }
        }
        this.secondSememes = secondList.toArray(new String[secondList.size()]);
        this.relationSememes = relationList.toArray(new String[relationList.size()]);
        this.symbolSememes = symbolList.toArray(new String[symbolList.size()]);
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public String getPos() {
        return pos;
    }

    public void setPos(String pos) {
        this.pos = pos;
    }

    public String getDefine() {
        return define;
    }

    public void setDefine(String define) {
        this.define = define;
    }

    public boolean isbSubstantive() {
        return bSubstantive;
    }

    public void setbSubstantive(boolean bSubstantive) {
        this.bSubstantive = bSubstantive;
    }

    public String getMainSememe() {
        return mainSememe;
    }

    public void setMainSememe(String mainSememe) {
        this.mainSememe = mainSememe;
    }

    public String[] getSecondSememes() {
        return secondSememes;
    }

    public void setSecondSememes(String[] secondSememes) {
        this.secondSememes = secondSememes;
    }

    public String[] getRelationSememes() {
        return relationSememes;
    }

    public void setRelationSememes(String[] relationSememes) {
        this.relationSememes = relationSememes;
    }

    public String[] getSymbolSememes() {
        return symbolSememes;
    }

    public void setSymbolSememes(String[] symbolSememes) {
        this.symbolSememes = symbolSememes;
    }

    public Set<String> getAllSememeNames() {
        Set<String> names = new HashSet<>();
        //主义原
        names.add(getMainSememe());
        //关系义原
        for (String item : getRelationSememes()) {
            names.add(item.substring(item.indexOf("=") + 1));
        }
        //符号义原
        for (String item : getSymbolSememes()) {
            names.add(item.substring(1));
        }
        //其他义原集合
        for (String item : getSecondSememes()) {
            names.add(item);
        }
        return names;
    }

    public String getType() {
        for (int i = 0; i < concept_Type.length; i++) {
            if (define.toUpperCase().indexOf(concept_Type[i][0].toUpperCase()) >= 0) {
                return concept_Type[i][1];
            }
        }
        return "普通";
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("名称=");
        sb.append(this.word);
        sb.append("; 词性=");
        sb.append(this.pos);
        sb.append("; 定义=");
        sb.append(this.define);
        sb.append("; 第一基本义元:[" + mainSememe);

        sb.append("]; 其他基本义元描述:[");
        for (String sem : secondSememes) {
            sb.append(sem);
            sb.append(";");
        }

        sb.append("]; [关系义元描述:");
        for (String sem : relationSememes) {
            sb.append(sem);
            sb.append(";");
        }

        sb.append("]; [关系符号描述:");
        for (String sem : symbolSememes) {
            sb.append(sem);
            sb.append(";");
        }
        sb.append("]");
        return sb.toString();
    }

    @Override
    public int hashCode() {
        return define == null ? word.hashCode() : define.hashCode();
    }

    @Override
    public boolean equals(Object object) {
        if (object instanceof Concept) {
            Concept c = (Concept) object;
            return word.equals(c.word) && define.equals(c.define);
        } else {
            return false;
        }
    }
}
