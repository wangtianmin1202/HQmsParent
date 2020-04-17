package com.hand.jobs.job;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.joda.time.LocalDate;
import org.quartz.JobExecutionContext;
import org.springframework.beans.factory.annotation.Autowired;

import com.hand.hap.job.AbstractJob;
import com.hand.hap.system.dto.CodeValue;
import com.hand.hap.system.mapper.CodeValueMapper;
import com.hand.hap.task.info.ExecutionInfo;
import com.hand.hap.task.service.ITask;
import com.hand.hqm.hqm_measure_tool.dto.MeasureTool;
import com.hand.hqm.hqm_measure_tool.service.IMeasureToolService;
import com.hand.hqm.hqm_msa_check_plan.dto.MetCheckPlan;
import com.hand.hqm.hqm_msa_check_plan.dto.MetCheckPlanLine;
import com.hand.hqm.hqm_msa_check_plan.mapper.MetCheckPlanMapper;
import com.hand.hqm.hqm_msa_check_plan.service.IMetCheckPlanLineService;
import com.hand.hqm.hqm_msa_check_plan.service.IMetCheckPlanService;

/**
 * 量具校验清单
 * 
 * @author KOCDZG1
 *
 */
public class MetCheckPlanJob extends AbstractJob implements ITask {
	@Autowired
	private CodeValueMapper codeValueMapper;
	@Autowired
	private IMeasureToolService measureToolService;
	@Autowired
	private IMetCheckPlanService metCheckPlanService;
	@Autowired
	private IMetCheckPlanLineService metCheckPlanLineService;
	@Autowired
	private MetCheckPlanMapper mapper;

	@Override
	public void execute(ExecutionInfo executionInfo) throws Exception {
		// TODO Auto-generated method stub
		checkPlanMain();
	}

	@Override
	public void safeExecute(JobExecutionContext context) throws Exception {
		// TODO Auto-generated method stub
		checkPlanMain();
	}

	/**
	 * 主函数
	 */
	private void checkPlanMain() {
		// 获取邮件提醒时间
		List<CodeValue> metCheckDateList = codeValueMapper.selectCodeValuesByCodeName("HQM_MET_CHECK_DATE");
		// 获取周期
		List<CodeValue> checkFrequencyList = codeValueMapper.selectCodeValuesByCodeName("HQM_CHECK_FREQUENCY");
		if (metCheckDateList != null && metCheckDateList.size() > 0 && checkFrequencyList != null
				&& checkFrequencyList.size() > 0) {
			for (CodeValue codeValue : metCheckDateList) {
				String[] stringArr = codeValue.getValue().split("-");
				if (stringArr.length == 2) {
					LocalDate today = LocalDate.now();
					// 判断月日是否于当天一致
					if (Integer.parseInt(stringArr[0]) == today.getMonthOfYear()
							&& Integer.parseInt(stringArr[1]) == today.getDayOfMonth()) {
						// 开始日期
						String startTime;
						// 结束日期（不包括）
						String endTime;
						SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
						Calendar calendar = new GregorianCalendar();
						calendar.setTime(new Date());

						calendar.add(Calendar.MONTH, 1);
						startTime = sdf.format(calendar.getTime());

						calendar.add(Calendar.MONTH, Integer.parseInt(checkFrequencyList.get(0).getValue()));
						endTime = sdf.format(calendar.getTime());

						MeasureTool measureTool = new MeasureTool();
						measureTool.setStartTime(startTime);
						measureTool.setEndTime(endTime);
						List<MeasureTool> measureToolList = measureToolService.metCheckPlanJob(measureTool);
						if (measureToolList != null && measureToolList.size() > 0) {
							createMetCheckPlanData(measureToolList);
						}
					}
				} else {
					throw new RuntimeException("快码HQM_MET_CHECK_DATE维护的格式不正确，正确格式为（月-日）");
				}
			}
		}
	}

	/**
	 * 生成量具校验清单数据
	 * 
	 * @param measureToolList
	 */
	private void createMetCheckPlanData(List<MeasureTool> measureToolList) {
		Set<Float> plantIdSet = measureToolList.stream().collect(Collectors.groupingBy(MeasureTool::getPlantId))
				.keySet();
		List<Float> plantIdList = new ArrayList(plantIdSet);
		// 生成流水号
		int num = mapper.queryMaxNum();
		for (Float plantId : plantIdList) {
			MetCheckPlan metCheckPlan = new MetCheckPlan();
			metCheckPlan.setPlantId(plantId);

			num++;
			// 序列号
			String numStr = String.format("%03d", num);
			SimpleDateFormat sdf = new SimpleDateFormat("yyMMdd");
			String metCode = "MC" + sdf.format(new Date()) + numStr;
			

			metCheckPlan.setCheckPlanCode(metCode);
			// 新增头信息
			metCheckPlan = metCheckPlanService.insertSelective(null, metCheckPlan);

			for (MeasureTool measureTool : measureToolList) {
				if (plantId.equals(measureTool.getPlantId())) {
					MetCheckPlanLine metCheckPlanLine = new MetCheckPlanLine();
					metCheckPlanLine.setMeasuretoolId(measureTool.getMeasureToolId());
					metCheckPlanLine.setCheckPlanId(metCheckPlan.getCheckPlanId());
					// 新增行
					metCheckPlanLineService.insertSelective(null, metCheckPlanLine);
				}
			}
		}
	}
}
