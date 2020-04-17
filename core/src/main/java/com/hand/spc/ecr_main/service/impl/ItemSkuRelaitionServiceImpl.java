package com.hand.spc.ecr_main.service.impl;

import com.hand.hap.system.service.impl.BaseServiceImpl;
import org.springframework.stereotype.Service;
import com.hand.spc.ecr_main.dto.ItemSkuRelaition;
import com.hand.spc.ecr_main.service.IItemSkuRelaitionService;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(rollbackFor = Exception.class)
public class ItemSkuRelaitionServiceImpl extends BaseServiceImpl<ItemSkuRelaition> implements IItemSkuRelaitionService{

}