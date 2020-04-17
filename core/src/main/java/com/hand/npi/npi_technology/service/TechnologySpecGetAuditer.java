package com.hand.npi.npi_technology.service;

import java.util.ArrayList;
import java.util.List;

import org.activiti.engine.delegate.DelegateExecution;
import org.springframework.stereotype.Service;
import org.springframework.web.context.ContextLoader;

import com.hand.hap.activiti.custom.IActivitiBean;
import com.hand.npi.npi_technology.dto.TechnologySpecAuditer;
import com.hand.npi.npi_technology.mapper.TechnologySpecAuditerMapper;

@Service
public class TechnologySpecGetAuditer implements IActivitiBean {

	public List<String> getEnginee(DelegateExecution execution) {
		TechnologySpecAuditerMapper technologySpecAuditerMapper = ContextLoader.getCurrentWebApplicationContext()
				.getBean(TechnologySpecAuditerMapper.class);
		
		List<String> list = new ArrayList<String>();
		
		TechnologySpecAuditer technologySpecAuditer = new TechnologySpecAuditer();
		technologySpecAuditer.setStartCode(String.valueOf(execution.getVariable("startCode")));
		technologySpecAuditer.setProcessName("动作要求审批流程");
		List<TechnologySpecAuditer> auditerList = technologySpecAuditerMapper.select(technologySpecAuditer);
		if(auditerList != null && auditerList.size() > 0) {
			String enginee = auditerList.get(0).getAuditerCode();
			list.add(enginee);
		} 
		
		return list;
	}
	
	@Override
	public String getBeanName() {
		return "TechnologySpecGetAuditer";
		
	}
}
