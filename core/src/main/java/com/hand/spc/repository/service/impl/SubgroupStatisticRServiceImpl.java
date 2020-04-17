package com.hand.spc.repository.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hand.hap.system.service.impl.BaseServiceImpl;
import com.hand.spc.repository.dto.CPKAnalyseReqDTO;
import com.hand.spc.repository.dto.SubgroupStatisticR;
import com.hand.spc.repository.dto.SubgroupStatisticVo;
import com.hand.spc.repository.mapper.SubgroupStatisticRMapper;
import com.hand.spc.repository.service.ISubgroupStatisticRService;

@Service
@Transactional(rollbackFor = Exception.class)
public class SubgroupStatisticRServiceImpl extends BaseServiceImpl<SubgroupStatisticR> implements ISubgroupStatisticRService {

    @Autowired
    private SubgroupStatisticRMapper subgroupStatisticMapper;

    @Override
    public int batchInsertRows(List<SubgroupStatisticR> subgroupStatisticList) {
        return subgroupStatisticMapper.batchInsertRows(subgroupStatisticList);
    }

    @Override
    public List<SubgroupStatisticVo> selectByCodeAndVersion(CPKAnalyseReqDTO cpkAnalyseReqDTO) {


        return subgroupStatisticMapper.selectByCodeAndVersion(cpkAnalyseReqDTO);
    }
}
