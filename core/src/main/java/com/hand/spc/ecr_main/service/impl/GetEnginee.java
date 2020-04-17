package com.hand.spc.ecr_main.service.impl;


import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.hand.hap.activiti.custom.IActivitiBean;
import com.hand.spc.ecr_main.mapper.EcrMainMapper;
import com.hand.spc.ecr_main.dto.EcrMain;

import org.activiti.engine.delegate.DelegateExecution;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@Service
public class GetEnginee implements IActivitiBean{
	
	@Autowired
	private EcrMainMapper ecrMainMapper;
	
	private final Logger logger = LoggerFactory.getLogger(GetEnginee.class); 
	
	public List<String> getEnginee(DelegateExecution execution){
		logger.debug("------------- 获取负责工程师-----------------");
		// Long processInstanceId = Long.valueOf(execution.getProcessInstanceId());
		Long businessKey = Long.valueOf(execution.getProcessInstanceBusinessKey());
		EcrMain ercMain = new EcrMain();
		ercMain.setKid(businessKey);
		EcrMain approver = ecrMainMapper.selectByPrimaryKey(ercMain);
		// 返回审批人：负责工程师
		List<String> empList = new ArrayList<>();
		empList.add("wl");
		// empList.add(approver.getProcessEmployeeCode());
		return empList;
	}
	
	@Override
	public String getBeanName() {
		return "GetEnginee";
		
	}
	
}
