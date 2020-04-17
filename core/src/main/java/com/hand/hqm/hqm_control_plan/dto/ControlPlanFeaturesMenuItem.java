//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.hand.hqm.hqm_control_plan.dto;

import java.io.Serializable;
import java.util.List;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Transient;

import org.hibernate.validator.constraints.Length;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;

public class ControlPlanFeaturesMenuItem implements Comparable<ControlPlanFeaturesMenuItem>, Serializable {
    private static final long serialVersionUID = -1412546289282861225L;
    private List<ControlPlanFeaturesMenuItem> children;
    private boolean expand = false;
    private String functionCode;
    private String icon;
    private Long id;
    private Boolean ischecked;
    @JsonIgnore
    private ControlPlanFeaturesMenuItem parent;
    private long score;
    private Long shortcutId;
    private String text;
    private String url;
    /**
     * 类型
     */
    private String type;
    
    //业务字段
    @Id
	@GeneratedValue
	private Long featuresId;

	private Long branchId;
    
	public Long getBranchId() {
		return branchId;
	}

	public void setBranchId(Long branchId) {
		this.branchId = branchId;
	}

	private Long parentFeaturesId;

	private Long controlPlanId;

	private Long ranks;

	@Length(max = 50)
	private String featuresName;

	@Length(max = 10)
	private String featuresType;

	@Length(max = 200)
	private String featuresContent;

	@Length(max = 50)
	private String equipment;

	@Length(max = 10)
	private String specialCharacterType;

	@Length(max = 200)
	private String standrad;

	@Length(max = 200)
	private String detectionEquipment;

	private String sampleSize;
	
	private String enableFlag;
	
	public String getEnableFlag() {
		return enableFlag;
	}

	public void setEnableFlag(String enableFlag) {
		this.enableFlag = enableFlag;
	}

	@Length(max = 50)
	private String detectionFrequency;

	@Length(max = 300)
	private String controlMethod;

	private Long grR;

	@Length(max = 50)
	private String processCapability;

	@Length(max = 300)
	private String reactionPlan;
	
	@Length(max = 300)
	private String feature;

    public String getFeature() {
		return feature;
	}

	public void setFeature(String feature) {
		this.feature = feature;
	}

	private Long objVNumber;
    
    private Long  processId;
    /**
     * 防篡改校验字段(非数据库字段).
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Transient
    private String _token;
    
    
    public String get_token() {
		return _token;
	}

	public void set_token(String _token) {
		this._token = _token;
	}

	public Long getObjVNumber() {
		return objVNumber;
	}

	public void setObjVNumber(Long objVNumber) {
		this.objVNumber = objVNumber;
	}

	public void setFeaturesId(Long featuresId) {
		this.featuresId = featuresId;
	}

	public Long getFeaturesId() {
		return featuresId;
	}

	public void setParentFeaturesId(Long parentFeaturesId) {
		this.parentFeaturesId = parentFeaturesId;
	}

	public Long getParentFeaturesId() {
		return parentFeaturesId;
	}

	public void setControlPlanId(Long controlPlanId) {
		this.controlPlanId = controlPlanId;
	}

	public Long getControlPlanId() {
		return controlPlanId;
	}

	public void setRanks(Long ranks) {
		this.ranks = ranks;
	}

	public Long getRanks() {
		return ranks;
	}

	public void setFeaturesName(String featuresName) {
		this.featuresName = featuresName;
	}

	public String getFeaturesName() {
		return featuresName;
	}

	public void setFeaturesType(String featuresType) {
		this.featuresType = featuresType;
	}

	public String getFeaturesType() {
		return featuresType;
	}

	public void setFeaturesContent(String featuresContent) {
		this.featuresContent = featuresContent;
	}

	public String getFeaturesContent() {
		return featuresContent;
	}

	public void setEquipment(String equipment) {
		this.equipment = equipment;
	}

	public String getEquipment() {
		return equipment;
	}

	public void setSpecialCharacterType(String specialCharacterType) {
		this.specialCharacterType = specialCharacterType;
	}

	public String getSpecialCharacterType() {
		return specialCharacterType;
	}

	public void setStandrad(String standrad) {
		this.standrad = standrad;
	}

	public String getStandrad() {
		return standrad;
	}

	public void setDetectionEquipment(String detectionEquipment) {
		this.detectionEquipment = detectionEquipment;
	}

	public String getDetectionEquipment() {
		return detectionEquipment;
	}

	public void setSampleSize(String sampleSize) {
		this.sampleSize = sampleSize;
	}

	public String getSampleSize() {
		return sampleSize;
	}

	public void setDetectionFrequency(String detectionFrequency) {
		this.detectionFrequency = detectionFrequency;
	}

	public String getDetectionFrequency() {
		return detectionFrequency;
	}

	public void setControlMethod(String controlMethod) {
		this.controlMethod = controlMethod;
	}

	public String getControlMethod() {
		return controlMethod;
	}

	public void setGrR(Long grR) {
		this.grR = grR;
	}

	public Long getGrR() {
		return grR;
	}

	public void setProcessCapability(String processCapability) {
		this.processCapability = processCapability;
	}

	public String getProcessCapability() {
		return processCapability;
	}

	public void setReactionPlan(String reactionPlan) {
		this.reactionPlan = reactionPlan;
	}

	public String getReactionPlan() {
		return reactionPlan;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public List<ControlPlanFeaturesMenuItem> getChildren() {
		return children;
	}

	public void setChildren(List<ControlPlanFeaturesMenuItem> children) {
		this.children = children;
	}

	public boolean isExpand() {
		return expand;
	}

	public void setExpand(boolean expand) {
		this.expand = expand;
	}

	public String getFunctionCode() {
		return functionCode;
	}

	public void setFunctionCode(String functionCode) {
		this.functionCode = functionCode;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Boolean getIschecked() {
		return ischecked;
	}

	public void setIschecked(Boolean ischecked) {
		this.ischecked = ischecked;
	}

	public ControlPlanFeaturesMenuItem getParent() {
		return parent;
	}

	public void setParent(ControlPlanFeaturesMenuItem parent) {
		this.parent = parent;
	}

	public long getScore() {
		return score;
	}

	public void setScore(long score) {
		this.score = score;
	}

	public Long getShortcutId() {
		return shortcutId;
	}

	public void setShortcutId(Long shortcutId) {
		this.shortcutId = shortcutId;
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

	@Override
    public int compareTo(ControlPlanFeaturesMenuItem o) {
        return (int)(this.score - o.score);
    }

	public Long getProcessId() {
		return processId;
	}

	public void setProcessId(Long processId) {
		this.processId = processId;
	}

}
