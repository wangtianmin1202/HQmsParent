package com.hand.sys.sys_if_invoke_outbound.mapper;

import java.util.Map;

import com.hand.hap.mybatis.common.Mapper;
import com.hand.sys.sys_if_invoke_outbound.dto.IfInvokeOutbound;

public interface IfInvokeOutboundMapper extends Mapper<IfInvokeOutbound>{

	void apsOperation(Map<String,String> map);
}