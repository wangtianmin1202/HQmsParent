package com.hand.hcm.hcm_category_settings.service;

import com.hand.hap.core.IRequest;
import com.hand.hap.core.ProxySelf;
import com.hand.hap.system.dto.ResponseData;
import com.hand.hap.system.service.IBaseService;
import com.hand.hcm.hcm_category_settings.dto.ItemCategorySettings;

import java.util.List;

public interface IItemCategorySettingsService extends IBaseService<ItemCategorySettings>, ProxySelf<IItemCategorySettingsService>{

    /**
     * 物料分类属性查询
     * @param request
     * @param dto
     * @param page
     * @param pageSize
     * @return
     */
    List<ItemCategorySettings> listQuery(IRequest request,ItemCategorySettings dto,int page,int pageSize);

    /**
     * 物料类别维护
     * @param request
     * @param dto
     * @return
     */
    ResponseData addOrUpdate(IRequest request, ItemCategorySettings dto);
}