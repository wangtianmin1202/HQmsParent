package com.hand.plm.spec_product_detail.constant;

public interface PlmSpecApprovalStatus {
	/**
	 * 在提交审批前均为此状态
	 */
	String DRAFT_VALUE = "DRAFT";
	
	/**
	 * 一级审批
	 */
	String FIRST_LEVEL_APPROVAL = "FIRST_LEVEL_APPROVAL";
	
	/**
	 * 二级审批
	 */
	String SECONDARY_APPROVAL = "SECONDARY_APPROVAL";
	
	/**
	 * 三级审批
	 */
	String THREE_LEVEL_APPROVAL = "THREE_LEVEL_APPROVAL";
	
	/**
	 * 生效
	 */
	String TAKE_EFFECT = "TAKE_EFFECT";
	
	/**
	 * 废止
	 */
	String ABOLISH = "ABOLISH";
	
	/**
	 * 业务状态（申请中的要求不能被更改）
	 */
	String UNDER_REVIEW = "UNDER_REVIEW";
	
}
