package com.hand.hqm.hqm_item_type_tests_his.service.impl;

import com.hand.hap.system.service.impl.BaseServiceImpl;
import org.springframework.stereotype.Service;
import com.hand.hqm.hqm_item_type_tests_his.dto.ItemTypeTestsHis;
import com.hand.hqm.hqm_item_type_tests_his.service.IItemTypeTestsHisService;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(rollbackFor = Exception.class)
public class ItemTypeTestsHisServiceImpl extends BaseServiceImpl<ItemTypeTestsHis> implements IItemTypeTestsHisService{

}