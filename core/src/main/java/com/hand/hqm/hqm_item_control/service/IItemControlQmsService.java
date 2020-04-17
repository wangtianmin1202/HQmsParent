package com.hand.hqm.hqm_item_control.service;

import java.util.List;

import com.hand.hap.core.IRequest;
import com.hand.hap.core.ProxySelf;
import com.hand.hap.system.service.IBaseService;
import com.hand.hqm.hqm_item_control.dto.ItemControlQms;

public interface IItemControlQmsService extends IBaseService<ItemControlQms>, ProxySelf<IItemControlQmsService>{

	/**
	 * @description 基础查询
	 * @author tianmin.wang
	 * @date 2019年12月17日 
	 * @param requestContext
	 * @param dto
	 * @param page
	 * @param pageSize
	 * @return
	 */
	List<ItemControlQms> reSelect(IRequest requestContext, ItemControlQms dto, int page, int pageSize);

	/**
	 * @description 批量更新
	 * @author tianmin.wang
	 * @date 2019年12月17日 
	 * @param request
	 * @param list
	 * @return
	 */
	List<ItemControlQms> reBatchUpdate(IRequest request, List<ItemControlQms> list);

	/**
	 * @description
	 * @author tianmin.wang
	 * @date 2019年12月17日 
	 * @param search
	 */
	void updateByUniqueKey(IRequest request,ItemControlQms search);

	/**
	 * @description
	 * @author tianmin.wang
	 * @date 2020年1月2日 
	 * @param requestCtx
	 * @param dto
	 * @return
	 */
	List<ItemControlQms> newBatchUpdate(IRequest requestCtx, List<ItemControlQms> dto);
	
	/**
	 * @description 记录历史的更新方式
	 * @author tianmin.wang
	 * @date 2020年3月4日 
	 * @param request
	 * @param t
	 * @return
	 */
	ItemControlQms updateByPrimaryKeySelectiveRecord(IRequest request, ItemControlQms t);

	/**
	 * @description 记录历史的新增方法
	 * @author tianmin.wang
	 * @date 2020年3月4日 
	 * @param request
	 * @param t
	 * @return
	 */
	ItemControlQms insertSelectiveRecord(IRequest request, ItemControlQms t);
	
}