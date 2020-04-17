package com.hand.plm.product_func_attr_basic.view;

import java.util.List;

public class UpdateDetailVO {
    /**
     * 明细ID
     */
    private String detailId;
    /**
     * 功能ID
     */
    private String kid;
    /**
     * 产品ID
     */
	private String productId;
    
    /**
     * 产品
     */
    private String product;
    /**
     * 产品功能ID
     */
	private String productFuncId;
    /**
     * 产品功能
     */
    private String productFunc;
    /**
     * 产品功能内容
     */
    private String productFuncAttr;
    /**
     * 版本
     */
    private String version;
    /**
     * 状态
     */
    private String status;


    /**
     * 明细ID集合
     */
    private List<String> detailIdList;


	public String getDetailId() {
		return detailId;
	}


	public void setDetailId(String detailId) {
		this.detailId = detailId;
	}


	public String getKid() {
		return kid;
	}


	public void setKid(String kid) {
		this.kid = kid;
	}


	public String getProductId() {
		return productId;
	}


	public void setProductId(String productId) {
		this.productId = productId;
	}


	public String getProduct() {
		return product;
	}


	public void setProduct(String product) {
		this.product = product;
	}


	public String getProductFuncId() {
		return productFuncId;
	}


	public void setProductFuncId(String productFuncId) {
		this.productFuncId = productFuncId;
	}


	public String getProductFunc() {
		return productFunc;
	}


	public void setProductFunc(String productFunc) {
		this.productFunc = productFunc;
	}


	public String getProductFuncAttr() {
		return productFuncAttr;
	}


	public void setProductFuncAttr(String productFuncAttr) {
		this.productFuncAttr = productFuncAttr;
	}


	public String getVersion() {
		return version;
	}


	public void setVersion(String version) {
		this.version = version;
	}


	public String getStatus() {
		return status;
	}


	public void setStatus(String status) {
		this.status = status;
	}


	public List<String> getDetailIdList() {
		return detailIdList;
	}


	public void setDetailIdList(List<String> detailIdList) {
		this.detailIdList = detailIdList;
	}

    
    
}
