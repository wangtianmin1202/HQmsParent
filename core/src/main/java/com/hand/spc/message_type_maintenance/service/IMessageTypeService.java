package com.hand.spc.message_type_maintenance.service;

import com.hand.hap.core.IRequest;
import com.hand.hap.core.ProxySelf;
import com.hand.hap.system.dto.ResponseData;
import com.hand.hap.system.service.IBaseService;
import com.hand.spc.message_type_maintenance.dto.MessageType;

import java.util.List;

public interface IMessageTypeService extends IBaseService<MessageType>, ProxySelf<IMessageTypeService>{

    /**
    * @Description 查找显示类型主题
    * @author hch
    * @date 2019/8/12 14:04
    * @Param [requestCtx, dto]
    * @return com.hand.hap.system.dto.ResponseData
    * @version 1.0
    */
    ResponseData queryMessageType(IRequest requestCtx, MessageType dto);

    /**
    * @Description 页面查询
    * @author hch
    * @date 2019/8/27 10:05
    * @Param [iRequest, dto, page, pageSize]
    * @return java.util.List<com.hand.spc.message_type_maintenance.dto.MessageType>
    * @version 1.0
    */
    List<MessageType> mySelect(IRequest iRequest, MessageType dto, int page, int pageSize);

    ResponseData queryElementStatus(IRequest requestCtx, MessageType dto);
}