package com.hand.hqm.hqm_iqc_inspection_template_h.service;

import java.io.InputStream;
import java.text.ParseException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.hand.hap.core.IRequest;
import com.hand.hap.core.ProxySelf;
import com.hand.hap.system.dto.ResponseData;
import com.hand.hap.system.service.IBaseService;
import com.hand.hcm.hcm_object_events.dto.ObjectEvents;
import com.hand.hqm.hqm_iqc_inspection_template_h.dto.IqcInspectionTemplateH;
import com.hand.hqm.hqm_iqc_inspection_template_l.dto.IqcInspectionTemplateL;

public interface IIqcInspectionTemplateHService extends IBaseService<IqcInspectionTemplateH>, ProxySelf<IIqcInspectionTemplateHService>{

	/**
	 * 
	 * @description 复制功能的查询
	 * @author tianmin.wang
	 * @date 2019年11月22日 
	 * @param requestContext
	 * @param dto
	 * @param page
	 * @param pageSize
	 * @return
	 */
	List<IqcInspectionTemplateH> selectforCopy(IRequest requestContext, IqcInspectionTemplateH dto, int page, int pageSize);
	
	/**
	 * 基础查询
	 * @description
	 * @author tianmin.wang
	 * @date 2019年11月22日 
	 * @param requestContext
	 * @param dto
	 * @param page
	 * @param pageSize
	 * @return
	 */
	List<IqcInspectionTemplateH> myselect(IRequest requestContext, IqcInspectionTemplateH dto, int page, int pageSize);
	
	/**
	 * 
	 * @description 版本号的批量更细
	 * @author tianmin.wang
	 * @date 2019年11月22日 
	 * @param requestCtx
	 * @param dto
	 * @return
	 * @throws Exception
	 */
	List<IqcInspectionTemplateH> versionNumberBatchUpdate(IRequest requestCtx, List<IqcInspectionTemplateH> dto) throws Exception;
	

	/**
	 * 
	 * @description 状态字段单独更新
	 * @author tianmin.wang
	 * @date 2019年11月22日 
	 * @param requestCtx
	 * @param dto
	 */
	void updateStatus(IRequest requestCtx, List<IqcInspectionTemplateH> dto);
	
	/**
	 * 
	 * @description 审批时展示历史变更记录
	 * @author kai.li
	 * @date 2020年03月13日 
	 * @param requestCtx
	 * @param headId
	 * @param startTime
	 */
	public List<ObjectEvents> getHisContent(IRequest requestContext, Float headId, String startTime);
	
	/**
	 * 
	 * @description L表的状态更新
	 * @author tianmin.wang
	 * @date 2019年11月22日 
	 * @param requestCtx
	 * @param dto
	 */
	void updateStatus_l(IRequest requestCtx, IqcInspectionTemplateH dto);

	/**
	 * 保存
	 * @description
	 * @author tianmin.wang
	 * @date 2019年11月22日 
	 * @param requestContext
	 * @param dto
	 * @return
	 */
	List<IqcInspectionTemplateH> reSave(IRequest requestContext, IqcInspectionTemplateH dto);
	

	/**
	 * excel导入
	 * @description
	 * @author tianmin.wang
	 * @date 2019年11月22日 
	 * @param dto
	 * @param requestContext
	 * @param request
	 * @param response
	 * @throws Throwable
	 */
	void exportExcel(IqcInspectionTemplateH dto,IRequest requestContext, HttpServletRequest request, HttpServletResponse response) throws Throwable;

	/**
	 * @description 退回
	 * @author tianmin.wang
	 * @date 2020年1月3日 
	 * @param requestCtx
	 * @param dto
	 */
	void reback(IRequest requestCtx, List<IqcInspectionTemplateH> dto);

	/**
	 * @description 审核
	 * @author tianmin.wang
	 * @date 2020年1月3日 
	 * @param requestCtx
	 * @param dto
	 */
	void audit(IRequest requestCtx, List<IqcInspectionTemplateH> dto);

	/**
	 * @description 提交
	 * @author tianmin.wang
	 * @date 2020年1月3日 
	 * @param requestCtx
	 * @param dto
	 */
	void commit(IRequest requestCtx, List<IqcInspectionTemplateH> dto);

	/**
	 * @description
	 * @author tianmin.wang
	 * @date 2020年2月21日 
	 * @param dto
	 * @param request
	 * @return 
	 */
	List<IqcInspectionTemplateH> vtpQuery(IqcInspectionTemplateH dto, HttpServletRequest request) throws Exception;

	/**
	 * @description
	 * @author tianmin.wang
	 * @date 2020年2月21日 
	 * @param dto
	 * @param request
	 * @return
	 */
	void vtpCreateSave(List<IqcInspectionTemplateL> dto, HttpServletRequest request) throws Exception;

	/**
	 * @description IQC/FQC检验单模板导入
	 * @author kai.li
	 * @date 2020年2月27日 
	 * @param request
	 * @param requestContext
	 * @param inputStream
	 * @return
	 */
	ResponseData inputDataFromExcel(HttpServletRequest request, IRequest requestContext, InputStream inputStream, String mainType)
			throws Exception;

	/**
	 * @description
	 * @author tianmin.wang
	 * @date 2020年3月9日 
	 * @param request
	 * @param t
	 * @return
	 */
	IqcInspectionTemplateH updateByPrimaryKeySelectiveRecord(IRequest request, IqcInspectionTemplateH t);

	/**
	 * @description
	 * @author tianmin.wang
	 * @date 2020年3月9日 
	 * @param request
	 * @param t
	 * @return
	 */
	IqcInspectionTemplateH insertSelectiveRecord(IRequest request, IqcInspectionTemplateH t);
	
}