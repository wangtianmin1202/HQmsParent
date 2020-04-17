package com.hand.spc.repository.mapper;

import java.util.List;

import com.hand.hap.mybatis.common.Mapper;
import com.hand.spc.repository.dto.CountStatisticR;
import com.hand.spc.repository.dto.CountStatisticVO;
import com.hand.spc.repository.dto.PlatoCountVO;
import com.hand.spc.repository.dto.SeCountPointDataDTO;
import com.hand.spc.repository.dto.SeRequestDTO;

/**
 * 计数型统计量表Mapper
 *
 * @author peng.hu04@hand-china.com 2019-06-20 11:01:57
 */
public interface CountStatisticRMapper extends Mapper<CountStatisticR> {
    /**
     * 查询计数型统计量
     *
     * @param tenantId
     * @param siteId
     * @param entityCode
     * @param entityVersion
     * @param maxSubgroupNum 组号最大
     * @param minSubgroupNum 组号最小
     * @param chartType 控制图类型
     * @return
     */
    public List<CountStatisticR> listCountStatistic(Long tenantId, Long siteId, String entityCode, String entityVersion, Long maxSubgroupNum, Long minSubgroupNum, String chartType);

    /**
     * 批量保存 计数型统计量数据
     *
     * @param countStatisticList
     * @return
     */
    public int batchInsertRows(List<CountStatisticR> countStatisticList);

    /**
     * 获取最大组号
     *
     * @param requestDTO
     * @return
     */
    public Long queryMaxSubgroupNum(SeRequestDTO requestDTO);

    /**
     * 柏拉图
     *
     * @param countStatisticVO
     * @return
     */
    public List<PlatoCountVO> listPlatoCount(CountStatisticVO countStatisticVO);

    /**
     * 图形展示
     *
     * @param countStatisticVO
     * @return
     */
    public List<SeCountPointDataDTO> listSECount(CountStatisticVO countStatisticVO);
}
