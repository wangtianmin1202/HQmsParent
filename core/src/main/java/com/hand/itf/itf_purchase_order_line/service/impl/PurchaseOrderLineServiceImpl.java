package com.hand.itf.itf_purchase_order_line.service.impl;

import com.hand.hap.system.service.impl.BaseServiceImpl;
import org.springframework.stereotype.Service;
import com.hand.itf.itf_purchase_order_line.dto.PurchaseOrderLine;
import com.hand.itf.itf_purchase_order_line.service.IPurchaseOrderLineService;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(rollbackFor = Exception.class)
public class PurchaseOrderLineServiceImpl extends BaseServiceImpl<PurchaseOrderLine> implements IPurchaseOrderLineService{

}