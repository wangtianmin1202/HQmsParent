package com.hand.spc.repository.service;

import com.hand.hap.core.ProxySelf;
import com.hand.hap.system.service.IBaseService;
import com.hand.spc.repository.dto.ChartR;
import com.hand.spc.repository.dto.EntityR;

public interface IChartRService extends IBaseService<ChartR>, ProxySelf<IChartRService> {
    ChartR selectByChartCode(ChartR chart);

    /**
     * 根据实体控制图获取控制
     *
     * @param entity
     * @return
     */
    public ChartR queryChartByEntity(EntityR entity);
}
