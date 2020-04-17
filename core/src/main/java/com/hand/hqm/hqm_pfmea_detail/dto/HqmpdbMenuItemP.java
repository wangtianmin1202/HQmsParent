//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.hand.hqm.hqm_pfmea_detail.dto;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.Transient;

import org.hibernate.validator.constraints.Length;
import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

public class HqmpdbMenuItemP implements Comparable<HqmpdbMenuItemP>, Serializable {
    private static final long serialVersionUID = -1412546289282861225L;
    private List<HqmpdbMenuItemP> children;
    private boolean expand = false;
    private String functionCode;
    private String icon;
    private Long id;
    private Boolean ischecked;
    @JsonIgnore
    private HqmpdbMenuItemP parent;
    private long score;
    private Long shortcutId;
    private String text;
    private String url;
    /**
     * 类型
     */
    private String type;
    
    //业务字段
    private Long branchId;
    @Transient
	private Long functionId;
    @Transient
	private Long structureId;

	private Long parentBranchId;

	private Long ranks;

	@Length(max = 100)
	private String branchName;
	
	private String rangeName;

	@Length(max = 200)
	private String invalidConsequence;

	private Long severity;

	@Length(max = 10)
	private String specialCharacteristicType;

	@Length(max = 200)
	private String failureReason;

	@Length(max = 200)
	private String preventiveMeasure;

	@Length(max = 200)
	private String detectionMeasure;

	private Long occurrence;

	private Long detection;

	private Long rpn;
	
    @Length(max = 200)
    private String suggestMeasure;

	private Float chargeId;
	@JsonFormat(pattern="yyyy-MM-dd",timezone = "GMT+8")
	private Date estimatedFinishTime;

	@Length(max = 200)
	private String measureResult;

	private Float postOccurrence;

	private Float postDetection;

	private Float postRpn;

	private Float postSeverity;

    @Transient
	private String useName;   
    

	@Transient
	private String functionName;

	@Transient
	private String structureName;
	
	private String claim;
	     
	private String potentialFailureMode;

	public String getClaim() {
		return claim;
	}

	public void setClaim(String claim) {
		this.claim = claim;
	}

	public String getPotentialFailureMode() {
		return potentialFailureMode;
	}

	public void setPotentialFailureMode(String potentialFailureMode) {
		this.potentialFailureMode = potentialFailureMode;
	}

	public String getSuggestMeasure() {
		return suggestMeasure;
	}

	public void setSuggestMeasure(String suggestMeasure) {
		this.suggestMeasure = suggestMeasure;
	}

	public Float getChargeId() {
		return chargeId;
	}

	public void setChargeId(Float chargeId) {
		this.chargeId = chargeId;
	}

	public Date getEstimatedFinishTime() {
		return estimatedFinishTime;
	}

	public void setEstimatedFinishTime(Date estimatedFinishTime) {
		this.estimatedFinishTime = estimatedFinishTime;
	}

	public String getMeasureResult() {
		return measureResult;
	}

	public void setMeasureResult(String measureResult) {
		this.measureResult = measureResult;
	}

	public Float getPostOccurrence() {
		return postOccurrence;
	}

	public void setPostOccurrence(Float postOccurrence) {
		this.postOccurrence = postOccurrence;
	}

	public Float getPostDetection() {
		return postDetection;
	}

	public void setPostDetection(Float postDetection) {
		this.postDetection = postDetection;
	}

	public Float getPostRpn() {
		return postRpn;
	}

	public void setPostRpn(Float postRpn) {
		this.postRpn = postRpn;
	}

	public Float getPostSeverity() {
		return postSeverity;
	}

	public void setPostSeverity(Float postSeverity) {
		this.postSeverity = postSeverity;
	}

	public String getUseName() {
		return useName;
	}

	public void setUseName(String useName) {
		this.useName = useName;
	}

	@Override
    public int compareTo(HqmpdbMenuItemP o) {
        return (int)(this.score - o.score);
    }

	public boolean isExpand() {
		return expand;
	}

	public void setExpand(boolean expand) {
		this.expand = expand;
	}


	public Long getFunctionId() {
		return functionId;
	}

	public void setFunctionId(Long functionId) {
		this.functionId = functionId;
	}

	public Long getStructureId() {
		return structureId;
	}

	public void setStructureId(Long structureId) {
		this.structureId = structureId;
	}

	

	public Long getRanks() {
		return ranks;
	}

	public void setRanks(Long ranks) {
		this.ranks = ranks;
	}

	
	public String getRangeName() {
		return rangeName;
	}

	public void setRangeName(String rangeName) {
		this.rangeName = rangeName;
	}

	public String getInvalidConsequence() {
		return invalidConsequence;
	}

	public void setInvalidConsequence(String invalidConsequence) {
		this.invalidConsequence = invalidConsequence;
	}

	
	

	

	public Long getOccurrence() {
		return occurrence;
	}

	public void setOccurrence(Long occurrence) {
		this.occurrence = occurrence;
	}

	public Long getDetection() {
		return detection;
	}

	public void setDetection(Long detection) {
		this.detection = detection;
	}

	public Long getRpn() {
		return rpn;
	}

	public String getFunctionCode() {
		return functionCode;
	}

	public void setFunctionCode(String functionCode) {
		this.functionCode = functionCode;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public void setRpn(Long rpn) {
		this.rpn = rpn;
	}

	public String getFunctionName() {
		return functionName;
	}

	public void setFunctionName(String functionName) {
		this.functionName = functionName;
	}

	public String getStructureName() {
		return structureName;
	}

	public void setStructureName(String structureName) {
		this.structureName = structureName;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public List<HqmpdbMenuItemP> getChildren() {
		return children;
	}

	public void setChildren(List<HqmpdbMenuItemP> children) {
		this.children = children;
	}

	public Long getBranchId() {
		return branchId;
	}

	public void setBranchId(Long branchId) {
		this.branchId = branchId;
	}

	public Long getParentBranchId() {
		return parentBranchId;
	}

	public void setParentBranchId(Long parentBranchId) {
		this.parentBranchId = parentBranchId;
	}

	public String getBranchName() {
		return branchName;
	}

	public void setBranchName(String branchName) {
		this.branchName = branchName;
	}

	public Long getSeverity() {
		return severity;
	}

	public void setSeverity(Long severity) {
		this.severity = severity;
	}

	public String getSpecialCharacteristicType() {
		return specialCharacteristicType;
	}

	public void setSpecialCharacteristicType(String specialCharacteristicType) {
		this.specialCharacteristicType = specialCharacteristicType;
	}

	public String getFailureReason() {
		return failureReason;
	}

	public void setFailureReason(String failureReason) {
		this.failureReason = failureReason;
	}

	public String getPreventiveMeasure() {
		return preventiveMeasure;
	}

	public void setPreventiveMeasure(String preventiveMeasure) {
		this.preventiveMeasure = preventiveMeasure;
	}

	public String getDetectionMeasure() {
		return detectionMeasure;
	}

	public void setDetectionMeasure(String detectionMeasure) {
		this.detectionMeasure = detectionMeasure;
	}
}
