package com.hand.hqm.file_email.service;

import java.util.List;

import com.hand.hap.core.IRequest;
import com.hand.hap.core.ProxySelf;
import com.hand.hap.system.service.IBaseService;
import com.hand.hqm.file_email.dto.FileEmail;

public interface IFileEmailService extends IBaseService<FileEmail>, ProxySelf<IFileEmailService>{

	/**
	 * @Description:
	 * @param requestContext
	 * @param dto
	 * @param page
	 * @param pageSize
	 * @return
	 */
	List<FileEmail> queryByCondition(IRequest requestContext, FileEmail dto, int page, int pageSize);

}