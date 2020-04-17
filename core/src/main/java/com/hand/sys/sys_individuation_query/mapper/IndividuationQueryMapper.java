package com.hand.sys.sys_individuation_query.mapper;

import com.hand.hap.mybatis.common.Mapper;
import com.hand.sys.sys_individuation_query.dto.IndividuationQuery;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface IndividuationQueryMapper extends Mapper<IndividuationQuery>{

    /**
     * 根据功能编码查询列
     * @param code 功能编码
     * @return List集合
     */
    public List<IndividuationQuery> selectColumnByFunction(@Param("functionCode") String code);

}