package com.hand.spc.ecr_main.service;

import java.util.List;

import com.hand.hap.core.IRequest;
import com.hand.hap.core.ProxySelf;
import com.hand.hap.system.service.IBaseService;
import com.hand.spc.ecr_main.dto.EcrDesignReview;

public interface IEcrDesignReviewService extends IBaseService<EcrDesignReview>, ProxySelf<IEcrDesignReviewService>{

    /**
     * 设计评审任务表初始化数据
     * @param request 评审负责人为ECR负责人(存在request 参数中)
     * @return
     */
	Boolean initData(IRequest request, String ecrno);

	/**
	 * 设计评审表-发起工作流
	 * @param request
	 * @param ecrno
	 * @param dutyby
	 * @return
	 */
	void startProcess(IRequest request, String ecrno, String dutyby);
}