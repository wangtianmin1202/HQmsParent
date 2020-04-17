package com.hand.plm.plm_prod_design_standard_detail.view;

import java.util.List;

import com.hand.plm.plm_prod_design_standard_detail.dto.ProdDesignStandardDetail;

public class ProdDesignStandardDetailVO extends ProdDesignStandardDetail {

	/**
	 * 产品品类
	 */
	private String productName;
	/**
	 * 结构模组
	 */
	private String structureModule;
	/**
	 * 零件名称
	 */
	private String partName;
	/**
	 * 所有结构名称拼接的字符串
	 */
	private String name;

	private List<String> detailIdList;

	private List<String> designStandardIdList;
	/**
	 * 树节点
	 */
	private String treeId;
	

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getStructureModule() {
		return structureModule;
	}

	public void setStructureModule(String structureModule) {
		this.structureModule = structureModule;
	}

	public String getPartName() {
		return partName;
	}

	public void setPartName(String partName) {
		this.partName = partName;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<String> getDetailIdList() {
		return detailIdList;
	}

	public void setDetailIdList(List<String> detailIdList) {
		this.detailIdList = detailIdList;
	}

	public List<String> getDesignStandardIdList() {
		return designStandardIdList;
	}

	public void setDesignStandardIdList(List<String> designStandardIdList) {
		this.designStandardIdList = designStandardIdList;
	}

	public String getTreeId() {
		return treeId;
	}

	public void setTreeId(String treeId) {
		this.treeId = treeId;
	}

}
