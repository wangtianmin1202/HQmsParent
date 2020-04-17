package com.hand.hcs.hcs_delivery_ticket.service.impl;

import com.github.pagehelper.PageHelper;
import com.hand.hap.account.dto.User;
import com.hand.hap.account.service.IUserService;
import com.hand.hap.core.IRequest;
import com.hand.hap.core.impl.ServiceRequest;
import com.hand.hap.intergration.service.IHapApiService;
import com.hand.hap.system.dto.CodeValue;
import com.hand.hap.system.dto.ResponseData;
import com.hand.hap.system.service.ICodeService;
import com.hand.hap.system.service.IPromptService;
import com.hand.hap.system.service.impl.BaseServiceImpl;
import com.hand.hap.util.SoapPostUtil;
import com.hand.hap.util.SystemApiMethod;

import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.Resource;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Service;
import com.hand.hcs.hcs_barcode.dto.SmallBarcode;
import com.hand.hcs.hcs_barcode.dto.SmallBarcodeControl;
import com.hand.hcs.hcs_barcode.service.ISmallBarcodeControlService;
import com.hand.hcs.hcs_barcode.service.ISmallBarcodeService;
import com.hand.hcs.hcs_barcode_relation.dto.BarcodeRelation;
import com.hand.hcs.hcs_barcode_relation.mapper.BarcodeRelationMapper;
import com.hand.hcs.hcs_barcode_relation.service.IBarcodeRelationService;
import com.hand.hcs.hcs_delivery_ticket.dto.DeliveryTicketH;
import com.hand.hcs.hcs_delivery_ticket.dto.DeliveryTicketL;
import com.hand.hcs.hcs_delivery_ticket.idto.BarCode;
import com.hand.hcs.hcs_delivery_ticket.idto.ShipHeader;
import com.hand.hcs.hcs_delivery_ticket.idto.ShipLine;
import com.hand.hcs.hcs_delivery_ticket.mapper.DeliveryTicketHMapper;
import com.hand.hcs.hcs_delivery_ticket.mapper.DeliveryTicketLMapper;
import com.hand.hcs.hcs_delivery_ticket.service.IDeliveryTicketHService;
import com.hand.hcs.hcs_delivery_ticket.service.IDeliveryTicketLService;
import com.hand.hcs.hcs_out_barcode.dto.OutBarcode;
import com.hand.hcs.hcs_out_barcode.service.IOutBarcodeService;
import com.hand.hcs.hcs_suppliers.dto.Suppliers;
import com.hand.hcs.hcs_suppliers.mapper.SuppliersMapper;
import com.hand.hcs.hcs_suppliers.service.ISuppliersService;
import com.hand.sys.sys_if_invoke_outbound.dto.IfInvokeOutbound;
import com.hand.sys.sys_if_invoke_outbound.mapper.IfInvokeOutboundMapper;

import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(rollbackFor = Exception.class)
public class DeliveryTicketHServiceImpl extends BaseServiceImpl<DeliveryTicketH> implements IDeliveryTicketHService {

	@Autowired
	private DeliveryTicketHMapper deliveryTicketHMapper;

	@Autowired
	private DeliveryTicketLMapper deliveryTicketLMapper;

	@Autowired
	private BarcodeRelationMapper barcodeRelationMapper;

	@Autowired
	private ISuppliersService suppliersService;
	@Autowired
	private IDeliveryTicketLService deliveryTicketLService;
	@Autowired
	private IUserService userService;
	@Autowired
	private IBarcodeRelationService barcodeRelationService;
	@Autowired
	private IOutBarcodeService outBarcodeService;
	@Autowired
	private ISmallBarcodeControlService smallBarcodeControlService;
	@Autowired
	private ISmallBarcodeService smallBarcodeService;
	@Autowired
	private IPromptService iPromptService;

	@Resource(name = "taskExecutor")
	private TaskExecutor taskExecutor;
	@Autowired
	ICodeService iCodeService;

	@Autowired
	SuppliersMapper suppliersMapper;

	private Logger logger = LoggerFactory.getLogger(DeliveryTicketHServiceImpl.class);

	@Override
	public String queryOrderNum(IRequest requestContext, DeliveryTicketH deliveryTicketH) {
		int num = deliveryTicketHMapper.queryMaxNum(deliveryTicketH);
		num = num + 1;
		// 获取供应商编码
		Suppliers suppliers = new Suppliers();
		suppliers.setSupplierId(deliveryTicketH.getSupplierId());
		suppliers = suppliersService.selectByPrimaryKey(requestContext, suppliers);
		// 序列号
		String numStr = String.format("%03d", num);
		// 年月日：yymmdd
		SimpleDateFormat simple = new SimpleDateFormat("yyMMdd");
		String now = simple.format(new Date());

		String orderNum = suppliers.getSupplierCode() + "-" + now + "-" + numStr;

		return orderNum;
	}

	@Override
	public List<DeliveryTicketH> query(IRequest requestContext, DeliveryTicketH deliveryTicketH, int page,
			int pageSize) {
		PageHelper.startPage(page, pageSize);
		return deliveryTicketHMapper.query(deliveryTicketH);
	}

	@Override
	public List<DeliveryTicketH> queryData(IRequest requestContext, DeliveryTicketH deliveryTicketH, int page,
			int pageSize) {

		User user = new User();
		user.setUserId(requestContext.getUserId());
		user = userService.selectByPrimaryKey(requestContext, user);
		deliveryTicketH.setUserType(user.getUserType());
		if ("S".equals(user.getUserType())) {
			deliveryTicketH.setSupplierId((float) user.getSupplierId());
		}
		PageHelper.startPage(page, pageSize);
		return deliveryTicketHMapper.queryData(deliveryTicketH);
	}

	@Override
	public ResponseData delivery(IRequest requestContext, DeliveryTicketH deliveryTicketH) {
		ResponseData responseData = new ResponseData();
		deliveryTicketH.setStatus("S");
		deliveryTicketH = self().updateByPrimaryKeySelective(requestContext, deliveryTicketH);
		DeliveryTicketL line = new DeliveryTicketL();
		line.setTicketId(deliveryTicketH.getTicketId());
		List<DeliveryTicketL> lineList = deliveryTicketLService.select(requestContext, line, 0, 0);
		for (DeliveryTicketL lineDate : lineList) {
			if ("N".equals(lineDate.getLineStatus())) {
				lineDate.setLineStatus("S");
				deliveryTicketLService.updateByPrimaryKeySelective(requestContext, lineDate);
			}
		}
		// 将包装信息更新为已发货状态(OutBarcode)
		BarcodeRelation barcodeRelation = new BarcodeRelation();
		barcodeRelation.setTicketId(deliveryTicketH.getTicketId());
		barcodeRelation.setEnableFlag("Y");
		List<BarcodeRelation> barcodeRelationList = barcodeRelationService.select(requestContext, barcodeRelation, 0,
				0);
		if (barcodeRelationList != null && barcodeRelationList.size() > 0) {
			for (BarcodeRelation rel : barcodeRelationList) {
				OutBarcode outBarcode = new OutBarcode();
				outBarcode.setObarcodeId(rel.getOutbarcodeId());
				outBarcode = outBarcodeService.selectByPrimaryKey(requestContext, outBarcode);
				if (outBarcode != null) {
					outBarcode.setStatus("FH");
					// update
					outBarcodeService.updateByPrimaryKeySelective(requestContext, outBarcode);
				}
				// 找里层标签
				BarcodeRelation barcodeRel = new BarcodeRelation();
				barcodeRel.setOutbarcodeId(rel.getInbarcodeId());
				barcodeRel.setEnableFlag("Y");
				List<BarcodeRelation> barcodeRelList = barcodeRelationService.select(requestContext, barcodeRel, 0, 0);
				if (barcodeRelList != null && barcodeRelList.size() > 0) {
					for (BarcodeRelation relation : barcodeRelList) {
						OutBarcode oBarcode = new OutBarcode();
						oBarcode.setObarcodeId(relation.getOutbarcodeId());
						oBarcode = outBarcodeService.selectByPrimaryKey(requestContext, oBarcode);
						if (oBarcode != null) {
							oBarcode.setStatus("FH");
							// update
							outBarcodeService.updateByPrimaryKeySelective(requestContext, oBarcode);
						}
					}
				}
			}
		}
		// 将包装信息更新为已发货状态(SmallBarcode)
		SmallBarcodeControl smallBarcodeControl = new SmallBarcodeControl();
		smallBarcodeControl.setTicketId(deliveryTicketH.getTicketId());
		List<SmallBarcodeControl> smallBarcodeControlList = smallBarcodeControlService.select(requestContext,
				smallBarcodeControl, 0, 0);
		if (smallBarcodeControlList != null && smallBarcodeControlList.size() > 0) {
			for (SmallBarcodeControl codeControl : smallBarcodeControlList) {
				SmallBarcode smallBarcode = new SmallBarcode();
				smallBarcode.setSbarcodeId(codeControl.getSbarcodeId());
				smallBarcode = smallBarcodeService.selectByPrimaryKey(requestContext, smallBarcode);
				if (smallBarcode != null) {
					smallBarcode.setStatus("F");
					// update
					smallBarcodeService.updateByPrimaryKeySelective(requestContext, smallBarcode);
				}
			}
		}

		/**
		 * 20191106 add by tianmin.wang: 发货结束后 将相关数据发送给第三方mes
		 */
		infoToMes(deliveryTicketH);

		responseData.setMessage("发货成功");
		return responseData;
	}

	@Autowired
	IfInvokeOutboundMapper ifInvokeOutboundMapper;

	/**
	 * 
	 * @description 将相关数据发送给第三方wms
	 * @author tianmin.wang
	 * @date 2019年11月19日
	 * @param deliveryTicketH
	 */
	public void infoToMes(DeliveryTicketH deliveryTicketH) {
		try {
			IfInvokeOutbound iio = new IfInvokeOutbound();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			ShipHeader shipHeader = new ShipHeader();
			DeliveryTicketH resH = deliveryTicketHMapper.queryByTicketId(deliveryTicketH).get(0);
			shipHeader.setTicketNumber(resH.getTicketNumber());
			shipHeader.setSupplierCode(resH.getSupplierCode());
			shipHeader.setSynDate(sdf.format(new Date()));
			// 构建lines数据
			List<ShipLine> shipLines = new ArrayList<ShipLine>();
			DeliveryTicketL searL = new DeliveryTicketL();
			searL.setTicketId(deliveryTicketH.getTicketId());
			List<DeliveryTicketL> resL = deliveryTicketLMapper.interfaceSelect(searL);
			for (DeliveryTicketL deliveryTicketL : resL) {
				ShipLine shipLine = new ShipLine();
				shipLine.setTicketLineNum(String.valueOf(deliveryTicketL.getTicketLineNum().intValue()));
				shipLine.setItemCode(deliveryTicketL.getItemCode());
				shipLine.setItemVersion(deliveryTicketL.getItemVersion());
				shipLine.setPackingSize(deliveryTicketL.getPackingSize());
				shipLine.setPacketInfo(deliveryTicketL.getPacketInfo());
				shipLine.setProductionBatch(deliveryTicketL.getLotsNum());
				shipLine.setSpreading(deliveryTicketL.getSpreading());
				shipLine.setShipQuantity(String.valueOf(deliveryTicketL.getShipQuantity().intValue()));
				shipLine.setUomCode(deliveryTicketL.getUomCode());
				shipLine.setRemarks(deliveryTicketL.getRemarks());
				shipLine.setPoNumber(deliveryTicketL.getPoNumber());
				shipLine.setPoLineNum(deliveryTicketL.getLineNum());
				// 构建barcode数据
				List<BarCode> barCodes = new ArrayList<BarCode>();
				BarcodeRelation searB = new BarcodeRelation();
				searB.setTicketLineId(deliveryTicketL.getTicketLineId());
				List<BarcodeRelation> resB = barcodeRelationMapper.interfaceSelect(searB);
				for (BarcodeRelation barcodeRelation : resB) {
					BarCode barCode = new BarCode();
					barCode.setOutBarCodeContent(barcodeRelation.getOutbarcodeContent());
					barCode.setOutBarCodeType(barcodeRelation.getOutbarcodeType());
					barCode.setInBarCodeContent(barcodeRelation.getInbarcodeContent());
					barCode.setInBarCodeType(barcodeRelation.getInbarcodeType());
					barCodes.add(barCode);
				}
				shipLine.setBarCodes(barCodes);
				shipLines.add(shipLine);
			}
			shipHeader.setShipLines(shipLines);
			taskExecutor.execute(new Runnable() {
				@Override
				public void run() {
					// 转为JSON String发送
					ObjectMapper omapper = new ObjectMapper();
					String post;
					try {
						ServiceRequest sr = new ServiceRequest();
						sr.setLocale("zh_CN");
						String uri = iCodeService.getCodeValueByMeaning(sr, "HAP.SYSTEM", "WMS_WS_URI");// 获取调用地址
						post = omapper.writeValueAsString(shipHeader);
						SoapPostUtil.Response re = SoapPostUtil.ticketSrmToWms(post, iio, uri);
						ifInvokeOutboundMapper.insertSelective(iio);
						logger.info(SoapPostUtil.getStringFromResponse(re));
					} catch (Exception e) {
						iio.setResponseContent(e.getMessage());
						iio.setResponseCode("E");
						ifInvokeOutboundMapper.insertSelective(iio);
						logger.warn(e.getMessage());
					}
				}
			});
		} catch (Exception e) {
			logger.warn("发送第三方发生错误:" + e.getMessage());
		}
	}

	@Override
	public ResponseData cancel(IRequest requestContext, DeliveryTicketH deliveryTicketH) {
		ResponseData responseData = new ResponseData();
		deliveryTicketH.setStatus("C");
		// update head
		self().updateByPrimaryKeySelective(requestContext, deliveryTicketH);
		DeliveryTicketL line = new DeliveryTicketL();
		line.setTicketId(deliveryTicketH.getTicketId());
		List<DeliveryTicketL> lineList = deliveryTicketLService.select(requestContext, line, 0, 0);
		// update line
		for (DeliveryTicketL deliveryTicketL : lineList) {
			if (deliveryTicketH.getCancleFlag() != null && "Q".equals(deliveryTicketH.getCancleFlag())
					&& !"C".equals(deliveryTicketL.getLineStatus())) {
				deliveryTicketL.setLineStatus("C");
				deliveryTicketL.setCancelFlag("Y");
				deliveryTicketL.setCancelDate(new Date());
				deliveryTicketL.setBarcodeQty(0f);
				deliveryTicketL.setCancelBy((float) requestContext.getUserId());

				deliveryTicketLService.updateByPrimaryKeySelective(requestContext, deliveryTicketL);
			} else if ("N".equals(deliveryTicketL.getLineStatus())) {
				deliveryTicketL.setLineStatus("C");
				deliveryTicketL.setCancelFlag("Y");
				deliveryTicketL.setCancelDate(new Date());
				deliveryTicketL.setBarcodeQty(0f);
				deliveryTicketL.setCancelBy((float) requestContext.getUserId());

				deliveryTicketLService.updateByPrimaryKeySelective(requestContext, deliveryTicketL);
			}

			// 解除送货单行和标签的绑定关系：
			BarcodeRelation barcodeRelation = new BarcodeRelation();
			barcodeRelation.setTicketLineId(deliveryTicketL.getTicketLineId());
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
					// 更新状态
					OutBarcode obarcode = new OutBarcode();
					obarcode.setObarcodeId(relation.getOutbarcodeId());
					obarcode = outBarcodeService.selectByPrimaryKey(requestContext, obarcode);
					if (obarcode != null && !"ZZ".equals(obarcode.getStatus())) {
						obarcode.setStatus("ZZ");
						outBarcodeService.updateByPrimaryKeySelective(requestContext, obarcode);
					}
					// 获取物料标签id
					List<Float> smallBarcodeIdList = barcodeRelationService
							.querySmallBarcodeId(relation.getOutbarcodeId(), relation.getOutbarcodeType());
					if (smallBarcodeIdList != null && smallBarcodeIdList.size() > 0) {
						for (float smallBarcodeId : smallBarcodeIdList) {
							// 更新物料标签状态为新建：N
							updateSmallBarcodeStatus(requestContext, smallBarcodeId, "N");

							SmallBarcodeControl smallBarcodeControl = new SmallBarcodeControl();
							smallBarcodeControl.setSbarcodeId(smallBarcodeId);
							// 清空物料标签控制表的送货单头行id
							smallBarcodeControlService.updateTicketIdBySbarcodeId(smallBarcodeControl);
						}
					}
				}
			}
			SmallBarcodeControl smallBarcodeControl = new SmallBarcodeControl();
			smallBarcodeControl.setTicketLineId(deliveryTicketL.getTicketLineId());
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
		}
		responseData.setMessage("取消送货单成功");
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
	public List<DeliveryTicketH> printQuery(IRequest requestContext, DeliveryTicketH deliveryTicketH) {
		// TODO Auto-generated method stub
		return deliveryTicketHMapper.printQuery(deliveryTicketH);
	}

	@Override
	public List<DeliveryTicketH> queryByTicketId(IRequest requestContext, DeliveryTicketH deliveryTicketH) {
		// TODO Auto-generated method stub
		return deliveryTicketHMapper.queryByTicketId(deliveryTicketH);
	}

	@Override
	public ResponseData updatePrintTime(IRequest requestContext, DeliveryTicketH deliveryTicketH) {
		ResponseData responseData = new ResponseData();
		if (deliveryTicketH.getQueryFlag() == null) {
			deliveryTicketH.setStatus("P");
		}
		deliveryTicketH.setPrintTime(deliveryTicketH.getPrintTime() + 1);

		self().updateByPrimaryKeySelective(requestContext, deliveryTicketH);
		return responseData;
	}

	@Override
	public ResponseData checkPrint(IRequest requestContext, DeliveryTicketH deliveryTicketH) {
		ResponseData responseData = new ResponseData();
		/**
		 * add by wtm 20200114 新增校验 如果满足条件就直接跳过校验
		 */
		boolean jump = customerCheck(requestContext, deliveryTicketH);
		DeliveryTicketL deliveryTicketL = new DeliveryTicketL();
		deliveryTicketL.setTicketId(deliveryTicketH.getTicketId());
		deliveryTicketL.setLineStatus("N");
		List<DeliveryTicketL> deliveryTicketLList = deliveryTicketLService.select(requestContext, deliveryTicketL, 0,
				0);
		if (deliveryTicketLList != null && deliveryTicketLList.size() > 0) {
			for (DeliveryTicketL ticketL : deliveryTicketLList) {
				StringBuilder totalMessage = new StringBuilder();
				if (!jump && (ticketL.getShipQuantity() == null || ticketL.getBarcodeQty() == null)) {
					String msg = SystemApiMethod.getPromptDescription(requestContext, iPromptService,
							"error.srm_purchase_1056");
					totalMessage.append(MessageFormat.format(msg, ticketL.getTicketLineNum()));
					responseData.setMessage(totalMessage.toString());
					responseData.setSuccess(false);
					return responseData;
				} else if (!jump && ((float) ticketL.getShipQuantity() != (float) ticketL.getBarcodeQty())) {
					String msg = SystemApiMethod.getPromptDescription(requestContext, iPromptService,
							"error.srm_purchase_1058");
					totalMessage.append(MessageFormat.format(msg, ticketL.getTicketLineNum()));
					responseData.setMessage(totalMessage.toString());
					responseData.setSuccess(false);
					return responseData;
				} else if ((ticketL.getItemVersion() == null || ticketL.getLotsNum() == null
						|| ticketL.getPacketInfo() == null || ticketL.getShipQuantity() == null)
						&& !"C".equals(ticketL.getLineStatus())) {
					String msg = SystemApiMethod.getPromptDescription(requestContext, iPromptService,
							"error.srm_purchase_11001");
					totalMessage.append(MessageFormat.format(msg, ticketL.getTicketLineNum()));
					responseData.setMessage(totalMessage.toString());
					responseData.setSuccess(false);
				}
			}
		}
		return responseData;
	}

	/**
	 * @description 供应商编码是否存在于某快码
	 * @author tianmin.wang
	 * @param deliveryTicketH
	 * @date 2020年1月14日
	 * @return
	 */
	private boolean customerCheck(IRequest requestContext, DeliveryTicketH deliveryTicketH) {
		deliveryTicketH = mapper.selectByPrimaryKey(deliveryTicketH.getTicketId());
		Suppliers suSearch = new Suppliers();
		suSearch.setSupplierId(deliveryTicketH.getSupplierId());
		Suppliers res = suppliersMapper.selectByPrimaryKey(suSearch);
		List<CodeValue> cvs = iCodeService.getCodeValuesByCode(requestContext, "SRM_SUPPLIER_DLY_BARCODE");
		Long count = cvs.stream().filter(p -> p.getValue().equals(res.getSupplierCode())).count();
		if (count > 0) {
			return true;
		} else {
			return false;
		}
	}

}