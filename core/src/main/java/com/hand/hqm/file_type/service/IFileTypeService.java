package com.hand.hqm.file_type.service;

import java.util.List;

import com.hand.hap.core.IRequest;
import com.hand.hap.core.ProxySelf;
import com.hand.hap.system.dto.ResponseData;
import com.hand.hap.system.service.IBaseService;
import com.hand.hqm.file_type.dto.FileType;
import com.hand.wfl.util.DropDownListDto;

public interface IFileTypeService extends IBaseService<FileType>, ProxySelf<IFileTypeService>{

	/**
	 * @Description:
	 * @param requestContext
	 * @param dto
	 * @param page
	 * @param pageSize
	 * @return
	 */
	List<FileType> queryByCondition(IRequest requestContext, FileType dto, int page, int pageSize);

	/**
	 * @Description:
	 * @param requestCtx
	 * @param dto
	 * @return
	 */
	ResponseData add(IRequest requestCtx, FileType dto);

	/**
	 * @Description:
	 * @param fileTypeName
	 * @return
	 */
	List<DropDownListDto> queryFileTypeName(String fileTypeName);

}