package com.hand.spc.pspc_entity.mapper;

import com.hand.hap.mybatis.common.Mapper;
import com.hand.spc.pspc_entity.dto.ChartShowVO;
import com.hand.spc.pspc_entity.dto.Entity;
import com.hand.spc.pspc_entity.view.ScatterPlotVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface EntityMapper extends Mapper<Entity>{

    /**
     *
     * @Description 根据传入参数判断是否有相关的实体引用
     *
     * @author yuchao.wang
     * @date 2019/8/11 13:33
     * @param dto
     * @return java.util.List<com.hand.spc.pspc_entity.dto.Entity>
     *
     */
    List<Entity> selectEntityInfo(Entity dto);

    /**
     * @Author han.zhang
     * @Description 查询图表展示的数据
     * @Date 13:50 2019/8/21
     * @Param [dto]
     * @param dto
     */
    List<ChartShowVO> selectChartShow(ChartShowVO dto);

    /**
     *
     * @Description 计量型实体控制图查询LOV
     *
     * @author yuchao.wang
     * @date 2019/8/23 9:56
     * @param dto
     * @return java.util.List<com.hand.spc.pspc_entity.dto.Entity>
     *
     */
    List<Entity> pspcEntitySampleLov(Entity dto);

    /**
     *
     * @Description 根据实体控制图ID查询对应的最大绘点数
     *
     * @author yuchao.wang
     * @date 2019/8/23 13:45
     * @param entityId 实体控制图ID
     * @return java.lang.Long
     *
     */
    Long queryMaxPlotPointsByEntityId(Long entityId);

    /**
     *
     * @Description 根据时间段来筛选对应实体控制图的数据
     *
     * @author yuchao.wang
     * @date 2019/8/23 15:08
     * @param entityCode 实体控制图编号
     * @param entityVersion 实体控制图版本
     * @param startDateStr 样本开始时间
     * @param endDateStr 样本结束时间
     * @return java.util.List<java.lang.String>
     *
     */
    List<String> querySampleValuesByTime(@Param("entityCode") String entityCode, @Param("entityVersion") String entityVersion,
                                         @Param("startDateStr") String startDateStr, @Param("endDateStr") String endDateStr);

    /**
     *
     * @Description 根据最大样本数来筛选对应实体控制图的数据
     *
     * @author yuchao.wang
     * @date 2019/8/23 15:08
     * @param entityCode 实体控制图编号
     * @param entityVersion 实体控制图版本
     * @param maxPlotPoints 最大样本数
     * @return java.util.List<java.lang.String>
     *
     */
    List<String> querySampleValuesByMaxPoints(@Param("entityCode") String entityCode, @Param("entityVersion") String entityVersion,
                                              @Param("maxPlotPoints") Long maxPlotPoints);

    /**
     *
     * @Description 查询数据
     *
     * @author wenzhang.yu
     * @date 2019/8/26
     * @param dto
     * @return java.util.List<com.hand.spc.pspc_entity.dto.Entity>
     *
     */
    List<Entity> selectData(Entity dto);

    List<String> querySampleValuesByMaxPoints(ScatterPlotVO dto);

    /**
     * @Author han.zhang
     * @Description 根据样本时间查询图形展示的数据
     * @Date 16:35 2019/8/27
     * @Param [dto]
     */
    List<ChartShowVO> selectChartShowByTime(ChartShowVO dto);

    /**
     * @Author han.zhang
     * @Description 查询计数型图形展示的数据
     * @Date 10:09 2019/8/29
     * @Param [dto]
     */
    List<ChartShowVO> selectCountChartShowData(ChartShowVO dto);

    /**
     * @Author han.zhang
     * @Description 计数型图形展示按时间查询
     * @Date 13:44 2019/9/3
     * @Param [dto]
     */
    List<ChartShowVO> selectCountChartShowDataByTime(ChartShowVO dto);
}