package com.hand.spc.pspc_entity.service.impl;

import com.hand.hap.cache.impl.SysCodeCache;
import com.hand.hap.core.IRequest;
import com.hand.hap.system.dto.Code;
import com.hand.hap.system.dto.ResponseData;
import com.github.pagehelper.PageHelper;
import com.hand.hap.system.service.impl.BaseServiceImpl;
import com.hand.spc.pspc_chart.dto.Chart;
import com.hand.spc.pspc_chart.dto.ChartDetail;
import com.hand.spc.pspc_chart.mapper.ChartDetailMapper;
import com.hand.spc.pspc_chart.mapper.ChartMapper;
import com.hand.spc.pspc_classify.dto.Classify;
import com.hand.spc.pspc_classify.mapper.ClassifyMapper;
import com.hand.spc.pspc_count_ooc.mapper.CountOocMapper;
import com.hand.spc.pspc_count_sample_data.dto.CountSampleData;
import com.hand.spc.pspc_count_sample_data.mapper.CountSampleDataMapper;
import com.hand.spc.pspc_count_sample_data_class.dto.CountSampleDataClass;
import com.hand.spc.pspc_count_sample_data_class.mapper.CountSampleDataClassMapper;
import com.hand.spc.pspc_count_sample_data_extend.dto.CountSampleDataExtend;
import com.hand.spc.pspc_count_sample_data_extend.mapper.CountSampleDataExtendMapper;
import com.hand.spc.pspc_entirety_statistic.dto.EntiretyStatistic;
import com.hand.spc.pspc_entirety_statistic.mapper.EntiretyStatisticMapper;
import com.hand.spc.pspc_entity.dto.ChartShowVO;
import com.hand.spc.pspc_entity.mapper.EntityMapper;
import com.hand.spc.pspc_entity.view.AndersonDarlingChartDataVo;
import com.hand.spc.pspc_ooc.dto.Ooc;
import com.hand.spc.pspc_ooc.mapper.OocMapper;
import com.hand.spc.pspc_entity.view.ParametricComparisonVO;
import com.hand.spc.pspc_sample_data.dto.SampleData;
import com.hand.spc.pspc_sample_data.mapper.SampleDataMapper;
import com.hand.spc.pspc_sample_subgroup_rel.dto.SampleSubgroupRel;
import com.hand.spc.pspc_sample_subgroup_rel.mapper.SampleSubgroupRelMapper;
import com.hand.utils.excelUtil.ExoprtExcelUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import com.hand.spc.pspc_entity.view.ScatterPlotVO;
import com.hand.utils.date.DateUtil;
import com.hand.spc.constants.SpcConstants;
import com.hand.spc.his.dto.*;
import com.hand.spc.his.service.*;
import com.hand.spc.pspc_count_ooc.dto.CountOoc;
import com.hand.spc.pspc_count_ooc.service.ICountOocService;
import com.hand.spc.pspc_count_statistic.dto.CountStatistic;
import com.hand.spc.pspc_count_statistic.service.ICountStatisticService;
import com.hand.spc.pspc_entirety_statistic.service.IEntiretyStatisticService;
import com.hand.spc.pspc_entity_role_relation.dto.EntityRoleRelation;
import com.hand.spc.pspc_entity_role_relation.service.IEntityRoleRelationService;
import com.hand.spc.pspc_message.dto.MessageL;
import com.hand.spc.pspc_message.service.IMessageServiceL;
import com.hand.spc.pspc_message_detail.dto.MessageDetail;
import com.hand.spc.pspc_message_detail.service.IMessageDetailService;
import com.hand.spc.pspc_ooc.service.IOocService;
import com.hand.spc.pspc_sample_subgroup.dto.SampleSubgroup;
import com.hand.spc.pspc_sample_subgroup.service.ISampleSubgroupService;
import com.hand.spc.pspc_sample_subgroup_rel.service.ISampleSubgroupRelService;
import com.hand.spc.pspc_subgroup_statistic.dto.SubgroupStatistic;
import com.hand.spc.pspc_subgroup_statistic.service.ISubgroupStatisticService;
import com.hand.spc.utils.ADHelperUtils;
import com.hand.spc.utils.CoordinateUtils;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import com.hand.spc.pspc_entity.dto.Entity;
import com.hand.spc.pspc_entity.service.IEntityService;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import java.util.*;
import java.util.stream.Collectors;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.stream.Stream;

@Service
@Transactional(rollbackFor = Exception.class)
public class EntityServiceImpl extends BaseServiceImpl<Entity> implements IEntityService,SpcConstants {

    @Autowired
    private EntityMapper entityMapper;

    @Autowired
    private SampleSubgroupRelMapper sampleSubgroupRelMapper;

    @Autowired
    private SampleDataMapper sampleDataMapper;

    @Autowired
    private EntiretyStatisticMapper entiretyStatisticMapper;

    @Autowired
    private ChartDetailMapper chartDetailMapper;

    @Autowired
    private IEntityHisService entityHisService;

    @Autowired
    private IEntityRoleRelationService entityRoleRelationService;

    @Autowired
    private IEntityRoleRelationHisService entityRoleRelationHisService;

    @Autowired
    private IOocService oocService;

    @Autowired
    private IOocHisService oocHisService;

    @Autowired
    private ISampleSubgroupService sampleSubgroupService;

    @Autowired
    private ISampleSubgroupHisService sampleSubgroupHisService;

    @Autowired
    private ISampleSubgroupRelService sampleSubgroupRelService;

    @Autowired
    private ISampleSubgroupRelHisService sampleSubgroupRelHisService;

    @Autowired
    private ISubgroupStatisticService subgroupStatisticService;

    @Autowired
    private ISubgroupStatisticHisService subgroupStatisticHisService;

    @Autowired
    private IEntiretyStatisticService entiretyStatisticService;

    @Autowired
    private IEntiretyStatisticHisService entiretyStatisticHisService;

    @Autowired
    private IMessageServiceL messageService;

    @Autowired
    private IMessageHisService messageHisService;

    @Autowired
    private IMessageDetailService messageDetailService;

    @Autowired
    private IMessageDetailHisService messageDetailHisService;

    @Autowired
    private ICountOocService countOocService;

    @Autowired
    private ICountOocHisService countOocHisService;

    @Autowired
    private ICountStatisticService countStatisticService;

    @Autowired
    private ICountStatisticHisService countStatisticHisService;

    @Autowired
    private OocMapper oocMapper;

    @Autowired
    private ChartMapper chartMapper;

    @Autowired
    private CountSampleDataMapper countSampleDataMapper;

    @Autowired
    private CountSampleDataClassMapper countSampleDataClassMapper;

    @Autowired
    private CountSampleDataExtendMapper countSampleDataExtendMapper;

    @Autowired
    private ClassifyMapper classifyMapper;

    @Autowired
    private CountOocMapper countOocMapper;

    @Autowired
    private SysCodeCache sysCodeCache;

    /**
     * @Author han.zhang
     * @Description 查询图表展示的数据
     * @Date 16:05 2019/8/21
     * @Param [dto]
     */
    @Override
    public List<ChartShowVO> selectChartShow(ChartShowVO dto) throws ParseException {
        //首先根据控制图id查找是什么类型的图形
        Chart chart = new Chart();
        chart.setChartId(Float.valueOf(dto.getChartId()));
        chart = chartMapper.selectByPrimaryKey(chart);
        if(null == chart){
            throw new RuntimeException("该实体没有对应的图表数据");
        }
        //判断是否在计数、计量的八种图形类型中
        boolean countFlag = Arrays.asList(countChartType).contains(chart.getChartType());
        boolean sampleFlag = Arrays.asList(sampleChartType).contains(chart.getChartType());
        if(!countFlag && !sampleFlag){
            throw new RuntimeException("该实体控制图没有对应的图表类型");
        }
        List<ChartShowVO> chartShowVOS;

        if(sampleFlag){
            //计量的类型
            chartShowVOS = self().selectSampleChartShow(dto);
        }else if(countFlag){
            //计数
            chartShowVOS = self().selectCountChartShow(dto);
        }else{
            throw new RuntimeException("没有该类型-"+chart.getChartType());
        }
        return chartShowVOS;
    }

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
    @Override
    public ResponseData queryScatterPlot(IRequest requestContext, List<ScatterPlotVO> dto) {
        ResponseData responseData = new ResponseData();
        ScatterPlotVO scatterPlotVO = new ScatterPlotVO();//最终返回的DTO
        List<Double> xSampleValues = new ArrayList<>();//散点图X坐标集合
        List<Double> ySampleValues = new ArrayList<>();//散点图Y坐标集合
        Map<String, String> scatterPlotSchema = new HashMap<>();//散点图XY轴名
        List<Map<String, String>> matrixScatterPlotSchema = new ArrayList<>();//矩阵散点图矩阵块集合 key:name/text value:名称/显示文字
        int minSize = Integer.MAX_VALUE;//所有实体控制图中最小的样本数量
        int maxSize = Integer.MIN_VALUE;//所有实体控制图中最大的样本数量

        if(dto == null || dto.size() < 2){
            responseData.setSuccess(false);
            responseData.setMessage("查询的实体控制图需要大于等于两个");
            return responseData;
        }

        //先校验数据，获取基础数据
        for (ScatterPlotVO vo : dto) {
            List<Double> sampleValues = new ArrayList<>();

            //校验时间
            if((vo.getStartDate() != null && vo.getEndDate() == null)
            || (vo.getStartDate() == null && vo.getEndDate() != null)) {
                responseData.setSuccess(false);
                responseData.setMessage("时间最长跨度为30天");
                return responseData;
            } else if (vo.getStartDate() != null && vo.getEndDate() != null) {
                //获取时间跨度
                long days = DateUtil.getDateInterval(vo.getStartDate(), vo.getEndDate());
                if(days < 0 || days > 30) {
                    responseData.setSuccess(false);
                    responseData.setMessage("时间最长跨度为30天");
                    return responseData;
                }

                //查询基础数据
                vo.setMaxPlotPoints(null);
                sampleValues = querySampleValuesByTime(vo.getEntityCode(), vo.getEntityVersion(), vo.getStartDateStr(),vo.getEndDateStr());
                vo.setSampleValues(sampleValues);
            } else {
                //获取最大样本数
                long maxPlotPoints = entityMapper.queryMaxPlotPointsByEntityId(vo.getEntityId());
                vo.setMaxPlotPoints(maxPlotPoints);

                //查询基础数据
                sampleValues = querySampleValuesByMaxPoints(vo.getEntityCode(), vo.getEntityVersion(), vo.getMaxPlotPoints());
                vo.setSampleValues(sampleValues);
            }

            //如果是单个分析，将数据点加入对应的XY集合
            if("Y".equals(vo.getIndividualAnalysis())) {
                if("abscissa".equals(vo.getCoordinateAxis())){
                    xSampleValues.addAll(sampleValues);
                    scatterPlotSchema.put("X", vo.getEntityCode() + "\n" + vo.getCeParameterName());
                } else if("ordinate".equals(vo.getCoordinateAxis())){
                    ySampleValues.addAll(vo.getSampleValues());
                    scatterPlotSchema.put("Y", vo.getEntityCode() + "\n" + vo.getCeParameterName());
                }
            }

            //实体控制图信息添加到Schema
            Map<String, String> schema = new HashMap<>();
            schema.put("name", String.valueOf(vo.getEntityId()));
            schema.put("text", vo.getEntityCode() + "\n" + vo.getCeParameterName());
            matrixScatterPlotSchema.add(schema);

            //比较获取实体控制图中最小及最小的样本数量
            minSize = Math.min(minSize, sampleValues.size());
            maxSize = Math.max(maxSize, sampleValues.size());
        }

        //构造散点图数据，以较小的坐标值构造点位
        if(!xSampleValues.isEmpty() && !ySampleValues.isEmpty()){
            List<Double[]> scatterPlotPoints = new ArrayList<>();
            int size = Math.min(xSampleValues.size(), ySampleValues.size());

            for(int i = 0; i < size; i++) {
//                scatterPlotPoints.add(new Long[]{xSampleValues.get(i), ySampleValues.get(i)});
                scatterPlotPoints.add(new Double[]{xSampleValues.get(i), ySampleValues.get(i)});
            }

            scatterPlotVO.setScatterPlotSchema(scatterPlotSchema);
            scatterPlotVO.setScatterPlotPoints(scatterPlotPoints);
        }

        //构造矩阵散点图数据及表格数据
        if(maxSize > 0) {
            List<List<Object>> tableData = new ArrayList<>();//表格数据集合
            List<List<Object>> matrixScatterPlotPoints = new ArrayList<>();//矩阵散点图点位集合

            for(int i = 0; i < maxSize; i++) {
                //构造表格数据
                List<Object> trData = new ArrayList<>();//表格行数据
                for (int j = 0; j < dto.size(); j++) {
                    ScatterPlotVO vo = dto.get(j);

                    if (i < vo.getSampleValues().size()) {
                        trData.add(vo.getSampleValues().get(i));
                    } else {
                        trData.add("-");
                    }
                }
                tableData.add(trData);

                //构造矩阵散点图数据
                if(i < minSize && minSize != Integer.MAX_VALUE) {
                    List<Object> matrixPoint = new ArrayList<>();

                    //将对应的点位值放入数组
                    for (ScatterPlotVO vo : dto) {
                        matrixPoint.add(vo.getSampleValues().get(i));
                    }

                    //Echarts矩阵散点图要求最后一列是数据类别，这里固定写死为空
                    matrixPoint.add("");
                    matrixScatterPlotPoints.add(matrixPoint);
                }
            }

            scatterPlotVO.setTableData(tableData);
            scatterPlotVO.setMatrixScatterPlotPoints(matrixScatterPlotPoints);
        }

        //将原始信息及样本数据信息存入VO
        scatterPlotVO.setPointCount(minSize);
        scatterPlotVO.setInVo(dto);
        scatterPlotVO.setMatrixScatterPlotSchema(matrixScatterPlotSchema);

        responseData.setRows(Collections.singletonList(scatterPlotVO));
        return responseData;
    }

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
    @Override
    public List<Double> querySampleValuesByMaxPoints(String entityCode, String entityVersion, Long maxPlotPoints) {
        List<String> sampleValues = entityMapper.querySampleValuesByMaxPoints(entityCode, entityVersion, maxPlotPoints);

        return sampleValues.stream().map(Double::valueOf).collect(Collectors.toList());
    }

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
    @Override
    public List<Double> querySampleValuesByTime(String entityCode, String entityVersion, String startDateStr, String endDateStr) {
        List<String> sampleValues = entityMapper.querySampleValuesByTime(entityCode, entityVersion, startDateStr, endDateStr);
        return sampleValues.stream().map(Double::parseDouble).collect(Collectors.toList());
    }

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
    @Override
    public void exportExcelScatterPlot(HttpServletRequest request, HttpServletResponse response, String matrixScatterPlotImg, String scatterPlotSimpleImg, String thead, String tbody) {
        String matrixScatterPlotUrl = "";//矩阵散点图URL
        String scatterPlotSimpleUrl = "";//散点图URL
        if(!StringUtils.isEmpty(matrixScatterPlotImg))
            matrixScatterPlotUrl = matrixScatterPlotImg.split(",")[1];
        if(!StringUtils.isEmpty(scatterPlotSimpleImg))
            scatterPlotSimpleUrl = scatterPlotSimpleImg.split(",")[1];

        //处理数据进行导出
        try {
            //创建工作簿
            XSSFWorkbook workbook = new XSSFWorkbook();

            //创建样式
            XSSFCellStyle style = ExoprtExcelUtil.getDefaultCellStyle(workbook);

            //创建sheet页，设置默认单元格宽高
            XSSFSheet sheet = workbook.createSheet();
            sheet.setDefaultColumnWidth(14);//与Excel设置的行高比例约为 1:1.17
            sheet.setDefaultRowHeightInPoints(19);//与Excel设置的行高一致

            /**
             * 大标题 2行16列 索引从0开始
             */
            XSSFRow firstRow = sheet.createRow(0);
            XSSFCell cellTitle = firstRow.createCell(0);
            cellTitle.setCellStyle(style);
            cellTitle.setCellValue("散点图");
            //创建一个合并单元格 firstRowIndex, lastRowIndex, firstColIndex, lastColIndex 索引从0开始
            sheet.addMergedRegion(new CellRangeAddress(0, 1, 0, 16));

            /**
             * 将图片插入进去
             */
            if(!StringUtils.isEmpty(matrixScatterPlotUrl))
                ExoprtExcelUtil.addPictureIntoExcel(matrixScatterPlotUrl, sheet, workbook, 3, 21, 0, 8);
            if(!StringUtils.isEmpty(scatterPlotSimpleUrl))
                ExoprtExcelUtil.addPictureIntoExcel(scatterPlotSimpleUrl, sheet, workbook, 3, 21, 8, 16);

            /**
             * 填充表格数据
             */
            //填充表头
            XSSFCellStyle headStyle = ExoprtExcelUtil.getCellStyle(workbook, FillPatternType.SOLID_FOREGROUND, new XSSFColor(java.awt.Color.ORANGE));
            XSSFRow theadRow = sheet.createRow(22);

            XSSFCell cellSeq = theadRow.createCell(0);
            cellSeq.setCellStyle(headStyle);
            cellSeq.setCellValue("序号");

            String[] theads = thead.split(",");
            for(int i=0; i<theads.length; i++){
                XSSFCell cellThead = theadRow.createCell(i+1);
                cellThead.setCellStyle(headStyle);
                cellThead.setCellValue(theads[i]);
            }

            //填充表数据
            String[] tbodyRows = tbody.split("#");
            for(int i=0; i<tbodyRows.length; i++){
                XSSFRow row = sheet.createRow(23+i);
                String[] tbodyCols = tbodyRows[i].split(",");
                for(int j=0; j<tbodyCols.length; j++){
                    XSSFCell cell = row.createCell(j);
                    cell.setCellStyle(style);
                    cell.setCellValue(tbodyCols[j]);
                }
            }

            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
            response.addHeader("Content-Disposition",
                    "attachment; filename=\"" + "scatterPlotExport_" + sdf.format(new Date()) + ".xlsx" + "\"");
            response.setContentType("application/vnd.ms-excel" + ";charset=" + "UTF-8");
            OutputStream out = response.getOutputStream();

            // 写入excel文件
            workbook.write(out);
            out.close();
            workbook.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

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
    @Override
    public ResponseData queryParametricComparisonChart(IRequest requestContext, List<ParametricComparisonVO> dto) {
        ResponseData responseData = new ResponseData();
        ParametricComparisonVO parametricComparisonVO = new ParametricComparisonVO();//最终返回的DTO
        List<List<Double>> parametricComparisonPoints = new ArrayList<>();//折线图点位集合
        List<Map<String, String>> parametricComparisonSchema = new ArrayList<>();//折线图schema集合 key:name/text value:名称/显示文字
        int maxSize = Integer.MIN_VALUE;//所有实体控制图中最大的样本数量

        if(dto == null || dto.size() < 2){
            responseData.setSuccess(false);
            responseData.setMessage("查询的实体控制图需要大于等于两个");
            return responseData;
        }

        //先校验数据，获取基础数据
        for (ParametricComparisonVO vo : dto) {
            List<Double> sampleValues = new ArrayList<>();

            //校验时间
            if((vo.getStartDate() != null && vo.getEndDate() == null)
                    || (vo.getStartDate() == null && vo.getEndDate() != null)) {
                responseData.setSuccess(false);
                responseData.setMessage("时间最长跨度为30天");
                return responseData;
            } else if (vo.getStartDate() != null && vo.getEndDate() != null) {
                //获取时间跨度
                long days = DateUtil.getDateInterval(vo.getStartDate(), vo.getEndDate());
                if(days < 0 || days > 30) {
                    responseData.setSuccess(false);
                    responseData.setMessage("时间最长跨度为30天");
                    return responseData;
                }

                //查询基础数据
                vo.setMaxPlotPoints(null);
                sampleValues = querySampleValuesByTime(vo.getEntityCode(), vo.getEntityVersion(), vo.getStartDateStr(),vo.getEndDateStr());
                vo.setSampleValues(sampleValues);
            } else {
                //获取最大样本数
                long maxPlotPoints = entityMapper.queryMaxPlotPointsByEntityId(vo.getEntityId());
                vo.setMaxPlotPoints(maxPlotPoints);

                //查询基础数据
                sampleValues = querySampleValuesByMaxPoints(vo.getEntityCode(), vo.getEntityVersion(), vo.getMaxPlotPoints());
                vo.setSampleValues(sampleValues);
            }

            //比较获取实体控制图中最大的样本数量
            maxSize = Math.max(maxSize, sampleValues.size());

            //将对应的点位值放入数组
            parametricComparisonPoints.add(sampleValues);

            //将实体控制图信息添加到Schema
            Map<String, String> schema = new HashMap<>();
            schema.put("name", String.valueOf(vo.getEntityId()));
            schema.put("text", vo.getEntityCode() + "\n" + vo.getDescription());
            parametricComparisonSchema.add(schema);
        }

        //构造表格数据
        if(maxSize > 0) {
            List<List<Object>> tableData = new ArrayList<>();//表格数据集合

            for(int i = 0; i < maxSize; i++) {
                //构造表格数据
                List<Object> trData = new ArrayList<>();//表格行数据
                for (int j = 0; j < dto.size(); j++) {
                    ParametricComparisonVO vo = dto.get(j);

                    if (i < vo.getSampleValues().size()) {
                        trData.add(vo.getSampleValues().get(i));
                    } else {
                        trData.add("-");
                    }
                }
                tableData.add(trData);
            }

            parametricComparisonVO.setTableData(tableData);
        }

        parametricComparisonVO.setInVo(dto);
        parametricComparisonVO.setParametricComparisonPoints(parametricComparisonPoints);
        parametricComparisonVO.setParametricComparisonSchema(parametricComparisonSchema);
        responseData.setRows(Collections.singletonList(parametricComparisonVO));
        return responseData;
    }

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
    @Override
    public void exportExcelSingleGraph(HttpServletRequest request, HttpServletResponse response, String img,
                                       String thead, String tbody, String title, String exportFileName,int rowEnd,int colEnd) {
        String imgUrl = "";//多参数对比图URL
        if(!StringUtils.isEmpty(img))
            imgUrl = img.split(",")[1];

        //处理数据进行导出
        try {
            //创建工作簿
            XSSFWorkbook workbook = new XSSFWorkbook();

            //创建样式
            XSSFCellStyle style = ExoprtExcelUtil.getDefaultCellStyle(workbook);

            //创建sheet页，设置默认单元格宽高
            XSSFSheet sheet = workbook.createSheet();
            sheet.setDefaultColumnWidth(14);//与Excel设置的行高比例约为 1:1.17
            sheet.setDefaultRowHeightInPoints(19);//与Excel设置的行高一致

            /**
             * 大标题 2行15列 索引从0开始
             */
            XSSFRow firstRow = sheet.createRow(0);
            XSSFCell cellTitle = firstRow.createCell(0);
            cellTitle.setCellStyle(style);
            cellTitle.setCellValue(title);
            //创建一个合并单元格 firstRowIndex, lastRowIndex, firstColIndex, lastColIndex 索引从0开始
            sheet.addMergedRegion(new CellRangeAddress(0, 1, 0, 15));

            /**
             * 将图片插入进去
             */
            if(!StringUtils.isEmpty(imgUrl))
                ExoprtExcelUtil.addPictureIntoExcel(imgUrl, sheet, workbook, 3, rowEnd, 0, colEnd);

            /**
             * 填充表格数据
             */
            //填充表头
            if(!StringUtils.isEmpty(thead)) {
                XSSFCellStyle headStyle = ExoprtExcelUtil.getCellStyle(workbook, FillPatternType.SOLID_FOREGROUND, new XSSFColor(java.awt.Color.ORANGE));
                XSSFRow theadRow = sheet.createRow(rowEnd+1);

                XSSFCell cellSeq = theadRow.createCell(0);
                cellSeq.setCellStyle(headStyle);
                cellSeq.setCellValue("序号");

                String[] theads = thead.split(",");
                for (int i = 0; i < theads.length; i++) {
                    XSSFCell cellThead = theadRow.createCell(i + 1);

                    cellThead.setCellStyle(headStyle);
                    cellThead.setCellValue(theads[i]);
                }
            }

            //填充表数据
            if(!StringUtils.isEmpty(tbody)) {
                String[] tbodyRows = tbody.split("#");
                for (int i = 0; i < tbodyRows.length; i++) {
                    XSSFRow row = sheet.createRow(rowEnd+2 + i);
                    String[] tbodyCols = tbodyRows[i].split(",");
                    for (int j = 0; j < tbodyCols.length; j++) {
                        XSSFCell cell = row.createCell(j);
                        cell.setCellStyle(style);
                        cell.setCellValue(tbodyCols[j]);
                    }
                }
            }
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");

            response.setCharacterEncoding("utf-8");
            response.addHeader("Content-Disposition",
                    "attachment; filename=\"" + new String(exportFileName.getBytes("gbk"),"iso8859-1") + "_" + sdf.format(new Date()) + ".xlsx" + "\"");
            response.setContentType("application/vnd.ms-excel" + ";charset=" + "UTF-8");
            OutputStream out = response.getOutputStream();

            // 写入excel文件
            workbook.write(out);
            out.close();
            workbook.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<ChartShowVO> selectCountChartShow(ChartShowVO dto) throws ParseException {
        List<ChartShowVO> chartShowVOS = new ArrayList<>();
        Entity entity = new Entity();
        entity.setEntityId(dto.getEntityId());
        entity = entityMapper.selectByPrimaryKey(entity);

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DATE_TIME_FORMAT);
        //时间为空则取数据限定前N条数据
        if(StringUtils.isBlank(dto.getSampleStartTimeStr()) && StringUtils.isBlank(dto.getSampleEndTimeStr())){
            chartShowVOS = entityMapper.selectCountChartShowData(dto);
        }else{
            //不为空则取时间范围内的数据
            dto.setSampleStartTime(simpleDateFormat.parse(dto.getSampleStartTimeStr()));
            dto.setSampleEndTime(simpleDateFormat.parse(dto.getSampleEndTimeStr()));
            chartShowVOS = entityMapper.selectCountChartShowDataByTime(dto);
        }
        if(CollectionUtils.isEmpty(chartShowVOS)){
            throw new RuntimeException("未查询到数据");
        }
        //所有的不良项
        List<Classify> classifyList = new ArrayList<>();
        //所有的批次
        List<String> batchCodeList = new ArrayList<>();

        Entity finalEntity = entity;
        chartShowVOS.forEach(vo -> {
            //不良项
            CountSampleDataClass countSampleDataClass = new CountSampleDataClass();
            countSampleDataClass.setCountSampleDataId(vo.getCountSampleDataId());
            List<CountSampleDataClass> sampleDataClasses = countSampleDataClassMapper.select(countSampleDataClass);
            vo.setSampleDataClassList(sampleDataClasses);

            //不良项 对应标题
            sampleDataClasses.forEach(dataClass -> {
                Classify classify = new Classify();
                classify.setClassifyId(dataClass.getClassifyId());
                classify = classifyMapper.selectByPrimaryKey(classify);
                Long classifyId = classify.getClassifyId();
                boolean existFlag = classifyList.stream().anyMatch(classifyDto -> classifyDto.getClassifyId().equals(classifyId));
                if(!existFlag){
                    classifyList.add(classify);
                }
            });

            //批次
            CountSampleDataExtend countSampleDataExtend = new CountSampleDataExtend();
            countSampleDataExtend.setCountSampleDataId(vo.getCountSampleDataId());
            List<CountSampleDataExtend> countSampleDataExtends = countSampleDataExtendMapper.select(countSampleDataExtend);
            vo.setSampleDataExtendList(countSampleDataExtends);
            //将所有的批次放入
            countSampleDataExtends.forEach(extend -> {
                boolean existFlag = batchCodeList.contains(extend.getExtendAttribute());
                if(!existFlag){
                    batchCodeList.add(extend.getExtendAttribute());
                }
            });
            //样本ooc
            CountOoc countOoc = new CountOoc();
            countOoc.setCountSampleDataId(vo.getCountSampleDataId());
            countOoc.setEntityCode(finalEntity.getEntityCode());
            countOoc.setEntityVersion(finalEntity.getEntityVersion());
            List<CountOoc> countOocs = countOocMapper.selectCountOocJudge(countOoc);
            vo.setCountOocList(countOocs);
        });
        if(chartShowVOS.size() > 0){
            chartShowVOS.get(0).setBadItemList(classifyList);
            chartShowVOS.get(0).setBatchCodeList(batchCodeList);

            //计算Y轴最大最小值
            ChartDetail chartDetail = new ChartDetail();
            chartDetail.setChartId(Float.valueOf(chartShowVOS.get(chartShowVOS.size() - 1).getChartId()));
            chartDetail.setChartDetailType("M");
            List<ChartDetail> chartDetails = chartDetailMapper.select(chartDetail);
            chartShowVOS.get(0).setChartDetailM(chartDetails.get(0));
        }
        return chartShowVOS;
    }

    @Override
    public List<ChartShowVO> selectSampleChartShow(ChartShowVO dto) throws ParseException {
        List<ChartShowVO> chartShowVOS;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DATE_TIME_FORMAT);
        //时间为空则取数据限定前N条数据
        if(StringUtils.isBlank(dto.getSampleStartTimeStr()) && StringUtils.isBlank(dto.getSampleEndTimeStr())){
            chartShowVOS = entityMapper.selectChartShow(dto);
        }else{
            //不为空则取时间范围内的数据
            dto.setSampleStartTime(simpleDateFormat.parse(dto.getSampleStartTimeStr()));
            dto.setSampleEndTime(simpleDateFormat.parse(dto.getSampleEndTimeStr()));
            chartShowVOS = entityMapper.selectChartShowByTime(dto);
        }
        if(CollectionUtils.isEmpty(chartShowVOS)){
            throw new RuntimeException("未查询到数据");
        }
        if(chartShowVOS.size() > 0){
            //样本数量
            chartShowVOS.get(0).setSampleCount(0L);
            //计算UCL、LCL、CL、LSL、LCL
            Long lastsSampleSubgroupId = chartShowVOS.get(chartShowVOS.size() - 1).getSampleSubgroupId();
            EntiretyStatistic entiretyStatistic = new EntiretyStatistic();
            entiretyStatistic.setSampleSubgroupId(Float.valueOf(lastsSampleSubgroupId));
            List<EntiretyStatistic> entiretyStatistics = entiretyStatisticMapper.select(entiretyStatistic);
            entiretyStatistics.forEach(statistic -> {
                Double uclValue = (null!=statistic.getEntiretyUcl())?Double.valueOf(String.valueOf(new BigDecimal(statistic.getEntiretyUcl()).setScale(4, RoundingMode.UP))):null;
                Double lclValue = (null!=statistic.getEntiretyLcl())?Double.valueOf(String.valueOf(new BigDecimal(statistic.getEntiretyLcl()).setScale(4, RoundingMode.UP))):null;
                Double clValue = (null!=statistic.getEntiretyCl())?Double.valueOf(String.valueOf(new BigDecimal(statistic.getEntiretyCl()).setScale(4, RoundingMode.UP))):null;
                Double uslValue = (null!=statistic.getEntiretyUsl())?Double.valueOf(String.valueOf(new BigDecimal(statistic.getEntiretyUsl()).setScale(4, RoundingMode.UP))):null;
                Double lslValue = (null!=statistic.getEntiretyLsl())?Double.valueOf(String.valueOf(new BigDecimal(statistic.getEntiretyLsl()).setScale(4, RoundingMode.UP))):null;
                Double sigma = (null!=statistic.getEntiretySigma())?Double.valueOf(String.valueOf(new BigDecimal(statistic.getEntiretySigma()).setScale(4, RoundingMode.UP))):null;
                //主图 UCL、LCL、CL、LSL、LCL、SIGMA
                if(M_TYPE.equals(statistic.getChartDetailType())){
                    chartShowVOS.get(0).setUclMValue(uclValue);
                    chartShowVOS.get(0).setLclMValue(lclValue);
                    chartShowVOS.get(0).setClMValue(clValue);
                    chartShowVOS.get(0).setUslMValue(uslValue);
                    chartShowVOS.get(0).setLslMValue(lslValue);
                    chartShowVOS.get(0).setSigmaMValue(sigma);
                }
                //次图 UCL、LCL、CL、LSL、LCL、SIGMA
                if(S_TYPE.equals(statistic.getChartDetailType())){
                    chartShowVOS.get(0).setUclSValue(uclValue);
                    chartShowVOS.get(0).setLclSValue(lclValue);
                    chartShowVOS.get(0).setClSValue(clValue);
                    chartShowVOS.get(0).setUslSValue(uslValue);
                    chartShowVOS.get(0).setLslSValue(lslValue);
                    chartShowVOS.get(0).setSigmaSValue(sigma);
                }
            });

            //计算SL值
            ChartDetail chartDetail = new ChartDetail();
            chartDetail.setChartId(Float.valueOf(chartShowVOS.get(chartShowVOS.size() - 1).getChartId()));
            List<ChartDetail> chartDetails = chartDetailMapper.select(chartDetail);
            chartDetails.forEach(detail -> {
                Double slValue = (null!=detail.getSpecTarget())?Double.valueOf(String.valueOf(new BigDecimal(detail.getSpecTarget()).setScale(4, RoundingMode.UP))):null;
                if(M_TYPE.equals(detail.getChartDetailType())){
                    chartShowVOS.get(0).setSlMValue(slValue);
                    chartShowVOS.get(0).setChartDetailM(detail);
                }
                if(S_TYPE.equals(detail.getChartDetailType())){
                    chartShowVOS.get(0).setSlSValue(slValue);
                    chartShowVOS.get(0).setChartDetailS(detail);
                }
            });

        }
        chartShowVOS.stream().forEach(vo -> {
            //添加样本
            List<SampleData> sampleDataList = new ArrayList<>();
            SampleSubgroupRel sampleSubgroupRel = new SampleSubgroupRel();
            sampleSubgroupRel.setSampleSubgroupId(Float.valueOf(vo.getSampleSubgroupId()));
            List<SampleSubgroupRel> sampleSubgroupRels = sampleSubgroupRelMapper.select(sampleSubgroupRel);
            sampleSubgroupRels.stream().forEach(rel -> {
                SampleData sampleData = new SampleData();
                sampleData.setSampleDataId(rel.getSampleDataId());
                sampleData = sampleDataMapper.selectByPrimaryKey(rel);
                if(null != sampleData){
                    sampleDataList.add(sampleData);
                }
            });
            //更新最大样本数量，主要是设定表格 标题样本列数
            if(chartShowVOS.get(0).getSampleCount() < sampleDataList.size()){
                chartShowVOS.get(0).setSampleCount(Long.valueOf(sampleDataList.size()));
            }
            vo.setSampleDataList(sampleDataList);
            //添加OOC
            Ooc ooc = new Ooc();
            ooc.setSampleSubgroupId(Float.valueOf(vo.getSampleSubgroupId()));
            List<Ooc> oocs = oocMapper.selectOocJudge(ooc);
            vo.setOocList(oocMapper.selectOocJudge(ooc));
        });
        return chartShowVOS;
    }

    @Override
    public List<ChartShowVO> selectNormalCalibrationData(ChartShowVO dto) throws ParseException {
        ADHelperUtils.demoRun();
        //校验控制图类型
        Chart chart = new Chart();
        chart.setChartId(Float.valueOf(dto.getChartId()));
        List<Chart> charts = chartMapper.select(chart);
        if(charts.size() == 0){
            throw new RuntimeException("控制图不存在");
        }
        chart = charts.get(0);
        String chartType = chart.getChartType();

        if(!Arrays.asList(sampleChartType).contains(chartType)){
            throw new RuntimeException("控制图不存在");
        }
        //查询图表中的数据
        List<ChartShowVO> chartShowVOS = self().selectSampleChartShow(dto);

        //去除所有的xi
        List<BigDecimal> xScotss = new ArrayList<>();
        chartShowVOS.stream().forEach(vo -> {
            List<BigDecimal> floatList = vo.getSampleDataList().stream()
                    .map(v -> new BigDecimal(String.valueOf(v.getSampleValue())))
                    .collect(Collectors.toList());
            xScotss.addAll(floatList);
        });
        //对xi从小到大进行排序
        List<BigDecimal> xScots = xScotss.stream().sorted().collect(Collectors.toList());
        List<BigDecimal> yScots = ADHelperUtils.getEstimatedProbability(xScotss);
        //计算zi
        List<BigDecimal> zScots = ADHelperUtils.getNormSInv(xScotss);
        //拼接xi zi格式
        List<AndersonDarlingChartDataVo> xzScots = CoordinateUtils.getCoordinate(xScots, null, zScots);

        BigDecimal averageValue = ADHelperUtils.getAverage(xScotss);
        BigDecimal sigmaValue = ADHelperUtils.getSigma(xScotss);
        //计算xi'
        List<BigDecimal> convertedX = ADHelperUtils.getConvertedX(ADHelperUtils.convertedY, averageValue.doubleValue(),
                sigmaValue.doubleValue());
        //计算zi'
        List<BigDecimal> convertedZ = ADHelperUtils.getConvertedZ(convertedX, ADHelperUtils.getAverage(xScotss), ADHelperUtils.getSigma(xScotss));
        //拼接xi' zi'
        List<AndersonDarlingChartDataVo> convertXzScots = CoordinateUtils.getCoordinate(convertedX,null,convertedZ);
        //计算平均值
        BigDecimal average = ADHelperUtils.getAverage(xScots);
        //计算标准差
        BigDecimal standardValue = ADHelperUtils.getSigma(xScots);
        //计算AD值
        BigDecimal adValue = ADHelperUtils.getAD(xScots);
        //计算P值
        BigDecimal pValue = ADHelperUtils.getPOfAD(xScots);

        chartShowVOS.get(0).setConvertX(convertedX);

        chartShowVOS.get(0).setXzScot(xzScots);
        chartShowVOS.get(0).setConvertXzScot(convertXzScots);
        chartShowVOS.get(0).setConvertZ(convertedZ);
        chartShowVOS.get(0).setAverageValue(average.doubleValue());
        chartShowVOS.get(0).setStandardValue(standardValue.doubleValue());
        chartShowVOS.get(0).setAdValue(adValue.doubleValue());
        chartShowVOS.get(0).setpOfAdValue(pValue.doubleValue());
        return chartShowVOS;

    }

    /**
     * @param request 基本参数
     * @param   dto 传入参数
     * @return : com.hand.hap.system.dto.ResponseData
     * @Description: 数据保存
     * @author: ywz
     * @date 2019/8/22 10:32
     * @version 1.0
     */
    @Override
    public List<Entity> selectData(IRequest request, Entity dto, int page, int pageSize){
        PageHelper.startPage(page,pageSize);
        List<Entity> result=entityMapper.selectData(dto);

        return result;
    }

    /**
     * @param requestCtx 基本参数
     * @param entityList  传入参数
     * @return : com.hand.hap.system.dto.ResponseData
     * @Description: 数据保存
     * @author: ywz
     * @date 2019/8/26 10:32
     * @version 1.0
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public ResponseData saveBaseData(IRequest requestCtx, List<Entity> entityList) {

        ResponseData responseData = new ResponseData();

        for (Entity item : entityList) {
            if (item.getEntityId() != null) {
                item.set__status("update");
            } else {
                item.set__status("add");
            }

            // 数据更新判断
            responseData = checkSubGroup(requestCtx, item);
            if (!responseData.isSuccess()) {
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                return responseData;
            }
            if (item.get__status().equals("add")) {
                // 判断 实体控制图赋值是否有值
                if (item.getCeCopy() != null) {
                    // 将要复制数据的明细全部复制到新建/修改的数据上
                    Entity entityDetail = new Entity();
                    entityDetail.setEntityId(item.getCeCopy());
                    Entity chartDetail = entityMapper.selectOne(entityDetail);
                    chartDetail.setEntityCode(item.getEntityCode());
                    chartDetail.setEntityVersion("1");
                    chartDetail.setEntityNew("Y");
                    //新增数据
                    self().insertSelective(requestCtx, chartDetail);

                    item.setCeCopy(null);

                } else {
                    // 直接更新数据
                    item.setEntityNew("Y");
                    item.setEntityStatus("ACTIVE");
                    self().insertSelective(requestCtx, item);
                }
            }
        }

        responseData.setSuccess(true);
        responseData.setRows(entityList);

        return responseData;
    }

    @Override
    public void exportExcelSingleGraphChartShow(HttpServletRequest request, HttpServletResponse response, String img,
                                       String thead, String tbody, String title, String exportFileName,int rowEnd,int colEnd) {
        String imgUrl = "";//多参数对比图URL
        if(!StringUtils.isEmpty(img))
            imgUrl = img.split(",")[1];

        //处理数据进行导出
        try {
            //创建工作簿
            XSSFWorkbook workbook = new XSSFWorkbook();

            //创建样式
            XSSFCellStyle style = ExoprtExcelUtil.getDefaultCellStyle(workbook);

            //创建sheet页，设置默认单元格宽高
            XSSFSheet sheet = workbook.createSheet();
            sheet.setDefaultColumnWidth(14);//与Excel设置的行高比例约为 1:1.17
            sheet.setDefaultRowHeightInPoints(19);//与Excel设置的行高一致

            /**
             * 大标题 2行15列 索引从0开始
             */
            XSSFRow firstRow = sheet.createRow(0);
            XSSFCell cellTitle = firstRow.createCell(0);
            cellTitle.setCellStyle(style);
            cellTitle.setCellValue(title);
            //创建一个合并单元格 firstRowIndex, lastRowIndex, firstColIndex, lastColIndex 索引从0开始
            sheet.addMergedRegion(new CellRangeAddress(0, 1, 0, 15));

            /**
             * 将图片插入进去
             */
            if(!StringUtils.isEmpty(imgUrl))
                ExoprtExcelUtil.addPictureIntoExcel(imgUrl, sheet, workbook, 3, rowEnd, 0, colEnd);

            /**
             * 填充表格数据
             */
            //填充表头
            if(!StringUtils.isEmpty(thead)) {
                XSSFCellStyle headStyle = ExoprtExcelUtil.getCellStyle(workbook, FillPatternType.SOLID_FOREGROUND, new XSSFColor(java.awt.Color.ORANGE));
                XSSFRow theadRow = sheet.createRow(rowEnd+1);

                XSSFCell cellSeq = theadRow.createCell(0);
                cellSeq.setCellStyle(headStyle);
                cellSeq.setCellValue("序号");

                String[] theads = thead.split(",");
                for (int i = 0; i < theads.length; i++) {
                    XSSFCell cellThead = theadRow.createCell(i + 1);

                    cellThead.setCellStyle(headStyle);
                    cellThead.setCellValue(theads[i]);
                }
            }

            //填充表数据
            if(!StringUtils.isEmpty(tbody)) {
                String[] tbodyRows = tbody.split("#");
                for (int i = 0; i < tbodyRows.length; i++) {
                    XSSFRow row = sheet.createRow(rowEnd+2 + i);
                    String[] tbodyCols = tbodyRows[i].split(",");
                    for (int j = 0; j < tbodyCols.length; j++) {
                        XSSFCell cell = row.createCell(j);
                        cell.setCellStyle(style);
                        cell.setCellValue(tbodyCols[j]);
                    }
                }
            }
            sheet.setColumnWidth(1,25*256);

            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");

            response.setCharacterEncoding("utf-8");
            response.addHeader("Content-Disposition",
                    "attachment; filename=\"" + new String(exportFileName.getBytes("gbk"),"iso8859-1") + "_" + sdf.format(new Date()) + ".xlsx" + "\"");
            response.setContentType("application/vnd.ms-excel" + ";charset=" + "UTF-8");
            OutputStream out = response.getOutputStream();

            // 写入excel文件
            workbook.write(out);
            out.close();
            workbook.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * @param entity 限制条件
     * @return : void
     * @Description: 判断子组大小是否更新
     * @author: ywz
     * @date 2019/8/26 17:54
     * @version 1.0
     */
    private ResponseData checkSubGroup(IRequest request, Entity entity) {

        ResponseData responseData = new ResponseData();
        //校验必输字段是为空
        if(entity.getCeCopy() == null)
        {
            if(entity.getDescription() == null||entity.getControlDate() == null||entity.getEntityStatus() == null
                    ||entity.getChartId() == null||entity.getCeParameterId() == null||entity.getAttachmentGroupId() == null
                    ||entity.getCeParameterId() == null)
            {
                responseData.setMessage("有必填项未输入");
                responseData.setSuccess(false);
                return responseData;
            }
        }

        //  只找更新数据
        if ("update".equals(entity.get__status())) {
            Entity entityRes = new Entity();
            entityRes.setEntityId(entity.getEntityId());
            entityRes = mapper.selectByPrimaryKey(entityRes);
            //判断是否升级版本,升级版本记录历史
            // 附着对象组IDATTACHMENT_GROUP
            //	控制要素组IDCE_GROUP
            //	控制要素IDCE_PARAMETER
            //	控制图IDchart_id
            //	控制时间
            if(!entityRes.getAttachmentGroupId().equals(entity.getAttachmentGroupId())||
                    !entityRes.getCeGroupId().equals(entity.getCeGroupId())||
                    !entityRes.getCeParameterId().equals(entity.getCeParameterId())||
                    !entityRes.getChartId().equals(entity.getChartId())||
                    !entityRes.getControlDate().equals(entity.getControlDate()))
            {
                recordHis(request, entityRes,entity);
            }else if(!entityRes.getDescription().equals(entity.getDescription())||
                    !entityRes.getEntityStatus().equals(entity.getEntityStatus()))
            {
                if(entityRes.getEntityStatus().equals("CLOSED"))
                {
                    entity.setEntityStatus("CLOSED");
                    self().updateByPrimaryKey(request,entity);
                }else {
                    self().updateByPrimaryKey(request,entity);
                }
            }
        }else {
            //新增 校验是否重复
            Entity entityTemp=new Entity();
            entityTemp.setEntityCode(entity.getEntityCode());
            List<Entity> entityList=mapper.select(entityTemp);
            if(entityList.size()>0)
            {
                responseData.setMessage("数据库中已存在该数据，请检查");
                responseData.setSuccess(false);
                return responseData;
            }
        }

        responseData.setSuccess(true);
        return responseData;

    }

    /**
     * @param requestCtx 基本参数
     * @param entity     传入参数
     * @return : void
     * @Description: 历史记录
     * @author: ywz
     * @date 2019/8/26
     * @version 1.0
     */
    private void recordHis(IRequest requestCtx, Entity entity, Entity newEntity) {

        //  复制原有数据
        Entity entityHis = new Entity();
        BeanUtils.copyProperties(entity, entityHis);

        // 删除数据
        self().deleteByPrimaryKey(entityHis);

        // 新增数据 将版本加1
        newEntity.setEntityVersion(String.valueOf(Long.valueOf(entity.getEntityVersion()) + 1));
        newEntity.setEntityStatus("ACTIVE");
        newEntity.setEntityNew("Y");
        self().insertSelective(requestCtx, newEntity);

        // 记录历史
        EntityHis his = new EntityHis();
        BeanUtils.copyProperties(entityHis, his);
        his.setEntityId(entityHis.getEntityId().floatValue());
        his.setTenantId(entityHis.getTenantId().floatValue());
        his.setSiteId(entityHis.getSiteId().floatValue());
        his.setAttachmentGroupId(entityHis.getAttachmentGroupId().floatValue());
        his.setCeParameterId(entityHis.getCeParameterId().floatValue());
        his.setCeGroupId(entityHis.getCeGroupId().floatValue());
        his.setChartId(entityHis.getChartId().floatValue());
        his.setControlDate(entityHis.getControlDate());
        his.setDescription(entityHis.getDescription());
        his.setEntityCode(entityHis.getEntityCode());
        his.setEntityStatus("CLOSED");
        his.setEntityVersion(entityHis.getEntityVersion());
        entityHisService.insertSelective(requestCtx, his);


        //记录pspc_entity_role_relation_his表
        EntityRoleRelation entityRoleRelation = new EntityRoleRelation();
        entityRoleRelation.setEntityCode(entity.getEntityCode());
        entityRoleRelation.setEntityVersion(entity.getEntityVersion());
        List<EntityRoleRelation> entityRoleRelationList = entityRoleRelationService.select(requestCtx,entityRoleRelation,0,0);

        for (int i = 0; i < entityRoleRelationList.size(); i++) {

            entityRoleRelation = new EntityRoleRelation();
            entityRoleRelation.setEntityRoleRelationId(entityRoleRelationList.get(i).getEntityRoleRelationId());
            // 删除数据
            entityRoleRelationService.deleteByPrimaryKey(entityRoleRelation);

            // 记录历史
            EntityRoleRelationHis entityRoleRelationHis = new EntityRoleRelationHis();
            BeanUtils.copyProperties(entityRoleRelationList.get(i), entityRoleRelationHis);
            entityRoleRelationHis.setEntityRoleRelationId(Float.valueOf(entityRoleRelationList.get(i).getEntityRoleRelationId()));
            entityRoleRelationHisService.insertSelective(requestCtx, entityRoleRelationHis);

        }


        //记录pspc_ooc_his表
        Ooc ooc = new Ooc();
        ooc.setEntityCode(entity.getEntityCode());
        ooc.setEntityVersion(entity.getEntityVersion());
        List<Ooc> oocList = oocService.select(requestCtx,ooc,0,0);

        for (int i = 0; i < oocList.size(); i++) {

            ooc = new Ooc();
            ooc.setOocId(oocList.get(i).getOocId());
            // 删除数据
            oocService.deleteByPrimaryKey(ooc);

            // 记录历史
            OocHis oocHis = new OocHis();
            BeanUtils.copyProperties(oocList.get(i), oocHis);
            oocHis.setOocId(oocList.get(i).getOocId());
            oocHisService.insertSelective(requestCtx, oocHis);

        }

        //记录pspc_sample_subgroup_his表
        SampleSubgroup sampleSubgroup = new SampleSubgroup();
        sampleSubgroup.setEntityCode(entity.getEntityCode());
        sampleSubgroup.setEntityVersion(entity.getEntityVersion());
        List<SampleSubgroup> sampleSubgroupList = sampleSubgroupService.select(requestCtx,sampleSubgroup,0,0);

        for (int i = 0; i < sampleSubgroupList.size(); i++) {
            sampleSubgroup = new SampleSubgroup();
            sampleSubgroup.setSampleSubgroupId(sampleSubgroupList.get(i).getSampleSubgroupId());
            // 删除数据
            sampleSubgroupService.deleteByPrimaryKey(sampleSubgroup);

            // 记录历史
            SampleSubgroupHis sampleSubgroupHis = new SampleSubgroupHis();
            BeanUtils.copyProperties(sampleSubgroupList.get(i), sampleSubgroupHis);
            sampleSubgroupHisService.insertSelective(requestCtx, sampleSubgroupHis);

        }

        //记录pspc_sample_subgroup_rel_his表
        SampleSubgroupRel sampleSubgroupRel = new SampleSubgroupRel();
        sampleSubgroupRel.setEntityCode(entity.getEntityCode());
        sampleSubgroupRel.setEntityVersion(entity.getEntityVersion());
        List<SampleSubgroupRel> sampleSubgroupRelList = sampleSubgroupRelService.select(requestCtx,sampleSubgroupRel,0,0);

        for (int i = 0; i < sampleSubgroupRelList.size(); i++) {

            sampleSubgroupRel = new SampleSubgroupRel();
            sampleSubgroupRel.setSampleSubgroupRelationId(sampleSubgroupRelList.get(i).getSampleSubgroupRelationId());

            // 删除数据
            sampleSubgroupRelService.deleteByPrimaryKey(sampleSubgroupRel);

            // 记录历史
            SampleSubgroupRelHis sampleSubgroupRelHis = new SampleSubgroupRelHis();
            BeanUtils.copyProperties(sampleSubgroupRelList.get(i), sampleSubgroupRelHis);
            sampleSubgroupRelHis.setSampleSubgroupRelId(sampleSubgroupRelList.get(i).getSampleSubgroupId());
            sampleSubgroupRelHisService.insertSelective(requestCtx, sampleSubgroupRelHis);
        }

        //记录pspc_subgroup_statistic_his表
        SubgroupStatistic subgroupStatistic = new SubgroupStatistic();
        subgroupStatistic.setEntityCode(entity.getEntityCode());
        subgroupStatistic.setEntityVersion(entity.getEntityVersion());
        List<SubgroupStatistic> subgroupStatisticList = subgroupStatisticService.select(requestCtx,subgroupStatistic,0,0);

        for (int i = 0; i < subgroupStatisticList.size(); i++) {

            subgroupStatistic = new SubgroupStatistic();
            subgroupStatistic.setSubgroupStatisticId(subgroupStatisticList.get(i).getSubgroupStatisticId());
            // 删除数据
            subgroupStatisticService.deleteByPrimaryKey(subgroupStatistic);

            // 记录历史
            SubgroupStatisticHis subgroupStatisticHis = new SubgroupStatisticHis();
            BeanUtils.copyProperties(subgroupStatisticList.get(i), subgroupStatisticHis);
            subgroupStatisticHisService.insertSelective(requestCtx, subgroupStatisticHis);
        }

        //记录pspc_entirety_statistic_his表
        EntiretyStatistic entiretyStatistic = new EntiretyStatistic();
        entiretyStatistic.setEntityCode(entity.getEntityCode());
        entiretyStatistic.setEntityVersion(entity.getEntityVersion());
        List<EntiretyStatistic> entiretyStatisticList = entiretyStatisticService.select(requestCtx,entiretyStatistic,0,0);

        for (int i = 0; i < entiretyStatisticList.size(); i++) {

            entiretyStatistic = new EntiretyStatistic();
            entiretyStatistic.setEntiretyStatisticId(entiretyStatisticList.get(i).getEntiretyStatisticId());
            // 删除数据
            entiretyStatisticService.deleteByPrimaryKey(entiretyStatistic);

            // 记录历史
            EntiretyStatisticHis entiretyStatisticHis = new EntiretyStatisticHis();
            BeanUtils.copyProperties(entiretyStatisticList.get(i), entiretyStatisticHis);
            entiretyStatisticHisService.insertSelective(requestCtx, entiretyStatisticHis);
        }

        //记录pspc_message_his表
        MessageL message = new MessageL();
        message.setEntityCode(entity.getEntityCode());
        message.setEntityVersion(entity.getEntityVersion());
        List<MessageL> messageList = messageService.select(requestCtx,message,0,0);

        for (int i = 0; i < messageList.size(); i++) {

            message = new MessageL();
            message.setMessageId(messageList.get(i).getMessageId());
            // 删除数据
            messageService.deleteByPrimaryKey(message);

            // 记录历史
            MessageHis messageHis = new MessageHis();
            BeanUtils.copyProperties(messageList.get(i), messageHis);
            messageHisService.insertSelective(requestCtx, messageHis);
        }

        //记录pspc_message_detail_his表
        MessageDetail messageDetail = new MessageDetail();
        messageDetail.setEntityCode(entity.getEntityCode());
        messageDetail.setEntityVersion(entity.getEntityVersion());
        List<MessageDetail> messageDetailList = messageDetailService.select(requestCtx,messageDetail,0,0);

        for (int i = 0; i < messageDetailList.size(); i++) {

            messageDetail = new MessageDetail();
            messageDetail.setMessageDetailId(messageDetailList.get(i).getMessageDetailId());
            // 删除数据
            messageDetailService.deleteByPrimaryId(messageDetail);

            // 记录历史
            MessageDetailHis messageDetailHis = new MessageDetailHis();
            BeanUtils.copyProperties(messageDetailList.get(i), messageDetailHis);
            messageDetailHisService.insertSelective(requestCtx, messageDetailHis);
        }

        //记录pspc_count_ooc_his表
        CountOoc countOoc = new CountOoc();
        countOoc.setEntityCode(entity.getEntityCode());
        countOoc.setEntityVersion(entity.getEntityVersion());
        List<CountOoc> countOocList = countOocService.select(requestCtx,countOoc,0,0);

        for (int i = 0; i < countOocList.size(); i++) {

            countOoc = new CountOoc();
            countOoc.setCountOocId(countOocList.get(i).getCountOocId());
            // 删除数据
            countOocService.deleteByPrimaryKey(countOoc);

            // 记录历史
            CountOocHis countOocHis = new CountOocHis();
            BeanUtils.copyProperties(countOocList.get(i), countOocHis);
            countOocHisService.insertSelective(requestCtx, countOocHis);
        }

        //记录pspc_count_statistic_his表
        CountStatistic countStatistic = new CountStatistic();
        countStatistic.setEntityCode(entity.getEntityCode());
        countStatistic.setEntityVersion(entity.getEntityVersion());
        List<CountStatistic> countStatisticList = countStatisticService.select(requestCtx,countStatistic,0,0);

        for (int i = 0; i < countStatisticList.size(); i++) {

            countStatistic = new CountStatistic();
            countStatistic.setCountStatisticId(countStatisticList.get(i).getCountStatisticId());
            // 删除数据
            countStatisticService.deleteByPrimaryKey(countStatistic);

            // 记录历史
            CountStatisticHis countStatisticHis = new CountStatisticHis();
            BeanUtils.copyProperties(countStatisticList.get(i), countStatisticHis);
            countStatisticHisService.insertSelective(requestCtx, countStatisticHis);

        }

    }

}