package com.hand.spc.pspc_entirety_statistic.mapper;

import com.hand.hap.mybatis.common.Mapper;
import com.hand.spc.pspc_entirety_statistic.dto.EntiretyStatistic;
import com.hand.spc.pspc_sample_subgroup.dto.SampleSubgroup;

public interface EntiretyStatisticMapper extends Mapper<EntiretyStatistic>{
    EntiretyStatistic selectByMaxNum(SampleSubgroup sampleSubgroup);
}