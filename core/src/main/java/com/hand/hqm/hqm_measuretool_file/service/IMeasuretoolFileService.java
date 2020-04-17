package com.hand.hqm.hqm_measuretool_file.service;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.hand.hap.core.IRequest;
import com.hand.hap.core.ProxySelf;
import com.hand.hap.system.dto.ResponseData;
import com.hand.hap.system.service.IBaseService;
import com.hand.hqm.hqm_measuretool_file.dto.MeasuretoolFile;

public interface IMeasuretoolFileService extends IBaseService<MeasuretoolFile>, ProxySelf<IMeasuretoolFileService>{
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
	 * 文件 查询
	 * 
	 * @param requestContext
	 * @param dto
	 * @param pageSize
	 * @param page
	 * @return
	 */
	List<MeasuretoolFile> query(IRequest requestContext, MeasuretoolFile dto, int page, int pageSize);
	/**
	 * 文件删除
	 * @param dto
	 * @return
	 */
	int deleteDataAndFile(List<MeasuretoolFile> dto);
}