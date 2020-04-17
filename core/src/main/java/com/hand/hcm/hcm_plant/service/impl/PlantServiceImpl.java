package com.hand.hcm.hcm_plant.service.impl;

import com.hand.hap.system.service.impl.BaseServiceImpl;
import org.springframework.stereotype.Service;
import com.hand.hcm.hcm_plant.dto.Plant;
import com.hand.hcm.hcm_plant.service.IPlantService;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(rollbackFor = Exception.class)
public class PlantServiceImpl extends BaseServiceImpl<Plant> implements IPlantService{

}