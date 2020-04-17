package com.hand.hcs.hcs_asl_control.service;

import java.util.List;

import com.hand.hap.core.ProxySelf;
import com.hand.hap.system.service.IBaseService;
import com.hand.hap.util.SoapPostUtil.Response;
import com.hand.hap.util.SoapPostUtil.ResponseSap;
import com.hand.hcs.hcs_asl_control.dto.AslControl;
import com.hand.itf.itf_supplier_materials.dto.SupplierMaterials;

public interface IAslControlService extends IBaseService<AslControl>, ProxySelf<IAslControlService>{

	/**
	 * sap物料供应商关系接口
	 * @param smilist
	 * @return
	 */
	ResponseSap transferSupplierMaterials(List<SupplierMaterials> smilist);

}