package com.hand.hcs.hcs_doc_adjustment.service.impl;

import com.github.pagehelper.PageHelper;
import com.hand.hap.core.IRequest;
import com.hand.hap.system.service.impl.BaseServiceImpl;

import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.hand.hcs.hcs_doc_adjustment.dto.DocAdjustment;
import com.hand.hcs.hcs_doc_adjustment.mapper.DocAdjustmentMapper;
import com.hand.hcs.hcs_doc_adjustment.service.IDocAdjustmentService;
import com.hand.hcs.hcs_doc_settlement.dto.DocSettlement;
import com.hand.hcs.hcs_doc_settlement.service.IDocSettlementService;
import com.hand.hcs.hcs_tax_invoice.dto.TaxInvoice;
import com.hand.hcs.hcs_tax_invoice.service.ITaxInvoiceService;

import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(rollbackFor = Exception.class)
public class DocAdjustmentServiceImpl extends BaseServiceImpl<DocAdjustment> implements IDocAdjustmentService{
	@Autowired
	private DocAdjustmentMapper mapper;
	@Autowired
	private ITaxInvoiceService taxInvoiceService;
	
	@Override
	public Integer queryMaxNumByInvoicId(IRequest requestContext, DocAdjustment docAdjustment) {
		// TODO Auto-generated method stub
		return mapper.queryMaxNumByInvoicId(docAdjustment);
	}

	@Override
	public List<DocAdjustment> update(IRequest requestContext, List<DocAdjustment> dto) {

		//dto.stream().filter(data -> StringUtils.isEmpty(data.getDocAdjustmentType())).findAny().orElseThrow(RuntimeException :: new);
		for(DocAdjustment adjustment : dto) {
			if(StringUtils.isEmpty(adjustment.getDocAdjustmentType())) {
				throw new RuntimeException("调整行类型为必输项");
			}
		}
		float noTaxAmountSCount = dto.stream().collect(Collectors.summingDouble(DocAdjustment :: getAdjustAmount)).floatValue();
		float invoiceTaxAmountS = 0;
		for(DocAdjustment adjustment : dto) {
			float adjustAmount = adjustment.getAdjustAmount() == null ? 1 : adjustment.getAdjustAmount();
			float tax = adjustment.getTaxCode().split("%")[0] == null ? 1 : Float.parseFloat(adjustment.getTaxCode().split("%")[0]);
			invoiceTaxAmountS += adjustAmount * tax * 0.01;
		}
		
		TaxInvoice taxInvoice = new TaxInvoice();
		taxInvoice.setWebInvoiceId(dto.get(0).getWebInvoiceId());
		taxInvoice = taxInvoiceService.selectByPrimaryKey(requestContext, taxInvoice);
		//不含税总额（系统）
		taxInvoice.setNoTaxAmountS((taxInvoice.getNoTaxAmountS() == null ? 0 : taxInvoice.getNoTaxAmountS()) + noTaxAmountSCount);
		//税额(系统)
		taxInvoice.setInvoiceTaxAmountS((taxInvoice.getInvoiceTaxAmountS() == null ? 0 : taxInvoice.getInvoiceTaxAmountS()) + invoiceTaxAmountS);
		//含税总额（系统）
		taxInvoice.setInvoiceAmountS((taxInvoice.getNoTaxAmountS() == null ? 0 : taxInvoice.getNoTaxAmountS()) + (taxInvoice.getInvoiceTaxAmountS() == null ? 0 : taxInvoice.getInvoiceTaxAmountS()));
		//更新网上发票信息
		taxInvoiceService.updateByPrimaryKeySelective(requestContext, taxInvoice);
		
		return self().batchUpdate(requestContext, dto);
	}

	@Override
	public List<DocAdjustment> query(IRequest requestContext, DocAdjustment dto, int page, int pageSize) {
		PageHelper.startPage(page, pageSize);
		return mapper.query(dto);
	}

}