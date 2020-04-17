/**
 * 
 */
package com.hand.jobs.job;
import java.util.Date;
import java.util.List;

import org.quartz.JobExecutionContext;
import org.springframework.beans.factory.annotation.Autowired;

import com.hand.hap.job.AbstractJob;
import com.hand.hap.task.info.ExecutionInfo;
import com.hand.hap.task.service.ITask;
import com.hand.hqm.file_manager.dto.Manager;
import com.hand.hqm.file_manager.mapper.ManagerMapper;


/** 
 *@version:1.0
 *@Description: 证书文件自动过期Job
 *@author: Magicor
 *@date: Nov 27, 2019 6:58:23 PM
*/
public class FileExpiredJob  extends AbstractJob implements ITask {
	
	@Autowired
	private ManagerMapper mapper;
	
	/* (non-Javadoc)
	 * @see com.hand.hap.task.service.ITask#execute(com.hand.hap.task.info.ExecutionInfo)
	 */
	@Override
	public void execute(ExecutionInfo executionInfo) throws Exception {
		// TODO Auto-generated method stub
		self();
	}

	/* (non-Javadoc)
	 * @see com.hand.hap.job.AbstractJob#safeExecute(org.quartz.JobExecutionContext)
	 */
	@Override
	public void safeExecute(JobExecutionContext context) throws Exception {
		// TODO Auto-generated method stub
		self();
	}
	
	private void self() throws Exception { 
		//当前时间
		Date date = new Date();
		//所有包含有效时间的数据
		List<Manager> list = mapper.selectAllFile();
		if (list != null && list.size() > 0) {
			list.forEach(e -> {
				long nowTime = date.getTime();
				long dbTime = e.getExpirationDate().getTime();
				
				if (nowTime >= dbTime) {
					e.setEnableFlag("N");
					mapper.updateByPrimaryKeySelective(e);
				}
			});
		}
	}
}
