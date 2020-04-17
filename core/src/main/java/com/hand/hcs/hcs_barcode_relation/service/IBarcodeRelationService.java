package com.hand.hcs.hcs_barcode_relation.service;

import java.util.List;

import com.hand.hap.core.IRequest;
import com.hand.hap.core.ProxySelf;
import com.hand.hap.system.dto.ResponseData;
import com.hand.hap.system.service.IBaseService;
import com.hand.hcs.hcs_barcode.dto.SmallBarcode;
import com.hand.hcs.hcs_barcode_relation.dto.BarcodeRelation;

public interface IBarcodeRelationService extends IBaseService<BarcodeRelation>, ProxySelf<IBarcodeRelationService>{
	/**
	 * 托盘查询
	 * @param dto
	 * @return
	 */
	ResponseData confirmBind(IRequest requestContext, BarcodeRelation dto);
	
	/**
	 * 清空送货单头行id
	 * @param barcodeRelation
	 */
	void updateTicketId(BarcodeRelation barcodeRelation);
	/**
	 * 获取物料标签id
	 * @param codeId
	 * @param codeType
	 * @return
	 */
	List<Float> querySmallBarcodeId(Float codeId, String codeType);
}