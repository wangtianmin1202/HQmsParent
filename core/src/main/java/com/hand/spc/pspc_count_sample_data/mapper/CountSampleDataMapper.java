package com.hand.spc.pspc_count_sample_data.mapper;

import com.hand.hap.mybatis.common.Mapper;
import com.hand.spc.pspc_count_sample_data.dto.CountSampleData;
import com.hand.spc.pspc_count_sample_data.dto.CountSampleDataVO;

import java.util.List;
import java.util.Map;

public interface CountSampleDataMapper extends Mapper<CountSampleData>{
    /**
     * @Author han.zhang
     * @Description 计数型数据导入 报表数据查询
     * @Date 20:32 2019/8/12
     * @Param [countSampleData]
     */
    List<CountSampleDataVO> selectCountData(CountSampleData countSampleData);
}