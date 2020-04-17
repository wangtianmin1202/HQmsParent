package com.hand.plm.spec_product_detail.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.hand.hap.mybatis.common.Mapper;
import com.hand.plm.spec_product_detail.dto.SpecProductDetail;


public interface SpecProductDetailMapper extends Mapper<SpecProductDetail>{
	/**
	 * 查询左边树状层级
	 * @param condition
	 * @return
	 */
	 List<SpecProductDetail> selectTreeDatas();
	 
	 /**
	  * 查询右边表格数据
	  * @return
	  */
	 List<SpecProductDetail> queryAll(@Param("tmp") String tmp, @Param("specLineIdList") List<String> specLineIdList);
	 
	 /**
	  * 废止时更新明细表中状态为：审批中
	  * @param list
	  */
	 void updateSpecLineIdStatus(@Param("list") List<SpecProductDetail> list);
	 
	 /**
	  * 通过lineId去复制一条明细数据到变更行表里（修改时）
	  * @param changeOrderId
	  * @param specLineId
	  */
	 void insertSpecChangeLinePen(@Param("changeOrderId") Long changeOrderId, @Param("specLineId") Long specLineId);
	 
	 /**
	  * 通过lineId去复制一条明细数据到变更行表里（除修改外）
	  * @param changeOrderId
	  * @param specLineId
	  */
	 void insertSpecChangeLineDdt(@Param("changeOrderId") Long changeOrderId, @Param("specLineId") Long specLineId);
	 
	 /**
	  * 点击右边树查询
	  * @return
	  */
	 List<String> queryLastSpecId(@Param("specId") Long specId);
	 
	 /**
	  * 下拉框查询节点子代
	  * @return
	  */
	 List<SpecProductDetail> selectTreeChild(@Param("specId") Long specId);
	 
	 /**
	  * 	查询主键
	  * @return
	  */
	 Long selectKey();
	 
	 /**
	  *	 查询当前层级
	  * @param levelNum
	  * @return
	  */
	 List<SpecProductDetail> selectLevelInfo(@Param("levelNum") Long levelNum);
	 
	 /**
	  *	 查询下级层级
	  * @param levelNum
	  * @return
	  */
	 List<SpecProductDetail> selectLevelInfoById(@Param("parentSpecId") Long parentSpecId);
	 
	 /**
	  * 	查询草稿数据
	  * @return
	  */
	 List<SpecProductDetail> queryAllPendding();
	 
	 /**
	  * 	查询已废止数据
	  * @return
	  */
	 List<SpecProductDetail> queryAllHis();
	 
	 /**
	  * 	查询首页父级数据
	  * @return
	  */
	 List<SpecProductDetail> selectParentTree();
	 
	  /**
	  *  查询废止数据
	  * @return
	  */
	 Long hisCount();
	 
}