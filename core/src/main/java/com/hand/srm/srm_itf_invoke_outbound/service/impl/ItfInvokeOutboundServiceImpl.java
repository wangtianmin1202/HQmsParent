package com.hand.srm.srm_itf_invoke_outbound.service.impl;

import com.hand.hap.system.service.impl.BaseServiceImpl;
import org.springframework.stereotype.Service;
import com.hand.srm.srm_itf_invoke_outbound.dto.ItfInvokeOutbound;
import com.hand.srm.srm_itf_invoke_outbound.service.IItfInvokeOutboundService;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(rollbackFor = Exception.class)
public class ItfInvokeOutboundServiceImpl extends BaseServiceImpl<ItfInvokeOutbound> implements IItfInvokeOutboundService{

}