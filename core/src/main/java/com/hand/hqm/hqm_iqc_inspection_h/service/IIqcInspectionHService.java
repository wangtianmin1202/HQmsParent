package com.hand.hqm.hqm_iqc_inspection_h.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.hand.hap.core.IRequest;
import com.hand.hap.core.ProxySelf;
import com.hand.hap.system.dto.ResponseData;
import com.hand.hap.system.service.IBaseService;
import com.hand.hqm.hqm_iqc_inspection_h.dto.IqcInspectionH;
import com.hand.hqm.hqm_iqc_inspection_l.dto.IqcInspectionL;
import com.hand.hqm.hqm_qc_task.dto.IqcTask;

public interface IIqcInspectionHService extends IBaseService<IqcInspectionH>, ProxySelf<IIqcInspectionHService>{

	/**
	 * 
	 * @description 流水
	 * @author tianmin.wang
	 * @date 2019年11月22日 
	 * @param requestContext
	 * @param dto
	 * @param page
	 * @param pageSize
	 * @return
	 */
	List<IqcInspectionH> selectByNumber(IRequest requestContext, IqcInspectionH dto,int page,int pageSize);
	
	/**
	 * 
	 * @description 暂挂
	 * @author tianmin.wang
	 * @date 2019年11月22日 
	 * @param dto
	 * @param lineList
	 * @param request
	 * @return
	 * @throws Exception
	 */
	ResponseData pauseInspection(IqcInspectionH dto, List<IqcInspectionL> lineList, HttpServletRequest request) throws Exception;

	/**
	 * 
	 * @description 提交
	 * @author tianmin.wang
	 * @date 2019年11月22日 
	 * @param dto
	 * @param lineList
	 * @param request
	 * @return
	 * @throws Exception
	 */
	ResponseData commitInspection(IqcInspectionH dto, List<IqcInspectionL> lineList, HttpServletRequest request, IRequest requestCtx) throws Exception;

	/**
	 * 
	 * @description 审核
	 * @author tianmin.wang
	 * @date 2019年11月22日 
	 * @param dto
	 * @param lineList
	 * @param request
	 * @return
	 * @throws Exception
	 */
	ResponseData auditInspection(IqcInspectionH dto, List<IqcInspectionL> lineList, HttpServletRequest request) throws Exception;
	
	List<IqcInspectionH> selectFornon(IRequest requestContext, IqcInspectionH dto);
	
	List<IqcInspectionH> selectForOther(IqcInspectionH dto,List<IqcInspectionL> lineList,int page, int pageSize);

	
	/**
	 * 
	 * @description 新增
	 * @author tianmin.wang
	 * @date 2019年11月22日 
	 * @param dto
	 * @param requestCtx
	 * @param request
	 * @return
	 */
	ResponseData addNewInspection(IqcInspectionH dto,IRequest requestCtx, HttpServletRequest request);

	/**
	 * 
	 * @description 从当前项开始 时间维度 向前查找N(转移得分表中的N值)项
	 * @author tianmin.wang
	 * @date 2019年12月18日 
	 * @param dto
	 * @return
	 */
	List<IqcInspectionH> getLimitCount(IqcInspectionH dto);
	
	List<IqcInspectionH> qmsQuery(IRequest requestContext, IqcInspectionH dto,int page,int pageSize);
	/**
	 * 检验单综合查询
	 * @param requestContext
	 * @param dto
	 * @param page
	 * @param pageSize
	 * @return
	 */
	List<IqcInspectionH> qmsQueryAll(IRequest requestContext, IqcInspectionH dto,int page,int pageSize);

	/**
	 * @description 通过IqcTask生成IQC检验单
	 * @author tianmin.wang
	 * @date 2020年1月7日 
	 * @param iqcinspectionh
	 * @param iqctask
	 * @param request
	 * @return
	 */
	void addNewInspection( IqcTask iqctask,IqcInspectionH iqcinspectionh,HttpServletRequest request);
	
	/**
	 * @description IOC抽样数反馈-WMS
	 * @author kai.li
	 * @date 2020年2月25日 
	 * @param rsd
	 * @return
	 */
	void samplingToWms(ResponseData rsd);

	/**
	 * @description
	 * @author tianmin.wang
	 * @date 2020年3月25日 
	 * @param dto
	 */
	void conclusionToWMS(IqcInspectionH dto);
}