package com.hand.npi.npi_route.service.impl;

import com.google.common.collect.Lists;
import com.hand.hap.core.IRequest;
import com.hand.hap.system.dto.DocSequence;
import com.hand.hap.system.service.IDocSequenceService;
import com.hand.hap.system.service.impl.BaseServiceImpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hand.npi.npi_route.dto.TechnologyWpActionEquipDetail;
import com.hand.npi.npi_route.mapper.TechnologyWpActionEquipDetailMapper;
import com.hand.npi.npi_route.service.ITechnologyWpActionEquipDetailService;

import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(rollbackFor = Exception.class)
public class TechnologyWpActionEquipDetailServiceImpl extends BaseServiceImpl<TechnologyWpActionEquipDetail> implements ITechnologyWpActionEquipDetailService{

	@Autowired
	private TechnologyWpActionEquipDetailMapper technologyWpActionEquipDetailMapper;
	
	@Autowired
	IDocSequenceService iDocSequenceService;
	
	@Override
	public List<TechnologyWpActionEquipDetail> addNewData(IRequest request, TechnologyWpActionEquipDetail dto) {
		List<TechnologyWpActionEquipDetail> resultList = Lists.newLinkedList();
		DocSequence docSequence = new DocSequence();
		docSequence.setDocType("equipCodeSeq");
		String equipCode = iDocSequenceService.getSequence(request, docSequence, "EM", 6, 1L);
		dto.setEquipmentCode(equipCode);
		technologyWpActionEquipDetailMapper.insertSelective(dto);
		resultList.add(dto);
		return resultList;
	}

}