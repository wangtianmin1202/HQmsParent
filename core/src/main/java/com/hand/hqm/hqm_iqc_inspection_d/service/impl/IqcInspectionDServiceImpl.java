package com.hand.hqm.hqm_iqc_inspection_d.service.impl;

import com.hand.hap.core.IRequest;
import com.hand.hap.system.service.impl.BaseServiceImpl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import com.hand.hqm.hqm_iqc_inspection_d.dto.IqcInspectionD;
import com.hand.hqm.hqm_iqc_inspection_d.service.IIqcInspectionDService;
import com.hand.hqm.hqm_iqc_inspection_l.dto.IqcInspectionL;

import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(rollbackFor = Exception.class)
public class IqcInspectionDServiceImpl extends BaseServiceImpl<IqcInspectionD> implements IIqcInspectionDService{

	@Override
	public List<IqcInspectionD> batchCreateByInspectionL(IRequest requestCtx,IqcInspectionL iqcInspectionL) {
		// TODO Auto-generated method stub
		List<IqcInspectionD> res = new ArrayList<>();
		for(int i=0;i<iqcInspectionL.getSampleSize().intValue();i++) {
			IqcInspectionD ind = new IqcInspectionD();
			ind.setLineId(iqcInspectionL.getLineId());
			ind.setOrderNum(String.valueOf(i));
			self().insertSelective(requestCtx, ind);
			res.add(ind);
		}
		return res;
	}

}