package com.hand.hqm.hqm_pqc_inspection_d.service.impl;

import com.hand.hap.core.IRequest;
import com.hand.hap.system.service.impl.BaseServiceImpl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hand.hqm.hqm_pqc_inspection_c.dto.PqcInspectionC;
import com.hand.hqm.hqm_pqc_inspection_d.dto.PqcInspectionD;
import com.hand.hqm.hqm_pqc_inspection_d.mapper.PqcInspectionDMapper;
import com.hand.hqm.hqm_pqc_inspection_d.service.IPqcInspectionDService;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(rollbackFor = Exception.class)
public class PqcInspectionDServiceImpl extends BaseServiceImpl<PqcInspectionD> implements IPqcInspectionDService {

	@Autowired
	PqcInspectionDMapper mapper;

	@Override
	public List<PqcInspectionD> batchCreateByInspectionC(IRequest requestCtx, PqcInspectionC pqcInspectionC) {
		// 如果已经存在_D表对应一批数据则查询 否则新增一批
		PqcInspectionD search = new PqcInspectionD();
		search.setConnectId(pqcInspectionC.getConnectId());
		List<PqcInspectionD> res = mapper.select(search);
		if (res == null || res.size() < 1) {
			res = new ArrayList<>();
			for (int i = 0; i < pqcInspectionC.getSampleQty().intValue(); i++) {
				PqcInspectionD ind = new PqcInspectionD();
				ind.setConnectId(pqcInspectionC.getConnectId());
				ind.setOrderNum(String.valueOf(i));
				self().insertSelective(requestCtx, ind);
				res.add(ind);
			}
		}
		return res;
	}

	@Override
	public void batchDeleteInspectionC(PqcInspectionC pqcInspectionC) {
		// TODO Auto-generated method stub
		PqcInspectionD search = new PqcInspectionD();
		search.setConnectId(pqcInspectionC.getConnectId());
		List<PqcInspectionD> res = mapper.select(search);
		for(PqcInspectionD pqcInspectionD : res) {
			mapper.deleteByPrimaryKey(pqcInspectionD);
		}
		
	}
}