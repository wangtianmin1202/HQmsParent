package com.hand.spc.ecr_main.view;

import java.io.Serializable;

public class EcrMaterialV1 implements   Serializable{
	private String ecrno; 
	private String mainDuty;
	private String subDuty;
	private String risk;
	public String getEcrno() {
		return ecrno;
	}
	public void setEcrno(String ecrno) {
		this.ecrno = ecrno;
	}
	 
	public String getMainDuty() {
		return mainDuty;
	}
	public void setMainDuty(String mainDuty) {
		this.mainDuty = mainDuty;
	}
	public String getSubDuty() {
		return subDuty;
	}
	public void setSubDuty(String subDuty) {
		this.subDuty = subDuty;
	}
	
	

}
