package com.hand.spc.repository.service;

import java.util.List;

import com.hand.hap.core.ProxySelf;
import com.hand.hap.system.service.IBaseService;
import com.hand.spc.repository.dto.JudgementR;

public interface IJudgementRService extends IBaseService<JudgementR>, ProxySelf<IJudgementRService> {

    /**
     * 查询判异规则
     *
     * @param tenantId
     * @param siteId
     * @param chartId
     * @return
     */
    public List<JudgementR> listJudegement(Long tenantId, Long siteId, Long chartId);

    /**
     * 查询判异规则(计数)
     *
     * @param tenantId
     * @param siteId
     * @param chartId
     * @return
     */
    public List<JudgementR> listCountJudegement(Long tenantId, Long siteId, Long chartId);
}
