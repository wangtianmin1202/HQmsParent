package com.hand.spc.repository.dto;

import java.math.BigDecimal;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import com.hand.hap.mybatis.annotation.ExtensionAttribute;
import com.hand.hap.system.dto.BaseDTO;

@Table(name = "pspc_coefficient")//系数表
@ExtensionAttribute(disable = true)
public class CoefficientR extends BaseDTO {

    @Id
    @GeneratedValue
    private Long coefficientId;//主键
    private Long subgroupSize;//子组大小
    private BigDecimal a2;
    private BigDecimal a3;
    private BigDecimal a4;
    private BigDecimal b3;
    private BigDecimal b4;
    private BigDecimal d3;
    private BigDecimal d4;
    private BigDecimal e2;

    public Long getCoefficientId() {
        return coefficientId;
    }

    public void setCoefficientId(Long coefficientId) {
        this.coefficientId = coefficientId;
    }

    public Long getSubgroupSize() {
        return subgroupSize;
    }

    public void setSubgroupSize(Long subgroupSize) {
        this.subgroupSize = subgroupSize;
    }

    public BigDecimal getA2() {
        return a2;
    }

    public void setA2(BigDecimal a2) {
        this.a2 = a2;
    }

    public BigDecimal getA3() {
        return a3;
    }

    public void setA3(BigDecimal a3) {
        this.a3 = a3;
    }

    public BigDecimal getA4() {
        return a4;
    }

    public void setA4(BigDecimal a4) {
        this.a4 = a4;
    }

    public BigDecimal getB3() {
        return b3;
    }

    public void setB3(BigDecimal b3) {
        this.b3 = b3;
    }

    public BigDecimal getB4() {
        return b4;
    }

    public void setB4(BigDecimal b4) {
        this.b4 = b4;
    }

    public BigDecimal getD3() {
        return d3;
    }

    public void setD3(BigDecimal d3) {
        this.d3 = d3;
    }

    public BigDecimal getD4() {
        return d4;
    }

    public void setD4(BigDecimal d4) {
        this.d4 = d4;
    }

    public BigDecimal getE2() {
        return e2;
    }

    public void setE2(BigDecimal e2) {
        this.e2 = e2;
    }
}
