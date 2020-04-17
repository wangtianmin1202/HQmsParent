package com.hand.npi.npi_technology.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.hand.hap.core.IRequest;
import com.hand.hap.core.ProxySelf;
import com.hand.hap.system.dto.ResponseData;
import com.hand.hap.system.service.IBaseService;
import com.hand.hqm.file_upload.dto.FileUpload;
import com.hand.npi.npi_technology.dto.InvalidCause;

public interface IInvalidCauseService extends IBaseService<InvalidCause>, ProxySelf<IInvalidCauseService>{
	
	ResponseData addNewInvalidCause(InvalidCause dto, IRequest requestCtx, HttpServletRequest request);
	
	String getInvalidCauseNumber(String invalidCauseNumber);
	
	void deleteInvalidCause(InvalidCause dto) throws Exception;
	
	List<InvalidCause> queryInvalidCauseList(InvalidCause dto);
	
	/**
	 * @Description:
	 * @param requestCtx
	 * @param dto
	 * @return
	 */
	void createProcessInstance(IRequest requestCtx, InvalidCause dto);

}