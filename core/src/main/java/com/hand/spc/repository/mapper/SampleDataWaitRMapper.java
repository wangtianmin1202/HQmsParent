package com.hand.spc.repository.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.hand.hap.mybatis.common.Mapper;
import com.hand.spc.repository.dto.SampleDataWaitR;
import com.hand.spc.repository.dto.SampleDataWaitDTO;
import com.hand.spc.repository.dto.SampleGroupDataVO;

public interface SampleDataWaitRMapper extends Mapper<SampleDataWaitR> {

	List<SampleDataWaitDTO> sampleDataWaitList(SampleDataWaitDTO sampleDataWaitDTO);

    /**
     * 获取预处理样本数据ID集合
     *
     * @param tenantId
     * @param siteId
     * @return
     */
    List<Long> listSampleDataWaitId(@Param("tenantId") Long tenantId, @Param("siteId") Long siteId);

    /**
     * 根据预分组样本数据ID 删除数据
     *
     * @param sampleDataWait
     * @return
     */
    int deleteSampleDataWait(SampleDataWaitR sampleDataWait);

    /**
     * 从临时表新增预处理样本数据
     *
     * @param tenantId
     * @param siteId
     * @return
     */
    int insertDataFromTmp(@Param("tenantId") Long tenantId, @Param("siteId") Long siteId);

    /**
     * 获取预分组样本数据
     *
     * @param tenantId
     * @param siteId
     * @return
     */
    List<SampleGroupDataVO> listSampleGroupData(@Param("tenantId") Long tenantId, @Param("siteId") Long siteId);

    /**
     * 抽样 样本数据预处理 数据
     *
     * @param sampleGroupDataVO
     * @return
     */
    List<SampleDataWaitR> listExtractSampleDataWait(SampleGroupDataVO sampleGroupDataVO);// modified 20190903 
    List<SampleDataWaitR> listExtractSampleDataWaitModified(@Param("whereInSql") String whereInSql);
    
    int batchDeleteSampleDataWaitByIds(@Param("idList") List<Long> sampleDataWaitIdList);

    void batchInsert(List<SampleDataWaitR> sampleDataWaitList);
}
