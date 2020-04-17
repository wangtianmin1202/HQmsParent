package com.hand.hqm.hqm_nonconforming_order.service;

import java.util.ArrayList;
import java.util.List;

import org.activiti.engine.delegate.DelegateExecution;

import com.github.pagehelper.StringUtil;
import com.hand.hap.activiti.custom.IActivitiBean;
import com.hand.hqm.hqm_iqc_inspection_h.dto.IqcInspectionH;
import com.hand.hqm.hqm_iqc_inspection_h.mapper.IqcInspectionHMapper;
import com.hand.hqm.hqm_iqc_inspection_h.service.IIqcInspectionHService;
import com.hand.hqm.hqm_nonconforming_order.dto.NonconformingOrder;
import com.hand.hqm.hqm_nonconforming_order.mapper.NonconformingOrderMapper;
import com.hand.hqm.hqm_qc_task.dto.IqcTask;
import com.hand.hqm.hqm_qc_task.mapper.IqcTaskMapper;
import com.hand.hqm.hqm_qc_task.service.IIqcTaskService;

import org.springframework.stereotype.Service;
import org.springframework.web.context.ContextLoader;

@Service
public class IqcUnqualityExamine implements IActivitiBean {

	public void auditRes(DelegateExecution execution) {
		NonconformingOrderMapper nonconformingOrderMapper = ContextLoader.getCurrentWebApplicationContext()
				.getBean(NonconformingOrderMapper.class);
		IqcInspectionHMapper iqcInspectionHMapper = ContextLoader.getCurrentWebApplicationContext()
				.getBean(IqcInspectionHMapper.class);
		IqcTaskMapper iqcTaskMapper = ContextLoader.getCurrentWebApplicationContext()
				.getBean(IqcTaskMapper.class);
		IIqcTaskService iIqcTaskService = ContextLoader.getCurrentWebApplicationContext()
				.getBean(IIqcTaskService.class);
		IIqcInspectionHService iIqcInspectionHService = ContextLoader.getCurrentWebApplicationContext()
				.getBean(IIqcInspectionHService.class);
		
		float noid = Float.valueOf(execution.getProcessInstanceBusinessKey());
		NonconformingOrder nonconformingOrder = new NonconformingOrder();
		nonconformingOrder.setNoId(noid);
		
		try {
			List<NonconformingOrder> nonconformingOrderList = nonconformingOrderMapper.select(nonconformingOrder);
			if(nonconformingOrderList != null && nonconformingOrderList.size() > 0) {
				NonconformingOrder dto = nonconformingOrderList.get(0);
				if (!dto.getNoStatus().equals("1")) {
					execution.setVariable("amineResult", Boolean.FALSE);
				} else {
					dto.setNoStatus("2");
					nonconformingOrderMapper.updateByPrimaryKeySelective(dto);
					
					IqcInspectionH iqcHSearch = new IqcInspectionH();
					iqcHSearch.setHeaderId(dto.getInspectionId());
					List<IqcInspectionH> iqcHResult = iqcInspectionHMapper.select(iqcHSearch);
					if (iqcHResult != null && iqcHResult.size() > 0) {
						IqcTask iqcTaskSearch = new IqcTask();
						iqcTaskSearch.setInspectionNum(iqcHResult.get(0).getInspectionNum());
						List<IqcTask> iqcTaskResult = iqcTaskMapper.select(iqcTaskSearch);
						if (iqcTaskResult != null && iqcTaskResult.size() > 0) {
							iqcTaskResult.forEach(p -> {
								p.setDealMethod(dto.getDealMethod());
								iqcTaskMapper.updateByPrimaryKeySelective(p);
							});
						}
					}
					/**
					 * 若DEAL_METHOD为 4 则往HQM_IQC_TASK新增一条数据
					 */
					if (StringUtil.isNotEmpty(dto.getDealMethod()) && "4".equals(dto.getDealMethod())) {
						IqcInspectionH iqchSearch = new IqcInspectionH();
						iqchSearch.setHeaderId(dto.getInspectionId());
						iqchSearch = iqcInspectionHMapper.selectByPrimaryKey(iqchSearch);
						IqcTask insert = new IqcTask();
						insert.setPlantId(dto.getPlantId());
						insert.setSourceOrder(iqchSearch.getSourceOrder());
						insert.setSourceType("0");

						IqcTask iqcTaskEarlist = new IqcTask();
						List<IqcTask> res = iqcTaskMapper.getIqcTaskEarlistBySourceOrder(dto.getSourceOrder());
						if (res == null || res.size() == 0) {
							iqcTaskEarlist = null;
						} else {
							iqcTaskEarlist = res.get(0);
						}
						
						if (iqcTaskEarlist == null) {
							return;
						}
						insert.setItemId(iqcTaskEarlist.getItemId());
						insert.setItemVersion(iqcTaskEarlist.getItemVersion());
						insert.setSpreading(iqcTaskEarlist.getSpreading());
						insert.setProductionBatch(iqcTaskEarlist.getProductionBatch());
						insert.setSupplierId(iqcTaskEarlist.getSupplierId());
						insert.setSupplierSiteId(iqcTaskEarlist.getSupplierSiteId());
						insert.setInspectionType("IQC");
						insert.setReceiveQty(iqcTaskEarlist.getReceiveQty());
						insert.setReceiveDate(iqcTaskEarlist.getReceiveDate());
						insert.setPackQty(iqcTaskEarlist.getPackQty());
						insert.setPacketInfo(iqcTaskEarlist.getPacketInfo());
						insert.setPoNumber(iqcTaskEarlist.getPoNumber());
						insert.setRecipientNum(iqcTaskEarlist.getRecipientNum());
						insert.setRecheckSampleWay(dto.getRecheckSampleWay());
						iqcTaskMapper.insertSelective(insert);
						
						// IQC审核 发送到 WMS
						iIqcInspectionHService.conclusionToWMS(iqcHResult.get(0));
						
						iIqcTaskService.createIqc(insert, null); // 生成报告的逻辑
						iqcTaskMapper.updateByPrimaryKeySelective(insert);
					}
					execution.setVariable("amineResult", Boolean.TRUE);
				}
			} else {
				execution.setVariable("amineResult", Boolean.FALSE);
			}
		} catch (Exception e) {
			execution.setVariable("amineResult", Boolean.FALSE);
		}
	}

	@Override
	public String getBeanName() {
		return "IqcUnqualityExamine";
	}
}
