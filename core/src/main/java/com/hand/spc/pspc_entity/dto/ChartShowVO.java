package com.hand.spc.pspc_entity.dto;

import com.hand.hap.system.dto.BaseDTO;
import com.hand.spc.pspc_chart.dto.ChartDetail;
import com.hand.spc.pspc_classify.dto.Classify;
import com.hand.spc.pspc_count_ooc.dto.CountOoc;
import com.hand.spc.pspc_count_sample_data_class.dto.CountSampleDataClass;
import com.hand.spc.pspc_count_sample_data_extend.dto.CountSampleDataExtend;
import com.hand.spc.pspc_entity.view.AndersonDarlingChartDataVo;
import com.hand.spc.pspc_ooc.dto.Ooc;
import com.hand.spc.pspc_sample_data.dto.SampleData;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @Author han.zhang
 * @Description 图形展示VO
 * @Date 15:52 2019/8/21
 * @Param
 */
public class ChartShowVO extends BaseDTO{
    private Long entityId;
    private Long sampleSubgroupId;
    private String entityCode;
    private Long subgroupNum;
    private String entityVersion;
    private Date sampleSubgroupTime;
    private String oocStatus;
    private Double subgroupBar;

    private Double subgroupR;

    private Double subgroupMax;

    private Long chartId;

    private Double subgroupMin;

    private Double subgroupSigma;

    private Double subgroupMe;

    private Double subgroupRm;

    private ChartDetail chartDetailM;

    private ChartDetail chartDetailS;

    /**
     * 子组大小
     */
    private Long subgroupSize;

    /**
     * 控制图标题
     */
    private String chartTitle;
    /**
     * 样本数量
     */
    private Long sampleCount;
    /**
     * 样本值
     */
    private List<SampleData> sampleDataList;
    /**
     * 主图的UCL值
     */
    private Double uclMValue;
    /**
     * 次图的UCL值
     */
    private Double uclSValue;
    /**
     * 主图的LCL值
     */
    private Double lclMValue;
    /**
     * 次图的LCL值
     */
    private Double lclSValue;
    /**
     * 主图的CL值
     */
    private Double clMValue;
    /**
     * 次图的CL值
     */
    private Double clSValue;
    /**
     * 主图的LSL值
     */
    private Double lslMValue;
    /**
     * 次图的LSL值
     */
    private Double lslSValue;
    /**
     * 主图的USL值
     */
    private Double uslMValue;
    /**
     * 次图的USL值
     */
    private Double uslSValue;

    /**
     * 主图的SL值
     */
    private Double slMValue;
    /**
     * 次图的SL值
     */
    private Double slSValue;
    /**
     * 主图的SIGMA值
     */
    private Double sigmaMValue;
    /**
     * 次图的SIGMA值
     */
    private Double sigmaSValue;

    /**
     * OOC
     */
    private List<Ooc> oocList;

    /**
     * 样本开始时间
     */
    private Date sampleStartTime;

    /**
     * 样本结束时间
     */
    private Date sampleEndTime;
    /**
     * 样本开始时间
     */
    private String sampleStartTimeStr;

    /**
     * 样本结束时间
     */
    private String sampleEndTimeStr;
    /**
     * 图表类型
     */
    private String chartType;

    /**
     * 计数型数据id
     */
    private Long countStatisticId;
    /**
     * 计数型样本时间
     */
    private Date sampleTime;

    private Long countSampleDataId;
    /**
     * 抽检数
     */
    private Long sampleValueCount;
    /**
     * 不合格数
     */
    private Double unqualifiedQuantity;
    /**
     * 计数型不良项
     */
    private List<CountSampleDataClass> sampleDataClassList;

    /**
     * 计数型批次
     */
    private List<CountSampleDataExtend> sampleDataExtendList;

    /**
     * 计数UCL
     */
    private Double upperControlLimit;
    /**
     * 计数CL
     */
    private Double centerLine;
    /**
     * 计数LCL
     */
    private Double lowerControlLimit;
    /**
     * 计数USL
     */
    private Double upperSpecLimit;
    /**
     * 计数SL
     */
    private Double specTarget;
    /**
     * 计数LSL
     */
    private Double lowerSpecLimit;

    /**
     * 不良项信息
     */
    private List<Classify> badItemList;
    /**
     * 批次
     */
    private List<String> batchCodeList;
    /**
     * 计数型样本
     */
    private List<CountOoc> countOocList;
    /**
     * 不合格率
     */
    private Double unqualifiedPercent;

    /**
     * 正态检验横坐标
     */
    private List<BigDecimal> xScotList;

    /**
     * 正态检验纵坐标
     */
    private List<BigDecimal> zScotList;

    /**
     * @Author han.zhang
     * @Description  x z坐标
     * @Date 10:14 2019/9/6
     * @Param
     */
    private List<List<BigDecimal>> xzScots;

    /**
     * @Author han.zhang
     * @Description TODO x'
     * @Date 16:29 2019/9/6
     * @Param
     */
    private List<BigDecimal> convertX;

    /**
     * @Author han.zhang
     * @Description TODO y'
     * @Date 16:29 2019/9/6
     * @Param
     */
    private List<BigDecimal> convertZ;

    /**
     * @Author han.zhang
     * @Description  x' z'坐标
     * @Date 10:14 2019/9/6
     * @Param
     */
    private List<List<BigDecimal>> convertXZ;
    /**
     * x z坐标点
     */
    private List<AndersonDarlingChartDataVo> xzScot;
    /**
     * x' z'坐标点
     */
    private List<AndersonDarlingChartDataVo> convertXzScot;
    /**
     * 平均值
     */
    private Double averageValue;
    /**
     * 标准差
     */
    private Double standardValue;
    /**
     * AD值
     */
    private Double adValue;
    /**
     * P值
     */
    private Double pOfAdValue;

    public Double getpOfAdValue() {
        return pOfAdValue;
    }

    public void setpOfAdValue(Double pOfAdValue) {
        this.pOfAdValue = pOfAdValue;
    }

    public Double getAdValue() {
        return adValue;
    }

    public void setAdValue(Double adValue) {
        this.adValue = adValue;
    }

    public Double getStandardValue() {
        return standardValue;
    }

    public void setStandardValue(Double standardValue) {
        this.standardValue = standardValue;
    }

    public Double getAverageValue() {
        return averageValue;
    }

    public void setAverageValue(Double averageValue) {
        this.averageValue = averageValue;
    }

    public List<BigDecimal> getConvertX() {
        return convertX;
    }

    public void setConvertX(List<BigDecimal> convertX) {
        this.convertX = convertX;
    }

    public List<AndersonDarlingChartDataVo> getXzScot() {
        return xzScot;
    }

    public void setXzScot(List<AndersonDarlingChartDataVo> xzScot) {
        this.xzScot = xzScot;
    }

    public List<AndersonDarlingChartDataVo> getConvertXzScot() {
        return convertXzScot;
    }

    public void setConvertXzScot(List<AndersonDarlingChartDataVo> convertXzScot) {
        this.convertXzScot = convertXzScot;
    }

    public List<BigDecimal> getConvertZ() {
        return convertZ;
    }

    public void setConvertZ(List<BigDecimal> convertZ) {
        this.convertZ = convertZ;
    }

    public List<List<BigDecimal>> getConvertXZ() {
        return convertXZ;
    }

    public void setConvertXZ(List<List<BigDecimal>> convertXZ) {
        this.convertXZ = convertXZ;
    }

    public List<List<BigDecimal>> getXzScots() {
        return xzScots;
    }

    public void setXzScots(List<List<BigDecimal>> xzScots) {
        this.xzScots = xzScots;
    }

    public List<BigDecimal> getxScotList() {
        return xScotList;
    }

    public void setxScotList(List<BigDecimal> xScotList) {
        this.xScotList = xScotList;
    }

    public List<BigDecimal> getzScotList() {
        return zScotList;
    }

    public void setzScotList(List<BigDecimal> zScotList) {
        this.zScotList = zScotList;
    }

    public ChartDetail getChartDetailM() {
        return chartDetailM;
    }

    public void setChartDetailM(ChartDetail chartDetailM) {
        this.chartDetailM = chartDetailM;
    }

    public ChartDetail getChartDetailS() {
        return chartDetailS;
    }

    public void setChartDetailS(ChartDetail chartDetailS) {
        this.chartDetailS = chartDetailS;
    }

    public Double getUnqualifiedPercent() {
        return unqualifiedPercent;
    }

    public void setUnqualifiedPercent(Double unqualifiedPercent) {
        this.unqualifiedPercent = unqualifiedPercent;
    }

    public List<CountOoc> getCountOocList() {
        return countOocList;
    }

    public void setCountOocList(List<CountOoc> countOocList) {
        this.countOocList = countOocList;
    }

    public List<Classify> getBadItemList() {
        return badItemList;
    }

    public void setBadItemList(List<Classify> badItemList) {
        this.badItemList = badItemList;
    }

    public List<String> getBatchCodeList() {
        return batchCodeList;
    }

    public void setBatchCodeList(List<String> batchCodeList) {
        this.batchCodeList = batchCodeList;
    }

    public Double getUpperControlLimit() {
        return upperControlLimit;
    }

    public void setUpperControlLimit(Double upperControlLimit) {
        this.upperControlLimit = upperControlLimit;
    }

    public Double getCenterLine() {
        return centerLine;
    }

    public void setCenterLine(Double centerLine) {
        this.centerLine = centerLine;
    }

    public Double getLowerControlLimit() {
        return lowerControlLimit;
    }

    public void setLowerControlLimit(Double lowerControlLimit) {
        this.lowerControlLimit = lowerControlLimit;
    }

    public Double getUpperSpecLimit() {
        return upperSpecLimit;
    }

    public void setUpperSpecLimit(Double upperSpecLimit) {
        this.upperSpecLimit = upperSpecLimit;
    }

    public Double getSpecTarget() {
        return specTarget;
    }

    public void setSpecTarget(Double specTarget) {
        this.specTarget = specTarget;
    }

    public Double getLowerSpecLimit() {
        return lowerSpecLimit;
    }

    public void setLowerSpecLimit(Double lowerSpecLimit) {
        this.lowerSpecLimit = lowerSpecLimit;
    }

    public List<CountSampleDataExtend> getSampleDataExtendList() {
        return sampleDataExtendList;
    }

    public void setSampleDataExtendList(List<CountSampleDataExtend> sampleDataExtendList) {
        this.sampleDataExtendList = sampleDataExtendList;
    }

    public List<CountSampleDataClass> getSampleDataClassList() {
        return sampleDataClassList;
    }


    public void setSampleDataClassList(List<CountSampleDataClass> sampleDataClassList) {
        this.sampleDataClassList = sampleDataClassList;
    }

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    public Long getSampleSubgroupId() {
        return sampleSubgroupId;
    }

    public void setSampleSubgroupId(Long sampleSubgroupId) {
        this.sampleSubgroupId = sampleSubgroupId;
    }

    public String getEntityCode() {
        return entityCode;
    }

    public void setEntityCode(String entityCode) {
        this.entityCode = entityCode;
    }

    public Long getSubgroupNum() {
        return subgroupNum;
    }

    public void setSubgroupNum(Long subgroupNum) {
        this.subgroupNum = subgroupNum;
    }

    public String getEntityVersion() {
        return entityVersion;
    }

    public void setEntityVersion(String entityVersion) {
        this.entityVersion = entityVersion;
    }

    public Date getSampleSubgroupTime() {
        return sampleSubgroupTime;
    }

    public void setSampleSubgroupTime(Date sampleSubgroupTime) {
        this.sampleSubgroupTime = sampleSubgroupTime;
    }

    public String getOocStatus() {
        return oocStatus;
    }

    public void setOocStatus(String oocStatus) {
        this.oocStatus = oocStatus;
    }

    public Double getSubgroupBar() {
        return subgroupBar;
    }

    public void setSubgroupBar(Double subgroupBar) {
        this.subgroupBar = subgroupBar;
    }

    public Double getSubgroupR() {
        return subgroupR;
    }

    public void setSubgroupR(Double subgroupR) {
        this.subgroupR = subgroupR;
    }

    public Double getSubgroupMax() {
        return subgroupMax;
    }

    public void setSubgroupMax(Double subgroupMax) {
        this.subgroupMax = subgroupMax;
    }

    public Double getSubgroupMin() {
        return subgroupMin;
    }

    public void setSubgroupMin(Double subgroupMin) {
        this.subgroupMin = subgroupMin;
    }

    public Long getSampleValueCount() {
        return sampleValueCount;
    }

    public void setSampleValueCount(Long sampleValueCount) {
        this.sampleValueCount = sampleValueCount;
    }

    public Double getUnqualifiedQuantity() {
        return unqualifiedQuantity;
    }

    public void setUnqualifiedQuantity(Double unqualifiedQuantity) {
        this.unqualifiedQuantity = unqualifiedQuantity;
    }

    public Double getSubgroupSigma() {
        return subgroupSigma;
    }

    public void setSubgroupSigma(Double subgroupSigma) {
        this.subgroupSigma = subgroupSigma;
    }

    public Double getSubgroupMe() {
        return subgroupMe;
    }

    public void setSubgroupMe(Double subgroupMe) {
        this.subgroupMe = subgroupMe;
    }

    public Double getSubgroupRm() {
        return subgroupRm;
    }

    public void setSubgroupRm(Double subgroupRm) {
        this.subgroupRm = subgroupRm;
    }

    public List<SampleData> getSampleDataList() {
        return sampleDataList;
    }

    public void setSampleDataList(List<SampleData> sampleDataList) {
        this.sampleDataList = sampleDataList;
    }

    public Long getSampleCount() {
        return sampleCount;
    }

    public void setSampleCount(Long sampleCount) {
        this.sampleCount = sampleCount;
    }

    public Double getUclMValue() {
        return uclMValue;
    }

    public void setUclMValue(Double uclMValue) {
        this.uclMValue = uclMValue;
    }

    public Double getUclSValue() {
        return uclSValue;
    }

    public void setUclSValue(Double uclSValue) {
        this.uclSValue = uclSValue;
    }

    public Double getLclMValue() {
        return lclMValue;
    }

    public void setLclMValue(Double lclMValue) {
        this.lclMValue = lclMValue;
    }

    public Double getLclSValue() {
        return lclSValue;
    }

    public void setLclSValue(Double lclSValue) {
        this.lclSValue = lclSValue;
    }

    public Double getClMValue() {
        return clMValue;
    }

    public void setClMValue(Double clMValue) {
        this.clMValue = clMValue;
    }

    public Double getClSValue() {
        return clSValue;
    }

    public void setClSValue(Double clSValue) {
        this.clSValue = clSValue;
    }

    public Double getLslMValue() {
        return lslMValue;
    }

    public void setLslMValue(Double lslMValue) {
        this.lslMValue = lslMValue;
    }

    public Double getLslSValue() {
        return lslSValue;
    }

    public void setLslSValue(Double lslSValue) {
        this.lslSValue = lslSValue;
    }

    public Double getUslMValue() {
        return uslMValue;
    }

    public void setUslMValue(Double uslMValue) {
        this.uslMValue = uslMValue;
    }

    public Double getUslSValue() {
        return uslSValue;
    }

    public void setUslSValue(Double uslSValue) {
        this.uslSValue = uslSValue;
    }

    public Long getChartId() {
        return chartId;
    }

    public void setChartId(Long chartId) {
        this.chartId = chartId;
    }

    public Double getSlMValue() {
        return slMValue;
    }

    public void setSlMValue(Double slMValue) {
        this.slMValue = slMValue;
    }

    public Double getSlSValue() {
        return slSValue;
    }

    public void setSlSValue(Double slSValue) {
        this.slSValue = slSValue;
    }

    public Double getSigmaMValue() {
        return sigmaMValue;
    }

    public void setSigmaMValue(Double sigmaMValue) {
        this.sigmaMValue = sigmaMValue;
    }

    public Double getSigmaSValue() {
        return sigmaSValue;
    }

    public void setSigmaSValue(Double sigmaSValue) {
        this.sigmaSValue = sigmaSValue;
    }

    public String getChartTitle() {
        return chartTitle;
    }

    public void setChartTitle(String chartTitle) {
        this.chartTitle = chartTitle;
    }

    public Long getSubgroupSize() {
        return subgroupSize;
    }

    public void setSubgroupSize(Long subgroupSize) {
        this.subgroupSize = subgroupSize;
    }

    public List<Ooc> getOocList() {
        return oocList;
    }

    public void setOocList(List<Ooc> oocList) {
        this.oocList = oocList;
    }

    public Date getSampleStartTime() {
        return sampleStartTime;
    }

    public void setSampleStartTime(Date sampleStartTime) {
        this.sampleStartTime = sampleStartTime;
    }

    public Date getSampleEndTime() {
        return sampleEndTime;
    }

    public void setSampleEndTime(Date sampleEndTime) {
        this.sampleEndTime = sampleEndTime;
    }

    public String getChartType() {
        return chartType;
    }

    public void setChartType(String chartType) {
        this.chartType = chartType;
    }

    public Long getCountStatisticId() {
        return countStatisticId;
    }

    public void setCountStatisticId(Long countStatisticId) {
        this.countStatisticId = countStatisticId;
    }

    public Date getSampleTime() {
        return sampleTime;
    }

    public void setSampleTime(Date sampleTime) {
        this.sampleTime = sampleTime;
    }

    public Long getCountSampleDataId() {
        return countSampleDataId;
    }

    public void setCountSampleDataId(Long countSampleDataId) {
        this.countSampleDataId = countSampleDataId;
    }

    public String getSampleStartTimeStr() {
        return sampleStartTimeStr;
    }

    public void setSampleStartTimeStr(String sampleStartTimeStr) {
        this.sampleStartTimeStr = sampleStartTimeStr;
    }

    public String getSampleEndTimeStr() {
        return sampleEndTimeStr;
    }

    public void setSampleEndTimeStr(String sampleEndTimeStr) {
        this.sampleEndTimeStr = sampleEndTimeStr;
    }
}
