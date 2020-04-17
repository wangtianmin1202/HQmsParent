package com.hand.spc.ecr_main.view;

import java.io.Serializable;
import java.util.List;

public class EcrSolutionSkuV2 implements Serializable{

	private EcrSolutionSkuV0 headList;
	private List<EcrSolutionSkuV1> lineList;
	public EcrSolutionSkuV0 getHeadList() {
		return headList;
	}
	public void setHeadList(EcrSolutionSkuV0 headList) {
		this.headList = headList;
	}
	public List<EcrSolutionSkuV1> getLineList() {
		return lineList;
	}
	public void setLineList(List<EcrSolutionSkuV1> lineList) {
		this.lineList = lineList;
	}
	 
 
}
