package com.hand.spc.message_config_mainten.service;

import com.hand.hap.core.IRequest;
import com.hand.hap.core.ProxySelf;
import com.hand.hap.system.dto.ResponseData;
import com.hand.hap.system.service.IBaseService;
import com.hand.spc.message_config_mainten.dto.MessageUploadConfig;

import java.util.List;

public interface IMessageUploadConfigService extends IBaseService<MessageUploadConfig>, ProxySelf<IMessageUploadConfigService>{
    List<MessageUploadConfig> queryData(IRequest request,MessageUploadConfig dto,int page,int pageSize);
    ResponseData update(IRequest request,MessageUploadConfig dto);

}