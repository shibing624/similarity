package org.xm.similarity.sentence.editdistance;


import org.xm.similarity.word.hownet.concept.ConceptSimilarity;
import org.xm.tokenizer.Word;

public class WordEditUnit extends EditUnit {

    private Word word = null;

    public WordEditUnit(Word word) {
        this.word = word;
    }

    @Override
    public String getUnitString() {
        return word.getName();
    }

    /**
     * 根据相似度获取替换代价
     */
    @Override
    public double getSubstitutionCost(EditUnit otherUnit) {
        if (!(otherUnit instanceof WordEditUnit))
            return 1.0;
        if (equals(otherUnit))
            return 0.0;

        WordEditUnit other = (WordEditUnit) otherUnit;
        // 词性不同，直接返回1.0
        if (word.getPos() != other.word.getPos()) {
            return 1.0;
        }
        return 1 - ConceptSimilarity.getInstance().getSimilarity(getUnitString(), other.getUnitString());
    }

    @Override
    public boolean equals(Object other) {
        if (!(other instanceof WordEditUnit))
            return false;
        WordEditUnit otherUnit = (WordEditUnit) other;
        Word otherWord = otherUnit.word;
        // 词性不同，直接返回1.0
        if (word.getPos() != otherWord.getPos()) {
            return false;
        }
        double sim = ConceptSimilarity.getInstance().getSimilarity(getUnitString(), otherUnit.getUnitString());
        return sim > 0.85;
    }

}
