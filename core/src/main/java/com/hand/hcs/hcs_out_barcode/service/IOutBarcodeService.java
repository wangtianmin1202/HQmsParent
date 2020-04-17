package com.hand.hcs.hcs_out_barcode.service;

import java.util.List;
import java.util.Map;

import com.hand.hap.core.IRequest;
import com.hand.hap.core.ProxySelf;
import com.hand.hap.system.dto.ResponseData;
import com.hand.hap.system.service.IBaseService;
import com.hand.hap.util.SoapPostUtil;
import com.hand.hap.webservice.ws.idto.ContainerInformation;
import com.hand.hcs.hcs_barcode.dto.SmallBarcode;
import com.hand.hcs.hcs_out_barcode.dto.OutBarcode;
import com.hand.itf.itf_container_info.dto.ContainerInfo;

public interface IOutBarcodeService extends IBaseService<OutBarcode>, ProxySelf<IOutBarcodeService>{
	/**
	 * 外箱条码查询
	 * @param requestContext
	 * @param outBarcode
	 * @param page
	 * @param pageSize
	 * @return
	 */
	List<OutBarcode> query(IRequest requestContext, OutBarcode outBarcode, int page, int pageSize);
	/**
	 * 生成条码号
	 * @param requestContext
	 * @param outBarcode
	 * @return
	 */
	String getobarcode(IRequest requestContext, OutBarcode outBarcode);
	/**
	 * 新建外箱条码
	 * @param requestContext
	 * @param outBarcode
	 * @return
	 */
	ResponseData addInfo(IRequest requestContext, OutBarcode outBarcode);
	/**
	 * 外箱条码打印
	 * @param requestContext
	 * @param outBarcodeList
	 * @return
	 */
	ResponseData printQuery(IRequest requestContext, List<OutBarcode> outBarcodeList);
	/**
	 * 失效
	 * @param requestContext
	 * @param outBarcodeList
	 * @return
	 */
	ResponseData changeFlag(IRequest requestContext, List<OutBarcode> outBarcodeList);
	/**
	 * 绑定查询
	 * @param requestContext
	 * @param outBarcode
	 * @param page
	 * @param pageSize
	 * @return
	 */
	List<OutBarcode> bindQuery(IRequest requestContext,OutBarcode outBarcode, int page, int pageSize);
	/**
	 * 确认绑定
	 * @param requestContext
	 * @param outBarcodeList
	 * @return
	 */
	ResponseData checkBind(IRequest requestContext, List<SmallBarcode> dto);
	/**
	 * 获取最大流水号
	 * @param outBarcode
	 * @return
	 */
	Map<String,String> getMaxNum(OutBarcode outBarcode);
	/**
	 * 容器绑定校验
	 * @param requestContext
	 * @param dto
	 * @return
	 */
	ResponseData bindValidator(IRequest requestContext, List<OutBarcode> dto);
	/**
	 * 容器绑定查询
	 * @param requestContext
	 * @param outBarcode
	 * @param page
	 * @param pageSize
	 * @return
	 */
	List<OutBarcode> outBindQuery(IRequest requestContext,OutBarcode outBarcode, int page, int pageSize);
	/**
	 * 确认绑定
	 * @param requestContext
	 * @param dto
	 * @return
	 */
	List<OutBarcode> confirmBind(IRequest requestContext, List<OutBarcode> dto);
	/**
	 * 解绑
	 * @param requestContext
	 * @param dto
	 * @return
	 */
	ResponseData unBind(IRequest requestContext, List<OutBarcode> dto);
	/**
	 * 详情查询
	 * @param requestContext
	 * @param outBarcode
	 * @param page
	 * @param pageSize
	 * @return
	 */
	List<OutBarcode> queryDetail(IRequest requestContext, OutBarcode outBarcode, int page, int pageSize);

	/**
	 * 绑定查询（左边）
	 * @param requestContext
	 * @param outBarcode
	 * @param page
	 * @param pageSize
	 * @return
	 */
	List<OutBarcode> bindQueryLeft(IRequest requestContext, OutBarcode outBarcode, int page, int pageSize);
	/**
	 * 已绑定标签查询(右边)
	 * @param requestContext
	 * @param outBarcode
	 * @param page
	 * @param pageSize
	 * @return
	 */
	List<OutBarcode> queryRight(IRequest requestContext, OutBarcode outBarcode, int page, int pageSize);
	/**
	 * 提交绑定关系
	 * @param requestContext
	 * @param dto
	 * @return
	 */
	List<OutBarcode> bind(IRequest requestContext, List<OutBarcode> dto);
	
	/**
	 * wms接口传输
	 * @param ci
	 */
	SoapPostUtil.Response transferContainer(ContainerInfo ci);
	/**
	 * 更新打印次数
	 * @param requestContext 请求上下文
	 * @param dto 打印信息
	 * @return 打印信息
	 */
	List<OutBarcode> updatePrintTime(IRequest requestContext, List<OutBarcode> dto);
}