/**
 * 
 */
package com.hand.wfl.util;

/** 
 *@version:1.0
 *@Description: 
 *@author: Magicor
 *@date: Jan 9, 2020 10:33:10 AM
*/
public class WflVariable {
	
	private String processInstanceId;//流程实例id
	
	private String executionId;//执行实例id
	
	private String name;//变量名称
	
	private String text;//变量值

	public String getProcessInstanceId() {
		return processInstanceId;
	}

	public void setProcessInstanceId(String processInstanceId) {
		this.processInstanceId = processInstanceId;
	}

	public String getExecutionId() {
		return executionId;
	}

	public void setExecutionId(String executionId) {
		this.executionId = executionId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}
	
	
}
