package com.hand.hcs.hcs_supplier_site.mapper;

import java.util.List;

import com.hand.hap.mybatis.common.Mapper;
import com.hand.hcs.hcs_supplier_site.dto.SupplierSite;

public interface SupplierSiteMapper extends Mapper<SupplierSite>{
	/**
	 * 供应商地点LOV
	 * @param supplierSite
	 * @return
	 */
	List<SupplierSite> supplierSiteLov(SupplierSite supplierSite);
}