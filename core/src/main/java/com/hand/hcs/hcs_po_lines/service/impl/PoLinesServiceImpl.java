package com.hand.hcs.hcs_po_lines.service.impl;

import com.github.pagehelper.PageHelper;
import com.hand.hap.core.IRequest;
import com.hand.hap.core.impl.ServiceRequest;
import com.hand.hap.mybatis.util.StringUtil;
import com.hand.hap.system.service.ICodeService;
import com.hand.hap.system.service.impl.BaseServiceImpl;
import com.hand.hap.util.SoapPostUtil.Response;
import com.hand.hap.util.SoapPostUtil.ResponseSap;
import com.hand.hcm.hcm_item_b.dto.ItemB;
import com.hand.hcm.hcm_item_b.mapper.ItemBMapper;
import com.hand.hcm.hcm_plant.dto.Plant;
import com.hand.hcm.hcm_plant.mapper.PlantMapper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hand.hcs.hcs_po_headers.dto.PoHeaders;
import com.hand.hcs.hcs_po_headers.mapper.PoHeadersMapper;
import com.hand.hcs.hcs_po_line_locations.dto.PoLineLocations;
import com.hand.hcs.hcs_po_line_locations.service.IPoLineLocationsService;
import com.hand.hcs.hcs_po_lines.dto.PoLines;
import com.hand.hcs.hcs_po_lines.dto.Receive;
import com.hand.hcs.hcs_po_lines.mapper.PoLinesMapper;
import com.hand.hcs.hcs_po_lines.service.IPoLinesService;
import com.hand.hcs.hcs_refund_order.dto.RefundOrderH;
import com.hand.hcs.hcs_refund_order.dto.RefundOrderL;
import com.hand.hcs.hcs_refund_order.mapper.RefundOrderHMapper;
import com.hand.hcs.hcs_refund_order.mapper.RefundOrderLMapper;
import com.hand.hcs.hcs_suppliers.dto.Suppliers;
import com.hand.hcs.hcs_suppliers.mapper.SuppliersMapper;
import com.hand.itf.itf_purchase_order_header.dto.PurchaseOrderHeader;
import com.hand.itf.itf_purchase_order_header.mapper.PurchaseOrderHeaderMapper;
import com.hand.itf.itf_purchase_order_line.dto.PurchaseOrderLine;
import com.hand.itf.itf_purchase_order_line.mapper.PurchaseOrderLineMapper;

import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(rollbackFor = Exception.class)
public class PoLinesServiceImpl extends BaseServiceImpl<PoLines> implements IPoLinesService {

	private Map<String, Float> pm;

	private SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
	@Autowired
	ICodeService iCodeService;
	@Autowired
	private PoLinesMapper poLinesMapper;
	@Autowired
	private IPoLineLocationsService poLineLocationService;
	@Autowired
	private PoHeadersMapper poHeadersMapper;
	@Autowired
	private PlantMapper plantMapper;
	@Autowired
	private SuppliersMapper suppliersMapper;
	@Autowired
	private ItemBMapper itemBMapper;
	@Autowired
	PurchaseOrderLineMapper purchaseOrderLineMapper;
	@Autowired
	PurchaseOrderHeaderMapper purchaseOrderHeaderMapper;
	@Autowired
	RefundOrderHMapper refundOrderHMapper;

	@Autowired
	RefundOrderLMapper refundOrderLMapper;

	@Override
	public List<PoLines> query(IRequest requestContext, PoLines poLines, int page, int pageSize) {
		PageHelper.startPage(page, pageSize);
		return poLinesMapper.query(poLines);
	}

	@Override
	public List<PoLines> submit(IRequest requestContext, List<PoLines> poLinesList) {
		for (PoLines poLine : poLinesList) {
			PoLineLocations lineLocations = new PoLineLocations();
			lineLocations.setLineLocationId(poLine.getLineLocationId());
			lineLocations.setPromisedDate(poLine.getPromisedDate());
			lineLocations.setLineLocationSupplierDesc(poLine.getLineLocationSupplierDesc());
			// 更新采购订单发运行表
			poLineLocationService.updateByPrimaryKeySelective(requestContext, lineLocations);
		}
		return poLinesList;
	}

	@Override
	public ResponseSap transferPurchaseOrder(PurchaseOrderHeader ipo, List<PurchaseOrderLine> lineList) {
		ResponseSap response = new ResponseSap();
		pm = getAllPlant();
		ipo.setProcessStatus("Y");
		try {
			if (!StringUtil.isEmpty(ipo.getBsart())) {
				switch (ipo.getBsart()) {
				case "NB":// 若为NB往采购订单头行表新增/更新数据
					PoHeaders pohsearch = new PoHeaders();
					pohsearch.setPoNumber(ipo.getBelnr());
					List<PoHeaders> polist = poHeadersMapper.select(pohsearch);
					if (polist == null || polist.size() < 1) {// 采购订单号不存在
						polist = insertPoHeader(ipo);
						refreshPoLine(polist.get(0), lineList, true, response);
					} else {// 采购订单号存在
						// 行更新
						refreshPoLine(polist.get(0), lineList, false, response);
						polist = updatePoHeader(polist.get(0), ipo, lineList);
					}
					break;
				case "RO":// 若为RO往退货单表新增/更新数据
					RefundOrderH rohsearch = new RefundOrderH();
					rohsearch.setRefundOrderNum(ipo.getBelnr());
					List<RefundOrderH> rolist = refundOrderHMapper.select(rohsearch);
					if (rolist == null || rolist.size() < 1) {// 采购订单号不存在
						rolist = insertRoHeader(ipo);
						refreshRoLine(rolist.get(0), lineList, true, response);
					} else {// 采购订单号存在
						// 行更新
						refreshRoLine(rolist.get(0), lineList, false, response);
						rolist = updateRoHeader(rolist.get(0), ipo, lineList);
					}
					break;
				}
				// 如果在行处理的过程中出错了 那么 头接口表的状态为E 错误
				if (lineList.stream().filter(t -> t.getProcessStatus().equals("E")).count() > 0) {
					ipo.setProcessStatus("E");
				}
			}
		} catch (Exception e) {
			response.setResult(false);
			response.setMessage(e.getMessage());
			ipo.setProcessStatus("E");
			ipo.setMessage(e.getMessage());
			throw new RuntimeException(e.getMessage());
		}
		return response;
	}

	/**
	 * 
	 * @description
	 * @author tianmin.wang
	 * @date 2019年11月13日
	 * @param poh
	 * @param lineList
	 * @param isAdd
	 */
	private void refreshPoLine(PoHeaders poh, List<PurchaseOrderLine> lineList, boolean isAdd, ResponseSap response) {
		List<String> errors = new ArrayList<String>();
		if (isAdd) {
			for (PurchaseOrderLine ipol : lineList) {
				ipol.setProcessStatus("Y");
				ipol.setProcessTime(new Date());
				try {
					switch (ipol.getAction()) {
					case "001":// 新增行
						interfaceInsertOne(poh, ipol);
						break;
					}
				} catch (Exception e) {
					ipol.setProcessStatus("E");
					ipol.setMessage(e.getMessage());
					errors.add(e.getMessage());
//					purchaseOrderLineMapper.updateByPrimaryKeySelective(ipol);
//					throw new RuntimeException(e.getMessage());
				}
			}
		} else {
			ServiceRequest request = new ServiceRequest();
			request.setLocale("zh_CN");
			String meanings = "";
			for (PurchaseOrderLine ipol : lineList) {
				ipol.setProcessStatus("Y");
				ipol.setProcessTime(new Date());
				try {
					PoLines polsear = new PoLines();
					polsear.setPoHeaderId(poh.getPoHeaderId());
					polsear.setLineNum(Float.valueOf(ipol.getPosex()));
					List<PoLines> res = mapper.select(polsear);
					PoLines polu = new PoLines();
					switch (ipol.getAction()) {
					case "001":// 新增行
						if (res == null || res.size() == 0) {
							interfaceInsertOne(poh, ipol);
						} else {
							ipol.setMessage("已存在的行数据,未进行新增操作");
						}
						break;
					case "002":
						if (res != null && res.size() > 0) {
							polu.setPoLineId(res.get(0).getPoLineId());
							polu.setQuantity(Float.valueOf(ipol.getBmng2()));
							polu.setUnitPrice(Float.valueOf(ipol.getVprei()));
							polu.setCurrency(ipol.getCurcy());
//							polu.setTaxRate(ipol.getMwskz());
							/**
							 * 20191223 更新订单交期 若采购订单行表 HCS_PO_LINES 的订单行状态 line_status 为 C -已关闭，将状态更新为 A
							 * -已确认
							 */
							if (res.get(0).getLineStatus().equals("C")) {
								polu.setLineStatus("A");
							}
							try {
								polu.setNeedByDate(sdf.parse(ipol.getEdatu()));
							} catch (ParseException e) {
								polu.setNeedByDate(null);
							}

							if (!StringUtil.isEmpty(ipol.getMwskz())) {
								meanings = iCodeService.getCodeMeaningByValue(request, "SRM_QUALITY_DEDUCTION_TAX",
										ipol.getMwskz());//
								if (StringUtil.isEmpty(meanings)) {
									throw new RuntimeException(
											"[" + ipol.getMwskz() + "]在快码SRM_QUALITY_DEDUCTION_TAX中未找到");
								}
								polu.setTaxRate(meanings);
							}
							polu.setPriceUnit(ipol.getPeinh());
							mapper.updateByPrimaryKeySelective(polu);
						}
						break;
					case "003":
						if (res != null && res.size() > 0) {
							polu.setPoLineId(res.get(0).getPoLineId());
							polu.setClosedDate(new Date());
							polu.setClosedBy("-1");
							polu.setClosedCode("Y");
							polu.setClosedFlag("Y");
							polu.setEnableFlag("N");
							polu.setLineStatus("R");
							mapper.updateByPrimaryKeySelective(polu);
						}
						break;
					}
				} catch (Exception e) {
					ipol.setProcessStatus("E");
					ipol.setMessage(e.getMessage());
					response.setResult(false);
					response.setMessage(e.getMessage());
					errors.add(e.getMessage());
				}

			}
		}
		if (errors.size() > 0) {
			throw new RuntimeException(errors.get(0));
		}
	}

	/**
	 * 
	 * @description 新增采购订单行从接口数据
	 * @author tianmin.wang
	 * @date 2019年11月13日
	 * @param poh
	 * @param ipol
	 */
	private void interfaceInsertOne(PoHeaders poh, PurchaseOrderLine ipol) {
//		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		ServiceRequest request = new ServiceRequest();
		request.setLocale("zh_CN");
		String meanings;
		PoLines polinsert = new PoLines();
		polinsert.setPoHeaderId(poh.getPoHeaderId());
		polinsert.setLineNum(Float.valueOf(ipol.getPosex()));
		polinsert.setPlantId(pm.get(ipol.getWerks()));
		polinsert.setItemId(getItemId(ipol.getIdtnr(), pm.get(ipol.getWerks())));
		polinsert.setEdition(ipol.getAnetw() == null ? null : ipol.getAnetw().substring(0, 2).trim());
		polinsert.setUnitCode(ipol.getPmene());
		polinsert.setQuantity(Float.valueOf(ipol.getBmng2()));
		polinsert.setCanceledFlag("N");
		polinsert.setClosedCode("N");
		polinsert.setClosedFlag("N");
		polinsert.setEnableFlag("Y");
		polinsert.setLineStatus("A");
		polinsert.setUnitPrice(Float.valueOf(ipol.getVprei()));
		polinsert.setCurrency(ipol.getCurcy());
		try {
			polinsert.setNeedByDate(sdf.parse(ipol.getEdatu()));
		} catch (ParseException e) {
			polinsert.setNeedByDate(null);
		}
		/*
		 * 接口表中的 税率 关联快速编码税率SRM_QUALITY_DEDUCTION_TAX的值 取含义
		 */
		if (!StringUtil.isEmpty(ipol.getMwskz())) {
			meanings = iCodeService.getCodeMeaningByValue(request, "SRM_QUALITY_DEDUCTION_TAX", ipol.getMwskz());//
			if (StringUtil.isEmpty(meanings)) {
				throw new RuntimeException("[" + ipol.getMwskz() + "]在快码SRM_QUALITY_DEDUCTION_TAX中未找到");
			}
			polinsert.setTaxRate(meanings);
		}
		polinsert.setPriceUnit(ipol.getPeinh());
		mapper.insertSelective(polinsert);
		updateItemPurchaseUomByItemPlantId(polinsert);
	}

	/**
	 * @description 更新物料表的PurchaseUom字段
	 * @author tianmin.wang
	 * @date 2019年11月29日
	 * @param polinsert
	 */
	private void updateItemPurchaseUomByItemPlantId(PoLines polinsert) {
		ItemB uib = new ItemB();
		uib.setItemId(polinsert.getItemId());
		uib.setPlantId(polinsert.getPlantId());
		uib.setPurchaseUom(polinsert.getUnitCode());
		uib.setLastUpdateDate(new Date());
		uib.setLastUpdatedBy(-1l);
		itemBMapper.updatePurchaseUomByItemPlantId(uib);
	}

	/**
	 * 
	 * @description 从接口数据更新采购订单头状态 updatePurchaseUomByItemPlantId
	 * @author tianmin.wang
	 * @date 2019年11月13日
	 * @param poh
	 * @param ipo
	 * @param lineList
	 * @return
	 * @throws ParseException
	 */
	private List<PoHeaders> updatePoHeader(PoHeaders poh, PurchaseOrderHeader ipo, List<PurchaseOrderLine> lineList)
			throws ParseException {
		List<PoHeaders> pos = new ArrayList<PoHeaders>();
		PoHeaders phi = new PoHeaders();
		phi.setPoHeaderId(poh.getPoHeaderId());
		if (lineList.stream().filter(p -> !p.getAction().equals("003")).count() == 0
				&& poHeadersMapper.getNoStatusCount(poh.getPoHeaderId()).intValue() == 0) {
			phi.setPoStatus("R");
		}
		phi.setReleaseDate(sdf.parse(ipo.getDatum()));
		poHeadersMapper.updateByPrimaryKeySelective(phi);
		pos.add(phi);
		return pos;
	}

	/**
	 * 
	 * @description 接口传入的信息新生成采购订单
	 * @author tianmin.wang
	 * @date 2019年11月13日
	 * @param ipo
	 * @return
	 * @throws ParseException
	 */
	private List<PoHeaders> insertPoHeader(PurchaseOrderHeader ipo) throws ParseException {
		List<PoHeaders> pos = new ArrayList<PoHeaders>();
		PoHeaders phi = new PoHeaders();
		phi.setPoNumber(ipo.getBelnr());
		phi.setPlantId(pm.get("CNKE"));
		phi.setSupplierId(getSupplierId(ipo.getPartn()));
		phi.setPoOrderType(ipo.getBsart());
		phi.setPoStatus("A");
		phi.setReleaseDate(sdf.parse(ipo.getDatum()));
		poHeadersMapper.insertSelective(phi);
		pos.add(phi);
		return pos;
	}

	/**
	 * 更新退货单行状态
	 * 
	 * @author tianmin.wang
	 * @date 2019年11月13日
	 * @param roh
	 * @param lineList
	 * @param isAdd
	 */
	private void refreshRoLine(RefundOrderH roh, List<PurchaseOrderLine> lineList, boolean isAdd,
			ResponseSap response) {
		List<String> errors = new ArrayList<String>();
		if (isAdd) {
			for (PurchaseOrderLine ipol : lineList) {
				ipol.setProcessStatus("Y");
				ipol.setProcessTime(new Date());
				try {
					switch (ipol.getAction()) {
					case "001":// 新增行
						interfaceRoLineInsertOne(roh, ipol);
						break;
					}
				} catch (Exception e) {
					errors.add(e.getMessage());
					ipol.setProcessStatus("E");
					ipol.setMessage(e.getMessage());
				}
			}
		} else {
			ServiceRequest request = new ServiceRequest();
			request.setLocale("zh_CN");
			String meanings;
			for (PurchaseOrderLine ipol : lineList) {
				ipol.setProcessStatus("Y");
				ipol.setProcessTime(new Date());
				try {
					RefundOrderL rolsear = new RefundOrderL();
					rolsear.setRoHeaderId(roh.getRoHeaderId());
					rolsear.setRoLineNum(Float.valueOf(ipol.getPosex()));
					List<RefundOrderL> resl = refundOrderLMapper.select(rolsear);
					switch (ipol.getAction()) {
					case "001":// 新增退货单行
						if (resl == null || resl.size() == 0) {
							interfaceRoLineInsertOne(roh, ipol);
						} else {
							ipol.setMessage("已存在的行数据,未进行新增操作");
						}
						break;
					case "002":// 更新退货单行
						if (resl != null && resl.size() > 0) {
							for (RefundOrderL res : resl) {
								RefundOrderL rolu = new RefundOrderL();
								rolu.setRoLineId(res.getRoLineId());
								rolu.setQuantity(Float.valueOf(ipol.getBmng2()));
								rolu.setUnitPrice(Float.valueOf(ipol.getVprei()));
								rolu.setCurrency(ipol.getCurcy());

								/**
								 * 20191223 更新订单交期
								 */
								try {
									rolu.setNeedByDate(sdf.parse(ipol.getEdatu()));
								} catch (ParseException e) {
									rolu.setNeedByDate(null);
								}

								if (!StringUtil.isEmpty(ipol.getMwskz())) {
									meanings = iCodeService.getCodeMeaningByValue(request, "SRM_QUALITY_DEDUCTION_TAX",
											ipol.getMwskz());//
									if (StringUtil.isEmpty(meanings)) {
										throw new RuntimeException(
												"[" + ipol.getMwskz() + "]在快码SRM_QUALITY_DEDUCTION_TAX中未找到");
									}
									rolu.setTaxRate(meanings);
								}
								rolu.setPriceUnit(ipol.getPeinh());
								refundOrderLMapper.updateByPrimaryKeySelective(rolu);
							}
						}
						break;
					case "003":// 关闭退货单行
						if (resl != null && resl.size() > 0) {
							for (RefundOrderL res : resl) {
								RefundOrderL rolu = new RefundOrderL();
								rolu.setRoLineId(res.getRoLineId());
								rolu.setRoLineStatus("R");
								rolu.setClosedFlag("Y");
								refundOrderLMapper.updateByPrimaryKeySelective(rolu);
							}
						}
						break;
					}
				} catch (Exception e) {
					errors.add(e.getMessage());
					ipol.setProcessStatus("E");
					ipol.setMessage(e.getMessage());
					response.setResult(false);
					response.setMessage(e.getMessage());
				}
			}
		}
		if (errors.size() > 0) {
			throw new RuntimeException(errors.get(0));
		}
	}

	/**
	 * 从接口数据新增退货单行
	 * 
	 * @author tianmin.wang
	 * @date 2019年11月13日
	 * @param roh
	 * @param ipol
	 */
	private void interfaceRoLineInsertOne(RefundOrderH roh, PurchaseOrderLine ipol) {
		ServiceRequest request = new ServiceRequest();
		request.setLocale("zh_CN");
		String meanings;
		RefundOrderL rolinsert = new RefundOrderL();
		rolinsert.setRoHeaderId(roh.getRoHeaderId());
		rolinsert.setRoLineNum(Float.valueOf(ipol.getPosex()));
		rolinsert.setPlantId(pm.get(ipol.getWerks()));
		rolinsert.setItemId(getItemId(ipol.getIdtnr(), pm.get(ipol.getWerks())));
		rolinsert.setItemVersion(ipol.getAnetw() == null ? null : ipol.getAnetw().substring(0, 2).trim());
		rolinsert.setUnitCode(ipol.getPmene());
		rolinsert.setQuantity(Float.valueOf(ipol.getBmng2()));
		rolinsert.setRoLineStatus("A");
		rolinsert.setUnitPrice(Float.valueOf(ipol.getVprei()));
		rolinsert.setCurrency(ipol.getCurcy());
		if (!StringUtil.isEmpty(ipol.getMwskz())) {
			meanings = iCodeService.getCodeMeaningByValue(request, "SRM_QUALITY_DEDUCTION_TAX", ipol.getMwskz());//
			if (StringUtil.isEmpty(meanings)) {
				throw new RuntimeException("[" + ipol.getMwskz() + "]在快码SRM_QUALITY_DEDUCTION_TAX中未找到");
			}
			rolinsert.setTaxRate(meanings);
		}
		rolinsert.setPriceUnit(ipol.getPeinh());
		rolinsert.setSettlementFlag("N");
		rolinsert.setClosedFlag("N");

		try {
			rolinsert.setNeedByDate(sdf.parse(ipol.getEdatu()));
		} catch (ParseException e) {
			rolinsert.setNeedByDate(null);
		}
		refundOrderLMapper.insertSelective(rolinsert);
	}

	/**
	 * 从接口数据更新退货单头数据 tianmin.wang 20191113
	 * 
	 * @param roh      退货单头
	 * @param ipo      接口的头数据
	 * @param lineList 接口收到的行数据
	 * @return
	 * @throws ParseException
	 */
	private List<RefundOrderH> updateRoHeader(RefundOrderH roh, PurchaseOrderHeader ipo,
			List<PurchaseOrderLine> lineList) throws ParseException {
		List<RefundOrderH> pos = new ArrayList<RefundOrderH>();
		RefundOrderH phi = new RefundOrderH();
		phi.setRoHeaderId(roh.getRoHeaderId());
		if (lineList.stream().filter(p -> !p.getAction().equals("003")).count() == 0
				&& refundOrderHMapper.getNoStatusCount(roh.getRoHeaderId()).intValue() == 0) {
			phi.setRefundOrderStatus("R");
		}
		phi.setConfirmDate(sdf.parse(ipo.getDatum()));
		refundOrderHMapper.updateByPrimaryKeySelective(phi);
		pos.add(phi);
		return pos;
	}

	/**
	 * 接口传入的信息新生成退货单
	 * 
	 * @author tianmin.wang
	 * @date 2019年11月13日
	 * @param ipo
	 * @return
	 * @throws ParseException
	 */
	private List<RefundOrderH> insertRoHeader(PurchaseOrderHeader ipo) throws ParseException {
		List<RefundOrderH> pos = new ArrayList<RefundOrderH>();
		RefundOrderH phi = new RefundOrderH();
		phi.setRefundOrderNum(ipo.getBelnr());
		phi.setPlantId(pm.get("CNKE"));
		phi.setSupplierId(getSupplierId(ipo.getPartn()));
		phi.setRefundOrderType(ipo.getBsart());
		phi.setRefundOrderStatus("A");
		phi.setConfirmDate(sdf.parse(ipo.getDatum()));
		refundOrderHMapper.insertSelective(phi);
		pos.add(phi);
		return pos;
	}

	/**
	 * 获取所有工厂写入缓存
	 * 
	 * @author tianmin.wang
	 * @date 2019年11月13日
	 * @return
	 */
	private Map<String, Float> getAllPlant() {
		Map<String, Float> re = new HashMap<String, Float>();
		List<Plant> plantList = plantMapper.selectAll();
		for (Plant plant : plantList) {
			re.put(plant.getPlantCode(), plant.getPlantId());
		}
		return re;
	}

	/**
	 * 获取物料ID
	 * 
	 * @author tianmin.wang
	 * @date 2019年11月13日
	 * @param itemCode
	 * @param plantId
	 * @return
	 */
	private Float getItemId(String itemCode, Float plantId) {
		ItemB ib = new ItemB();
		ib.setPlantId(plantId);
		ib.setItemCode(itemCode);
		List<ItemB> re = itemBMapper.select(ib);
		if (re == null || re.size() == 0) {
			throw new RuntimeException("未找到相关物料[" + itemCode + "]");
		}
		return re.get(0).getItemId();
	}

	/**
	 * 通过供应商编码获取主键
	 * 
	 * @author tianmin.wang
	 * @date 2019年11月13日
	 * @param supplierCode
	 * @return
	 */
	private Float getSupplierId(String supplierCode) {
		Suppliers su = new Suppliers();
		su.setSupplierCode(supplierCode);
		List<Suppliers> re = suppliersMapper.select(su);
		if (re == null || re.size() == 0) {
			throw new RuntimeException("未找到相关供应商[" + supplierCode + "]");
		}
		return re.get(0).getSupplierId();
	}
}