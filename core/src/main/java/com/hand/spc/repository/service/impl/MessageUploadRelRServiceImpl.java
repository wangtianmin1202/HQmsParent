package com.hand.spc.repository.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.hand.hap.system.service.impl.BaseServiceImpl;
import com.hand.spc.repository.dto.MessageUploadRelR;
import com.hand.spc.repository.mapper.MessageUploadRelRMapper;
import com.hand.spc.repository.service.IMessageUploadRelRService;

/**
 * 资源库实现
 *
 * @author peng.hu04@hand-china.com 2019-07-07 11:32:28
 */
@Component
public class MessageUploadRelRServiceImpl extends BaseServiceImpl<MessageUploadRelR> implements IMessageUploadRelRService {
    @Autowired
    private MessageUploadRelRMapper messageUploadRelMapper;

    @Override
    public int batchInsertRows(List<MessageUploadRelR> messageUploadRelList) {
        return messageUploadRelMapper.batchInsertRows(messageUploadRelList);
    }
}
