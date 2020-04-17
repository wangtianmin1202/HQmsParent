package com.hand.hqm.hqm_nonconforming_order.service;

import java.util.List;

import org.activiti.engine.delegate.DelegateExecution;
import org.springframework.stereotype.Service;
import org.springframework.web.context.ContextLoader;

import com.hand.hap.activiti.custom.IActivitiBean;
import com.hand.hqm.hqm_nonconforming_order.dto.NonconformingOrder;
import com.hand.hqm.hqm_nonconforming_order.mapper.NonconformingOrderMapper;

@Service
public class ProcessCheckSourceType implements IActivitiBean {

	public void dealCheck(DelegateExecution execution) {
		NonconformingOrderMapper nonconformingOrderMapper = ContextLoader.getCurrentWebApplicationContext()
				.getBean(NonconformingOrderMapper.class);
		
		float noid = Float.valueOf(execution.getProcessInstanceBusinessKey());
		NonconformingOrder nonconformingOrder = new NonconformingOrder();
		nonconformingOrder.setNoId(noid);
		
		List<NonconformingOrder> nonconformingOrderList = nonconformingOrderMapper.select(nonconformingOrder);
		nonconformingOrder = nonconformingOrderList.get(0);
		if("1".equals(nonconformingOrder.getIssueSourceType())) {
			execution.setVariable("dealSourceTypeResult", String.valueOf(1));
		} else if("2".equals(nonconformingOrder.getIssueSourceType())) {
			execution.setVariable("dealSourceTypeResult", String.valueOf(2));
		}
	}

	@Override
	public String getBeanName() {
		return "ProcessCheckSourceType";
	}
	
}
 