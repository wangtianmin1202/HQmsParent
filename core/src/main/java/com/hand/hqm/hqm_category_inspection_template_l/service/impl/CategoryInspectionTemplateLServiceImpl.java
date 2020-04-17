package com.hand.hqm.hqm_category_inspection_template_l.service.impl;

import com.hand.hap.system.service.impl.BaseServiceImpl;
import org.springframework.stereotype.Service;
import com.hand.hqm.hqm_category_inspection_template_l.dto.CategoryInspectionTemplateL;
import com.hand.hqm.hqm_category_inspection_template_l.service.ICategoryInspectionTemplateLService;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(rollbackFor = Exception.class)
public class CategoryInspectionTemplateLServiceImpl extends BaseServiceImpl<CategoryInspectionTemplateL> implements ICategoryInspectionTemplateLService{

}