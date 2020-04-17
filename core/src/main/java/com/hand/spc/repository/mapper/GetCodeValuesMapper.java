package com.hand.spc.repository.mapper;

import java.util.List;

import com.hand.hap.mybatis.common.Mapper;
import com.hand.hap.system.dto.CodeValue;

public interface GetCodeValuesMapper extends Mapper<CodeValue> {
	List<CodeValue> getCodeValues(String codeName);
}
