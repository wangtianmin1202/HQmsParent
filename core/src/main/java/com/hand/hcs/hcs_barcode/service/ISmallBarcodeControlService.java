package com.hand.hcs.hcs_barcode.service;

import com.hand.hap.core.IRequest;
import com.hand.hap.core.ProxySelf;
import com.hand.hap.system.service.IBaseService;
import com.hand.hcs.hcs_barcode.dto.SmallBarcodeControl;

public interface ISmallBarcodeControlService extends IBaseService<SmallBarcodeControl>, ProxySelf<ISmallBarcodeControlService>{
	/**
	 * 清空外箱条码ID
	 * @param dto
	 */
	void updateObarcodeId(IRequest requestContext,SmallBarcodeControl dto);
	
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