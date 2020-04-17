package com.hand.hqm.hqm_nonconforming_order.service;

import java.util.List;

import org.activiti.engine.delegate.DelegateExecution;
import org.springframework.stereotype.Service;
import org.springframework.web.context.ContextLoader;

import com.hand.hap.activiti.custom.IActivitiBean;
import com.hand.hqm.hqm_fqc_inspection_h.dto.FqcInspectionH;
import com.hand.hqm.hqm_fqc_inspection_h.mapper.FqcInspectionHMapper;
import com.hand.hqm.hqm_nonconforming_order.dto.NonconformingOrder;
import com.hand.hqm.hqm_nonconforming_order.mapper.NonconformingOrderMapper;
import com.hand.hqm.hqm_qc_task.dto.FqcTask;
import com.hand.hqm.hqm_qc_task.mapper.FqcTaskMapper;

@Service
public class FqcProcessCheckWay implements IActivitiBean {
	
	public void dealCheck(DelegateExecution execution) {
		NonconformingOrderMapper nonconformingOrderMapper = ContextLoader.getCurrentWebApplicationContext()
				.getBean(NonconformingOrderMapper.class);
		
		FqcInspectionHMapper fqcInspectionHMapper = ContextLoader.getCurrentWebApplicationContext()
				.getBean(FqcInspectionHMapper.class);
		
		FqcTaskMapper fqcTaskMapper = ContextLoader.getCurrentWebApplicationContext()
				.getBean(FqcTaskMapper.class);
		
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
				} else if("2".equals(nonconformingOrderR.getDealMethod())) {//返工
					execution.setVariable("dealWayResult", String.valueOf(2));
				} else if("3".equals(nonconformingOrderR.getDealMethod())) {//特采
					FqcInspectionH fqcInspectionH = new FqcInspectionH();
					fqcInspectionH.setHeaderId(nonconformingOrderR.getInspectionId());
					List<FqcInspectionH> fqcInspectionHList = fqcInspectionHMapper.select(fqcInspectionH);
					if (fqcInspectionHList != null && fqcInspectionHList.size() > 0) {
						FqcTask fqcTaskSearch = new FqcTask();
						fqcTaskSearch.setInspectionNum(fqcInspectionHList.get(0).getInspectionNum());
						List<FqcTask> fqcTaskResult = fqcTaskMapper.select(fqcTaskSearch);
						if (fqcTaskResult != null && fqcTaskResult.size() > 0) {
							boolean isAfterPro = false;
							for(FqcTask ft:fqcTaskResult) {
								if("16".equals(ft.getSourceType())) {
									isAfterPro = true;
								}
							}
							
							if(isAfterPro) {//特采 + 量产后
								execution.setVariable("dealWayResult", String.valueOf(4));
							} else {//特采 + 量产前
								execution.setVariable("dealWayResult", String.valueOf(3));
							}
						} else {//默认量产后
							execution.setVariable("dealWayResult", String.valueOf(4));
						}
					}
				} else if("1".equals(nonconformingOrderR.getDealMethod()) && "1".equals(nonconformingOrderR.getIssueType())) {//放行+非功能类问题
					execution.setVariable("dealWayResult", String.valueOf(5));
				} else if("1".equals(nonconformingOrderR.getDealMethod()) && "2".equals(nonconformingOrderR.getIssueType())) {//放行+功能类问题
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
		return "FqcProcessCheckWay";
	}
}
