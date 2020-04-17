package com.hand.itf.itf_function_info.service.impl;

import com.github.pagehelper.PageHelper;
import com.hand.hap.core.IRequest;
import com.hand.hap.system.service.impl.BaseServiceImpl;
import com.hand.itf.itf_function_info.mapper.FunctionInfoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.hand.itf.itf_function_info.dto.FunctionInfo;
import com.hand.itf.itf_function_info.service.IFunctionInfoService;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(rollbackFor = Exception.class)
public class FunctionInfoServiceImpl extends BaseServiceImpl<FunctionInfo> implements IFunctionInfoService{

    @Autowired
    FunctionInfoMapper functionInfoMapper;

    @Override
    public List<FunctionInfo> individuationQuery(IRequest request,int page,int pageSize,String individuationSql){
        PageHelper.startPage(page, pageSize);
        return functionInfoMapper.individuationQuery(individuationSql);
    }

}