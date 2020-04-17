package com.hand.spc.pspc_judgement_group.mapper;

import com.hand.hap.mybatis.common.Mapper;
import com.hand.spc.pspc_judgement_group.dto.Judgement;
import com.hand.spc.pspc_judgement_group.dto.JudgementGroup;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface JudgementMapper extends Mapper<Judgement>{

    /**
    * @Description 根据头ID查找行表数据
    * @author hch
    * @date 2019/8/14 11:50
    * @Param [dto]
    * @return java.util.List<com.hand.spc.pspc_judgement_group.dto.Judgement>
    * @version 1.0
    */
    List<Judgement> selectData(Judgement dto);


    long validateUnique(Judgement dto);

    int selectChartDetailCount(@Param("judgementId") Float judgementId);

    int selectOocRecordCount(@Param("judgementId") Float judgementId);

    int selectOocCount(@Param("judgementId") Float judgementId);
}