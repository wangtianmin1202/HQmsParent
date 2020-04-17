package com.hand.hqm.hqm_stan_op_item_h.mapper;

import java.util.List;

import com.hand.hap.mybatis.common.Mapper;
import com.hand.hqm.hqm_stan_op_item_h.dto.StandardOpItemH;
import com.hand.hqm.hqm_standard_op_ins_h.dto.StandardOpInspectionH;

public interface StandardOpItemHMapper extends Mapper<StandardOpItemH>{
	List<StandardOpItemH> myselect(StandardOpItemH dto);
	
	/**
	 * 
	 * @description 新建临时检验项的 检验组查询
	 * @author tianmin.wang
	 * @date 2019年12月12日 
	 * @param dto
	 * @return
	 */
	List<StandardOpItemH> selectByCategoryId(com.hand.hqm.hqm_temporary_inspection.dto.TemporaryInspection dto);

}