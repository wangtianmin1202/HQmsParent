package com.hand.spc.repository.service;

import java.util.List;

import com.hand.hap.core.ProxySelf;
import com.hand.hap.system.service.IBaseService;
import com.hand.spc.repository.dto.ErrorMessageR;

/**
 * 资源库
 *
 * @author linjie.shi@hand-china.com 2019-08-12 10:13:01
 */
public interface IErrorMessageRService extends IBaseService<ErrorMessageR>, ProxySelf<ErrorMessageR>{

    String getErrorMessageWithModule(Long tenantId, String code, String module, String... args);

    List<String> messageLimitMessageCodeQuery(Long tenantId, String module, String message);

    String messageCodeLimitMessageGet(Long tenantId, String module, String messageCode);
}
