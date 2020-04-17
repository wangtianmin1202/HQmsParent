package com.hand.spc.pspc_classify_group.mapper;

import com.hand.hap.mybatis.common.Mapper;
import com.hand.spc.pspc_classify_group.dto.ClassifyGroup;

import java.util.List;

public interface ClassifyGroupMapper extends Mapper<ClassifyGroup>{
    /**
     * @Author han.zhang
     * @Description 分类组lov查询，支持编码和描述模糊查询
     * @Date 15:22 2019/8/28
     * @Param [classifyGroup]
     */
    List<ClassifyGroup> selectClassifyGroupLov(ClassifyGroup classifyGroup);
}