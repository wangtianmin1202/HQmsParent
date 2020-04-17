package com.hand.spc.pspc_count_ooc.service.impl;

import com.hand.hap.core.IRequest;
import com.hand.hap.system.service.impl.BaseServiceImpl;
import com.hand.spc.constants.SpcConstants;
import com.hand.spc.pspc_chart.dto.Chart;
import com.hand.spc.pspc_chart.dto.ChartDetail;
import com.hand.spc.pspc_chart.mapper.ChartDetailMapper;
import com.hand.spc.pspc_chart.mapper.ChartMapper;
import com.hand.spc.pspc_count_ooc.mapper.CountOocMapper;
import com.hand.spc.pspc_count_statistic.dto.CountStatistic;
import com.hand.spc.pspc_count_statistic.mapper.CountStatisticMapper;
import com.hand.spc.pspc_entirety_statistic.dto.EntiretyStatistic;
import com.hand.spc.pspc_entirety_statistic.mapper.EntiretyStatisticMapper;
import com.hand.spc.pspc_ooc.view.OocReportVO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.hand.spc.pspc_count_ooc.dto.CountOoc;
import com.hand.spc.pspc_count_ooc.service.ICountOocService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.List;

@Service
@Transactional(rollbackFor = Exception.class)
public class CountOocServiceImpl extends BaseServiceImpl<CountOoc> implements ICountOocService,SpcConstants {

    @Autowired
    private CountOocMapper countOocMapper;
    @Autowired
    private ChartMapper chartMapper;
    @Autowired
    private ChartDetailMapper chartDetailMapper;
    @Autowired
    private CountStatisticMapper countStatisticMapper;

    /**
     *
     * @Description OOC报表查询
     *
     * @author yuchao.wang
     * @date 2019/8/29 22:10
     * @param requestContext
     * @param dto
     * @param page
     * @param pageSize
     * @return java.util.List<com.hand.spc.pspc_ooc.view.OocReportVO>
     *
     */
    @Override
    public List<OocReportVO> queryOocReport(IRequest requestContext, OocReportVO dto, int page, int pageSize) {
        //分页查询
        //PageHelper.startPage(page, pageSize);
        List<OocReportVO> oocReportVOList = countOocMapper.queryOocReport(dto);

        return oocReportVOList;
    }

    @Override
    public List<CountOoc> updateAndSave(IRequest requestCtx, List<CountOoc> dtos) {
        dtos.forEach(dto -> {
            if(dto.get__status().equals(ADD.toLowerCase())){
                Chart chart = new Chart();
                ChartDetail chartDetail = new ChartDetail();
                CountStatistic countStatistic = new CountStatistic();

                Long chartId = dto.getChartId();
                chart.setChartId(Float.valueOf(chartId));
                Chart chart1 = chartMapper.selectByPrimaryKey(chart);
                if(null == chart1){
                    throw new RuntimeException("未在chart表找到数据");
                }
                chartDetail.setChartId(Float.valueOf(dto.getChartId()));
                List<ChartDetail> chartDetails = chartDetailMapper.select(chartDetail);
                if(CollectionUtils.isEmpty(chartDetails)){
                    throw new RuntimeException("未在PSPC_CHART_DETAIL表找到数据");
                }

                countStatistic.setCountSampleDataId(dto.getCountSampleDataId());
                countStatistic.setEntityCode(dto.getEntityCode());
                countStatistic.setEntityVersion(dto.getEntityVersion());
                List<CountStatistic> countStatistics = countStatisticMapper.select(countStatistic);
                if(CollectionUtils.isEmpty(countStatistics)){
                    throw new RuntimeException("未在PSPC_COUNT_STATISTIC表找到数据");
                }
                dto.setCountSampleDataId(dto.getCountSampleDataId());
                dto.setMaxPlotPoints(chart1.getMaxPlotPoints());
                dto.setTickLabelX(chart1.getTickLabelX());
                dto.setAxisLabelX(chartDetails.get(0).getAxisLabelX());
                dto.setAxisLabelY(chartDetails.get(0).getAxisLabelY());
                dto.setChartDetailType(chartDetails.get(0).getChartDetailType());
                dto.setUpperControlLimit(countStatistics.get(0).getUpperControlLimit());
                dto.setCenterLine(countStatistics.get(0).getCenterLine());
                dto.setLowerControlLimit(countStatistics.get(0).getLowerControlLimit());
                dto.setUpperSpecLimit(countStatistics.get(0).getUpperSpecLimit());
                dto.setLowerSpecLimit(countStatistics.get(0).getLowerSpecLimit());
            }else{
                if(null == dto.getClassifyGroupId() && null == dto.getClassifyId() && StringUtils.isEmpty(dto.getRemark())){
                    dto.setOocStatus(UNPROCESSED);
                }else{
                    dto.setOocStatus(PROCESSED);
                }
            }
        });
        return self().batchUpdate(requestCtx,dtos);
    }

    @Override
    public List<CountOoc> selectCountOoc(IRequest requestContext, CountOoc dto) {
        return countOocMapper.selectCountOocJudge(dto);
    }
}