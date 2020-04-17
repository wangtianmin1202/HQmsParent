package com.hand.spc.message_type_maintenance.service.impl;

import com.hand.hap.core.IRequest;
import com.hand.hap.system.dto.ResponseData;
import com.hand.hap.system.service.impl.BaseServiceImpl;
import com.hand.spc.message_type_maintenance.dto.MessageType;
import com.hand.spc.message_type_maintenance.mapper.MessageTypeDetailMapper;
import com.hand.spc.message_type_maintenance.mapper.MessageTypeMapper;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.hand.spc.message_type_maintenance.dto.MessageTypeDetail;
import com.hand.spc.message_type_maintenance.service.IMessageTypeDetailService;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(rollbackFor = Exception.class)
public class MessageTypeDetailServiceImpl extends BaseServiceImpl<MessageTypeDetail> implements IMessageTypeDetailService{

    @Autowired
    private MessageTypeDetailMapper messageTypeDetailMapper;
    @Autowired
    private MessageTypeMapper messageTypeMapper;

    @Override
    public ResponseData queryMessageType(IRequest requestCtx, MessageTypeDetail dto) {
        ResponseData responseData = new ResponseData();
        List<MessageTypeDetail> messageTypeDetailList = messageTypeDetailMapper.queryMessageType(dto.getMessageTypeId());
        List<String> stringList = messageTypeDetailList.stream().map(MessageTypeDetail::getElementCode).collect(Collectors.toList());
        //String join = StringUtils.join(stringList, ",");
        responseData.setRows(stringList);
        return responseData;
    }

    @Override
    public ResponseData queryMessageTypeContent(IRequest requestCtx, MessageTypeDetail dto) {
        ResponseData responseData = new ResponseData();
        List<MessageTypeDetail> messageTypeDetailList = messageTypeDetailMapper.queryMessageTypeContent(dto.getMessageTypeId());
        List<String> stringList = messageTypeDetailList.stream().map(MessageTypeDetail::getElementCode).collect(Collectors.toList());
        //String join = StringUtils.join(stringList, ",");
        responseData.setRows(stringList);
        return responseData;
    }


//    @Override
//    public ResponseData queryElementStatus(IRequest requestCtx, MessageTypeDetail dto) {
//        ResponseData responseData = new ResponseData();
//        List<MessageTypeDetail> messageTypeDetailList = messageTypeDetailMapper.queryElementStatus(dto.getMessageTypeId());
//        responseData.setRows(messageTypeDetailList);
//        return responseData;
//    }

    /**
    * @Description 头保存方法
    * @author hch
    * @date 2019/8/12 18:48
    * @Param [requestCtx, dto]
    * @return com.hand.hap.system.dto.ResponseData
    * @version 1.0
    */
    @Override
    public ResponseData submit(IRequest requestCtx, List<MessageTypeDetail> dto) {
        ResponseData responseData = new ResponseData();
        //因为前台有删除的逻辑，走更新逻辑不合适，所以直接走删除后保存当前界面输入的值（明细表的id跟其他表没有关联，可以删除）
        //根据传进来的头ID删除所有数据
        messageTypeDetailMapper.deleteAll(dto.get(0).getMessageTypeId());

        //改变头表有效性的值
        MessageType messageType = new MessageType();
        messageType.setMessageTypeId(dto.get(0).getMessageTypeId());
        messageType.setMessageTypeStatus(dto.get(0).getElementStatus());
        messageTypeMapper.updateByPrimaryKeySelective(messageType);

        for (int i = 0; i < dto.size(); i++) {
            //新增表数据
//            MessageTypeDetail messageTypeDetail = new MessageTypeDetail();
//            messageTypeDetail.setMessageTypeId(dto.get(i).getMessageTypeId());
//            messageTypeDetail.setElementCategory(dto.get(i).getElementCategory());
//            messageTypeDetail.setElementCode(dto.get(i).getElementCode());

            messageTypeDetailMapper.insertSelective(dto.get(i));
        }
        responseData.setMessage("消息类型保存成功！");
        responseData.setSuccess(true);
        return responseData;
    }
}