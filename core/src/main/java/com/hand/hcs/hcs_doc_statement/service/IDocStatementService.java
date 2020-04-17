package com.hand.hcs.hcs_doc_statement.service;

import java.util.List;

import com.hand.hap.core.IRequest;
import com.hand.hap.core.ProxySelf;
import com.hand.hap.system.dto.ResponseData;
import com.hand.hap.system.service.IBaseService;
import com.hand.hcs.hcs_doc_statement.dto.DocStatement;

public interface IDocStatementService extends IBaseService<DocStatement>, ProxySelf<IDocStatementService>{
	/**
	 * 查询今年最大流水号
	 * @param 
	 * @return
	 */
	Integer queryMaxNum();
	/**
	 * 对账单确认 查询
	 * @param requestContext
	 * @param dto
	 * @param page
	 * @param pageSize
	 * @return
	 */
	List<DocStatement> query(IRequest requestContext, DocStatement dto, int page, int pageSize);
	/**
	 * 对账单确认
	 * @param requestContext
	 * @param dto
	 * @return
	 */
	ResponseData confirm(IRequest requestContext, DocStatement dto);
	/**
	 * 对账单取消
	 * @param requestContext
	 * @param dto
	 * @return
	 */
	ResponseData cancel(IRequest requestContext, DocStatement dto);
	/**
	 * 新建对账单查询
	 * @param requestContext 请求上下文
	 * @param dto 对账单信息
	 * @param page 页码
	 * @param pageSize 页容量
	 * @return 对账单信息
	 */
	List<DocStatement> queryAddInto(IRequest requestContext, DocStatement dto, int page, int pageSize);
}