package com.hand.spc.pspc_message.service.impl;

import com.hand.hap.system.service.impl.BaseServiceImpl;
import com.hand.spc.pspc_message.dto.MessageL;
import org.springframework.stereotype.Service;
import com.hand.spc.pspc_message.service.IMessageServiceL;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(rollbackFor = Exception.class)
public class MessageServiceLImpl extends BaseServiceImpl<MessageL> implements IMessageServiceL {

}