package com.hand.spc.ecr_main.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.hand.hap.mybatis.common.Mapper;
import com.hand.spc.ecr_main.dto.EcrProjectTracking;
import com.hand.spc.ecr_main.dto.EcrQtp;

public interface EcrProjectTrackingMapper extends Mapper<EcrProjectTracking>{
	
	/**
	 * 查询 skuCode
	 * @param skuId
	 * @return
	 */
	List<String> selectSkuCode(String skuId);

	/**
	 * 查询本表信息
	 * @param dto
	 * @return
	 */
	List<EcrProjectTracking> selectGrid(EcrProjectTracking dto);
	
	/**
	 * 查询项目跟踪信息基本信息（根据ECR编码）
	 * @param ecrno
	 * @return
	 */
	List<EcrProjectTracking> selectInfo(String ecrno);
	
	/**
	 * ECR受影响物料价格趋势查询报表 
	 * @param skuId
	 * @return
	 */
	List<EcrProjectTracking> selectInfluence(String skuId);
	
	
	/**----------------------------------  计算实际成本 -----------------------------------*/
	
	/**
	 * 查询前一次的实际价格 
	 * @param skuId
	 * @return
	 */
	 Double selectLastActualPrice(@Param("ecrno")String ecrno, @Param("skuId")String skuId);
	
	/**
	 * 查询标准价格 
	 * @param skuId
	 * @return
	 */
	 List<EcrProjectTracking> selectStdPrice(@Param("ecrno")String ecrno,@Param("skuId")String skuId);
	 
	 /**
	  * 查询SKU物料成本变化 
	  * @param skuId
	  * @return
	  */
	 List<EcrProjectTracking> selectCostChange(@Param("ecrno")String ecrno,@Param("skuId")String skuId);
	 
	 /**
	  * 查询 SKU 工时变化
	  * @param skuId
	  * @return
	  */
	 List<EcrProjectTracking> selectWorkTimeChange(@Param("ecrno")String ecrno,@Param("skuId")String skuId);
		 
	 /**
	  * 查询 stdPriceUnit
	  * @param skuId
	  * @return
	  */
	 Double selectStdPriceUnit(@Param("ecrno")String ecrno,@Param("skuId")String skuId);
	 
	 /**
	  * 保存到实际成本（ hpm_ecr_item_sku_b）
	  * @param ecrno
	  * @param skuId
	  * @param actCost
	  */
	 void updateCostItem(@Param("ecrno")String ecrno, @Param("skuId")String skuId,
			 @Param("actCost")Double actCost);
	 
	 /**
	  * 保存到实际成本（hpm_ecr_solution_sku）
	  * @param ecrno
	  * @param skuId
	  * @param actualCost
	  */
	 void updateCostSolution(@Param("ecrno")String ecrno, @Param("skuId")String skuId,
			 @Param("actCost")Double actCost);
	 
	/**----------------------------------  计算实际成本 -----------------------------------*/
	 

	 
	/**----------------------------------  QTP 任务 -----------------------------------*/
	 /**
	  * 查询技术文件状态已完成
	  * @return
	  */
	 public int selectFileItemId(@Param("ecrno")String ecrno);
	 
	 /**
	  * 查询物料数据1
	  * @return
	  */
	 List<EcrProjectTracking> selectSku(@Param("itemId")String itemId);
	 
	 /**
	  * 查询物料数据2
	  * @return
	  */
	 List<EcrProjectTracking> selectItem(@Param("itemId")String itemId);
	 
	 /**
	  * 查询QTP次序
	  * @return
	  */
	 Long selectQtpSeq(@Param("ecrno")String ecrno, @Param("itemId")String itemId);
	 
	 /**
	  * 保存到 QTP
	  * @return
	  */
	 Long insertQtp(@Param("ecrno")String ecrno, @Param("itemId")String itemId,
			 @Param("qtpSeq")Long qtpSeq);
	 
	 /**
	  * 保存到 QTP
	  * @return
	  */
	 void insertQtpSelect(EcrProjectTracking dto);
	 
	 /**
	  * 查询申请页面的产品品类 
	  * @param itemId
	  * @return
	  */
	 String selectCategory(@Param("itemId")String itemId);
	 
	 /**
	  * 查询物料主负责人
	  * @param ecrno
	  * @return
	  */
	 String selectMainDuty(@Param("ecrno")String ecrno);
	 
	 /**
	  * 更新QTP主负责人 
	  * @param qtpId
	  * @param mainDuty
	  * @return
	  */
	 void updateQtpDutyby(@Param("ecrno")String ecrno, @Param("itemId")String itemId,
			 @Param("qtpSeq")Long qtpSeq, @Param("mainDuty")String mainDuty);
	/**----------------------------------  QTP 任务 -----------------------------------*/
	 
	 
	 
    /**----------------------------------  VTP 任务 -----------------------------------*/
	 /**
	  * 查询技术文件状态已完成
	 * @param ecrno 
	  * @param fileTypes 文件类型
	 * @param fileTypes 
	  * @return
	  */
	 String selectFilesItemId(@Param("ecrno")String ecrno,
			 @Param("types")List<String> fileTypes);
	 
	 /**
	  * 查询 QTP 表状态
	  * @param fileTypes 文件类型
	  * @return
	  */
	 String selectQtpStatus(@Param("ecrno")String ecrno, @Param("itemId")String itemId);
	 
	 
	 /**
	  * 查询VTP次序
	  * @return
	  */
	 Long selectVtpSeq(@Param("ecrno")String ecrno, @Param("itemId")String itemId);
	 
	 /**
	  * 保存到 VTP
	  * @return
	  */
	 Long insertVtp(@Param("ecrno")String ecrno, @Param("itemId")String itemId,
			 @Param("vtpSeq")Long vtpSeq);
	 
	 /**
	  * 查询 VTP 主负责人
	  * @param ecrno
	  * @return
	  */
	 List<String> selectVtpMainDuty(@Param("itemId")String itemId);
	 
	 /**
	  * 更新 VTP 主负责人 
	  * @param qtpId
	  * @param mainDuty
	  * @return
	  */
	 void updateVtpDutyby(@Param("ecrno")String ecrno, @Param("itemId")String itemId,
			 @Param("vtpSeq")Long qtpSeq, @Param("mainDuty")String mainDuty);
    /**----------------------------------  VTP 任务 -----------------------------------*/
	 
	 
}