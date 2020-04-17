package com.hand.spc.repository.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hand.hap.system.service.impl.BaseServiceImpl;
import com.hand.spc.repository.dto.EntiretyStatisticR;
import com.hand.spc.repository.dto.SampleSubgroupR;
import com.hand.spc.repository.mapper.EntiretyStatisticRMapper;
import com.hand.spc.repository.service.IEntiretyStatisticRService;

@Service
@Transactional(rollbackFor = Exception.class)
public class EntiretyStatisticRServiceImpl extends BaseServiceImpl<EntiretyStatisticR> implements IEntiretyStatisticRService {

    @Autowired
    private EntiretyStatisticRMapper entiretyStatisticMapper;

    @Override
    public int batchInsertRows(List<EntiretyStatisticR> entiretyStatisticList) {
        return entiretyStatisticMapper.batchInsertRows(entiretyStatisticList);
    }

    @Override
    public EntiretyStatisticR selectByMaxNum(SampleSubgroupR sampleSubgroup) {
        EntiretyStatisticR entiretyStatistic =entiretyStatisticMapper.selectByMaxNum(sampleSubgroup);

        return entiretyStatistic;
    }
}
