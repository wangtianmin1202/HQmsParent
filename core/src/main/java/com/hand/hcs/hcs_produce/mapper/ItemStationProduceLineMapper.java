package com.hand.hcs.hcs_produce.mapper;

import com.hand.hap.mybatis.common.Mapper;
import com.hand.hcs.hcs_produce.dto.ItemStationProduceLine;

import java.util.List;

public interface ItemStationProduceLineMapper extends Mapper<ItemStationProduceLine>{

    List<ItemStationProduceLine> listQuery(ItemStationProduceLine dto);

    Long maxLineNum();

}