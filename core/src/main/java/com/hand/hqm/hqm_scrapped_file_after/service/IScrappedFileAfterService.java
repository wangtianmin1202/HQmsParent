package com.hand.hqm.hqm_scrapped_file_after.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.hand.hap.core.IRequest;
import com.hand.hap.core.ProxySelf;
import com.hand.hap.system.service.IBaseService;
import com.hand.hqm.hqm_scrapped_file_after.dto.ScrappedFileAfter;

public interface IScrappedFileAfterService extends IBaseService<ScrappedFileAfter>, ProxySelf<IScrappedFileAfterService>{

	/**
	 * 文件上载
	 * @description
	 * @author tianmin.wang
	 * @date 2019年11月22日 
	 * @param request
	 * @return
	 */
	List<ScrappedFileAfter> fileUp(HttpServletRequest request);

	/**
	 * 删除
	 * @description
	 * @author tianmin.wang
	 * @date 2019年11月22日 
	 * @param dto
	 * @return
	 */
	int reBatchDelete(List<ScrappedFileAfter> dto);

	
	/**
	 * 查询
	 * @description
	 * @author tianmin.wang
	 * @date 2019年11月22日 
	 * @param requestContext
	 * @param dto
	 * @param page
	 * @param pageSize
	 * @return
	 */
	List<ScrappedFileAfter> reSelect(IRequest requestContext, ScrappedFileAfter dto, int page, int pageSize);

}