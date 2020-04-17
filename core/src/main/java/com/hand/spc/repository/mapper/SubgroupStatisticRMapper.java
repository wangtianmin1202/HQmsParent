package com.hand.spc.repository.mapper;

import java.util.List;

import com.hand.hap.mybatis.common.Mapper;
import com.hand.spc.repository.dto.AndersonDarlingDTO;
import com.hand.spc.repository.dto.CPKAnalyseReqDTO;
import com.hand.spc.repository.dto.SubgroupStatisticR;
import com.hand.spc.repository.dto.SubgroupStatisticVo;

public interface SubgroupStatisticRMapper extends Mapper<SubgroupStatisticR> {

    /**
     * 批量保存子组计算数据
     *
     * @param subgroupStatisticList
     * @return
     */
    public int batchInsertRows(List<SubgroupStatisticR> subgroupStatisticList);

    List<SubgroupStatisticVo> selectByCodeAndVersion(CPKAnalyseReqDTO cpkAnalyseReqDTO);
    
    List<SubgroupStatisticVo> selectByCodeAndVersionByAD(AndersonDarlingDTO cpkAnalyseReqDTO);

}
