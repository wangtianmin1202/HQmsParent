package com.hand.hqm.hqm_fqc_barcode.service;

import com.hand.hap.core.ProxySelf;
import com.hand.hap.system.service.IBaseService;
import com.hand.hap.util.SoapPostUtil.ResponseSap;
import com.hand.hap.webservice.ws.idto.FqcBarcodeInfo;
import com.hand.hqm.hqm_fqc_barcode.dto.FqcBarcode;

public interface IFqcBarcodeService extends IBaseService<FqcBarcode>, ProxySelf<IFqcBarcodeService>{

	/**
	 * @description 接口数据接收
	 * @author tianmin.wang
	 * @date 2020年2月27日 
	 * @param fbi
	 * @return
	 */
	ResponseSap transferFqcBarcode(FqcBarcodeInfo fbi);

}