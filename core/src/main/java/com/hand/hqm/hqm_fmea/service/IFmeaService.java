package com.hand.hqm.hqm_fmea.service;

import java.util.List;

import com.hand.hap.core.IRequest;
import com.hand.hap.core.ProxySelf;
import com.hand.hap.system.dto.ResponseData;
import com.hand.hap.system.service.IBaseService;
import com.hand.hqm.hqm_fmea.dto.Fmea;
import com.hand.hqm.hqm_pfmea_level.dto.PfmeaLevel;
import com.hand.hqm.hqm_sam_use_order_h.dto.SamUseOrderH;

public interface IFmeaService extends IBaseService<Fmea>, ProxySelf<IFmeaService>{
	 /**
     * 保存DFMEA 行信息
     * @param dto list 操作数据集
     * @param request 请求
     * @return 操作结果
     */
	 ResponseData dsave(IRequest requestContext, List<Fmea> dto);
	 /**
	     * 保存PFMEA 行信息
	     * @param dto list 操作数据集
	     * @param request 请求
	     * @return 操作结果
	     */
	 ResponseData psave(IRequest requestContext, List<Fmea> dto);
	 /**
	     * PFMEA页面查询
	     * @param dto 查询内容
	     * @param page 页码
	     * @param pageSize 页大小
	     * @param request 请求
	     * @return 结果集
	     */
	 List<Fmea>  dmyselect(IRequest requestContext,Fmea dto,int page, int pageSize); 
	 /**
	     * DFMEA页面查询
	     * @param dto 查询内容
	     * @param page 页码
	     * @param pageSize 页大小
	     * @param request 请求
	     * @return 结果集
	     */
	 List<Fmea>  pmyselect(IRequest requestContext,Fmea dto,int page, int pageSize); 
	 /**
	     * PFMEA页面查询
	     * @param dto 查询内容
	     * @param page 页码
	     * @param pageSize 页大小
	     * @param request 请求
	     * @return 结果集
	     */
	 List<Fmea>  PselectV(IRequest requestContext,Fmea dto,int page, int pageSize); 
	 /**
	     * PFMEA页面查询
	     * @param dto 查询内容
	     * @param page 页码
	     * @param pageSize 页大小
	     * @param request 请求
	     * @return 结果集
	     */	 
	 List<Fmea>  DselectV(IRequest requestContext,Fmea dto,int page, int pageSize);
	 /**
	     * 新增控制计划数据
	     * @param dto  操作数据集
	     * @param request 请求
	     * @return 操作结果
	     */
	 ResponseData addContralPlan(IRequest requestCtx, Fmea dto); 

}