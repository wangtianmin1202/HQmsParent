package com.hand.spc.message_type_maintenance.service;

import com.hand.hap.core.IRequest;
import com.hand.hap.core.ProxySelf;
import com.hand.hap.system.dto.ResponseData;
import com.hand.hap.system.service.IBaseService;
import com.hand.spc.message_type_maintenance.dto.MessageTypeDetail;

import java.util.List;

public interface IMessageTypeDetailService extends IBaseService<MessageTypeDetail>, ProxySelf<IMessageTypeDetailService>{

    ResponseData queryMessageType(IRequest requestCtx, MessageTypeDetail dto);

    ResponseData queryMessageTypeContent(IRequest requestCtx, MessageTypeDetail dto);

    /**
    * @Description 头保存方法
    * @author hch
    * @date 2019/8/12 18:47
    * @Param [requestCtx, dto]
    * @return com.hand.hap.system.dto.ResponseData
    * @version 1.0
    */
    ResponseData submit(IRequest requestCtx, List<MessageTypeDetail> dto);

    //查有效性
//    ResponseData queryElementStatus(IRequest requestCtx, MessageTypeDetail dto);
}