package com.hand.hcs.hcs_barcode.service.impl;

import com.hand.hap.core.IRequest;
import com.hand.hap.system.service.impl.BaseServiceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.hand.hcs.hcs_barcode.dto.SmallBarcodeControl;
import com.hand.hcs.hcs_barcode.mapper.SmallBarcodeControlMapper;
import com.hand.hcs.hcs_barcode.service.ISmallBarcodeControlService;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(rollbackFor = Exception.class)
public class SmallBarcodeControlServiceImpl extends BaseServiceImpl<SmallBarcodeControl> implements ISmallBarcodeControlService{
	
	@Autowired
	private SmallBarcodeControlMapper mapper;
	@Override
	public void updateObarcodeId(IRequest requestContext, SmallBarcodeControl dto) {
		// TODO Auto-generated method stub
		mapper.updateObarcodeId(dto);
	}
	@Override
	public void updateTicketIdBySbarcodeId(SmallBarcodeControl dto) {
		// TODO Auto-generated method stub
		mapper.updateTicketIdBySbarcodeId(dto);
	}
	@Override
	public void updateTicketIdByLineId(SmallBarcodeControl dto) {
		// TODO Auto-generated method stub
		mapper.updateTicketIdByLineId(dto);
	}

}