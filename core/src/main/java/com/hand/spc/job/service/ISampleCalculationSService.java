package com.hand.spc.job.service;

import com.hand.hap.core.ProxySelf;
import com.hand.hap.system.service.IBaseService;
import com.hand.spc.temppkg.dto.Temppkgdto;

public interface ISampleCalculationSService extends IBaseService<Temppkgdto>, ProxySelf<ISampleCalculationSService> {

    /**
     * 样本数据分组计算(计量型)
     *
     * @param tenantId
     * @param siteId
     */
    public void sampleDataSubgroupCalculation(Long tenantId, Long siteId);
}
