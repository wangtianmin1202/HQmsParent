package com.hand.spc.his.service.impl;

import com.hand.hap.system.service.impl.BaseServiceImpl;
import org.springframework.stereotype.Service;
import com.hand.spc.his.dto.EntityHis;
import com.hand.spc.his.service.IEntityHisService;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(rollbackFor = Exception.class)
public class EntityHisServiceImpl extends BaseServiceImpl<EntityHis> implements IEntityHisService{

}