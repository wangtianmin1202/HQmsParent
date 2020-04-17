package com.hand.plm.plm_product_func_attr_approve.view;

import com.hand.plm.plm_product_func_attr_approve.dto.ProductFuncAttrApprove;

public class ProductFuncAttrApproveVO extends ProductFuncAttrApprove{
	private String createBy;
	private String lastUpdateby;
	
	
	
	public String getCreateBy() {
		return createBy;
	}
	public void setCreateBy(String createBy) {
		this.createBy = createBy;
	}
	public String getLastUpdateby() {
		return lastUpdateby;
	}
	public void setLastUpdateby(String lastUpdateby) {
		this.lastUpdateby = lastUpdateby;
	}
}
