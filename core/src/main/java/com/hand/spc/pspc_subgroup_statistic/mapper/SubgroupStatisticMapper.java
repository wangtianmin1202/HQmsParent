package com.hand.spc.pspc_subgroup_statistic.mapper;

import com.hand.hap.mybatis.common.Mapper;
import com.hand.spc.pspc_cpk_analysis.dto.CPKAnalyseReqDTO;
import com.hand.spc.pspc_subgroup_statistic.dto.SubgroupStatistic;
import com.hand.spc.pspc_subgroup_statistic.vo.SubgroupStatisticVo;

import java.util.List;

public interface SubgroupStatisticMapper extends Mapper<SubgroupStatistic>{
    List<SubgroupStatisticVo> selectByCodeAndVersion(CPKAnalyseReqDTO cpkAnalyseReqDTO);
}