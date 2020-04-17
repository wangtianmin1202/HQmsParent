package com.hand.spc.repository.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hand.hap.system.service.impl.BaseServiceImpl;
import com.hand.spc.repository.dto.SampleDataR;
import com.hand.spc.repository.dto.SampleDataWaitR;
import com.hand.spc.repository.mapper.SampleDataRMapper;
import com.hand.spc.repository.service.ISampleDataRService;

@Service
@Transactional(rollbackFor = Exception.class)
public class SampleDataRepositoryImpl extends BaseServiceImpl<SampleDataR> implements ISampleDataRService {

    @Autowired
    private SampleDataRMapper sampleDataMapper;

    @Override
    public int insertSampleData(SampleDataWaitR sampleDataWait) {
        return sampleDataMapper.insertSampleData(sampleDataWait);
    }
    
    @Override
    public List<SampleDataWaitR> selectIds(List<Long> ids) {
        return sampleDataMapper.selectByKeys(ids);
    }

    @Override
    public int batchInsertSampleData(List<SampleDataR> sampleDataWaitList) {
        return sampleDataMapper.batchInsertSampleData(sampleDataWaitList);
    }
}
