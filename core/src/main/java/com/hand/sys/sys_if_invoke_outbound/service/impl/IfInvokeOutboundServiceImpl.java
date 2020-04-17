package com.hand.sys.sys_if_invoke_outbound.service.impl;

import com.hand.hap.system.service.impl.BaseServiceImpl;
import org.springframework.stereotype.Service;
import com.hand.sys.sys_if_invoke_outbound.dto.IfInvokeOutbound;
import com.hand.sys.sys_if_invoke_outbound.service.IIfInvokeOutboundService;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(rollbackFor = Exception.class)
public class IfInvokeOutboundServiceImpl extends BaseServiceImpl<IfInvokeOutbound> implements IIfInvokeOutboundService{

}