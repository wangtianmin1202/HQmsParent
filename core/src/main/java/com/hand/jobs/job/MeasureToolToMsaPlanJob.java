package com.hand.jobs.job;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import org.quartz.DisallowConcurrentExecution;
import org.quartz.JobExecutionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.hand.hap.core.IRequest;
import com.hand.hap.job.AbstractJob;
import com.hand.hap.system.dto.CodeValue;
import com.hand.hap.system.mapper.CodeMapper;
import com.hand.hap.system.mapper.CodeValueMapper;
import com.hand.hap.system.service.ICodeService;
import com.hand.hap.task.info.ExecutionInfo;
import com.hand.hap.task.service.ITask;
import com.hand.hqm.hqm_measure_tool.dto.MeasureTool;
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

/**
 * 量具MSA周期校验
 * 
 * @author KOCDZG1
 *
 */
@DisallowConcurrentExecution
@Transactional(rollbackFor = Exception.class)
public class MeasureToolToMsaPlanJob extends AbstractJob implements ITask {

	// @Autowired
	// private SysCode sysCode;
	@Autowired
	private MsaPlanMapper mapper;
	@Autowired
	private IMsaPlanService msaPlanService;
	@Autowired
	private IMsaPlanLineService msaPlanLineService;
	@Autowired
	private IMeasureToolService measureToolService;
	@Autowired
	private IMeasureToolMsaService measureToolMsaService;
	@Autowired
	private IMeasureToolHisService measureToolHisService;
	@Autowired
	private IMeasureToolMsaHisService measureToolMsaHisService;
	@Autowired
	private CodeValueMapper codeValueMapper;

	@Override
	public void execute(ExecutionInfo executionInfo) throws Exception {
		// TODO Auto-generated method stub
		measureToolToMsaPlanData();
	}

	@Override
	public void safeExecute(JobExecutionContext context) throws Exception {
		// TODO Auto-generated method stub
		measureToolToMsaPlanData();
	}

	/**
	 *  量具MSA周期校验入口
	 */
	private void measureToolToMsaPlanData() {
		
		List<CodeValue> codeValue = codeValueMapper.selectCodeValuesByCodeName("HQM_MSA_ADVANCE_TIME");

		if (codeValue != null && codeValue.size() > 0) {
			// 根据快码获取时间跨度
			int dataInterval = Integer.parseInt(codeValue.get(0).getValue());
			// SimpleDateFormat sdft = new SimpleDateFormat("yyyy-MM-dd");
			Date date = new Date();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			Calendar calendar = new GregorianCalendar();
			calendar.setTime(date);
			calendar.add(Calendar.DAY_OF_MONTH, dataInterval); // 把日期往后增加一年，整数往后推，负数往前移
			date = calendar.getTime();

			// 获取符合条件的量具信息
			MeasureTool measureTool = new MeasureTool();
			measureTool.setJobDate(sdf.format(date));
			List<MeasureTool> mewsureToolList = measureToolService.jobSelect(measureTool);

			if (mewsureToolList != null && mewsureToolList.size() > 0) {
				//生成计划信息
				getMsaPlanData(mewsureToolList);
			}

		}
	}
	/**
	 * 生成计划信息
	 * @param mewsureToolList
	 */
	private void getMsaPlanData(List<MeasureTool> mewsureToolList) {
		List<CodeValue> codeValueTime = codeValueMapper.selectCodeValuesByCodeName("HQM_MSA_TIME");
		if (codeValueTime != null && codeValueTime.size() > 0) {
			for (MeasureTool tool : mewsureToolList) {
				// 获取分析人
				MeasureTool measureTool = new MeasureTool();
				measureTool.setMeasureToolId(tool.getMeasureToolId());
				List<MeasureTool> list = measureToolService.MeasureToolLov(measureTool);

				if (list != null && list.size() == 1) {
					//生成计划头信息
					MsaPlan msaPlan = createMsaPlan(tool, codeValueTime.get(0).getValue(), list.get(0).getAnalystId());

					// 更改量具状态 待分析、下次msa日期
					Calendar cal = getCalendar(tool.getMsaCycle(), new Date());
					MeasureTool measure = new MeasureTool();
					measure.setNextMsaDate(cal.getTime());
					measure.setMeasureToolId(tool.getMeasureToolId());
					measure.setMsaStatus("1");
					measureToolService.updateByPrimaryKeySelective(null, measure);
					//记录量具历史
					saveMeasureToolHis(null,measure);
					
					//获取量具行
					MeasureToolMsa measureToolMsa = new MeasureToolMsa();
					measureToolMsa.setMeasureToolId(tool.getMeasureToolId());
					List<MeasureToolMsa> measureToolMsaList = measureToolMsaService.select(null, measureToolMsa, 0, 0);
					if (measureToolMsaList != null && measureToolMsaList.size() > 0) {
						// 生成计划行信息
						for (MeasureToolMsa toolMsa : measureToolMsaList) {
							createMsaPlanLine(toolMsa,msaPlan.getMsaPlanId());
						}
					}
				}
			}
		}
	}
	/**
	 * 生成计划头信息
	 * @param tool
	 * @param value
	 * @param analystId
	 * @return
	 */
	private MsaPlan createMsaPlan(MeasureTool tool, String value, Float analystId) {
		Calendar calendar = new GregorianCalendar();
		calendar.setTime(tool.getNextMsaDate());
		calendar.add(Calendar.DAY_OF_MONTH, Integer.parseInt(value)); // 把日期往后增加一天，整数往后推，负数往前移
		Date estimatedFinishTime = calendar.getTime();

		// 生成流水号
		int num = mapper.queryMaxNum();
		num++;
		// 序列号
		String numStr = String.format("%05d", num);
		SimpleDateFormat sdf = new SimpleDateFormat("yyMMdd");
		String planNum = "M" + sdf.format(new Date()) + numStr;

		// 生成计划头信息
		MsaPlan msaPlan = new MsaPlan();
		msaPlan.setMsaPlanNum(planNum);
		msaPlan.setMsaType(tool.getMsaType());
		msaPlan.setMeasureToolNum(tool.getMeasureToolNum());
		msaPlan.setMeasurePlanStatus("1");
		msaPlan.setEstimatedFinishTime(estimatedFinishTime);
		msaPlan.setPlantId(tool.getPlantId());
		msaPlan.setAnalyzedBy(analystId);
		msaPlan = msaPlanService.insertSelective(null, msaPlan);

		return msaPlan;
	}
	/**
	 * 生成计划行信息
	 * @param toolMsa
	 * @param msaPlanId
	 */
	private void createMsaPlanLine(MeasureToolMsa toolMsa,Float msaPlanId) {
		MsaPlanLine msaPlanLine = new MsaPlanLine();
		msaPlanLine.setMsaContent(toolMsa.getMsaContent());
		msaPlanLine.setMeasurePlanId(msaPlanId);
		msaPlanLineService.insertSelective(null, msaPlanLine);
	}
	/**
	 * 时间计算
	 * @param cycle
	 * @param date
	 * @return
	 */
	private Calendar getCalendar(String cycle,Date date) {
		String start = cycle.substring(0, cycle.length() - 1);
		String end = cycle.substring(cycle.length() - 1);

		Calendar cal = Calendar.getInstance();
		cal.setTime(date);

		if ("M".equals(end)) {
			cal.add(Calendar.MONTH, Integer.parseInt(start.trim()));
		} else if ("Y".equals(end)) {
			cal.add(Calendar.YEAR, Integer.parseInt(start.trim()));
		}
		return cal;
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
		measureTool = measureToolService.selectByPrimaryKey(null,measureTool);
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
