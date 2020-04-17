package com.hand.hqm.hqm_msa_plan.service.impl;

import com.github.pagehelper.PageHelper;
import com.hand.hap.core.IRequest;
import com.hand.hap.system.service.impl.BaseServiceImpl;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hand.hqm.hqm_measure_tool.dto.MeasureTool;
import com.hand.hqm.hqm_measure_tool.mapper.MeasureToolMapper;
import com.hand.hqm.hqm_measure_tool.service.IMeasureToolService;
import com.hand.hqm.hqm_measure_tool_his.dto.MeasureToolHis;
import com.hand.hqm.hqm_measure_tool_his.service.IMeasureToolHisService;
import com.hand.hqm.hqm_measure_tool_msa.dto.MeasureToolMsa;
import com.hand.hqm.hqm_measure_tool_msa.service.IMeasureToolMsaService;
import com.hand.hqm.hqm_measure_tool_msa_his.dto.MeasureToolMsaHis;
import com.hand.hqm.hqm_measure_tool_msa_his.service.IMeasureToolMsaHisService;
import com.hand.hqm.hqm_msa_plan.dto.MsaPlan;
import com.hand.hqm.hqm_msa_plan.mapper.MsaPlanMapper;
import com.hand.hqm.hqm_msa_plan.service.IMsaPlanService;
import com.hand.hqm.hqm_msa_plan_line.dto.MsaPlanLine;
import com.hand.hqm.hqm_msa_plan_line.service.IMsaPlanLineService;
import com.hand.utils.ObjectUtils.CopyObject;

import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(rollbackFor = Exception.class)
public class MsaPlanServiceImpl extends BaseServiceImpl<MsaPlan> implements IMsaPlanService {

	@Autowired
	private MsaPlanMapper mapper;
	@Autowired
	private IMsaPlanLineService msaPlanLineService;
	@Autowired
	private MeasureToolMapper msasureToolMapper;
	@Autowired
	private IMeasureToolService measureToolService;
	@Autowired
	private IMeasureToolMsaService measureToolMsaService;
	@Autowired
	private IMeasureToolHisService measureToolHisService;
	@Autowired
	private IMeasureToolMsaHisService measureToolMsaHisService;

	@Override
	public List<MsaPlan> query(IRequest requestContext, MsaPlan dto, int page, int PageSize) {
		// TODO Auto-generated method stub
		PageHelper.startPage(page, PageSize);
		return mapper.query(dto);
	}

	@Override
	public List<MsaPlan> update(IRequest requestContext, List<MsaPlan> dto) {
		if ("add".equals(dto.get(0).get__status())) {
			for (MsaPlan msaPlan : dto) {
				// 生成流水号
				int num = mapper.queryMaxNum();
				num++;
				// 序列号
				String numStr = String.format("%05d", num);
				SimpleDateFormat sdf = new SimpleDateFormat("yyMMdd");
				String planNum = "M" + sdf.format(new Date()) + numStr;
				msaPlan.setMsaPlanNum(planNum);

				msaPlan = self().insertSelective(requestContext, msaPlan);

				// 获取msa量具信息，新建Msa计划行信息
				MeasureTool measureTool = new MeasureTool();
				measureTool.setMeasureToolNum(msaPlan.getMeasureToolNum());
				measureTool = msasureToolMapper.selectOne(measureTool);
				if (measureTool != null) {
					MeasureToolMsa measureToolMsa = new MeasureToolMsa();
					measureToolMsa.setMeasureToolId(measureTool.getMeasureToolId());

					List<MeasureToolMsa> measureToolMsaList = measureToolMsaService.select(requestContext,
							measureToolMsa, 0, 0);
					if (measureToolMsaList != null && measureToolMsaList.size() > 0) {
						for (MeasureToolMsa toolMsa : measureToolMsaList) {
							MsaPlanLine msaPlanLine = new MsaPlanLine();
							msaPlanLine.setMeasurePlanId(msaPlan.getMsaPlanId());
							msaPlanLine.setMsaContent(toolMsa.getMsaContent());
							// 插入msa计划行信息
							msaPlanLineService.insertSelective(requestContext, msaPlanLine);
						}
					}
				}

			}
			return dto;
		} else {
			return self().batchUpdate(requestContext, dto);
		}
	}

	@Override
	public List<MsaPlan> cancel(IRequest requestContext, List<MsaPlan> dto) {
		for (MsaPlan msaPlan : dto) {
			msaPlan.setMeasurePlanStatus("4");
			self().updateByPrimaryKeySelective(requestContext, msaPlan);
		}
		return dto;
	}

	@Override
	public List<MsaPlan> complete(IRequest requestContext, List<MsaPlan> dto) {
		for (MsaPlan msaPlan : dto) {
			// 更新计划头状态 ： 3
			MsaPlan plan = new MsaPlan();
			plan.setMsaPlanId(msaPlan.getMsaPlanId());
			plan.setMeasurePlanStatus("3");
			self().updateByPrimaryKeySelective(requestContext, plan);

			MsaPlanLine msaPlanLine = new MsaPlanLine();
			msaPlanLine.setMeasurePlanId(msaPlan.getMsaPlanId());

			List<MsaPlanLine> msaPlanLineList = msaPlanLineService.select(requestContext, msaPlanLine, 0, 0);
			if (msaPlanLineList != null && msaPlanLineList.size() > 0) {
				// 判断计划是否完成
				msaPlanLineList.stream().filter(data -> StringUtils.isEmpty(data.getMsaResult())).findAny()
						.ifPresent(s -> {
							throw new RuntimeException("计划编号为：" + msaPlan.getMsaPlanNum() + "的计划未完成，不能关闭");
						});

				MeasureTool measureTool = new MeasureTool();
				measureTool.setMeasureToolNum(msaPlan.getMeasureToolNum());
				measureTool = msasureToolMapper.selectOne(measureTool);
				if (measureTool != null) {

					MeasureToolMsa measureToolMsa = new MeasureToolMsa();
					measureToolMsa.setMeasureToolId(measureTool.getMeasureToolId());
					List<MeasureToolMsa> measureToolMsaList = measureToolMsaService.select(requestContext,
							measureToolMsa, 0, 0);

					// 更新量具行信息
					for (MsaPlanLine item : msaPlanLineList) {
						for (MeasureToolMsa toolMsa : measureToolMsaList) {
							if (toolMsa.getMsaContent().equals(item.getMsaContent())) {
								toolMsa.setMsaResult(item.getMsaResult());
								measureToolMsaService.updateByPrimaryKeySelective(requestContext, toolMsa);
								measureToolMsaList.remove(toolMsa);
								break;
							}
						}
					}

					

					// 更新量具状态为已完成: 2;下次msa日期
					measureTool.setMsaStatus("2");
					String start = measureTool.getCheckCycle().substring(0, measureTool.getCheckCycle().length() - 1);
					String end = measureTool.getCheckCycle().substring(measureTool.getCheckCycle().length() - 1);
					Calendar cal = Calendar.getInstance();
					cal.setTime(new Date());
					if ("M".equals(end)) {
						cal.add(Calendar.MONTH, Integer.parseInt(start.trim()));
					} else if ("Y".equals(end)) {
						cal.add(Calendar.YEAR, Integer.parseInt(start.trim()));
					}
					measureTool.setNextCheckDate(cal.getTime());
					//判断行分析结果是否都为OK
					MeasureToolMsa msa = new MeasureToolMsa();
					msa.setMeasureToolId(measureTool.getMeasureToolId());
					msa.setMsaResult("NG");
					List<MeasureToolMsa> msaList = measureToolMsaService.select(requestContext, msa, 0, 0);
					if (msaList == null || msaList.size() == 0) {
						measureTool.setMsaResult("OK");
					}else {
						measureTool.setMsaResult("NG");
						measureTool.setMeasureToolStatus("2");
					}
					measureTool = measureToolService.updateByPrimaryKeySelective(requestContext, measureTool);
					//记录量具历史
					saveMeasureToolHis(requestContext,measureTool);
				}
			}
		}
		return dto;
	}
	/**
	 * 记历史
	 * 
	 * @param requestContext
	 * @param measureTool
	 * @return
	 */
	private MeasureToolHis saveMeasureToolHis(IRequest requestContext, MeasureTool measureTool) {
		// 记头历史
		MeasureToolHis measureToolHis = new MeasureToolHis();
		measureTool = measureToolService.selectByPrimaryKey(requestContext,measureTool);
		CopyObject.copyByName(measureTool, measureToolHis);
		measureToolHis.setHisType("3");
		measureToolHis = measureToolHisService.insertSelective(requestContext, measureToolHis);

		MeasureToolMsa measureToolMsa = new MeasureToolMsa();
		measureToolMsa.setMeasureToolId(measureTool.getMeasureToolId());
		List<MeasureToolMsa> measureToolMasList = measureToolMsaService.select(requestContext, measureToolMsa, 0, 0);
		if (measureToolMasList != null && measureToolMasList.size() > 0) {
			for (MeasureToolMsa sureToolMsa : measureToolMasList) {
				// 行历史
				MeasureToolMsaHis measureToolMsaHis = new MeasureToolMsaHis();
				CopyObject.copyByName(sureToolMsa, measureToolMsaHis);
				measureToolMsaHis.setMeasureToolHisId(measureToolHis.getMeasureToolHisId());
				measureToolMsaHisService.insertSelective(requestContext, measureToolMsaHis);
			}
		}
		return measureToolHis;
	}
}