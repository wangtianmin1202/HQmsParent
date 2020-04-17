package com.hand.spc.ecr_main.view;

import java.io.Serializable;
import java.util.List;

public class EcrItemSkuV2  implements Serializable{
	
	private EcrItemSkuV0 headList;
	private List<EcrItemSkuV1> lineList;
	 
		
	public EcrItemSkuV0 getHeadList() {
		return headList;
	}
	public void setHeadList(EcrItemSkuV0 headList) {
		this.headList = headList;
	}
	public List<EcrItemSkuV1> getLineList() {
		return lineList;
	}
	public void setLineList(List<EcrItemSkuV1> lineList) {
		this.lineList = lineList;
	}	
}
