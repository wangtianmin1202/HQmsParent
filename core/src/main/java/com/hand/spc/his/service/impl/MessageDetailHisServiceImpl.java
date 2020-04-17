package com.hand.spc.his.service.impl;

import com.hand.hap.system.service.impl.BaseServiceImpl;
import org.springframework.stereotype.Service;
import com.hand.spc.his.dto.MessageDetailHis;
import com.hand.spc.his.service.IMessageDetailHisService;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(rollbackFor = Exception.class)
public class MessageDetailHisServiceImpl extends BaseServiceImpl<MessageDetailHis> implements IMessageDetailHisService{

}