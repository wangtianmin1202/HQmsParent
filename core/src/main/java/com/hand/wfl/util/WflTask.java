/**
 * 
 */
package com.hand.wfl.util;

import java.util.Date;

/** 
 *@version:1.0
 *@Description: 
 *@author: zc
 *@date: Jan 6, 2020 3:06:48 PM
*/
public class WflTask {
	
	private String taskId;//任务id
	
	private String processInstanceId;//流程实例id
	
	private String taskName;//任务名称，流程状态
	
	private String employeeCode;//审批人名称(employee.employeeCode)
	
	private Date taskEndTime;//任务结束时间
	
	private Float employeeId;//审批人，员工id
	
	private Float userId;//审批人，用户id
	
	private String taskDefKey;
	
	public String getTaskDefKey() {
		return taskDefKey;
	}

	public void setTaskDefKey(String taskDefKey) {
		this.taskDefKey = taskDefKey;
	}

	public Float getUserId() {
		return userId;
	}

	public void setUserId(Float userId) {
		this.userId = userId;
	}

	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	public String getProcessInstanceId() {
		return processInstanceId;
	}

	public void setProcessInstanceId(String processInstanceId) {
		this.processInstanceId = processInstanceId;
	}

	public String getTaskName() {
		return taskName;
	}

	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}

	public String getEmployeeCode() {
		return employeeCode;
	}

	public void setEmployeeCode(String employeeCode) {
		this.employeeCode = employeeCode;
	}

	public Date getTaskEndTime() {
		return taskEndTime;
	}

	public void setTaskEndTime(Date taskEndTime) {
		this.taskEndTime = taskEndTime;
	}

	public Float getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(Float employeeId) {
		this.employeeId = employeeId;
	}
	
}
