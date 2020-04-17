package com.hand.spc.repository.mapper;

import java.util.List;

import com.hand.hap.mybatis.common.Mapper;
import com.hand.spc.repository.dto.ChartR;
import com.hand.spc.repository.dto.EntityR;

public interface ChartRMapper extends Mapper<ChartR> {
	List<ChartR> selectByChartCode(ChartR chart);

    /**
     * 根据实体控制图获取控制
     *
     * @param entity
     * @return
     */
    ChartR queryChartByEntity(EntityR entity);

    ChartR selectByIndex(ChartR chart);

    ChartR queryChartEntity(EntityR entity);


}
