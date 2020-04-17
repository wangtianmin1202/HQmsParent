package com.hand.spc.repository.mapper;

import java.util.List;

import com.hand.hap.mybatis.common.Mapper;
import com.hand.spc.repository.dto.CountSampleDataR;
import com.hand.spc.repository.dto.CountSampleDataWaitR;

/**
 * 样本数据(计数)Mapper
 *
 * @author peng.hu04@hand-china.com 2019-06-20 11:02:38
 */
public interface CountSampleDataRMapper extends Mapper<CountSampleDataR> {

    /**
     * 根据预处理样本数据ID集合新增样本数据
     *
     * @param countSampleDataWait
     * @return
     */
    public int insertCountSampleData(CountSampleDataWaitR countSampleDataWait);

    /**
     * 通过查询条件查找样本数据
     * @param countSampleData
     * @return
     */
    List<CountSampleDataR> queryCountSampleData(CountSampleDataR countSampleData);

}
