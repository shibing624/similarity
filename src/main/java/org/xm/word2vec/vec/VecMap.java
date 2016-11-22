package org.xm.word2vec.vec;

import java.util.HashMap;

/**
 * 自定义的hashmap
 *
 * @author xuming
 */
public class VecMap<T> {
    private HashMap<T, Integer> hm = null;

    public VecMap() {
        this.hm = new HashMap<T, Integer>();
    }

    public VecMap(int size) {
        this.hm = new HashMap<T, Integer>(size);
    }

    public void add(T t, int n) {
        Integer integer;
        if ((integer = this.hm.get(t)) != null) {
            this.hm.put(t, Integer.valueOf(integer.intValue() + n));
        } else {
            this.hm.put(t, Integer.valueOf(n));
        }
    }

    public void add(T t) {
        this.add(t, 1);
    }

    public int size() {
        return hm.size();
    }

    public void remove(T t) {
        this.hm.remove(t);
    }

    public HashMap<T, Integer> getHm() {
        return hm;
    }

    public void setHm(HashMap<T, Integer> hm) {
        this.hm = hm;
    }
}
