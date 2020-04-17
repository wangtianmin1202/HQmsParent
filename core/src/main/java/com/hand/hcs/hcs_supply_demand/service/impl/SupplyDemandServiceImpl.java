package com.hand.hcs.hcs_supply_demand.service.impl;

import com.hand.hap.system.service.impl.BaseServiceImpl;
import org.springframework.stereotype.Service;
import com.hand.hcs.hcs_supply_demand.dto.SupplyDemand;
import com.hand.hcs.hcs_supply_demand.service.ISupplyDemandService;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(rollbackFor = Exception.class)
public class SupplyDemandServiceImpl extends BaseServiceImpl<SupplyDemand> implements ISupplyDemandService{

}