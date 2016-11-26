package org.xm.similarity.text;

import org.xm.similarity.ISimilarity;

/**
 * @author xuming
 */
public interface ITextSimilarity extends ISimilarity{
    // 阈值
    float thresholdRate = 0.5f;

    double getSimilarity();


}
