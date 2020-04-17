package com.hand.hcm.hcm_category_settings.mapper;

import com.hand.hap.mybatis.common.Mapper;
import com.hand.hcm.hcm_category_settings.dto.ItemCategorySettings;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ItemCategorySettingsMapper extends Mapper<ItemCategorySettings>{

    List<ItemCategorySettings> listQuery(ItemCategorySettings dto);

    int update(ItemCategorySettings dto);

    int updateItemVersion(@Param("itemId")  Long itemId,@Param("itemVersion") Long itemVersion);
}