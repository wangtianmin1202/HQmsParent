package com.hand.plm.spec_product_detail.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.hand.hap.mybatis.common.Mapper;
import com.hand.plm.spec_product_detail.dto.SpecChangeOrder;
import com.hand.plm.spec_product_detail.dto.SpecProductDetail;

public interface SpecChangeOrderMapper extends Mapper<SpecChangeOrder>{

	/**
	 * 获取变更编码序列号
	 * @return
	 */
	String getChangeOrderCode();
	
	 /**
	  * 查询右边表格数据
	  * @return
	  */
	 List<SpecChangeOrder> queryAll(SpecChangeOrder changeOrder);
}