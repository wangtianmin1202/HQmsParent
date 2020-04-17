package com.hand.hcs.hcs_suppliers.mapper;

import java.util.List;

import com.hand.hap.mybatis.common.Mapper;
import com.hand.hcs.hcs_suppliers.dto.Suppliers;

public interface SuppliersMapper extends Mapper<Suppliers>{
	
	List<Suppliers> lovselect(Suppliers dto); 
	/**
	 * 供应商LOV
	 * @param dto
	 * @return
	 */
	List<Suppliers> supplierLov(Suppliers dto); 
	/**
	 * 供方账号管理
	 * @param dto
	 * @return
	 */
	List<Suppliers> query(Suppliers dto);
	/**
	 * 删除供应商用户关系
	 * @param dto
	 */
	void updateSupplier(Suppliers dto);
	/**
	 * 更新用户供应商信息 
	 * @param dto
	 */
	void updateUserInfo(Suppliers dto);
	
	void updateHaveYes(Suppliers suppliers);
	
	void updateHaveNo(Suppliers suppliers);
	
	/**
	 * 
	 * @description 通过供应商编码得到供应商ID
	 * @author tianmin.wang
	 * @date 2019年12月16日 
	 * @param code
	 * @return
	 */
	Float getSupplierIdByCode(String code);
	
	String getSupplierNameById(Float id);
	
}