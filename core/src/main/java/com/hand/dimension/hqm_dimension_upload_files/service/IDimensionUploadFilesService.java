package com.hand.dimension.hqm_dimension_upload_files.service;

import com.hand.hap.core.IRequest;
import com.hand.hap.core.ProxySelf;
import com.hand.hap.system.dto.ResponseData;
import com.hand.hap.system.service.IBaseService;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.hand.dimension.hqm_dimension_upload_files.dto.DimensionUploadFiles;

public interface IDimensionUploadFilesService extends IBaseService<DimensionUploadFiles>, ProxySelf<IDimensionUploadFilesService>{

	/**
	 * 
	 * @description 如果数据删除成功 那么会把linux的文件清除掉
	 * @author tianmin.wang
	 * @date 2019年11月22日 
	 * @param dto
	 * @return
	 */
	int reBatchDelete(List<DimensionUploadFiles> dto);

	/**
	 * @description 文件上传
	 * @author tianmin.wang
	 * @date 2019年11月28日 
	 * @param requestCtx
	 * @param request
	 * @return
	 */
	ResponseData fileUpload(IRequest requestCtx, HttpServletRequest request);

	/**
	 * @description 长期措施 预防措施查询
	 * @author tianmin.wang
	 * @date 2019年11月28日 
	 * @param requestContext
	 * @param dto
	 * @param page
	 * @param pageSize
	 * @return
	 */
	List<DimensionUploadFiles> reSelect(IRequest requestContext, DimensionUploadFiles dto, int page, int pageSize);

}