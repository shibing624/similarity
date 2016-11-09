package org.xm.similarity.dictionary;

/**
 * 遍历接口
 * @author xuming
 */
public interface TraverseEvent<T> {
    /**
     * 遍历每一个
     * @param item
     * @return
     */
    boolean visit(T item);
}
