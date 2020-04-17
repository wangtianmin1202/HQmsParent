package com.hand.npi.npi_technology.service.impl;

import com.hand.hap.system.service.impl.BaseServiceImpl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hand.npi.npi_technology.dto.CharacteristicClassification;
import com.hand.npi.npi_technology.mapper.CharacteristicClassificationMapper;
import com.hand.npi.npi_technology.service.ICharacteristicClassificationService;

import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(rollbackFor = Exception.class)
public class CharacteristicClassificationServiceImpl extends BaseServiceImpl<CharacteristicClassification> implements ICharacteristicClassificationService{
	@Autowired
	CharacteristicClassificationMapper characteristicClassificationMapper;

	/**
	 * @author likai 2020.03.24
	 * @description 数据重复校验
	 * @param dtos
	 * @return
	 */
	@Override
	public boolean checkSame(List<CharacteristicClassification> dtos) {
		if(dtos == null || dtos.size() == 0) {
			return false;
		}
		
		boolean isSame = false;
		List<CharacteristicClassification> list = new ArrayList<CharacteristicClassification>();
		for(CharacteristicClassification ydto:dtos) {
			list.add(ydto);
		}
		
		List<CharacteristicClassification> listTable = characteristicClassificationMapper.selectAll();
		if(listTable != null && listTable.size() > 0) {
			for(CharacteristicClassification dtoTable:listTable) {
				boolean isIn = false;
				for(CharacteristicClassification dto:dtos) {
					if(dto.getCharacteristicId() != null) {
						if(dtoTable.getCharacteristicId() == dto.getCharacteristicId()) {
							isIn = true;
						}
					}
				}
				
				if(!isIn) {
					list.add(dtoTable);
				}
			}
		}
		
		if(list.size() == 1) {
			return false;
		}
		for(int i = 0; i < list.size(); i++) {
			CharacteristicClassification li = list.get(i);
			for(int j = i+1; j < list.size(); j++) {
				boolean check = false;
				
				if(!li.getCharacteristicName().equals(list.get(j).getCharacteristicName())) {
					check = true;
				}
				if(!li.getMeteringCount().equals(list.get(j).getMeteringCount())) {
					check = true;
				}
				
				if(!check) {
					return true;
				}
			}
			
		}
		return isSame;
	}
	
}