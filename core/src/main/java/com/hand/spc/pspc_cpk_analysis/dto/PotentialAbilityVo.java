package com.hand.spc.pspc_cpk_analysis.dto;

import java.math.BigDecimal;

public class PotentialAbilityVo {

    private BigDecimal CP	;//
    private BigDecimal 	CPL	;//
    private BigDecimal 	CPU	;//
    private BigDecimal 	CPK	;//

    public BigDecimal getCP() {
        return CP;
    }

    public void setCP(BigDecimal CP) {
        this.CP = CP;
    }

    public BigDecimal getCPL() {
        return CPL;
    }

    public void setCPL(BigDecimal CPL) {
        this.CPL = CPL;
    }

    public BigDecimal getCPU() {
        return CPU;
    }

    public void setCPU(BigDecimal CPU) {
        this.CPU = CPU;
    }

    public BigDecimal getCPK() {
        return CPK;
    }

    public void setCPK(BigDecimal CPK) {
        this.CPK = CPK;
    }
}
