package com.hand.spc.ecr_main.mapper;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.hand.hap.mybatis.common.Mapper;
import com.hand.spc.ecr_main.dto.EcrItemReport;
import com.hand.spc.ecr_main.dto.EcrItemReportDetail;
import com.hand.spc.ecr_main.view.EcrItemReportV0;
import com.hand.spc.ecr_main.view.EcrItemReportV2;
import com.hand.spc.ecr_main.view.EcrItemReportV3;

public interface EcrItemReportMapper extends Mapper<EcrItemReport>{
	
	/**
	 * 获取物料属性
	 * @param ecrno
	 * @return
	 */
	public List<EcrItemReportV0> getReportItems(@Param("ecrno")String ecrno);
	/**
	 * 获取PO未处理订单数量
	 * @param itemId
	 * @param supplierId
	 * @return
	 */
	public Float getPoOnlineQty(@Param("itemId") String itemId,@Param("supplierId")String supplierId);	
	/**
	 * 获取供应商库存数量  供应商在制数量
	 * @param itemId
	 * @param supplierId
	 * @return
	 */
	public EcrItemReportV2 getSupplierQty(@Param("itemId") String itemId,@Param("supplierId")String supplierId,@Param("itemVersion")String itemVersion);
	/**
	 * 获取采购件数量
	 * @param itemId
	 * @param supplierId
	 * @return
	 */
	public EcrItemReportV2 getBomQtys(@Param("itemId") String itemId,@Param("supplierId")String supplierId,@Param("itemVersion")String itemVersion);
	
	/**
	 * 获取预测计划数量
	 * @param itemId
	 * @param demandDate
	 * @param itemVersion
	 * @return
	 */
	public List<EcrItemReportV3> getDemandQty(@Param("itemId") String itemId,@Param("demandDate") Date demandDate,@Param("itemVersion") String itemVersion);
	
	/**
	 * 用受影响料号查出对应SKU和用量比列
	 * @param ecrno
	 * @param itemId
	 * @return
	 */
	public List<EcrItemReportDetail> getBomSkuItems(@Param("ecrno")String ecrno,@Param("itemId") String itemId);
}