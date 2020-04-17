package com.hand.hqm.hqm_program_sku_rel.mapper;

import java.util.List;

import com.hand.hap.mybatis.common.Mapper;
import com.hand.hqm.hqm_program_sku_rel.dto.ProgramSkuRel;

public interface ProgramSkuRelMapper extends Mapper<ProgramSkuRel>{

	/**
	 * @description
	 * @author tianmin.wang
	 * @date 2020年1月9日 
	 * @param dto
	 * @return
	 */
	List<ProgramSkuRel> reSelect(ProgramSkuRel dto);

}