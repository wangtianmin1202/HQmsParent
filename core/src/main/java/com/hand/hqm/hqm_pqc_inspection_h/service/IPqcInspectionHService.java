package com.hand.hqm.hqm_pqc_inspection_h.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.hand.hap.core.IRequest;
import com.hand.hap.core.ProxySelf;
import com.hand.hap.system.dto.ResponseData;
import com.hand.hap.system.service.IBaseService;
import com.hand.hap.util.SoapPostUtil;
import com.hand.hap.webservice.ws.idto.PqcProductionInfo;
import com.hand.hqm.hqm_pqc_inspection_h.dto.PqcInspectionH;
import com.hand.hqm.hqm_pqc_inspection_l.dto.PqcInspectionL;
import com.hand.hqm.hqm_qc_task.dto.PqcTask;

public interface IPqcInspectionHService extends IBaseService<PqcInspectionH>, ProxySelf<IPqcInspectionHService> {

	/**
	 * 基础查询
	 * 
	 * @description
	 * @author tianmin.wang
	 * @date 2019年11月22日
	 * @param requestContext
	 * @param dto
	 * @param page
	 * @param pageSize
	 * @return
	 */
	List<PqcInspectionH> myselect(IRequest requestContext, PqcInspectionH dto, int page, int pageSize);

	/**
	 * 新增
	 * 
	 * @description
	 * @author tianmin.wang
	 * @date 2019年11月22日
	 * @param dto
	 * @param requestContext
	 * @param request
	 * @return
	 * @throws Exception
	 */
	ResponseData addNewInspection(PqcInspectionH dto, IRequest requestContext, HttpServletRequest request)
			throws Exception;

	/**
	 * 保存
	 * 
	 * @description
	 * @author tianmin.wang
	 * @date 2019年11月22日
	 * @param dto
	 * @param requestContext
	 * @param request
	 * @return
	 * @throws Exception
	 */
	ResponseData saveChangeAll(PqcInspectionH dto, IRequest requestContext, HttpServletRequest request)
			throws Exception;

	/**
	 * 更新OK NG 数量
	 * 
	 * @description
	 * @author tianmin.wang
	 * @date 2019年11月22日
	 * @param headId
	 * @throws Exception
	 */
	void updateOkNgQty(Float headId) throws Exception;

	/**
	 * 更新OK NG 数量
	 * 
	 * @description
	 * @author tianmin.wang
	 * @date 2019年11月22日
	 * @param headId
	 * @throws Exception
	 */
	void updateOkNgQty(Float headId, String inspectionStatus) throws Exception;

	/**
	 * JOB入口
	 * 
	 * @description
	 * @author tianmin.wang
	 * @date 2019年11月22日
	 * @param dto
	 * @param lineList
	 * @return
	 * @throws Exception
	 */
	ResponseData addNewInspectionJob(PqcInspectionH dto, List<PqcInspectionL> lineList) throws Exception;

	/**
	 * 提交
	 * 
	 * @description
	 * @author tianmin.wang
	 * @date 2019年11月22日
	 * @param dto
	 * @param requestContext
	 * @param request
	 * @return
	 * @throws Exception
	 */
	ResponseData commitItem(PqcInspectionH dto, IRequest requestContext, HttpServletRequest request) throws Exception;

	/**
	 * 审核
	 * 
	 * @description
	 * @author tianmin.wang
	 * @date 2019年11月22日
	 * @param dto
	 * @param requestContext
	 * @param request
	 * @return
	 * @throws Exception
	 */
	ResponseData auditItem(PqcInspectionH dto, IRequest requestContext, HttpServletRequest request) throws Exception;

	/**
	 * 批量删除
	 * 
	 * @description
	 * @author tianmin.wang
	 * @date 2019年11月22日
	 * @param dto
	 * @return
	 */
	int reBatchDelete(List<PqcInspectionH> dto);

	/**
	 * 工序扫描
	 * 
	 * @description
	 * @author tianmin.wang
	 * @date 2019年11月22日
	 * @param requestContext
	 * @param request
	 * @param dto
	 * @throws Exception
	 */
	void operationScan(IRequest requestContext, HttpServletRequest request, PqcInspectionH dto) throws Exception;

	/**
	 * 综合查询
	 * 
	 * @description
	 * @author tianmin.wang
	 * @date 2019年11月22日
	 * @param requestContext
	 * @param dto
	 * @param page
	 * @param pageSize
	 * @return
	 */
	List<PqcInspectionH> qmsQuery(IRequest requestContext, PqcInspectionH dto, int page, int pageSize);

	/**
	 * 通过检验任务生成检验单
	 * 
	 * @description
	 * @author tianmin.wang
	 * @date 2020年1月8日
	 * @param request
	 * @param pqcTask
	 */
	void generateByTask(HttpServletRequest request, PqcTask pqcTask);

	/**
	 * @description
	 * @author tianmin.wang
	 * @date 2020年2月21日 
	 * @param request
	 * @param headerId
	 */
	void getProcess(HttpServletRequest request, Float headerId) throws Exception;
	
	/**
	 * @throws Exception 
	 * 
	 */
	SoapPostUtil.ResponseSap transferPqcProductionInfo(PqcProductionInfo ppi) throws Exception;

	/**
	 * 工序获取 mes接口的方式
	 * @param request
	 * @param line
	 * @param inspectionNum
	 * @throws Exception 
	 */
	void operationGet(HttpServletRequest request, String line, String inspectionNum) throws Exception;
}