package com.hand.hcs.hcs_station.mapper;

import com.hand.hap.mybatis.common.Mapper;
import com.hand.hap.system.dto.ResponseData;
import com.hand.hcs.hcs_station.dto.SupplierItemStation;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SupplierItemStationMapper extends Mapper<SupplierItemStation>{

    List<SupplierItemStation> headQuery(SupplierItemStation dto);

    void enable(@Param("itemStationId") Float itemStationId, @Param("enableFlag") String enableFlag);

    Long maxSequence(SupplierItemStation dto);
}