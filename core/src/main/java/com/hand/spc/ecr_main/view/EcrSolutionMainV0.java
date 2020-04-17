package com.hand.spc.ecr_main.view;

import java.util.Date;

import com.hand.spc.ecr_main.dto.EcrSolutionMain;

public class EcrSolutionMainV0 extends EcrSolutionMain {

	private Date finishDate;
	
	private String mainDuty;

	 

	public Date getFinishDate() {
		return finishDate;
	}

	public void setFinishDate(Date finishDate) {
		this.finishDate = finishDate;
	}

	public String getMainDuty() {
		return mainDuty;
	}

	public void setMainDuty(String mainDuty) {
		this.mainDuty = mainDuty;
	}
	
	
}
