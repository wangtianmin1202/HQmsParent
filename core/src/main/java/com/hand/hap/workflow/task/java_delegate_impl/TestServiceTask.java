/**
 * 
 */
package com.hand.hap.workflow.task.java_delegate_impl;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;

/**
* @author tainmin.wang 服务任务测试
* @version date：2019年12月6日 上午9:26:08
* 
*/
public class TestServiceTask implements JavaDelegate{

	/* (non-Javadoc)
	 * @see org.activiti.engine.delegate.JavaDelegate#execute(org.activiti.engine.delegate.DelegateExecution)
	 */
	@Override
	public void execute(DelegateExecution execution) {
	}

}
