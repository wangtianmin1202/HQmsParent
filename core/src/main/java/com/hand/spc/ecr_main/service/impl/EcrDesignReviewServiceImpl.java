package com.hand.spc.ecr_main.service.impl;

import com.hand.hap.core.IRequest;
import com.hand.hap.system.dto.CodeValue;
import com.hand.hap.system.service.ICodeService;
import com.hand.hap.system.service.impl.BaseServiceImpl;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.hand.spc.ecr_main.dto.EcrDesignReview;
import com.hand.spc.ecr_main.dto.EcrMain;
import com.hand.spc.ecr_main.mapper.EcrDesignReviewMapper;
import com.hand.spc.ecr_main.mapper.EcrMainMapper;
import com.hand.spc.ecr_main.service.IEcrDesignReviewService;
import com.hand.spc.ecr_main.service.IEcrProcessService;

import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(rollbackFor = Exception.class)
public class EcrDesignReviewServiceImpl extends BaseServiceImpl<EcrDesignReview> implements IEcrDesignReviewService{

	@Autowired
	EcrDesignReviewMapper designMapper;
	
	@Autowired
	private ICodeService codeService; 
	
	@Autowired
	private EcrMainMapper mainMapper;
	
	@Autowired
	private IEcrProcessService processService;
	
	Logger logger = LoggerFactory.getLogger(EcrDesignReviewServiceImpl.class);
	
	@Override
	public Boolean initData(IRequest request, String ecrno) {
		EcrDesignReview dto = new EcrDesignReview();
		dto.setEcrno(ecrno);
		// 评审负责人为 ECR负责人
		dto.setDutyby(request.getEmployeeCode());
		// 要求完成日期=提交日期+标准工时（快友维护）
		CodeValue cv = codeService.getCodeValue(request, "HPM_ERC_TRACKING", "7");
		Integer workDays = Integer.valueOf(cv.getTag());
		if(workDays != null) {
			Calendar calendar= Calendar.getInstance();
			calendar.setTime(new Date());
			calendar.add(Calendar.DAY_OF_YEAR, workDays);
			dto.setAskFinishedDate(calendar.getTime());
		}
		// 初始化数据
		designMapper.insertSelective(dto);
		return true;
	}

	@Override
	public void startProcess(IRequest request, String ecrno, String dutyby) {
		// 评审负责人为 ECR负责人（同申请人）
		request.setEmployeeCode(dutyby);
		initData(request, ecrno);
		// 发起工作流
		EcrMain main = new EcrMain();
		main.setEcrno(ecrno);
		List<EcrMain> mainList = mainMapper.select(main);
		if(mainList.isEmpty() && mainList.get(0) == null ) {
			logger.debug("========== 未找到 ecrId");
		} else {
			Long ecrId = mainList.get(0).getKid();
			processService.startTask(request, "designReview", request.getEmployeeCode(), ecrId, ecrno);
			logger.debug("========== 发起\"设计评审\"工作流 , 负责人：" + request.getEmployeeCode() + ", ecrno:" + ecrno);
		}
		
	}

}