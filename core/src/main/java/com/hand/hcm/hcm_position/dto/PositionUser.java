package com.hand.hcm.hcm_position.dto;

import com.hand.hap.mybatis.annotation.ExtensionAttribute;
import com.hand.hap.system.dto.BaseDTO;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * description
 *
 * @author KOCDZX0 2020/03/11 9:52 AM
 */
@ExtensionAttribute(disable=true)
@Table(name = "HCM_POSITION_USER_ROLE")
public class PositionUser extends BaseDTO {

    @Id
    @GeneratedValue
    private Long kid;

    private Long positionId;

    private Long userId;

    @Transient
    private String employeeName;

    @Transient
    private String userName;

    @Transient
    private String email;

    public Long getKid() {
        return kid;
    }

    public void setKid(Long kid) {
        this.kid = kid;
    }

    public Long getPositionId() {
        return positionId;
    }

    public void setPositionId(Long positionId) {
        this.positionId = positionId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
