package com.hand.spc.his.service.impl;

import com.hand.hap.system.service.impl.BaseServiceImpl;
import org.springframework.stereotype.Service;
import com.hand.spc.his.dto.MessageHis;
import com.hand.spc.his.service.IMessageHisService;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(rollbackFor = Exception.class)
public class MessageHisServiceImpl extends BaseServiceImpl<MessageHis> implements IMessageHisService{

}