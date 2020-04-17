package com.hand.spc.pspc_count_sample_data_wait.service;

import com.hand.hap.core.IRequest;
import com.hand.hap.core.ProxySelf;
import com.hand.hap.system.dto.ResponseData;
import com.hand.hap.system.service.IBaseService;
import com.hand.spc.pspc_count_sample_data_wait.dto.CountSampleDataWait;

import javax.servlet.http.HttpServletRequest;

public interface ICountSampleDataWaitService extends IBaseService<CountSampleDataWait>, ProxySelf<ICountSampleDataWaitService>{

    ResponseData importExcel(IRequest requestContext, HttpServletRequest request,
                             Long groupId, Long parameterId, Long ceGroupId, Long classifyExtendCount, Long attributeExtendCount) throws NoSuchFieldException, IllegalAccessException;
}