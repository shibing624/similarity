package org.xm.similarity.sentence.editdistance;

/**
 * 编辑单元
 * @author xuming
 */
public abstract class EditUnit {

	/**
	 * 获取编辑单元的内部字符串
	 * @return
	 */
	public abstract String getUnitString();

	/**
	 * 获取替换代价，默认替换代价当替换单元的内容相同时为0，
	 * 不同时为1
	 */
	public double getSubstitutionCost(EditUnit other) {
		return this.equals(other) ? 0 : 1;
	}

	/**
	 * 获取删除代价,标准算法的默认值为1.0, 此处也设为1.0
	 * 具体的编辑单元可以通过覆盖该方法设置不同的删除代价
	 * @return 删除代价
	 */
	public double getDeletionCost() {
		return 1.0;
	}

	/**
	 * 获取插入代价,标准算法的默认值为1.0.
	 * 具体的编辑单元可以通过覆盖该方法设置不同的插入代价
	 */
	public double getInsertionCost() {
		return 1.0;
	}

	@Override
	public boolean equals(Object other) {
		if (!(other instanceof EditUnit))
			return false;
		return getUnitString().equals(((EditUnit) other).getUnitString());
	}

	@Override
	public String toString() {
		return getUnitString();
	}

}
