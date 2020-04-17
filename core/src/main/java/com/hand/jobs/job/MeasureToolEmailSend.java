package com.hand.jobs.job;

import java.util.List;

import org.joda.time.LocalDate;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.JobExecutionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.hand.hap.job.AbstractJob;
import com.hand.hap.system.dto.CodeValue;
import com.hand.hap.system.mapper.CodeValueMapper;
import com.hand.hap.task.info.ExecutionInfo;
import com.hand.hap.task.service.ITask;

/**
 * 量具校验提醒
 * 
 * @author KOCDZG1
 *
 */
@DisallowConcurrentExecution
@Transactional(rollbackFor = Exception.class)
public class MeasureToolEmailSend extends AbstractJob implements ITask {

	@Autowired
	private CodeValueMapper codeValueMapper;

	@Override
	public void execute(ExecutionInfo executionInfo) throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public void safeExecute(JobExecutionContext context) throws Exception {
		// TODO Auto-generated method stub

	}

	private void measureToolMain() {
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
						
					}
				} else {
					throw new RuntimeException("快码HQM_MET_CHECK_DATE维护的格式不正确，正确格式为（月-日）");
				}
			}
		}
	}
}
