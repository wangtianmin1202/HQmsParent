package com.hand.hqm.hqm_platform_program.service;

import java.util.List;

import com.hand.hap.core.IRequest;
import com.hand.hap.core.ProxySelf;
import com.hand.hap.system.service.IBaseService;
import com.hand.hqm.hqm_platform_program.dto.PlatformProgram;

public interface IPlatformProgramService extends IBaseService<PlatformProgram>, ProxySelf<IPlatformProgramService>{

	/**
	 * @description
	 * @author tianmin.wang
	 * @date 2019年12月20日 
	 * @param requestContext
	 * @param dto
	 * @param page
	 * @param pageSize
	 * @return
	 */
	List<PlatformProgram> reSelect(IRequest requestContext, PlatformProgram dto, int page, int pageSize);

	
	/**
	 * @description
	 * @author tianmin.wang
	 * @date 2020年3月4日 
	 * @param requestCtx
	 * @param dto
	 * @return
	 */
	List<PlatformProgram> reBatchUpdate(IRequest requestCtx, List<PlatformProgram> dto);

	/**
	 * @description 记录历史的更新方式
	 * @author tianmin.wang
	 * @date 2020年3月4日 
	 * @param request
	 * @param t
	 * @return
	 */
	PlatformProgram updateByPrimaryKeySelectiveRecord(IRequest request, PlatformProgram t);

	/**
	 * @description 记录历史的新增方法
	 * @author tianmin.wang
	 * @date 2020年3月4日 
	 * @param request
	 * @param t
	 * @return
	 */
	PlatformProgram insertSelectiveRecord(IRequest request, PlatformProgram t);
}