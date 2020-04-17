package com.hand.hap.system.dto;

import com.hand.hap.mybatis.annotation.ExtensionAttribute;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import java.util.Date;

/**
 * create by:jialong.zuo@hand-china.com on 2016/10/11.
 */

@Table(name = "sys_user_login")
@ExtensionAttribute(disable = true)
public class UserLogin extends BaseDTO{
    @Id
    @GeneratedValue
    private Long loginId;

    private Long userId;

    private Date loginTime;

    private String ip;

    private String referer;

    private String userAgent;
    
    private String successFlag;
    
    @Transient
    private String userName;
    @Transient
    private Float employeeId;
    @Transient
    private String employeeCode;
    @Transient 
    private String name;
    @Transient
    private Float supplierId;
    @Transient
    private String supplierCode;
    @Transient
    private String supplierName;
    @Transient
    private String startTime;
    @Transient
    private String endTime;
    @Transient
    private String enableFlag;

    public String getSuccessFlag() {
		return successFlag;
	}

	public void setSuccessFlag(String successFlag) {
		this.successFlag = successFlag;
	}

	public String getEnableFlag() {
		return enableFlag;
	}

	public void setEnableFlag(String enableFlag) {
		this.enableFlag = enableFlag;
	}

	public Long getLoginId() {
        return loginId;
    }

    public void setLoginId(Long loginId) {
        this.loginId = loginId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Date getLoginTime() {
        return loginTime;
    }

    public void setLoginTime(Date loginTime) {
        this.loginTime = loginTime;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getReferer() {
        return referer;
    }

    public void setReferer(String referer) {
        this.referer = referer;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public Float getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(Float employeeId) {
		this.employeeId = employeeId;
	}

	public String getEmployeeCode() {
		return employeeCode;
	}

	public void setEmployeeCode(String employeeCode) {
		this.employeeCode = employeeCode;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Float getSupplierId() {
		return supplierId;
	}

	public void setSupplierId(Float supplierId) {
		this.supplierId = supplierId;
	}

	public String getSupplierCode() {
		return supplierCode;
	}

	public void setSupplierCode(String supplierCode) {
		this.supplierCode = supplierCode;
	}

	public String getSupplierName() {
		return supplierName;
	}

	public void setSupplierName(String supplierName) {
		this.supplierName = supplierName;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
    
}
