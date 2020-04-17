package com.hand.spc.repository.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.hand.hap.mybatis.common.Mapper;
import com.hand.spc.repository.dto.SampleDataDTO;
import com.hand.spc.repository.dto.SampleDataR;
import com.hand.spc.repository.dto.SampleDataWaitR;

public interface SampleDataRMapper extends Mapper<SampleDataR> {

    List<SampleDataDTO> sampleDataList(SampleDataDTO sampleDataDTO);

    /**
     * 根据预处理样本数据ID集合新增样本数据
     *
     * @param sampleDataWait
     * @return
     */
    public int insertSampleData(SampleDataWaitR sampleDataWait);

    List<SampleDataWaitR> selectByKeys(List<Long> sampleDataWaitIdList);
    
    int batchInsertSampleData(@Param("sampleDataList")List<SampleDataR> sampleDataWaitList);

}
