package com.hand.jobs.job;

import com.hand.hap.job.AbstractJob;
import com.hand.hap.task.info.ExecutionInfo;
import com.hand.hap.task.service.ITask;

import java.util.List;

import org.quartz.JobExecutionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import com.hand.hap.core.IRequest;
import com.hand.hqm.hqm_qc_task.dto.PqcTask;
import com.hand.hqm.hqm_qc_task.mapper.PqcTaskMapper;
import com.hand.hqm.hqm_qua_ins_time_h.dto.QuaInsTimeH;
import com.hand.hqm.hqm_qua_ins_time_h.mapper.QuaInsTimeHMapper;

/**
 * 定时生成巡检任务 Task
 * 
 * @author tainmin.wang
 * @version date：2019年8月7日 下午1:55:16
 * 
 */
@Transactional(rollbackFor = Exception.class)
public class CreatePqcTask extends AbstractJob implements ITask {

	@Autowired
	QuaInsTimeHMapper quaInsTimeHMapper;
	@Autowired
	PqcTaskMapper pqcTaskMapper;

	IRequest requestContext;

	@Override
	public void execute(ExecutionInfo executionInfo) throws Exception {
		self();
	}

	@Override
	public void safeExecute(JobExecutionContext context) throws Exception {
		self();
	}

	private void self() throws Exception {
		List<QuaInsTimeH> list = quaInsTimeHMapper.jobSelect();
		list.forEach(p -> {
			PqcTask insert = new PqcTask();
			insert.setPlantId(p.getPlantId());
			insert.setTimeId(p.getTimeId());
			insert.setProdLineId(p.getProdLineId());
			insert.setShiftCode(p.getShiftCode());
			insert.setCheckDate(p.getCheckDate());
			insert.setInspectorId(p.getInspectorId());
			pqcTaskMapper.insertSelective(insert);
		});
	}

}
