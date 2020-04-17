package com.hand.spc.pspc_sample_subgroup_rel.mapper;

import com.hand.hap.mybatis.common.Mapper;
import com.hand.spc.pspc_cpk_analysis.dto.CPKAnalyseReqDTO;
import com.hand.spc.pspc_sample_subgroup_rel.dto.SampleSubgroupRel;

import java.util.List;

public interface SampleSubgroupRelMapper extends Mapper<SampleSubgroupRel>{
    List<SampleSubgroupRel> selectBySampleSubgroup(CPKAnalyseReqDTO cpkAnalyseReqDTO);
}