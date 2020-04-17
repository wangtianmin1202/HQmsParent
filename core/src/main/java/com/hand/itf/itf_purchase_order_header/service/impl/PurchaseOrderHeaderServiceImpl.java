package com.hand.itf.itf_purchase_order_header.service.impl;

import com.hand.hap.system.service.impl.BaseServiceImpl;
import org.springframework.stereotype.Service;
import com.hand.itf.itf_purchase_order_header.dto.PurchaseOrderHeader;
import com.hand.itf.itf_purchase_order_header.service.IPurchaseOrderHeaderService;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(rollbackFor = Exception.class)
public class PurchaseOrderHeaderServiceImpl extends BaseServiceImpl<PurchaseOrderHeader> implements IPurchaseOrderHeaderService{

}