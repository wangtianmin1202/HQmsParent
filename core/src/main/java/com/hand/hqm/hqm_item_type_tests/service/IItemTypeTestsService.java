package com.hand.hqm.hqm_item_type_tests.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.hand.hap.core.IRequest;
import com.hand.hap.core.ProxySelf;
import com.hand.hap.system.service.IBaseService;
import com.hand.hqm.hqm_item_type_tests.dto.ItemTypeTests;

public interface IItemTypeTestsService extends IBaseService<ItemTypeTests>, ProxySelf<IItemTypeTestsService>{

	/**
	 * @description 查询 接口方法
	 * @author tianmin.wang
	 * @date 2019年12月17日 
	 * @param requestContext
	 * @param dto
	 * @param page
	 * @param pageSize
	 * @return
	 */
	List<ItemTypeTests> reSelect(IRequest requestContext, ItemTypeTests dto, int page, int pageSize);

	/**
	 * @description excel数据导入
	 * @author tianmin.wang
	 * @date 2019年12月17日 
	 * @param requestCtx
	 * @param forModel
	 * @return
	 */
	 List<ItemTypeTests> excelImport(IRequest requestCtx, MultipartFile forModel);

	/**
	 * @description
	 * @author tianmin.wang
	 * @date 2020年3月3日 
	 * @param requestCtx
	 * @param dto
	 * @return
	 */
	List<ItemTypeTests> reBatchUpdate(IRequest requestCtx, List<ItemTypeTests> dto);

	/**
	 * @description
	 * @author tianmin.wang
	 * @date 2020年3月21日 
	 * @param request
	 * @param t
	 * @return
	 */
	ItemTypeTests insertSelectiveRecord(IRequest request, ItemTypeTests t);

	/**
	 * @description
	 * @author tianmin.wang
	 * @date 2020年3月21日 
	 * @param request
	 * @param t
	 * @return
	 */
	ItemTypeTests updateByPrimaryKeySelectiveRecord(IRequest request, ItemTypeTests t);

}