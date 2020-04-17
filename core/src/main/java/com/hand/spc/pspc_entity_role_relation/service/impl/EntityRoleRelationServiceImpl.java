package com.hand.spc.pspc_entity_role_relation.service.impl;

import com.hand.hap.system.service.impl.BaseServiceImpl;
import org.springframework.stereotype.Service;
import com.hand.spc.pspc_entity_role_relation.dto.EntityRoleRelation;
import com.hand.spc.pspc_entity_role_relation.service.IEntityRoleRelationService;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(rollbackFor = Exception.class)
public class EntityRoleRelationServiceImpl extends BaseServiceImpl<EntityRoleRelation> implements IEntityRoleRelationService{

}