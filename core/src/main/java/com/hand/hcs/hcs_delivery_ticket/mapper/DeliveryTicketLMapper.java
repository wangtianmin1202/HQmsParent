package com.hand.hcs.hcs_delivery_ticket.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.hand.hap.mybatis.common.Mapper;
import com.hand.hcs.hcs_delivery_ticket.dto.DeliveryTicketL;

public interface DeliveryTicketLMapper extends Mapper<DeliveryTicketL> {
	/**
	 * 送货单编辑页面查询
	 * 
	 * @param lineLocationIdList
	 * @return
	 */
	// List<DeliveryTicketL> query(@Param("ticketId")float
	// ticketId,@Param("lineLocationIdList") List<Float>
	// lineLocationIdList,@Param("ticketLineIdList") List<Float> ticketLineIdList);
	List<DeliveryTicketL> query(DeliveryTicketL dto);

	/**
	 * 送货单打印行查询
	 * 
	 * @param dto
	 * @return
	 */
	List<DeliveryTicketL> printQueryLine(DeliveryTicketL dto);

	/**
	 * 采购订单-按明细查询-查询已接收数量
	 * 
	 * @param dto
	 * @return
	 */
	Long queryReceiveQty(DeliveryTicketL dto);

	/**
	 * 送货单头行查询
	 * 
	 * @param dto
	 * @return
	 */
	List<DeliveryTicketL> queryHeadLine(DeliveryTicketL dto);

	List<DeliveryTicketL> supplyPlanSumSelect(DeliveryTicketL dto);

	Double querySum(DeliveryTicketL dto);

	/**
	 * 采购订单发运明细 关闭按钮校验查询
	 * 
	 * @param dto
	 * @return
	 */
	List<DeliveryTicketL> queryLocationCount(DeliveryTicketL dto);

	/**
	 * 送货单结算Job(获取数据源)
	 * 
	 * @param dto
	 * @return
	 */
	List<DeliveryTicketL> deliveryTicketJobQuery();

	/**
	 * 传wms接口查询
	 * 
	 * @param dto
	 * @return
	 */
	List<DeliveryTicketL> interfaceSelect(DeliveryTicketL dto);

	/**
	 * 供货执行情况查询
	 * 
	 * @param dto
	 * @return
	 */
	List<DeliveryTicketL> queryReport(DeliveryTicketL dto);

	/**
	 * @description R Q 状态的送货单 单个POlineID下的 接收数量和 receive_qua
	 * @author tianmin.wang
	 * @date 2019年12月23日
	 * @param poLineId
	 * @return
	 */
	Float getSumReceiveQuantityByPoLineId(Float poLineId);

	/**
	 * @description 根据送货单行表的物料id、送货单头表的工厂id、供应商id关联合格供应商表 HCS_ASL 取asl_id，再关联合格供应商拓展表 HCS_ASL_CONTROL 取underdelivery_tolerance_limit，若此值为空，取0
	 * @author tianmin.wang
	 * @date 2019年12月23日
	 * @param ticketLineId
	 * @return
	 */
	Float getUnderdeliveryToleranceLimit(Float ticketLineId);

	/**
	 * @description
	 * @author tianmin.wang
	 * @date 2019年12月23日 
	 * @param lineLocationId
	 * @return
	 */
	Float getSumReceiveQuantityByLineLocationId(Float lineLocationId);

	/**
	 * @description 该订单行其他送货单行的已发运数量
	 * @author tianmin.wang
	 * @date 2019年12月24日 
	 * @param deliveryTicketLs
	 * @return
	 */
	Float getShipedQuantity(DeliveryTicketL deliveryTicketL);

}