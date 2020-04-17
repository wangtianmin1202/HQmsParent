package com.hand.hqm.hqm_sam_use_order_l.mapper;

import java.util.List;

import com.hand.hap.mybatis.common.Mapper;
import com.hand.hqm.hqm_sam_use_order_l.dto.SamUseOrderL;

public interface SamUseOrderLMapper extends Mapper<SamUseOrderL>{
	/**
     * 页面查询
     * @param dto 查询内容
     * @return 结果集
     */
	List<SamUseOrderL> myselect(SamUseOrderL dto);
}