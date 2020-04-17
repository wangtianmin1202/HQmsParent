package com.hand.npi.npi_route.dto;

import java.util.List;

/**Auto Generated By Hap Code Generator**/
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import com.hand.hap.mybatis.annotation.ExtensionAttribute;
import org.hibernate.validator.constraints.Length;
import javax.persistence.Table;
import com.hand.hap.system.dto.BaseDTO;
import javax.persistence.Transient;
import com.hand.hap.core.annotation.Children;

@ExtensionAttribute(disable = true)
@Table(name = "NPI_TECHNOLOGY_WORKING_PROCEDURE")
public class TechnologyWorkingProcedure extends BaseDTO {
	//工序信息表
	public static final String FIELD_ID = "id";
	public static final String FIELD_STATUS = "status";

	@Id
	@GeneratedValue
	private Float routeWpRefId;
	
	/**
	 * 工艺路径主键
	 */
	private Float routeId;
	
	/**
	 * 工序主键
	 */
	private Float wpId;
	
	/**
	 * 工序名称
	 */
	@Transient
	private String wpName;
	/**
	 * 工序编码
	 */
	@Transient
	private String wpCode;

	/**
	 * 序号
	 */
	private Float serialNumber;

	/**
	 * 标准工时
	 */
	private Float standardWorkingHours;

	/**
	 * 前置工序
	 */
	@Length(max = 50)
	private String preWorkingProcedure;

	/**
	 * 内制/外制
	 */
	@Length(max = 10)
	private String inOutMake;
	
	/**
	 * 手动/自动
	 */
	@Length(max = 10)
	private String autoFlag;

	/**
	 * 工艺路径和工序关系版本
	 */
	@Length(max = 10)
	private String version;


	/**
	 * SOP动画ID
	 */
	private Float wpSopRefId;

	/**
	 * 主线/辅线
	 */
	@Length(max = 10)
	private String lineType;

	/**
	 * 主线/辅线编号
	 */
	@Length(max = 50)
	private String lineCode;

	/**
	 * 状态
	 */
	@Length(max = 10)
	private String status;
	
	/**
	 * 是否是复制来的数据 1是 0不是
	 */
	@Length(max = 10)
	private String copyFlag;
	
	@Transient
	private String skuId;

	// 工序有三个行表
	// 1.工艺动作行表
	@Children
	@Transient
	private List<TechnologyWpAction> matList;

	// 2.标准动作行表
	@Children
	@Transient
	private TechnologyWpStandardActionDetail staDetail;

	// 3.spec要求行表
	@Children
	@Transient
	private List<TechnologyWpSpecDetail> specDetail;
	
	// 4.工装设备的grid数据
	@Children
	@Transient
	private List<TechnologyWpActionEquipDetail> equipDetail;

	public void setSerialNumber(Float serialNumber) {
		this.serialNumber = serialNumber;
	}

	public Float getSerialNumber() {
		return serialNumber;
	}



	public void setStandardWorkingHours(Float standardWorkingHours) {
		this.standardWorkingHours = standardWorkingHours;
	}

	public Float getStandardWorkingHours() {
		return standardWorkingHours;
	}

	public void setPreWorkingProcedure(String preWorkingProcedure) {
		this.preWorkingProcedure = preWorkingProcedure;
	}

	public String getPreWorkingProcedure() {
		return preWorkingProcedure;
	}

	public void setInOutMake(String inOutMake) {
		this.inOutMake = inOutMake;
	}

	public String getInOutMake() {
		return inOutMake;
	}

	public Float getWpSopRefId() {
		return wpSopRefId;
	}

	public void setWpSopRefId(Float wpSopRefId) {
		this.wpSopRefId = wpSopRefId;
	}

	public void setLineType(String lineType) {
		this.lineType = lineType;
	}

	public String getLineType() {
		return lineType;
	}

	public void setLineCode(String lineCode) {
		this.lineCode = lineCode;
	}

	public String getLineCode() {
		return lineCode;
	}

	
	public String getAutoFlag() {
		return autoFlag;
	}

	public void setAutoFlag(String autoFlag) {
		this.autoFlag = autoFlag;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getStatus() {
		return status;
	}

	public List<TechnologyWpAction> getMatList() {
		return matList;
	}

	public void setMatList(List<TechnologyWpAction> matList) {
		this.matList = matList;
	}

	public TechnologyWpStandardActionDetail getStaDetail() {
		return staDetail;
	}

	public void setStaDetail(TechnologyWpStandardActionDetail staDetail) {
		this.staDetail = staDetail;
	}

	public String getSkuId() {
		return skuId;
	}

	public void setSkuId(String skuId) {
		this.skuId = skuId;
	}

	public String getCopyFlag() {
		return copyFlag;
	}

	public void setCopyFlag(String copyFlag) {
		this.copyFlag = copyFlag;
	}

	public List<TechnologyWpSpecDetail> getSpecDetail() {
		return specDetail;
	}

	public void setSpecDetail(List<TechnologyWpSpecDetail> specDetail) {
		this.specDetail = specDetail;
	}

	public List<TechnologyWpActionEquipDetail> getEquipDetail() {
		return equipDetail;
	}

	public void setEquipDetail(List<TechnologyWpActionEquipDetail> equipDetail) {
		this.equipDetail = equipDetail;
	}

	public Float getRouteWpRefId() {
		return routeWpRefId;
	}

	public void setRouteWpRefId(Float routeWpRefId) {
		this.routeWpRefId = routeWpRefId;
	}

	public Float getRouteId() {
		return routeId;
	}

	public void setRouteId(Float routeId) {
		this.routeId = routeId;
	}

	public Float getWpId() {
		return wpId;
	}

	public void setWpId(Float wpId) {
		this.wpId = wpId;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getWpName() {
		return wpName;
	}

	public void setWpName(String wpName) {
		this.wpName = wpName;
	}


	public String getWpCode() {
		return wpCode;
	}

	public void setWpCode(String wpCode) {
		this.wpCode = wpCode;
	}


}