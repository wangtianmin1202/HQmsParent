/**
 * 
 */
package com.hand.hap.workflow.task.task_listener_impl;

import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;
import org.springframework.stereotype.Component;

/**
* @author tainmin.wang 任务监听器
* @version date：2019年12月5日 下午3:18:22
* 
*/
public class TestTaskListenerImpl implements TaskListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8192167829774434374L;
	
	/* (non-Javadoc)
	 * @see org.activiti.engine.delegate.TaskListener#notify(org.activiti.engine.delegate.DelegateTask)
	 */
	@Override
	public void notify(DelegateTask delegateTask) {
		delegateTask.setVariable(EVENTNAME_ALL_EVENTS, delegateTask);
	}

}
