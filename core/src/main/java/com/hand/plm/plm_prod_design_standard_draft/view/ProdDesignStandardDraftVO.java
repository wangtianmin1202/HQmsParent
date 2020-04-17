package com.hand.plm.plm_prod_design_standard_draft.view;

import java.util.List;

import com.hand.plm.plm_prod_design_standard_draft.dto.ProdDesignStandardDraft;

public class ProdDesignStandardDraftVO extends ProdDesignStandardDraft {
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
	
	private	List<String> detailIdList;
	
	private List<String>designStandardIdList;
	
	private String checkerName;
	
	
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
	public String getCheckerName() {
		return checkerName;
	}
	public void setCheckerName(String checkerName) {
		this.checkerName = checkerName;
	}
	
}
