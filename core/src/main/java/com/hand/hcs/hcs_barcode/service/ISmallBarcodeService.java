package com.hand.hcs.hcs_barcode.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.hand.hap.core.IRequest;
import com.hand.hap.core.ProxySelf;
import com.hand.hap.system.dto.ResponseData;
import com.hand.hap.system.service.IBaseService;
import com.hand.hcs.hcs_barcode.dto.SmallBarcode;
import com.hand.hcs.hcs_out_barcode.dto.OutBarcode;

public interface ISmallBarcodeService extends IBaseService<SmallBarcode>, ProxySelf<ISmallBarcodeService>{
	/**
	 * 查询
	 * @param deliveryTicketH
	 * @return
	 */
	List<SmallBarcode> query(IRequest requestCtx,SmallBarcode smallBarcode, int page, int pageSize);
	/**
	 * 新增物料标签
	 * @param dto 物料标签信息
	 * @param requestCtx 请求上下文
	 * @param request 请求
	 * @return
	 */
	ResponseData insertsmall(SmallBarcode dto,IRequest requestCtx, HttpServletRequest request);
	/**
	 * 更新状态
	 * @param requestCtx 请求上下文
	 * @param smallBarcode 更新数据集
	 */
	void changeFlag(IRequest requestCtx,List<SmallBarcode> smallBarcode);
	/**
	 * 关联小箱条码
	 * @param requestCtx 请求上下文
	 * @param smallBarcode 查询条件
	 * @param page 页码
	 * @param pageSize 页大小
	 * @return 查询结果集
	 */
	List<SmallBarcode> querySmallBarcode(IRequest requestCtx, SmallBarcode smallBarcode, int page, int pageSize);
	/**
	 * 关联小箱条码 完成按钮
	 * @param requestContext 请求上下文
	 * @param smallBarcode 小箱条码数据集
	 * @return
	 */
	ResponseData saveInfo(IRequest requestContext,List<SmallBarcode> smallBarcode);
	
	/**
	 * 打印外箱条码查询
	 * @param smallBarcode 物料标签
	 * @return
	 */
	List<SmallBarcode> printQuery(SmallBarcode smallBarcode);
	/**
	 * 更改打印次数
	 * @param requestContext 请求上下文
	 * @param smallBarcodeList 打印信息
	 * @return
	 */
	List<SmallBarcode> updatePrintTime(IRequest requestContext,List<SmallBarcode> smallBarcodeList);
	/**
	 * 获取物料标签
	 * @param requestContext 请求上下文
	 * @param smallBarcode 物料标签
	 * @return 物料标签号
	 */
	String getsbarcode(IRequest requestContext, SmallBarcode smallBarcode);
	/**
	 * 解绑
	 * @param requestContext 请求上下文
	 * @param smallBarcodeList 解绑物料标签信息
	 * @return
	 */
	List<SmallBarcode> unBind(IRequest requestContext,List<SmallBarcode> smallBarcodeList);
	/**
	 * 确认绑定
	 * @param requestContext 请求上下文
	 * @param smallBarcodeList 绑定信息
	 * @return
	 */
	List<SmallBarcode> confirmBind(IRequest requestContext,List<SmallBarcode> smallBarcodeList);
}