package org.xm.tendency.word;

import org.junit.Test;

/**
 * @author xuming
 */
public class HownetWordTendencyTest {
    @Test
    public void getTendency() throws Exception {
        HownetWordTendency hownet = new HownetWordTendency();
        double sim = hownet.getTendency("流氓");
        System.out.println(sim);
    }

}