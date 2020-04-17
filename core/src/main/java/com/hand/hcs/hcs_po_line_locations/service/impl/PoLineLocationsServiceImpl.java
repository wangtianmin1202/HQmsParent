package com.hand.hcs.hcs_po_line_locations.service.impl;

import com.github.pagehelper.PageHelper;
import com.hand.hap.account.dto.User;
import com.hand.hap.account.service.IUserService;
import com.hand.hap.core.IRequest;
import com.hand.hap.system.service.impl.BaseServiceImpl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hand.hcs.hcs_po_headers.dto.PoHeaders;
import com.hand.hcs.hcs_po_headers.service.IPoHeadersService;
import com.hand.hcs.hcs_po_line_locations.dto.PoLineLocations;
import com.hand.hcs.hcs_po_line_locations.mapper.PoLineLocationsMapper;
import com.hand.hcs.hcs_po_line_locations.service.IPoLineLocationsService;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(rollbackFor = Exception.class)
public class PoLineLocationsServiceImpl extends BaseServiceImpl<PoLineLocations> implements IPoLineLocationsService{
	
	@Autowired
	private PoLineLocationsMapper mapper;
	@Autowired
	private IUserService userService;
	@Autowired
	private IPoHeadersService poHeadersService;

	@Override
	public void saveInfo(IRequest requestContext, List<PoLineLocations> dto) {
		
		for(PoLineLocations poLineLocations : dto) {
			//Date promisedDate = poLineLocations.getPromisedDate();
			poLineLocations = mapper.selectByPrimaryKey(poLineLocations);
			poLineLocations.setShipmentStatus("A");
			poLineLocations.setConfirmTime(new Date());
			poLineLocations.setConfirmFlag("Y");
			poLineLocations.setConfirmBy((float)requestContext.getUserId());
			//poLineLocations.setPromisedDate(promisedDate);
			self().updateByPrimaryKey(requestContext, poLineLocations);
		}
		PoLineLocations lineLocations = new PoLineLocations();
		lineLocations.setPoHeaderId(dto.get(0).getPoHeaderId());
		List<PoLineLocations> PoLineLocationsList = self().select(requestContext, lineLocations, 0, 0);
		long count = PoLineLocationsList.stream().filter(data -> "P".equals(data.getShipmentStatus())).count();
		if(count == 0) {
			PoHeaders poHeaders = new PoHeaders();
			poHeaders.setPoHeaderId(dto.get(0).getPoHeaderId());
			poHeaders = poHeadersService.selectByPrimaryKey(requestContext, poHeaders);
			//更新采购订单头
			poHeaders.setPoStatus("A");
			poHeaders.setConfirmTime(new Date());
			poHeaders.setConfirmFlag("Y");
			poHeadersService.updateByPrimaryKeySelective(requestContext, poHeaders);
		}
		PoHeaders head = new PoHeaders();
		head.setPoHeaderId(dto.get(0).getPoHeaderId());
		
		
	}

	@Override
	public List<PoLineLocations> queryPoLineLocations(IRequest requestContext, PoLineLocations poLineLocations,
			int page, int pageSize) {
		PageHelper.startPage(page,pageSize);
		return mapper.queryPoLineLocations(poLineLocations);
	}

	@Override
	public List<PoLineLocations> queryPoLineDetail(IRequest requestContext, PoLineLocations poLineLocations, int page,
			int pageSize) {
		PageHelper.startPage(page,pageSize);
		return mapper.queryPoLineDetail(poLineLocations);
	}

	@Override
	public List<PoLineLocations> close(IRequest requestContext, List<PoLineLocations> dto) {
		for(PoLineLocations poLineLocations : dto){
			PoLineLocations lineLocations = new PoLineLocations();
			lineLocations.setLineLocationId(poLineLocations.getLineLocationId());
			lineLocations = mapper.selectByPrimaryKey(lineLocations);
			if(lineLocations != null && !"C".equals(lineLocations.getShipmentStatus())) {				
				lineLocations.setShipmentStatus("C");
				lineLocations.setClosedBy((float)requestContext.getUserId());
				lineLocations.setClosedTime(new Date());
				
				self().updateByPrimaryKeySelective(requestContext, lineLocations);
			}
		}
		return dto;
	}

	@Override
	public List<PoLineLocations> queryLocationDetail(IRequest requestContext, PoLineLocations poLineLocations, int page,
			int pageSize) {
		PageHelper.startPage(page,pageSize);
		return mapper.queryLocationDetail(poLineLocations);
	}

}