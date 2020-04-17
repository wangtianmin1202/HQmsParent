package com.hand.hqm.hqm_iqc_inspection_template_h.service;

import org.activiti.engine.delegate.DelegateExecution;
import org.springframework.stereotype.Service;
import org.springframework.web.context.ContextLoader;

import com.hand.hap.activiti.custom.IActivitiBean;
import com.hand.hqm.hqm_iqc_inspection_template_h.dto.IqcInspectionTemplateH;
import com.hand.hqm.hqm_iqc_inspection_template_h.mapper.IqcInspectionTemplateHMapper;

@Service
public class IqcTaskExamine implements IActivitiBean {

	public void auditRes(DelegateExecution execution) {
		IqcInspectionTemplateHMapper iqcInspectionTemplateHMapper = ContextLoader.getCurrentWebApplicationContext()
				.getBean(IqcInspectionTemplateHMapper.class);
		
		float headerId = Float.valueOf(execution.getProcessInstanceBusinessKey());
		
		try {
			IqcInspectionTemplateH search = new IqcInspectionTemplateH();
			search.setHeaderId((float)headerId);
			search = iqcInspectionTemplateHMapper.selectOne(search);
			IqcInspectionTemplateH updateDto = new IqcInspectionTemplateH();
			updateDto.setHeaderId(search.getHeaderId());
			updateDto.setHistoryNum(search.getHistoryNum() + 1);
			updateDto.setVersionNum(String.valueOf(Integer.valueOf(search.getVersionNum()) + 1));
			updateDto.setStatus("3");
			iqcInspectionTemplateHMapper.updateByPrimaryKeySelective(updateDto);
			
			execution.setVariable("amineResult", Boolean.TRUE); 
			//execution.setVariable("comment", "审核成功");
		} catch (Exception e) {
			execution.setVariable("amineResult", Boolean.FALSE);
		}
	}
	
	@Override
	public String getBeanName() {
		return "IqcTaskExamine";
	}
	
}
