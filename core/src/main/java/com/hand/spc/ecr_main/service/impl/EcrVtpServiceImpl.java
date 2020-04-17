package com.hand.spc.ecr_main.service.impl;

import com.hand.hap.system.service.impl.BaseServiceImpl;
import com.hand.spc.ecr_main.dto.EcrVtp;
import com.hand.spc.ecr_main.mapper.EcrVtpMapper;
import com.hand.spc.ecr_main.service.IEcrVtpService;
import com.hand.spc.ecr_main.view.EcrVtpVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * description vtp查询接口
 *
 * @author KOCDZX0 2020/03/09 11:45 AM
 */
@Service
public class EcrVtpServiceImpl extends BaseServiceImpl<EcrVtp> implements IEcrVtpService {

    @Autowired
    private EcrVtpMapper vtpMapper;

    @Override
    public List<EcrVtpVO> listQuery(EcrVtpVO vo) {
        return vtpMapper.vtpQuery(vo);
    }
}
