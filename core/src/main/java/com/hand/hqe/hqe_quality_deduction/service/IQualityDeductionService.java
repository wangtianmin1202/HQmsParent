package com.hand.hqe.hqe_quality_deduction.service;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.hand.hap.core.IRequest;
import com.hand.hap.core.ProxySelf;
import com.hand.hap.system.dto.ResponseData;
import com.hand.hap.system.service.IBaseService;
import com.hand.hqe.hqe_quality_deduction.dto.QualityDeduction;

public interface IQualityDeductionService extends IBaseService<QualityDeduction>, ProxySelf<IQualityDeductionService>{

	/**
	 * 
	 * @param requestContext 请求
	 * @param qualityDeduction 质量扣款单据
	 * @param page 页数
	 * @param pageSize 每页记录数
	 * @return 质量扣款单据集合
	 */
	List<QualityDeduction>	query(IRequest requestContext, QualityDeduction qualityDeduction, int page, int pageSize);
	
	/**
	 * 获取最大流水号
	 * @param qualityDeduction 质量扣款单据
	 * @return 流水号最大值
	 */
	Integer queryMaxNum(QualityDeduction qualityDeduction);
	/**
	 * 生成结算单据
	 * @param requestContext 请求
	 * @param dto 质量扣款单据集合
	 * @return 质量扣款单据集合
	 */
	List<QualityDeduction> createSettlement(IRequest requestContext, List<QualityDeduction> dto);
	/**
	 * 取消
	 * @param requestContext 请求
	 * @param dto 质量扣款单据集合
	 * @return 质量扣款单据集合
	 */
	List<QualityDeduction> cancel(IRequest requestContext, List<QualityDeduction> dto);
	
	/**
	 * 上传文件
	 * 
	 * @param requestCtx 请求上下文
	 * @param request 请求
	 * @return
	 * @throws IllegalStateException
	 * @throws IOException
	 */
	ResponseData fileUpload(IRequest requestCtx, HttpServletRequest request) throws IllegalStateException, IOException;
	
	/**
	 * 删除文件
	 * @param requestContext 请求上下文
	 * @param dto 质量扣款单据集合
	 * @return
	 */
	int updaeDataAndDelFile(IRequest requestContext, List<QualityDeduction> dto);
}