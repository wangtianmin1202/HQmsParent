package com.hand.spc.ecr_main.service;

import java.util.Date;
import java.util.List;

import com.hand.hap.core.IRequest;
import com.hand.hap.core.ProxySelf;
import com.hand.hap.system.service.IBaseService;
import com.hand.spc.ecr_main.dto.EcrRfq;

public interface IEcrRfqService extends IBaseService<EcrRfq>, ProxySelf<IEcrRfqService>{

	/**
	 * RFQ 任务写到任务列表
	 * @param request 
	 * @param ecrno
	 */
	List<EcrRfq> initData(IRequest request, String ecrno, List<String> itemIds);

	/**
	 * - 查询任务流程数据
	 * @param requestContext
	 * @param dto
	 * @return
	 */
	List<EcrRfq> selectTask(IRequest requestContext, EcrRfq dto, int page, int pageSize);

	/**
	 * - 保存负责人确认节点信息
	 * @param requestCtx
	 * @param dto
	 * @return
	 */
	List<EcrRfq> saveTask(IRequest request, List<EcrRfq> dto, String taskType);
	
	/**
	 * -查询任务完成状态，完成时将新版本和时间更新到任务表中,再结束流程
	 * @param request
	 * @param taskType
	 * @param ecrno
	 */
	void taskFinish(IRequest request, String taskType, String ecrno, String skuId, String newVersion, String taskStatus, Date actFinishDate);
	
	/**
	 * -返回当前版本（不需修改的情况）
	 * @param request
	 * @param taskType
	 * @param ecrno
	 */
	void returnNewVersion(IRequest request, String taskType, String ecrno, String skuId);

	/**
	 * 初始化  RFQ 数据 + 发起 RFQ 工作流
	 * @param request
	 * @param ecrno
	 * @param itemIds
	 */
	void startProcessRfq(IRequest request, String ecrno, List<String> itemIds);

	/**
	 * - 查询 isNeed 字段，更新 hpm_ecr_main 字段是否 QTP，VTP 要做
	 * @param requestCtx
	 * @param ecrno
	 * @param taskType
	 * @param id
	 * @param dutyby
	 */
	void updateIsNeed(IRequest requestCtx, String ecrno, String taskType, String id, String dutyby);



}