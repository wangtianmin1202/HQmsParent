package com.hand.plm.product_func_attr_basic.view;

public class TreeVO {
	/**
	 * 树ID
	 */
	private String id;
	/**
	 * 父节点ID
	 */
	private String pId;
	/**
	 * 内容
	 */
	private String name;
	/**
	 * 是否展开
	 */
	private boolean open = true;
	/**
	 * 是否点击
	 */
	private boolean click = false;
	/**
	 * 层级
	 */
	private String level;
	
	private String nodeType;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getpId() {
		return pId;
	}

	public void setpId(String pId) {
		this.pId = pId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getNodeType() {
		return nodeType;
	}

	public void setNodeType(String nodeType) {
		this.nodeType = nodeType;
	}

	public boolean isOpen() {
		return open;
	}

	public void setOpen(boolean open) {
		this.open = open;
	}

	public boolean isClick() {
		return click;
	}

	public void setClick(boolean click) {
		this.click = click;
	}

	public String getLevel() {
		return level;
	}

	public void setLevel(String level) {
		this.level = level;
	}
}
