package com.hand.spc.repository.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.hand.hap.system.service.impl.BaseServiceImpl;
import com.hand.spc.repository.dto.CountSampleDataWaitR;
import com.hand.spc.repository.dto.CountSampleDataWaitVO;
import com.hand.spc.repository.mapper.CountSampleDataWaitRMapper;
import com.hand.spc.repository.service.ICountSampleDataWaitRService;

/**
 * 样本数据(计数)预处理 资源库实现
 *
 * @author peng.hu04@hand-china.com 2019-06-20 11:02:18
 */
@Transactional(rollbackFor = Exception.class)
public class CountSampleDataWaitRServiceImpl extends BaseServiceImpl<CountSampleDataWaitR> implements ICountSampleDataWaitRService {

    @Autowired
    CountSampleDataWaitRMapper countSampleDataWaitMapper;

    @Override
    public List<CountSampleDataWaitR> listExtractCountSampleDataWait(CountSampleDataWaitVO countSampleDataWaitVO) {
        return countSampleDataWaitMapper.listExtractCountSampleDataWait(countSampleDataWaitVO);
    }

    @Override
    public List<CountSampleDataWaitVO> listCountSampleDataWait(Long tenantId, Long siteId) {
        return countSampleDataWaitMapper.listCountSampleDataWait(tenantId, siteId);
    }

    @Override
    public int deleteCountSampleDataWait(CountSampleDataWaitR countSampleDataWait) {
        return countSampleDataWaitMapper.deleteCountSampleDataWait(countSampleDataWait);
    }
    
    @Override
    public int batchInsertCountSampleDataWait(List<CountSampleDataWaitR> countSampleDataWaits) {
        return countSampleDataWaitMapper.batchInsertCountSampleDataWait(countSampleDataWaits);
    }

    @Override
    public List<Long> selectIsCountData(CountSampleDataWaitVO countSampleDataWaitVO) {
        return countSampleDataWaitMapper.selectIsCountData(countSampleDataWaitVO);
    }

	@Override
	public List<CountSampleDataWaitR> listExtractCountSampleDataWaitModified(String whereInSql) {
		return countSampleDataWaitMapper.listExtractCountSampleDataWaitModified(whereInSql);
	}

	@Override
	public List<Long> selectIsCountDataModified(String whereInSql) {
		return countSampleDataWaitMapper.selectIsCountDataModified(whereInSql);
	}
}
