//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.hand.hqm.file_classify.dto;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.Transient;

import org.hibernate.validator.constraints.Length;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class MenuItem implements Comparable<MenuItem>, Serializable {
    private static final long serialVersionUID = -1412546289282861225L;
    private List<MenuItem> children;
    private boolean expand = false;
    private String functionCode;
    private String icon;
    private Long id;
    private Boolean ischecked;
    @JsonIgnore
    private MenuItem parent;
    private long score;
    private Long shortcutId;
    private String text;
    private String url;
    /**
     * 类型
     */
    private String type;
    
    //业务字段
    private Long classifyId;
    @Transient
	private Long functionId;
    @Transient
	private Long structureId;
    @Length(max = 30)
	private String classifyCode;
	@Length(max = 30)
	private String classifyDescriptions;

	private Long parentClassifyId;
	@Length(max = 15)
	private String mainClassifyCode;

	@Length(max = 15)
	private String enableFlag;

	@Transient
	private Float sparePartId;
	
	@Transient
	private String sparePartCode;
	
	@Transient
	private String sparePartName;
	
	@Transient
	private Float parentId;

	public Float getSparePartId() {
		return sparePartId;
	}

	public void setSparePartId(Float sparePartId) {
		this.sparePartId = sparePartId;
	}

	public String getSparePartCode() {
		return sparePartCode;
	}

	public void setSparePartCode(String sparePartCode) {
		this.sparePartCode = sparePartCode;
	}

	public String getSparePartName() {
		return sparePartName;
	}

	public void setSparePartName(String sparePartName) {
		this.sparePartName = sparePartName;
	}

	public Float getParentId() {
		return parentId;
	}

	public void setParentId(Float parentId) {
		this.parentId = parentId;
	}

	@Override
    public int compareTo(MenuItem o) {
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

	

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public List<MenuItem> getChildren() {
		return children;
	}

	public void setChildren(List<MenuItem> children) {
		this.children = children;
	}

	public Long getClassifyId() {
		return classifyId;
	}

	public void setClassifyId(Long classifyId) {
		this.classifyId = classifyId;
	}

	public String getClassifyCode() {
		return classifyCode;
	}

	public void setClassifyCode(String classifyCode) {
		this.classifyCode = classifyCode;
	}

	public String getClassifyDescriptions() {
		return classifyDescriptions;
	}

	public void setClassifyDescriptions(String classifyDescriptions) {
		this.classifyDescriptions = classifyDescriptions;
	}

	public Long getParentClassifyId() {
		return parentClassifyId;
	}

	public void setParentClassifyId(Long parentClassifyId) {
		this.parentClassifyId = parentClassifyId;
	}

	public String getMainClassifyCode() {
		return mainClassifyCode;
	}

	public void setMainClassifyCode(String mainClassifyCode) {
		this.mainClassifyCode = mainClassifyCode;
	}

	public String getEnableFlag() {
		return enableFlag;
	}

	public void setEnableFlag(String enableFlag) {
		this.enableFlag = enableFlag;
	}

	
	
	
	

}
