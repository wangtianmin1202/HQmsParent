package com.hand.spc.repository.service;

import java.util.List;

import com.hand.hap.core.ProxySelf;
import com.hand.hap.system.service.IBaseService;
import com.hand.spc.repository.dto.CPKAnalyseReqDTO;
import com.hand.spc.repository.dto.SubgroupStatisticR;
import com.hand.spc.repository.dto.SubgroupStatisticVo;

public interface ISubgroupStatisticRService extends IBaseService<SubgroupStatisticR>, ProxySelf<ISubgroupStatisticRService> {

    /**
     * 批量保存子组计算数据
     *
     * @param subgroupStatisticList
     * @return
     */
    public int batchInsertRows(List<SubgroupStatisticR> subgroupStatisticList);

    List<SubgroupStatisticVo> selectByCodeAndVersion(CPKAnalyseReqDTO cpkAnalyseReqDTO);


}
