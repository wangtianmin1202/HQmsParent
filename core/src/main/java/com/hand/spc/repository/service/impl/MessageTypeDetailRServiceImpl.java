package com.hand.spc.repository.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hand.hap.system.service.impl.BaseServiceImpl;
import com.hand.spc.repository.dto.MessageTypeDetailR;
import com.hand.spc.repository.service.IMessageTypeDetailRService;

@Service
@Transactional(rollbackFor = Exception.class)
public class MessageTypeDetailRServiceImpl extends BaseServiceImpl<MessageTypeDetailR> implements IMessageTypeDetailRService{

}