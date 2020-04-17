package com.hand.hqm.hqm_pqc_warning.service;

import java.util.List;

import com.hand.hap.core.IRequest;
import com.hand.hap.core.ProxySelf;
import com.hand.hap.system.service.IBaseService;
import com.hand.hqm.hqm_item_control.dto.ItemControlQms;
import com.hand.hqm.hqm_pqc_warning.dto.PqcWarning;

public interface IPqcWarningService extends IBaseService<PqcWarning>, ProxySelf<IPqcWarningService>{

	/**
	 * @description
	 * @author tianmin.wang
	 * @date 2019年12月26日 
	 * @param requestContext
	 * @param dto
	 * @param page
	 * @param pageSize
	 * @return
	 */
	List<PqcWarning> reSelect(IRequest requestContext, PqcWarning dto, int page, int pageSize);

	/**
	 * @description
	 * @author tianmin.wang
	 * @date 2020年1月10日 
	 * @param requestCtx
	 * @param dto
	 * @return
	 */
	List<PqcWarning> reBatchUpdate(IRequest requestCtx, List<PqcWarning> dto);

	
	/**
	 * @description 记录历史的更新方式
	 * @author tianmin.wang
	 * @date 2020年3月4日 
	 * @param request
	 * @param t
	 * @return
	 */
	PqcWarning updateByPrimaryKeySelectiveRecord(IRequest request, PqcWarning t);

	/**
	 * @description 记录历史的新增方法
	 * @author tianmin.wang
	 * @date 2020年3月4日 
	 * @param request
	 * @param t
	 * @return
	 */
	PqcWarning insertSelectiveRecord(IRequest request, PqcWarning t);
}