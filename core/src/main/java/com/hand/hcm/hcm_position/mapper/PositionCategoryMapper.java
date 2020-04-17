package com.hand.hcm.hcm_position.mapper;

import com.hand.hap.mybatis.common.Mapper;
import com.hand.hcm.hcm_position.dto.PositionCategory;
import org.apache.ibatis.annotations.Param;

public interface PositionCategoryMapper extends Mapper<PositionCategory>{

    PositionCategory query(@Param("sparePartId") Long sparePartId);

    void deleteByPositionId(@Param("positionId") Long positionId);
}