package com.hand.spc.ecr_main.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.hand.hap.mybatis.common.Mapper;
import com.hand.hcm.hcm_item_b.dto.ItemB;
import com.hand.spc.ecr_main.dto.EcrSolutionSku;
import com.hand.spc.ecr_main.dto.EcrSolutionSku;
import com.hand.spc.ecr_main.view.EcrApsTmpV0;
import com.hand.spc.ecr_main.view.EcrSolutionSkuV0;
import com.hand.spc.ecr_main.view.EcrSolutionSkuV1;

public interface EcrSolutionSkuMapper extends Mapper<EcrSolutionSku>{

	/**
	 * 获取头汇总数据
	 */
	public List<EcrSolutionSkuV0> getHeadSql(@Param("ecrno")String ecrno);
	/**
	 * 获取行数据
	*/
	public List<EcrSolutionSkuV1> getLineSql(@Param("itemSkuId")String itemSkuId);
	
	/**
	 * 根据ecr编码和物料id 修改skuItem数据
	 */
	public void updateByEcrnoAndItemId(EcrSolutionSku dto);
	
	public void  updateByEcrnoAndSkuId(EcrSolutionSku dto);
	
	/**
	 *	   获取预测需求量
	 */
	public Float getForecastQty(@Param("itemId")String itemId,@Param("itemVersion")String itemVersion);
	/**
	 * 	获取完工数量
	 */
	public Float getCompletedQty(@Param("itemId")String itemId,@Param("itemVersion")String itemVersion);

	/**
	 * 	获取当月完工数量
	 */
	public Float getCompletedMonthQty(@Param("itemId")String itemId,@Param("itemVersion")String itemVersion);
	
	/**
	 *   更新年用量
	 * 
	 */
	
	public void updateEcrYearQty(EcrSolutionSku dto);
	/**
	 * 获取单笔物料
	 */
	public ItemB getSingle(ItemB dto);
	
	/**
	 *	   获取预测需求量(月份汇总)
	 */
	public List<EcrApsTmpV0> getForecastQtys(@Param("itemId")String itemId,@Param("itemVersion")String itemVersion);
	/**
	 * 	获取完工数量(月份汇总)
	 */
	public List<EcrApsTmpV0> getCompletedQtys(@Param("itemId")String itemId,@Param("itemVersion")String itemVersion);
	
	public List<EcrSolutionSkuV1> getDetailRfq(@Param("itemId")String itemId,@Param("ecrno")String ecrno);
	
	public List<EcrSolutionSkuV1> getSolutionList(@Param("ecrno")String ecrno);
	
	public List<EcrSolutionSkuV1> getRfqCommitList(@Param("ecrno")String ecrno);
}