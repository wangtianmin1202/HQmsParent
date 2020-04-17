package com.hand.hcm.hcm_item_b.service.impl;

import com.hand.hap.system.service.impl.BaseServiceImpl;
import org.springframework.stereotype.Service;
import com.hand.hcm.hcm_item_b.dto.ItemControl;
import com.hand.hcm.hcm_item_b.service.IItemControlService;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(rollbackFor = Exception.class)
public class ItemControlServiceImpl extends BaseServiceImpl<ItemControl> implements IItemControlService{

}