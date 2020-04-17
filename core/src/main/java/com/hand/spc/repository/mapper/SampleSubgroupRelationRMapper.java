package com.hand.spc.repository.mapper;

import java.util.List;

import com.hand.hap.mybatis.common.Mapper;
import com.hand.spc.repository.dto.AndersonDarlingDTO;
import com.hand.spc.repository.dto.CPKAnalyseReqDTO;
import com.hand.spc.repository.dto.SampleSubgroupRelationR;

public interface SampleSubgroupRelationRMapper extends Mapper<SampleSubgroupRelationR> {

    /**
     * 批量保存分组关系
     *
     * @param subgroupRelationList
     * @return
     */
    public int batchInsertRows(List<SampleSubgroupRelationR> subgroupRelationList);


    List<SampleSubgroupRelationR> selectBySampleSubgroup(CPKAnalyseReqDTO cpkAnalyseReqDTO);
    
    List<SampleSubgroupRelationR> selectBySampleSubgroupByAD(AndersonDarlingDTO andersonDarlingDTO);
}
