package com.hand.spc.repository.dto;

import java.util.List;

public class SubGroupCalcResultVO extends BaseCalcResultVO {

    private List<SubgroupStatisticR> subgroupStatisticList;

    public List<SubgroupStatisticR> getSubgroupStatisticList() {
        return subgroupStatisticList;
    }

    public void setSubgroupStatisticList(List<SubgroupStatisticR> subgroupStatisticList) {
        this.subgroupStatisticList = subgroupStatisticList;
    }

}
