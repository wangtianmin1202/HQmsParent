package com.hand.spc.pspc_chart.mapper;

import com.hand.hap.mybatis.common.Mapper;
import com.hand.spc.pspc_chart.dto.Chart;

import java.util.List;

public interface ChartMapper extends Mapper<Chart> {

    /**
     * @param chart 限制条件
     * @return : java.util.List<com.hand.spc.pspc_chart.dto.Chart>
     * @Description: 基本数据查询
     * @author: ywj
     * @date 2019/8/19 9:53
     * @version 1.0
     */
    List<Chart> queryBaseData(Chart chart);
}