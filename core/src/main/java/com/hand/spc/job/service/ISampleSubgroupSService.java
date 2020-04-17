package com.hand.spc.job.service;

import java.util.List;
import java.util.Map;

import com.hand.hap.core.ProxySelf;
import com.hand.hap.system.service.IBaseService;
import com.hand.spc.repository.dto.EntityR;
import com.hand.spc.repository.dto.SampleGroupDataVO;
import com.hand.spc.repository.dto.SubGroupCalcResultVO;

/**
 * 样本数据分组
 */
public interface ISampleSubgroupSService extends IBaseService<EntityR>,ProxySelf<ISampleSubgroupSService>{

	/**
     * 样本数据预分组
     *
     * @param sampleGroupDataVO
     */
    void sampleDataSubgroupWait(SampleGroupDataVO sampleGroupDataVO, String uuid);

    /**
     * 样本数据分组
     *
     * @param entity
     * @param uuid
     */
    SubGroupCalcResultVO sampleDataSubgroup(EntityR entity, String uuid);

    /**
     * 
     * @param entityList
     * @param uuId
     */
    Map<EntityR, SubGroupCalcResultVO>  sampleDataSubgroupList(List<EntityR> entityList, String uuId);
}
