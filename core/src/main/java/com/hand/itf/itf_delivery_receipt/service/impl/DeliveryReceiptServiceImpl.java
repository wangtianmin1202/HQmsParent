package com.hand.itf.itf_delivery_receipt.service.impl;

import com.hand.hap.system.service.impl.BaseServiceImpl;
import org.springframework.stereotype.Service;
import com.hand.itf.itf_delivery_receipt.dto.DeliveryReceipt;
import com.hand.itf.itf_delivery_receipt.service.IDeliveryReceiptService;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(rollbackFor = Exception.class)
public class DeliveryReceiptServiceImpl extends BaseServiceImpl<DeliveryReceipt> implements IDeliveryReceiptService{

}