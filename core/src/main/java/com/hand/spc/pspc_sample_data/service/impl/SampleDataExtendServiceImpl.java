package com.hand.spc.pspc_sample_data.service.impl;

import com.github.pagehelper.PageHelper;
import com.hand.hap.core.IRequest;
import com.hand.hap.system.service.impl.BaseServiceImpl;
import com.hand.spc.pspc_sample_data.mapper.SampleDataExtendMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.hand.spc.pspc_sample_data.dto.SampleDataExtend;
import com.hand.spc.pspc_sample_data.service.ISampleDataExtendService;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(rollbackFor = Exception.class)
public class SampleDataExtendServiceImpl extends BaseServiceImpl<SampleDataExtend> implements ISampleDataExtendService{

    @Autowired
    private SampleDataExtendMapper sampleDataExtendMapper;

    /**
     * @param iRequest         基本参数
     * @param sampleDataExtend 限制条件
     * @param page             页数
     * @param pageSize         页数大小
     * @return : java.util.List<com.hand.spc.pspc_sample_data.dto.SampleDataExtend>
     * @Description: 基础数据查询
     * @author: ywj
     * @date 2019/8/14 17:50
     * @version 1.0
     */
    @Override
    public List<SampleDataExtend> queryBaseData(IRequest iRequest, SampleDataExtend sampleDataExtend, int page, int pageSize) {

        // 分页
        PageHelper.startPage(page,pageSize);

        // 数据查询
        return sampleDataExtendMapper.queryBaseData(sampleDataExtend);
    }
}