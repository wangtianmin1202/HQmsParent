package com.hand.hcs.hcs_delivery_ticket.service;

import java.util.List;

import com.hand.hap.core.IRequest;
import com.hand.hap.core.ProxySelf;
import com.hand.hap.system.dto.ResponseData;
import com.hand.hap.system.service.IBaseService;
import com.hand.hap.util.SoapPostUtil;
import com.hand.hcs.hcs_delivery_ticket.dto.DeliveryTicketL;
import com.hand.hcs.hcs_delivery_ticket.dto.TicketReport;

public interface IDeliveryTicketLService extends IBaseService<DeliveryTicketL>, ProxySelf<IDeliveryTicketLService>{
	/**
	 * 送货单编辑页面明细查询
	 * @param requestContext 请求上下文
	 * @param dto 送货单行
	 * @param page 页码
	 * @param pageSize 页大小
	 * @return 结果集
	 */
	List<DeliveryTicketL> query(IRequest requestContext, DeliveryTicketL dto, int page, int pageSize);
	/**
	 * 创建送货单
	 * @param requestContext 请求上下文
	 * @param dto 送货单行集合
	 * @return 数据返回对象
	 */
	ResponseData saveHeadLine(IRequest requestContext, List<DeliveryTicketL> dto);
	/**
	 * 送货单头查行信息
	 * @param requestContext 请求上下文
	 * @param dto 送货单行
	 * @return 结果集
	 */
	List<DeliveryTicketL> queryByHeadId(IRequest requestContext, DeliveryTicketL dto);
	/**
	 * 取消送货单行状态
	 * @param requestContext 请求上下文
	 * @param dto 送货单行集合
	 * @return 数据返回对象
	 */
	ResponseData cancelLine(IRequest requestContext, List<DeliveryTicketL> dto);
	/**
	 * 送货单打印行查询
	 * @param requestContext 请求上下文
	 * @param dto 送货单行
	 * @return 结果集
	 */
	List<DeliveryTicketL> printQueryLine(IRequest requestContext,DeliveryTicketL dto);
	/**
	 * 采购订单-按明细查询-查询已接收数量 
	 * @param requestContext 请求上下文
	 * @param dto 送货单行
	 * @return 已接收数量
	 */
	Long queryReceiveQty(IRequest requestContext,DeliveryTicketL dto);
	/**
	 * 送货单查询-明细
	 * @param requestContext 请求上下文
	 * @param dto 送货单行
	 * @param page 页码
	 * @param pageSize 页大小
	 * @return 结果集
	 */
	List<DeliveryTicketL> queryHeadLine(IRequest requestContext, DeliveryTicketL dto, int page, int pageSize);
	/**
	 * 采购订单发运明细  关闭按钮校验查询 
	 * @param dto
	 * @return
	 */
	ResponseData queryLocationCount(IRequest requestContext, DeliveryTicketL dto);
	
	/**
	 * 送货单收货接口
	 * @param cr
	 */
	SoapPostUtil.Response transferDeliveryReceipt(com.hand.itf.itf_delivery_receipt.dto.DeliveryReceipt cr);
	/**
	 * 供货执行情况查询
	 * @param requestContext
	 * @param dto
	 * @return
	 */
	List<TicketReport> planReport(IRequest requestContext, DeliveryTicketL dto) throws Exception;
}