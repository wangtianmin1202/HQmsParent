package com.hand.hcs.hcs_refund_order.service.impl;

import com.github.pagehelper.PageHelper;
import com.hand.hap.core.IRequest;
import com.hand.hap.system.service.impl.BaseServiceImpl;
import com.hand.hap.util.SoapPostUtil;
import com.hand.hap.webservice.ws.idto.ReturnDeliveryExecute;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hand.hcs.hcs_po_headers.dto.PoHeaders;
import com.hand.hcs.hcs_po_headers.mapper.PoHeadersMapper;
import com.hand.hcs.hcs_po_lines.dto.PoLines;
import com.hand.hcs.hcs_po_lines.mapper.PoLinesMapper;
import com.hand.hcs.hcs_refund_order.dto.RefundOrderH;
import com.hand.hcs.hcs_refund_order.dto.RefundOrderL;
import com.hand.hcs.hcs_refund_order.mapper.RefundOrderHMapper;
import com.hand.hcs.hcs_refund_order.mapper.RefundOrderLMapper;
import com.hand.hcs.hcs_refund_order.service.IRefundOrderLService;
import com.hand.itf.itf_return_delivery_execute.mapper.ReturnDeliveryExecuteMapper;

import jodd.util.StringUtil;

import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(rollbackFor = Exception.class)
public class RefundOrderLServiceImpl extends BaseServiceImpl<RefundOrderL> implements IRefundOrderLService {
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	@Autowired
	private RefundOrderLMapper mapper;
	@Autowired
	private PoHeadersMapper poHeadersMapper;
	@Autowired
	private PoLinesMapper poLinesMapper;
	@Autowired
	private RefundOrderHMapper refundOrderHMapper;

	@Override
	public List<RefundOrderL> query(IRequest requestContext, RefundOrderL dto, int page, int pageSize) {
		PageHelper.startPage(page, pageSize);
		return mapper.query(dto);
	}

	@Override
	public List<RefundOrderL> queryLine(IRequest requestContext, RefundOrderL dto, int page, int pageSize) {
		PageHelper.startPage(page, pageSize);
		return mapper.queryLine(dto);
	}

	@Autowired
	ReturnDeliveryExecuteMapper returnDeliveryExecuteMapper;// 接口表

	@Override
	public SoapPostUtil.Response transferReturnDeliveryExecute(
			com.hand.itf.itf_return_delivery_execute.dto.ReturnDeliveryExecute rde) {// 退货执行wms接口
		SoapPostUtil.Response response = new SoapPostUtil.Response();
		Date now = new Date();
		rde.setProcessStatus("Y");
		rde.setProcessTime(new Date());
		RefundOrderH rohs = new RefundOrderH();
		rohs.setRefundOrderNum(rde.getReturnOrder());
		List<RefundOrderH> rohr = refundOrderHMapper.select(rohs);

		PoHeaders pores = new PoHeaders();
		pores.setPoNumber(rde.getReturnOrder());
		List<PoHeaders> pors = poHeadersMapper.select(pores);
		if ((pors == null || pors.size() == 0) && rde.getReturnOrder().substring(0, 1).equals("1")) {
			rde.setMessage("RO号的PO号不存在 不进行操作");
			returnDeliveryExecuteMapper.updateByPrimaryKeySelective(rde);
			return response;
		}
		try {
			if (rohr != null && rohr.size() > 0) {
				RefundOrderL rols = new RefundOrderL();
				rols.setRoHeaderId(rohr.get(0).getRoHeaderId());
				rols.setRoLineNum(Float.valueOf(rde.getReturnLineNo()));
				List<RefundOrderL> rolr = mapper.select(rols);
				if (rolr != null && rolr.size() > 0) {
					if (rde.getReturnOrder().substring(0, 1).equals("6")) {
						int insertCount = 0;
						for (RefundOrderL rol : rolr) {
							if (rol.getQuantityRefunded() == null) {// 如果为null 就更新
								updateLine(rohr, rol, rde);
							} else {// 如果不为null就新增
								if (insertCount > 0)
									continue;
								insertLineFromRo(rohr, rol, rde);
								insertCount++;
							}
						}
						// 刷新头表的状态
						refundOrderHMapper.refreshHeaderStatus(rohr.get(0).getRoHeaderId());
					} else if (rde.getReturnOrder().substring(0, 1).equals("1")) {
						for (RefundOrderL rol : rolr) {
							insertLineFromPo(rohr, null, rde);
							break;
						}
					}
				} else {// 传过来的头号存在行号不存在
					if (rde.getReturnOrder().substring(0, 1).equals("1")) {
						insertLineFromPo(rohr, null, rde);
					}
				}
			} else {// 传过来的头号不存在
				if (rde.getReturnOrder().substring(0, 1).equals("1")) {
					insertHeaderAndLine(rde);
				}
			}

			/**
			 *  100订单退货，打开关闭的采购订单：根据退货单号关联采购订单头表 HCS_PO_HEADERS 的po_number取po_header_id
			 * 再根据po_header_id、退货单行号关联采购订单行表 HCS_PO_LINES 取行状态 line_status 若为 C -已关闭 将其值更新为
			 * A -已确认
			 */
			poOpen(rde.getReturnOrder(), rde.getReturnLineNo());

		} catch (Exception e) {
			response.setResult(false);
			response.setMessage(e.getMessage());
			rde.setProcessStatus("E");
			rde.setMessage(e.getMessage());
		}
		returnDeliveryExecuteMapper.updateByPrimaryKeySelective(rde);
		return response;
	}

	/**
	 * @description 退货执行时 若有为C的poLine 则 更新为 A
	 * @author tianmin.wang
	 * @date 2019年12月23日
	 * @param returnOrder
	 * @param returnLineNo
	 */
	private void poOpen(String returnOrder, String returnLineNo) {
		// TODO Auto-generated method stub
		PoHeaders pohSearch = new PoHeaders();
		pohSearch.setPoNumber(returnOrder);
		List<PoHeaders> hList = poHeadersMapper.select(pohSearch);// 找PO头
		if (hList != null && hList.size() > 0) {
			PoLines polSearch = new PoLines();
			polSearch.setPoHeaderId(hList.get(0).getPoHeaderId());
			polSearch.setLineNum(Float.valueOf(returnLineNo));
			List<PoLines> lList = poLinesMapper.select(polSearch);
			if (lList != null && lList.size() > 0 && lList.get(0).getLineStatus().equals("C")) {
				// 更新
				PoLines updateLine = new PoLines();
				updateLine.setPoLineId(lList.get(0).getPoLineId());
				updateLine.setLineStatus("A");
				updateLine.setLastUpdateDate(new Date());
				poLinesMapper.updateByPrimaryKeySelective(updateLine);
			}

			if ("C".equals(hList.get(0).getPoStatus())) {
				hList.get(0).setPoStatus("A");
				poHeadersMapper.updateByPrimaryKeySelective(hList.get(0));
			}
		}
	}

	/**
	 * 
	 * @description 头行数据处理
	 * @author tianmin.wang
	 * @date 2019年11月19日
	 * @param rde
	 * @throws ParseException
	 */
	private void insertHeaderAndLine(com.hand.itf.itf_return_delivery_execute.dto.ReturnDeliveryExecute rde)
			throws ParseException {
		RefundOrderH rohi = new RefundOrderH();
		PoHeaders pores = new PoHeaders();
		pores.setPoNumber(rde.getReturnOrder());
		pores = poHeadersMapper.select(pores).get(0);
		PoLines pols = new PoLines();
		pols.setPoHeaderId(pores.getPoHeaderId());
		pols.setLineNum(Float.valueOf(rde.getReturnLineNo()));
		pols = poLinesMapper.select(pols).get(0);
		rohi.setRefundOrderNum(rde.getReturnOrder());
		rohi.setRefundOrderType("NB");
		rohi.setRefundOrderStatus("I");
		rohi.setSupplierId(pores.getSupplierId());
		rohi.setPlantId(pores.getPlantId());
		rohi.setConfirmDate(pores.getReleaseDate());
		refundOrderHMapper.insertSelective(rohi);
		RefundOrderL roli = new RefundOrderL();
		roli.setRoHeaderId(rohi.getRoHeaderId());
		roli.setRoLineNum(Float.valueOf(rde.getReturnLineNo()));
		roli.setPlantId(getPlantId(rde.getReturnOrder(), rde.getReturnLineNo()));
		roli.setItemId(pols.getItemId());
		roli.setItemVersion(pols.getEdition());
		roli.setUnitCode(pols.getUnitCode());
		roli.setQuantity(Float.valueOf(rde.getReturnQty()));
		roli.setQuantityRefunded(Float.valueOf(rde.getReturnQty()));
		roli.setRoLineStatus("I");
		roli.setUnitPrice(pols.getUnitPrice());
		roli.setCurrency(pols.getCurrency());
		try {
			if (!StringUtil.isEmpty(rde.getReturnDate())) {
				roli.setRefundedDate(sdf.parse(rde.getReturnDate()));
			}
		} catch (Exception e) {
			throw new RuntimeException("时间转换失败[" + rde.getReturnDate() + "]");
		}
		roli.setTaxRate(pols.getTaxRate());
		roli.setPriceUnit(pols.getPriceUnit());
		roli.setSettlementFlag("N");
		mapper.insertSelective(roli);
	}

	/**
	 * 
	 * @description
	 * @author tianmin.wang
	 * @date 2019年11月19日
	 * @param rohr
	 * @param rol
	 * @param rde
	 * @throws ParseException
	 */
	public void updateLine(List<RefundOrderH> rohr, RefundOrderL rol,
			com.hand.itf.itf_return_delivery_execute.dto.ReturnDeliveryExecute rde) {

		Date now = new Date();
		RefundOrderL rolu = new RefundOrderL();
		rolu.setRoLineId(rol.getRoLineId());
		rolu.setQuantityRefunded(Float.valueOf(rde.getReturnQty()));
		try {
			if (!StringUtil.isEmpty(rde.getReturnDate())) {
				rolu.setRefundedDate(sdf.parse(rde.getReturnDate()));
			}
		} catch (Exception e) {
			throw new RuntimeException("时间转换失败[" + rde.getReturnDate() + "]");
		}
		rolu.setQuantityRefunded(Float.valueOf(rde.getReturnQty()));
		rolu.setRoLineStatus("I");
		rolu.setLastUpdateDate(now);
		mapper.updateByPrimaryKeySelective(rolu);

	}

	/**
	 * 
	 * @description 从已存在的退货单行新增退货单行
	 * @author tianmin.wang
	 * @date 2019年11月14日
	 * @param rohr
	 * @param rol  已存在的退货单行
	 * @param rde
	 * @throws ParseException
	 */
	public void insertLineFromRo(List<RefundOrderH> rohr, RefundOrderL rol,
			com.hand.itf.itf_return_delivery_execute.dto.ReturnDeliveryExecute rde) throws ParseException {
		RefundOrderL roli = new RefundOrderL();
		roli.setRoHeaderId(rohr.get(0).getRoHeaderId());
		roli.setRoLineNum(Float.valueOf(rde.getReturnLineNo()));
		roli.setPlantId(rol.getPlantId());
		roli.setItemId(rol.getItemId());
		roli.setItemVersion(rol.getItemVersion());
		roli.setUnitCode(rol.getUnitCode());
//		roli.setQuantity(Float.valueOf(rde.getReturnQty()));
		roli.setQuantity(rol.getQuantity());
		roli.setQuantityRefunded(Float.valueOf(rde.getReturnQty()));
		roli.setRoLineStatus("I");
		roli.setUnitPrice(rol.getUnitPrice());
		roli.setCurrency(rol.getCurrency());
		roli.setTaxRate(rol.getTaxRate());
		roli.setPriceUnit(rol.getPriceUnit());
		try {
			if (!StringUtil.isEmpty(rde.getReturnDate())) {
				roli.setRefundedDate(sdf.parse(rde.getReturnDate()));
			}
		} catch (Exception e) {
			throw new RuntimeException("时间转换失败[" + rde.getReturnDate() + "]");
		}
		roli.setSettlementFlag("N");
		mapper.insertSelective(roli);
	}

	/**
	 * 
	 * @description 从已存在的采购订单行新增退货单行
	 * @author tianmin.wang
	 * @date 2019年11月14日
	 * @param rohr 退货单头
	 * @param rol  默认传入为null
	 * @param rde  接口信息
	 * @throws ParseException
	 */
	public void insertLineFromPo(List<RefundOrderH> rohr, RefundOrderL rol,
			com.hand.itf.itf_return_delivery_execute.dto.ReturnDeliveryExecute rde) {
		RefundOrderL roli = new RefundOrderL();
		PoHeaders pores = new PoHeaders();
		pores.setPoNumber(rde.getReturnOrder());
		pores = poHeadersMapper.select(pores).get(0);// 可能会发生 index0 size0
		PoLines pols = new PoLines();
		pols.setPoHeaderId(pores.getPoHeaderId());
		pols.setLineNum(Float.valueOf(rde.getReturnLineNo()));
		pols = poLinesMapper.select(pols).get(0);
		roli.setRoHeaderId(rohr.get(0).getRoHeaderId());
		roli.setRoLineNum(Float.valueOf(rde.getReturnLineNo()));
		roli.setPlantId(getPlantId(rde.getReturnOrder(), rde.getReturnLineNo()));
		roli.setItemId(pols.getItemId());
		roli.setItemVersion(pols.getEdition());
		roli.setUnitCode(pols.getUnitCode());
		roli.setQuantity(Float.valueOf(rde.getReturnQty()));
		roli.setQuantityRefunded(Float.valueOf(rde.getReturnQty()));
		try {
			if (!StringUtil.isEmpty(rde.getReturnDate())) {
				roli.setRefundedDate(sdf.parse(rde.getReturnDate()));
			}
		} catch (Exception e) {
			throw new RuntimeException("时间转换失败[" + rde.getReturnDate() + "]");
		}
		roli.setRoLineStatus("I");
		roli.setUnitPrice(pols.getUnitPrice());
		roli.setCurrency(pols.getCurrency());
		roli.setTaxRate(pols.getTaxRate());
		roli.setPriceUnit(pols.getPriceUnit());
		roli.setSettlementFlag("N");
		mapper.insertSelective(roli);
	}

	/**
	 * 
	 * @description 获取工厂
	 * @author tianmin.wang
	 * @date 2019年11月19日
	 * @param refundOrderNum
	 * @param ReturnLineNo
	 * @return
	 */
	public Float getPlantId(String refundOrderNum, String ReturnLineNo) {
		return poHeadersMapper.selectPlant(refundOrderNum, ReturnLineNo);
	}

}