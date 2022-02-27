package org.xm.similarity.text;

import org.junit.Test;

/**
 * @author xuming
 */
public class EditDistanceSimilarityTest {
    @Test
    public void getSimilarityImpl() throws Exception {
        String text1 = "对于俄罗斯来说，最大的战果莫过于夺取乌克兰首都基辅，也就是现任总统泽连斯基和他政府的所在地。目前夺取基辅的战斗已经打响。";
        String text2 = "迄今为止，俄罗斯的入侵似乎没有完全按计划成功执行——英国国防部情报部门表示，在乌克兰军队激烈抵抗下，俄罗斯军队已经损失数以百计的士兵。尽管如此，俄军在继续推进。";

        TextSimilarity textSimilarity = new EditDistanceSimilarity();
        double score1pk2 = textSimilarity.getSimilarity(text1, text2);
        System.out.println("edit相似度分值：" + score1pk2);
    }

}