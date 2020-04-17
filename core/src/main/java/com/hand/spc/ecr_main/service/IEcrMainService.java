package com.hand.spc.ecr_main.service;

import java.util.Date;
import java.util.List;

import com.hand.hap.core.IRequest;
import com.hand.hap.core.ProxySelf;
import com.hand.hap.system.dto.ResponseData;
import com.hand.hap.system.service.IBaseService;
import com.hand.spc.ecr_main.dto.EcrMain;
import com.hand.spc.ecr_main.dto.EcrSolutionSku;
import com.hand.spc.ecr_main.view.EcrMainV0;
import com.hand.spc.ecr_main.view.EcrMainV1;
import com.hand.spc.ecr_main.view.EcrMainV2;
import com.hand.spc.ecr_main.view.EcrTechnicalFileLineV0;

public interface IEcrMainService extends IBaseService<EcrMain>, ProxySelf<IEcrMainService>{
	/*
	 * 提交创建ECR申请
	 */
	public ResponseData createOrder(IRequest iRequest,EcrMainV0 dto);
	/*
	 * 查询申请信息
	 */
	public List<EcrMainV0> selectSingleOrder(IRequest iRequest,EcrMain dto);
	
	/*
	 * 主界面左边查询功能 
	 */
	public List<EcrMainV1> baseQuery(IRequest iRequest,EcrMainV1 dto, int page, int pageSize);
	/*
	 * 提交主负责人功能
	*/
	public ResponseData saveMainDuty(IRequest iRequest,EcrMainV2 dto);
	public void saveDutys(EcrMainV2 dto);

	/**
	 * 启动流程
	 * @param request 
	 * @param dto EcrMain
	 * @return EcrMain
	 */
	public EcrMain startProcess(IRequest request, EcrMain dto);
	
	/**
	 *    根据风险和类型 获取预测时间
	 */
	public Date getChangeFinishedDate(IRequest iRequest,String type,Date oldFinishedDate,String risk);
	/**
	 * 负责人分配逻辑编辑
	*/
	public void allocationTechBy(IRequest iRequest,EcrTechnicalFileLineV0 dto); 
}

