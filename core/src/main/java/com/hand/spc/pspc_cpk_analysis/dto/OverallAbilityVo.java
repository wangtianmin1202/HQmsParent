package com.hand.spc.pspc_cpk_analysis.dto;

import java.math.BigDecimal;

public class OverallAbilityVo {

    private BigDecimal PP	;//	过程能力指数
    private BigDecimal 	ppl	;//	下限过程能力指数
    private BigDecimal 	PPU	;//	上限过程能力指数
    private BigDecimal 	PPK	;//	修正的过程能力指数
    private BigDecimal 	CPM	;//todo

    public BigDecimal getPP() {
        return PP;
    }

    public void setPP(BigDecimal PP) {
        this.PP = PP;
    }

    public BigDecimal getPpl() {
        return ppl;
    }

    public void setPpl(BigDecimal ppl) {
        this.ppl = ppl;
    }

    public BigDecimal getPPU() {
        return PPU;
    }

    public void setPPU(BigDecimal PPU) {
        this.PPU = PPU;
    }

    public BigDecimal getPPK() {
        return PPK;
    }

    public void setPPK(BigDecimal PPK) {
        this.PPK = PPK;
    }

    public BigDecimal getCPM() {
        return CPM;
    }

    public void setCPM(BigDecimal CPM) {
        this.CPM = CPM;
    }
}
