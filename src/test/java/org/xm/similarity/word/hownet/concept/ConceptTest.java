package org.xm.similarity.word.hownet.concept;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import org.junit.Test;

import java.util.Collection;

/**
 * @author xuming
 */
public class ConceptTest {
    @Test
    public void test() throws Exception {
        Multimap<String, Concept> CONCEPTS = HashMultimap.create();
        CONCEPTS.put("打", new Concept("打", "V", "TakeOutOfWater|捞起"));
        CONCEPTS.put("打", new Concept("打", "V", "TakeOutOfWater|捞起"));
        CONCEPTS.put("打", new Concept("打", "V", "TakeOutOfWater|捞起"));
        CONCEPTS.put("打", new Concept("打", "V", "TakeOutOfWater|捞起"));

        Collection<Concept> collection = CONCEPTS.get("打");
        for (Concept c : collection) {
            System.out.println(c);

        }

        Multimap<String, Integer> map = HashMultimap.create();
        map.put("打", 1);
        map.put("打", 1);
        map.put("打", 1);
        map.put("打", 2);

        Collection<Integer> cc = map.get("打");
        for (Integer i : cc) {
            System.out.println(i);
        }
    }

}