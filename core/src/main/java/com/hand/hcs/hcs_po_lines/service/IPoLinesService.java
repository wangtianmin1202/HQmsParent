package com.hand.hcs.hcs_po_lines.service;

import java.util.List;

import com.hand.hap.core.IRequest;
import com.hand.hap.core.ProxySelf;
import com.hand.hap.system.service.IBaseService;
import com.hand.hap.util.SoapPostUtil.Response;
import com.hand.hap.util.SoapPostUtil.ResponseSap;
import com.hand.hcs.hcs_po_lines.dto.PoLines;
import com.hand.hcs.hcs_po_lines.dto.Receive;
import com.hand.itf.itf_purchase_order_header.dto.PurchaseOrderHeader;
import com.hand.itf.itf_purchase_order_line.dto.PurchaseOrderLine;

public interface IPoLinesService extends IBaseService<PoLines>, ProxySelf<IPoLinesService>{
	
	/**
	 * 明细信息查询
	 * @param requestContext
	 * @param poLines
	 * @param page
	 * @param pageSize
	 * @return
	 */
	List<PoLines> query(IRequest requestContext, PoLines poLines, int page, int pageSize);
	/**
	 * 采购订单确认明细界面： 保存
	 * @param requestContext
	 * @param poLinesList
	 * @return
	 */
	List<PoLines> submit(IRequest requestContext, List<PoLines> poLinesList);
	
	
	/**
	 * 
	 * @description sap传srm采购订单接口
	 * @author tianmin.wang
	 * @date 2019年11月13日 
	 * @param ipo 头数据
	 * @param lineList 多行数据
	 * @return
	 */
	ResponseSap transferPurchaseOrder(PurchaseOrderHeader ipo, List<PurchaseOrderLine> lineList);
}