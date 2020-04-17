package com.hand.spc.pspc_classify.mapper;

import com.hand.hap.mybatis.common.Mapper;
import com.hand.spc.pspc_classify.dto.Classify;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ClassifyMapper extends Mapper<Classify>{
    /**
     * @Author han.zhang
     * @Description 根据控制要素id查询分类项
     * @Date 13:43 2019/8/13
     * @Param [ceParameterId]
     * @param ceParameterId
     */
    List<Classify> queryByParamterId(@Param("ceParameterId") Long ceParameterId);

    /**
     * @Author han.zhang
     * @Description 分类项lov查询，支持编码和描述模糊查询
     * @Date 15:15 2019/8/28
     * @Param [dto]
     */
    List<Classify> selectClassifyLov(Classify dto);
}