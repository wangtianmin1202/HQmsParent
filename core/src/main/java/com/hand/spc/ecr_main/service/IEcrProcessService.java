package com.hand.spc.ecr_main.service;

import com.hand.hap.core.IRequest;
import com.hand.hap.core.ProxySelf;
import com.hand.hap.system.service.IBaseService;
import com.hand.spc.ecr_main.dto.EcrItemResult;
import com.hand.spc.ecr_main.dto.EcrMain;
import com.hand.spc.ecr_main.dto.EcrProcess;

public interface IEcrProcessService extends IBaseService<EcrProcess>, ProxySelf<IEcrProcessService>{

    /**
     *  1.1 启动流程入口(ECR&ECN 物料管控)
     * @param request
     * @return
     */
	void startMatScrap(IRequest requestCtx, EcrMain dto);
	
    /**
     * 1.2 流程中"再提交"按钮的业务功能(ECR&ECN 物料管控)
     * @param dto
     * @param request
     * @return
     */
	void matScrapCommit(IRequest requestContext, EcrItemResult dto, String ecrno);

    /**
     * 1.3 更新流程状态(ECR&ECN 物料管控)
     * @param request
     * @return
     */
	void matScrapStatus(IRequest requestCtx, String ecrno, String apply, String flowno);
	
	/**
	 * 2.1 启动流程入口(ECR 改善方案)
	 * @param request 
	 * @param dto EcrMain
	 * @return EcrMain
	 */
	void startSolution(IRequest request, EcrMain dto);

	/**
	 * 2.2  更新流程状态(ECR 改善方案)    
	 * @param requestCtx
	 * @param ecrno
	 * @param approveResult
	 * @param flowno
	 * @param skuCost
	 */
	void updateSolution(IRequest request, String ecrno, String approveResult, String flowno, Double skuCost);

	/**
	 * 3.1 启动流程入口(发布任务工作流)
	 * @param requestCtx
	 * @param processCode
	 * @param processEmployeeCode
	 * @param id
	 * @param ecrno
	 */
	void startTask(IRequest requestCtx, String processCode, String processEmployeeCode, Long id, String ecrno);


}