package com.hand.spc.repository.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hand.hap.system.service.impl.BaseServiceImpl;
import com.hand.spc.repository.dto.MessageDetailR;
import com.hand.spc.repository.mapper.MessageDetailRMapper;
import com.hand.spc.repository.service.IMessageDetailRService;

@Service
@Transactional(rollbackFor = Exception.class)
public class MessageDetailRServiceImpl extends BaseServiceImpl<MessageDetailR> implements IMessageDetailRService {

    @Autowired
    private MessageDetailRMapper messageDetailMapper;

    @Override
    public int batchInsertRows(List<MessageDetailR> messageDetailList) {
        return messageDetailMapper.batchInsertRows(messageDetailList);
    }
}
