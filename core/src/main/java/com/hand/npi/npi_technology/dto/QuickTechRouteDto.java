package com.hand.npi.npi_technology.dto;

public class QuickTechRouteDto {
	
	/**
	 * 参考sku
	 */
	private Float oldSku;
	/**
	 * 参考工艺路径版本
	 */
	private String routeVersion;
	/**
	 * 参考Line
	 */
	private String oldLine;
	/**
	 * 新Sku
	 */
	private Float newSku;
	/**
	 * 新Line
	 */
	private String newLine;


	public String getRouteVersion() {
		return routeVersion;
	}

	public void setRouteVersion(String routeVersion) {
		this.routeVersion = routeVersion;
	}

	public String getOldLine() {
		return oldLine;
	}

	public void setOldLine(String oldLine) {
		this.oldLine = oldLine;
	}


	public String getNewLine() {
		return newLine;
	}

	public void setNewLine(String newLine) {
		this.newLine = newLine;
	}

	public Float getOldSku() {
		return oldSku;
	}

	public void setOldSku(Float oldSku) {
		this.oldSku = oldSku;
	}

	public Float getNewSku() {
		return newSku;
	}

	public void setNewSku(Float newSku) {
		this.newSku = newSku;
	}
}
