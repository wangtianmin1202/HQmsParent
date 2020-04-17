package com.hand.spc.ecr_main.view;

import java.io.Serializable;

import com.hand.spc.ecr_main.dto.EcrTechnicalFileLine;

public class EcrTechnicalFileLineV0 extends EcrTechnicalFileLine{
	
	private String fileType;

	private String ecrno;
	
	
	public String getEcrno() {
		return ecrno;
	}

	public void setEcrno(String ecrno) {
		this.ecrno = ecrno;
	}

	public String getFileType() {
		return fileType;
	}

	public void setFileType(String fileType) {
		this.fileType = fileType;
	}
	
	
}
