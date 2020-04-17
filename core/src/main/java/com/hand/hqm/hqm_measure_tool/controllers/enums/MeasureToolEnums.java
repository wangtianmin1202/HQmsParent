package com.hand.hqm.hqm_measure_tool.controllers.enums;

public enum MeasureToolEnums {
	NECKBAND("NECKBAND","领用"),
	MAINTAIN("MAINTAIN","维修"),
	SCRAP("SCRAP","报废");

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

	private MeasureToolEnums(String value, String desc) {
		this.value = value;
		this.desc = desc;
	}
	
    public static MeasureToolEnums getByValue(String value) {  
        for (MeasureToolEnums measureToolEnums : values()) {  
            if (measureToolEnums.getValue().equals(value)) {  
                return measureToolEnums;
            }  
        }  
        return null;  
    } 
	
}
