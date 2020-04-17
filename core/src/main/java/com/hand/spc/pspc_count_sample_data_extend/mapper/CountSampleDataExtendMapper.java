package com.hand.spc.pspc_count_sample_data_extend.mapper;

import com.hand.hap.mybatis.common.Mapper;
import com.hand.spc.pspc_count_sample_data_extend.dto.CountSampleDataExtend;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface CountSampleDataExtendMapper extends Mapper<CountSampleDataExtend>{

    List<CountSampleDataExtend> getExtAttribute(@Param("countSampleDataId") Long countSampleDataId);
}