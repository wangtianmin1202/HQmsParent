package com.hand.spc.message_type_maintenance.service.impl;

import com.hand.hap.core.IRequest;
import com.hand.hap.system.dto.ResponseData;
import com.hand.hap.system.service.impl.BaseServiceImpl;
import com.hand.spc.message_type_maintenance.mapper.MessageTypeUploadRelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.hand.spc.message_type_maintenance.dto.MessageTypeUploadRel;
import com.hand.spc.message_type_maintenance.service.IMessageTypeUploadRelService;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(rollbackFor = Exception.class)
public class MessageTypeUploadRelServiceImpl extends BaseServiceImpl<MessageTypeUploadRel> implements IMessageTypeUploadRelService{

    @Autowired
    private MessageTypeUploadRelMapper messageTypeUploadRelMapper;

    @Override
    public List<MessageTypeUploadRel> selectData(IRequest requestContext, MessageTypeUploadRel dto, int page, int pageSize) {
        return messageTypeUploadRelMapper.selectData(dto.getMessageTypeId());
    }

    /**
    * @Description 校验唯一性
    * @author hch
    * @date 2019/8/27 9:44
    * @Param [requestCtx, dto]
    * @return com.hand.hap.system.dto.ResponseData
    * @version 1.0
    */
    @Override
    public ResponseData validateUnique(IRequest requestCtx, List<MessageTypeUploadRel> dto) {
        ResponseData responseData = new ResponseData();
        responseData.setSuccess(true);
        for (MessageTypeUploadRel messageTypeUploadRel : dto) {
            //新增和修改的时候校验数据唯一性
            if("add".equals(messageTypeUploadRel.get__status())||"update".equals(messageTypeUploadRel.get__status())){
                MessageTypeUploadRel relDto = new MessageTypeUploadRel();
                relDto.setUploadConfigId(messageTypeUploadRel.getUploadConfigId());
                relDto.setMessageTypeId(messageTypeUploadRel.getMessageTypeId());
                List<MessageTypeUploadRel> list = this.messageTypeUploadRelMapper.select(relDto); //调用Mapper的select（）方法查出现有的数据给list，然后下面就可以拿这个list判断如果现有的数据存在，则报重复
                if (list != null && list.size() > 0) {
                    responseData.setSuccess(false);
                    responseData.setMessage("数据重复");
                    return responseData;
                }
            }
        }
        return responseData;
    }
}