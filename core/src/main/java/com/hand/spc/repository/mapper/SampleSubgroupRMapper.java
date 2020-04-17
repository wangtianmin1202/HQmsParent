package com.hand.spc.repository.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.hand.hap.mybatis.common.Mapper;
import com.hand.spc.repository.dto.AndersonDarlingDTO;
import com.hand.spc.repository.dto.CPKAnalyseReqDTO;
import com.hand.spc.repository.dto.EntityR;
import com.hand.spc.repository.dto.SampleDataR;
import com.hand.spc.repository.dto.SampleSubgroupR;
import com.hand.spc.repository.dto.SePointDataDTO;
import com.hand.spc.repository.dto.SeRequestDTO;

public interface SampleSubgroupRMapper extends Mapper<SampleSubgroupR> {
	 /**
     * 查询当前分组之前特定个数分组数据
     *
     * @param tenantId
     * @param siteId
     * @param entityCode
     * @param entityVersion
     * @param maxSubgroupNum
     * @param minSubgroupNum
     * @return
     */
    List<SampleSubgroupR> queryPreSubgroupStatistic(@Param("tenantId") Long tenantId, @Param("siteId") Long siteId, @Param("entityCode") String entityCode, @Param("entityVersion") String entityVersion, @Param("maxSubgroupNum") Long maxSubgroupNum, @Param("minSubgroupNum") Long minSubgroupNum, @Param("chartType") String chartType);

    /**
     * 获取最大组号
     *
     * @param requestDTO
     * @return
     */
    Long queryMaxSubgroupNum(SeRequestDTO requestDTO);

    /**
     * SE图形分组数据查询
     *
     * @param requestDTO
     * @return
     */
    List<SePointDataDTO> listSePointData(SeRequestDTO requestDTO);

    /**
     * 批量保存分组数据
     *
     * @param sampleSubgroupList
     * @return
     */
    int batchInsertRows(List<SampleSubgroupR> sampleSubgroupList);

    /**
     * CPK分析图查询
     *
     * @param requestDTO
     * @return
     */
    List<SampleSubgroupR> querySampleSubgroupByCPK(CPKAnalyseReqDTO requestDTO);

    List<SampleSubgroupR> querySampleSubgroupByAD(AndersonDarlingDTO requestDTO);

    List<SampleDataR> queryDateForDataCompare(EntityR entity);

    List<SampleDataR> queryDateForCorrelationAnalysis(EntityR entity);


}
