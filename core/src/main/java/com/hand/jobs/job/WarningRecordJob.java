/**
 * 
 */
package com.hand.jobs.job;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import org.quartz.JobExecutionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.TaskExecutor;
import org.springframework.transaction.annotation.Transactional;
import com.hand.hap.job.AbstractJob;
import com.hand.hap.task.info.ExecutionInfo;
import com.hand.hap.task.service.ITask;
import com.hand.hap.util.MailUtil;
import com.hand.hqm.hqm_pqc_warning_record.dto.PqcWarningRecord;
import com.hand.hqm.hqm_pqc_warning_record.mapper.PqcWarningRecordMapper;
import com.hand.hqm.hqm_qc_task.dto.PqcTask;
import com.hand.hqm.hqm_qc_task.mapper.PqcTaskMapper;
import com.hand.sys.sys_user.mapper.UserSysMapper;

/**
 * @author tainmin.wang
 * @version date：2019年12月27日 上午9:41:29 定时生成巡检预警记录 前台配置 间隔时常 300s (5分钟)
 */
@Transactional(rollbackFor = Exception.class)
public class WarningRecordJob extends AbstractJob implements ITask {

	@Autowired
	private PqcTaskMapper pqcTaskMapper;
	@Autowired
	PqcWarningRecordMapper pqcWarningRecordMapper;
	@Resource(name = "taskExecutor")
	private TaskExecutor taskExecutor;
	@Autowired
	UserSysMapper userSysMapper;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.hand.hap.task.service.ITask#execute(com.hand.hap.task.info.ExecutionInfo)
	 */
	@Override
	public void execute(ExecutionInfo executionInfo) throws Exception {
		self();

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.hand.hap.job.AbstractJob#safeExecute(org.quartz.JobExecutionContext)
	 */
	@Override
	public void safeExecute(JobExecutionContext context) throws Exception {
		self();
	}

	/**
	 * @description
	 * @author tianmin.wang
	 * @date 2019年12月27日
	 */
	private void self() {
		List<PqcTask> pqcTasks = pqcTaskMapper.jobSelect();
		List<PqcWarningRecord> inserts = new ArrayList<PqcWarningRecord>();
		for (PqcTask pqcTask : pqcTasks) {
			PqcWarningRecord insert = new PqcWarningRecord();
			insert.setWarningId(pqcTask.getWarningId());
			insert.setTaskId(pqcTask.getTaskId());
			insert.setWarningDesc(pqcTask.getWarningDesc());
			insert.setPlantId(pqcTask.getPlantId());
			insert.setWarningObject(pqcTask.getWarningObject());
			insert.setIsSend("N");
			pqcWarningRecordMapper.insertSelective(insert);
			inserts.add(insert);
		}
		sendMail(inserts);
	}

	/**
	 * @description
	 * @author tianmin.wang
	 * @date 2019年12月27日
	 * @param insert
	 */
	private void sendMail(List<PqcWarningRecord> inserts) {
		taskExecutor.execute(new Runnable() {
			@Override
			public void run() {
				try {
					operationIdToEmail(inserts);
				} catch (Exception e) {

				}
			}
		});
	}

	/**
	 * 
	 * @description 把预警对象中的userid 转换为 员工email 用 , 拼接
	 * @author tianmin.wang
	 * @date 2019年12月26日
	 * @param retu
	 */
	public void operationIdToEmail(List<PqcWarningRecord> retu) {
		Map<String, String> cache = new HashMap<String, String>();
		for (PqcWarningRecord pw : retu) {
			String warningObjectName = "";
			List<String> warningObjectList = Arrays.asList(pw.getWarningObject().split(","));
			for (String p : warningObjectList) {
				String name = cache.get(p);
				if (name == null) {
					name = userSysMapper.getEmailByUserId(Float.valueOf(p));
					cache.put(p, name == null ? "" : name);
				}
				warningObjectName = warningObjectName + "," + name;
			}
			emailSend(warningObjectName.substring(1), pw.getWarningDesc());
		}
	}

	/**
	 * @description
	 * @author tianmin.wang
	 * @date 2019年12月27日
	 * @param to      接收人 邮件内容
	 * @param context
	 */
	private void emailSend(String to, String context) {
		try {
			MailUtil.sendExcelMail(to, null, "PQC产线巡检超时预警", context, null, null, "HAP-QMS@kohler.com.cn", null);
		} catch (Exception e) {

		}
	}
}
