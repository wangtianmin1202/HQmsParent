package com.hand.hqm.hqm_fqc_inspection_h.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.hand.hap.core.IRequest;
import com.hand.hap.core.ProxySelf;
import com.hand.hap.system.dto.ResponseData;
import com.hand.hap.system.service.IBaseService;
import com.hand.hqm.hqm_fqc_inspection_h.dto.FqcInspectionH;
import com.hand.hqm.hqm_fqc_inspection_l.dto.FqcInspectionL;
import com.hand.hqm.hqm_qc_task.dto.FqcTask;

public interface IFqcInspectionHService extends IBaseService<FqcInspectionH>, ProxySelf<IFqcInspectionHService>{
	/**
	 * 
	 * @description 单号维度的查询
	 * @author tianmin.wang
	 * @date 2019年11月22日 
	 * @param requestContext
	 * @param dto
	 * @return
	 */
	List<FqcInspectionH> selectByNumber(IRequest requestContext, FqcInspectionH dto);

	/**
	 * 
	 * @description 暂挂检验单
	 * @author tianmin.wang
	 * @date 2019年11月22日 
	 * @param dto
	 * @param lineList
	 * @param request
	 * @return
	 * @throws Exception
	 */
	ResponseData pauseInspection(FqcInspectionH dto, List<FqcInspectionL> lineList, HttpServletRequest request) throws Exception;

	/**
	 * 
	 * @description 提交检验单
	 * @author tianmin.wang
	 * @date 2019年11月22日 
	 * @param dto
	 * @param lineList
	 * @param request
	 * @return
	 * @throws Exception
	 */
	ResponseData commitInspection(FqcInspectionH dto, List<FqcInspectionL> lineList, HttpServletRequest request, IRequest requestContext) throws Exception;

	/**
	 * 
	 * @description 检验单审核
	 * @author tianmin.wang
	 * @date 2019年11月22日 
	 * @param dto
	 * @param lineList
	 * @param request
	 * @return
	 * @throws Exception
	 */
	ResponseData auditInspection(FqcInspectionH dto, List<FqcInspectionL> lineList, HttpServletRequest request) throws Exception;

	/**
	 * 
	 * @description 添加新检验单
	 * @author tianmin.wang
	 * @date 2019年11月22日 
	 * @param dto
	 * @param requestCtx
	 * @param request
	 * @return
	 * @throws Exception
	 */
	ResponseData addNewInspection(FqcInspectionH dto,IRequest requestCtx, HttpServletRequest request) throws Exception;
	
	/**
	 * 
	 * @description qms的查询
	 * @author tianmin.wang
	 * @date 2019年11月22日 
	 * @param requestContext
	 * @param dto
	 * @param page
	 * @param pageSize
	 * @return
	 */
	List<FqcInspectionH> qmsQuery(IRequest requestContext,FqcInspectionH dto,int page, int pageSize);

	/**
	 * @description 加抽
	 * @author tianmin.wang
	 * @date 2019年11月21日 
	 * @param dto
	 * @param request
	 */
	void addSample(FqcInspectionH dto, HttpServletRequest request);

	/**
	 * @description
	 * @author tianmin.wang
	 * @date 2019年12月13日 
	 * @param dto
	 * @return
	 */
	List<FqcInspectionH> getLimitCount(FqcInspectionH dto);

	/**
	 * @description fqc检验任务生成检验单操作
	 * @author tianmin.wang
	 * @date 2020年1月7日 
	 * @param fqctask
	 * @param fqcinspectionh
	 * @param request
	 */
	void addNewInspection(FqcTask fqctask, FqcInspectionH fqcinspectionh, HttpServletRequest request);
	
	/**
	 * @description FQC加严抽检数反馈
	 * @author kai.li
	 * @date 2020年2月26日 
	 * @param rsd
	 */
	void samplingFeedback(ResponseData rsd);

	/**
	 * @description
	 * @author tianmin.wang
	 * @date 2020年3月25日 
	 * @param dto
	 * @param lineList
	 */
	void chooseInspectionType(FqcInspectionH dto, List<FqcInspectionL> lineList);

	/**
	 * @description
	 * @author tianmin.wang
	 * @date 2020年4月2日 
	 * @param requestContext
	 * @param dto
	 * @param page
	 * @param pageSize
	 * @return
	 */
	List<FqcInspectionH> reSelect(IRequest requestContext, FqcInspectionH dto, int page, int pageSize);
}