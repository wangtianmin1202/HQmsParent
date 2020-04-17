package com.hand.hcm.hcm_item_b.service.impl;

import com.hand.hap.system.service.impl.BaseServiceImpl;
import org.springframework.stereotype.Service;
import com.hand.hcm.hcm_item_b.dto.ItemTl;
import com.hand.hcm.hcm_item_b.service.IItemTlService;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(rollbackFor = Exception.class)
public class ItemTlServiceImpl extends BaseServiceImpl<ItemTl> implements IItemTlService{

}