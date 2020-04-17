package com.hand.spc.pspc_count_sample_data_extend.service;

import com.hand.hap.core.IRequest;
import com.hand.hap.core.ProxySelf;
import com.hand.hap.system.service.IBaseService;
import com.hand.spc.pspc_count_sample_data.dto.CountSampleData;
import com.hand.spc.pspc_count_sample_data_extend.dto.CountSampleDataExtend;

import java.util.List;

public interface ICountSampleDataExtendService extends IBaseService<CountSampleDataExtend>, ProxySelf<ICountSampleDataExtendService>{
    /**
     * @Author han.zhang
     * @Description 根据样本ID找对应的扩展属性
     * @Date 13:55 2019/8/14
     * @Param [requestContext, dto]
     */
    List<CountSampleDataExtend> queryExtendColumns(IRequest requestContext, CountSampleData dto);
}