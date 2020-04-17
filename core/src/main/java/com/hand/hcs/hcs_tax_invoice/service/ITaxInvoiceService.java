package com.hand.hcs.hcs_tax_invoice.service;

import java.util.List;

import com.hand.hap.core.IRequest;
import com.hand.hap.core.ProxySelf;
import com.hand.hap.system.service.IBaseService;
import com.hand.hcs.hcs_tax_invoice.dto.TaxInvoice;

public interface ITaxInvoiceService extends IBaseService<TaxInvoice>, ProxySelf<ITaxInvoiceService>{
	
	/**
	 * 应收发票创建 查询
	 * @param requestContext
	 * @param dto
	 * @param page
	 * @param pageSize
	 * @return
	 */
	List<TaxInvoice> query(IRequest requestContext,TaxInvoice dto,int page, int pageSize);
	/**
	 * 查询今年最大流水号
	 * @param requestContext
	 * @param dto
	 * @return
	 */
	String queryMaxNum(IRequest requestContext,TaxInvoice dto);
	/**
	 * 创建应收发票
	 * @param requestContext
	 * @param dto
	 * @return
	 */
	List<TaxInvoice> createInvoice(IRequest requestContext, TaxInvoice dto);
	/**
	 * 应收发票编辑--提交
	 * @param requestContext
	 * @param dto
	 */
	void confirm(IRequest requestContext, TaxInvoice dto);
	/**
	 * 删除
	 * @param requestContext
	 * @param dto
	 */
	void changeFlag(IRequest requestContext, TaxInvoice dto);
	/**
	 * 拒绝
	 * @param requestContext
	 * @param dto
	 */
	void refused(IRequest requestContext, List<TaxInvoice> dto);
}