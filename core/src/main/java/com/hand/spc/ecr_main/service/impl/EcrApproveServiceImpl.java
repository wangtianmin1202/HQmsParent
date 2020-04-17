package com.hand.spc.ecr_main.service.impl;

import com.hand.hap.core.IRequest;
import com.hand.spc.ecr_main.mapper.EcrApproveMapper;
import com.hand.spc.ecr_main.service.IEcrApproveService;
import com.hand.spc.ecr_main.view.EcrApproveV1;
import com.hand.spc.ecr_main.view.EcrApproveV2;
import com.hand.spc.ecr_main.view.EcrApproveV3;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * description
 *
 * @author KOCDZX0 2020/03/09 4:31 PM
 */
@Service
public class EcrApproveServiceImpl implements IEcrApproveService {

    @Autowired
    private EcrApproveMapper ecrApproveMapper;

    @Override
    public List<EcrApproveV1> approve1Query(IRequest requestCtx, EcrApproveV1 vo) {
        return ecrApproveMapper.approve1Query(vo);

    }

    @Override
    public List<EcrApproveV2> approve2Query(IRequest requestCtx, EcrApproveV2 vo) {
        return ecrApproveMapper.approve2Query(vo);
    }

    @Override
    public List<EcrApproveV3> approve3Query(EcrApproveV3 vo) {
        return ecrApproveMapper.approve3Query(vo);
    }
}
