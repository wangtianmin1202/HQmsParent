package com.hand.spc.pspc_sample_data.service;

import com.hand.hap.core.IRequest;
import com.hand.hap.core.ProxySelf;
import com.hand.hap.system.service.IBaseService;
import com.hand.spc.pspc_sample_data.dto.SampleDataExtend;

import java.util.List;

public interface ISampleDataExtendService extends IBaseService<SampleDataExtend>, ProxySelf<ISampleDataExtendService> {

    /**
     * @param iRequest         基本参数
     * @param sampleDataExtend 限制条件
     * @param page             页数
     * @param pageSize         页数大小
     * @return : java.util.List<com.hand.spc.pspc_sample_data.dto.SampleDataExtend>
     * @Description: 基础数据查询
     * @author: ywj
     * @date 2019/8/14 17:50
     * @version 1.0
     */
    List<SampleDataExtend> queryBaseData(IRequest iRequest, SampleDataExtend sampleDataExtend, int page, int pageSize);
}