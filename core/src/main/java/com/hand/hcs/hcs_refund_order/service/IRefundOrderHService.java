package com.hand.hcs.hcs_refund_order.service;

import java.util.List;

import com.hand.hap.core.IRequest;
import com.hand.hap.core.ProxySelf;
import com.hand.hap.system.service.IBaseService;
import com.hand.hcs.hcs_refund_order.dto.RefundOrderH;

public interface IRefundOrderHService extends IBaseService<RefundOrderH>, ProxySelf<IRefundOrderHService>{
	/**
	 * 退货单主界面查询
	 * @param reuqestContext
	 * @param dto
	 * @param page
	 * @param pageSize
	 * @return
	 */
	List<RefundOrderH> query(IRequest reuqestContext, RefundOrderH dto, int page, int pageSize);
}