package com.hand.spc.ecr_main.service.impl;

import com.github.pagehelper.PageHelper;
import com.hand.hap.core.IRequest;
import com.hand.hap.system.service.impl.BaseServiceImpl;
import com.hand.spc.ecr_main.dto.EcrQtp;
import com.hand.spc.ecr_main.mapper.EcrQtpMapper;
import com.hand.spc.ecr_main.service.IEcrQtpService;
import com.hand.spc.ecr_main.view.EcrQtpV0;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * description
 *
 * @author KOCDZX0 2020/03/12 1:51 PM
 */
@Service
public class EcrQtpServiceImpl extends BaseServiceImpl<EcrQtp> implements IEcrQtpService {

    @Autowired
    private EcrQtpMapper ecrQtpMapper;


    @Override
    public List<EcrQtpV0> eqQuery(IRequest request, EcrQtpV0 dto) {
        return ecrQtpMapper.eqQuery(dto);
    }
}
