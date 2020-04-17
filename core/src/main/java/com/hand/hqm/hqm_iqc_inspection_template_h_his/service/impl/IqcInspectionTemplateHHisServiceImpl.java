package com.hand.hqm.hqm_iqc_inspection_template_h_his.service.impl;

import com.hand.hap.system.service.impl.BaseServiceImpl;
import org.springframework.stereotype.Service;
import com.hand.hqm.hqm_iqc_inspection_template_h_his.dto.IqcInspectionTemplateHHis;
import com.hand.hqm.hqm_iqc_inspection_template_h_his.service.IIqcInspectionTemplateHHisService;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(rollbackFor = Exception.class)
public class IqcInspectionTemplateHHisServiceImpl extends BaseServiceImpl<IqcInspectionTemplateHHis> implements IIqcInspectionTemplateHHisService{

}