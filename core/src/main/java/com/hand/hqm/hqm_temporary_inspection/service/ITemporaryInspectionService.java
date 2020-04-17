package com.hand.hqm.hqm_temporary_inspection.service;

import java.util.List;

import com.hand.hap.core.IRequest;
import com.hand.hap.core.ProxySelf;
import com.hand.hap.system.service.IBaseService;
import com.hand.hqm.hqm_temporary_inspection.dto.TemporaryInspection;

public interface ITemporaryInspectionService extends IBaseService<TemporaryInspection>, ProxySelf<ITemporaryInspectionService>{

	/**
	 * @description 临时检验单创建临时界面的新建保存
	 * @author tianmin.wang
	 * @date 2019年12月10日 
	 * @param requestContext
	 * @param dto
	 * @return
	 */
	void addNew(IRequest requestContext, TemporaryInspection dto);

	/**
	 * @description
	 * @author tianmin.wang
	 * @date 2019年12月10日 
	 * @param requestContext
	 * @param dto
	 * @param page
	 * @param pageSize
	 * @return
	 */
	List<TemporaryInspection> reSelect(IRequest requestContext, TemporaryInspection dto, int page, int pageSize);

	/**
	 * @description
	 * @author tianmin.wang
	 * @date 2019年12月10日 
	 * @param requestCtx
	 * @param dto
	 * @return
	 */
	void batchIssue(IRequest requestCtx, List<TemporaryInspection> dto);

	/**
	 * @description
	 * @author tianmin.wang
	 * @date 2019年12月10日 
	 * @param requestCtx
	 * @param dto
	 */
	void batchDisable(IRequest requestCtx, List<TemporaryInspection> dto);

}