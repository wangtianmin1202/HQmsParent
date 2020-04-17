package com.hand.spc.repository.service;

import java.util.List;

import com.hand.hap.core.ProxySelf;
import com.hand.hap.system.service.IBaseService;
import com.hand.spc.repository.dto.SampleDataR;
import com.hand.spc.repository.dto.SampleDataWaitR;

public interface ISampleDataRService extends IBaseService<SampleDataR>, ProxySelf<ISampleDataRService> {

    /**
     * 根据预处理样本数据ID集合新增样本数据
     *
     * @param sampleDataWait
     * @return
     */
    public int insertSampleData(SampleDataWaitR sampleDataWait);
    
    List<SampleDataWaitR> selectIds(List<Long> ids);

    int batchInsertSampleData(List<SampleDataR> sampleDataWaitList);

}
