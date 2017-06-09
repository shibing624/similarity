package org.xm.similarity.word.hownet.sememe;

/**
 * 描述知网义原的基本对象
 * sememe cn="成功" define="{experiencer,scope}" en="succeed" id="1-1-2-1-4-5"/>
 * 义原的id表明了义原之间的上下位关系和义原的深度。
 *
 * @author xuming
 */
public class Sememe {
    // 义原编号
    private String id;
    // 中文名称
    private String cnWord;
    // 英文名称
    private String enWord;
    // 定义
    private String define;

    /**
     * 每一行的形式为：be|是 {relevant,isa}/{relevant,descriptive}
     * <br/>或者 official|官 [#organization|组织,#employee|员]
     * <br/>或者 amount|多少
     * <br/>把相应的部分赋予不同的属性
     * 出于性能考虑，把未用到的英文名称、定义等忽略
     *
     * @param id
     * @param en
     * @param cn
     * @param define
     */
    public Sememe(String id, String en, String cn, String define) {
        this.id = id;
        this.cnWord = cn;
        this.enWord = en;
        this.define = define;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCnWord() {
        return cnWord;
    }

    public void setCnWord(String cnWord) {
        this.cnWord = cnWord;
    }

    public String getEnWord() {
        return enWord;
    }

    public void setEnWord(String enWord) {
        this.enWord = enWord;
    }

    public String getDefine() {
        return define;
    }

    public void setDefine(String define) {
        this.define = define;
    }

    public int getType() {
        char c = id.charAt(0);
        switch (c) {
            case '1':
                return SememeType.Event;
            case '2':
                return SememeType.Entity;
            case '3':
                return SememeType.Attribute;
            case '4':
                return SememeType.Quantity;
            case '5':
                return SememeType.AValue;
            case '6':
                return SememeType.QValue;
            case '7':
                return SememeType.SecondaryFeature;
            case '8':
                return SememeType.Syntax;
            case '9':
                return SememeType.EventRoleAndFeature;
            default:
                return 0;
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("义原编号=");
        sb.append(id);
        sb.append(";中文名称=");
        sb.append(cnWord);
        sb.append(";英文名称=");
        sb.append(";定义=");
        sb.append(define);
        return sb.toString();
    }
}
