package com.hand.hqm.hqm_nonconforming_order.service;

import java.util.List;

import org.activiti.engine.delegate.DelegateExecution;
import org.springframework.stereotype.Service;
import org.springframework.web.context.ContextLoader;

import com.hand.hap.activiti.custom.IActivitiBean;
import com.hand.hqm.hqm_nonconforming_order.dto.NonconformingOrder;
import com.hand.hqm.hqm_nonconforming_order.mapper.NonconformingOrderMapper;

@Service
public class ProcessCheckWay implements IActivitiBean {

	public void dealCheck(DelegateExecution execution) {
		NonconformingOrderMapper nonconformingOrderMapper = ContextLoader.getCurrentWebApplicationContext()
				.getBean(NonconformingOrderMapper.class);
		
		float noid = Float.valueOf(execution.getProcessInstanceBusinessKey());
		NonconformingOrder nonconformingOrder = new NonconformingOrder();
		nonconformingOrder.setNoId(noid);
		
		execution.setVariable("dealWayResult", String.valueOf(7));
		try {
			List<NonconformingOrder> nonconformingOrderList = nonconformingOrderMapper.select(nonconformingOrder);
			if(nonconformingOrderList != null && nonconformingOrderList.size() > 0) {
				NonconformingOrder nonconformingOrderR = nonconformingOrderList.get(0);
				if("4".equals(nonconformingOrderR.getDealMethod())) {//加严抽检
					execution.setVariable("dealWayResult", String.valueOf(1));
				} else if("1".equals(nonconformingOrderR.getDealMethod()) && "1".equals(nonconformingOrderR.getIssueType())) {//放行+非功能类问题
					execution.setVariable("dealWayResult", String.valueOf(2));
				} else if("1".equals(nonconformingOrderR.getDealMethod()) && "2".equals(nonconformingOrderR.getIssueType())) {//放行+功能类问题
					execution.setVariable("dealWayResult", String.valueOf(3));
				} else if("3".equals(nonconformingOrderR.getDealMethod())) {//特采
					execution.setVariable("dealWayResult", String.valueOf(4));
				} else if("0".equals(nonconformingOrderR.getDealMethod())) {//退货
					execution.setVariable("dealWayResult", String.valueOf(5));
				} else if("2".equals(nonconformingOrderR.getDealMethod())) {//返工
					execution.setVariable("dealWayResult", String.valueOf(6));
				}
			} else {
				execution.setVariable("dealWayResult", String.valueOf(7));
			}
		} catch (Exception e) {
			execution.setVariable("dealWayResult", String.valueOf(7));
		}
		
	}

	@Override
	public String getBeanName() {
		return "ProcessCheckWay";
	}
}
