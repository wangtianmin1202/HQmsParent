package com.hand.spc.repository.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.hand.hap.system.service.impl.BaseServiceImpl;
import com.hand.spc.repository.dto.CountSampleDataWaitVO;
import com.hand.spc.repository.dto.EntityByChartVO;
import com.hand.spc.repository.dto.EntityR;
import com.hand.spc.repository.dto.SampleGroupDataVO;
import com.hand.spc.repository.mapper.EntityRMapper;
import com.hand.spc.repository.service.IEntityRService;
import com.hand.spc.utils.Page;
@Transactional(rollbackFor = Exception.class)
public class EntityRServiceImpl extends BaseServiceImpl<EntityR> implements IEntityRService {

    @Autowired
    private EntityRMapper entityMapper;

    @Override
    public List<EntityR> listSubgroupWaitData(SampleGroupDataVO sampleGroupDataVO) {
        return entityMapper.listSubgroupWaitData(sampleGroupDataVO);
    }

    @Override
    public List<EntityR> listCountSampleDataWait(CountSampleDataWaitVO countSampleDataWaitVO) {
        return entityMapper.listCountSampleDataWait(countSampleDataWaitVO);
    }

    @Override
    public Page<EntityR> queryEntity(EntityR entity) {
        return entityMapper.queryEntity(entity);
    }

    @Override
    public List<EntityByChartVO> queryEntityByChart(EntityR entity) {
        return entityMapper.queryEntityByChart(entity);
    }
}
