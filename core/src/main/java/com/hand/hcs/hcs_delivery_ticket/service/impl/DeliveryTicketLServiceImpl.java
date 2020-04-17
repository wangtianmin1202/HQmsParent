package com.hand.hcs.hcs_delivery_ticket.service.impl;

import java.text.MessageFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.PageHelper;
import com.hand.hap.account.dto.User;
import com.hand.hap.account.service.IUserService;
import com.hand.hap.core.IRequest;
import com.hand.hap.system.dto.Code;
import com.hand.hap.system.dto.CodeValue;
import com.hand.hap.system.dto.ResponseData;
import com.hand.hap.system.mapper.CodeMapper;
import com.hand.hap.system.mapper.CodeValueMapper;
import com.hand.hap.system.service.IPromptService;
import com.hand.hap.system.service.impl.BaseServiceImpl;
import com.hand.hap.util.SoapPostUtil;
import com.hand.hap.util.SystemApiMethod;
import com.hand.hcm.hcm_item.dto.Item;
import com.hand.hcm.hcm_item.service.IItemService;
import com.hand.hcs.hcs_asl.dto.Asl;
import com.hand.hcs.hcs_asl.mapper.AslMapper;
import com.hand.hcs.hcs_asl_control.dto.AslControl;
import com.hand.hcs.hcs_asl_control.mapper.AslControlMapper;
import com.hand.hcs.hcs_barcode.dto.SmallBarcode;
import com.hand.hcs.hcs_barcode.dto.SmallBarcodeControl;
import com.hand.hcs.hcs_barcode.service.ISmallBarcodeControlService;
import com.hand.hcs.hcs_barcode.service.ISmallBarcodeService;
import com.hand.hcs.hcs_barcode_relation.dto.BarcodeRelation;
import com.hand.hcs.hcs_barcode_relation.service.IBarcodeRelationService;
import com.hand.hcs.hcs_delivery_ticket.dto.DeliveryTicketH;
import com.hand.hcs.hcs_delivery_ticket.dto.DeliveryTicketL;
import com.hand.hcs.hcs_delivery_ticket.dto.TicketReport;
import com.hand.hcs.hcs_delivery_ticket.mapper.DeliveryTicketHMapper;
import com.hand.hcs.hcs_delivery_ticket.mapper.DeliveryTicketLMapper;
import com.hand.hcs.hcs_delivery_ticket.service.IDeliveryTicketHService;
import com.hand.hcs.hcs_delivery_ticket.service.IDeliveryTicketLService;
import com.hand.hcs.hcs_out_barcode.dto.OutBarcode;
import com.hand.hcs.hcs_out_barcode.service.IOutBarcodeService;
import com.hand.hcs.hcs_po_headers.dto.PoHeaders;
import com.hand.hcs.hcs_po_headers.mapper.PoHeadersMapper;
import com.hand.hcs.hcs_po_line_locations.dto.PoLineLocations;
import com.hand.hcs.hcs_po_line_locations.mapper.PoLineLocationsMapper;
import com.hand.hcs.hcs_po_lines.dto.PoLines;
import com.hand.hcs.hcs_po_lines.mapper.PoLinesMapper;
import com.hand.itf.itf_delivery_receipt.mapper.DeliveryReceiptMapper;

@Service
@Transactional(rollbackFor = Exception.class)
public class DeliveryTicketLServiceImpl extends BaseServiceImpl<DeliveryTicketL> implements IDeliveryTicketLService {
	@Autowired
	private DeliveryTicketLMapper mapper;
	@Autowired
	private IDeliveryTicketHService deliveryTicketHService;

	@Autowired
	private DeliveryTicketHMapper deliveryTicketHMapper;

	@Autowired
	private IUserService userService;
	@Autowired
	private IItemService itemService;
	@Autowired
	private IBarcodeRelationService barcodeRelationService;
	@Autowired
	private ISmallBarcodeControlService smallBarcodeControlService;
	@Autowired
	private IPromptService iPromptService;
	@Autowired
	private IOutBarcodeService outBarcodeService;
	@Autowired
	private ISmallBarcodeService smallBarcodeService;
	@Autowired
	private CodeMapper codeMapper;
	@Autowired
	private CodeValueMapper codeValueMapper;
	@Autowired
	DeliveryReceiptMapper deliveryReceiptMapper;
	@Autowired
	PoLinesMapper poLinesMapper;
	@Autowired
	PoLineLocationsMapper poLineLocationsMapper;

	@Autowired
	AslMapper aslMapper;
	@Autowired
	AslControlMapper aslControlMapper;
	@Autowired
	PoHeadersMapper poHeadersMapper;

	private final String COLUMN_1 = "column1";
	private final String COLUMN_2 = "column2";
	private final String COLUMN_3 = "column3";
	private final String COLUMN_4 = "column4";
	private final String COLUMN_5 = "column5";

	@Override
	public List<DeliveryTicketL> query(IRequest requestContext, DeliveryTicketL dto, int page, int pageSize) {
		List<Float> lineLocationIdList = new ArrayList();
		List<Float> ticketLineId = new ArrayList();
		boolean flag = false;
		for (float id : dto.getLineLocationIdList()) {
			if (id == -1) {
				flag = true;
				continue;
			}
			if (!flag) {
				lineLocationIdList.add(id);
			} else {
				ticketLineId.add(id);
			}

		}
		float ticketId = -1;
		if (dto.getTicketId() != null) {
			ticketId = dto.getTicketId();
		}
		if (lineLocationIdList != null) {
			dto.setLineLocationIdList(lineLocationIdList);
		} else {
			dto.setLineLocationIdList(null);
		}
		//PageHelper.startPage(page, pageSize);
		List<DeliveryTicketL> list = mapper.query(dto);
		float seq = (page - 1) * pageSize;
		float max = list.stream().map(p -> p.getTicketLineNum() == null ? 0f : p.getTicketLineNum()).max(Float::compare)
				.get();
		for (int i = 0; i < list.size(); i++) {
			if (list.get(i).getTicketLineNum() == null) {
				list.get(i).setTicketLineNum((float) seq + max + i + 1);
			}
		}
		return list;
	}

	@Override
	public ResponseData saveHeadLine(IRequest requestContext, List<DeliveryTicketL> dto) {
		ResponseData responseData = new ResponseData();
		String addFlag = "N";
//		if(compareDate(dto.get(0).getShipDate(),dto.get(0).getExpectedDate()) != -1) {
//			throw new RuntimeException("ErrorMessageDate");
//		}

		if (dto.get(0).getExpectedDate().compareTo(dto.get(0).getShipDate()) != 1) {
			throw new RuntimeException("ErrorMessageDate");
		}
		// 头操作
		DeliveryTicketH head = new DeliveryTicketH();
		if (StringUtils.isNotBlank(dto.get(0).getTicketNumber())) {
			head.setTicketNumber(dto.get(0).getTicketNumber());
		} else {
			DeliveryTicketH ticketH = new DeliveryTicketH();
			ticketH.setSupplierId(dto.get(0).getSupplierId());
			String num = deliveryTicketHService.queryOrderNum(requestContext, ticketH);
			head.setTicketNumber(num);
		}
		List<DeliveryTicketH> headList = deliveryTicketHService.select(requestContext, head, 0, 0);
		// 送货单id
		float ticketId = -1;
		if (headList != null && headList.size() > 0) {
			ticketId = headList.get(0).getTicketId();
		}

		// 校验本次发运数量和可发运数量关系
		Map<String, List<DeliveryTicketL>> groupMap = dto.stream()
				.collect(Collectors.groupingBy(DeliveryTicketL::getGroupField));
		for (Map.Entry<String, List<DeliveryTicketL>> entry : groupMap.entrySet()) {
			Item item = new Item();
			// itemService
			item.setItemId(entry.getValue().get(0).getItemId());
			item.setPlantId(entry.getValue().get(0).getPlantId());
			List<Item> itemList = itemService.query(item);
			if (itemList != null && itemList.size() == 1) {
				if (itemList.get(0).getPackQty() == null || itemList.get(0).getPackQty() == 0) {
					continue;
				}
			} else {
				continue;
			}
			DeliveryTicketL ticketL = new DeliveryTicketL();
			if (ticketId != -1) {
				ticketL.setTicketId(ticketId);
			}
			ticketL.setPoHeaderId(entry.getValue().get(0).getPoHeaderId());
			ticketL.setPoLineId(entry.getValue().get(0).getPoLineId());
			ticketL.setLineLocationId(entry.getValue().get(0).getLineLocationId());

			float sum = mapper.querySum(ticketL).floatValue();//

			float sumShipQty = entry.getValue().stream()
					.collect(Collectors.summingDouble(DeliveryTicketL::getShipQuantity)).floatValue();
			float shippingAble = entry.getValue().get(0).getQuantity() - entry.getValue().get(0).getShipped();// 剩余需发运数量
			float num = sumShipQty - sum; // 新增的本次发运数量之和

			/**
			 * added by wtm 20190104 N= 剩余需发运数量/物料包装数 若N为整数 最大可发运数量=剩余需发运数量 若N为小数 N取整数部分
			 * 最大可发运数量= N+1 物料包装数 itemList.get(0).getPackQty() 物料包装数
			 */
			float yNum = shippingAble % itemList.get(0).getPackQty(); // 求余数 来确认是否可以整除
			float maxNum = 0;// 最大可发运数量
			if (yNum == 0) {// 可以整除
				maxNum = shippingAble;
			} else {// 不能整除
				maxNum = (float) ((Math.floor((shippingAble / itemList.get(0).getPackQty())) + 1)
						* itemList.get(0).getPackQty());// 最大可发运数量
			}
			if (num > maxNum) {
				throw new RuntimeException("ErrorMessage");
			}
		}

		head.setSupplierId(dto.get(0).getSupplierId());
		// head.setSupplierSiteId(dto.get(0).getSupplierSiteId());
		// head.setTicketType(dto.get(0).getTicketType());
		head.setPlantId(dto.get(0).getPlantId());
		head.setShipDate(dto.get(0).getShipDate());
		head.setExpectedDate(dto.get(0).getExpectedDate());
		head.setRemarks(dto.get(0).getRemarksH());
		head.setStatus("N");
		head.setPrintTime(0F);
		if (headList.size() > 0) {
			ticketId = headList.get(0).getTicketId();
			head.setTicketId(ticketId);
			// update
			head = deliveryTicketHService.updateByPrimaryKeySelective(requestContext, head);
		} else {
			addFlag = "Y";
			// insert
			head = deliveryTicketHService.insertSelective(requestContext, head);
			ticketId = head.getTicketId();
		}

		// 行操作
		for (DeliveryTicketL line : dto) {
			line.setTicketId(ticketId);
			DeliveryTicketL lineData = new DeliveryTicketL();
			lineData.setTicketId(ticketId);
			lineData.setTicketLineNum(line.getTicketLineNum());
			lineData = mapper.selectOne(lineData);
			if (lineData == null) {
				// insert
				self().insertSelective(requestContext, line);
			} else {
				// update
				self().updateByPrimaryKeySelective(requestContext, line);
			}
		}
		/**
		 * add by wtm 20200114: 此未取消的 line_status<> C 送货单行中不能存在相同 物料 工厂 包装方式 采购订单行id 版本
		 * 的 否则提示：error.srm_purchase_1133 不能存在订单行、版本、批次、包装方式相同的送货单行
		 */
		for (DeliveryTicketL line : dto) {
			if ("C".equals(line.getLineStatus())) {
				continue;
			}
			DeliveryTicketL search = new DeliveryTicketL();
			search.setTicketId(line.getTicketId());
			search.setLotsNum(line.getLotsNum());
			search.setItemId(line.getItemId());
			search.setPlantId(line.getPlantId());
			search.setPacketInfo(line.getPacketInfo());
			search.setPoLineId(line.getPoLineId());
			search.setItemVersion(line.getItemVersion());
			List<DeliveryTicketL> res = mapper.select(search);
			if (res != null && res.stream().filter(p -> !"C".equals(p.getLineStatus())).count() > 1) {
				throw new RuntimeException(SystemApiMethod.getPromptDescription(requestContext, iPromptService,
						"error.srm_purchase_1133"));
			}
		}
		StringBuilder totalMessage = new StringBuilder();
		if ("Y".equals(addFlag)) {
			String msg = SystemApiMethod.getPromptDescription(requestContext, iPromptService,
					"error.srm_purchase_1079");
			totalMessage.append(MessageFormat.format(msg, head.getTicketNumber()));
		} else {
			String msg = SystemApiMethod.getPromptDescription(requestContext, iPromptService, "hap.tip.success");
			totalMessage.append(MessageFormat.format(msg, head.getTicketNumber()));
		}

		List<DeliveryTicketL> list = new ArrayList();
		DeliveryTicketL line = new DeliveryTicketL();
		line.setTicketNumber(head.getTicketNumber());
		line.setTicketId(ticketId);
		line.setAddFlag(addFlag);
		list.add(line);
		responseData.setRows(list);
		responseData.setMessage(totalMessage.toString());

		/**
		 * 允差校验 added by wtm 20191223
		 */
		customCheck(requestContext, dto);

		/*
		 * dto.get(0).setTicketNumber(head.getTicketNumber());
		 * dto.get(0).setTicketId(ticketId);
		 */

		return responseData;
	}

	/**
	 * @description 允差校验
	 * @author tianmin.wang
	 * @date 2019年12月23日
	 * @param requestContext
	 * @param dto
	 */
	private void customCheck(IRequest requestContext, List<DeliveryTicketL> deliveryTicketLs) {

		for (DeliveryTicketL dto : deliveryTicketLs) {
			/**
			 * 该订单行其他送货单行的已发运数量 aValue = ship_quantity(line_status为 N、P、S
			 * 的)+receive_quantity(line_status为 Q、R 的)
			 */
			Float aValue = null;
			aValue = mapper.getShipedQuantity(dto);

			/**
			 *  该订单行数量(B)：根据HCS_PO_LINE_LOCATIONS 表po_line_id关联订单行表 HCS_PO_LINES ,取quantity
			 */
			Float bValue = null;
			bValue = poLineLocationsMapper.getSumQuantity(dto.getPoLineId());

			/**
			 *  该物料的允差上限(C)、下限(D)：根据物料id、工厂id、供应商id关联合格供应商表 HCS_ASL 取asl_id,再关联合格供应商拓展表
			 * HCS_ASL_CONTROL 取overdelivery_tolerance_limit 、underdelivery_tolerance_limit
			 * 若无值取0
			 */
			Float cValue = 0f;
			Float dValue = 0f;
			Asl aslSearch = new Asl();
			aslSearch.setItemId(dto.getItemId());
			aslSearch.setPlantId(dto.getPlantId());
			aslSearch.setSupplierId(dto.getSupplierId());
			List<Asl> aslResult = aslMapper.select(aslSearch);
			if (aslResult != null && aslResult.size() > 0) {
				AslControl aslControlSearch = new AslControl();
				aslControlSearch.setAslId(aslResult.get(0).getAslId());
				List<AslControl> acList = aslControlMapper.select(aslControlSearch);
				if (acList != null && acList.size() > 0) {
					cValue = acList.get(0).getOverdeliveryToleranceLimit() == null ? 0f
							: acList.get(0).getOverdeliveryToleranceLimit();
					dValue = acList.get(0).getUnderdeliveryToleranceLimit() == null ? 0f
							: acList.get(0).getUnderdeliveryToleranceLimit();
				}
			}
			/**
			 * E=（1+C）*B–A，向下取整,小于0时取0
			 */
			Double eValue = Math.floor((1 + cValue) * bValue - aValue);
			if (eValue < 0) {
				eValue = 0d;
			}
			/**
			 * 若 1+C <(A+本次发运数量) / B
			 * 则提示：error.srm_purchase_1131（此采购订单行最多发运{0}，如需多发，请联系采购员修改订单数量）;---{0}为E的值
			 */
			if (1 + cValue - (aValue + dto.getShipQuantity()) / bValue < 0) {
				throw new RuntimeException(MessageFormat.format(
						SystemApiMethod.getPromptDescription(requestContext, iPromptService, "error.srm_purchase_1131"),
						String.valueOf(eValue.intValue())));
			}
		}

	}

	@Override
	public List<DeliveryTicketL> queryByHeadId(IRequest requestContext, DeliveryTicketL dto) {
		DeliveryTicketL line = new DeliveryTicketL();
		line.setTicketId(dto.getTicketId());
		List<DeliveryTicketL> lineList = mapper.select(line);
		return lineList;
	}

	@Override
	public ResponseData cancelLine(IRequest requestContext, List<DeliveryTicketL> dto) {
		ResponseData responseData = new ResponseData();
		for (DeliveryTicketL line : dto) {
			DeliveryTicketL lineData = new DeliveryTicketL();
			lineData.setTicketLineId(line.getTicketLineId());
			lineData = mapper.selectByPrimaryKey(lineData);
			if (lineData != null && "N".equals(lineData.getLineStatus())) {
				lineData.setLineStatus("C");
				lineData.setCancelFlag("Y");
				lineData.setCancelDate(new Date());
				lineData.setCancelBy((float) requestContext.getUserId());
				lineData.setBarcodeQty(0f);
				self().updateByPrimaryKeySelective(requestContext, lineData);
			}

			// 解除送货单行和标签的绑定关系：
			BarcodeRelation barcodeRelation = new BarcodeRelation();
			barcodeRelation.setTicketLineId(line.getTicketLineId());
			barcodeRelation.setEnableFlag("Y");
			List<BarcodeRelation> barcodeRelationList = barcodeRelationService.select(requestContext, barcodeRelation,
					0, 0);
			if (barcodeRelationList != null && barcodeRelationList.size() > 0) {
				for (BarcodeRelation relation : barcodeRelationList) {
					// 更新里层容器状态
					updateOutBarcodeStatus(requestContext, relation.getInbarcodeType(), relation.getInbarcodeId());
					// 更新外层容器状态
					updateOutBarcodeStatus(requestContext, relation.getOutbarcodeType(), relation.getOutbarcodeId());

					// 清空送货单头行id
					barcodeRelationService.updateTicketId(relation);
					// 获取物料标签id
					List<Float> smallBarcodeIdList = barcodeRelationService
							.querySmallBarcodeId(relation.getOutbarcodeId(), relation.getOutbarcodeType());
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
			smallBarcodeControl.setTicketLineId(line.getTicketLineId());
			List<SmallBarcodeControl> smallBarcodeControlList = smallBarcodeControlService.select(requestContext,
					smallBarcodeControl, 0, 0);
			if (smallBarcodeControlList != null && smallBarcodeControlList.size() > 0) {
				for (SmallBarcodeControl control : smallBarcodeControlList) {
					// 更新物料标签状态为新建：N
					updateSmallBarcodeStatus(requestContext, control.getSbarcodeId(), "N");
				}
			}

			// 清空物料标签控制表的送货单头行id
			smallBarcodeControlService.updateTicketIdByLineId(smallBarcodeControl);

		}
		DeliveryTicketL deliveryTicketL = new DeliveryTicketL();
		deliveryTicketL.setTicketId(dto.get(0).getTicketId());
		List<DeliveryTicketL> deliveryTicketLList = mapper.select(deliveryTicketL);
		long count = deliveryTicketLList.stream().filter(data -> !"C".equals(data.getLineStatus())).count();
		if (count == 0) {
			DeliveryTicketH deliveryTicketH = new DeliveryTicketH();
			deliveryTicketH.setTicketId(dto.get(0).getTicketId());
			deliveryTicketH = deliveryTicketHService.selectByPrimaryKey(requestContext, deliveryTicketH);
			deliveryTicketH.setStatus("C");
			// 更新头状态
			deliveryTicketHService.updateByPrimaryKeySelective(requestContext, deliveryTicketH);
			dto.get(0).setHeadFlag("N");
		}
		responseData.setRows(dto);
		return responseData;
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
	 * 更新容器状态为 装载:ZZ
	 * 
	 * @param oBarcodeType 类型
	 * @param oBarcodeId   容器id
	 */
	private void updateOutBarcodeStatus(IRequest requestContext, String oBarcodeType, float oBarcodeId) {
		if ("TP".equals(oBarcodeType) || "GS".equals(oBarcodeType) || "ZZ".equals(oBarcodeType)
				|| "ZB".equals(oBarcodeType)) {
			OutBarcode oBarcode = new OutBarcode();
			oBarcode.setObarcodeId(oBarcodeId);
			oBarcode.setStatus("ZZ");
			outBarcodeService.updateByPrimaryKeySelective(requestContext, oBarcode);
		}
	}

	@Override
	public List<DeliveryTicketL> printQueryLine(IRequest requestContext, DeliveryTicketL dto) {
		return mapper.printQueryLine(dto);
	}

	@Override
	public Long queryReceiveQty(IRequest requestContext, DeliveryTicketL dto) {
		// TODO Auto-generated method stub
		return mapper.queryReceiveQty(dto);
	}

	@Override
	public List<DeliveryTicketL> queryHeadLine(IRequest requestContext, DeliveryTicketL dto, int page, int pageSize) {
		User user = new User();
		user.setUserId(requestContext.getUserId());
		user = userService.selectByPrimaryKey(requestContext, user);
		dto.setUserType(user.getUserType());
		if ("S".equals(user.getUserType())) {
			dto.setSupplierId((float) user.getSupplierId());
		}
		PageHelper.startPage(page, pageSize);
		return mapper.queryHeadLine(dto);
	}

	/**
	 * 比较日期大小 （年月日比较）
	 * 
	 * @param date        日期
	 * @param dateAnother 日期
	 * @return 1:date > dateAnother 2: date < dateAnother 0: date = dateAnother
	 */
	private int compareDate(Date date, Date dateAnother) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
		String dateFirst = dateFormat.format(date);
		String dateLast = dateFormat.format(dateAnother);
		int dateFirstIntVal = Integer.parseInt(dateFirst);
		int dateLastIntVal = Integer.parseInt(dateLast);
		if (dateFirstIntVal > dateLastIntVal) {
			return 1;
		} else if (dateFirstIntVal < dateLastIntVal) {
			return -1;
		}
		return 0;
	}

	@Override
	public ResponseData queryLocationCount(IRequest requestContext, DeliveryTicketL dto) {
		ResponseData responseData = new ResponseData();
		List<DeliveryTicketL> deliveryTicketLList = mapper.queryLocationCount(dto);
		if (deliveryTicketLList != null && deliveryTicketLList.size() > 0) {
			StringBuilder totalMessage = new StringBuilder();
			String msg = SystemApiMethod.getPromptDescription(requestContext, iPromptService,
					"error.srm_purchase_1062");
			totalMessage.append(MessageFormat.format(msg, deliveryTicketLList.get(0).getConcatStr()));
			responseData.setMessage(totalMessage.toString());
			responseData.setSuccess(false);
		}
		return responseData;
	}

	@Override
	public SoapPostUtil.Response transferDeliveryReceipt(com.hand.itf.itf_delivery_receipt.dto.DeliveryReceipt cr) {// 送货单收货wms接口传输数据处理
		SoapPostUtil.Response response = new SoapPostUtil.Response();
		Date now = new Date();
		cr.setProcessStatus("Y");
		cr.setProcessTime(now);
		DeliveryTicketH dths = new DeliveryTicketH();
		dths.setTicketNumber(cr.getDeliveryOrderNo());
		List<DeliveryTicketH> dthr = deliveryTicketHMapper.select(dths);
		try {

			if (dthr != null && dthr.size() > 0) {
				DeliveryTicketL dtls = new DeliveryTicketL();
				dtls.setTicketId(dthr.get(0).getTicketId());
				dtls.setTicketLineNum(Float.valueOf(cr.getDeliveryLineNo()));
				List<DeliveryTicketL> dtlr = mapper.select(dtls);
				if (dtlr != null && dtlr.size() > 0) {
					DeliveryTicketL dtlu = new DeliveryTicketL();
					dtlu.setTicketLineId(dtlr.get(0).getTicketLineId());
					dtlu.setLastUpdatedBy(-2l);
					dtlu.setLastUpdateDate(now);

					if ("Y".equals(cr.getInspectOk())) {
						dtlu.setLineStatus("Q");
						dtlu.setDeliverdQuantity(
								cr.getQualifiedQty() == null ? null : Float.valueOf(cr.getQualifiedQty()));
						dtlu.setQuarantineFlag("Y");
						// 更新
						mapper.updateByPrimaryKeySelective(dtlu);
					} else if ("N".equals(cr.getInspectOk())) {
						SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
						dtlu.setReceiveQuantity(Float.valueOf(cr.getReceivingQty()));
						dtlu.setQuarantineQty(Float.valueOf(cr.getInspectQty()));
						dtlu.setQuarantineFlag("N");
						dtlu.setLineStatus("R");
						try {
							dtlu.setReceiveDate(sdf.parse(cr.getReceivingDate()));
						} catch (ParseException e) {
//						throw new RuntimeException("时间转换失败[" + cr.getReceivingDate() + "]");
							response.setResult(false);
							response.setMessage("时间转换失败[" + cr.getReceivingDate() + "]");
							cr.setProcessStatus("E");
							cr.setMessage("时间转换失败[" + cr.getReceivingDate() + "]");
						}
						// 更新
						mapper.updateByPrimaryKeySelective(dtlu);
					}

					/**
					 * 是否关闭此送货单对应的采购订单行
					 */
					poClose(dtlr.get(0));
					/**
					 * 是否关闭此送货单对应的发运计划行line_location_id HCS_PO_LINE_LOCATIONS
					 */
					poLocationsClose(dtlr.get(0));

				} else {
					cr.setMessage("送货单行号[" + cr.getDeliveryLineNo() + "]不存在");
				}

				/**
				 * 对送货单号依据所有行状态进行一次状态修改 若该送货单的所有送货单行状态存在不为C-已取消,R-已接收,Q-已检验的数据,送货单状态更新为PR-部分接收
				 * 否则,送货单状态更新为R-已接收
				 */
				if (deliveryTicketHMapper.countCRQSelect(dthr.get(0).getTicketId()) > 0) {
					dthr.get(0).setStatus("PR");
					dthr.get(0).setLastUpdateDate(now);
					deliveryTicketHMapper.updateByPrimaryKeySelective(dthr.get(0));
				} else {
					dthr.get(0).setStatus("R");
					dthr.get(0).setLastUpdateDate(now);
					deliveryTicketHMapper.updateByPrimaryKeySelective(dthr.get(0));
				}

			} else {
				cr.setMessage("送货单号[" + cr.getDeliveryOrderNo() + "]不存在");
			}
		} catch (Exception e) {
			response.setResult(false);
			response.setMessage(e.getMessage());
			cr.setProcessStatus("E");
			cr.setMessage(e.getMessage());
		}
		deliveryReceiptMapper.updateByPrimaryKeySelective(cr);
		return response;
	}

	/**
	 * @description 接口 发运计划行关闭
	 * @author tianmin.wang
	 * @date 2019年12月23日
	 * @param deliveryTicketL
	 */
	private void poLocationsClose(DeliveryTicketL deliveryTicketL) {
		/**
		 *   根据送货单行表 HCS_DELIVERY_TICKET_L 的line_location_id
		 * 查询送货单行表中此line_location_id的所有已接收、已检验LINE_STATUS为R、Q送货单行的实收数量receive_quantity求和得a
		 */
		Float aValue = mapper.getSumReceiveQuantityByLineLocationId(deliveryTicketL.getLineLocationId());

		/**
		 *  再根据送货单行表 HCS_DELIVERY_TICKET_L 的line_location_id，关联采购订单行发运表
		 * HCS_PO_LINE_LOCATIONS 的数量 quantity 得b
		 */
		Float bValue = 0f;
		PoLineLocations locSearch = new PoLineLocations();
		locSearch.setLineLocationId(deliveryTicketL.getLineLocationId());
		List<PoLineLocations> locResult = poLineLocationsMapper.select(locSearch);
		if (locResult != null && locResult.size() > 0) {
			bValue = locResult.get(0).getQuantity();
		} else {
			return;
		}
		if (aValue - bValue >= 0) {
			PoLineLocations update = new PoLineLocations();
			update.setShipmentStatus("C");
			update.setLineLocationId(deliveryTicketL.getLineLocationId());
			update.setLastUpdateDate(new Date());
			poLineLocationsMapper.updateByPrimaryKeySelective(update);
		}

	}

	/**
	 * @description 判断是否关闭POl 并关闭
	 * @author tianmin.wang
	 * @date 2019年12月23日
	 * @param deliveryTicketL
	 */
	private void poClose(DeliveryTicketL deliveryTicketL) {
		/**
		 *  根据送货单行表HCS_DELIVERY_TICKET_L的po_line_id
		 * 查询送货单行表中此po_line_id的所有已接收、已检验LINE_STATUS为R、Q送货单行的实收数量receive_quantity求和得A
		 */
		Float aValue = mapper.getSumReceiveQuantityByPoLineId(deliveryTicketL.getPoLineId());
		/**
		 * 再根据送货单行表 HCS_DELIVERY_TICKET_L 的po_line_id，关联采购订单行表订单数量 quantity 得B
		 */
		Float bValue = 0f;
		PoLines polSearch = new PoLines();
		polSearch.setPoLineId(deliveryTicketL.getPoLineId());
		List<PoLines> polListResult = poLinesMapper.select(polSearch);
		if (polListResult != null && polListResult.size() > 0) {
			bValue = polListResult.get(0).getQuantity();
		} else {
			return;
		}
		/**
		 *  再根据送货单行表的物料id、送货单头表的工厂id、供应商id关联合格供应商表 HCS_ASL 取asl_id 再关联合格供应商拓展表
		 * HCS_ASL_CONTROL 取underdelivery_tolerance_limit，若此值为空 取0
		 */
		Float cValue = mapper.getUnderdeliveryToleranceLimit(deliveryTicketL.getTicketLineId());

		/**
		 *  判断：若(1-underdelivery_tolerance_limit）<=（A/B）时，将采购订单行状态更新为C-已关闭
		 */
		if (1 - cValue - aValue / bValue <= 0) {

			PoLines uppo = new PoLines();
			uppo.setLineStatus("C");
			uppo.setPoLineId(deliveryTicketL.getPoLineId());
			uppo.setLastUpdateDate(new Date());
			poLinesMapper.updateByPrimaryKeySelective(uppo);
		}

		/**
		 * 若该采购订单下的采购订单行全部为 C 或者 R ，将采购订单头状态更新为 C -已关闭
		 */
		PoLines polSearch1 = new PoLines();
		polSearch1.setPoHeaderId(polListResult.get(0).getPoHeaderId());
		List<PoLines> polListResult1 = poLinesMapper.select(polSearch1);
		if (polListResult1.stream().filter(p -> {
			return !"C".equals(p.getLineStatus()) && !"R".equals(p.getLineStatus());
		}).count() == 0) {
			PoHeaders poh = new PoHeaders();
			poh.setPoHeaderId(polListResult.get(0).getPoHeaderId());
			poh.setPoStatus("C");
			poHeadersMapper.updateByPrimaryKeySelective(poh);
		}

	}

	@Override
	public List<TicketReport> planReport(IRequest requestContext, DeliveryTicketL dto) throws Exception {
		// 报表数据
		List<TicketReport> ticketReportList = new ArrayList();
		// 获取数据源
		List<DeliveryTicketL> deliveryTicketLList = mapper.queryReport(dto);
		// 获取快码信息
		List<CodeValue> codeValueList = getCode("HCS_SUPPLY_STATISTICS_FACTOR");

		// 按供应商分组
		Map<Float, List<DeliveryTicketL>> ticketLMap = deliveryTicketLList.stream()
				.collect(Collectors.groupingBy(DeliveryTicketL::getSupplierId));
		for (Map.Entry<Float, List<DeliveryTicketL>> enty : ticketLMap.entrySet()) {
			List<DeliveryTicketL> ticketLList = enty.getValue();
			if (ticketLList != null && ticketLList.size() > 0) {
				// 合格总数
				long qualifiedCount = 0;
				// 分数
				long molecular = 0;
				// column1数量
				long column1Count = 0;
				// column2数量
				long column2Count = 0;
				// column3数量
				long column3Count = 0;
				// column4数量
				long column4Count = 0;
				// column5数量
				long column5Count = 0;

				// 获取列值
				for (DeliveryTicketL ticketL : ticketLList) {
					String column = selectColumn(codeValueList, ticketL, dto.getStatisticsType());
					if (COLUMN_1.equals(column)) {
						column1Count++;
					} else if (COLUMN_2.equals(column)) {
						column2Count++;
					} else if (COLUMN_3.equals(column)) {
						column3Count++;
					} else if (COLUMN_4.equals(column)) {
						column4Count++;
					} else if (COLUMN_5.equals(column)) {
						column5Count++;
					}
				}
				// 计算合格总数
				for (CodeValue codeValue : codeValueList) {
					if (StringUtils.isNotBlank(codeValue.getTag()) && "OK".equals(codeValue.getTag())) {
						if ((long) codeValue.getOrderSeq() == 1l) {
							qualifiedCount += column1Count;
						} else if ((long) codeValue.getOrderSeq() == 2l) {
							qualifiedCount += column2Count;
						} else if ((long) codeValue.getOrderSeq() == 3l) {
							qualifiedCount += column3Count;
						} else if ((long) codeValue.getOrderSeq() == 4l) {
							qualifiedCount += column4Count;
						} else if ((long) codeValue.getOrderSeq() == 5l) {
							qualifiedCount += column5Count;
						}
					}
				}
				// 分数
				molecular = qualifiedCount / ticketLList.size();

				TicketReport ticketReport = new TicketReport();
				ticketReport.setSupplierCode(ticketLList.get(0).getSupplierCode());
				ticketReport.setSupplierName(ticketLList.get(0).getSupplierName());
				ticketReport.setCount((long) ticketLList.size());
				ticketReport.setColumn1(column1Count);
				ticketReport.setColumn2(column2Count);
				ticketReport.setColumn3(column3Count);
				ticketReport.setColumn4(column4Count);
				ticketReport.setColumn5(column5Count);
				ticketReport.setQualifiedCount(qualifiedCount);
				ticketReport.setMolecular((float) Math.round(molecular * 100) / 100);

				ticketReportList.add(ticketReport);
			}
		}
		return ticketReportList;
	}

	/**
	 * 获取快码内容
	 * 
	 * @param fastCode 快码
	 * @return 快码内容
	 */
	private List<CodeValue> getCode(String fastCode) {
		List<CodeValue> codeValueList = new ArrayList();
		Code code = codeMapper.getByCodeName(fastCode);
		if (code != null) {
			CodeValue codeValue = new CodeValue();
			codeValue.setCodeId(code.getCodeId());
			codeValueList = codeValueMapper.selectCodeValuesByCodeId(codeValue);
		} else {
			throw new RuntimeException("fastCode：" + fastCode + " is not exist");
		}
		return codeValueList;
	}

	/**
	 * 判断送货单行属于那一列
	 * 
	 * @param codeValueList 快码内容
	 * @param ticketL       送货单行
	 * @param type          类型
	 * @return 列名
	 * @throws ParseException
	 */
	private String selectColumn(List<CodeValue> codeValueList, DeliveryTicketL ticketL, String type)
			throws ParseException {
		// 列名
		String columnNum = "column";
		int dayBetween = 0;
		// 获取需求时间
		if ("PO".equals(type)) {
			dayBetween = daysBetween(ticketL.getPoDate(), ticketL.getReceiveDate());
		} else if ("SP".equals(type)) {
			dayBetween = daysBetween(ticketL.getSpDate(), ticketL.getReceiveDate());
		} else {
			return "-1";
		}
		if (codeValueList != null && codeValueList.size() > 0) {
			for (CodeValue codeValue : codeValueList) {
				String[] strArr = codeValue.getMeaning().split("_");
				// 只有左边
				if (strArr.length == 1) {
					if (Integer.parseInt(strArr[0]) <= dayBetween) {
						return columnNum + codeValue.getOrderSeq();
					}
				} else if (strArr.length == 2) {
					// 判断左边是否为空
					if (StringUtils.isNotBlank(strArr[0])) {
						// 左边不为空
						if (Integer.parseInt(strArr[0]) <= dayBetween && Integer.parseInt(strArr[1]) >= dayBetween) {
							return columnNum + codeValue.getOrderSeq();
						}
					} else {
						// 左边为空
						if (Integer.parseInt(strArr[1]) >= dayBetween) {
							return columnNum + codeValue.getOrderSeq();
						}
					}
				} else {
					return "-1";
				}
			}
		}
		return "-1";
	}

	/**
	 * 计算两个日期之间相差的天数
	 * 
	 * @param smdate 较小的时间
	 * @param bdate  较大的时间
	 * @return 相差天数
	 * @throws ParseException
	 */
	private int daysBetween(Date smdate, Date bdate) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		smdate = sdf.parse(sdf.format(smdate));
		bdate = sdf.parse(sdf.format(bdate));
		Calendar cal = Calendar.getInstance();
		cal.setTime(smdate);
		long time1 = cal.getTimeInMillis();
		cal.setTime(bdate);
		long time2 = cal.getTimeInMillis();
		long between_days = (time2 - time1) / (1000 * 3600 * 24);

		return Integer.parseInt(String.valueOf(between_days));
	}
}