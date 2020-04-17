package com.hand.spc.repository.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hand.hap.system.service.impl.BaseServiceImpl;
import com.hand.spc.repository.dto.Sequence;
import com.hand.spc.repository.mapper.SequenceMapper;
import com.hand.spc.repository.service.ISequenceRService;

@Service
@Transactional(rollbackFor = Exception.class)
public class SequenceRServiceImpl extends BaseServiceImpl<Sequence> implements ISequenceRService {

    @Autowired
    private SequenceMapper sequenceMapper;

    @Override
    public Long getNextValue(Long tenantId, Long siteId, String sequenceCode) {

        Sequence sequence = new Sequence();
        sequence.setTenantId(tenantId);
        sequence.setSiteId(siteId);
        sequence.setSequenceCode(sequenceCode);

        List<Sequence> sequenceList = sequenceMapper.select(sequence);
        if (sequenceList.size() == 1) {
            //序列编码存在
            sequence = sequenceList.get(0);
            sequence.setMaxNum(sequence.getMaxNum()+1);
            sequenceMapper.updateByPrimaryKey(sequence);
        } else {
            //序列不存在
            sequence.setMaxNum(1L);
            sequenceMapper.insert(sequence);
        }

        return sequence.getMaxNum();
    }
}
