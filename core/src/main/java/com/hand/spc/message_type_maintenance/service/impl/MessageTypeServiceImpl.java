package com.hand.spc.message_type_maintenance.service.impl;

import com.hand.hap.core.IRequest;
import com.hand.hap.system.dto.ResponseData;
import com.hand.hap.system.service.impl.BaseServiceImpl;
import com.hand.spc.message_type_maintenance.dto.MessageTypeDetail;
import com.hand.spc.message_type_maintenance.mapper.MessageTypeDetailMapper;
import com.hand.spc.message_type_maintenance.mapper.MessageTypeMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.hand.spc.message_type_maintenance.dto.MessageType;
import com.hand.spc.message_type_maintenance.service.IMessageTypeService;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional(rollbackFor = Exception.class)
public class MessageTypeServiceImpl extends BaseServiceImpl<MessageType> implements IMessageTypeService{

    @Autowired
    private MessageTypeDetailMapper messageTypeDetailMapper;
    @Autowired
    private MessageTypeMapper messageTypeMapper;

    /**
    * @Description 查找显示类型主题
    * @author hch
    * @date 2019/8/12 14:05
    * @Param [requestCtx, dto]
    * @return com.hand.hap.system.dto.ResponseData
    * @version 1.0
    */
    @Override
    public ResponseData queryMessageType(IRequest requestCtx, MessageType dto) {
        List<MessageTypeDetail>  messageTypeDetailList = new ArrayList<>();

        //return messageTypeDetailMapper.queryMessageType(dto.getMessageTypeId());
        return null;
    }

    /**
    * @Description 页面查询
    * @author hch
    * @date 2019/8/27 10:05
    * @Param [iRequest, dto, page, pageSize]
    * @return java.util.List<com.hand.spc.message_type_maintenance.dto.MessageType>
    * @version 1.0
    */
    @Override
    public List<MessageType> mySelect(IRequest iRequest, MessageType dto, int page, int pageSize) {
        List<MessageType> list = messageTypeMapper.selectData(dto);
        return list;
    }

    @Override
    public ResponseData queryElementStatus(IRequest requestCtx, MessageType dto) {
        ResponseData responseData = new ResponseData();
        List<MessageType> messageTypeDetailList = messageTypeMapper.queryElementStatus(dto.getMessageTypeId());
        responseData.setRows(messageTypeDetailList);
        return responseData;
    }
}