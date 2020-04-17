package com.hand.hcs.hcs_barcode_relation.service.impl;

import com.hand.hap.core.IRequest;
import com.hand.hap.system.dto.ResponseData;
import com.hand.hap.system.service.IPromptService;
import com.hand.hap.system.service.impl.BaseServiceImpl;
import com.hand.hap.util.SystemApiMethod;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hand.hcs.hcs_barcode.dto.SmallBarcode;
import com.hand.hcs.hcs_barcode.service.ISmallBarcodeService;
import com.hand.hcs.hcs_barcode_relation.dto.BarcodeRelation;
import com.hand.hcs.hcs_barcode_relation.mapper.BarcodeRelationMapper;
import com.hand.hcs.hcs_barcode_relation.service.IBarcodeRelationService;
import com.hand.hcs.hcs_out_barcode.dto.OutBarcode;
import com.hand.hcs.hcs_out_barcode.service.IOutBarcodeService;

import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(rollbackFor = Exception.class)
public class BarcodeRelationServiceImpl extends BaseServiceImpl<BarcodeRelation> implements IBarcodeRelationService{
	
	@Autowired
	private BarcodeRelationMapper mapper; 
	@Autowired
	private ISmallBarcodeService smallBarcodeService;
	@Autowired
	private IPromptService iPromptService;
	@Autowired
	private IOutBarcodeService outBarcodeService;
	
	@Override
	public ResponseData confirmBind(IRequest requestContext,BarcodeRelation dto) {
		ResponseData responseData = new ResponseData();
		
		BarcodeRelation barcodeRel = new BarcodeRelation();
		barcodeRel.setOutbarcodeId(dto.getOutbarcodeId());
		barcodeRel.setEnableFlag("Y");
		List<BarcodeRelation> list = mapper.select(barcodeRel);
		
//		for(BarcodeRelation relation : list) {			
//			if("WL".equals(relation.getInbarcodeType())) {
//				//校验
//				responseData = checkFunction(requestContext, relation, dto, "error.srm_purchase_1025");
//				if(!responseData.isSuccess()) {
//					return responseData;
//				}
//			}else {
//				BarcodeRelation barcodeRelation = new BarcodeRelation();
//				barcodeRelation.setOutbarcodeId(relation.getInbarcodeId());
//				barcodeRelation.setEnableFlag("Y");
//				List<BarcodeRelation> relationList = mapper.select(barcodeRelation);
//				for(BarcodeRelation codeRelation : relationList) {
//					//校验
//					responseData = checkFunction(requestContext, codeRelation, dto, "error.srm_purchase_1026");
//					if(!responseData.isSuccess()) {
//						return responseData;
//					}
//				}
//			}
//		}
		//更新托盘信息
		OutBarcode barcode = new OutBarcode();
		barcode.setObarcodeId(dto.getOutbarcodeId());
		barcode = outBarcodeService.selectByPrimaryKey(requestContext, barcode);
		barcode.setStatus("ZZ");;
		outBarcodeService.updateByPrimaryKeySelective(requestContext, barcode);
		for(Float obarcodeId : dto.getRqObarcodeIdList()) {			
			//更新主界面勾选容器信息
			OutBarcode outBarcode = new OutBarcode();
			outBarcode.setObarcodeId(obarcodeId);
			outBarcode = outBarcodeService.selectByPrimaryKey(requestContext, outBarcode);
			if(!"TP".equals(outBarcode.getObarcodeType())) {				
				outBarcode.setPackingFlag("Y");
				outBarcodeService.updateByPrimaryKeySelective(requestContext, outBarcode);
			}
			
			//新增包装标签关联关系表
			BarcodeRelation rel = new BarcodeRelation();
			rel.setOutbarcodeId(dto.getOutbarcodeId());
			rel.setOutbarcodeType("TP");
			rel.setInbarcodeId(obarcodeId);
			rel.setInbarcodeType(outBarcode.getObarcodeType());
			rel.setEnableFlag("Y");
			
			self().insertSelective(requestContext, rel);
		}
		return responseData;
	}

	private ResponseData checkFunction(IRequest requestContext,BarcodeRelation relation,BarcodeRelation dto,String fastCode) {
		ResponseData responseData = new ResponseData();
		//获取描述		
		SmallBarcode smallBarcode = new SmallBarcode();
		smallBarcode.setSbarcodeId(relation.getInbarcodeId());
		smallBarcode = smallBarcodeService.selectByPrimaryKey(requestContext, smallBarcode);
		
		if(!(dto.getPlantId() == smallBarcode.getPlantId() && dto.getItemId() == smallBarcode.getItemId()
				&& dto.getItemVersion().equals(smallBarcode.getItemVersion()) && dto.getLotCode().equals(smallBarcode.getLotCode()))) {
			responseData.setMessage(SystemApiMethod.getPromptDescription(requestContext, iPromptService, fastCode));
			responseData.setSuccess(false);
			return responseData;
		}
		return responseData;
	}

	@Override
	public void updateTicketId(BarcodeRelation barcodeRelation) {
		// TODO Auto-generated method stub
		mapper.updateTicketId(barcodeRelation);
	}

	@Override
	public List<Float> querySmallBarcodeId(Float codeId, String codeType) {
		if(codeId == null || StringUtils.isBlank(codeType)) {
			return null;
		}
		List<Float> smallBarcodeIdList = new ArrayList(); 
		// TODO Auto-generated method stub
		if("TP".equals(codeType)) {
			BarcodeRelation relation = new BarcodeRelation();
			relation.setOutbarcodeId(codeId);
			relation.setEnableFlag("Y");
			List<BarcodeRelation> relationList = mapper.select(relation);
			if(relationList != null && relationList.size() > 0) {
				for(BarcodeRelation barcodeRelation : relationList) {
					//类型为物料
					if("WL".equals(barcodeRelation.getInbarcodeType())) {
						//获取物料标签id
						smallBarcodeIdList.add(barcodeRelation.getInbarcodeId());
					}else {
						//找内层
						BarcodeRelation rel = new BarcodeRelation();
						rel.setOutbarcodeId(barcodeRelation.getInbarcodeId());
						rel.setEnableFlag("Y");
						List<BarcodeRelation> relList = mapper.select(rel);
						if(relList != null && relList.size() > 0) {
							for(BarcodeRelation codeRel : relList) {
								//获取物料标签id
								smallBarcodeIdList.add(codeRel.getInbarcodeId());
							}
						}
					}
				}
			}
		}else if("WL".equals(codeType)) {
			smallBarcodeIdList.add(codeId); 
		}else {
			//type: GS  ZB ZZ	
			BarcodeRelation relation = new BarcodeRelation();
			relation.setOutbarcodeId(codeId);
			relation.setEnableFlag("Y");
			List<BarcodeRelation> relationList = mapper.select(relation);
			if(relationList != null && relationList.size() > 0) {
				for(BarcodeRelation barcodeRelation : relationList) {
					smallBarcodeIdList.add(barcodeRelation.getInbarcodeId());
				}
			}
		}
		return smallBarcodeIdList;
	}
}