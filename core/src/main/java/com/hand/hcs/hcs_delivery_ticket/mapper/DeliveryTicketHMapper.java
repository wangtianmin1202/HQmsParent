package com.hand.hcs.hcs_delivery_ticket.mapper;

import java.util.List;

import com.hand.hap.mybatis.common.Mapper;
import com.hand.hcs.hcs_delivery_ticket.dto.DeliveryTicketH;

public interface DeliveryTicketHMapper extends Mapper<DeliveryTicketH>{
	
	/**
	 * 查询供应商当天最大流水号
	 * @param deliveryTicketH
	 * @return
	 */
	Integer queryMaxNum(DeliveryTicketH deliveryTicketH);
	/**
	 * 已有送货单查询
	 * @param deliveryTicketH
	 * @return
	 */
	List<DeliveryTicketH> query(DeliveryTicketH deliveryTicketH);
	/**
	 * 送货单编辑主界面查询
	 * @param deliveryTicketH
	 * @return
	 */
	List<DeliveryTicketH> queryData(DeliveryTicketH deliveryTicketH);
	/**
	 * 送货单打印查询
	 * @param deliveryTicketH
	 * @return
	 */
	List<DeliveryTicketH> printQuery(DeliveryTicketH deliveryTicketH);
	/**
	 * 查询送货单头
	 * @param deliveryTicketH
	 * @return
	 */
	List<DeliveryTicketH> queryByTicketId(DeliveryTicketH deliveryTicketH);
	/**
	 * @description
	 * @author tianmin.wang
	 * @date 2019年11月14日 
	 * @return
	 */
	int countCRQSelect(Float hid);
}