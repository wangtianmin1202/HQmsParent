package com.hand.spc.repository.service;

import java.util.List;

import com.hand.hap.core.ProxySelf;
import com.hand.hap.system.service.IBaseService;
import com.hand.spc.repository.dto.CPKAnalyseReqDTO;
import com.hand.spc.repository.dto.SampleSubgroupR;
import com.hand.spc.repository.dto.SePointDataDTO;
import com.hand.spc.repository.dto.SeRequestDTO;

public interface ISampleSubgroupRService extends IBaseService<SampleSubgroupR>,ProxySelf<ISampleSubgroupRService> {

    /**
     * 查询当前分组之前特定个数分组点
     *
     * @param tenantId
     * @param siteId
     * @param entityCode
     * @param entityVersion
     * @param maxSubgroupNum
     * @param minSubgroupNum
     * @param chartType
     * @return
     */
    public List<SampleSubgroupR> queryPreSubgroupStatistic(Long tenantId, Long siteId, String entityCode, String entityVersion, Long maxSubgroupNum, Long minSubgroupNum, String chartType);

    /**
     * 获取最大组号
     *
     * @param requestDTO
     * @return
     */
    public Long queryMaxSubgroupNum(SeRequestDTO requestDTO);

    /**
     * SE图形分组数据查询
     *
     * @param requestDTO
     * @return
     */
    public List<SePointDataDTO> listSePointData(SeRequestDTO requestDTO);

    /**
     * 批量保存分组数据
     *
     * @param sampleSubgroupList
     * @return
     */
    public int batchInsertRows(List<SampleSubgroupR> sampleSubgroupList);


    /**
     * CPK分析图查询
     *
     * @param requestDTO
     * @return
     */
    public List<SampleSubgroupR> querySampleSubgroupByCPK(CPKAnalyseReqDTO requestDTO);



}
