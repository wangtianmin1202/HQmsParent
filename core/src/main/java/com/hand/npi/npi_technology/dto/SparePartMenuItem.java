/**
 * 
 */
package com.hand.npi.npi_technology.dto;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hand.hqm.file_classify.dto.MenuItem;


/** 
 *@version:1.0
 *@Description: 
 *@author: Magicor
 *@date: Jan 19, 2020 9:38:52 AM
*/
public class SparePartMenuItem implements Comparable<SparePartMenuItem>, Serializable {
	
	private static final long serialVersionUID = -1412546289282861225L;
	
	private List<SparePartMenuItem> children;
	
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
	private Float sparePartId;
	
	private String sparePartCode;
	
	private String sparePartName;
	
	private Float parentId;
	
	private String sparePartLevel;
	
	@Override
	public int compareTo(SparePartMenuItem o) {
		return (int)(this.score - o.score);
	}

	public List<SparePartMenuItem> getChildren() {
		return children;
	}

	public void setChildren(List<SparePartMenuItem> children) {
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

	public MenuItem getParent() {
		return parent;
	}

	public void setParent(MenuItem parent) {
		this.parent = parent;
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

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

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

	public String getSparePartLevel() {
		return sparePartLevel;
	}

	public void setSparePartLevel(String sparePartLevel) {
		this.sparePartLevel = sparePartLevel;
	}
}
