package com.hand.hcs.hcs_delivery_ticket.service;

import java.util.List;

import com.hand.hap.core.IRequest;
import com.hand.hap.core.ProxySelf;
import com.hand.hap.system.dto.ResponseData;
import com.hand.hap.system.service.IBaseService;
import com.hand.hcs.hcs_delivery_ticket.dto.DeliveryTicketH;

public interface IDeliveryTicketHService extends IBaseService<DeliveryTicketH>, ProxySelf<IDeliveryTicketHService>{

	/**
	 * 生产供应商送货单号  
	 * @param requestContext 请求上下文
	 * @param deliveryTicketH 送货单头
	 * @return 送货单号
	 */
	String queryOrderNum(IRequest requestContext,DeliveryTicketH deliveryTicketH);
	/**
	 * 已有送货单查询
	 * @param requestContext 请求上下文
	 * @param deliveryTicketH 送货单头
	 * @param page 页码
	 * @param pageSize 页数
	 * @return 结果集
	 */
	List<DeliveryTicketH> query(IRequest requestContext,DeliveryTicketH deliveryTicketH, int page, int pageSize);
	/**
	 * 送货单编辑主界面查询
	 * @param requestContext 请求上下文
	 * @param deliveryTicketH 送货单头
	 * @param page 页码
	 * @param pageSize 页数
	 * @return 结果集
	 */
	List<DeliveryTicketH> queryData(IRequest requestContext,DeliveryTicketH deliveryTicketH, int page, int pageSize);
	/**
	 * 发货
	 * @param requestContext 请求上下文
	 * @param deliveryTicketH 送货单头
	 * @return 数据返回对象
	 */
	ResponseData delivery(IRequest requestContext,DeliveryTicketH deliveryTicketH);
	/**
	 * 取消
	 * @param requestContext 请求上下文
	 * @param deliveryTicketH 送货单头
	 * @return 数据返回对象
	 */
	ResponseData cancel(IRequest requestContext,DeliveryTicketH deliveryTicketH);
	/**
	 * 送货单打印查询
	 * @param requestContext 请求上下文
	 * @param deliveryTicketH
	 * @return
	 */
	List<DeliveryTicketH> printQuery(IRequest requestContext,DeliveryTicketH deliveryTicketH);
	/**
	 * 送货单头查询
	 * @param requestContext 请求上下文
	 * @param deliveryTicketH 送货单头
	 * @return 结果集
	 */
	List<DeliveryTicketH> queryByTicketId(IRequest requestContext,DeliveryTicketH deliveryTicketH);
	/**
	 * 更新打印次数
	 * @param requestContext 请求上下文
	 * @param deliveryTicketH 送货单头
	 * @return 数据返回对象
	 */
	ResponseData updatePrintTime(IRequest requestContext,DeliveryTicketH deliveryTicketH);
	/**
	 * 打印校验
	 * @param requestContext 请求上下文
	 * @param deliveryTicketH 送货单头
	 * @return 数据返回对象
	 */
	ResponseData checkPrint(IRequest requestContext,DeliveryTicketH deliveryTicketH);
}