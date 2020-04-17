package com.hand.hcs.hcs_barcode.mapper;

import com.hand.hap.mybatis.common.Mapper;
import com.hand.hcs.hcs_barcode.dto.SmallBarcodeControl;

public interface SmallBarcodeControlMapper extends Mapper<SmallBarcodeControl>{
	/**
	 * 清空外箱条码ID
	 * @param dto
	 */
	void updateObarcodeId(SmallBarcodeControl dto);
	/**
	 * 通过  sbarcodeId 清空送货单头行id
	 * @param dto
	 */
	void updateTicketIdBySbarcodeId(SmallBarcodeControl dto);
	/**
	 * 根据送货单行id 清空送货单头行id
	 * @param dto
	 */
	void updateTicketIdByLineId(SmallBarcodeControl dto);
}