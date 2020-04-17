package com.hand.spc.repository.service;

import java.util.List;

import com.hand.hap.core.ProxySelf;
import com.hand.hap.system.service.IBaseService;
import com.hand.spc.repository.dto.CPKAnalyseReqDTO;
import com.hand.spc.repository.dto.SampleSubgroupRelationR;

public interface ISampleSubgroupRelationRService extends IBaseService<SampleSubgroupRelationR>,ProxySelf<ISampleSubgroupRelationRService> {

    /**
     * 批量保存分组关系
     *
     * @param subgroupRelationList
     * @return
     */
    public int batchInsertRows(List<SampleSubgroupRelationR> subgroupRelationList);

    List<SampleSubgroupRelationR> selectBySampleSubgroup(CPKAnalyseReqDTO cpkAnalyseReqDTO);


}
