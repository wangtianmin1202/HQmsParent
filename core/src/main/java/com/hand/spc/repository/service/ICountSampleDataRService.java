package com.hand.spc.repository.service;

import org.springframework.data.domain.PageRequest;

import com.hand.hap.core.ProxySelf;
import com.hand.hap.system.service.IBaseService;
import com.hand.spc.repository.dto.CountSampleDataR;
import com.hand.spc.repository.dto.CountSampleDataWaitR;
import com.hand.spc.utils.Page;

/**
 * 样本数据(计数)资源库
 *
 * @author peng.hu04@hand-china.com 2019-06-20 11:02:38
 */
public interface ICountSampleDataRService extends IBaseService<CountSampleDataR>, ProxySelf<ICountSampleDataRService> {

    /**
     * 根据预处理样本数据ID集合新增样本数据
     *
     * @param countSampleDataWait
     * @return
     */
    public int insertCountSampleData(CountSampleDataWaitR countSampleDataWait);

    /**
     * 通过查询条件查询数据（分页处理）
     *
     * @param countSampleData
     * @return
     */
    Page<CountSampleDataR> queryCountSampleData(CountSampleDataR countSampleData, PageRequest pageRequest);

}
