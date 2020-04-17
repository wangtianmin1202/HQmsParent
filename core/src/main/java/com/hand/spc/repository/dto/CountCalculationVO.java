package com.hand.spc.repository.dto;

import java.util.List;

public class CountCalculationVO extends BaseCalcResultVO {

    private List<CountStatisticR> countStatisticList;

    public List<CountStatisticR> getCountStatisticList() {
        return countStatisticList;
    }

    public void setCountStatisticList(List<CountStatisticR> countStatisticList) {
        this.countStatisticList = countStatisticList;
    }

}
