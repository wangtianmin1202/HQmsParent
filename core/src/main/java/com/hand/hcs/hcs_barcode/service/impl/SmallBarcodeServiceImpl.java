package com.hand.hcs.hcs_barcode.service.impl;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.PageHelper;
import com.hand.hap.core.IRequest;
import com.hand.hap.system.dto.ResponseData;
import com.hand.hap.system.service.impl.BaseServiceImpl;
import com.hand.hcm.hcm_item.dto.Item;
import com.hand.hcs.hcs_barcode.dto.SmallBarcode;
import com.hand.hcs.hcs_barcode.dto.SmallBarcodeControl;
import com.hand.hcs.hcs_barcode.mapper.SmallBarcodeControlMapper;
import com.hand.hcs.hcs_barcode.mapper.SmallBarcodeMapper;
import com.hand.hcs.hcs_barcode.service.ISmallBarcodeControlService;
import com.hand.hcs.hcs_barcode.service.ISmallBarcodeService;
import com.hand.hcs.hcs_barcode_relation.dto.BarcodeRelation;
import com.hand.hcs.hcs_barcode_relation.mapper.BarcodeRelationMapper;
import com.hand.hcs.hcs_barcode_relation.service.IBarcodeRelationService;
import com.hand.hcs.hcs_out_barcode.dto.OutBarcode;
import com.hand.hcs.hcs_out_barcode.mapper.OutBarcodeMapper;
import com.hand.hcs.hcs_out_barcode.service.IOutBarcodeService;
import com.hand.hcs.hcs_suppliers.dto.Suppliers;
import com.hand.hcs.hcs_suppliers.service.ISuppliersService;

@Service
@Transactional(rollbackFor = Exception.class)
public class SmallBarcodeServiceImpl extends BaseServiceImpl<SmallBarcode> implements ISmallBarcodeService {

	@Autowired
	private SmallBarcodeMapper smallBarcodeMapper;

	@Autowired
	private SmallBarcodeControlMapper smallBarcodeControlMapper;

	@Autowired
	private ISmallBarcodeControlService smallBarcodeControlService;

	@Autowired
	private ISuppliersService suppliersService;

	@Autowired
	private IBarcodeRelationService barcodeRelationService;
	@Autowired
	private IOutBarcodeService outBarcodeService;
	@Autowired
	BarcodeRelationMapper barcodeRelationMapper;
	@Autowired
	OutBarcodeMapper outBarcodeMapper;

	@Override
	public List<SmallBarcode> query(IRequest requestCtx, SmallBarcode smallBarcode, int page, int pageSize) {
		PageHelper.startPage(page, pageSize);
		return smallBarcodeMapper.query(smallBarcode);
	}

	@Override
	public ResponseData insertsmall(SmallBarcode dto, IRequest requestCtx, HttpServletRequest request) {

		ResponseData responseData = new ResponseData();

		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");

		for (int i = 0; i < dto.getCodeSum(); i++) {
			String caseNumber = (i + 1) + "/" + dto.getCodeSum();

			String orderNum = getsbarcode(requestCtx, dto);

			String s = dto.getQuantity() + "";
			if (s.indexOf(".") > 0) {
				// 正则表达
				s = s.replaceAll("0+?$", "");// 去掉后面无用的零
				s = s.replaceAll("[.]$", "");// 如小数点后面全是零则去掉小数点
			}

			StringBuffer barcodeContent = new StringBuffer();
			barcodeContent.append(dto.getItemCode()).append(",").append(dto.getLotCode()).append(",")
					.append(sdf.format(dto.getProductionTime())).append(",").append(s).append(",")
					.append(dto.getPackingSize() != null ? dto.getPackingSize() : "").append(",")
					.append(dto.getPoNumberPrint() != null ? dto.getPoNumberPrint() : "").append(",")
					.append(dto.getPackingLevel()).append(",")
					.append(dto.getSpreading() != null ? dto.getSpreading() : "").append(",")
					.append(dto.getItemVersion()).append(",").append(caseNumber);

			// dto.setSbarcode("jy_test");
			dto.setEnableFlag("Y");
			dto.setStatus("N");
			dto.setSbarcodeType("WL");
			dto.setFreeFlag("N");
			dto.setFreezeFlag("N");
			dto.setPackingFlag("N");
			dto.setPrintTime(0f);
			dto.setCaseNumber(caseNumber);
			dto.setBarcodeContent(barcodeContent + "");

			// 取物料表单位
			Item itemDto = new Item();
			itemDto.setPlantId(dto.getPlantId());
			itemDto.setItemId(dto.getItemId());
			String uom = smallBarcodeMapper.selectitems(itemDto);
			dto.setUom(uom);
			dto.setSbarcode(orderNum);

			dto = self().insertSelective(requestCtx, dto);// smallBarcodeMapper.insertSelective(dto);
			SmallBarcodeControl smallBarcodeControl = new SmallBarcodeControl();
			smallBarcodeControl.setSbarcodeId(dto.getSbarcodeId());
			smallBarcodeControl.setSupplierId(dto.getSupplierId());
			smallBarcodeControlService.insertSelective(requestCtx, smallBarcodeControl);
		}
		return responseData;
	}

	@Override
	public void changeFlag(IRequest requestCtx, List<SmallBarcode> smallBarcodeList) {
		for (SmallBarcode smallBarcode : smallBarcodeList) {
			SmallBarcode smallCode = new SmallBarcode();
			smallCode.setSbarcodeId(smallBarcode.getSbarcodeId());
			smallCode.setEnableFlag("N");
			smallCode.setStatus("C");
			self().updateByPrimaryKeySelective(requestCtx, smallCode);
		}
	}

	@Override
	public List<SmallBarcode> querySmallBarcode(IRequest requestCtx, SmallBarcode smallBarcode, int page,
			int pageSize) {
		PageHelper.startPage(page, pageSize);
		return smallBarcodeMapper.querySmallBarcode(smallBarcode);
	}

	@Override
	public ResponseData saveInfo(IRequest requestContext, List<SmallBarcode> smallBarcodeList) {
		ResponseData responseData = new ResponseData();
		Float bigId = smallBarcodeList.get(0).getBigId();
		List<SmallBarcode> smallCodeList = smallBarcodeList.stream()
				.filter(data -> !StringUtils.isEmpty(data.getBindFlag())).collect(Collectors.toList());
		for (SmallBarcode smallBarcode : smallCodeList) {
			SmallBarcode barCode = new SmallBarcode();
			barCode.setSbarcodeId(smallBarcode.getSbarcodeId());
			barCode = smallBarcodeMapper.selectByPrimaryKey(barCode);

			SmallBarcodeControl smallBarcodeControl = new SmallBarcodeControl();
			smallBarcodeControl.setSbarcodeId(barCode.getSbarcodeId());
			smallBarcodeControl = smallBarcodeControlMapper.selectOne(smallBarcodeControl);

			if ("Y".equals(smallBarcode.getBindFlag()) && smallBarcodeControl.getObarcodeId() == null) {
				// 绑定
				barCode.setPackingFlag("Y");
				// update SmallBarcode
				self().updateByPrimaryKeySelective(requestContext, barCode);
				smallBarcodeControl.setObarcodeId(bigId);
				// update SmallBarcodeControl
				smallBarcodeControlService.updateByPrimaryKeySelective(requestContext, smallBarcodeControl);
			} else if ("N".equals(smallBarcode.getBindFlag()) && smallBarcodeControl.getObarcodeId() != null) {
				// 解绑
				barCode.setPackingFlag("N");
				// update SmallBarcode
				self().updateByPrimaryKeySelective(requestContext, barCode);
				smallBarcodeControl.setObarcodeId(null);
				// update SmallBarcodeControl
				smallBarcodeControlService.updateObarcodeId(requestContext, smallBarcodeControl);
			}
		}
		return responseData;
	}

	@Override
	public List<SmallBarcode> printQuery(SmallBarcode smallBarcode) {
		// TODO Auto-generated method stub
		return smallBarcodeMapper.printQuery(smallBarcode);
	}

	@Override
	public List<SmallBarcode> updatePrintTime(IRequest requestContext, List<SmallBarcode> smallBarcodeList) {
		for (SmallBarcode smallBarcode : smallBarcodeList) {
			SmallBarcode smallCode = new SmallBarcode();
			smallCode.setSbarcodeId(smallBarcode.getSbarcodeId());
			if (smallBarcode.getPrintTime() == null) {
				smallCode.setPrintTime(1F);
			} else {
				smallCode.setPrintTime(smallBarcode.getPrintTime() + 1F);
			}
			// smallCode.setStatus("P");
			self().updateByPrimaryKeySelective(requestContext, smallCode);
		}
		return smallBarcodeList;
	}

	@Override
	public String getsbarcode(IRequest requestContext, SmallBarcode smallBarcode) {
		int num = smallBarcodeMapper.selectMaxNum(smallBarcode);
		num = num + 1;
		Suppliers supplier = new Suppliers();
		supplier.setSupplierId((float) smallBarcode.getSupplierId());
		supplier = suppliersService.selectByPrimaryKey(requestContext, supplier);

		// 序列号
		String numStr = String.format("%04d", num);
		// 年月日：yymmdd
		SimpleDateFormat simple = new SimpleDateFormat("yyyyMMdd");
		String now = simple.format(new Date());

		// String orderNum = supplier.getSupplierCode() + now + '1' + numStr;
		String orderNum = now + "-" + numStr;
		return orderNum;
	}

	@Override
	public List<SmallBarcode> unBind(IRequest requestContext, List<SmallBarcode> smallBarcodeList) {
		for (SmallBarcode smallBarcode : smallBarcodeList) {
			SmallBarcodeControl control = new SmallBarcodeControl();
			control.setSbarcodeId(smallBarcode.getSbarcodeId());
			List<SmallBarcodeControl> controlList = smallBarcodeControlService.select(requestContext, control, 0, 0);
			if (controlList != null && controlList.size() > 0) {
				for (SmallBarcodeControl smallBarcodeControl : controlList) {
					// 清空容器Id
					smallBarcodeControlMapper.updateObarcodeId(smallBarcodeControl);
				}
			}
			// 更新物料标签状态
			SmallBarcode small = new SmallBarcode();
			small.setPackingFlag("N");
			small.setSbarcodeId(smallBarcode.getSbarcodeId());

			self().updateByPrimaryKeySelective(requestContext, small);
			// 失效物料标签绑定关系
			BarcodeRelation relation = new BarcodeRelation();
			relation.setInbarcodeId(smallBarcode.getSbarcodeId());
			relation.setInbarcodeType(smallBarcode.getSbarcodeType());
			relation.setEnableFlag("Y");
			List<BarcodeRelation> relationList = barcodeRelationService.select(requestContext, relation, 0, 0);
			if (relationList != null && relationList.size() > 0) {
				for (BarcodeRelation barcodeRelation : relationList) {
					// 失效物料标签绑定关系
					barcodeRelation.setEnableFlag("N");
					barcodeRelationService.updateByPrimaryKeySelective(requestContext, barcodeRelation);

					BarcodeRelation rel = new BarcodeRelation();
					rel.setOutbarcodeId(barcodeRelation.getOutbarcodeId());
					rel.setEnableFlag("Y");
					List<BarcodeRelation> relList = barcodeRelationService.select(requestContext, rel, 0, 0);
					if (relList == null || relList.size() == 0) {
						// 更新容器状态为空置 KZ
						OutBarcode oBarcode = new OutBarcode();
						oBarcode.setObarcodeId(barcodeRelation.getOutbarcodeId());
						oBarcode = outBarcodeService.selectByPrimaryKey(requestContext, oBarcode);
						if (oBarcode != null && !"KZ".equals(oBarcode.getStatus())) {
							oBarcode.setStatus("KZ");

							// 捆包标识为N added by wtm 20200205
							oBarcode.setPackingFlag("N");
							outBarcodeService.updateByPrimaryKeySelective(requestContext, oBarcode);
						}
						/**
						 * 附加逻辑 added by wtm 20200105
						 */
						customOperate(oBarcode.getObarcodeId());
					}
				}
			}

		}
		return smallBarcodeList;
	}

	/**
	 * @description
	 * @author tianmin.wang
	 * @date 2020年1月5日
	 * @param obarcodeId
	 */
	private void customOperate(Float obarcodeId) {
		/**
		 * 再关联包装标签关系表 HCS_BARCODE_RELATION 的 inbarcode_id 如果不存在有效数据 不做处理 如果存在有效数据 将此数据失效
		 * 并根据其失效数据的外层标签id obarcode_id 关联包装标签关系表 HCS_BARCODE_RELATION 的outbarcode_id
		 * 如果不存在有效数据了 再关联容器标签表 HCS_OUT_BARCODE 将状态更新为 KZ -空置 否则 不做处理
		 */
		BarcodeRelation brSearch = new BarcodeRelation();
		brSearch.setInbarcodeId(obarcodeId);
		List<BarcodeRelation> resultList = barcodeRelationMapper.select(brSearch);
		resultList.stream().filter(p -> {
			return "Y".equals(p.getEnableFlag());
		}).forEach(p -> {
			p.setEnableFlag("N");// 失效此数据
			barcodeRelationMapper.updateByPrimaryKeySelective(p);
			BarcodeRelation brSearch1 = new BarcodeRelation();
			brSearch1.setOutbarcodeId(p.getOutbarcodeId());
			List<BarcodeRelation> resultList1 = barcodeRelationMapper.select(brSearch1);
			if (resultList1 == null || (resultList1 != null && resultList1.stream().filter(p1 -> {
				return "Y".equals(p1.getEnableFlag());
			}).count() == 0)) {// 关联容器标签表 HCS_OUT_BARCODE 将状态更新为 KZ
				OutBarcode updator = new OutBarcode();
				updator.setObarcodeId(p.getOutbarcodeId());
				updator.setStatus("KZ");
				outBarcodeMapper.updateByPrimaryKeySelective(updator);
			}
		});
	}

	@Override
	public List<SmallBarcode> confirmBind(IRequest requestContext, List<SmallBarcode> smallBarcodeList) {
		OutBarcode outBarcode = new OutBarcode();
		outBarcode.setObarcodeId(smallBarcodeList.get(0).getObarcodeId());
		outBarcode = outBarcodeService.selectByPrimaryKey(requestContext, outBarcode);
		outBarcode.setStatus("ZZ");
		outBarcode = outBarcodeService.updateByPrimaryKeySelective(requestContext, outBarcode);

		for (SmallBarcode smallBarcode : smallBarcodeList) {
			smallBarcode = mapper.selectByPrimaryKey(smallBarcode);
			smallBarcode.setPackingFlag("Y");
			// 更新标签
			smallBarcode = self().updateByPrimaryKeySelective(requestContext, smallBarcode);

			SmallBarcodeControl smallBarcodeControl = new SmallBarcodeControl();
			smallBarcodeControl.setSbarcodeId(smallBarcode.getSbarcodeId());
			List<SmallBarcodeControl> smallBarcodeControlList = smallBarcodeControlService.select(requestContext,
					smallBarcodeControl, 0, 0);
			if (smallBarcodeControlList != null && smallBarcodeControlList.size() > 0) {
				for (SmallBarcodeControl barcodeControl : smallBarcodeControlList) {
					barcodeControl.setObarcodeId(outBarcode.getObarcodeId());
					// 更新控制表
					smallBarcodeControlService.updateByPrimaryKeySelective(requestContext, barcodeControl);
				}
			}

			BarcodeRelation barcodeRelation = new BarcodeRelation();
			barcodeRelation.setInbarcodeId(smallBarcode.getSbarcodeId());
			barcodeRelation.setInbarcodeType(smallBarcode.getSbarcodeType());
			barcodeRelation.setOutbarcodeType(outBarcode.getObarcodeType());
			barcodeRelation.setOutbarcodeId(outBarcode.getObarcodeId());
			barcodeRelation.setEnableFlag("Y");
			// 新增关系信息
			barcodeRelationService.insertSelective(requestContext, barcodeRelation);
		}
		return smallBarcodeList;
	}

}