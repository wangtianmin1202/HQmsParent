package com.hand.spc.pspc_sample_subgroup.mapper;

import com.hand.hap.mybatis.common.Mapper;
import com.hand.spc.pspc_cpk_analysis.dto.CPKAnalyseReqDTO;
import com.hand.spc.pspc_sample_subgroup.dto.SampleSubgroup;

import java.util.List;

public interface SampleSubgroupMapper extends Mapper<SampleSubgroup>{
    /**
     * CPK分析图查询
     *
     * @param requestDTO
     * @return
     */
    public List<SampleSubgroup> querySampleSubgroupByCPK(CPKAnalyseReqDTO requestDTO);

}