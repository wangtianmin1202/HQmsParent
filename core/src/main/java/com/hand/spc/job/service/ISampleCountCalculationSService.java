package com.hand.spc.job.service;

import com.hand.hap.core.ProxySelf;
import com.hand.hap.system.service.IBaseService;
import com.hand.spc.temppkg.dto.Temppkgdto;

public interface ISampleCountCalculationSService extends IBaseService<Temppkgdto>, ProxySelf<ISampleCountCalculationSService> {
    /**
     * 样本数据(计数)计算
     *
     * @param tenantId
     * @param siteId
     */
    public void sampleCountCalculation(Long tenantId, Long siteId);
}
