package org.xm.similarity.word.hownet.concept;

import java.util.LinkedList;

/**
 * 概念处理列
 *
 * @author xuming
 */
public class ConceptLinkedList extends LinkedList<Concept> {
    public void removeLast(int size) {
        for (int i = 0; i < size; i++) {
            removeLast();
        }
    }

    public void addByDefine(Concept concept) {
        for (Concept c : this) {
            if (c.getDefine().equals(concept.getDefine())) {
                return;
            }
        }
        add(concept);
    }
}
