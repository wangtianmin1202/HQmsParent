package com.hand.hqm.hqm_item_control_his.service.impl;

import com.hand.hap.system.service.impl.BaseServiceImpl;
import org.springframework.stereotype.Service;
import com.hand.hqm.hqm_item_control_his.dto.ItemControlHis;
import com.hand.hqm.hqm_item_control_his.service.IItemControlHisService;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(rollbackFor = Exception.class)
public class ItemControlHisServiceImpl extends BaseServiceImpl<ItemControlHis> implements IItemControlHisService{

}