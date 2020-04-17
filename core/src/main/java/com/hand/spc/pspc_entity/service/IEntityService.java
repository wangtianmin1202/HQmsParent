package com.hand.spc.pspc_entity.service;

import com.hand.hap.core.IRequest;
import com.hand.hap.core.ProxySelf;
import com.hand.hap.system.dto.ResponseData;
import com.hand.hap.system.service.IBaseService;
import com.hand.spc.pspc_entity.dto.ChartShowVO;
import com.hand.spc.pspc_entity.dto.Entity;
import com.hand.spc.pspc_entity.view.ParametricComparisonVO;
import com.hand.spc.pspc_entity.view.ScatterPlotVO;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.ParseException;
import java.util.List;

public interface IEntityService extends IBaseService<Entity>, ProxySelf<IEntityService>{
    /**
     * @Author han.zhang
     * @Description 查询图表展示的数据
     * @Date 16:05 2019/8/21
     * @Param [dto]
     */
     List<ChartShowVO> selectChartShow(ChartShowVO dto) throws ParseException;

    /**
     *
     * @Description 散点图查询
     *
     * @author yuchao.wang
     * @date 2019/8/23 11:37
     * @param requestContext
     * @param dto
     * @return com.hand.hap.system.dto.ResponseData
     *
     */
    ResponseData queryScatterPlot(IRequest requestContext, List<ScatterPlotVO> dto);

    /**
     *
     * @Description 根据最大样本数来筛选对应实体控制图的数据
     *
     * @author yuchao.wang
     * @date 2019/8/23 15:08
     * @param entityCode 实体控制图编号
     * @param entityVersion 实体控制图版本
     * @param maxPlotPoints 最大样本数
     * @return java.util.List<java.lang.Long>
     *
     */
    List<Double> querySampleValuesByMaxPoints(String entityCode, String entityVersion, Long maxPlotPoints);

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
     * @return java.util.List<java.lang.Long>
     *
     */
    List<Double> querySampleValuesByTime(String entityCode, String entityVersion, String startDateStr, String endDateStr);

    /**
     *
     * @Description 散点图导出Excel
     *
     * @author yuchao.wang
     * @date 2019/8/26 21:26
     * @param request
     * @param response
     * @param matrixScatterPlotImg 矩阵散点图-可为空
     * @param scatterPlotSimpleImg 散点图-可为空
     * @param thead 表头 半角逗号分隔
     * @param tbody 表数据 井号分隔行，半角逗号分隔列
     * @return
     *
     */
    void exportExcelScatterPlot(HttpServletRequest request, HttpServletResponse response, String matrixScatterPlotImg, String scatterPlotSimpleImg, String thead, String tbody);

    /**
     * @param request 基本参数
     * @param dto  传入参数
     * @return : com.hand.hap.system.dto.ResponseData
     * @Description: 数据保存
     * @author: ywz
     * @date 2019/8/26
     * @version 1.0
     */
    List<Entity> selectData(IRequest request, Entity dto, int page, int pageSize);

    /**
     * @param requestCtx 基本参数
     * @param chartList  传入参数
     * @return : com.hand.hap.system.dto.ResponseData
     * @Description: 数据保存
     * @author: ywz
     * @date 2019/8/26
     * @version 1.0
     */
    ResponseData saveBaseData(IRequest requestCtx, List<Entity> chartList);

    /**
     *
     * @Description 多参数对比图查询
     *
     * @author yuchao.wang
     * @date 2019/8/27 15:26
     * @param dto
     * @param requestContext
     * @return com.hand.hap.system.dto.ResponseData
     *
     */
    ResponseData queryParametricComparisonChart(IRequest requestContext, List<ParametricComparisonVO> dto);

    /**
     *
     * @Description 多参数对比图导出Excel
     *
     * @author yuchao.wang
     * @date 2019/8/28 15:26
     * @param request
     * @param response
     * @param img 多参数对比图-可为空
     * @param thead 表头 半角逗号分隔
     * @param tbody 表数据 井号分隔行，半角逗号分隔列
     * @param title 导出文件名前缀
     * @param exportFileName 导出文件名前缀-支持中文
     * @return
     *
     */
    void exportExcelSingleGraph(HttpServletRequest request, HttpServletResponse response, String img,
                                String thead, String tbody, String title, String exportFileName,int rowEnd,int colEnd);

    /**
     * @Author han.zhang
     * @Description 图形展示的导出
     * @Date 下午5:27 2019/10/18
     * @Param [request, response, img, thead, tbody, title, exportFileName, rowEnd, colEnd]
     **/
    void exportExcelSingleGraphChartShow(HttpServletRequest request, HttpServletResponse response, String img,
                                String thead, String tbody, String title, String exportFileName,int rowEnd,int colEnd);

    /**
     * @Author han.zhang
     * @Description 计数型图形展示数据查询
     * @Date 22:35 2019/8/28
     * @Param [dto]
     */
    List<ChartShowVO> selectCountChartShow(ChartShowVO dto) throws ParseException;

    /**
     * @Author han.zhang
     * @Description 计量型图形展示数据查询
     * @Date 22:35 2019/8/28
     * @Param [dto]
     */
    List<ChartShowVO> selectSampleChartShow(ChartShowVO dto) throws ParseException;

    /**
     * @Author han.zhang
     * @Description  查询正态检验图形点的数据
     * @Date 14:43 2019/9/5
     * @Param [dto]
     */
    List<ChartShowVO> selectNormalCalibrationData(ChartShowVO dto) throws ParseException;
}