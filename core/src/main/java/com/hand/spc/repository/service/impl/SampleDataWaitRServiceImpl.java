package com.hand.spc.repository.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;

import com.hand.hap.system.service.impl.BaseServiceImpl;
import com.hand.spc.repository.dto.SampleDataDTO;
import com.hand.spc.repository.dto.SampleDataWaitR;
import com.hand.spc.repository.dto.SampleGroupDataVO;
import com.hand.spc.repository.mapper.SampleDataRMapper;
import com.hand.spc.repository.mapper.SampleDataWaitRMapper;
import com.hand.spc.repository.service.ISampleDataWaitRService;
import com.hand.spc.utils.Page;
@Transactional(rollbackFor = Exception.class)
public class SampleDataWaitRServiceImpl extends BaseServiceImpl<SampleDataWaitR> implements ISampleDataWaitRService {

    @Autowired
    SampleDataWaitRMapper sampleDataWaitMapper;

    @Autowired
    SampleDataRMapper sampleDataMapper;

    //@Override
    //public List<SampleDataWaitDTO> getSampleDataWait(SampleDataWaitDTO sampleDataWaitDTO) {
    //return sampleDataWaitMapper.sampleDataWaitList(sampleDataWaitDTO);
    //}

    @Override
    public Page<SampleDataDTO> getSampleData(SampleDataDTO sampleDataDTO, PageRequest pageRequest) {
        // 20190813 return PageHelper.doPage(pageRequest.getPage(), pageRequest.getSize(), () -> sampleDataMapper.sampleDataList(sampleDataDTO));
        //return sampleDataWaitMapper.sampleDataWaitList(sampleDataWaitDTO);
    	
    	return (Page<SampleDataDTO>) sampleDataMapper.sampleDataList(sampleDataDTO);
    }

    @Override
    public int deleteSampleDataWait(SampleDataWaitR sampleDataWait) {
        return sampleDataWaitMapper.deleteSampleDataWait(sampleDataWait);
    }

    @Override
    public int insertDataFromTmp(Long tenantId, Long siteId) {
        return sampleDataWaitMapper.insertDataFromTmp(tenantId, siteId);
    }

    @Override
    public List<SampleGroupDataVO> listSampleGroupData(Long tenantId, Long siteId) {
        return sampleDataWaitMapper.listSampleGroupData(tenantId, siteId);
    }
    
    @Override
    public List<SampleDataWaitR> listExtractSampleDataWait(SampleGroupDataVO sampleGroupDataVO) { // modified 20190903 
        return sampleDataWaitMapper.listExtractSampleDataWait(sampleGroupDataVO);
    }
    
    @Override
   	public List<SampleDataWaitR> listExtractSampleDataWaitModified(String whereInSql) {
   		return sampleDataWaitMapper.listExtractSampleDataWaitModified(whereInSql);
   	}
    
    @Override
    public int batchDeleteSampleDataWaitByIds(List<Long> sampleDataWaitIdList) {
        return sampleDataWaitMapper.batchDeleteSampleDataWaitByIds(sampleDataWaitIdList);
    }
    
   

}
