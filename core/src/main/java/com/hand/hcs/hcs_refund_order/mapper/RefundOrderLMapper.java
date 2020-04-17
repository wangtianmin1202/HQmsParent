package com.hand.hcs.hcs_refund_order.mapper;

import java.util.List;

import com.hand.hap.mybatis.common.Mapper;
import com.hand.hcs.hcs_refund_order.dto.RefundOrderL;

public interface RefundOrderLMapper extends Mapper<RefundOrderL>{
	/**
	 * 退货单查询 按明细行查询
	 * @return
	 */
	List<RefundOrderL> query(RefundOrderL refundOrderL);
	/**
	 * 退货单查询明细
	 * @param refundOrderL
	 * @return
	 */
	List<RefundOrderL> queryLine(RefundOrderL refundOrderL);
	/**
	 * 退货单结算Job(获取数据源)
	 * @return
	 */
	List<RefundOrderL> refundOrderJobQuery();
}