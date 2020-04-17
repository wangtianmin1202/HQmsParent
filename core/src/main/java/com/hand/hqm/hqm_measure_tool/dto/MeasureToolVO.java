package com.hand.hqm.hqm_measure_tool.dto;

public class MeasureToolVO {
	//量具类型
	private String measureToolType;
	
	//量具数量
	private Integer count;
	
	//需要MSA的数量
	private Integer num;

	public Integer getNum() {
		return num;
	}

	public void setNum(Integer num) {
		this.num = num;
	}

	public String getMeasureToolType() {
		return measureToolType;
	}

	public void setMeasureToolType(String measureToolType) {
		this.measureToolType = measureToolType;
	}

	public Integer getCount() {
		return count;
	}

	public void setCount(Integer count) {
		this.count = count;
	}
	
	
}
