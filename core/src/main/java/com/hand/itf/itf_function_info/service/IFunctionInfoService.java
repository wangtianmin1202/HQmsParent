package com.hand.itf.itf_function_info.service;

import com.hand.hap.core.IRequest;
import com.hand.hap.core.ProxySelf;
import com.hand.hap.system.service.IBaseService;
import com.hand.itf.itf_function_info.dto.FunctionInfo;

import java.util.List;

public interface IFunctionInfoService extends IBaseService<FunctionInfo>, ProxySelf<IFunctionInfoService>{

    /**
     * 自定义查询
     * @param request
     * @param individuationSql
     * @return
     */
    public List<FunctionInfo> individuationQuery(IRequest request,int page,int pageSize,String individuationSql);

}