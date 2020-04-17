package com.hand.hqm.hqm_nonconforming_order.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.activiti.engine.delegate.DelegateExecution;
import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.pagehelper.StringUtil;
import com.hand.hap.activiti.custom.IActivitiBean;
import com.hand.hap.core.impl.ServiceRequest;
import com.hand.hap.system.service.ICodeService;
import com.hand.hap.util.SoapPostUtil;
import com.hand.hqm.hqm_fqc_inspection_d.dto.FqcInspectionD;
import com.hand.hqm.hqm_fqc_inspection_d.mapper.FqcInspectionDMapper;
import com.hand.hqm.hqm_fqc_inspection_h.dto.FqcInspectionH;
import com.hand.hqm.hqm_fqc_inspection_h.mapper.FqcInspectionHMapper;
import com.hand.hqm.hqm_fqc_inspection_h.service.IFqcInspectionHService;
import com.hand.hqm.hqm_fqc_inspection_l.dto.FqcInspectionL;
import com.hand.hqm.hqm_fqc_inspection_l.mapper.FqcInspectionLMapper;
import com.hand.hqm.hqm_nonconforming_order.dto.NonconformingOrder;
import com.hand.hqm.hqm_nonconforming_order.idto.FqcInpectionDealMethod;
import com.hand.hqm.hqm_nonconforming_order.mapper.NonconformingOrderMapper;
import com.hand.hqm.hqm_qc_task.dto.FqcTask;
import com.hand.hqm.hqm_qc_task.mapper.FqcTaskMapper;
import com.hand.hqm.hqm_qc_task.service.IFqcTaskService;
import com.hand.sys.sys_if_invoke_outbound.dto.IfInvokeOutbound;
import com.hand.sys.sys_if_invoke_outbound.mapper.IfInvokeOutboundMapper;

import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.web.context.ContextLoader;

@Service
public class FqcUnqualityExamine implements IActivitiBean {

	Logger logger = LoggerFactory.getLogger(FqcUnqualityExamine.class);
	
	public void auditRes(DelegateExecution execution) {
		NonconformingOrderMapper nonconformingOrderMapper = ContextLoader.getCurrentWebApplicationContext()
				.getBean(NonconformingOrderMapper.class);
		FqcInspectionHMapper fqcInspectionHMapper = ContextLoader.getCurrentWebApplicationContext()
				.getBean(FqcInspectionHMapper.class);
		FqcTaskMapper fqcTaskMapper = ContextLoader.getCurrentWebApplicationContext()
				.getBean(FqcTaskMapper.class);
		IFqcTaskService iFqcTaskService = ContextLoader.getCurrentWebApplicationContext()
				.getBean(IFqcTaskService.class);
		IFqcInspectionHService iFqcInspectionHService = ContextLoader.getCurrentWebApplicationContext()
				.getBean(IFqcInspectionHService.class);
		FqcInspectionLMapper fqcInspectionLMapper = ContextLoader.getCurrentWebApplicationContext()
				.getBean(FqcInspectionLMapper.class);
		
		float noid = Float.valueOf(execution.getProcessInstanceBusinessKey());
		NonconformingOrder nonconformingOrder = new NonconformingOrder();
		nonconformingOrder.setNoId(noid);
		
		try {
			List<NonconformingOrder> nonconformingOrderList = nonconformingOrderMapper.select(nonconformingOrder);
			if(nonconformingOrderList != null && nonconformingOrderList.size() > 0) {
				NonconformingOrder dto = nonconformingOrderList.get(0);
				if (!dto.getNoStatus().equals("1")) {
					execution.setVariable("amineResult", Boolean.FALSE);
				}else {
					dto.setNoStatus("2");
					nonconformingOrderMapper.updateByPrimaryKeySelective(dto);
					
					/*
					 * 检验单号关联HQM_FQC_TASK 更新HQM_IQC_TASK的DEAL_METHOD为不合格处理单的DEAL_METHOD
					 */
					FqcInspectionH fqcHSearch = new FqcInspectionH();
					fqcHSearch.setHeaderId(dto.getInspectionId());
					List<FqcInspectionH> fqcHResult = fqcInspectionHMapper.select(fqcHSearch);
					if (fqcHResult != null && fqcHResult.size() > 0) {
						FqcTask fqcTaskSearch = new FqcTask();
						fqcTaskSearch.setInspectionNum(fqcHResult.get(0).getInspectionNum());
						List<FqcTask> fqcTaskResult = fqcTaskMapper.select(fqcTaskSearch);
						if (fqcTaskResult != null && fqcTaskResult.size() > 0) {
							fqcTaskResult.forEach(p -> {
								p.setDealMethod(dto.getDealMethod());
								fqcTaskMapper.updateByPrimaryKeySelective(p);
							});
						}
					}
					/**
					 * 若DEAL_METHOD为 3 4 则往HQM_FQC_TASK新增一条数据
					 */
					if (StringUtil.isNotEmpty(dto.getDealMethod())
							&& ("4".equals(dto.getDealMethod()) || "3".equals(dto.getDealMethod()))) {
						// 找关联的FQC
						FqcInspectionH fqchSearch = new FqcInspectionH();
						fqchSearch.setHeaderId(dto.getInspectionId());
						fqchSearch = fqcInspectionHMapper.selectByPrimaryKey(fqchSearch);
						FqcTask insert = new FqcTask();
						insert.setPlantId(dto.getPlantId());
						insert.setSourceOrder(fqchSearch.getSourceOrder());
						insert.setSourceType("6");
						FqcTask fqcTaskEarlist = null;
						List<FqcTask> res = fqcTaskMapper.getFqcTaskEarlistBySourceOrder(dto.getSourceOrder());
						if (res == null || res.size() == 0) {
							fqcTaskEarlist = null;
						} else {
							fqcTaskEarlist = res.get(0);
						}
						
						if (fqcTaskEarlist == null) {
							execution.setVariable("amineResult", Boolean.TRUE);
							return;
						} else {
							insert.setProdLineId(fqcTaskEarlist.getProdLineId());
							insert.setItemId(fqcTaskEarlist.getItemId());
							insert.setItemVersion(fqcTaskEarlist.getItemVersion());
							insert.setSpreading(fqcTaskEarlist.getSpreading());
							insert.setProductionBatch(fqcTaskEarlist.getProductionBatch());
							insert.setInspectionType("FQC");
							insert.setProductQty(fqcTaskEarlist.getProductQty());
							insert.setProductDate(fqcTaskEarlist.getProductDate());
							insert.setRelOrder(fqcTaskEarlist.getInspectionNum());
							insert.setRecheckSampleWay(dto.getRecheckSampleWay());
							insert.setTaskFrom(dto.getDealMethod());
							fqcTaskMapper.insertSelective(insert);
							iFqcTaskService.createFqc(insert, null);
							fqcTaskMapper.updateByPrimaryKeySelective(insert);
						}	
					}
					FqcInspectionH fqcSearch = new FqcInspectionH();
					fqcSearch.setInspectionNum(dto.getInspectionCode());
					List<FqcInspectionH> res = fqcInspectionHMapper.select(fqcSearch);
					FqcInspectionL fqclSearch = new FqcInspectionL();
					fqclSearch.setHeaderId(res.get(0).getHeaderId());
					iFqcInspectionHService.chooseInspectionType(res.get(0), fqcInspectionLMapper.select(fqclSearch));
					
					pushToMes(dto);
					
					execution.setVariable("amineResult", Boolean.TRUE);
				}
			} else {
				execution.setVariable("amineResult", Boolean.FALSE);
			}
		} catch (Exception e) {
			execution.setVariable("amineResult", Boolean.FALSE);
		}
		
	}
	
	public void pushToMes(NonconformingOrder dto) {
		FqcInspectionHMapper fqcInspectionHMapper = ContextLoader.getCurrentWebApplicationContext()
				.getBean(FqcInspectionHMapper.class);
		FqcInspectionLMapper fqcInspectionLMapper = ContextLoader.getCurrentWebApplicationContext()
				.getBean(FqcInspectionLMapper.class);
		FqcInspectionDMapper fqcInspectionDMapper = ContextLoader.getCurrentWebApplicationContext()
				.getBean(FqcInspectionDMapper.class);
		TaskExecutor taskExecutor = ContextLoader.getCurrentWebApplicationContext()
				.getBean(TaskExecutor.class);
		ICodeService iCodeService = ContextLoader.getCurrentWebApplicationContext()
				.getBean(ICodeService.class);
		IfInvokeOutboundMapper ifInvokeOutboundMapper = ContextLoader.getCurrentWebApplicationContext()
				.getBean(IfInvokeOutboundMapper.class);
		
		FqcInpectionDealMethod idm = new FqcInpectionDealMethod();
		idm.plantCode = dto.getPlantCode();
		FqcInspectionH fihSearch = new FqcInspectionH();
		fihSearch.setInspectionNum(dto.getInspectionCode());
		List<FqcInspectionH> hResult = fqcInspectionHMapper.select(fihSearch);
		if (hResult == null || hResult.size() == 0)
			return;
		idm.orderNo = hResult.get(0).getSourceOrder();
		idm.method = dto.getDealMethod();
		if ("1".equals(dto.getDealMethod())) {
			idm.reworkType = dto.getReworkType();
		} else {
			List<FqcInspectionD> max = new ArrayList<FqcInspectionD>();
			FqcInspectionL lSearch = new FqcInspectionL();
			lSearch.setHeaderId(hResult.get(0).getHeaderId());
			List<FqcInspectionL> lineResult = fqcInspectionLMapper.select(lSearch);
			for (FqcInspectionL line : lineResult) {
				FqcInspectionD dataSearch = new FqcInspectionD();
				dataSearch.setLineId(line.getLineId());
				List<FqcInspectionD> dataResult = fqcInspectionDMapper.select(dataSearch);
				if (dataResult != null && dataResult.size() > max.size())
					max = dataResult;
			}
			idm.serialNumber = StringUtils.join(max.stream().filter(p -> "NG".equals(p.getSnStatus()))
					.map(FqcInspectionD::getSerialNumber).collect(Collectors.toList()), ",");

		}
		taskExecutor.execute(() -> {
			IfInvokeOutbound iio = new IfInvokeOutbound();
			ObjectMapper obj = new ObjectMapper();
			try {
				String param = obj.writeValueAsString(idm);
				ServiceRequest sr = new ServiceRequest();
				sr.setLocale("zh_CN");
				String uri = iCodeService.getCodeValueByMeaning(sr, "HAP.SYSTEM", "MES_WS_URI");// 获取调用地址
				SoapPostUtil.Response re = SoapPostUtil.ticketSrmToMes(uri, "getInspectionDealMethod", param, iio);
				ifInvokeOutboundMapper.insertSelective(iio);
				logger.info(SoapPostUtil.getStringFromResponse(re));
			} catch (Exception e) {
				iio.setResponseContent(e.getMessage());
				iio.setResponseCode("E");
				ifInvokeOutboundMapper.insertSelective(iio);
				logger.warn(e.getMessage());
			}
		});
	}

	@Override
	public String getBeanName() {
		return "FqcUnqualityExamine";
	}
}
