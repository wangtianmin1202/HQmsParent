package com.hand.spc.ecr_main.service;

import java.util.List;

import com.hand.hap.core.IRequest;
import com.hand.hap.core.ProxySelf;
import com.hand.hap.system.service.IBaseService;
import com.hand.spc.ecr_main.dto.EcrItemResult;

public interface IApproveEcrService extends IBaseService<EcrItemResult>, ProxySelf<IApproveEcrService>{

	/**
	 * 流程中"再提交"按钮的业务功能
	 * @param request 请求
	 * @param dto 实体
	 */
	void updateState(IRequest request, EcrItemResult dto, String kid);

	/**
	 * 更新流程状态
	 * @param requestCtx
	 * @param kid
	 * @param apply
	 * @param flowno
	 */
	void updateApplyStatus(IRequest requestCtx, String kid, String apply, String flowno);

}