package com.hand.spc.repository.service;

import java.util.List;

import com.hand.hap.core.ProxySelf;
import com.hand.hap.system.dto.CodeValue;
import com.hand.hap.system.service.IBaseService;

public interface IGetCodeValuesService extends IBaseService<CodeValue>, ProxySelf<IGetCodeValuesService> {
	
	List<CodeValue> getCodeValues(String codeName);
}
