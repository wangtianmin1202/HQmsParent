package com.hand.spc.repository.service;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.data.domain.PageRequest;

import com.hand.hap.core.ProxySelf;
import com.hand.hap.system.service.IBaseService;
import com.hand.spc.repository.dto.SampleDataDTO;
import com.hand.spc.repository.dto.SampleDataWaitR;
import com.hand.spc.repository.dto.SampleGroupDataVO;
import com.hand.spc.utils.Page;

public interface ISampleDataWaitRService extends IBaseService<SampleDataWaitR>, ProxySelf<ISampleDataWaitRService> {

    //public List<SampleDataWaitDTO> getSampleDataWait(SampleDataWaitDTO sampleDataWaitDTO);
    public Page<SampleDataDTO> getSampleData(SampleDataDTO sampleDataDTO, PageRequest pageRequest);

    /**
     * 根据预分组样本数据ID 删除数据
     *
     * @param sampleDataWait
     * @return
     */
    public int deleteSampleDataWait(SampleDataWaitR sampleDataWait);

    /**
     * 从临时表新增预处理样本数据
     *
     * @param tenantId
     * @param siteId
     * @return
     */
    public int insertDataFromTmp(Long tenantId, Long siteId);

    /**
     * 获取预分组样本数据
     *
     * @param tenantId
     * @param siteId
     * @return
     */
    public List<SampleGroupDataVO> listSampleGroupData(Long tenantId, Long siteId);
    
    /**
     * 抽样 样本数据预处理 数据
     *
     * @param sampleGroupDataVO
     * @return
     */
    List<SampleDataWaitR> listExtractSampleDataWait(SampleGroupDataVO sampleGroupDataVO);  // modified 20190903 
    List<SampleDataWaitR> listExtractSampleDataWaitModified(@Param("whereInSql") String whereInSql);

    int batchDeleteSampleDataWaitByIds(List<Long> sampleDataWaitIdList);
}
