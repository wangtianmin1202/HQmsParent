package com.hand.spc.pspc_cpk_analysis.service.impl;


import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.hand.spc.constants.SpcConstants;
import com.hand.spc.pspc_chart.dto.Chart;
import com.hand.spc.pspc_chart.dto.ChartDetail;
import com.hand.spc.pspc_chart.mapper.ChartDetailMapper;
import com.hand.spc.pspc_chart.mapper.ChartMapper;
import com.hand.spc.pspc_cpk_analysis.dto.CPKAnalyseChartDataVo;
import com.hand.spc.pspc_cpk_analysis.dto.CPKAnalyseChartVo;
import com.hand.spc.pspc_cpk_analysis.dto.CPKAnalyseReqDTO;
import com.hand.spc.pspc_cpk_analysis.dto.CPKAnalyseResDTO;
import com.hand.spc.pspc_cpk_analysis.dto.CPKAnalyseTableDataVo;
import com.hand.spc.pspc_cpk_analysis.dto.CPKAnalyseTableVo;
import com.hand.spc.pspc_cpk_analysis.dto.OverallAbilityVo;
import com.hand.spc.pspc_cpk_analysis.dto.PotentialAbilityVo;
import com.hand.spc.pspc_cpk_analysis.dto.ProcessDataVo;
import com.hand.spc.pspc_cpk_analysis.service.CPKAnalyseService;
import com.hand.spc.pspc_entirety_statistic.dto.EntiretyStatistic;
import com.hand.spc.pspc_entirety_statistic.mapper.EntiretyStatisticMapper;
import com.hand.spc.pspc_entity.dto.Entity;
import com.hand.spc.pspc_entity.mapper.EntityMapper;
import com.hand.spc.pspc_sample_subgroup.dto.SampleSubgroup;
import com.hand.spc.pspc_sample_subgroup.mapper.SampleSubgroupMapper;
import com.hand.spc.pspc_sample_subgroup_rel.dto.SampleSubgroupRel;
import com.hand.spc.pspc_sample_subgroup_rel.mapper.SampleSubgroupRelMapper;
import com.hand.spc.pspc_subgroup_statistic.mapper.SubgroupStatisticMapper;
import com.hand.spc.pspc_subgroup_statistic.vo.SubgroupStatisticVo;

@Service
public class CPKAnalyseServiceImpl implements CPKAnalyseService,SpcConstants {

    private Logger logger = LoggerFactory.getLogger(CPKAnalyseServiceImpl.class);
    @Autowired
    private EntityMapper entityMapper;
    @Autowired
    private ChartMapper chartMapper;
    @Autowired
    private ChartDetailMapper chartDetailMapper;
    @Autowired
    private SampleSubgroupMapper sampleSubgroupMapper;
    @Autowired
    private SampleSubgroupRelMapper sampleSubgroupRelMapper;
    @Autowired
    private EntiretyStatisticMapper entiretyStatisticMapper;
    @Autowired
    private SubgroupStatisticMapper subgroupStatisticMapper;

    @Override
    public CPKAnalyseResDTO listCPK(CPKAnalyseReqDTO requestDTO) {
        CPKAnalyseResDTO cpkAnalyseResDTO =new CPKAnalyseResDTO();

        ChartDetail chartDetail =new ChartDetail();

        //规格下限
        BigDecimal lowerSpecLimit =new BigDecimal(0);
        //规格上线
        BigDecimal upperSpecLimit = new BigDecimal(0);
        //过程数据中的目标
        BigDecimal specTarget = BigDecimal.ZERO;

        Entity entity =new Entity();
        entity.setEntityCode(requestDTO.getEntityCode());
        entity.setEntityVersion(requestDTO.getEntityVersion());

        entity = entityMapper.selectOne(entity);
        if(entity!=null){

            if(requestDTO.getEndDate()==null&&requestDTO.getStartDate()==null){
                Chart chart =new Chart();
                chart.setChartId(Float.valueOf(entity.getChartId()));
                chart =chartMapper.selectByPrimaryKey(chart);

                String type =chart.getChartType();
                if(type.equals("nP")||type.equals("P")||type.equals("C")||type.equals("U")){
                    throw new RuntimeException("pspc.error.cpkanalyse.chartType.null");
                }
                requestDTO.setSize(chart.getMaxPlotPoints().longValue());
            }

            ChartDetail chartDetail1 =new ChartDetail();
            chartDetail1.setChartId(Float.valueOf(entity.getChartId()));
            chartDetail1.setChartDetailType("M");
                chartDetail =chartDetailMapper.selectOne(chartDetail1);
                if(chartDetail!=null){
                    if(null == chartDetail.getLowerSpecLimit()){
                        throw new RuntimeException("规格下限为空");
                    }
                    lowerSpecLimit = BigDecimal.valueOf(chartDetail.getLowerSpecLimit());
                    if(null == chartDetail.getLowerSpecLimit()){
                        throw new RuntimeException("规格上限为空");
                    }
                    upperSpecLimit = BigDecimal.valueOf(chartDetail.getUpperSpecLimit());
                    if(null == chartDetail.getLowerSpecLimit()){
                        throw new RuntimeException("目标为空");
                    }
                    specTarget = BigDecimal.valueOf(chartDetail.getSpecTarget());
                    cpkAnalyseResDTO.setLowerSpecLimit(lowerSpecLimit);
                    cpkAnalyseResDTO.setUpperSpecLimit(upperSpecLimit);
            }
        }else {
            //实体控制图不存在
            throw new RuntimeException("pspc.error.entity.notexists");
        }

        List<SampleSubgroup> sampleSubgroupList =new ArrayList<>();
        sampleSubgroupList =sampleSubgroupMapper.querySampleSubgroupByCPK(requestDTO);
        if(sampleSubgroupList.size()==0){
            throw new RuntimeException("没有查询到该实体控制图对应的数据！");
        }

        //1.	过程数据计算
        ProcessDataVo processDataVo =new ProcessDataVo();
        processDataVo =getProcessData(sampleSubgroupList,requestDTO,specTarget,upperSpecLimit,lowerSpecLimit);
        cpkAnalyseResDTO.setProcessDataVo(processDataVo);

        //2.	整体能力计算
        OverallAbilityVo overallAbilityVo =new OverallAbilityVo();
        overallAbilityVo =this.getOverallAbility(processDataVo);
        cpkAnalyseResDTO.setOverallAbilityVo(overallAbilityVo);

        //3.	潜在（组内）能力
        PotentialAbilityVo potentialAbilityVo =new PotentialAbilityVo();
        potentialAbilityVo =this.getPotentialAbility(processDataVo);
        cpkAnalyseResDTO.setPotentialAbilityVo(potentialAbilityVo);

        //4 辅助图形
        CPKAnalyseChartVo cpkAnalyseChartVo =new CPKAnalyseChartVo();
        cpkAnalyseChartVo =this.getCPKChart(processDataVo);
        cpkAnalyseResDTO.setCpkAnalyseChartVo(cpkAnalyseChartVo);

        //5  表格展示
        CPKAnalyseTableVo cpkAnalyseTableVo =new CPKAnalyseTableVo();
        cpkAnalyseTableVo =this.getCPKTable(processDataVo);
        cpkAnalyseResDTO.setCpkAnalyseTableVo(cpkAnalyseTableVo);

        processDataVo.setCpkAnalyseTableVo(null);

        //将计算出来的数据转换成echarts所需的集合格式
        setChartData(cpkAnalyseResDTO,cpkAnalyseChartVo);
        return cpkAnalyseResDTO;
    }

    //5   列表内容
    private CPKAnalyseTableVo getCPKTable(ProcessDataVo processDataVo) {
       return processDataVo.getCpkAnalyseTableVo();
    }

    //4  条形图内容
    CPKAnalyseChartVo getCPKChart(ProcessDataVo processDataVo){
        CPKAnalyseChartVo cpkAnalyseChartVo = new CPKAnalyseChartVo();

        BigDecimal upperSpecLimit =processDataVo.getUpperSpecLimit();
        BigDecimal lowerControlLimit =processDataVo.getLowerControlLimit();

        //1 柱子个数 n = √N+1
        BigDecimal samplen = new BigDecimal(0);
        BigDecimal 	sampleN =processDataVo.getSampleN();
        double d =Math.sqrt(sampleN.doubleValue());
        samplen =(BigDecimal.valueOf(d)).add(BigDecimal.valueOf(1)).setScale(0,RoundingMode.HALF_UP);
        cpkAnalyseChartVo.setSamplen(samplen);


        //2.	计算直方图组距i = （MAX(USL,X_max ) - MIN⁡(LSL,X_min )）÷ √N
        BigDecimal distanceI =new BigDecimal(0);

        List<BigDecimal> sampleValues =new ArrayList<>(50);
        sampleValues.addAll(processDataVo.getSampleValues());

        BigDecimal maxSampleValue =new BigDecimal(0);
        BigDecimal minSampleValue =new BigDecimal(0);

        if(!CollectionUtils.isEmpty(sampleValues)){
            maxSampleValue =Collections.max(sampleValues);
            minSampleValue =Collections.min(sampleValues);
        }
        distanceI =(maxSampleValue.max(upperSpecLimit).subtract(minSampleValue.min(lowerControlLimit))).divide(BigDecimal.valueOf(d),2,RoundingMode.HALF_UP);
        cpkAnalyseChartVo.setDistanceI(distanceI);

        //3.	计算样本频数
        List<CPKAnalyseChartDataVo> cpkAnalyseChartDataVoList =new ArrayList<CPKAnalyseChartDataVo>(samplen.intValue());

        //起始点0
        BigDecimal X0 =new BigDecimal(minSampleValue.min(lowerControlLimit).doubleValue()).setScale(4,RoundingMode.HALF_UP);
//        X0 =X0.add(distanceI).divide(BigDecimal.valueOf(2)).subtract(distanceI);

        BigDecimal leftInterval =new BigDecimal(0);
        BigDecimal rightInterval =new BigDecimal(0);

        leftInterval =leftInterval.subtract(distanceI.divide(BigDecimal.valueOf(2))).add(X0);
        rightInterval =rightInterval.add(distanceI.divide(BigDecimal.valueOf(2))).add(X0);


        int samplenIntValue =samplen.intValue();
        for(int i = 0;i<samplenIntValue;i++){

            CPKAnalyseChartDataVo cpkAnalyseChartDataVo =new CPKAnalyseChartDataVo();

            X0 =X0.add(distanceI);
            cpkAnalyseChartDataVo.setxCoordinate(X0);

            BigDecimal F =new BigDecimal(0);

            leftInterval =leftInterval.add(distanceI);
            rightInterval =rightInterval.add(distanceI);

            for(BigDecimal sampleValue: sampleValues){
                if(sampleValue.compareTo(rightInterval)==-1 && sampleValue.compareTo(leftInterval)==1){
                    F =F.add(BigDecimal.valueOf(1));
                }
            }
            cpkAnalyseChartDataVo.setyStripGraph(F);
            cpkAnalyseChartDataVo.setyDottedGraph(this.calculatedProbability(processDataVo.getStandardDeviationIn(),processDataVo.getSampleMean(),X0));
            cpkAnalyseChartDataVo.setySolidGraph(this.calculatedProbability(processDataVo.getStandardDeviationOver(),processDataVo.getSampleMean(),X0));

            cpkAnalyseChartDataVoList.add(cpkAnalyseChartDataVo);
        }
        cpkAnalyseChartVo.setCpkAnalyseChartDataVo(cpkAnalyseChartDataVoList);

        cpkAnalyseChartVo.setEntiretyCl(processDataVo.getCpkAnalyseChartVo().getEntiretyCl());
        cpkAnalyseChartVo.setEntiretyLcl(processDataVo.getCpkAnalyseChartVo().getEntiretyLcl());
        cpkAnalyseChartVo.setEntiretyUcl(processDataVo.getCpkAnalyseChartVo().getEntiretyUcl());


        return cpkAnalyseChartVo;
    }

    //1  过程数据
    ProcessDataVo getProcessData(List<SampleSubgroup> sampleSubgroupList,CPKAnalyseReqDTO requestDTO,BigDecimal specTarget,BigDecimal upperSpecLimit,BigDecimal lowerControlLimit){
        ProcessDataVo processDataVo =new ProcessDataVo();
        CPKAnalyseTableVo cpkAnalyseTableVo =new CPKAnalyseTableVo();

        long start1 =System.currentTimeMillis();

        BigDecimal sampleValues =new BigDecimal(0);
        int sizeN =0;
        BigDecimal standardDeviationOver =new BigDecimal(0);
        List<BigDecimal> SsampleValues =new ArrayList<>(50);

        List<SampleSubgroupRel> sampleSubgroupRelationList =sampleSubgroupRelMapper.selectBySampleSubgroup(requestDTO);
        if(!(sampleSubgroupRelationList==null||sampleSubgroupRelationList.size()==0)){
            for(SampleSubgroupRel subgroupRelation:sampleSubgroupRelationList){
                BigDecimal sampleValue = BigDecimal.valueOf(subgroupRelation.getSampleValue());
                sampleValues = sampleValues.add(sampleValue);
                SsampleValues.add(sampleValue);
                sizeN++;
            }
        }else {
            throw new RuntimeException("pspc.error.cpkanalyse.sampleSubgroupRelation.null");
        }

        SampleSubgroup sampleSubgroup =new SampleSubgroup();
        sampleSubgroup.setEntityVersion(requestDTO.getEntityVersion());
        sampleSubgroup.setEntityCode(requestDTO.getEntityCode());
        sampleSubgroup.setTenantId(requestDTO.getTenantId());
        sampleSubgroup.setSiteId(requestDTO.getSiteId());
        EntiretyStatistic entiretyStatistic =entiretyStatisticMapper.selectByMaxNum(sampleSubgroup);
        CPKAnalyseChartVo cpkAnalyseChartVo =new CPKAnalyseChartVo();
        if(entiretyStatistic!=null){
            cpkAnalyseChartVo.setEntiretyCl(BigDecimal.valueOf(entiretyStatistic.getEntiretyCl()));
            cpkAnalyseChartVo.setEntiretyLcl(BigDecimal.valueOf(entiretyStatistic.getEntiretyLcl()));
            cpkAnalyseChartVo.setEntiretyUcl(BigDecimal.valueOf(entiretyStatistic.getEntiretyUcl()));
        }
        processDataVo.setCpkAnalyseChartVo(cpkAnalyseChartVo);

        processDataVo.setLowerControlLimit(lowerControlLimit);
        processDataVo.setUpperSpecLimit(upperSpecLimit);

        //样本值list  4 辅助图形—CPK分析图图形展示  用到
        processDataVo.setSampleValues(SsampleValues);

        //	样本N
        processDataVo.setSampleN(BigDecimal.valueOf(sizeN));
        //	目标
        processDataVo.setSpecTarget(specTarget);

        //样本平均数N μ=∑_(ⅈ=1)^n▒x_i/N
        BigDecimal sampleMean = sampleValues.divide(BigDecimal.valueOf(sizeN),4,RoundingMode.HALF_UP);
        processDataVo.setSampleMean(sampleMean);

        //标准差（整体）S S=√(1/(n-1) ∑_(i=1)^n▒(x_i-μ)^2 )
        BigDecimal Spow =new BigDecimal(0);
        for(BigDecimal s:SsampleValues){
            Spow =Spow.add((s.subtract(sampleMean)).pow(2));
        }
        Spow =Spow.divide(BigDecimal.valueOf(sizeN-1),4, BigDecimal.ROUND_HALF_UP);
        Double mathS =Math.sqrt(Spow.doubleValue());
        standardDeviationOver = BigDecimal.valueOf(mathS).setScale(4,RoundingMode.HALF_UP);
        processDataVo.setStandardDeviationOver(standardDeviationOver);

        //	标准差（组内）σ  σ=√((∑_(j=1)^k▒〖S_i〗^2 )/k)
        BigDecimal 	standardDeviationIn  = new BigDecimal(0);//标准差
        BigDecimal subgroupSigma = new BigDecimal(0);
        BigDecimal subgroupSigRm =BigDecimal.ZERO;
        Double mathStandardDeviationIn =new Double(0);

        List<CPKAnalyseTableDataVo> cpkAnalyseTableDataVoList =new ArrayList<>(sampleSubgroupList.size());

        //给前端传的序号
        int j=1;
        logger.info("***********************0.10查询开始");

        long start2 =System.currentTimeMillis();

        //样本组里最大的样本数
        int sampleMaxCount = 0;
        if(!CollectionUtils.isEmpty(sampleSubgroupList)){

            List<SubgroupStatisticVo> subgroupStatisticList =
                        subgroupStatisticMapper.selectByCodeAndVersion(requestDTO);

            logger.info("*****************************0.8查询时间:"+(System.currentTimeMillis()-start2));

            logger.info("***********************循环开始");

            logger.info("*****************************0.10查询时间:"+(System.currentTimeMillis()-start2));
            logger.info("***********************0.10循环开始");

            long start3 =System.currentTimeMillis();
            int isnotnull =0;
            for(SubgroupStatisticVo subgroupStatisticVo :subgroupStatisticList){
                List<SampleSubgroupRel> sampleSubgroupRelations =new ArrayList<>();
                Long statisticGroupId = subgroupStatisticVo.getSampleSubgroupId();
                for(SampleSubgroupRel subgroupRelation:sampleSubgroupRelationList){
                    if(subgroupRelation.getSampleSubgroupId().equals(Float.valueOf(statisticGroupId))){
                        sampleSubgroupRelations.add(subgroupRelation);
                    }
                }
                subgroupStatisticVo.setSampleSubgroupRelationList(sampleSubgroupRelations);

                if(sampleSubgroupRelations.size() > sampleMaxCount){
                    sampleMaxCount = sampleSubgroupRelations.size();
                }

                if(sampleSubgroupList.get(0).getSubgroupSize()!=1){
                    subgroupSigma =subgroupSigma.add(subgroupStatisticVo.getSubgroupSigma().pow(2));
                }else {
                    if(subgroupStatisticVo.getSubgroupRm()!=null){
                        isnotnull++;
                        subgroupSigRm =subgroupSigRm.add(subgroupStatisticVo.getSubgroupRm());
                    }
                }

                CPKAnalyseTableDataVo cpkAnalyseTableDataVo =new CPKAnalyseTableDataVo();

                cpkAnalyseTableDataVo.setSerialNumber(j++);
                cpkAnalyseTableDataVo.setSubgroupBar(subgroupStatisticVo.getSubgroupBar());
                cpkAnalyseTableDataVo.setSubgroupMax(subgroupStatisticVo.getSubgroupMax());
                cpkAnalyseTableDataVo.setSubgroupMe(subgroupStatisticVo.getSubgroupMe());
                cpkAnalyseTableDataVo.setSubgroupMin(subgroupStatisticVo.getSubgroupMin());
                cpkAnalyseTableDataVo.setSubgroupR(subgroupStatisticVo.getSubgroupR());
                cpkAnalyseTableDataVo.setSubgroupSigma(subgroupStatisticVo.getSubgroupSigma());
                cpkAnalyseTableDataVo.setSampleTime(subgroupStatisticVo.getSampleSubgroupTime());

                List<SampleSubgroupRel> subgroupRelationList =subgroupStatisticVo.getSampleSubgroupRelationList();
                List<BigDecimal> samplValues =new ArrayList<>();
                if(!CollectionUtils.isEmpty(subgroupRelationList)){
                    for(SampleSubgroupRel subgroupRelation1:subgroupRelationList){
                        samplValues.add(BigDecimal.valueOf(subgroupRelation1.getSampleValue()));
                    }
                }
                cpkAnalyseTableDataVo.setSampleValueList(samplValues);

                cpkAnalyseTableDataVoList.add(cpkAnalyseTableDataVo);
            }
            logger.info("*****************************0.10循环时间:"+(System.currentTimeMillis()-start3));
            cpkAnalyseTableVo.setCpkAnalyseTableDataVoList(cpkAnalyseTableDataVoList);

            if(sampleSubgroupList.get(0).getSubgroupSize()!=1){
                mathStandardDeviationIn =Math.sqrt(subgroupSigma.divide((BigDecimal.valueOf(sampleSubgroupList.size())),4,RoundingMode.HALF_UP).doubleValue());
            }else {
                if(isnotnull==sampleSubgroupList.size()){
                    mathStandardDeviationIn =subgroupSigRm.divide(BigDecimal.valueOf(sampleSubgroupList.size()-1),4,RoundingMode.HALF_UP).
                            divide(BigDecimal.valueOf(1.128),4,RoundingMode.HALF_UP).doubleValue();
                }else {
                    mathStandardDeviationIn =subgroupSigRm.divide(BigDecimal.valueOf(sampleSubgroupList.size()-2),4,RoundingMode.HALF_UP).
                            divide(BigDecimal.valueOf(1.128),4,RoundingMode.HALF_UP).doubleValue();
                }

            }
        }

        standardDeviationIn =BigDecimal.valueOf(mathStandardDeviationIn).setScale(4,RoundingMode.HALF_UP);
        processDataVo.setStandardDeviationIn(standardDeviationIn);
        processDataVo.setCpkAnalyseTableVo(cpkAnalyseTableVo);
        processDataVo.setMaxSampleCount(sampleMaxCount);
        return processDataVo;
    }

    //2 整体能力
    OverallAbilityVo getOverallAbility(ProcessDataVo processDataVo){
        OverallAbilityVo overallAbilityVo =new OverallAbilityVo();

        //	PP过程能力指数
        BigDecimal PP =new BigDecimal(0);
        BigDecimal upperSpecLimit =processDataVo.getUpperSpecLimit();
        BigDecimal lowerControlLimit =processDataVo.getLowerControlLimit();
        BigDecimal standardDeviationOver =processDataVo.getStandardDeviationOver();
        PP =(upperSpecLimit.subtract(lowerControlLimit)).divide(standardDeviationOver,4,RoundingMode.HALF_UP).divide(BigDecimal.valueOf(6),4,RoundingMode.HALF_UP);
        overallAbilityVo.setPP(PP);

        //	Ppl下限过程能力指数
        BigDecimal PPL =new BigDecimal(0);
        BigDecimal sampleMean =processDataVo.getSampleMean();
        PPL =sampleMean.subtract(lowerControlLimit).divide(standardDeviationOver,4,RoundingMode.HALF_UP).divide(BigDecimal.valueOf(3),4,RoundingMode.HALF_UP);
        overallAbilityVo.setPpl(PPL);

        //	Ppu上限过程能力指数
        BigDecimal PPU =new BigDecimal(0);
        PPU = upperSpecLimit.subtract(sampleMean).divide(standardDeviationOver,4,RoundingMode.HALF_UP).divide(BigDecimal.valueOf(3),4,RoundingMode.HALF_UP);
        overallAbilityVo.setPPU(PPU);

        //	Ppk修正的过程能力指数
        BigDecimal PPK =new BigDecimal(0);
        PPK =PPL.min(PPU);
        overallAbilityVo.setPPK(PPK);

        //TODO    CPM

        return overallAbilityVo;
    }

    //3 潜在能力
    PotentialAbilityVo getPotentialAbility(ProcessDataVo processDataVo){
        PotentialAbilityVo potentialAbilityVo =new PotentialAbilityVo();

        //	Cp过程能力指数
        BigDecimal CP =new BigDecimal(0);
        BigDecimal upperSpecLimit =processDataVo.getUpperSpecLimit();
        BigDecimal lowerControlLimit =processDataVo.getLowerControlLimit();
        BigDecimal standardDeviationIn =processDataVo.getStandardDeviationIn();
        BigDecimal sampleMean =processDataVo.getSampleMean();
        CP =upperSpecLimit.subtract(lowerControlLimit).divide(standardDeviationIn,4,RoundingMode.HALF_UP).divide(BigDecimal.valueOf(6),4,RoundingMode.HALF_UP);
        potentialAbilityVo.setCP(CP);

        //	CPL
        BigDecimal CPL =new BigDecimal(0);
        CPL =sampleMean.subtract(lowerControlLimit).divide(standardDeviationIn,4,RoundingMode.HALF_UP).divide(BigDecimal.valueOf(3),4,RoundingMode.HALF_UP);
        potentialAbilityVo.setCPL(CPL);

        //	CPU
        BigDecimal CPU =new BigDecimal(0);
        CPU =upperSpecLimit.subtract(sampleMean).divide(standardDeviationIn,4,RoundingMode.HALF_UP).divide(BigDecimal.valueOf(3),4,RoundingMode.HALF_UP);
        potentialAbilityVo.setCPU(CPU);

        //	Cpk
        BigDecimal CPK =new BigDecimal(0);
        CPK =CPL.min(CPU);
        potentialAbilityVo.setCPK(CPK);




        return potentialAbilityVo;
    }


    /**
     *f(x,μ,s)=1/(s√2π) ⅇ^(-((x-u)^2/(2s^2 )) )
     * s为标准差（整体），μ为样本均值，x为区间起始点的值
     * @param standardDeviation   标准差
     * @param averageValue   平均值
     * @param startingPoint  起始点
     * @return
     */
    public String calculatedProbability(BigDecimal standardDeviation,BigDecimal averageValue,BigDecimal startingPoint){


        BigDecimal res =new BigDecimal(0).setScale(4,RoundingMode.HALF_UP);
        BigDecimal pi =new BigDecimal(3.1415926);
        BigDecimal e =new BigDecimal(2.7182818);

        BigDecimal start =new BigDecimal(1);

        //根号2pi
        BigDecimal _pi =new BigDecimal(0);
        _pi =BigDecimal.valueOf(Math.sqrt(pi.multiply(BigDecimal.valueOf(2)).doubleValue()));

        //n次方计算
        BigDecimal NthPower =new BigDecimal(0);
        NthPower =NthPower.subtract(((startingPoint.subtract(averageValue)).pow(2)).divide((BigDecimal.valueOf(2).multiply((standardDeviation.pow(2)))),
                4,RoundingMode.HALF_UP));



        start =start.divide(standardDeviation,4,RoundingMode.HALF_UP).divide(_pi,4,RoundingMode.HALF_UP);
        double resDouble =Math.pow(e.doubleValue(),NthPower.doubleValue());
        res =start.multiply(BigDecimal.valueOf(resDouble)).setScale(20,RoundingMode.HALF_UP);

        return res.stripTrailingZeros().toPlainString();
    }


    public static void main(String[] args) {

        CPKAnalyseServiceImpl service =new CPKAnalyseServiceImpl();

        BigDecimal standardDeviation = BigDecimal.valueOf(3.2025);
        BigDecimal averageValue =BigDecimal.valueOf(25.0359);
        BigDecimal startingPoint =BigDecimal.valueOf(14.9587);

        BigDecimal res =new BigDecimal(0).setScale(4,RoundingMode.HALF_UP);
        BigDecimal pi =new BigDecimal(3.1415926).setScale(4,RoundingMode.HALF_UP);
        BigDecimal e =new BigDecimal(2.7182818).setScale(4,RoundingMode.HALF_UP);

        BigDecimal start =new BigDecimal(1);

        //根号2pi
        BigDecimal _pi =new BigDecimal(0).setScale(4,RoundingMode.HALF_UP);
        _pi =BigDecimal.valueOf(Math.sqrt(pi.multiply(BigDecimal.valueOf(2)).doubleValue()));

        //n次方计算
        BigDecimal NthPower =new BigDecimal(0);
        NthPower =NthPower.subtract(((startingPoint.subtract(averageValue)).pow(2)).divide((BigDecimal.valueOf(2).multiply((standardDeviation.pow(2)))),
                4,RoundingMode.HALF_UP));

        start =start.divide(standardDeviation,4,RoundingMode.HALF_UP).divide(_pi,4,RoundingMode.HALF_UP).multiply(e);
        double resDouble =Math.pow(start.doubleValue(),NthPower.doubleValue());
        res =BigDecimal.valueOf(resDouble).setScale(4,RoundingMode.HALF_UP);

        System.out.println(res);



//        List<BigDecimal> SsampleValues =new ArrayList<>(50);
//        SsampleValues.add(new BigDecimal(131));
//        SsampleValues.add(new BigDecimal(110));
//        SsampleValues.add(new BigDecimal(90));
//        SsampleValues.add(new BigDecimal(88));
//
//        BigDecimal Spow =new BigDecimal(0);
//        BigDecimal sampleMean =new BigDecimal(104.750);
//        for(BigDecimal s:SsampleValues){
//            Spow =Spow.add((s.subtract(sampleMean)).pow(2));
//        }
//
//        BigDecimal standardDeviationOver =new BigDecimal(0);
//        Spow =Spow.divide(BigDecimal.valueOf(4-1),4, BigDecimal.ROUND_HALF_UP);
//
//        Double mathS =Math.sqrt(Spow.doubleValue());
//
//        standardDeviationOver = BigDecimal.valueOf(mathS).setScale(4,RoundingMode.HALF_UP);
//
//        System.out.println(standardDeviationOver);


//        BigDecimal sampleValues =new BigDecimal(72.776).setScale(4,RoundingMode.HALF_UP);
//        BigDecimal s = sampleValues.divide(BigDecimal.valueOf(3),4,RoundingMode.HALF_UP);
//
//
//        System.out.println(s);


        BigDecimal samplen = new BigDecimal(0).setScale(0,RoundingMode.HALF_UP);

        double d =Math.sqrt(3);
        samplen =(BigDecimal.valueOf(d)).add(BigDecimal.valueOf(1)).setScale(0,RoundingMode.HALF_UP);

        System.out.println(samplen);

    }

    /**
     * @Author han.zhang
     * @Description 将计算出来的数据转换成echarts所需的集合格式
     * @Date 10:56 2019/9/19
     * @Param [cpkAnalyseResDTO 返回的dto, cpkAnalyseChartVo 计算出来的数据]
     */
    private void setChartData(CPKAnalyseResDTO cpkAnalyseResDTO,CPKAnalyseChartVo cpkAnalyseChartVo){
        //横坐标：中点
        List<BigDecimal> xList = new ArrayList<>();
        //直方图: 频数
        List<BigDecimal> frequencyList = new ArrayList<>();
        //虚线：组内概率
        List<String> intraGroupProbabilityList = new ArrayList<>();
        //实线：整体概率
        List<String> overallProbabilityList = new ArrayList<>();
        cpkAnalyseChartVo.getCpkAnalyseChartDataVo().forEach(vo -> {
            xList.add(vo.getxCoordinate());
            frequencyList.add(vo.getyStripGraph());
            intraGroupProbabilityList.add(String.valueOf(Double.valueOf(vo.getyDottedGraph())*100));
            overallProbabilityList.add(String.valueOf(Double.valueOf(vo.getySolidGraph())*100));
        });
        cpkAnalyseResDTO.setxList(xList);
        cpkAnalyseResDTO.setFrequency(frequencyList);
        cpkAnalyseResDTO.setIntraGroupProbability(intraGroupProbabilityList);
        cpkAnalyseResDTO.setOverallProbability(overallProbabilityList);
    }
}
