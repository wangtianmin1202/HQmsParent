package com.hand.spc.pspc_count_sample_data_extend.service.impl;

import com.hand.hap.core.IRequest;
import com.hand.hap.system.service.impl.BaseServiceImpl;
import com.hand.spc.pspc_count_sample_data.dto.CountSampleData;
import com.hand.spc.pspc_count_sample_data.dto.CountSampleDataVO;
import com.hand.spc.pspc_count_sample_data.mapper.CountSampleDataMapper;
import com.hand.spc.pspc_count_sample_data_extend.mapper.CountSampleDataExtendMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.hand.spc.pspc_count_sample_data_extend.dto.CountSampleDataExtend;
import com.hand.spc.pspc_count_sample_data_extend.service.ICountSampleDataExtendService;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional(rollbackFor = Exception.class)
public class CountSampleDataExtendServiceImpl extends BaseServiceImpl<CountSampleDataExtend> implements ICountSampleDataExtendService{
    @Autowired
    private CountSampleDataExtendMapper countSampleDataExtendMapper;
    @Autowired
    private CountSampleDataMapper countSampleDataMapper;
    @Override
    public List<CountSampleDataExtend> queryExtendColumns(IRequest requestContext, CountSampleData dto) {
        //查询基本列
        List<CountSampleDataVO> countSampleDataVOS = countSampleDataMapper.selectCountData(dto);
        List<CountSampleDataExtend> countSampleDataExtendList = new ArrayList<>();
        if(countSampleDataVOS.size() > 0){
            countSampleDataExtendList = countSampleDataExtendMapper.getExtAttribute(countSampleDataVOS.get(0).getCountSampleDataId());
        }
        return countSampleDataExtendList;
    }
}