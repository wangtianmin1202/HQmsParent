package com.hand.spc.pspc_sample_data.mapper;

import com.hand.hap.mybatis.common.Mapper;
import com.hand.spc.pspc_sample_data.dto.SampleData;
import com.hand.spc.pspc_sample_data.view.SampleDataQueryVO;

import java.util.List;

public interface SampleDataMapper extends Mapper<SampleData> {

    /**
     * @param sampleDataQueryVO 限制条件
     * @return : java.util.List<com.hand.spc.pspc_sample_data.view.SampleDataQueryVO>
     * @Description: 基本数据查询
     * @author: ywj
     * @date 2019/8/14 17:31
     * @version 1.0
     */
    List<SampleDataQueryVO> queryBaseData(SampleDataQueryVO sampleDataQueryVO);
}