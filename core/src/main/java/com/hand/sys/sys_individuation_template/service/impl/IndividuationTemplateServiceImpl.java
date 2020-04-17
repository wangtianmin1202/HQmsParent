package com.hand.sys.sys_individuation_template.service.impl;

import com.hand.hap.system.service.impl.BaseServiceImpl;
import org.springframework.stereotype.Service;
import com.hand.sys.sys_individuation_template.dto.IndividuationTemplate;
import com.hand.sys.sys_individuation_template.service.IIndividuationTemplateService;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(rollbackFor = Exception.class)
public class IndividuationTemplateServiceImpl extends BaseServiceImpl<IndividuationTemplate> implements IIndividuationTemplateService{

}