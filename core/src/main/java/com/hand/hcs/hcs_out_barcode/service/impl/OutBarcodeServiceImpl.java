package com.hand.hcs.hcs_out_barcode.service.impl;

import com.github.pagehelper.PageHelper;
import com.hand.hap.account.dto.User;
import com.hand.hap.account.service.IUserService;
import com.hand.hap.core.IRequest;
import com.hand.hap.system.dto.ResponseData;
import com.hand.hap.system.service.IPromptService;
import com.hand.hap.system.service.impl.BaseServiceImpl;
import com.hand.hap.util.SoapPostUtil;
import com.hand.hap.util.SystemApiMethod;
import com.hand.hap.webservice.ws.idto.ContainerInformation;
import com.hand.hcm.hcm_plant.dto.Plant;
import com.hand.hcm.hcm_plant.service.IPlantService;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;
import java.util.stream.Collectors;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hand.hcs.hcs_barcode.dto.SmallBarcode;
import com.hand.hcs.hcs_barcode.dto.SmallBarcodeControl;
import com.hand.hcs.hcs_barcode.service.ISmallBarcodeControlService;
import com.hand.hcs.hcs_barcode.service.ISmallBarcodeService;
import com.hand.hcs.hcs_barcode_relation.dto.BarcodeRelation;
import com.hand.hcs.hcs_barcode_relation.mapper.BarcodeRelationMapper;
import com.hand.hcs.hcs_barcode_relation.service.IBarcodeRelationService;
import com.hand.hcs.hcs_delivery_ticket.dto.DeliveryTicketL;
import com.hand.hcs.hcs_delivery_ticket.service.IDeliveryTicketLService;
import com.hand.hcs.hcs_out_barcode.dto.OutBarcode;
import com.hand.hcs.hcs_out_barcode.mapper.OutBarcodeMapper;
import com.hand.hcs.hcs_out_barcode.service.IOutBarcodeService;
import com.hand.hcs.hcs_out_barcode_control.dto.OutBarcodeControl;
import com.hand.hcs.hcs_out_barcode_control.service.IOutBarcodeControlService;
import com.hand.hcs.hcs_suppliers.dto.Suppliers;
import com.hand.hcs.hcs_suppliers.service.ISuppliersService;
import com.hand.itf.itf_container_info.dto.ContainerInfo;
import com.hand.itf.itf_container_info.mapper.ContainerInfoMapper;

import jodd.util.StringUtil;

import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(rollbackFor = Exception.class)
public class OutBarcodeServiceImpl extends BaseServiceImpl<OutBarcode> implements IOutBarcodeService {
	@Autowired
	private OutBarcodeMapper mapper;
	@Autowired
	private ISuppliersService suppliersService;
	@Autowired
	private IOutBarcodeControlService outBarcodeControlService;
	@Autowired
	private ISmallBarcodeService smallBarcodeService;
	@Autowired
	private ISmallBarcodeControlService smallBarcodeControlService;
	@Autowired
	private IBarcodeRelationService barcodeRelationService;
	@Autowired
	private IPromptService iPromptService;
	@Autowired
	private IPlantService plantService;
	@Autowired
	private IDeliveryTicketLService deliveryTicketLService;
	@Autowired
	BarcodeRelationMapper barcodeRelationMapper;

	@Override
	public List<OutBarcode> query(IRequest requestContext, OutBarcode outBarcode, int page, int pageSize) {
		PageHelper.startPage(page, pageSize);
		return mapper.query(outBarcode);
	}

	@Override
	public String getobarcode(IRequest requestContext, OutBarcode outBarcode) {

		int num = mapper.selectMaxNum(outBarcode);
		num = num + 1;
		Suppliers supplier = new Suppliers();
		supplier.setSupplierId((float) outBarcode.getSupplierId());
		supplier = suppliersService.selectByPrimaryKey(requestContext, supplier);

		// 序列号
		String numStr = String.format("%04d", num);
		// 年月日：yymmdd
		SimpleDateFormat simple = new SimpleDateFormat("yyyyMMdd");
		String now = simple.format(new Date()).substring(1);

		String orderNum = supplier.getSupplierCode() + now + '2' + numStr;

		return orderNum;
	}

//	@Override
//	public ResponseData addInfo(IRequest requestContext, OutBarcode outBarcode) {
//		ResponseData responseData = new ResponseData();
//		for(int i=0;i<outBarcode.getCodeSum();i++) {			
//			String orderNum = getobarcode(requestContext,outBarcode);
//			OutBarcode dto = new OutBarcode();
//			dto.setObarcode(orderNum);
//			if(outBarcode.getPackingSize() != null) {				
//				dto.setPackingSize(outBarcode.getPackingSize());
//			}
//			dto.setPackingLevel(outBarcode.getPackingLevel());
//			if(outBarcode.getRemarks() != null) {			
//				dto.setRemarks(outBarcode.getRemarks());
//			}
//			dto.setStatus("N");
//			dto.setObarcodeType("2");
//			dto.setFreeFlag("N");
//			dto.setEnableFlag("Y");
//			dto.setPackingFlag("N");
//			dto.setPrintTime((float)0);
//			//insert
//			dto = self().insertSelective(requestContext, dto);
//			
//			OutBarcodeControl outBarcodeControl = new OutBarcodeControl();
//			outBarcodeControl.setObarcodeId(dto.getObarcodeId());
//			outBarcodeControl.setSupplierId(outBarcode.getSupplierId());
//			//insert
//			outBarcodeControlService.insertSelective(requestContext, outBarcodeControl);
//		}
//		responseData.setMessage("新建成功");
//		return responseData;
//	}
	@Override
	public ResponseData addInfo(IRequest requestContext, OutBarcode outBarcode) {
		ResponseData responseData = new ResponseData();
		Plant plant = new Plant();
		plant.setPlantCode(outBarcode.getPlantCode());
		List<Plant> plantList = plantService.select(requestContext, plant, 0, 0);
		if (plantList == null || plantList.size() != 1) {
			responseData.setMessage("PLANT获取失败:" + outBarcode.getPlantCode());
			responseData.setSuccess(false);
			return responseData;
		}
		for (int i = 0; i < outBarcode.getCodeSum(); i++) {
			Map<String, String> map = getMaxNum(outBarcode);

			String obarcode = outBarcode.getObarcodeType() + outBarcode.getSupplierCode() + map.get("maxObarcode")
					+ "000";

			String obarcodeNum = outBarcode.getSupplierCode() + "-" + outBarcode.getObarcodeTypeDesc() + "-"
					+ map.get("maxObarcodeNum");

			OutBarcode dto = new OutBarcode();
			dto.setObarcode(obarcode);
			dto.setObarcodeNum(obarcodeNum);
			dto.setObarcodeType(outBarcode.getObarcodeType());
			dto.setPlantId(plantList.get(0).getPlantId());

			if (outBarcode.getRemarks() != null) {
				dto.setRemarks(outBarcode.getRemarks());
			}
			dto.setSupplierId(outBarcode.getSupplierId());
			dto.setStatus("KZ");
			dto.setEnableFlag("Y");
			dto.setFreeFlag("N");
			dto.setPackingFlag("N");
			dto.setPrintTime((float) 0);
			// insert
			dto = self().insertSelective(requestContext, dto);
		}
		return responseData;
	}

	@Override
	public ResponseData printQuery(IRequest requestContext, List<OutBarcode> outBarcodeList) {
		ResponseData responseData = new ResponseData();
		List<OutBarcode> list = new ArrayList();
		for (OutBarcode outBarcode : outBarcodeList) {
			SmallBarcode smallBarcode = new SmallBarcode();
			smallBarcode.setObarcodeId(outBarcode.getObarcodeId());
			List<OutBarcode> outList = mapper.printQueryOutCode(outBarcode);
			OutBarcode dto = new OutBarcode();
			dto = outList.get(0);
			List<SmallBarcode> smallBarcodeList = smallBarcodeService.printQuery(smallBarcode);
			dto.setSmallBarcodeList(smallBarcodeList);
			list.add(dto);

			// 更新打印次数
			OutBarcode barCode = new OutBarcode();
			barCode.setObarcodeId(outBarcode.getObarcodeId());
			barCode = mapper.selectByPrimaryKey(barCode);
			if ("N".equals(outBarcode.getStatus())) {
				barCode.setPrintTime(barCode.getPrintTime() + 1);
				barCode.setStatus("P");
			} else {
				barCode.setPrintTime(barCode.getPrintTime() + 1);
			}
			self().updateByPrimaryKeySelective(requestContext, barCode);
		}
		responseData.setRows(list);
		return responseData;
	}

	@Override
	public ResponseData changeFlag(IRequest requestContext, List<OutBarcode> outBarcodeList) {
		ResponseData responseData = new ResponseData();
		for (OutBarcode outBarcode : outBarcodeList) {
			OutBarcode dto = new OutBarcode();
			dto.setEnableFlag("N");
			dto.setObarcodeId(outBarcode.getObarcodeId());
			dto.setStatus("SX");
			// update OutBarcode
			self().updateByPrimaryKeySelective(requestContext, dto);

		}
		responseData.setRows(outBarcodeList);
		return responseData;
	}

	@Override
	public List<OutBarcode> bindQuery(IRequest requestContext, OutBarcode outBarcode, int page, int pageSize) {
		PageHelper.startPage(page, pageSize);
		return mapper.bindQuery(outBarcode);
	}

	@Override
	public ResponseData checkBind(IRequest requestContext, List<SmallBarcode> dto) {
		ResponseData responseData = new ResponseData();
		BarcodeRelation barcodeRelation = new BarcodeRelation();
		// 校验物料标签是否已绑定送货单
		for (SmallBarcode sBarcode : dto) {
			SmallBarcodeControl control = new SmallBarcodeControl();
			control.setSbarcodeId(sBarcode.getSbarcodeId());
			List<SmallBarcodeControl> controlList = smallBarcodeControlService.select(requestContext, control, 0, 0);
			if (controlList != null && controlList.size() > 0) {
				for (SmallBarcodeControl sControl : controlList) {
					if (sControl.getTicketId() != null) {
						String msg = SystemApiMethod.getPromptDescription(requestContext, iPromptService,
								"error.srm_purchase_1075");
						responseData.setMessage(msg);
						responseData.setSuccess(false);
						return responseData;
					}
				}
			}
		}
		barcodeRelation.setOutbarcodeId(dto.get(0).getObarcodeId());
		barcodeRelation.setOutbarcodeType(dto.get(0).getObarcodeType());
		barcodeRelation.setEnableFlag("Y");

		List<BarcodeRelation> barcodeRelationList = barcodeRelationService.select(requestContext, barcodeRelation, 0,
				0);
		if (barcodeRelationList != null && barcodeRelationList.size() > 0) {
			if ("TP".equals(dto.get(0).getObarcodeType())) {
				List<SmallBarcode> smallBarcodeList = dto;
				for (BarcodeRelation relation : barcodeRelationList) {
					if ("ZZ".equals(relation.getInbarcodeType()) || "ZB".equals(relation.getInbarcodeType())
							|| "GS".equals(relation.getInbarcodeType())) {
						BarcodeRelation relationData = new BarcodeRelation();

						relationData.setOutbarcodeId(relation.getInbarcodeId());
						// relationData.setOutbarcodeType(relation.getInbarcodeType());
						relationData.setEnableFlag("Y");

						List<BarcodeRelation> list = barcodeRelationService.select(requestContext, relationData, 0, 0);
						for (BarcodeRelation rel : barcodeRelationList) {
							// 校验物料标签
							responseData = checkSmallBarcode(requestContext, rel, smallBarcodeList,
									barcodeRelationList);
							if (!responseData.isSuccess()) {
								return responseData;
							}
						}
					}
					// 校验物料标签
					responseData = checkSmallBarcode(requestContext, relation, smallBarcodeList, barcodeRelationList);
					if (!responseData.isSuccess()) {
						return responseData;
					}
				}
			} else {
				String msg = SystemApiMethod.getPromptDescription(requestContext, iPromptService,
						"error.srm_purchase_1007");
				responseData.setRows(barcodeRelationList);
				responseData.setSuccess(false);
				responseData.setMessage(msg);
				return responseData;
			}
		}

		return responseData;
	}

	/**
	 * 物料标签校验
	 * 
	 * @param requestContext
	 * @param relation
	 * @param smallBarcodeList
	 * @param systemApiMethod
	 * @param barcodeRelationList
	 * @return
	 */
	private ResponseData checkSmallBarcode(IRequest requestContext, BarcodeRelation relation,
			List<SmallBarcode> smallBarcodeList, List<BarcodeRelation> barcodeRelationList) {
		ResponseData responseData = new ResponseData();
		SmallBarcode smallBarcode = new SmallBarcode();
		smallBarcode.setSbarcodeId(relation.getInbarcodeId());
		smallBarcode = smallBarcodeService.selectByPrimaryKey(requestContext, smallBarcode);
		if (smallBarcode != null) {
			for (SmallBarcode small : smallBarcodeList) {
				if (!(smallBarcode.getPlantId() == small.getPlantId() && smallBarcode.getItemId() == small.getItemId()
						&& smallBarcode.getLotCode() == small.getLotCode()
						&& smallBarcode.getItemVersion() == small.getItemVersion())) {
					String msg = SystemApiMethod.getPromptDescription(requestContext, iPromptService,
							"error.srm_purchase_1011");
					responseData.setRows(barcodeRelationList);
					responseData.setSuccess(false);
					responseData.setMessage(msg);
					return responseData;
				}
			}
		}
		return responseData;
	}

	@Override
	public Map<String, String> getMaxNum(OutBarcode outBarcode) {
		Map<String, String> map = new HashMap(16);

		int maxObarcode = mapper.selectMaxObarcode(outBarcode);
		int maxObarcodeNum = mapper.selectMaxObarcodeNum(outBarcode);
		maxObarcode++;
		maxObarcodeNum++;
		String maxObarcodeStr = String.format("%06d", maxObarcode);
		String maxObarcodeNumStr = String.format("%06d", maxObarcodeNum);

		map.put("maxObarcode", maxObarcodeStr);
		map.put("maxObarcodeNum", maxObarcodeNumStr);
		return map;
	}

	@Override
	public ResponseData bindValidator(IRequest requestContext, List<OutBarcode> dto) {
		ResponseData responseData = new ResponseData();
		List<SmallBarcode> smallBarcodeList = new ArrayList();
		for (OutBarcode outBarcode : dto) {
			BarcodeRelation barcodeRealation = new BarcodeRelation();
			barcodeRealation.setOutbarcodeId(outBarcode.getObarcodeId());
			barcodeRealation.setInbarcodeType("WL");
			barcodeRealation.setEnableFlag("Y");
			List<BarcodeRelation> relationList = barcodeRelationService.select(requestContext, barcodeRealation, 0, 0);
			if (relationList != null && relationList.size() > 0) {
				for (BarcodeRelation relation : relationList) {
					SmallBarcode smallBarcode = new SmallBarcode();
					smallBarcode.setSbarcodeId(relation.getInbarcodeId());
					smallBarcode = smallBarcodeService.selectByPrimaryKey(requestContext, smallBarcode);
					smallBarcodeList.add(smallBarcode);
				}
			}
		}
		for (int i = 1; i < smallBarcodeList.size(); i++) {
			if (!(smallBarcodeList.get(i - 1).getPlantId().equals(smallBarcodeList.get(i).getPlantId())
					&& smallBarcodeList.get(i - 1).getItemId().equals(smallBarcodeList.get(i).getItemId())
					&& smallBarcodeList.get(i - 1).getItemVersion().equals(smallBarcodeList.get(i).getItemVersion())
					&& smallBarcodeList.get(i - 1).getLotCode().equals(smallBarcodeList.get(i).getLotCode()))) {
				responseData.setMessage("ERROR-1");
				responseData.setSuccess(false);
				responseData.setRows(smallBarcodeList);
				return responseData;
			}
		}
//		int count = smallBarcodeList.size();
//		List<SmallBarcode> list = smallBarcodeList.stream().collect(Collectors.collectingAndThen(
//				Collectors.toCollection(() -> new TreeSet<>(Comparator.comparing(
//						o -> o.getPlantId() + ";" + o.getItemId() + ";" + o.getItemVersion() + ";" + o.getLotCode()))),
//				ArrayList::new));
//		if (count == list.size()) {
//			
//		}
		// responseData.setRows(list);
		responseData.setRows(smallBarcodeList);
		return responseData;
	}

	@Override
	public List<OutBarcode> outBindQuery(IRequest requestContext, OutBarcode outBarcode, int page, int pageSize) {
		PageHelper.startPage(page, pageSize);
		return mapper.outBindQuery(outBarcode);
	}

	@Override
	public List<OutBarcode> confirmBind(IRequest requestContext, List<OutBarcode> dto) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ResponseData unBind(IRequest requestContext, List<OutBarcode> dto) {
		ResponseData responseData = new ResponseData();

		for (OutBarcode outBarcode : dto) {
			// 更新箱子标签
			OutBarcode barcode = new OutBarcode();
			barcode.setObarcodeId(outBarcode.getObarcodeId());
			barcode.setPackingFlag("N");
			self().updateByPrimaryKeySelective(requestContext, barcode);

			// 失效包装关系
			BarcodeRelation relation = new BarcodeRelation();
			relation.setInbarcodeId(outBarcode.getObarcodeId());
			relation.setEnableFlag("Y");
			List<BarcodeRelation> relationList = barcodeRelationService.select(requestContext, relation, 0, 0);
			if (relationList != null && relationList.size() > 0) {
				for (BarcodeRelation barcodeRelation : relationList) {
					barcodeRelation.setEnableFlag("N");
					barcodeRelationService.updateByPrimaryKeySelective(requestContext, barcodeRelation);

					// 更新箱子为空置
					BarcodeRelation rel = new BarcodeRelation();
					rel.setOutbarcodeId(barcodeRelation.getOutbarcodeId());
					rel.setEnableFlag("Y");
					List<BarcodeRelation> relList = barcodeRelationService.select(requestContext, rel, 0, 0);
					if (relList == null || relList.size() == 0) {
						OutBarcode oBarcode = new OutBarcode();
						oBarcode.setObarcodeId(barcodeRelation.getOutbarcodeId());
						oBarcode.setStatus("KZ");
						self().updateByPrimaryKeySelective(requestContext, oBarcode);
					}
				}
			}

		}
		responseData.setRows(dto);
		return responseData;
	}

	@Override
	public List<OutBarcode> queryDetail(IRequest requestContext, OutBarcode outBarcode, int page, int pageSize) {
		PageHelper.startPage(page, pageSize);
		List<OutBarcode> outBarcodeList;
		if ("TP".equals(outBarcode.getObarcodeType())) {
			// 托盘详情
			outBarcodeList = mapper.tpQuery(outBarcode);
		} else {
			// 箱子详情
			outBarcodeList = mapper.xzQuery(outBarcode);
		}
		return outBarcodeList;
	}

	@Override
	public List<OutBarcode> bindQueryLeft(IRequest requestContext, OutBarcode outBarcode, int page, int pageSize) {
		PageHelper.startPage(page, pageSize);
		List<OutBarcode> list = new ArrayList();
		if ("WL".equals(outBarcode.getObarcodeType())) {
			list = mapper.wlQuery(outBarcode);
		} else {
			list = mapper.unWlQuery(outBarcode);
		}
		return list;
	}

	@Override
	public List<OutBarcode> queryRight(IRequest requestContext, OutBarcode outBarcode, int page, int pageSize) {
		PageHelper.startPage(page, pageSize);
		return mapper.queryRight(outBarcode);
	}

	@Override
	public List<OutBarcode> bind(IRequest requestContext, List<OutBarcode> dto) {
		float ticketId = dto.get(0).getTicketId();
		float ticketLineId = dto.get(0).getTicketLineId();
		float barcodeQty = dto.get(0).getBarcodeQty();
		// 1.清空容器标签/物料标签和送货单行的绑定关系
		BarcodeRelation barcodeRelation = new BarcodeRelation();
		barcodeRelation.setTicketLineId(dto.get(0).getTicketLineId());
		barcodeRelation.setEnableFlag("Y");
		List<BarcodeRelation> barcodeRelationList = barcodeRelationService.select(requestContext, barcodeRelation, 0,
				0);
		if (barcodeRelationList != null && barcodeRelationList.size() > 0) {
			for (BarcodeRelation relation : barcodeRelationList) {
				// 更新里层容器状态
				updateOutBarcodeStatus(requestContext, relation.getInbarcodeType(), relation.getInbarcodeId(), "ZZ");
				// 更新外层容器状态
				updateOutBarcodeStatus(requestContext, relation.getOutbarcodeType(), relation.getOutbarcodeId(), "ZZ");

				// 清空送货单头行id
				barcodeRelationService.updateTicketId(relation);
				// 获取物料标签id
				List<Float> smallBarcodeIdList = barcodeRelationService.querySmallBarcodeId(relation.getOutbarcodeId(),
						relation.getOutbarcodeType());
				if (smallBarcodeIdList != null && smallBarcodeIdList.size() > 0) {
					for (float smallBarcodeId : smallBarcodeIdList) {
						// 更新物料标签状态为新建：N
						updateSmallBarcodeStatus(requestContext, smallBarcodeId, "N");

						// 清空物料标签控制表的送货单头行id
						SmallBarcodeControl smallBarcodeControl = new SmallBarcodeControl();
						smallBarcodeControl.setSbarcodeId(smallBarcodeId);
						smallBarcodeControlService.updateTicketIdBySbarcodeId(smallBarcodeControl);
					}
				}
			}
		}
		SmallBarcodeControl smallBarcodeControl = new SmallBarcodeControl();
		smallBarcodeControl.setTicketLineId(dto.get(0).getTicketLineId());
		List<SmallBarcodeControl> smallBarcodeControlList = smallBarcodeControlService.select(requestContext,
				smallBarcodeControl, 0, 0);
		// 清空物料标签控制表的送货单头行id
		smallBarcodeControlService.updateTicketIdByLineId(smallBarcodeControl);
		// 更新标签状态
		if (smallBarcodeControlList != null && smallBarcodeControlList.size() > 0) {
			for (SmallBarcodeControl sControl : smallBarcodeControlList) {
				// 更新物料标签状态为新建：N
				updateSmallBarcodeStatus(requestContext, sControl.getSbarcodeId(), "N");
			}
		}

		// 2.建立容器标签/物料标签和送货单行的新绑定关系
		for (OutBarcode obarcode : dto) {
			// 获取物料标签id
			List<Float> smallBarcodeIdList = barcodeRelationService.querySmallBarcodeId(obarcode.getObarcodeId(),
					obarcode.getObarcodeType());
			if (smallBarcodeIdList != null && smallBarcodeIdList.size() > 0) {
				for (float smallBarcodeId : smallBarcodeIdList) {
					// 更新标签状态为已绑定 T
					SmallBarcode sBarcode = new SmallBarcode();
					sBarcode.setSbarcodeId(smallBarcodeId);
					sBarcode.setStatus("T");
					smallBarcodeService.updateByPrimaryKeySelective(requestContext, sBarcode);
					// 建立绑定关系
					SmallBarcodeControl sBarcodeControl = new SmallBarcodeControl();
					sBarcodeControl.setSbarcodeId(smallBarcodeId);
					List<SmallBarcodeControl> sBarcodeControlList = smallBarcodeControlService.select(requestContext,
							sBarcodeControl, 0, 0);
					if (sBarcodeControlList != null && sBarcodeControlList.size() > 0) {
						for (SmallBarcodeControl barcodeControl : sBarcodeControlList) {
							barcodeControl.setTicketId(ticketId);
							barcodeControl.setTicketLineId(ticketLineId);
							smallBarcodeControlService.updateByPrimaryKeySelective(requestContext, barcodeControl);
						}
					}
				}
			}

			if (StringUtils.isNotBlank(obarcode.getObarcodeType()) && !"WL".equals(obarcode.getObarcodeType())) {
				BarcodeRelation relation = new BarcodeRelation();
				relation.setEnableFlag("Y");
				relation.setOutbarcodeId(obarcode.getObarcodeId());
				List<BarcodeRelation> relationList = barcodeRelationService.select(requestContext, relation, 0, 0);
				if (relationList != null && relationList.size() > 0) {
					for (BarcodeRelation rel : relationList) {
						rel.setTicketId(ticketId);
						rel.setTicketLineId(ticketLineId);
						barcodeRelationService.updateByPrimaryKeySelective(requestContext, rel);

						// 更新里层容器状态
						updateOutBarcodeStatus(requestContext, rel.getInbarcodeType(), rel.getInbarcodeId(), "BT");
						// 更新外层容器状态
						updateOutBarcodeStatus(requestContext, rel.getOutbarcodeType(), rel.getOutbarcodeId(), "BT");
						// 继续找里层(物料标签)
						BarcodeRelation barcodeRel = new BarcodeRelation();
						barcodeRel.setOutbarcodeId(rel.getInbarcodeId());
						barcodeRel.setEnableFlag("Y");
						List<BarcodeRelation> barcodeRelList = barcodeRelationService.select(requestContext, barcodeRel,
								0, 0);
						if (barcodeRelList != null && barcodeRelList.size() > 0) {
							for (BarcodeRelation codeRel : barcodeRelList) {
								codeRel.setTicketId(ticketId);
								codeRel.setTicketLineId(ticketLineId);
								barcodeRelationService.updateByPrimaryKeySelective(requestContext, codeRel);

							}
						}
					}
				}
			}
		}
		// 更新已绑定标签数量
		DeliveryTicketL ticketl = new DeliveryTicketL();
		ticketl.setTicketLineId(ticketLineId);
		ticketl.setBarcodeQty(barcodeQty);
		deliveryTicketLService.updateByPrimaryKeySelective(requestContext, ticketl);

		return dto;
	}

	/**
	 * 更新标签状态
	 * 
	 * @param requestContext
	 * @param sBarcodeId
	 */
	private void updateSmallBarcodeStatus(IRequest requestContext, float sBarcodeId, String smallBarcodeType) {
		SmallBarcode sBarcode = new SmallBarcode();
		sBarcode.setSbarcodeId(sBarcodeId);
		sBarcode.setStatus(smallBarcodeType);
		smallBarcodeService.updateByPrimaryKeySelective(requestContext, sBarcode);
	}

	/**
	 * 更新容器状态
	 * 
	 * @param oBarcodeType 类型
	 * @param oBarcodeId   容器id
	 * @param type         更改目标状态
	 */
	private void updateOutBarcodeStatus(IRequest requestContext, String oBarcodeType, float oBarcodeId, String type) {
		if ("TP".equals(oBarcodeType) || "GS".equals(oBarcodeType) || "ZZ".equals(oBarcodeType)
				|| "ZB".equals(oBarcodeType)) {
			OutBarcode oBarcode = new OutBarcode();
			oBarcode.setObarcodeId(oBarcodeId);
			oBarcode.setStatus(type);
			self().updateByPrimaryKeySelective(requestContext, oBarcode);
		}
	}

	@Autowired
	ContainerInfoMapper containerInfoMapper;

	@Override
	public SoapPostUtil.Response transferContainer(ContainerInfo ci) {
		SoapPostUtil.Response response = new SoapPostUtil.Response();
		ci.setProcessStatus("Y");
		ci.setProcessTime(new Date());
		try {
			if (!StringUtil.isEmpty(ci.getCleanFlag()) && "Y".equals(ci.getCleanFlag())) {
				OutBarcode obs = new OutBarcode();
				obs.setObarcode(ci.getContainerId());
				List<OutBarcode> res = mapper.select(obs);
				if (res != null && res.size() > 0) {
					OutBarcode obu = new OutBarcode();
					obu.setObarcodeId(res.get(0).getObarcodeId());
					obu.setPackingFlag("N");
					obu.setStatus("KZ");
					obu.setLastUpdatedBy(-2l);
					mapper.updateByPrimaryKeySelective(obu);
					// barcodeRelationMapper
					BarcodeRelation brs = new BarcodeRelation();
					brs.setOutbarcodeId(res.get(0).getObarcodeId());
					brs.setEnableFlag("Y");
					List<BarcodeRelation> brl = barcodeRelationMapper.select(brs);
					if (brl != null && brl.size() > 0) {
						for (BarcodeRelation barcodeRelation : brl) {
							BarcodeRelation bru = new BarcodeRelation();
							bru.setBarcodeRelationId(barcodeRelation.getBarcodeRelationId());
							bru.setEnableFlag("N");
							bru.setLastUpdateDate(new Date());
							barcodeRelationMapper.updateByPrimaryKeySelective(bru);
						}
					}
				} else {
					ci.setProcessStatus("E");
					response.setResult(false);
					response.setMessage("未查询到[" + ci.getContainerId() + "]的相关信息");
					ci.setMessage("未查询到[" + ci.getContainerId() + "]的相关信息");
				}

			}
		} catch (Exception e) {
			response.setResult(false);
			response.setMessage(e.getMessage());
			ci.setMessage(e.getMessage());
			ci.setProcessStatus("E");
		}
		containerInfoMapper.updateByPrimaryKeySelective(ci);
		return response;
	}

	@Override
	public List<OutBarcode> updatePrintTime(IRequest requestContext, List<OutBarcode> dto) {
		for (OutBarcode outBarcode : dto) {
			OutBarcode barcode = new OutBarcode();
			barcode.setObarcodeId(outBarcode.getObarcodeId());
			if (outBarcode.getPrintTime() == null) {
				barcode.setPrintTime(1F);
			} else {
				barcode.setPrintTime(outBarcode.getPrintTime() + 1F);
			}
			self().updateByPrimaryKeySelective(requestContext, barcode);
		}
		return dto;
	}

}