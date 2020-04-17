package com.hand.hcs.hcs_supply_plan.mapper;

import java.util.List;

import com.hand.hap.mybatis.common.Mapper;
import com.hand.hcs.hcs_supply_plan.dto.SupplyPlan;

public interface SupplyPlanMapper extends Mapper<SupplyPlan> {
	/**
	 * 基础查询
	 * @description
	 * @author tianmin.wang
	 * @date 2019年11月22日 
	 * @param dto
	 * @return
	 */
	List<SupplyPlan> reSelect(SupplyPlan dto);
	
	/**
	 * 存在性查询
	 * @description
	 * @author tianmin.wang
	 * @date 2019年11月22日 
	 * @param dto
	 * @return
	 */
	List<SupplyPlan> existsSelect(SupplyPlan dto);
	
	/**
	 * LeadTime为参数的查询
	 * @description
	 * @author tianmin.wang
	 * @date 2019年11月22日 
	 * @param supplyPlan
	 * @return
	 */
	List<SupplyPlan> selectLeadTime(SupplyPlan supplyPlan);

	/**
	 * 最大流水
	 * @description
	 * @author tianmin.wang
	 * @date 2019年11月22日 
	 * @param sr
	 * @return
	 */
	List<SupplyPlan> selectMaxSupplyPlanNum(SupplyPlan sr);

	
	/**
	 * 最大行号
	 * @description
	 * @author tianmin.wang
	 * @date 2019年11月22日 
	 * @param dto
	 * @return
	 */
	List<SupplyPlan> maxLineNumSelect(SupplyPlan dto);
	
	
	/**
	 * DeliveryQty求和查询
	 * @description
	 * @author tianmin.wang
	 * @date 2019年11月22日 
	 * @param dto
	 * @return
	 */
	List<SupplyPlan> selectSumSupplierDeliveryQty(SupplyPlan dto);
	
	
	/**
	 * otherFactorFlag 为Y的数据
	 * @description
	 * @author tianmin.wang
	 * @date 2019年11月22日 
	 * @param dto
	 * @return
	 */
	List<SupplyPlan> otherFactorFlagSelect(SupplyPlan dto);
	
	/**
	 *  物料发运job
	 * @param dto
	 * @return
	 */
	List<SupplyPlan> selectForecastDelivery(SupplyPlan dto);

	
	/**
	 * 查询需要确认的数据
	 * @description
	 * @author tianmin.wang
	 * @date 2019年11月22日 
	 * @param dto
	 * @return
	 */
	Float selectNeedConfirm(Float dto);

	/**
	 * @description  此供货计划存在批号相同 行号第1位相同的 并且 已确认 状态的供货计划 将这些供货计划的 交货数量 求和
	 * @author tianmin.wang
	 * @date 2020年1月13日 
	 * @param supplyPlan
	 * @return
	 */
	Float getSupplierDeliveryQtyHaveSplit(SupplyPlan supplyPlan);

	/**
	 * @description
	 * @author tianmin.wang
	 * @date 2020年1月15日 
	 * @param supplyPlanNum
	 * @param supplyPlanLineNum
	 * @return
	 */
	Integer getSumSupplierdeliveryQty(String supplyPlanNum, String supplyPlanLineNum);
	
}