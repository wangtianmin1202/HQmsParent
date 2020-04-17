package com.hand.hqe.hqe_quality_deduction.service.impl;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.hand.hap.core.IRequest;
import com.hand.hap.system.dto.ResponseData;
import com.hand.hap.system.service.impl.BaseServiceImpl;
import com.hand.hap.util.SystemApiMethod;
import com.hand.hcs.hcs_doc_settlement.dto.DocSettlement;
import com.hand.hcs.hcs_doc_settlement.service.IDocSettlementService;
import com.hand.hqe.hqe_quality_deduction.dto.QualityDeduction;
import com.hand.hqe.hqe_quality_deduction.mapper.QualityDeductionMapper;
import com.hand.hqe.hqe_quality_deduction.service.IQualityDeductionService;
import com.hand.hqm.hqm_qc_files.dto.QcFiles;

import net.logstash.logback.encoder.org.apache.commons.lang.StringUtils;

@Service
@Transactional(rollbackFor = Exception.class)
public class QualityDeductionServiceImpl extends BaseServiceImpl<QualityDeduction> implements IQualityDeductionService{
	
	@Autowired
	private QualityDeductionMapper mapper;
	
	@Autowired
	private IDocSettlementService docSettlementService;
	
	
	@Override
	public List<QualityDeduction> query(IRequest requestContext, QualityDeduction qualityDeduction, int page,
			int pageSize) {
		// TODO Auto-generated method stub
		return mapper.query(qualityDeduction);
	}
	@Override
	public Integer queryMaxNum(QualityDeduction qualityDeduction) {
		// TODO Auto-generated method stub
		return mapper.queryMaxNum(qualityDeduction);
	}
	@Override
	public List<QualityDeduction> createSettlement(IRequest requestContext, List<QualityDeduction> dto) {
		for(QualityDeduction qualityDeduction : dto) {
			//更新质量扣款单据状态
			QualityDeduction deduction = new QualityDeduction();
			deduction.setStatus("S");
			deduction.setQualityDeductionId(qualityDeduction.getQualityDeductionId());
			self().updateByPrimaryKeySelective(requestContext, deduction);
			
			
			DocSettlement docSettlement = new DocSettlement();
			docSettlement.setSupplierId(qualityDeduction.getSupplierId());
			int num = docSettlementService.queryMaxNum(docSettlement);
			num++;
			// 序列号
			String numStr = String.format("%04d", num);
			// 年月日：yyyy
			SimpleDateFormat simple = new SimpleDateFormat("yyMM");
			String now = simple.format(new Date());
			String docSettlementNum = qualityDeduction.getSupplierCode() + now + numStr;
			
			//生成结算单据
			docSettlement.setDocSettlementNum(docSettlementNum);
			docSettlement.setDocType("Q");
			docSettlement.setAccountDate(qualityDeduction.getAccountDate());
			docSettlement.setSettlementStatus("U");
			docSettlement.setRelDocNumH(qualityDeduction.getQualityDeductionNum());
			docSettlement.setSupplierId(qualityDeduction.getSupplierId());
			docSettlement.setActualAmount(qualityDeduction.getActualAmount());
			docSettlement.setTaxCode(qualityDeduction.getTaxCode());
			docSettlement.setCurrency(qualityDeduction.getCurrency());
			docSettlement.setPlantId(qualityDeduction.getPlantId());
			docSettlementService.insertSelective(requestContext, docSettlement);
		}
		return dto;
	}
	@Override
	public List<QualityDeduction> cancel(IRequest requestContext, List<QualityDeduction> dto) {
		for(QualityDeduction qualityDeduction : dto) {
			//更新质量扣款单据状态
			QualityDeduction deduction = new QualityDeduction();
			deduction.setStatus("C");
			deduction.setQualityDeductionId(qualityDeduction.getQualityDeductionId());
			self().updateByPrimaryKeySelective(requestContext, deduction);
		}
		return dto;
	}
	@Override
	public ResponseData fileUpload(IRequest requestCtx, HttpServletRequest request)
			throws IllegalStateException, IOException {
		String qualityDeductionNum = request.getParameter("qualityDeductionNum");
		String qualityDeductionId = String.valueOf(Float.valueOf(request.getParameter("qualityDeductionId")).intValue());
		ResponseData responseData = new ResponseData();
		List<String> rows = new ArrayList<String>();
    	
    	MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;

		// 获取文件map集合
		Map<String, MultipartFile> fileMap = multipartRequest.getFileMap();
		String rootPath = "/apps/hap/resource";
		String endPath = "/demension/QualityDeductionFiles/file_"+qualityDeductionNum+"/";
		String path = rootPath + endPath;
		if(SystemApiMethod.getOsType().equals("window")) {
			rootPath = "C:/apps/hap/resource";
			endPath = "/demension/QualityDeductionFiles/file_"+qualityDeductionNum+"/";
			path = rootPath + endPath;
		}
		File dir = new File(path);
		if(!dir.exists()) {
			dir.mkdirs();
		}
		for(Map.Entry<String, MultipartFile> entry : fileMap.entrySet()) {
			rows.add(entry.getValue().getOriginalFilename());
			MultipartFile forModel = entry.getValue();
			File file = new File(path,forModel.getOriginalFilename());
			//是否已经存在文件
			QualityDeduction qualityDeduction = new QualityDeduction();
			qualityDeduction.setQualityDeductionId(Float.valueOf(qualityDeductionId));
			qualityDeduction = self().selectByPrimaryKey(requestCtx, qualityDeduction);
			if(qualityDeduction != null && !StringUtils.isNotBlank(qualityDeduction.getAttachmentAdd())) {
				//执行新增
				forModel.transferTo(file);
				qualityDeduction.setAttachmentAdd(endPath + entry.getValue().getOriginalFilename());
				self().updateByPrimaryKeySelective(requestCtx, qualityDeduction);
			}else{
				continue;
			}
			responseData.setMessage(entry.getValue().getOriginalFilename());
			break;
		}
		return responseData;
	}
	@Override
	public int updaeDataAndDelFile(IRequest requestContext, List<QualityDeduction> dto) {
		String rootPath = "/apps/hap/resource";
		if (SystemApiMethod.getOsType().equals("window")) {
			rootPath = "C:/apps/hap/resource";
		}
		int c = 0;
		for (QualityDeduction t : dto) {
			t = self().selectByPrimaryKey(requestContext, t);
			if(t != null) {	
				//删除文件
				File file = new File(rootPath + t.getAttachmentAdd());
				if (file.exists()) {
					file.delete();
				}
				//清空数据
				t.setAttachmentAdd("");
				self().updateByPrimaryKeySelective(requestContext, t);
				c++;
			}
		}
		return c;
	}

}