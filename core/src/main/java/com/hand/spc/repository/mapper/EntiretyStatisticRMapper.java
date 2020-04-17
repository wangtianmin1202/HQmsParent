package com.hand.spc.repository.mapper;

import java.util.List;

import com.hand.hap.mybatis.common.Mapper;
import com.hand.spc.repository.dto.EntiretyStatisticR;
import com.hand.spc.repository.dto.SampleSubgroupR;

public interface EntiretyStatisticRMapper extends Mapper<EntiretyStatisticR> {

    /**
     * 批量保存整体计算数据
     *
     * @param entiretyStatisticList
     * @return
     */
    public int batchInsertRows(List<EntiretyStatisticR> entiretyStatisticList);

    EntiretyStatisticR selectByMaxNum(SampleSubgroupR sampleSubgroup);
}
