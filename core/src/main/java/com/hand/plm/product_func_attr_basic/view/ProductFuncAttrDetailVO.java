package com.hand.plm.product_func_attr_basic.view;

import java.util.List;

import com.hand.plm.product_func_attr_basic.dto.ProductFuncAttrDetail;

public class ProductFuncAttrDetailVO extends ProductFuncAttrDetail {
	/**
	 * 产品功能属性
	 */
	private String description;
	
	private List<String>kidIdList;
	/**
	 * 查询参数
	 */
	private String queryParam;
	/**
	 * 产品
	 */
	private String product;
	
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public List<String> getKidIdList() {
		return kidIdList;
	}
	public void setKidIdList(List<String> kidIdList) {
		this.kidIdList = kidIdList;
	}
	public String getQueryParam() {
		return queryParam;
	}
	public void setQueryParam(String queryParam) {
		this.queryParam = queryParam;
	}
	public String getProduct() {
		return product;
	}
	public void setProduct(String product) {
		this.product = product;
	}
	
	
	
}
