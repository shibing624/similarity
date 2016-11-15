package org.xm.classification;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * 分类的类别
 */
public class Variable {

    /**
     * 类别信息
     */
    Map<String, CategoryInfo> categoryMap = new HashMap<>();

    Map<String, Feature> features = new HashMap<>();

    /**
     * 所有文档的数量
     */
    private int docCount = 0;

    public void write(DataOutput out) throws IOException {
        //保存文档总数
        out.writeInt(docCount);

        //写入类别总数
        out.writeInt(categoryMap.size());
        for (String category : categoryMap.keySet()) {
            out.writeUTF(category);
            categoryMap.get(category).write(out);
        }

        //写入Feature总数
        out.writeInt(features.size());
        for (String key : features.keySet()) {
            out.writeUTF(key);
            features.get(key).write(out);
        }
    }

    public void readFields(DataInput in) throws IOException {
        this.docCount = in.readInt();

        int size = in.readInt();
        categoryMap = new HashMap<>();
        for (int i = 0; i < size; i++) {
            String category = in.readUTF();
            CategoryInfo info = CategoryInfo.read(in);
            categoryMap.put(category, info);
        }

        size = in.readInt();
        features = new HashMap<>();
        for (int i = 0; i < size; i++) {
            String word = in.readUTF();
            Feature feature = Feature.read(in);
            features.put(word, feature);
        }
    }

    public static Variable read(DataInput in) throws IOException {
        Variable v = new Variable();
        v.readFields(in);
        return v;
    }

    public Collection<String> getCategories() {
        return categoryMap.keySet();
    }

    public int getFeatureCount() {
        return features.size();
    }

    public boolean containFeature(String feature) {
        return features.containsKey(feature);
    }

    public void incDocCount() {
        this.docCount++;
    }

    public int getDocCount() {
        return this.docCount;
    }

    /**
     * 获取置顶类别下的文档数量
     *
     * @param category
     * @return
     */
    public int getDocCount(String category) {
        return categoryMap.get(category).getDocCount();
    }

    /**
     * 获取feature在指定类别下的文档出现数量
     *
     * @param feature
     * @param category
     * @return
     */
    public int getDocCount(String feature, String category) {
        Feature f = features.get(feature);
        if (f != null) {
            return f.getDocCount(category);
        }
        return 0;
    }

    public void addInstance(Instance instance) {
        incDocCount();
        CategoryInfo info;
        if (categoryMap.containsKey(instance.getCategory())) {
            info = categoryMap.get(instance.getCategory());
        } else {
            info = new CategoryInfo();
        }
        info.incDocCount();
        categoryMap.put(instance.getCategory(), info);

        for (String word : instance.getWords()) {
            Feature feature = features.get(word);

            if (feature == null)
                feature = new Feature();

            feature.setName(word);
            feature.incDocCount(instance.getCategory());

            features.put(word, feature);
        }
    }

    public static class CategoryInfo {
        private int docCount;

        public int getDocCount() {
            return docCount;
        }

        public void incDocCount() {
            this.docCount++;
        }

        public void setDocCount(int docCount) {
            this.docCount = docCount;
        }

        public void write(DataOutput out) throws IOException {
            out.writeInt(docCount);
        }

        public void readFields(DataInput in) throws IOException {
            this.docCount = in.readInt();
        }

        public static CategoryInfo read(DataInput in) throws IOException {
            CategoryInfo c = new CategoryInfo();
            c.readFields(in);
            return c;
        }
    }

}
