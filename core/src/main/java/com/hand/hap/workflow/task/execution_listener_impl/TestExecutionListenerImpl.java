/**
 * 
 */
package com.hand.hap.workflow.task.execution_listener_impl;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.ExecutionListener;
import org.activiti.engine.impl.el.FixedValue;
import org.springframework.stereotype.Component;

/**
* @author tainmin.wang 执行监听器
* @version date：2019年12月6日 下午12:02:55
* 
*/
public class TestExecutionListenerImpl implements ExecutionListener {

	/* (non-Javadoc)
	 * @see org.activiti.engine.delegate.ExecutionListener#notify(org.activiti.engine.delegate.DelegateExecution)
	 */
	private FixedValue fieldName;
	
	public FixedValue getFieldName() {
		return fieldName;
	}

	public void setFieldName(FixedValue fieldName) {
		this.fieldName = fieldName;
	}

	@Override
	public void notify(DelegateExecution execution) {
		System.out.println(execution);
	}

}
