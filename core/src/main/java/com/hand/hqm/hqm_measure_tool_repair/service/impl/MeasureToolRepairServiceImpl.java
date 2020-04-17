package com.hand.hqm.hqm_measure_tool_repair.service.impl;

import com.github.pagehelper.PageHelper;
import com.hand.hap.core.IRequest;
import com.hand.hap.system.service.impl.BaseServiceImpl;
import com.hand.hap.util.MailUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hand.hqm.hqm_measure_tool.dto.MeasureTool;
import com.hand.hqm.hqm_measure_tool.service.IMeasureToolService;
import com.hand.hqm.hqm_measure_tool_repair.dto.MeasureToolRepair;
import com.hand.hqm.hqm_measure_tool_repair.mapper.MeasureToolRepairMapper;
import com.hand.hqm.hqm_measure_tool_repair.service.IMeasureToolRepairService;

import oracle.net.aso.d;

import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(rollbackFor = Exception.class)
public class MeasureToolRepairServiceImpl extends BaseServiceImpl<MeasureToolRepair> implements IMeasureToolRepairService{

	@Autowired
	private MeasureToolRepairMapper measureToolRepairMapper;
	
	@Autowired
	private IMeasureToolService measureToolService;
	
	@Override
	public List<MeasureToolRepair> queryByCondition(IRequest requestContext, MeasureToolRepair dto, int page,
			int pageSize) {
		PageHelper.startPage(page, pageSize);
		return measureToolRepairMapper.queryByCondition(dto);
	}

	
	@Override
	public List<MeasureToolRepair> add(IRequest requestCtx, MeasureToolRepair dto) {
		dto.setRepairStartTime(new Date());
		self().insertSelective(requestCtx, dto);
		MeasureTool measureTool = new MeasureTool();
		measureTool.setMeasureToolId(dto.getMeasureToolId());
		measureTool.setMeasureToolStatus("4");
		measureTool.setMeasureToolPositionStatus("I");
		measureToolService.self().updateByPrimaryKeySelective(requestCtx, measureTool);
		return Arrays.asList(dto);
	}


	@Override
	public List<MeasureToolRepair> confirmRepair(IRequest requestCtx, MeasureToolRepair dto) {
		dto.setRepairEndTime(new Date());
		self().updateByPrimaryKeySelective(requestCtx, dto);
		dto = self().selectByPrimaryKey(requestCtx, dto);
		MeasureTool measureTool = new MeasureTool();
		measureTool.setMeasureToolId(dto.getMeasureToolId());
		measureTool.setMeasureToolStatus("1");
		measureTool.setMeasureToolPositionStatus("I");
		measureToolService.self().updateByPrimaryKeySelective(requestCtx, measureTool);
		return Arrays.asList(dto);
	}

}