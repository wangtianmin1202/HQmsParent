package com.hand.spc.job.service;

import java.util.List;
import java.util.Map;

import com.hand.hap.core.ProxySelf;
import com.hand.hap.system.service.IBaseService;
import com.hand.spc.repository.dto.CountCalculationVO;
import com.hand.spc.repository.dto.CountSampleDataWaitVO;
import com.hand.spc.repository.dto.EntityR;
import com.hand.spc.temppkg.dto.Temppkgdto;

/**
 * 样本数据(计数)运算
 */
public interface ISampleCountCalculationRunSService extends IBaseService<Temppkgdto>, ProxySelf<ISampleCountCalculationRunSService> {

	/**
     * 样本数据(计数)预处理
     *
     * @param countSampleDataWaitVO
     */
    void execCountSampleDataWait(CountSampleDataWaitVO countSampleDataWaitVO, String uuid);

    /**
     * 计数型统计量
     *
     * @param entity
     * @param uuid
     */
    CountCalculationVO execCountStatistic(EntityR entity, String uuid);

    Map<EntityR, CountCalculationVO> batchExecCountStatistic(List<EntityR> entityList, String uuId);
}
