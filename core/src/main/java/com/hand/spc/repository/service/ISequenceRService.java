package com.hand.spc.repository.service;

import com.hand.hap.core.ProxySelf;
import com.hand.hap.system.service.IBaseService;
import com.hand.spc.repository.dto.Sequence;

public interface ISequenceRService extends IBaseService<Sequence>, ProxySelf<ISequenceRService> {

    /**
     * 获取序号
     */
    public Long getNextValue(Long tenantId,Long siteId,String sequenceCode);
}
