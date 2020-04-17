package com.hand.spc.pspc_box_plot.mapper;

import com.hand.hap.mybatis.common.Mapper;
import com.hand.spc.pspc_box_plot.view.BoxPlotVO;
import com.hand.spc.pspc_entity.dto.Entity;

import java.util.List;


public interface BoxPlotMapper {

    //箱线图查询 1、带有时间计算的
    List<BoxPlotVO> querySampleValuesByTime(BoxPlotVO boxPlotVO);

    //箱线图查询 2、不带有时间计算的
    List<BoxPlotVO> querySampleValuesByMaxPoints(BoxPlotVO boxPlotVO);

}