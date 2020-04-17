package com.hand.spc.message_type_maintenance.service;

import com.hand.hap.core.IRequest;
import com.hand.hap.core.ProxySelf;
import com.hand.hap.system.dto.ResponseData;
import com.hand.hap.system.service.IBaseService;
import com.hand.spc.message_type_maintenance.dto.MessageTypeUploadRel;

import java.util.List;

public interface IMessageTypeUploadRelService extends IBaseService<MessageTypeUploadRel>, ProxySelf<IMessageTypeUploadRelService>{

    List<MessageTypeUploadRel> selectData(IRequest requestContext, MessageTypeUploadRel dto, int page, int pageSize);

    /**
    * @Description 校验唯一性
    * @author hch
    * @date 2019/8/27 9:44
    * @Param [requestCtx, dto]
    * @return com.hand.hap.system.dto.ResponseData
    * @version 1.0
    */
    ResponseData validateUnique(IRequest requestCtx, List<MessageTypeUploadRel> dto);
}