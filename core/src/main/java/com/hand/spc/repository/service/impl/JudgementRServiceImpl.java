package com.hand.spc.repository.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hand.hap.system.service.impl.BaseServiceImpl;
import com.hand.spc.repository.dto.JudgementR;
import com.hand.spc.repository.mapper.JudgementRMapper;
import com.hand.spc.repository.service.IJudgementRService;

@Service
@Transactional(rollbackFor = Exception.class)
public class JudgementRServiceImpl extends BaseServiceImpl<JudgementR> implements IJudgementRService {

    @Autowired
    private JudgementRMapper judgementMapper;

    @Override
    public List<JudgementR> listJudegement(Long tenantId, Long siteId, Long chartId) {
        return judgementMapper.listJudegement(tenantId, siteId, chartId);
    }

    @Override
    public List<JudgementR> listCountJudegement(Long tenantId, Long siteId, Long chartId) {
        return judgementMapper.listCountJudegement(tenantId, siteId, chartId);
    }
}
