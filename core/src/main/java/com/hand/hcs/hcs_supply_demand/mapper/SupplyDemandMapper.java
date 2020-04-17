package com.hand.hcs.hcs_supply_demand.mapper;

import java.util.List;

import com.hand.hap.mybatis.common.Mapper;
import com.hand.hcs.hcs_supply_demand.dto.SupplyDemand;

public interface SupplyDemandMapper extends Mapper<SupplyDemand>{
	List<SupplyDemand> selectMaxSupplyDemandNum(SupplyDemand dto);
	
	List<SupplyDemand> selectWeeksGroup(SupplyDemand dto);
	
	List<SupplyDemand> selectLeadTime(SupplyDemand dto);
}