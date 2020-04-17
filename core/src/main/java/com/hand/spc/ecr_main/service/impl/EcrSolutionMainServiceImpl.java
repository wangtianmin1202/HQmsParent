package com.hand.spc.ecr_main.service.impl;

import com.hand.hap.core.IRequest;
import com.hand.hap.system.service.impl.BaseServiceImpl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hand.spc.ecr_main.dto.EcrMain;
import com.hand.spc.ecr_main.dto.EcrProcess;
import com.hand.spc.ecr_main.dto.EcrSolutionMain;
import com.hand.spc.ecr_main.mapper.EcrMainMapper;
import com.hand.spc.ecr_main.mapper.EcrProcessMapper;
import com.hand.spc.ecr_main.mapper.EcrSampleMapper;
import com.hand.spc.ecr_main.mapper.EcrSolutionMainMapper;
import com.hand.spc.ecr_main.service.IEcrSolutionMainService;
import com.hand.spc.ecr_main.view.EcrSolutionMainV0;

import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(rollbackFor = Exception.class)
public class EcrSolutionMainServiceImpl extends BaseServiceImpl<EcrSolutionMain> implements IEcrSolutionMainService{

	

	@Autowired
	private EcrSolutionMainMapper ecrSolutionMainMapper;
	
	@Autowired
	private EcrMainMapper ecrMainMapper;
	
	@Autowired
	private EcrProcessMapper ecrProcessMapper;
	
	/*
	 *   改善方案数据查询
	 */
	public List<EcrSolutionMainV0> baseQuery(IRequest iRequest,EcrSolutionMainV0 dto) {
		
		List<EcrSolutionMainV0> ecrSolutionMains=ecrSolutionMainMapper.baseQuery(dto);
		EcrMain ecrMain=new EcrMain();
		ecrMain.setEcrno(dto.getEcrno());
		
		List<EcrMain> ems=new ArrayList();
		ems=ecrMainMapper.select(ecrMain);
		
		if(ems.size()>0) {
			if(!iRequest.getEmployeeCode().equals(ems.get(0).getMainDuty())) {
				for(EcrSolutionMain esm:ecrSolutionMains) {
					esm.setStatus("C");
				}	
			}
		}
		EcrProcess ecrProcess=new EcrProcess();
		ecrProcess.setProcessCode("MatScrapSolutionApprOfECR");
		ecrProcess.setProcessStatus("A");
		ecrProcess.setEcrno(dto.getEcrno());
		int ct= ecrProcessMapper.selectCount(ecrProcess);
		if(ct>0) {
			for(EcrSolutionMain esm:ecrSolutionMains) {
				esm.setStatus("C");
			}	
		}
		if(ecrSolutionMains.size()>0) {
			return ecrSolutionMains;
		}
		EcrSolutionMainV0 esm=new EcrSolutionMainV0();
		esm.setStatus("C");
		ecrSolutionMains.add(esm);
		return ecrSolutionMains;
	}
	public void updateqvpTmp(String itemId) {
		
	}
	
}