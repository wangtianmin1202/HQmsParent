package com.hand.spc.repository.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hand.hap.system.service.impl.BaseServiceImpl;
import com.hand.spc.repository.dto.CountSampleDataR;
import com.hand.spc.repository.dto.CountSampleDataWaitR;
import com.hand.spc.repository.mapper.CountSampleDataRMapper;
import com.hand.spc.repository.service.ICountSampleDataRService;
import com.hand.spc.utils.Page;

/**
 * 样本数据(计数) 资源库实现
 *
 * @author peng.hu04@hand-china.com 2019-06-20 11:02:38
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class CountSampleDataRServiceImpl extends BaseServiceImpl<CountSampleDataR> implements ICountSampleDataRService {

    @Autowired
    private CountSampleDataRMapper countSampleDataMapper;

    @Override
    public int insertCountSampleData(CountSampleDataWaitR countSampleDataWait) {
        return countSampleDataMapper.insertCountSampleData(countSampleDataWait);
    }

    @Override
    public Page<CountSampleDataR> queryCountSampleData(CountSampleDataR countSampleData, PageRequest pageRequest) {
        return null;//PageHelper.doPage(pageRequest.getPage(), pageRequest.getSize(), () -> countSampleDataMapper.queryCountSampleData(countSampleData));
    }
}
