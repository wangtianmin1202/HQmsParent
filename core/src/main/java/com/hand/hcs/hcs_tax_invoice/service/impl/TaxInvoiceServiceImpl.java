package com.hand.hcs.hcs_tax_invoice.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.PageHelper;
import com.hand.hap.core.IRequest;
import com.hand.hap.system.service.impl.BaseServiceImpl;
import com.hand.hcs.hcs_doc_settlement.dto.DocSettlement;
import com.hand.hcs.hcs_doc_settlement.service.IDocSettlementService;
import com.hand.hcs.hcs_tax_invoice.dto.TaxInvoice;
import com.hand.hcs.hcs_tax_invoice.mapper.TaxInvoiceMapper;
import com.hand.hcs.hcs_tax_invoice.service.ITaxInvoiceService;
import com.hand.hqe.hqe_quality_deduction.dto.QualityDeduction;
import com.hand.hqe.hqe_quality_deduction.service.IQualityDeductionService;

@Service
@Transactional(rollbackFor = Exception.class)
public class TaxInvoiceServiceImpl extends BaseServiceImpl<TaxInvoice> implements ITaxInvoiceService{
	
	@Autowired
	private TaxInvoiceMapper mapper;
	@Autowired
	private IDocSettlementService docSettlementService;
	@Autowired
	private IQualityDeductionService qualityDeductionService;
	
	@Override
	public List<TaxInvoice> query(IRequest requestContext, TaxInvoice dto, int page, int pageSize) {
		PageHelper.startPage(page, pageSize);
		return mapper.query(dto);
	}

	@Override
	public String queryMaxNum(IRequest requestContext, TaxInvoice dto) {
		int maxNum = mapper.queryMaxNum();
		maxNum++;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
		String dateStr = sdf.format(new Date());
		
		String num = "1" + dateStr + String.format("%07d", maxNum);
		return num;
	}

	@Override
	public List<TaxInvoice> createInvoice(IRequest requestContext, TaxInvoice dto) {
		TaxInvoice taxInvoice = new TaxInvoice();
		taxInvoice.setWebInvoiceNum(dto.getWebInvoiceNum());
		taxInvoice = mapper.selectOne(taxInvoice);
		//判断标识  更新/插入
		boolean flag = false;
		if(taxInvoice == null) {
			taxInvoice = new TaxInvoice();
			taxInvoice.setWebInvoiceNum(dto.getWebInvoiceNum());
			taxInvoice.setWebInvoiceStatus("N");
		}else {
			flag = true;
		}
		taxInvoice.setPlantId(dto.getPlantId());
		taxInvoice.setCurrency(dto.getCurrency());
		taxInvoice.setSupplierId(dto.getSupplierId());
		taxInvoice.setSupplierSiteId(dto.getSupplierSiteId());
		taxInvoice.setNoTaxAmountS(dto.getNoTaxAmountS());
		taxInvoice.setInvoiceAmountS(dto.getInvoiceAmountS());
		taxInvoice.setInvoiceTaxAmountS(dto.getInvoiceTaxAmountS());
		
		if(dto.getNoTaxAmount() != null) {
			taxInvoice.setNoTaxAmount(dto.getNoTaxAmount());
		}
		if(dto.getInvoiceAmount() != null) {
			taxInvoice.setInvoiceAmount(dto.getInvoiceAmount());
		}
		if(dto.getInvoiceTaxAmount() != null) {
			taxInvoice.setInvoiceTaxAmount(dto.getInvoiceTaxAmount());
		}
		if(!StringUtils.isEmpty(dto.getSupplierRemarks())) {
			taxInvoice.setSupplierRemarks(dto.getSupplierRemarks());
		}
		if(dto.getTaxInvoiceQty() != null) {
			taxInvoice.setTaxInvoiceQty(dto.getTaxInvoiceQty());
		}
		if(!StringUtils.isEmpty(dto.getTaxInvoiceNum())) {
			taxInvoice.setTaxInvoiceNum(dto.getTaxInvoiceNum());
		}
		if(!StringUtils.isEmpty(dto.getApprovedRemarks())) {
			taxInvoice.setApprovedRemarks(dto.getApprovedRemarks());
		}
		
		if(flag) {
			//更新应收发票信息
			taxInvoice = self().updateByPrimaryKeySelective(requestContext, taxInvoice);
		}else {
			//插入应收发票信息
			taxInvoice = self().insertSelective(requestContext, taxInvoice);
		}
		
		
		for(float settelementId : dto.getDocSettlementIdList()) {
			DocSettlement docSettlement = new DocSettlement();
			docSettlement.setDocSettlementId(settelementId);
			docSettlement.setSettlementStatus("I");
			docSettlement.setWebInvoiceId(taxInvoice.getWebInvoiceId());
			//更新结算单据
			docSettlement = docSettlementService.updateByPrimaryKeySelective(requestContext, docSettlement);
			docSettlement = docSettlementService.selectByPrimaryKey(requestContext, docSettlement);
			if(docSettlement != null && "Q".equals(docSettlement.getDocType())) {
				//更新质量单据状态
				QualityDeduction qualityDeduction = new QualityDeduction();
				qualityDeduction.setQualityDeductionNum(docSettlement.getRelDocNumH());
				List<QualityDeduction> qualityDeductionList = qualityDeductionService.select(requestContext, qualityDeduction, 0, 0);
				if(qualityDeductionList != null && qualityDeductionList.size() > 0) {
					for(QualityDeduction deduction : qualityDeductionList) {
						if(!"I".equals(deduction.getStatus())) {
							deduction.setStatus("I");
							qualityDeductionService.updateByPrimaryKeySelective(requestContext, deduction);
						}
					}
				}
			}
			
		}
		List<TaxInvoice> taxInvoiceList = new ArrayList();
		taxInvoiceList.add(taxInvoice);
		return taxInvoiceList;
	}
	
	@Override
	public void confirm(IRequest requestContext, TaxInvoice dto) {
		TaxInvoice taxInvoice = new TaxInvoice();
		taxInvoice.setWebInvoiceId(dto.getWebInvoiceId());
		taxInvoice.setWebInvoiceStatus("S");
		taxInvoice.setSubmitedId((float)requestContext.getUserId());
		taxInvoice.setSubmitedDate(new Date());
		
		self().updateByPrimaryKeySelective(requestContext, taxInvoice);
	}

	@Override
	public void changeFlag(IRequest requestContext, TaxInvoice dto) {
		TaxInvoice taxInvoice = new TaxInvoice();
		taxInvoice.setWebInvoiceId(dto.getWebInvoiceId());
		taxInvoice.setWebInvoiceStatus("D");
		taxInvoice.setEnableFlag("N");
		taxInvoice = self().updateByPrimaryKeySelective(requestContext, taxInvoice);
		
		DocSettlement docSettlement = new DocSettlement();
		docSettlement.setWebInvoiceId(dto.getWebInvoiceId());
		List<DocSettlement> docSettlementList = docSettlementService.select(requestContext, docSettlement, 0, 0);
		for(DocSettlement settlement : docSettlementList) {
			settlement.setSettlementStatus("P");
			docSettlementService.updateWebInvoiceId(requestContext, settlement);
			
			settlement = docSettlementService.selectByPrimaryKey(requestContext, settlement);
			if(settlement != null && "Q".equals(settlement.getDocType())) {
				//更新质量单据状态
				QualityDeduction qualityDeduction = new QualityDeduction();
				qualityDeduction.setQualityDeductionNum(settlement.getRelDocNumH());
				List<QualityDeduction> qualityDeductionList = qualityDeductionService.select(requestContext, qualityDeduction, 0, 0);
				if(qualityDeductionList != null && qualityDeductionList.size() > 0) {
					for(QualityDeduction deduction : qualityDeductionList) {
						if(!"S".equals(deduction.getStatus())) {
							deduction.setStatus("S");
							qualityDeductionService.updateByPrimaryKeySelective(requestContext, deduction);
						}
					}
				}
			}
		}
	}

	@Override
	public void refused(IRequest requestContext, List<TaxInvoice> dto) {
		for(TaxInvoice taxInvoice : dto) {
			TaxInvoice invoice = new TaxInvoice();
			invoice.setWebInvoiceId(taxInvoice.getWebInvoiceId());
			invoice.setWebInvoiceStatus("R");
			
			invoice = self().updateByPrimaryKeySelective(requestContext, invoice);
		}
		
	}

}