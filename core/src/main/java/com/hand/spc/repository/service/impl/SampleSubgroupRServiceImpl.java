package com.hand.spc.repository.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hand.hap.system.service.impl.BaseServiceImpl;
import com.hand.spc.repository.dto.CPKAnalyseReqDTO;
import com.hand.spc.repository.dto.SampleSubgroupR;
import com.hand.spc.repository.dto.SePointDataDTO;
import com.hand.spc.repository.dto.SeRequestDTO;
import com.hand.spc.repository.mapper.SampleSubgroupRMapper;
import com.hand.spc.repository.service.ISampleSubgroupRService;

@Service
@Transactional(rollbackFor = Exception.class)
public class SampleSubgroupRServiceImpl extends BaseServiceImpl<SampleSubgroupR> implements ISampleSubgroupRService {

    @Autowired
    private SampleSubgroupRMapper sampleSubgroupMapper;

    @Override
    public List<SampleSubgroupR> queryPreSubgroupStatistic(Long tenantId, Long siteId, String entityCode, String entityVersion, Long maxSubgroupNum, Long minSubgroupNum, String chartType) {
        return sampleSubgroupMapper.queryPreSubgroupStatistic(tenantId, siteId, entityCode, entityVersion, maxSubgroupNum, minSubgroupNum, chartType);
    }

    @Override
    public Long queryMaxSubgroupNum(SeRequestDTO requestDTO) {
        return sampleSubgroupMapper.queryMaxSubgroupNum(requestDTO);
    }

    @Override
    public List<SePointDataDTO> listSePointData(SeRequestDTO requestDTO) {
        return sampleSubgroupMapper.listSePointData(requestDTO);
    }

    @Override
    public int batchInsertRows(List<SampleSubgroupR> sampleSubgroupList) {
        return sampleSubgroupMapper.batchInsertRows(sampleSubgroupList);
    }

    @Override
    public List<SampleSubgroupR> querySampleSubgroupByCPK(CPKAnalyseReqDTO requestDTO) {
        return sampleSubgroupMapper.querySampleSubgroupByCPK(requestDTO);
    }
}
