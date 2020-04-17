package com.hand.hcs.hcs_barcode_relation.mapper;

import java.util.List;

import com.hand.hap.mybatis.common.Mapper;
import com.hand.hcs.hcs_barcode_relation.dto.BarcodeRelation;

public interface BarcodeRelationMapper extends Mapper<BarcodeRelation>{
	/**
	 * 清空送货单头行id
	 * @param barcodeRelation
	 */
	void updateTicketId(BarcodeRelation barcodeRelation);
	
	List<BarcodeRelation> interfaceSelect(BarcodeRelation dto);
}