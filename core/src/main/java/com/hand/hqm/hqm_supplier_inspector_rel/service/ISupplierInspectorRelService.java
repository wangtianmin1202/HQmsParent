package com.hand.hqm.hqm_supplier_inspector_rel.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.hand.hap.core.IRequest;
import com.hand.hap.core.ProxySelf;
import com.hand.hap.system.service.IBaseService;
import com.hand.hqm.hqm_supplier_inspector_rel.dto.SupplierInspectorRel;

public interface ISupplierInspectorRelService extends IBaseService<SupplierInspectorRel>, ProxySelf<ISupplierInspectorRelService>{

	List<SupplierInspectorRel> reSelect(IRequest requestContext,SupplierInspectorRel dto, int page, int pageSize);

	/**
	 * @description excel 数据维护
	 * @author tianmin.wang
	 * @date 2019年12月16日 
	 * @param requestCtx
	 * @param forModel
	 * @return
	 */
	List<SupplierInspectorRel> excelImport(IRequest requestCtx, MultipartFile forModel);

	/**
	 * @description
	 * @author tianmin.wang
	 * @date 2019年12月16日 
	 * @param requestContext
	 * @param dto
	 */
	void batchSave(IRequest requestContext, SupplierInspectorRel dto);

	/**
	 * @description
	 * @author tianmin.wang
	 * @date 2020年3月4日 
	 * @param requestCtx
	 * @param dto
	 * @return
	 */
	List<SupplierInspectorRel> reBatchUpdate(IRequest requestCtx, List<SupplierInspectorRel> dto);

	/**
	 * @description 记录历史的更新方式
	 * @author tianmin.wang
	 * @date 2020年3月4日 
	 * @param request
	 * @param t
	 * @return
	 */
	SupplierInspectorRel updateByPrimaryKeySelectiveRecord(IRequest request, SupplierInspectorRel t);

	/**
	 * @description 记录历史的新增方法
	 * @author tianmin.wang
	 * @date 2020年3月4日 
	 * @param request
	 * @param t
	 * @return
	 */
	SupplierInspectorRel insertSelectiveRecord(IRequest request, SupplierInspectorRel t);
}