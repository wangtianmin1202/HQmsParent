package com.hand.hqm.hqm_fqc_inspection_d.service.impl;

import com.hand.hap.core.IRequest;
import com.hand.hap.system.service.impl.BaseServiceImpl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import com.hand.hqm.hqm_fqc_inspection_d.dto.FqcInspectionD;
import com.hand.hqm.hqm_fqc_inspection_d.service.IFqcInspectionDService;
import com.hand.hqm.hqm_fqc_inspection_l.dto.FqcInspectionL;

import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(rollbackFor = Exception.class)
public class FqcInspectionDServiceImpl extends BaseServiceImpl<FqcInspectionD> implements IFqcInspectionDService{
	@Override
	public List<FqcInspectionD> batchCreateByInspectionL(IRequest requestCtx,FqcInspectionL fqcInspectionL) {
		// TODO Auto-generated method stub
		List<FqcInspectionD> res = new ArrayList<>();
		for(int i=0;i<fqcInspectionL.getSampleQty().intValue();i++) {
			FqcInspectionD ind = new FqcInspectionD();
			ind.setLineId(fqcInspectionL.getLineId());
			ind.setOrderNum(String.valueOf(i));
			self().insertSelective(requestCtx, ind);
			res.add(ind);
		}
		return res;
	}
}