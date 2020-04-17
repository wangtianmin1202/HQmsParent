package com.hand.hqm.hqm_measure_tool.controllers.enums;

public enum MeasureToolReportEnums {
	PURCHASE_DATE_REPORT("A","购买日期报表"),
	CHECK_REPORT("B","校验报表"),
	DEPARTMENT_USAGE_REPORT("C","部门使用报表"),
	MSA_REPORT("D","MSA报表");
	
	private String value;
	
	private String desc;

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	private MeasureToolReportEnums(String value, String desc) {
		this.value = value;
		this.desc = desc;
	}
	
    public static MeasureToolReportEnums getByValue(String value) {  
        for (MeasureToolReportEnums measureToolEnums : values()) {  
            if (measureToolEnums.getValue().equals(value)) {  
                return measureToolEnums;
            }  
        }  
        return null;  
    } 
	
}
