package com.hand.spc.repository.service;

import java.util.List;

import com.hand.hap.core.ProxySelf;
import com.hand.hap.system.service.IBaseService;
import com.hand.spc.repository.dto.EntiretyStatisticR;
import com.hand.spc.repository.dto.SampleSubgroupR;

public interface IEntiretyStatisticRService extends IBaseService<EntiretyStatisticR>, ProxySelf<IEntiretyStatisticRService> {

    /**
     * 批量保存整体计算数据
     *
     * @param entiretyStatisticList
     * @return
     */
    public int batchInsertRows(List<EntiretyStatisticR> entiretyStatisticList);

    EntiretyStatisticR selectByMaxNum(SampleSubgroupR sampleSubgroup);
}
