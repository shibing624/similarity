package org.xm.classification;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * 文档的特征
 */
public class Feature {

    /**
     * 每个关键词在不同类别中出现的文档数量
     */
    private Map<String, Integer> docCountMap = new HashMap<>();
    /**
     * 特征名称
     */
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void incDocCount(String category) {
        if (docCountMap.containsKey(category)) {
            docCountMap.put(category, docCountMap.get(category) + 1);
        } else {
            docCountMap.put(category, 1);
        }
    }

    public int getDocCount(String category) {
        if (docCountMap.containsKey(category)) {
            return docCountMap.get(category);
        } else {
            return 0;
        }
    }

    public void write(DataOutput out) throws IOException {
        out.writeUTF(name == null ? "" : name);

        out.writeInt(docCountMap.size());
        for (String category : docCountMap.keySet()) {
            out.writeUTF(category);
            out.writeInt(docCountMap.get(category));
        }
    }

    public void readFields(DataInput in) throws IOException {
        this.name = in.readUTF();

        docCountMap = new HashMap<>();
        int size = in.readInt();
        for (int i = 0; i < size; i++) {
            String category = in.readUTF();
            int docCount = in.readInt();
            docCountMap.put(category, docCount);
        }
    }

    public static Feature read(DataInput in) throws IOException {
        Feature f = new Feature();
        f.readFields(in);
        return f;
    }

}
