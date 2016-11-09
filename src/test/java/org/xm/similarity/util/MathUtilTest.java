package org.xm.similarity.util;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author xuming
 */
public class MathUtilTest {
    @Test
    public void min() throws Exception {
        Assert.assertTrue(MathUtil.min(1, 2, 3) < 2);
        System.out.println("1 vs 2 vs 3 , min:" + MathUtil.min(1,2, 3));
    }

    @Test
    public void max() throws Exception {
        Assert.assertTrue(MathUtil.max(2, 3) > 2);
        System.out.println("2 vs 3 , max:" + MathUtil.max(2, 3));
    }

}