package com.hand.hcs.hcs_refund_order.mapper;

import java.util.List;

import com.hand.hap.mybatis.common.Mapper;
import com.hand.hcs.hcs_refund_order.dto.RefundOrderH;

public interface RefundOrderHMapper extends Mapper<RefundOrderH>{
	/**
	 * 退货单主界面查询
	 * @param refundOrderH
	 * @return
	 */
	List<RefundOrderH> query(RefundOrderH refundOrderH);

	void refreshHeaderStatus(Float float1);

	Float getNoStatusCount(Float roHeaderId);
}