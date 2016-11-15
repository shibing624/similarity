package org.xm.similarity.sentence.editdistance;

public class ChunkEditUnit extends EditUnit {

	private SuperString<? extends EditUnit> chunk = null;

	public ChunkEditUnit(SuperString<? extends EditUnit> chunk) {
		this.chunk = chunk;
	}

	@Override
	public String getUnitString() {
		return chunk.toString();
	}

	/**
	 * 根据此语的相似度获取替换代价
	 */
	@Override
	public double getSubstitutionCost(EditUnit otherUnit) {
		if (!(otherUnit instanceof ChunkEditUnit))
			return chunk.length();
		if (equals(otherUnit))
			return 0.0;

		ChunkEditUnit other = (ChunkEditUnit) otherUnit;
		return new StandardEditDistanceSimilarity().getEditDistance(chunk, other.chunk);
	}

	/**
	 * 获取删除代价,标准算法的默认值为1.0, 此处也设为1.0
	 * 具体的编辑单元可以通过覆盖该方法设置不同的删除代价
	 * @return 删除代价
	 */
	@Override
	public double getDeletionCost() {
		return chunk.length();
	}

	/**
	 * 获取插入代价,标准算法的默认值为1.0.
	 * 具体的编辑单元可以通过覆盖该方法设置不同的插入代价
	 */
	@Override
	public double getInsertionCost() {
		return chunk.length();
	}

}
