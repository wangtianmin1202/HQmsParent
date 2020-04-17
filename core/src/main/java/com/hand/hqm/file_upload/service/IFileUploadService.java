package com.hand.hqm.file_upload.service;

import java.util.List;

import com.hand.hap.activiti.dto.TaskActionRequestExt;
import com.hand.hap.activiti.exception.TaskActionException;
import com.hand.hap.core.IRequest;
import com.hand.hap.core.ProxySelf;
import com.hand.hap.system.dto.ResponseData;
import com.hand.hap.system.service.IBaseService;
import com.hand.hqm.file_upload.dto.FileUpload;

public interface IFileUploadService extends IBaseService<FileUpload>, ProxySelf<IFileUploadService>{

	/**
	 * @Description:
	 * @param requestContext
	 * @param dto
	 * @param page
	 * @param pageSize
	 * @return
	 */
	List<FileUpload> queryByCondition(IRequest requestContext, FileUpload dto, int page, int pageSize);

	/**
	 * @Description:
	 * @param requestCtx
	 * @param dto
	 * @return
	 */
	List<FileUpload> add(IRequest requestCtx, FileUpload dto);

	/**
	 * @Description:
	 * @param requestCtx
	 * @param dto
	 * @return
	 */
	void createProcessInstance(IRequest requestCtx, FileUpload dto);

	/**
	 * @Description:
	 * @param requestCtx
	 * @param dto
	 * @param actionRequest
	 * @param processInstanceId
	 * @param flowNum
	 * @return
	 * @throws TaskActionException 
	 */
	ResponseData approveProcess(IRequest requestCtx, FileUpload dto, TaskActionRequestExt actionRequest,
			String processInstanceId, Integer flowNum) throws TaskActionException;

}