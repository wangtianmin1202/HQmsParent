package com.hand.hqm.hqm_msa_check_plan.service;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.hand.hap.core.IRequest;
import com.hand.hap.core.ProxySelf;
import com.hand.hap.system.dto.ResponseData;
import com.hand.hap.system.service.IBaseService;
import com.hand.hqm.hqm_msa_check_plan.dto.MetCheckPlanLine;

public interface IMetCheckPlanLineService extends IBaseService<MetCheckPlanLine>, ProxySelf<IMetCheckPlanLineService>{
	public List<MetCheckPlanLine> selectCheckPlanLine(MetCheckPlanLine metCheckPlanLine);
	/**
	 * 文件上传
	 * @param requestCtx 请求上下文
	 * @param request 请求
	 * @return
	 * @throws IllegalStateException
	 * @throws IOException
	 */
	ResponseData fileUpload(IRequest requestCtx, HttpServletRequest request) throws IllegalStateException, IOException;
	/**
	 * 更新
	 * @param dto 校验历史集合
	 * @param requestCtx 请求上下文
	 * @return
	 */
	public ResponseData update(List<MetCheckPlanLine> dto, IRequest requestCtx);
	/**
	 * 编辑
	 * @param requestContext 请求上下文
	 * @param dto
	 */
	void updateData(IRequest requestContext,MetCheckPlanLine dto);
	/**
	 * 校验历史查询
	 * @param requestContext 请求上下文
	 * @param metCheckPlanLine 校验历史
	 * @param page 页码
	 * @param pageSize 页大小
	 * @return 结果集
	 */
	List<MetCheckPlanLine> query(IRequest requestContext,MetCheckPlanLine metCheckPlanLine,int page, int pageSize);
}