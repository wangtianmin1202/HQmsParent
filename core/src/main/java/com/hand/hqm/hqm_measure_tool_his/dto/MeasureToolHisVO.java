package com.hand.hqm.hqm_measure_tool_his.dto;

public class MeasureToolHisVO {
	//校验机构
	private String checkType;
	
	//合格数量
	private Integer okNum;
	
	//不合格数量
	private Integer ngNum;
	
	//使用部门
	private String departmentUsage;
	
	//数量
	private Integer num;

	public String getDepartmentUsage() {
		return departmentUsage;
	}

	public void setDepartmentUsage(String departmentUsage) {
		this.departmentUsage = departmentUsage;
	}

	public Integer getNum() {
		return num;
	}

	public void setNum(Integer num) {
		this.num = num;
	}

	public String getCheckType() {
		return checkType;
	}

	public void setCheckType(String checkType) {
		this.checkType = checkType;
	}

	public Integer getOkNum() {
		return okNum;
	}

	public void setOkNum(Integer okNum) {
		this.okNum = okNum;
	}

	public Integer getNgNum() {
		return ngNum;
	}

	public void setNgNum(Integer ngNum) {
		this.ngNum = ngNum;
	}

	
	
	
}
