package com.hand.spc.pspc_count_statistic.mapper;

import com.hand.hap.mybatis.common.Mapper;
import com.hand.spc.pspc_count_statistic.dto.CountStatistic;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CountStatisticMapper extends Mapper<CountStatistic>{

    //柏拉图展示查询 主要数据查询
    List<CountStatistic> queryReport(CountStatistic dto);

    //根据count_sample_data_id获取status
    List<String> queryOccStatus( @Param("countSampleDataId") String countSampleDataId);

    //获取快速编码的信息
    List<CountStatistic> queryFastCode(CountStatistic countStatistic);

    //时间为null时，查询最大的limit
    String queryLimit(CountStatistic dto);

    //根据 entity_id 找到对应的  控制图类型
    String queryCharType(CountStatistic dto);

}