package com.hand.hcs.hcs_tax_invoice.mapper;

import java.util.List;

import com.hand.hap.mybatis.common.Mapper;
import com.hand.hcs.hcs_tax_invoice.dto.TaxInvoice;

public interface TaxInvoiceMapper extends Mapper<TaxInvoice>{
	
	/**
	 * 应收发票创建/ 查询
	 * @param dto
	 * @return
	 */
	List<TaxInvoice> query(TaxInvoice dto);
	/**
	 * 查询今年最大流水号
	 * @param dto
	 * @return
	 */
	Integer queryMaxNum();
}