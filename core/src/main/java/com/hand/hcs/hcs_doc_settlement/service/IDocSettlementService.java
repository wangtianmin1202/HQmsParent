package com.hand.hcs.hcs_doc_settlement.service;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.hand.hap.core.IRequest;
import com.hand.hap.core.ProxySelf;
import com.hand.hap.system.dto.ResponseData;
import com.hand.hap.system.service.IBaseService;
import com.hand.hcs.hcs_doc_settlement.dto.DocSettlement;

public interface IDocSettlementService extends IBaseService<DocSettlement>, ProxySelf<IDocSettlementService>{
	/**
	 * 对账单创建  查询
	 * @param requestContext
	 * @param dto
	 * @param page
	 * @param pageSize
	 * @return
	 */
	List<DocSettlement> query(IRequest requestContext,DocSettlement dto, int page, int pageSize);
	/**
	 * 对账单创建
	 * @param requestContext
	 * @param dto
	 * @return
	 */
	String createSettlement(IRequest requestContext,List<DocSettlement> dto);
	/**
	 * 对账单明细查询
	 * @param requestContext
	 * @param dto
	 * @param page
	 * @param pageSize
	 * @return
	 */
	List<DocSettlement> queryDetail(IRequest requestContext,DocSettlement dto, int page, int pageSize);
	
	/**
	 * 取消对账单行
	 * @param requestContext
	 * @param dto
	 * @return
	 */
	List<DocSettlement> cancel(IRequest requestContext,List<DocSettlement> dto);
	/**
	 * 清空对账单id 更改状态
	 * @param requestContext
	 * @param dto
	 */
	void updateDocStatementId(IRequest requestContext,DocSettlement dto);
	/**
	 * 清空网上发票id 更改状态
	 * @param requestContext
	 * @param dto
	 */
	void updateWebInvoiceId(IRequest requestContext,DocSettlement dto);
	/**
	 * 获取最大流水号
	 * @return
	 */
	Integer queryMaxNum(DocSettlement dto);
	/**
	 * 添加至已有对账单  确认
	 * @param requestContext 请求上下文
	 * @param dto 对账单信息
	 * @return 对账单集合
	 */
	List<DocSettlement> confirm(IRequest requestContext,List<DocSettlement> dto);
	/**
	 * 发票明细预览 数据获取
	 * @param dto 对账单信息
	 * @return 数据源
	 */
	DocSettlement printQuery(DocSettlement dto);
	/**
	 * @description
	 * @author tianmin.wang
	 * @date 2020年1月16日 
	 * @param dto
	 * @param response 
	 * @param request 
	 * @return
	 * @throws IOException 
	 */
	void detailExcelExport(DocSettlement dto, HttpServletRequest request, HttpServletResponse response) throws IOException;
}