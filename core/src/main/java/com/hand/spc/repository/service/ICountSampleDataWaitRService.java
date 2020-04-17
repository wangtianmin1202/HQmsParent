package com.hand.spc.repository.service;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.hand.hap.core.ProxySelf;
import com.hand.hap.system.service.IBaseService;
import com.hand.spc.repository.dto.CountSampleDataWaitR;
import com.hand.spc.repository.dto.CountSampleDataWaitVO;

/**
 * 样本数据(计数)预处理资源库
 *
 * @author peng.hu04@hand-china.com 2019-06-20 11:02:18
 */
public interface ICountSampleDataWaitRService extends IBaseService<CountSampleDataWaitR>, ProxySelf<ICountSampleDataWaitRService> {
    /**
     * 抽样 样本数据(计数)预处理 数据
     *
     * @param countSampleDataWaitVO
     * @return
     */
    public List<CountSampleDataWaitR> listExtractCountSampleDataWait(CountSampleDataWaitVO countSampleDataWaitVO);
    public List<CountSampleDataWaitR> listExtractCountSampleDataWaitModified(@Param("whereInSql") String whereInSql);


    /**
     * 获取 样本数据(计数)预处理 数据
     *
     * @param tenantId
     * @param siteId
     * @return
     */
    public List<CountSampleDataWaitVO> listCountSampleDataWait(@Param("tenantId") Long tenantId, @Param("siteId") Long siteId);

    /**
     * 根据 样本数据(计数)预处理数据ID 删除数据
     *
     * @param countSampleDataWait
     * @return
     */
    public int deleteCountSampleDataWait(CountSampleDataWaitR countSampleDataWait);
    
    int batchInsertCountSampleDataWait(List<CountSampleDataWaitR> countSampleDataWaits);

    List<Long> selectIsCountData(CountSampleDataWaitVO countSampleDataWaitVO);
    
    List<Long> selectIsCountDataModified(@Param("whereInSql") String whereInSql);
}
