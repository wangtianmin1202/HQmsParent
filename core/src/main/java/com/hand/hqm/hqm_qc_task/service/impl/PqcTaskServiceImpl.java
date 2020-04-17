package com.hand.hqm.hqm_qc_task.service.impl;

import com.github.pagehelper.PageHelper;
import com.hand.hap.core.IRequest;
import com.hand.hap.core.impl.RequestHelper;
import com.hand.hap.system.service.impl.BaseServiceImpl;
import com.hand.hap.util.SoapPostUtil.ResponseSap;
import com.hand.hap.webservice.ws.idto.WorkOperationChange;
import com.hand.hcm.hcm_item_b.dto.ItemB;
import com.hand.hcm.hcm_item_b.mapper.ItemBMapper;
import com.hand.hcm.hcm_plant.dto.Plant;
import com.hand.hcm.hcm_plant.mapper.PlantMapper;
import com.hand.hcm.hcm_production_line.dto.ProductionLine;
import com.hand.hcm.hcm_production_line.mapper.ProductionLineMapper;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hand.hqm.hqm_pqc_inspection_h.service.IPqcInspectionHService;
import com.hand.hqm.hqm_qc_task.dto.PqcTask;
import com.hand.hqm.hqm_qc_task.mapper.PqcTaskMapper;
import com.hand.hqm.hqm_qc_task.service.IPqcTaskService;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(rollbackFor = Exception.class)
public class PqcTaskServiceImpl extends BaseServiceImpl<PqcTask> implements IPqcTaskService {

	@Autowired
	PqcTaskMapper mapper;
	@Autowired
	IPqcInspectionHService iPqcInspectionHService;
	@Autowired
	PlantMapper plantMapper;
	@Autowired
	ProductionLineMapper productionLineMapper;
	@Autowired
	ItemBMapper itemBMapper;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.hand.hqm.hqm_qc_task.service.IPqcTaskService#reSelect(com.hand.hap.core.
	 * IRequest, com.hand.hqm.hqm_qc_task.dto.PqcTask, int, int)
	 */
	@Override
	public List<PqcTask> reSelect(IRequest requestContext, PqcTask dto, int page, int pageSize) {
		// TODO Auto-generated method stub
		PageHelper.startPage(page, pageSize);
		return mapper.reSelect(dto);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.hand.hqm.hqm_qc_task.service.IPqcTaskService#createPqc(javax.servlet.http
	 * .HttpServletRequest, com.hand.hqm.hqm_qc_task.dto.PqcTask)
	 */
	@Override
	public String createPqc(HttpServletRequest request, PqcTask pqcTask) {// 生成报告 pqcTask 传入的只有一个 taskId
		pqcTask = mapper.selectByPrimaryKey(pqcTask);
		iPqcInspectionHService.generateByTask(request, pqcTask);
		pqcTask.setSolveStatus("Y");
		self().updateByPrimaryKeySelective(RequestHelper.createServiceRequest(request), pqcTask);
		return pqcTask.getInspectionNum();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.hand.hqm.hqm_qc_task.service.IPqcTaskService#workOperationChange(com.hand
	 * .hap.webservice.ws.idto.WorkOperationChange)
	 */
	@Override
	public ResponseSap workOperationChange(WorkOperationChange woc) {
		ResponseSap response = new ResponseSap();
		// 接口表的工厂编号找到hcm_plant表的工厂code找到工厂ID作为plant_id
		Plant plant = new Plant();
		if(StringUtils.isEmpty(woc.plantCode))
			woc.plantCode = "CNKE";
		plant.setPlantCode(woc.plantCode);
		plant = plantMapper.select(plant).get(0);
		// 接口表的产线找到hcm_production_line表的产线ID作为prod_line_id
		ProductionLine pl = new ProductionLine();
		pl.setProdLineCode(woc.line);
		pl = productionLineMapper.select(pl).get(0);
		// 找物料 ID
		ItemB ib = new ItemB();
		ib.setItemCode(woc.itemCode);
		ib.setPlantId(pl.getPlantId());
		ib = itemBMapper.select(ib).get(0);
		//找 shift_code
		String shiftCode = productionLineMapper.queryShiftCode(pl);
		if(StringUtils.isEmpty(shiftCode))
			throw new RuntimeException("此产线没有班次");
		// time_id默认为-1 接口表的触发时间作为check_date
		PqcTask insert = new PqcTask();
		insert.setPlantId(plant.getPlantId());
		insert.setProdLineId(pl.getProdLineId());
		insert.setTimeId(-1f);
		insert.setCheckDate(new Date());
		insert.setItemId(ib.getItemId());
		insert.setOrderNum(woc.orderNo);
		insert.setItemVersion(woc.itemVersion);
		insert.setShiftCode(shiftCode);
		mapper.insertSelective(insert);
		return response;
	}
	

}