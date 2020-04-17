package com.hand.spc.repository.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hand.hap.system.service.impl.BaseServiceImpl;
import com.hand.spc.repository.dto.MessageR;
import com.hand.spc.repository.mapper.MessageRMapper;
import com.hand.spc.repository.service.IMessageRService;

@Service
@Transactional(rollbackFor = Exception.class)
public class MessageRServiceImpl extends BaseServiceImpl<MessageR> implements IMessageRService {

    @Autowired
    private MessageRMapper messageMapper;

    @Override
    public int batchInsertRows(List<MessageR> messageList) {
        return messageMapper.batchInsertRows(messageList);
    }
}
