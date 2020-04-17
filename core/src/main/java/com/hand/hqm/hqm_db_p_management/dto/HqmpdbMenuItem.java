//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.hand.hqm.hqm_db_p_management.dto;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Transient;

import org.hibernate.validator.constraints.Length;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class HqmpdbMenuItem implements Comparable<HqmpdbMenuItem>, Serializable {
    private static final long serialVersionUID = -1412546289282861225L;
    private List<HqmpdbMenuItem> children;
    private boolean expand = false;
    private String functionCode;
    private String icon;
    private Long id;
    private Boolean ischecked;
    @JsonIgnore
    private HqmpdbMenuItem parent;
    private long score;
    private Long shortcutId;
    private String text;
    private String url;
    /**
     * 类型
     */
    private String type;
    
    //业务字段
    private Long invalidId;
    @Transient
	private Long functionId;
    @Transient
	private Long structureId;

	private Long parentInvalidId;

	private Long ranks;

	@Length(max = 100)
	private String invalidName;
	
	private String rangeName;

	@Length(max = 200)
	private String invalidConsequence;

	private Long serious;

	@Length(max = 10)
	private String specialCharacterType;

	@Length(max = 200)
	private String invalidReason;

	@Length(max = 200)
	private String preventMeasure;

	@Length(max = 200)
	private String detectMeasure;

	private Long occurrence;

	private Long detection;

	private Long rpn;

	@Transient
	private String functionName;

	@Transient
	private String structureName;

	@Override
    public int compareTo(HqmpdbMenuItem o) {
        return (int)(this.score - o.score);
    }

	public boolean isExpand() {
		return expand;
	}

	public void setExpand(boolean expand) {
		this.expand = expand;
	}

	public Long getInvalidId() {
		return invalidId;
	}

	public void setInvalidId(Long invalidId) {
		this.invalidId = invalidId;
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

	public Long getParentInvalidId() {
		return parentInvalidId;
	}

	public void setParentInvalidId(Long parentInvalidId) {
		this.parentInvalidId = parentInvalidId;
	}

	public Long getRanks() {
		return ranks;
	}

	public void setRanks(Long ranks) {
		this.ranks = ranks;
	}

	public String getInvalidName() {
		return invalidName;
	}

	public void setInvalidName(String invalidName) {
		this.invalidName = invalidName;
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

	public Long getSerious() {
		return serious;
	}

	public void setSerious(Long serious) {
		this.serious = serious;
	}

	public String getInvalidReason() {
		return invalidReason;
	}

	public void setInvalidReason(String invalidReason) {
		this.invalidReason = invalidReason;
	}

	public String getPreventMeasure() {
		return preventMeasure;
	}

	public void setPreventMeasure(String preventMeasure) {
		this.preventMeasure = preventMeasure;
	}

	public String getDetectMeasure() {
		return detectMeasure;
	}

	public void setDetectMeasure(String detectMeasure) {
		this.detectMeasure = detectMeasure;
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

	public List<HqmpdbMenuItem> getChildren() {
		return children;
	}

	public void setChildren(List<HqmpdbMenuItem> children) {
		this.children = children;
	}

	public String getSpecialCharacterType() {
		return specialCharacterType;
	}

	public void setSpecialCharacterType(String specialCharacterType) {
		this.specialCharacterType = specialCharacterType;
	}
	
	

}
