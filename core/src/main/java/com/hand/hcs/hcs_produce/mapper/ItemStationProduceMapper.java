package com.hand.hcs.hcs_produce.mapper;

import com.hand.hap.mybatis.common.Mapper;
import com.hand.hcs.hcs_produce.dto.ItemStationProduce;

import java.util.List;

public interface ItemStationProduceMapper extends Mapper<ItemStationProduce>{

    List<ItemStationProduce> listQuery(ItemStationProduce dto);

    /**
     * 查询存在ng项的制程
     * @return
     */
    List<ItemStationProduce> listExistNg();

    /**
     * 查询不存在ng项的制程
     * @return
     */
    List<ItemStationProduce> listNotExistNg();
}