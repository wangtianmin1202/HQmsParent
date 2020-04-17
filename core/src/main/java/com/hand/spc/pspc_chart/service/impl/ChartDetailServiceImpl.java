package com.hand.spc.pspc_chart.service.impl;

import com.hand.hap.core.IRequest;
import com.hand.hap.system.dto.ResponseData;
import com.hand.hap.system.service.impl.BaseServiceImpl;
import com.hand.spc.pspc_chart.mapper.ChartDetailMapper;
import com.hand.spc.pspc_chart.view.ChartDetailSaveVO;
import com.hand.spc.pspc_judgement_group.dto.JudgementGroup;
import com.hand.spc.pspc_judgement_group.service.IJudgementGroupService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.hand.spc.pspc_chart.dto.ChartDetail;
import com.hand.spc.pspc_chart.service.IChartDetailService;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional(rollbackFor = Exception.class)
public class ChartDetailServiceImpl extends BaseServiceImpl<ChartDetail> implements IChartDetailService {

    @Autowired
    private ChartDetailMapper chartDetailMapper;

    @Autowired
    private IJudgementGroupService judgementGroupService;

    /**
     * @param requestCtx  基本参数
     * @param chartDetail 限制条件
     * @return : com.hand.hap.system.dto.ResponseData
     * @Description: 数据查询
     * @author: ywj
     * @date 2019/8/19 11:01
     * @version 1.0
     */
    @Override
    public ResponseData queryBaseDataByChartId(IRequest requestCtx, ChartDetail chartDetail) {

        // 数据查询
        List<ChartDetail> chartDetailList = chartDetailMapper.select(chartDetail);

        for (ChartDetail detail : chartDetailList) {
            if (detail.getJudgementGroupId() != null) {
                JudgementGroup judgementGroup = new JudgementGroup();
                judgementGroup.setJudgementGroupId(detail.getJudgementGroupId());
                judgementGroup = judgementGroupService.selectByPrimaryKey(requestCtx, judgementGroup);
                detail.setJudgementGroupCode(judgementGroup == null ? "" : judgementGroup.getJudgementGroupCode());
            }
        }

        ResponseData responseData = new ResponseData();
        responseData.setSuccess(true);
        responseData.setRows(chartDetailList);
        return responseData;
    }


    /**
     * @param requestCtx        基本参数
     * @param chartDetailSaveVO 传入参数
     * @return : com.hand.hap.system.dto.ResponseData
     * @Description: 数据保存
     * @author: ywj
     * @date 2019/8/19 14:18
     * @version 1.0
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public ResponseData saveData(IRequest requestCtx, ChartDetailSaveVO chartDetailSaveVO) {

        List<ChartDetail> chartDetailList = new ArrayList<>();

        // 当明细主键为空时，表明新建； 不为空时，表明更新

        // 主要控制图
        ChartDetail chartDetailM = new ChartDetail();
        chartDetailM.setChartDetailType("M");

        chartDetailM.setChartId(chartDetailSaveVO.getChartId());

        chartDetailM.setChartDetailId(chartDetailSaveVO.getChartDetailIdMain());

        if (StringUtils.isNotEmpty(chartDetailSaveVO.getxAxisLabelMain())) {
            chartDetailM.setAxisLabelX(chartDetailSaveVO.getxAxisLabelMain());
        }
        if (StringUtils.isNotEmpty(chartDetailSaveVO.getyAxisLabelMain())) {
            chartDetailM.setAxisLabelY(chartDetailSaveVO.getyAxisLabelMain());
        }

        chartDetailM.setAxisMaxY(chartDetailSaveVO.getyAxisMaxMain());
        chartDetailM.setAxisMinY(chartDetailSaveVO.getyAxisMinMain());

        chartDetailM.setCenterLine(chartDetailSaveVO.getCenterLineMain());
        chartDetailM.setControlLimitUsage(chartDetailSaveVO.getControlLimitUsageMain());
        chartDetailM.setDisplaySpecLimit(chartDetailSaveVO.getDisplaySpecLimitMain());
        chartDetailM.setEnableJudgeGroup(chartDetailSaveVO.getEnableJudgeGroupMain());
        chartDetailM.setJudgementGroupId(chartDetailSaveVO.getJudgementGroupIdMain());
        chartDetailM.setLowerControlLimit(chartDetailSaveVO.getLowerControlLimitMain());
        chartDetailM.setLowerSpecLimit(chartDetailSaveVO.getLowerSpecLimitMain());
        chartDetailM.setSpecTarget(chartDetailSaveVO.getSpecTargetMain());
        chartDetailM.setUpperControlLimit(chartDetailSaveVO.getUpperControlLimitMain());
        chartDetailM.setUpperSpecLimit(chartDetailSaveVO.getUpperSpecLimitMain());

        // 当 控制线选项 为NULL时， 控制上限，中心线，下限 全部设置为空
        if (!"FIXED".equals(chartDetailSaveVO.getControlLimitUsageMain())) {
            chartDetailM.setUpperControlLimit(null);
            chartDetailM.setCenterLine(null);
            chartDetailM.setLowerControlLimit(null);

        }

        // 用于判断更新还是新增
        if (chartDetailM.getChartDetailId() == null) {
            chartDetailM = self().insertSelective(requestCtx, chartDetailM);
        } else {
            chartDetailM.setTenantId(-1F);
            chartDetailM.setSiteId(-1F);
            self().updateByPrimaryKey(requestCtx, chartDetailM);
        }

        // 次要控制图
        ChartDetail chartDetailS = new ChartDetail();
        chartDetailS.setChartDetailType("S");

        chartDetailS.setChartId(chartDetailSaveVO.getChartId());

        chartDetailS.setChartDetailId(chartDetailSaveVO.getChartDetailIdSecond());

        if (StringUtils.isNotEmpty(chartDetailSaveVO.getxAxisLabelSecond())) {
            chartDetailS.setAxisLabelX(chartDetailSaveVO.getxAxisLabelSecond());
        }
        if (StringUtils.isNotEmpty(chartDetailSaveVO.getyAxisLabelMain())) {
            chartDetailS.setAxisLabelY(chartDetailSaveVO.getyAxisLabelSecond());
        }

        chartDetailS.setAxisMaxY(chartDetailSaveVO.getyAxisMaxSecond());
        chartDetailS.setAxisMinY(chartDetailSaveVO.getyAxisMinSecond());

        chartDetailS.setCenterLine(chartDetailSaveVO.getCenterLineSecond());
        chartDetailS.setControlLimitUsage(chartDetailSaveVO.getControlLimitUsageSecond());
        chartDetailS.setDisplaySpecLimit(chartDetailSaveVO.getDisplaySpecLimitSecond());
        chartDetailS.setEnableJudgeGroup(chartDetailSaveVO.getEnableJudgeGroupSecond());
        chartDetailS.setJudgementGroupId(chartDetailSaveVO.getJudgementGroupIdSecond());
        chartDetailS.setLowerControlLimit(chartDetailSaveVO.getLowerControlLimitSecond());
        chartDetailS.setLowerSpecLimit(chartDetailSaveVO.getLowerSpecLimitSecond());
        chartDetailS.setSpecTarget(chartDetailSaveVO.getSpecTargetSecond());
        chartDetailS.setUpperControlLimit(chartDetailSaveVO.getUpperControlLimitSecond());
        chartDetailS.setUpperSpecLimit(chartDetailSaveVO.getUpperSpecLimitSecond());

        // 当 控制线选项 为NULL时， 控制上限，中心线，下限 全部设置为空
        if (!"FIXED".equals(chartDetailSaveVO.getControlLimitUsageSecond())) {
            chartDetailS.setUpperControlLimit(null);
            chartDetailS.setCenterLine(null);
            chartDetailS.setLowerControlLimit(null);
        }

        // 用于判断更新还是新增
        if (chartDetailS.getChartDetailId() == null) {
            chartDetailS = self().insertSelective(requestCtx, chartDetailS);
        } else {
            chartDetailS.setTenantId(-1F);
            chartDetailS.setSiteId(-1F);
            self().updateByPrimaryKey(requestCtx, chartDetailS);
        }

        chartDetailList.add(chartDetailM);
        chartDetailList.add(chartDetailS);

        ResponseData responseData = new ResponseData();
        responseData.setSuccess(true);
        responseData.setRows(chartDetailList);

        return responseData;
    }
}