package com.hand.hcs.hcs_refund_order.service;

import java.util.List;

import com.hand.hap.core.IRequest;
import com.hand.hap.core.ProxySelf;
import com.hand.hap.system.service.IBaseService;
import com.hand.hap.util.SoapPostUtil;
import com.hand.hap.webservice.ws.idto.ReturnDeliveryExecute;
import com.hand.hcs.hcs_refund_order.dto.RefundOrderL;

public interface IRefundOrderLService extends IBaseService<RefundOrderL>, ProxySelf<IRefundOrderLService>{
	
	/**
	 * 退货单查询 按明细行查询
	 * @param requestContext
	 * @param dto
	 * @param page
	 * @param pageSize
	 * @return
	 */
	List<RefundOrderL> query(IRequest requestContext,RefundOrderL dto, int page, int pageSize);
	/**
	 * 退货单查询明细
	 * @param requestContext
	 * @param dto
	 * @param page
	 * @param pageSize
	 * @return
	 */
	List<RefundOrderL> queryLine(IRequest requestContext,RefundOrderL dto, int page, int pageSize);
	
	/**
	 * 退货执行wms接口
	 * @param rde
	 */
	SoapPostUtil.Response transferReturnDeliveryExecute(com.hand.itf.itf_return_delivery_execute.dto.ReturnDeliveryExecute rde);
}