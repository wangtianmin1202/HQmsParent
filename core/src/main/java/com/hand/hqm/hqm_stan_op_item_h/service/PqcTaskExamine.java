package com.hand.hqm.hqm_stan_op_item_h.service;

import org.activiti.engine.delegate.DelegateExecution;
import org.springframework.stereotype.Service;
import org.springframework.web.context.ContextLoader;

import com.hand.hap.activiti.custom.IActivitiBean;
import com.hand.hqm.hqm_stan_op_item_h.dto.StandardOpItemH;
import com.hand.hqm.hqm_stan_op_item_h.mapper.StandardOpItemHMapper;

@Service
public class PqcTaskExamine implements IActivitiBean {

	public void auditRes(DelegateExecution execution) {
		StandardOpItemHMapper standardOpItemHMapper = ContextLoader.getCurrentWebApplicationContext()
				.getBean(StandardOpItemHMapper.class);
		
		float headerId = Float.valueOf(execution.getProcessInstanceBusinessKey());
		
		try {
			StandardOpItemH standardOpItemH = new StandardOpItemH();
			standardOpItemH.setHeadId((float)headerId);
			standardOpItemH.setStatus("3");
			
			standardOpItemHMapper.updateByPrimaryKeySelective(standardOpItemH);
			
			execution.setVariable("amineResult", Boolean.TRUE); 
		} catch (Exception e) {
			execution.setVariable("amineResult", Boolean.FALSE); 
		}
	}

	@Override
	public String getBeanName() {
		return "PqcTaskExamine";
	}
}
