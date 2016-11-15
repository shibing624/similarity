package org.xm.tendency.word;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author xuming
 */
public class HownetWordTendencyTest {
    @Test
    public void getTendency() throws Exception {
        HownetWordTendency hownet = new HownetWordTendency();
        double sim = hownet.getTendency("喜悦");
        Assert.assertTrue(sim>0);
        System.out.println(sim);
    }

}