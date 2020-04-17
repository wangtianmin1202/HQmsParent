package com.hand.spc.pspc_message_detail.service.impl;

import com.hand.hap.system.service.impl.BaseServiceImpl;
import com.hand.spc.pspc_message_detail.mapper.MessageDetailMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.hand.spc.pspc_message_detail.dto.MessageDetail;
import com.hand.spc.pspc_message_detail.service.IMessageDetailService;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(rollbackFor = Exception.class)
public class MessageDetailServiceImpl extends BaseServiceImpl<MessageDetail> implements IMessageDetailService{

    @Autowired
    private MessageDetailMapper messageDetailMapper;

    /**
     * @param messageDetail 传入参数
     * @return : void
     * @Description: 数据删除 by detailId
     * @author: ywj
     * @date 2019/8/20 20:33
     * @version 1.0
     */
    @Override
    public void deleteByPrimaryId(MessageDetail messageDetail) {

        messageDetailMapper.deleteByPrimaryId(messageDetail);
    }
}