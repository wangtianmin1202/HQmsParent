package com.hand.spc.repository.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hand.hap.system.service.impl.BaseServiceImpl;
import com.hand.spc.repository.dto.EntityR;
import com.hand.spc.repository.dto.SampleDataWaitR;
import com.hand.spc.repository.dto.SampleSubgroupWaitR;
import com.hand.spc.repository.mapper.SampleSubgroupWaitRMapper;
import com.hand.spc.repository.service.ISampleSubgroupWaitRService;

@Service
@Transactional(rollbackFor = Exception.class)
public class SampleSubgroupWaitRServiceImpl extends BaseServiceImpl<SampleSubgroupWaitR> implements ISampleSubgroupWaitRService {

    @Autowired
    private SampleSubgroupWaitRMapper subgroupWaitMapper;

    @Override
    public int insertOldSubgroupWaitData(EntityR entity) {
        return subgroupWaitMapper.insertOldSubgroupWaitData(entity);
    }

    @Override
    public int insertSubgroupWaitData(SampleDataWaitR sampleDataWait) {
        return subgroupWaitMapper.insertSubgroupWaitData(sampleDataWait);
    }

    @Override
    public int deleteSubgroupWaitDataByIdList(SampleSubgroupWaitR sampleSubgroupWait) {
        return subgroupWaitMapper.deleteSubgroupWaitDataByIdList(sampleSubgroupWait);
    }
    
    @Override
    public List<SampleSubgroupWaitR> selectSubgroupWaitData(SampleDataWaitR sampleDataWait) { //modified 20190903 
        return subgroupWaitMapper.selectSubgroupWaitData(sampleDataWait);
    }
    
    @Override
    public List<SampleSubgroupWaitR> selectSubgroupWaitDataModified(String whereInSql) {
        return subgroupWaitMapper.selectSubgroupWaitDataModified(whereInSql);
    }
    
    @Override
    public int batchInsertSampleSubgroupWait(List<SampleSubgroupWaitR> sampleSubgroupWaitList) {
        return subgroupWaitMapper.batchInsertSampleSubgroupWait(sampleSubgroupWaitList);
    }
}
