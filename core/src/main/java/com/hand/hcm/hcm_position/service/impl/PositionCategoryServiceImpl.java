package com.hand.hcm.hcm_position.service.impl;

import com.hand.hap.system.service.impl.BaseServiceImpl;
import org.springframework.stereotype.Service;
import com.hand.hcm.hcm_position.dto.PositionCategory;
import com.hand.hcm.hcm_position.service.IPositionCategoryService;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(rollbackFor = Exception.class)
public class PositionCategoryServiceImpl extends BaseServiceImpl<PositionCategory> implements IPositionCategoryService{

}