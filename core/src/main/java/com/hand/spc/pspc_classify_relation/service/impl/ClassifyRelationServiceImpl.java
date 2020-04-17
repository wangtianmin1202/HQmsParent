package com.hand.spc.pspc_classify_relation.service.impl;

import com.hand.hap.system.dto.ResponseData;
import com.hand.hap.system.service.impl.BaseServiceImpl;
import com.hand.spc.pspc_classify_relation.mapper.ClassifyRelationMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.hand.spc.pspc_classify_relation.dto.ClassifyRelation;
import com.hand.spc.pspc_classify_relation.service.IClassifyRelationService;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(rollbackFor = Exception.class)
public class ClassifyRelationServiceImpl extends BaseServiceImpl<ClassifyRelation> implements IClassifyRelationService{

}