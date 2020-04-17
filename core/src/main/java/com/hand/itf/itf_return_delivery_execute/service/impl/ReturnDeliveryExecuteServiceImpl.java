package com.hand.itf.itf_return_delivery_execute.service.impl;

import com.hand.hap.system.service.impl.BaseServiceImpl;
import org.springframework.stereotype.Service;
import com.hand.itf.itf_return_delivery_execute.dto.ReturnDeliveryExecute;
import com.hand.itf.itf_return_delivery_execute.service.IReturnDeliveryExecuteService;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(rollbackFor = Exception.class)
public class ReturnDeliveryExecuteServiceImpl extends BaseServiceImpl<ReturnDeliveryExecute> implements IReturnDeliveryExecuteService{

}