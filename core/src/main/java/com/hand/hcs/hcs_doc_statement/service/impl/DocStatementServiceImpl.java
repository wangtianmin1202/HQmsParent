package com.hand.hcs.hcs_doc_statement.service.impl;

import com.github.pagehelper.PageHelper;
import com.hand.hap.core.IRequest;
import com.hand.hap.system.dto.ResponseData;
import com.hand.hap.system.service.impl.BaseServiceImpl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hand.hcs.hcs_doc_settlement.dto.DocSettlement;
import com.hand.hcs.hcs_doc_settlement.service.IDocSettlementService;
import com.hand.hcs.hcs_doc_statement.dto.DocStatement;
import com.hand.hcs.hcs_doc_statement.mapper.DocStatementMapper;
import com.hand.hcs.hcs_doc_statement.service.IDocStatementService;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(rollbackFor = Exception.class)
public class DocStatementServiceImpl extends BaseServiceImpl<DocStatement> implements IDocStatementService{
	@Autowired
	private DocStatementMapper docStatementMapper;
	@Autowired
	private IDocSettlementService docSettlementService;
	
	@Override
	public Integer queryMaxNum() {
		return docStatementMapper.queryMaxNum();
	}
	@Override
	public List<DocStatement> query(IRequest requestContext, DocStatement dto, int page, int pageSize) {
		PageHelper.startPage(page, pageSize);
		return docStatementMapper.query(dto);
	}
	@Override
	public ResponseData confirm(IRequest requestContext, DocStatement dto) {
		ResponseData responseData = new ResponseData();
		DocStatement docStatement = new DocStatement();
		docStatement.setDocStatementId(dto.getDocStatementId());
		docStatement.setDocStatementStatus("A");
		docStatement.setConfirmBy((float)requestContext.getUserId());
		docStatement.setConfirmTime(new Date());
		//更新对账单
		docStatement = self().updateByPrimaryKeySelective(requestContext, docStatement);
		
		DocSettlement docSettlement = new DocSettlement(); 
		docSettlement.setDocStatementId(docStatement.getDocStatementId());
		List<DocSettlement> docSettlementList = docSettlementService.select(requestContext, docSettlement, 0, 0);
		for(DocSettlement settlement : docSettlementList) {
			settlement.setSettlementStatus("P");
			//更新结算单据
			docSettlementService.updateByPrimaryKeySelective(requestContext, settlement);
		}
		responseData.setMessage("确认成功");
		return responseData;
	}
	@Override
	public ResponseData cancel(IRequest requestContext, DocStatement dto) {
		ResponseData responseData = new ResponseData();
		DocStatement docStatement = new DocStatement();
		docStatement.setDocStatementId(dto.getDocStatementId());
		docStatement.setDocStatementStatus("C");
		docStatement.setConfirmBy((float)requestContext.getUserId());
		docStatement.setConfirmTime(new Date());
		docStatement.setSumAmount(0F);
		//更新对账单
		docStatement = self().updateByPrimaryKeySelective(requestContext, docStatement);
		
		DocSettlement docSettlement = new DocSettlement(); 
		docSettlement.setDocStatementId(docStatement.getDocStatementId());
		List<DocSettlement> docSettlementList = docSettlementService.select(requestContext, docSettlement, 0, 0);
		for(DocSettlement settlement : docSettlementList) {
			settlement.setSettlementStatus("U");
			//更新结算单据
			docSettlementService.updateDocStatementId(requestContext, settlement);
		}
		responseData.setMessage("取消成功");
		return responseData;
	}
	@Override
	public List<DocStatement> queryAddInto(IRequest requestContext, DocStatement dto, int page, int pageSize) {
		PageHelper.startPage(page, pageSize);
		return docStatementMapper.queryAddInto(dto);
	}

}