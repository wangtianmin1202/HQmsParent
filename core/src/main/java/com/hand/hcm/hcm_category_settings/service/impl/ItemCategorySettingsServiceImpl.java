package com.hand.hcm.hcm_category_settings.service.impl;

import com.github.pagehelper.PageHelper;
import com.hand.hap.core.IRequest;
import com.hand.hap.system.dto.DTOStatus;
import com.hand.hap.system.dto.ResponseData;
import com.hand.hap.system.service.impl.BaseServiceImpl;
import com.hand.hcm.hcm_category_settings.mapper.ItemCategorySettingsMapper;
import com.hand.hcm.hcm_item_b.dto.ItemB;
import com.hand.hcm.hcm_item_b.mapper.ItemBMapper;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.hand.hcm.hcm_category_settings.dto.ItemCategorySettings;
import com.hand.hcm.hcm_category_settings.service.IItemCategorySettingsService;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
@Transactional(rollbackFor = Exception.class)
public class ItemCategorySettingsServiceImpl extends BaseServiceImpl<ItemCategorySettings> implements IItemCategorySettingsService{

    @Autowired
    private ItemCategorySettingsMapper settingsMapper;

    @Autowired
    private ItemBMapper itemBMapper;


    @Override
    public List<ItemCategorySettings> listQuery(IRequest request, ItemCategorySettings dto,int page,int pageSize) {
        PageHelper.startPage(page,pageSize);
        return settingsMapper.listQuery(dto);
    }

    @Override
    public ResponseData addOrUpdate(IRequest request, ItemCategorySettings dto) {
        ResponseData responseData = new ResponseData();
        //更新物料版本
        settingsMapper.updateItemVersion(dto.getItemId(),dto.getItemEdition());

        //更新物料分类
        ItemCategorySettings select = settingsMapper.selectByPrimaryKey(dto.getItemId());
        if(DTOStatus.UPDATE.equals(dto.get__status())){
            if(Objects.isNull(select)){
                self().insertSelective(request,dto);
            }else {
                settingsMapper.update(dto);
            }

        }
        responseData.setMessage("修改成功");
        responseData.setSuccess(true);
        return responseData;
    }
}