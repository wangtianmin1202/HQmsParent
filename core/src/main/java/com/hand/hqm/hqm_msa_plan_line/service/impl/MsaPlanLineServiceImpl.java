package com.hand.hqm.hqm_msa_plan_line.service.impl;

import com.hand.hap.core.IRequest;
import com.hand.hap.system.service.impl.BaseServiceImpl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hand.hqm.hqm_msa_plan.dto.MsaPlan;
import com.hand.hqm.hqm_msa_plan.service.IMsaPlanService;
import com.hand.hqm.hqm_msa_plan_line.dto.MsaPlanLine;
import com.hand.hqm.hqm_msa_plan_line.mapper.MsaPlanLineMapper;
import com.hand.hqm.hqm_msa_plan_line.service.IMsaPlanLineService;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(rollbackFor = Exception.class)
public class MsaPlanLineServiceImpl extends BaseServiceImpl<MsaPlanLine> implements IMsaPlanLineService{
	@Autowired
	private MsaPlanLineMapper mapper;
	@Autowired
	private IMsaPlanService msaPlanService;
	
	@Override
	public void updateResult(IRequest requestContext, MsaPlanLine dto) {
		MsaPlanLine msaPlanLine = new MsaPlanLine();
		msaPlanLine.setMeasurePlanId(dto.getMeasurePlanId());
		msaPlanLine.setMsaContent(dto.getMsaContent());
		
		msaPlanLine = mapper.selectOne(msaPlanLine);
		if(msaPlanLine != null) {
			msaPlanLine.setMsaResult(dto.getMsaResult());
			msaPlanLine.setMsaConclusion(dto.getMsaConclusion());
			self().updateByPrimaryKeySelective(requestContext, msaPlanLine);
		}
		
		MsaPlan msaPlan = new MsaPlan();
		msaPlan.setMsaPlanId(dto.getMeasurePlanId());
		msaPlan = msaPlanService.selectByPrimaryKey(requestContext, msaPlan);
		
		if(msaPlan!=null && "1".equals(msaPlan.getMeasurePlanStatus())) {
			msaPlan.setMeasurePlanStatus("2");
			msaPlanService.updateByPrimaryKeySelective(requestContext, msaPlan);
		}
		
	}

	@Override
	public List<MsaPlanLine> updateAnalyzeData(IRequest requestContext, MsaPlanLine dto) {
		// TODO Auto-generated method stub
		
		MsaPlanLine msaPlanLine = self().selectByPrimaryKey(requestContext,dto);
		if(msaPlanLine != null) {
			msaPlanLine.setAnalyzeDate(new Date());
			msaPlanLine = self().updateByPrimaryKeySelective(requestContext, msaPlanLine);
			List<MsaPlanLine> list = new ArrayList();
			list.add(msaPlanLine);
			return list;
		}else {
			throw new RuntimeException("获取行失败");
		}
	}

}