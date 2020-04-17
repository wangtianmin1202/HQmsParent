package com.hand.hqm.hqm_iqc_inspection_template_l_his.service.impl;

import com.hand.hap.system.service.impl.BaseServiceImpl;
import org.springframework.stereotype.Service;
import com.hand.hqm.hqm_iqc_inspection_template_l_his.dto.IqcInspectionTemplateLHis;
import com.hand.hqm.hqm_iqc_inspection_template_l_his.service.IIqcInspectionTemplateLHisService;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(rollbackFor = Exception.class)
public class IqcInspectionTemplateLHisServiceImpl extends BaseServiceImpl<IqcInspectionTemplateLHis> implements IIqcInspectionTemplateLHisService{

}