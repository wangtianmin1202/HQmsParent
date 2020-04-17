package com.hand.itf.itf_function_info.mapper;

import com.hand.hap.mybatis.common.Mapper;
import com.hand.itf.itf_function_info.dto.FunctionInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface FunctionInfoMapper extends Mapper<FunctionInfo>{

    public List<FunctionInfo> individuationQuery(@Param("individuationSql") String individuationSql);

}