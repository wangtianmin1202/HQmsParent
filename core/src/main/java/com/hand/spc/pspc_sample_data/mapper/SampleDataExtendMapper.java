package com.hand.spc.pspc_sample_data.mapper;

import com.hand.hap.mybatis.common.Mapper;
import com.hand.spc.pspc_sample_data.dto.SampleDataExtend;

import java.util.List;

public interface SampleDataExtendMapper extends Mapper<SampleDataExtend> {

    /**
     * @param sampleDataExtend 限制条件
     * @return : java.util.List<com.hand.spc.pspc_sample_data.dto.SampleDataExtend>
     * @Description:数据查询
     * @author: ywj
     * @date 2019/8/14 17:50
     * @version 1.0
     */
    List<SampleDataExtend> queryBaseData(SampleDataExtend sampleDataExtend);
}