package com.hand.hcs.hcs_doc_settlement.service.impl;

import com.github.pagehelper.PageHelper;
import com.hand.hap.core.IRequest;
import com.hand.hap.core.impl.RequestHelper;
import com.hand.hap.system.dto.ResponseData;
import com.hand.hap.system.service.IPromptService;
import com.hand.hap.system.service.impl.BaseServiceImpl;
import com.hand.hap.util.SystemApiMethod;

import java.io.IOException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.hand.hcs.hcs_doc_settlement.dto.DocSettlement;
import com.hand.hcs.hcs_doc_settlement.mapper.DocSettlementMapper;
import com.hand.hcs.hcs_doc_settlement.service.IDocSettlementService;
import com.hand.hcs.hcs_doc_statement.dto.DocStatement;
import com.hand.hcs.hcs_doc_statement.service.IDocStatementService;

import org.springframework.transaction.annotation.Transactional;
import org.apache.poi.xssf.util.*;

@Service
@Transactional(rollbackFor = Exception.class)
public class DocSettlementServiceImpl extends BaseServiceImpl<DocSettlement> implements IDocSettlementService {
	@Autowired
	private DocSettlementMapper mapper;
	@Autowired
	private IDocStatementService docStatementService;
	@Autowired
	IPromptService iPromptService;

	@Override
	public List<DocSettlement> query(IRequest requestContext, DocSettlement dto, int page, int pageSize) {
		PageHelper.startPage(page, pageSize);
		return mapper.query(dto);
	}

	@Override
	public String createSettlement(IRequest requestContext, List<DocSettlement> dto) {
		float sumAmount = 0;
		// 可开票金额
		sumAmount = dto.stream().collect(Collectors.summingDouble(DocSettlement::getActualAmount)).floatValue();
		// 生成对账单号
		int num = docStatementService.queryMaxNum();
		num++;
		// 序列号
		String numStr = String.format("%07d", num);
		// 年月日：yyyy
		SimpleDateFormat simple = new SimpleDateFormat("yyyy");
		String now = simple.format(new Date());
		String docStatementNum = "2" + now + numStr;

		DocStatement docStatement = new DocStatement();
		docStatement.setSumAmount(sumAmount);
		docStatement.setSupplierId(dto.get(0).getSupplierId());
		docStatement.setSupplierSiteId(dto.get(0).getSupplierSiteId());
		docStatement.setPlantId(dto.get(0).getPlantId());
		docStatement.setCurrency(dto.get(0).getCurrency());
		docStatement.setDocStatementNum(docStatementNum);
		docStatement.setDocStatementStatus("N");
		// 创建对账单
		docStatement = docStatementService.insertSelective(requestContext, docStatement);

		for (DocSettlement docSettlement : dto) {
			DocSettlement settlement = new DocSettlement();
			settlement.setDocSettlementId(docSettlement.getDocSettlementId());
			settlement.setSettlementStatus("C");
			settlement.setDocStatementId(docStatement.getDocStatementId());
			// 更新结算单据
			self().updateByPrimaryKeySelective(requestContext, settlement);
		}
		return docStatementNum;
	}

	@Override
	public List<DocSettlement> queryDetail(IRequest requestContext, DocSettlement dto, int page, int pageSize) {
		PageHelper.startPage(page, pageSize);
		return mapper.queryDetail(dto);
	}

	@Override
	public List<DocSettlement> cancel(IRequest requestContext, List<DocSettlement> dto) {
		float statementId = dto.get(0).getDocStatementId();
		float sumAmount = 0;
		for (DocSettlement docSettlement : dto) {
			DocSettlement settlement = new DocSettlement();
			settlement.setDocSettlementId(docSettlement.getDocSettlementId());
			settlement.setSettlementStatus("U");
			sumAmount += docSettlement.getActualAmount();
			// 更新
			self().updateDocStatementId(requestContext, settlement);
		}
		DocSettlement settlement = new DocSettlement();
		settlement.setDocStatementId(statementId);
		List<DocSettlement> list = mapper.select(settlement);
		dto.get(0).setCancelFlag("N");
		if (list == null || list.size() == 0) {
			DocStatement docStatement = new DocStatement();
			docStatement.setDocStatementId(statementId);
			docStatement = docStatementService.selectByPrimaryKey(requestContext, docStatement);

			docStatement.setDocStatementStatus("C");
			docStatement.setSumAmount(docStatement.getSumAmount() - sumAmount);

			docStatement = docStatementService.updateByPrimaryKeySelective(requestContext, docStatement);
			dto.get(0).setCancelFlag("Y");
			dto.get(0).setSumAmount(docStatement.getSumAmount());
			;
		} else {
			DocStatement docStatement = new DocStatement();
			docStatement.setDocStatementId(statementId);
			docStatement = docStatementService.selectByPrimaryKey(requestContext, docStatement);

			docStatement.setSumAmount(docStatement.getSumAmount() - sumAmount);
			docStatement = docStatementService.updateByPrimaryKeySelective(requestContext, docStatement);
			dto.get(0).setSumAmount(docStatement.getSumAmount());
			;
		}
		return dto;
	}

	@Override
	public void updateDocStatementId(IRequest requestContext, DocSettlement dto) {
		mapper.updateDocStatementId(dto);
	}

	@Override
	public void updateWebInvoiceId(IRequest requestContext, DocSettlement dto) {
		mapper.updateWebInvoiceId(dto);
	}

	@Override
	public Integer queryMaxNum(DocSettlement dto) {
		// TODO Auto-generated method stub
		return mapper.queryMaxNum(dto);
	}

	@Override
	public List<DocSettlement> confirm(IRequest requestContext, List<DocSettlement> dto) {
		float docStatementId = dto.get(0).getDocStatementId();
		float sumAmount = 0;
		for (DocSettlement docSettlement : dto) {
			DocSettlement settlement = new DocSettlement();
			settlement.setDocSettlementId(docSettlement.getDocSettlementId());
			settlement = mapper.selectByPrimaryKey(settlement);
			if (settlement != null) {
				// 添加对账单
				settlement.setSettlementStatus("C");
				settlement.setDocStatementId(docStatementId);
				settlement = self().updateByPrimaryKeySelective(requestContext, settlement);

				sumAmount += settlement.getActualAmount();
			}
		}
		// 更新对账单可开票金额
		DocStatement statement = new DocStatement();
		statement.setDocStatementId(docStatementId);
		statement = docStatementService.selectByPrimaryKey(requestContext, statement);
		if (statement != null) {
			statement.setSumAmount(statement.getSumAmount() + sumAmount);
			docStatementService.updateByPrimaryKeySelective(requestContext, statement);
		}
		return dto;
	}

	@Override
	public DocSettlement printQuery(DocSettlement dto) {
		DocSettlement docSettlement = new DocSettlement();
		// 结算单据类型：D、R actual_amount < 0
		List<DocSettlement> printQueryDRNegative = mapper.printQueryDR(dto);
		// 结算单据类型：Q actual_amount < 0
		List<DocSettlement> printQueryQNegative = mapper.printQueryQ(dto);
		// 调整行:adjust_amount < 0
		List<DocSettlement> printQueryAdjustmentNegative = mapper.printQueryAdjustment(dto);
		dto.setCompareFlag("Y");
		// 结算单据类型：D、R actual_amount > 0
		List<DocSettlement> printQueryDRUnNegative = mapper.printQueryDR(dto);
		// 结算单据类型：Q actual_amount > 0
		List<DocSettlement> printQueryQUnNegative = mapper.printQueryQ(dto);
		// 调整行:adjust_amount > 0
		List<DocSettlement> printQueryAdjustmentUnNegative = mapper.printQueryAdjustment(dto);

		docSettlement.setPrintQueryAdjustmentNegative(printQueryAdjustmentNegative);
		docSettlement.setPrintQueryAdjustmentUnNegative(printQueryAdjustmentUnNegative);
		docSettlement.setPrintQueryDRNegative(printQueryDRNegative);
		docSettlement.setPrintQueryDRUnNegative(printQueryDRUnNegative);
		docSettlement.setPrintQueryQNegative(printQueryQNegative);
		docSettlement.setPrintQueryQUnNegative(printQueryQUnNegative);

		return docSettlement;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.hand.hcs.hcs_doc_settlement.service.IDocSettlementService#
	 * detailExcelExport(com.hand.hcs.hcs_doc_settlement.dto.DocSettlement,
	 * javax.servlet.http.HttpServletRequest,
	 * javax.servlet.http.HttpServletResponse)
	 */
	@Override
	public void detailExcelExport(DocSettlement dto, HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		DocSettlement docSettlement = self().query(RequestHelper.createServiceRequest(request), dto, 1, 1).get(0);
		DocSettlement dao = new DocSettlement();
		dao.setWebInvoiceId(dto.getWebInvoiceId());
		// 结算单据类型：D、R actual_amount < 0
		List<DocSettlement> printQueryDRNegative = mapper.printQueryDR(dao);
		// 结算单据类型：Q actual_amount < 0
		List<DocSettlement> printQueryQNegative = mapper.printQueryQ(dao);
		// 调整行:adjust_amount < 0
		List<DocSettlement> printQueryAdjustmentNegative = mapper.printQueryAdjustment(dao);
		dao.setCompareFlag("Y");
		// 结算单据类型：D、R actual_amount > 0
		List<DocSettlement> printQueryDRUnNegative = mapper.printQueryDR(dao);
		// 结算单据类型：Q actual_amount > 0
		List<DocSettlement> printQueryQUnNegative = mapper.printQueryQ(dao);
		// 调整行:adjust_amount > 0
		List<DocSettlement> printQueryAdjustmentUnNegative = mapper.printQueryAdjustment(dao);

		docSettlement.setPrintQueryAdjustmentNegative(printQueryAdjustmentNegative);
		docSettlement.setPrintQueryAdjustmentUnNegative(printQueryAdjustmentUnNegative);
		docSettlement.setPrintQueryDRNegative(printQueryDRNegative);
		docSettlement.setPrintQueryDRUnNegative(printQueryDRUnNegative);
		docSettlement.setPrintQueryQNegative(printQueryQNegative);
		docSettlement.setPrintQueryQUnNegative(printQueryQUnNegative);
		detailExcelGenerate(docSettlement, request, response);
	}

	public void detailExcelGenerate(DocSettlement docSettlement, HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		String fileName = docSettlement.getSupplierCode() + docSettlement.getSupplierName()
				+ sdf.format(docSettlement.getSubmitedDate()) + "明细";
		response.addHeader("Content-Disposition",
				"attachment;filename=\"" + URLEncoder.encode(fileName + ".xlsx", "UTF-8") + "\"");
		response.setContentType("application/vnd.ms-excel;charset=" + "UTF-8" + "");
		response.setHeader("Accept-Ranges", "bytes");
		ServletOutputStream outputStream = response.getOutputStream();
		XSSFWorkbook wb = new XSSFWorkbook();
		Sheet sheet = wb.createSheet("sheet1");
		sheet.setColumnWidth(0, 5000);
		sheet.setColumnWidth(1, 3000);
		sheet.setColumnWidth(2, 5000);
		sheet.setColumnWidth(5, 4000);
		sheet.setColumnWidth(7, 4000);
		createRows(request, docSettlement, sheet);
		wb.write(outputStream);
		outputStream.flush();
		outputStream.close();
		try {
			wb.close();
		} catch (Exception e) {
		}
	}

	/**
	 * @description 生成excel行
	 * @author tianmin.wang
	 * @date 2020年1月16日
	 * @param docSettlement
	 * @param sheet
	 */

	private void createRows(HttpServletRequest request, DocSettlement docSettlement, Sheet sheet) {
		IRequest ir = RequestHelper.createServiceRequest(request);
		Integer rowCount = 0;// 总行数
		// 第一行 主题
		Row rowTitle = sheet.createRow(0);
		rowTitle.createCell(4).setCellValue("发票明细清单");
		// sheet.addMergedRegion(new CellRangeAddress(0, 0, 4, 5));

		Row row1 = sheet.createRow(1);// 供应商编码行
		row1.createCell(0)
				.setCellValue(SystemApiMethod.getPromptDescription(ir, iPromptService, "settlement.suppliercode"));
		row1.createCell(1).setCellValue(docSettlement.getSupplierCode());

		Row row2 = sheet.createRow(2);// 供应商名称行
		row2.createCell(0)
				.setCellValue(SystemApiMethod.getPromptDescription(ir, iPromptService, "settlement.suppliername"));
		row2.createCell(1).setCellValue(docSettlement.getSupplierName());

		Row row3 = sheet.createRow(3);// 发票号 币种
		row3.createCell(0)
				.setCellValue(SystemApiMethod.getPromptDescription(ir, iPromptService, "settlement.webinvoicenum"));
		row3.createCell(1).setCellValue(docSettlement.getTaxInvoiceNum());
		row3.createCell(9)
				.setCellValue(SystemApiMethod.getPromptDescription(ir, iPromptService, "settlement.currency"));
		row3.createCell(10).setCellValue(docSettlement.getCurrency());
		sheet.createRow(4);// 空白间隔行

		titleCreat(ir, sheet, sheet.createRow(5));// 创建table的标题行
		rowCount = 5;
		rowCount = createBatch(ir, rowCount, docSettlement, sheet);
	}

	private int createBatch(IRequest ir, int rowCount, DocSettlement docSettlement, Sheet sheet) {// 数据组一
		if (docSettlement.getPrintQueryDRUnNegative() != null && docSettlement.getPrintQueryDRUnNegative().size() > 0) {
			rowCount = unNegativeDR(ir, rowCount, docSettlement, sheet);
		}
		if (docSettlement.getPrintQueryQUnNegative() != null && docSettlement.getPrintQueryQUnNegative().size() > 0) {
			rowCount = unNegativeQ(ir, rowCount, docSettlement, sheet);
		}
		if (docSettlement.getPrintQueryAdjustmentUnNegative() != null
				&& docSettlement.getPrintQueryAdjustmentUnNegative().size() > 0) {
			rowCount = unNegativeAdjuest(ir, rowCount, docSettlement, sheet);
		}
		rowCount = unNegativeLittleSum(ir, rowCount, docSettlement, sheet);
		if (docSettlement.getPrintQueryDRNegative() != null && docSettlement.getPrintQueryDRNegative().size() > 0) {
			rowCount = negativeDR(ir, rowCount, docSettlement, sheet);
		}
		if (docSettlement.getPrintQueryQNegative() != null && docSettlement.getPrintQueryQNegative().size() > 0) {
			rowCount = negativeQ(ir, rowCount, docSettlement, sheet);
		}
		if (docSettlement.getPrintQueryAdjustmentNegative() != null
				&& docSettlement.getPrintQueryAdjustmentNegative().size() > 0) {
			rowCount = negativeAdjuest(ir, rowCount, docSettlement, sheet);
		}
		rowCount = negativeLittleSum(ir, rowCount, docSettlement, sheet);

		rowCount = fullSum(ir, rowCount, docSettlement, sheet);
		return rowCount;
	}

	/**
	 * 总计行
	 * 
	 * @description
	 * @author tianmin.wang
	 * @date 2020年1月17日
	 * @param ir
	 * @param rowCount
	 * @param docSettlement
	 * @param sheet
	 * @return
	 */
	private int fullSum(IRequest ir, int rowCount, DocSettlement docSettlement, Sheet sheet) {
		rowCount++;
		Float unNegativeActualAmount = 0f;
		Float unNegativeTax = 0f;
		Float unNegativeTotalTax = 0f;
		if (docSettlement.getPrintQueryDRUnNegative() != null && docSettlement.getPrintQueryDRUnNegative().size() > 0) {
			unNegativeActualAmount = unNegativeActualAmount
					+ docSettlement.getPrintQueryDRUnNegative().stream().map(p -> {
						return p.getActualAmount() == null ? 0f : p.getActualAmount();
					}).reduce(Float::sum).get();
			unNegativeTax = unNegativeTax + docSettlement.getPrintQueryDRUnNegative().stream().map(p -> {
				return p.getTax() == null ? 0f : p.getTax();
			}).reduce(Float::sum).get();
			unNegativeTotalTax = unNegativeTotalTax + docSettlement.getPrintQueryDRUnNegative().stream().map(p -> {
				return p.getTotalTax() == null ? 0f : p.getTotalTax();
			}).reduce(Float::sum).get();
		}
		if (docSettlement.getPrintQueryQUnNegative() != null && docSettlement.getPrintQueryQUnNegative().size() > 0) {
			unNegativeActualAmount = unNegativeActualAmount
					+ docSettlement.getPrintQueryQUnNegative().stream().map(p -> {
						return p.getActualAmount() == null ? 0f : p.getActualAmount();
					}).reduce(Float::sum).get();
			unNegativeTax = unNegativeTax + docSettlement.getPrintQueryQUnNegative().stream().map(p -> {
				return p.getTax() == null ? 0f : p.getTax();
			}).reduce(Float::sum).get();
			unNegativeTotalTax = unNegativeTotalTax + docSettlement.getPrintQueryQUnNegative().stream().map(p -> {
				return p.getTotalTax() == null ? 0f : p.getTotalTax();
			}).reduce(Float::sum).get();
		}
		if (docSettlement.getPrintQueryAdjustmentUnNegative() != null
				&& docSettlement.getPrintQueryAdjustmentUnNegative().size() > 0) {
			unNegativeActualAmount = unNegativeActualAmount
					+ docSettlement.getPrintQueryAdjustmentUnNegative().stream().map(p -> {
						return p.getAdjustAmount() == null ? 0f : p.getAdjustAmount();
					}).reduce(Float::sum).get();// DocSettlement::getActualAmount
			unNegativeTax = unNegativeTax + docSettlement.getPrintQueryAdjustmentUnNegative().stream().map(p -> {
				return p.getTax() == null ? 0f : p.getTax();
			}).reduce(Float::sum).get();
			unNegativeTotalTax = unNegativeTotalTax
					+ docSettlement.getPrintQueryAdjustmentUnNegative().stream().map(p -> {
						return p.getTotalTax() == null ? 0f : p.getTotalTax();
					}).reduce(Float::sum).get();
		}
		Float negativeActualAmount = 0f;
		Float negativeTax = 0f;
		Float negativeTotalTax = 0f;
		if (docSettlement.getPrintQueryDRNegative() != null && docSettlement.getPrintQueryDRNegative().size() > 0) {
			negativeActualAmount = negativeActualAmount + docSettlement.getPrintQueryDRNegative().stream().map(p -> {
				return p.getActualAmount() == null ? 0f : p.getActualAmount();
			}).reduce(Float::sum).get();
			negativeTax = negativeTax + docSettlement.getPrintQueryDRNegative().stream().map(p -> {
				return p.getTax() == null ? 0f : p.getTax();
			}).reduce(Float::sum).get();
			negativeTotalTax = negativeTotalTax + docSettlement.getPrintQueryDRNegative().stream().map(p -> {
				return p.getTotalTax() == null ? 0f : p.getTotalTax();
			}).reduce(Float::sum).get();
		}
		if (docSettlement.getPrintQueryQNegative() != null && docSettlement.getPrintQueryQNegative().size() > 0) {
			negativeActualAmount = negativeActualAmount + docSettlement.getPrintQueryQNegative().stream().map(p -> {
				return p.getActualAmount() == null ? 0f : p.getActualAmount();
			}).reduce(Float::sum).get();
			negativeTax = negativeTax + docSettlement.getPrintQueryQNegative().stream().map(p -> {
				return p.getTax() == null ? 0f : p.getTax();
			}).reduce(Float::sum).get();
			negativeTotalTax = negativeTotalTax + docSettlement.getPrintQueryQNegative().stream().map(p -> {
				return p.getTotalTax() == null ? 0f : p.getTotalTax();
			}).reduce(Float::sum).get();
		}
		if (docSettlement.getPrintQueryAdjustmentNegative() != null
				&& docSettlement.getPrintQueryAdjustmentNegative().size() > 0) {
			negativeActualAmount = negativeActualAmount
					+ docSettlement.getPrintQueryAdjustmentNegative().stream().map(p -> {
						return p.getAdjustAmount() == null ? 0f : p.getAdjustAmount();
					}).reduce(Float::sum).get();// DocSettlement::getActualAmount
			negativeTax = negativeTax + docSettlement.getPrintQueryAdjustmentNegative().stream().map(p -> {
				return p.getTax() == null ? 0f : p.getTax();
			}).reduce(Float::sum).get();
			negativeTotalTax = negativeTotalTax + docSettlement.getPrintQueryAdjustmentNegative().stream().map(p -> {
				return p.getTotalTax() == null ? 0f : p.getTotalTax();
			}).reduce(Float::sum).get();
		}
		Row row = sheet.createRow(rowCount);
//		row.createCell(0).setCellValue(ds.getRelDocNumH());
//		row.createCell(1).setCellValue(ds.getRelDocNumL());
//		row.createCell(2).setCellValue(ds.getItemCode());
//		row.createCell(3).setCellValue(ds.getItemName());
//		row.createCell(4).setCellValue(ds.getDocQty());
		row.createCell(5)
				.setCellValue(SystemApiMethod.getPromptDescription(ir, iPromptService, "settlement.resultall"));//
//		row.createCell(6).setCellValue(ds.getPriceUnit());
		row.createCell(7).setCellValue(unNegativeActualAmount + negativeActualAmount);
//		row.createCell(8).setCellValue(ds.getTaxCode());
		row.createCell(9).setCellValue(unNegativeTax + negativeTax);
		row.createCell(10).setCellValue(unNegativeTotalTax + negativeTotalTax);
		return 0;
	}

	private int unNegativeLittleSum(IRequest ir, int rowCount, DocSettlement docSettlement, Sheet sheet) {// 结算单据类型：D、R
		// actual_amount > 0

		rowCount++;
		Float unNegativeActualAmount = 0f;
		Float unNegativeTax = 0f;
		Float unNegativeTotalTax = 0f;
		if (docSettlement.getPrintQueryDRUnNegative() != null && docSettlement.getPrintQueryDRUnNegative().size() > 0) {
			unNegativeActualAmount = unNegativeActualAmount
					+ docSettlement.getPrintQueryDRUnNegative().stream().map(p -> {
						return p.getActualAmount() == null ? 0f : p.getActualAmount();
					}).reduce(Float::sum).get();
			unNegativeTax = unNegativeTax + docSettlement.getPrintQueryDRUnNegative().stream().map(p -> {
				return p.getTax() == null ? 0f : p.getTax();
			}).reduce(Float::sum).get();
			unNegativeTotalTax = unNegativeTotalTax + docSettlement.getPrintQueryDRUnNegative().stream().map(p -> {
				return p.getTotalTax() == null ? 0f : p.getTotalTax();
			}).reduce(Float::sum).get();
		}
		if (docSettlement.getPrintQueryQUnNegative() != null && docSettlement.getPrintQueryQUnNegative().size() > 0) {
			unNegativeActualAmount = unNegativeActualAmount
					+ docSettlement.getPrintQueryQUnNegative().stream().map(p -> {
						return p.getActualAmount() == null ? 0f : p.getActualAmount();
					}).reduce(Float::sum).get();
			unNegativeTax = unNegativeTax + docSettlement.getPrintQueryQUnNegative().stream().map(p -> {
				return p.getTax() == null ? 0f : p.getTax();
			}).reduce(Float::sum).get();
			unNegativeTotalTax = unNegativeTotalTax + docSettlement.getPrintQueryQUnNegative().stream().map(p -> {
				return p.getTotalTax() == null ? 0f : p.getTotalTax();
			}).reduce(Float::sum).get();
		}
		if (docSettlement.getPrintQueryAdjustmentUnNegative() != null
				&& docSettlement.getPrintQueryAdjustmentUnNegative().size() > 0) {
			unNegativeActualAmount = unNegativeActualAmount
					+ docSettlement.getPrintQueryAdjustmentUnNegative().stream().map(p -> {
						return p.getActualAmount() == null ? 0f : p.getActualAmount();
					}).reduce(Float::sum).get();// DocSettlement::getActualAmount
			unNegativeTax = unNegativeTax + docSettlement.getPrintQueryAdjustmentUnNegative().stream().map(p -> {
				return p.getTax() == null ? 0f : p.getTax();
			}).reduce(Float::sum).get();
			unNegativeTotalTax = unNegativeTotalTax
					+ docSettlement.getPrintQueryAdjustmentUnNegative().stream().map(p -> {
						return p.getTotalTax() == null ? 0f : p.getTotalTax();
					}).reduce(Float::sum).get();
		}
		Row row = sheet.createRow(rowCount);
//		row.createCell(0).setCellValue(ds.getRelDocNumH());
//		row.createCell(1).setCellValue(ds.getRelDocNumL());
//		row.createCell(2).setCellValue(ds.getItemCode());
//		row.createCell(3).setCellValue(ds.getItemName());
//		row.createCell(4).setCellValue(ds.getDocQty());
		row.createCell(5).setCellValue(SystemApiMethod.getPromptDescription(ir, iPromptService, "settlement.result"));//
//		row.createCell(6).setCellValue(ds.getPriceUnit());
		row.createCell(7).setCellValue(unNegativeActualAmount);
//		row.createCell(8).setCellValue(ds.getTaxCode());
		row.createCell(9).setCellValue(unNegativeTax);
		row.createCell(10).setCellValue(unNegativeTotalTax);
		return rowCount;
	}

	private int negativeLittleSum(IRequest ir, int rowCount, DocSettlement docSettlement, Sheet sheet) {// 结算单据类型：D、R
		// actual_amount > 0

		rowCount++;
		Float negativeActualAmount = 0f;
		Float negativeTax = 0f;
		Float negativeTotalTax = 0f;
		if (docSettlement.getPrintQueryDRNegative() != null && docSettlement.getPrintQueryDRNegative().size() > 0) {
			negativeActualAmount = negativeActualAmount + docSettlement.getPrintQueryDRNegative().stream().map(p -> {
				return p.getActualAmount() == null ? 0f : p.getActualAmount();
			}).reduce(Float::sum).get();
			negativeTax = negativeTax + docSettlement.getPrintQueryDRNegative().stream().map(p -> {
				return p.getTax() == null ? 0f : p.getTax();
			}).reduce(Float::sum).get();
			negativeTotalTax = negativeTotalTax + docSettlement.getPrintQueryDRNegative().stream().map(p -> {
				return p.getTotalTax() == null ? 0f : p.getTotalTax();
			}).reduce(Float::sum).get();
		}
		if (docSettlement.getPrintQueryQNegative() != null && docSettlement.getPrintQueryQNegative().size() > 0) {
			negativeActualAmount = negativeActualAmount + docSettlement.getPrintQueryQNegative().stream().map(p -> {
				return p.getActualAmount() == null ? 0f : p.getActualAmount();
			}).reduce(Float::sum).get();
			negativeTax = negativeTax + docSettlement.getPrintQueryQNegative().stream().map(p -> {
				return p.getTax() == null ? 0f : p.getTax();
			}).reduce(Float::sum).get();
			negativeTotalTax = negativeTotalTax + docSettlement.getPrintQueryQNegative().stream().map(p -> {
				return p.getTotalTax() == null ? 0f : p.getTotalTax();
			}).reduce(Float::sum).get();
		}
		if (docSettlement.getPrintQueryAdjustmentNegative() != null
				&& docSettlement.getPrintQueryAdjustmentNegative().size() > 0) {
			negativeActualAmount = negativeActualAmount
					+ docSettlement.getPrintQueryAdjustmentNegative().stream().map(p -> {
						return p.getAdjustAmount() == null ? 0f : p.getAdjustAmount();
					}).reduce(Float::sum).get();// DocSettlement::getActualAmount
			negativeTax = negativeTax + docSettlement.getPrintQueryAdjustmentNegative().stream().map(p -> {
				return p.getTax() == null ? 0f : p.getTax();
			}).reduce(Float::sum).get();
			negativeTotalTax = negativeTotalTax + docSettlement.getPrintQueryAdjustmentNegative().stream().map(p -> {
				return p.getTotalTax() == null ? 0f : p.getTotalTax();
			}).reduce(Float::sum).get();
		}
		Row row = sheet.createRow(rowCount);
//		row.createCell(0).setCellValue(ds.getRelDocNumH());
//		row.createCell(1).setCellValue(ds.getRelDocNumL());
//		row.createCell(2).setCellValue(ds.getItemCode());
//		row.createCell(3).setCellValue(ds.getItemName());
//		row.createCell(4).setCellValue(ds.getDocQty());
		row.createCell(5).setCellValue(SystemApiMethod.getPromptDescription(ir, iPromptService, "settlement.result"));//
//		row.createCell(6).setCellValue(ds.getPriceUnit());
		row.createCell(7).setCellValue(negativeActualAmount);
//		row.createCell(8).setCellValue(ds.getTaxCode());
		row.createCell(9).setCellValue(negativeTax);
		row.createCell(10).setCellValue(negativeTotalTax);
		return rowCount;
	}

	private int unNegativeDR(IRequest ir, int rowCount, DocSettlement docSettlement, Sheet sheet) {// 结算单据类型：D、R
		// actual_amount > 0
		for (DocSettlement ds : docSettlement.getPrintQueryDRUnNegative()) {
			rowCount++;
			Row row = sheet.createRow(rowCount);
			row.createCell(0).setCellValue(ds.getRelPoNumH());
			row.createCell(1).setCellValue(ds.getRelPoNumL());
			row.createCell(2).setCellValue(ds.getItemCode());
			row.createCell(3).setCellValue(ds.getItemName());
			row.createCell(4).setCellValue(ds.getDocQty());
			row.createCell(5).setCellValue(ds.getUnitPrice());
			row.createCell(6).setCellValue(ds.getPriceUnit());
			row.createCell(7).setCellValue(ds.getActualAmount());
			row.createCell(8).setCellValue(ds.getTaxCode());
			row.createCell(9).setCellValue(ds.getTax());
			row.createCell(10).setCellValue(ds.getTotalTax());
		}
		return rowCount;
	}

	private int negativeDR(IRequest ir, int rowCount, DocSettlement docSettlement, Sheet sheet) {//
		for (DocSettlement ds : docSettlement.getPrintQueryDRNegative()) {
			rowCount++;
			Row row = sheet.createRow(rowCount);
			row.createCell(0).setCellValue(ds.getRelPoNumH());
			row.createCell(1).setCellValue(ds.getRelPoNumL());
			row.createCell(2).setCellValue(ds.getItemCode());
			row.createCell(3).setCellValue(ds.getItemName());
			row.createCell(4).setCellValue(ds.getDocQty());
			row.createCell(5).setCellValue(ds.getUnitPrice());
			row.createCell(6).setCellValue(ds.getPriceUnit());
			row.createCell(7).setCellValue(ds.getActualAmount());
			row.createCell(8).setCellValue(ds.getTaxCode());
			row.createCell(9).setCellValue(ds.getTax());
			row.createCell(10).setCellValue(ds.getTotalTax());
		}
		return rowCount;
	}

	private int unNegativeQ(IRequest ir, int rowCount, DocSettlement docSettlement, Sheet sheet) {//
		for (DocSettlement ds : docSettlement.getPrintQueryQUnNegative()) {
			rowCount++;
			Row row = sheet.createRow(rowCount);
			row.createCell(0).setCellValue(ds.getRelDocNumH());
			// row.createCell(1).setCellValue(ds.getRelDocNumL());
			row.createCell(2)
					.setCellValue(SystemApiMethod.getPromptDescription(ir, iPromptService, "error.srm_purchase_1135"));
			// row.createCell(3).setCellValue(ds.getItemName());
			// row.createCell(4).setCellValue(ds.getDocQty());
			// row.createCell(5).setCellValue(ds.getUnitPrice());
			// row.createCell(6).setCellValue(ds.getPriceUnit());
			row.createCell(7).setCellValue(ds.getActualAmount());
			row.createCell(8).setCellValue(ds.getTaxCode());
			row.createCell(9).setCellValue(ds.getTax());
			row.createCell(10).setCellValue(ds.getTotalTax());
		}
		return rowCount;
	}

	private int negativeQ(IRequest ir, int rowCount, DocSettlement docSettlement, Sheet sheet) {//
		for (DocSettlement ds : docSettlement.getPrintQueryQNegative()) {
			rowCount++;
			Row row = sheet.createRow(rowCount);
			row.createCell(0).setCellValue(ds.getRelDocNumH());
			// row.createCell(1).setCellValue(ds.getRelDocNumL());
			row.createCell(2)
					.setCellValue(SystemApiMethod.getPromptDescription(ir, iPromptService, "error.srm_purchase_1135"));
			// row.createCell(3).setCellValue(ds.getItemName());
			// row.createCell(4).setCellValue(ds.getDocQty());
			// row.createCell(5).setCellValue(ds.getUnitPrice());
			// row.createCell(6).setCellValue(ds.getPriceUnit());
			row.createCell(7).setCellValue(ds.getActualAmount() == null ? 0 : ds.getActualAmount().intValue());
			row.createCell(8).setCellValue(ds.getTaxCode());
			row.createCell(9).setCellValue(ds.getTax() == null ? 0 : ds.getTax().intValue());
			row.createCell(10).setCellValue(ds.getTotalTax() == null ? 0 : ds.getTotalTax().intValue());
		}
		return rowCount;
	}

	private int unNegativeAdjuest(IRequest ir, int rowCount, DocSettlement docSettlement, Sheet sheet) {//
		for (DocSettlement ds : docSettlement.getPrintQueryAdjustmentUnNegative()) {
			rowCount++;
			Row row = sheet.createRow(rowCount);
			row.createCell(0)
					.setCellValue(SystemApiMethod.getPromptDescription(ir, iPromptService, "settlement.adjustment"));
			row.createCell(1).setCellValue(ds.getDocAdjustmentNum());
			row.createCell(2).setCellValue(ds.getRemarks());
//			row.createCell(3).setCellValue(ds.getItemName());
//			row.createCell(4).setCellValue(ds.getDocQty());
//			row.createCell(5).setCellValue(ds.getUnitPrice());
//			row.createCell(6).setCellValue(ds.getPriceUnit());
			row.createCell(7).setCellValue(ds.getAdjustAmount());
			row.createCell(8).setCellValue(ds.getTaxCode());
			row.createCell(9).setCellValue(ds.getTax() == null ? 0 : ds.getTax().intValue());
			row.createCell(10).setCellValue(ds.getTotalTax() == null ? 0 : ds.getTotalTax().intValue());
		}
		return rowCount;
	}

	private int negativeAdjuest(IRequest ir, int rowCount, DocSettlement docSettlement, Sheet sheet) {//
		for (DocSettlement ds : docSettlement.getPrintQueryAdjustmentNegative()) {
			rowCount++;
			Row row = sheet.createRow(rowCount);
			row.createCell(0)
					.setCellValue(SystemApiMethod.getPromptDescription(ir, iPromptService, "settlement.adjustment"));
			row.createCell(1).setCellValue(ds.getDocAdjustmentNum());
			row.createCell(2).setCellValue(ds.getRemarks());
//			row.createCell(3).setCellValue(ds.getItemName());
//			row.createCell(4).setCellValue(ds.getDocQty());
//			row.createCell(5).setCellValue(ds.getUnitPrice());
//			row.createCell(6).setCellValue(ds.getPriceUnit());
			row.createCell(7).setCellValue(ds.getAdjustAmount());
			row.createCell(8).setCellValue(ds.getTaxCode());
			row.createCell(9).setCellValue(ds.getTax());
			row.createCell(10).setCellValue(ds.getTotalTax());
		}
		return rowCount;
	}

	public void titleCreat(IRequest ir, Sheet sheet, Row firstRow) {
		firstRow.createCell(0)
				.setCellValue(SystemApiMethod.getPromptDescription(ir, iPromptService, "settlement.number"));
		firstRow.createCell(1)
				.setCellValue(SystemApiMethod.getPromptDescription(ir, iPromptService, "settlement.linenum"));
		firstRow.createCell(2)
				.setCellValue(SystemApiMethod.getPromptDescription(ir, iPromptService, "settlement.itemcode"));
		firstRow.createCell(3)
				.setCellValue(SystemApiMethod.getPromptDescription(ir, iPromptService, "settlement.itemname"));
		firstRow.createCell(4)
				.setCellValue(SystemApiMethod.getPromptDescription(ir, iPromptService, "settlement.docqty"));
		firstRow.createCell(5)
				.setCellValue(SystemApiMethod.getPromptDescription(ir, iPromptService, "settlement.unitprice"));
		firstRow.createCell(6)
				.setCellValue(SystemApiMethod.getPromptDescription(ir, iPromptService, "settlement.priceunit"));
		firstRow.createCell(7)
				.setCellValue(SystemApiMethod.getPromptDescription(ir, iPromptService, "settlement.actualamount"));
		firstRow.createCell(8)
				.setCellValue(SystemApiMethod.getPromptDescription(ir, iPromptService, "settlement.taxcode"));
		firstRow.createCell(9).setCellValue(SystemApiMethod.getPromptDescription(ir, iPromptService, "settlement.tax"));
		firstRow.createCell(10)
				.setCellValue(SystemApiMethod.getPromptDescription(ir, iPromptService, "settlement.totaltax"));
	}
}