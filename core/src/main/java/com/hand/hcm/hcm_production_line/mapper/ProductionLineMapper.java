package com.hand.hcm.hcm_production_line.mapper;

import com.hand.hap.mybatis.common.Mapper;
import com.hand.hcm.hcm_production_line.dto.ProductionLine;

public interface ProductionLineMapper extends Mapper<ProductionLine>{

	/**
	 * @description
	 * @author tianmin.wang
	 * @date 2020年3月9日 
	 * @param pl
	 * @return
	 */
	String queryShiftCode(ProductionLine pl);

}