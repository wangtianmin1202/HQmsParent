package com.hand.hcs.hcs_business_unit.mapper;

import java.util.List;

import com.hand.hap.mybatis.common.Mapper;
import com.hand.hcs.hcs_business_unit.dto.BusinessUnit;

public interface BusinessUnitMapper extends Mapper<BusinessUnit>{
	/**
	 * 业务实体LOV
	 * @param businessUnit
	 * @return
	 */
	List<BusinessUnit> buinessUnitLov(BusinessUnit businessUnit);
}