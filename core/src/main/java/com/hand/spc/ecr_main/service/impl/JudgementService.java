package com.hand.spc.ecr_main.service.impl;

import org.activiti.engine.impl.persistence.entity.TaskEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hand.spc.ecr_main.dto.EcrDesignReview;
import com.hand.spc.ecr_main.dto.EcrProcess;
import com.hand.spc.ecr_main.dto.EcrRfq;
import com.hand.spc.ecr_main.mapper.EcrDesignReviewMapper;
import com.hand.spc.ecr_main.mapper.EcrItemSkuMapper;
import com.hand.spc.ecr_main.mapper.EcrProcessMapper;
import com.hand.spc.ecr_main.mapper.EcrRfqMapper;
import com.hand.spc.ecr_main.mapper.EcrSolutionSkuMapper;
import com.hand.spc.ecr_main.service.IEcrRfqService;
import com.hand.spc.ecr_main.view.EcrItemSkuV0;
import com.hand.spc.ecr_main.view.EcrSolutionSkuV0;
import com.hand.spc.utils.MailUtil;
import net.logstash.logback.encoder.org.apache.commons.lang.StringUtils;

import com.github.pagehelper.PageHelper;
import com.hand.hap.activiti.custom.IActivitiBean;
import com.hand.hap.core.IRequest;
import com.hand.hap.core.impl.DefaultRequestListener;
import com.hand.hap.hr.dto.Employee;
import com.hand.hap.hr.mapper.EmployeeMapper;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.activiti.engine.delegate.DelegateExecution;

@Service
public class JudgementService implements IActivitiBean {
	
	@Autowired
	private EmployeeMapper empMapper;
	
	@Autowired
	private EcrRfqMapper rfqMapper;
	
	@Autowired
	private EcrSolutionSkuMapper eiSkuMapper;
	
	@Autowired
	EcrProcessMapper processMapper;
	
	@Autowired
	EcrDesignReviewMapper designMapper;
	
	@Autowired
	private IEcrRfqService rfqService;
	 
	private final Logger logger = LoggerFactory.getLogger(JudgementService.class); 
	
	private static final String MATESCRAPEM = "matScrapEmail";
	private static final String MATESCRAPEC = "matScrapEcrno";
	private static final String SOLUTIONEM = "solutionEmail";
	private static final String SOLUTIONEC = "solutionEcrno";
	
	/**
	 * 2. ECR改善方案-判断成本有变化
	 * @param execution
	 * @param tasks
	 * @param empCode
	 */
	public void solution(DelegateExecution exe, String approveResult) {
		logger.debug("----------2.ECR改善方案--判断成本有变化  start-----------");
		String bk = exe.getProcessInstanceBusinessKey();
		String flowName = exe.getCurrentActivityId();
		String ecrno = (String) exe.getVariable(SOLUTIONEC);
		
		// 查询 skuCostChange 成本是否有变化初始值
		List<EcrSolutionSkuV0> ecrItemSkus = eiSkuMapper.getHeadSql(ecrno);
		Double skuCost = 0.0;
		Boolean skuCostChange = true;
		int i = 1;
		for(EcrSolutionSkuV0 sku : ecrItemSkus){
			if(!"".equals(sku.getSumItemCost()) && null != sku.getSumItemCost()) {
				skuCost = skuCost + Double.valueOf(sku.getSumItemCost());
			}
			if(!"".equals(sku.getSumWorkCost()) && null != sku.getSumWorkCost()) {
				skuCost = skuCost + Double.valueOf(sku.getSumWorkCost());
			}
			logger.debug("sku["+ i +"].getSumItemCost:" + sku.getSumItemCost());
			logger.debug("sku["+ i++ +"].getSumWorkCost:" + i++ + sku.getSumWorkCost());
		}
		if(skuCost == 0.0) {
			skuCostChange = false;
		}
		// 设置流程变量
		exe.setVariable("skuCost", skuCost);
		exe.setVariable("skuCostChange", skuCostChange);
		
		logger.debug("bk: " + bk);
		logger.debug("flowName:" + flowName);
		logger.debug("approveResult: "+ approveResult);
		logger.debug("ecrno" + ecrno);
		logger.debug("skuCost: " + skuCost);
		logger.debug("skuCostChange: " + skuCostChange);
		
		// 判断审批结果和成本有变化的值
		if("APPROVED".equals(approveResult) && skuCostChange) { 
			exe.setVariable("flag", "change");
		} else if("APPROVED".equals(approveResult)) {
			exe.setVariable("flag", "noChange");
		} else {
			exe.setVariable("flag", "false");
		}
		
		logger.debug("flag: "+ exe.getVariable("flag"));
		logger.debug("----------2.ECR改善方案--判断成本有变化 end-----------");
	}

	/**
	 * 3.  任务发布给各模块流程--查询是否需要变更
	 * @param exe
	 * @param approveResult
	 */
	public void isNeed(DelegateExecution exe, String approveResult) {
		logger.debug("----------3.  任务发布给各模块流程--查询是否需要变更  start-----------");
		String bk = exe.getProcessInstanceBusinessKey();  // 文件修改：headId,其他：ecrId
		String ecrno = (String) exe.getVariable("ecrno");
		String taskType = (String) exe.getVariable("processCode");
    	List<EcrRfq> list = new ArrayList<>();
    	if(taskType == null) {
    		return;
    	}
    	String isNeed = "N";
    	EcrRfq dto = new EcrRfq();
		dto.setEcrno(ecrno);
    	switch(taskType) {
    		case "QTP":
    			list = rfqMapper.selectTaskQtp(dto);
    			if(!list.isEmpty() && list.get(0) != null && "Y".equals(list.get(0).getIsNeed())) {
    				isNeed = "Y";
    			}
    			break;
    		case "VTP":
    			list = rfqMapper.selectTaskVtp(dto);
    			if(!list.isEmpty() && list.get(0) != null && "Y".equals(list.get(0).getIsNeed())) {
    				isNeed = "Y";
    			}
    			break;
    		case "PCI":
    			list = rfqMapper.selectTaskPci(dto);
    			if(!list.isEmpty() && list.get(0) != null && "Y".equals(list.get(0).getIsNeed())) {
    				isNeed = "Y";
    			}
    			break;
    		case "RFQ":
    			// 添加责任人条件
    			String dutyby = (String) exe.getVariable("dutyby");
    			dto.setDutyby(dutyby);
    			list = rfqMapper.selectTaskRfq(dto);
    			if(!list.isEmpty() && list.get(0) != null && "Y".equals(list.get(0).getIsNeed())) {
    				isNeed = "Y";
    			}
    			break;
    		case "designReview":
    			EcrDesignReview edr = new EcrDesignReview();
    			edr.setEcrno(ecrno);
    			List<EcrDesignReview> edrList = designMapper.select(edr);
    			if(!edrList.isEmpty() && edrList.get(0) != null && "Y".equals(edrList.get(0).getIsNeed())) {
    				isNeed = "Y";
    			}
    			break;
			default:
				// 添加 bk 作为 head 表的 id
				dto.setId(Long.valueOf(bk));
    			list = rfqMapper.selectTaskFile(dto);
    			if(!list.isEmpty() && list.get(0) != null && "Y".equals(list.get(0).getEditFlag())) {
    				isNeed = "Y";
    			}
    			break;
    	}
    	// 逻辑待修改
		if("Y".equals(isNeed)) {
			exe.setVariable("isNeed", "Y");
		}else {
			exe.setVariable("isNeed", "N");
		}
		logger.debug("----------3.  任务发布给各模块流程--查询是否需要变更  end-----------");
	}
	
	/**
	 * 3.  任务发布给各模块流程--返回当前版本给 ERC（不需要）
	 * @param exe
	 * @param approveResult
	 */
	public void returnNewVersion(DelegateExecution exe) {
		IRequest request = new DefaultRequestListener().newInstance();
		String bk = exe.getProcessInstanceBusinessKey();
		String ecrno = (String) exe.getVariable("ecrno");
		String taskType = (String) exe.getVariable("processCode");
		logger.debug("ecrno:" + ecrno + ",bk:" + bk + ",taskType:" + taskType);
		rfqService.returnNewVersion(request, taskType, ecrno, null);
	}
	
	@Override
	public String getBeanName() {
		return "JudgementService";
	}
	
}
