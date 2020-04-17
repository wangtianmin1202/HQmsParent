package com.hand.hqm.hqm_qc_files.service;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.hand.hap.core.IRequest;
import com.hand.hap.core.ProxySelf;
import com.hand.hap.system.dto.ResponseData;
import com.hand.hap.system.service.IBaseService;
import com.hand.hqm.hqm_qc_files.dto.QcFiles;

public interface IQcFilesService extends IBaseService<QcFiles>, ProxySelf<IQcFilesService> {
	/**
	 * 质量文件管理 查询
	 * 
	 * @param requestContext
	 * @param dto
	 * @param pageSize
	 * @param page
	 * @return
	 */
	List<QcFiles> query(IRequest requestContext, QcFiles dto, int page, int pageSize);

	/**
	 * 上传文件
	 * 
	 * @param requestCtx
	 * @param request
	 * @return
	 * @throws IllegalStateException
	 * @throws IOException
	 */
	ResponseData fileUpload(IRequest requestCtx, HttpServletRequest request) throws IllegalStateException, IOException;
	/**
	 * 质量文件管理 删除
	 * @param dto
	 * @return
	 */
	int deleteDataAndFile(List<QcFiles> dto);
}