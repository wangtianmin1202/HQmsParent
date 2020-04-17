package com.hand.hcm.hcm_position.service.impl;

import com.github.pagehelper.PageHelper;
import com.hand.hap.core.IRequest;
import com.hand.hap.system.service.impl.BaseServiceImpl;
import com.hand.hcm.hcm_position.dto.PositionUser;
import com.hand.hcm.hcm_position.mapper.PositionUserMapper;
import com.hand.hcm.hcm_position.service.IPositionUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

/**
 * description
 *
 * @author KOCDZX0 2020/03/11 10:00 AM
 */
@Service
public class PositionUserServiceImpl extends BaseServiceImpl<PositionUser> implements IPositionUserService {


    @Autowired
    private PositionUserMapper userMapper;


    @Override
    public List<PositionUser> query(IRequest request, PositionUser dto, int page, int pageSize) {
        PageHelper.startPage(page,pageSize);
        return userMapper.puQuery(dto);
    }

    @Override
    public List<PositionUser> add(IRequest requestCtx, PositionUser dto) {
        if ("add".equals(dto.get__status())) {
            self().insertSelective(requestCtx, dto);
        } else if ("update".equals(dto.get__status())) {
            self().updateByPrimaryKeySelective(requestCtx, dto);
        }
        return Arrays.asList(dto);
    }
}
