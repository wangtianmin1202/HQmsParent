package com.hand.spc.repository.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.hand.hap.mybatis.common.Mapper;
import com.hand.spc.repository.dto.JudgementR;

public interface JudgementRMapper extends Mapper<JudgementR> {

    /**
     * 查询判异规则
     * @param tenantId
     * @param siteId
     * @param chartId
     * @return
     */
    public List<JudgementR> listJudegement(@Param("tenantId")Long tenantId,@Param("siteId")Long siteId,@Param("chartId")Long chartId);

    /**
     * 查询判异规则
     * @param tenantId
     * @param siteId
     * @param chartId
     * @return
     */
    public List<JudgementR> listCountJudegement(@Param("tenantId")Long tenantId,@Param("siteId")Long siteId,@Param("chartId")Long chartId);
}
