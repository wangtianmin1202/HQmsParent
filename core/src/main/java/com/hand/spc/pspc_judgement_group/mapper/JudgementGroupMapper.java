package com.hand.spc.pspc_judgement_group.mapper;

import com.hand.hap.mybatis.common.Mapper;
import com.hand.spc.pspc_judgement_group.dto.JudgementGroup;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface JudgementGroupMapper extends Mapper<JudgementGroup>{

    /**
    * @Description 若判异规则组已经被(SPC_ENTITY)SPC实体控制图引用，则不可删除，通过判异规则组ID关联相关表
    * @author hch
    * @date 2019/8/14 17:19
    * @Param [judgementGroupId]
    * @return int
    * @version 1.0
    */
    int selectEntityCount(@Param("judgementGroupId")Float judgementGroupId);

    int selectChartDetailCount(@Param("judgementGroupId")Float judgementGroupId);

    int selectOocRecordCount(@Param("judgementGroupId") Float judgementGroupId);

    int selectOocCount(@Param("judgementGroupId") Float judgementGroupId);

    /**
    * @Description 判异规则组维护前台查询
    * @author hch
    * @date 2019/8/28 15:36
    * @Param [dto]
    * @return java.util.List<com.hand.spc.pspc_judgement_group.dto.JudgementGroup>
    * @version 1.0
    */
    List<JudgementGroup> selectDate(JudgementGroup dto);

    long validateUnique(JudgementGroup dto);
}