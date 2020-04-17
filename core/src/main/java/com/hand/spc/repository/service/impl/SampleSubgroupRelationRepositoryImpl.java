package com.hand.spc.repository.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hand.hap.system.service.impl.BaseServiceImpl;
import com.hand.spc.repository.dto.CPKAnalyseReqDTO;
import com.hand.spc.repository.dto.SampleSubgroupRelationR;
import com.hand.spc.repository.mapper.SampleSubgroupRelationRMapper;
import com.hand.spc.repository.service.ISampleSubgroupRelationRService;

@Service
@Transactional(rollbackFor = Exception.class)
public class SampleSubgroupRelationRepositoryImpl extends BaseServiceImpl<SampleSubgroupRelationR> implements ISampleSubgroupRelationRService {

    @Autowired
    private SampleSubgroupRelationRMapper sampleSubgroupRelationMapper;

    @Override
    public int batchInsertRows(List<SampleSubgroupRelationR> subgroupRelationList) {
        return sampleSubgroupRelationMapper.batchInsertRows(subgroupRelationList);
    }

    @Override
    public List<SampleSubgroupRelationR> selectBySampleSubgroup(CPKAnalyseReqDTO cpkAnalyseReqDTO) {


        return sampleSubgroupRelationMapper.selectBySampleSubgroup(cpkAnalyseReqDTO);
    }
}
