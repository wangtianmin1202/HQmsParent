package com.hand.spc.ecr_main.service.impl;

import com.hand.hap.core.IRequest;
import com.hand.hap.system.service.impl.BaseServiceImpl;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.hand.spc.ecr_main.dto.EcrItemResult;
import com.hand.spc.ecr_main.dto.EcrMain;
import com.hand.spc.ecr_main.mapper.EcrItemResultMapper;
import com.hand.spc.ecr_main.mapper.EcrMainMapper;
import com.hand.spc.ecr_main.service.IApproveEcrService;
import com.hand.spc.ecr_main.service.IEcrItemResultService;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(rollbackFor = Exception.class)
public class ApproveEcrServiceImpl extends BaseServiceImpl<EcrItemResult> implements IApproveEcrService{
	
	private final Logger logger = LoggerFactory.getLogger(ApproveEcrServiceImpl.class); 
	
	@Autowired
	EcrItemResultMapper mapper;
	
	@Autowired
	EcrMainMapper mainMapper;

	@Override
	public void updateState(IRequest request, EcrItemResult dto, String kid) {
		logger.debug("------------1.物料管控物料处理方案 - 更新采用方案  start-------------");
		logger.debug("kid:" + kid);		
		if("".equals(dto.getEcrno()) || dto.getEcrno() == null){
			logger.debug("ecrno is null");
			return;
		}
		// 1.该组数据 审批结果已采用修改为未采用
		EcrItemResult rs = new EcrItemResult();
		rs.setEcrno(dto.getEcrno());
		rs.setStatust("B");
		mapper.updateState(rs);
		// 2. 本条数据审批结果修改为已采用
		dto.setStatust("A");
		mapper.updateState(dto);
		// 3. 修改流程状态
		updateApplyStatus(request, kid, "APPROVED", "90");
		logger.debug("------------1.物料管控物料处理方案 - 更新采用方案  end-------------");
	}

	@Override
	public void updateApplyStatus(IRequest requestCtx, String kid, String apply, String flowno) {
		logger.debug("------------1.物料管控物料处理方案-更新流程状态 start-------------");
		logger.debug("kid" + kid);
		logger.debug("apply" + apply);
		logger.debug("flowno" + flowno);
		
		String status = "A";
		boolean end = false;
		switch(flowno) {
			case "10":
			case "20":
			case "30":
			case "40":
			case "50":
			case "60":
				if("APPROVED".equals(apply)) {
					status = "A";
				}else if("REJECTED".equals(apply)) {
					status = "B";
				}
				break;
			case "70":
			case "80":
				if("APPROVED".equals(apply)) {
					status = "D";
					end = true;
				}else if("REJECTED".equals(apply)) {
					status = "B";
				}
				break;
			case "90":
				if("APPROVED".equals(apply)) {
					status = "A";
				}else if("REJECTED".equals(apply)) {
					status = "C";
					end = true;
				}
				break;
			default:
				break;
		}
		
		logger.debug("status" + status);
		
		// 更新流程状态
		EcrMain dto = new EcrMain();
		dto.setKid(Long.valueOf(kid));
		dto = mainMapper.selectByPrimaryKey(dto);
		dto.setProcessStatus(status);
		if (end) {
			dto.setProcessEndDate(new Date());
		}
		if(dto.getKid() != null) {
			mainMapper.updateByPrimaryKey(dto);
		}
		
		logger.debug("------------1.物料管控物料处理方案-更新流程状态 end-------------");

	}

}