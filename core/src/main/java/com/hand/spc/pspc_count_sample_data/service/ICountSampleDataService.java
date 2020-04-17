package com.hand.spc.pspc_count_sample_data.service;

import com.hand.hap.core.IRequest;
import com.hand.hap.core.ProxySelf;
import com.hand.hap.system.dto.ResponseData;
import com.hand.hap.system.service.IBaseService;
import com.hand.spc.pspc_count_sample_data.dto.CountSampleData;

import java.util.List;
import java.util.Map;

public interface ICountSampleDataService extends IBaseService<CountSampleData>, ProxySelf<ICountSampleDataService>{
    /**
     * @Author han.zhang
     * @Description 计数型数据导入查询
     * @Date 20:56 2019/8/12
     * @Param [requestContext, dto, page, pageSize]
     */
    List<Map<String, String>> queryCountData(IRequest requestContext, CountSampleData dto, int page, int pageSize);
    /**
     * @Author han.zhang
     * @Description 计数数据维护
     * @Date 22:31 2019/8/14
     * @Param [requestCtx, dtos]
     */
    ResponseData saveCountData(IRequest requestCtx, List<Map<String,String>> dtos);
    /**
     * @Author han.zhang
     * @Description 计数型数据删除
     * @Date 23:51 2019/8/15
     * @Param [dto]
     */
    ResponseData deleteCountSamleDate(List<CountSampleData> dto);
}