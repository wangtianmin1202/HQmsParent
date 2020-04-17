package com.hand.hcs.hcs_doc_adjustment.service;

import java.util.List;

import com.hand.hap.core.IRequest;
import com.hand.hap.core.ProxySelf;
import com.hand.hap.system.service.IBaseService;
import com.hand.hcs.hcs_doc_adjustment.dto.DocAdjustment;

public interface IDocAdjustmentService extends IBaseService<DocAdjustment>, ProxySelf<IDocAdjustmentService>{
	
	/**
	 * 查询最大行号
	 * @param requestContext
	 * @param docAdjustment
	 * @return
	 */
	Integer queryMaxNumByInvoicId(IRequest requestContext, DocAdjustment docAdjustment);
	/**
	 * update
	 * @param requestContext
	 * @param dto
	 * @return
	 */
	List<DocAdjustment> update(IRequest requestContext, List<DocAdjustment> dto);
	/**
	 * 调整行明细查询
	 * @param requestContext
	 * @param dto
	 * @param page
	 * @param pageSize
	 * @return
	 */
	List<DocAdjustment> query(IRequest requestContext, DocAdjustment dto, int page, int pageSize);
} 