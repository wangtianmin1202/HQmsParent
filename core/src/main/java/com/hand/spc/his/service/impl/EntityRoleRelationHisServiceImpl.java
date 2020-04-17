package com.hand.spc.his.service.impl;

import com.hand.hap.system.service.impl.BaseServiceImpl;
import org.springframework.stereotype.Service;
import com.hand.spc.his.dto.EntityRoleRelationHis;
import com.hand.spc.his.service.IEntityRoleRelationHisService;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(rollbackFor = Exception.class)
public class EntityRoleRelationHisServiceImpl extends BaseServiceImpl<EntityRoleRelationHis> implements IEntityRoleRelationHisService{

}