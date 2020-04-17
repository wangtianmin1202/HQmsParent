package com.hand.npi.npi_technology.service;

import java.util.List;

import com.hand.hap.core.ProxySelf;
import com.hand.hap.system.service.IBaseService;
import com.hand.npi.npi_technology.dto.CharacteristicClassification;

public interface ICharacteristicClassificationService extends IBaseService<CharacteristicClassification>, ProxySelf<ICharacteristicClassificationService>{

	/**
	 * @author likai 2020.03.24
	 * @description 数据重复校验
	 * @param dtos
	 * @return
	 */
	boolean checkSame(List<CharacteristicClassification> dtos);
}