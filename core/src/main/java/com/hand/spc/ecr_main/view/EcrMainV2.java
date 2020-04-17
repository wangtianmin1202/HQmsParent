package com.hand.spc.ecr_main.view;

import java.io.Serializable;

/*
 * 查询对应职位属性，物料级别属性，主负责人 辅助负责人信息
 */
public class EcrMainV2 implements Serializable{
	
	private String mainDuty;
	private String subDuty;
	private String mainPosition;
	private String subPosition;
	private String categoryFirst;
	private String categorySecond;
	private String categoryThird;
	private String ecrno;
	
	private String updateBy;
	
	private String email;
	
	
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getUpdateBy() {
		return updateBy;
	}
	public void setUpdateBy(String updateBy) {
		this.updateBy = updateBy;
	}
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
	public String getMainPosition() {
		return mainPosition;
	}
	public void setMainPosition(String mainPosition) {
		this.mainPosition = mainPosition;
	}
	public String getSubPosition() {
		return subPosition;
	}
	public void setSubPosition(String subPosition) {
		this.subPosition = subPosition;
	}
	public String getCategoryFirst() {
		return categoryFirst;
	}
	public void setCategoryFirst(String categoryFirst) {
		this.categoryFirst = categoryFirst;
	}
	public String getCategorySecond() {
		return categorySecond;
	}
	public void setCategorySecond(String categorySecond) {
		this.categorySecond = categorySecond;
	}
	public String getCategoryThird() {
		return categoryThird;
	}
	public void setCategoryThird(String categoryThird) {
		this.categoryThird = categoryThird;
	}
	
	
	

}
