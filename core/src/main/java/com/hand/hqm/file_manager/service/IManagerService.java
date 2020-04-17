package com.hand.hqm.file_manager.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.hand.hap.core.IRequest;
import com.hand.hap.core.ProxySelf;
import com.hand.hap.system.dto.ResponseData;
import com.hand.hap.system.service.IBaseService;
import com.hand.hqm.file_classify.dto.Classify;
import com.hand.hqm.file_classify.dto.MenuItem;
import com.hand.hqm.file_manager.dto.Manager;
import com.hand.hqm.file_upload.dto.FileUpload;
import com.hand.hqm.hqm_dfmea_detail.dto.DfmeaDetail;

public interface IManagerService extends IBaseService<Manager>, ProxySelf<IManagerService>{

	 /**
    * @Author ruifu.jiang
    * @Description 新增
    * @Date 11:40 2019/8/26
    * @Param [requestContext, dto]
    */
	ResponseData addNew(Manager dto,IRequest requestCtx, HttpServletRequest request);
	/**
	    * @Author ruifu.jiang
	    * @Description 数据查询
	    * @Date 11:40 2019/8/26
	    * @Param [requestContext, dto，page,pageSize]
	    */
	
    List<Manager>  myselect(IRequest requestContext,Manager dto,int page, int pageSize); 
    /**
	    * @Author ruifu.jiang
	    * @Description 文件上传
	    * @Date 11:40 2019/8/26
	    * @Param [requestCtx, request]
	    */
    ResponseData fileUpload(IRequest requestCtx, HttpServletRequest request);
/*    List<MenuItem> queryTreeData(IRequest requestContext, Classify dto);
*/
	/**
	 * @Description:
	 * @param requestCtx
	 * @param request
	 * @return
	 */
	ResponseData activityUpload(IRequest requestCtx, HttpServletRequest request);
	/**
	 * @Description:
	 * @param requestContext
	 * @param dto
	 * @return
	 */
	List<Manager> queryFileUpload(IRequest requestContext, FileUpload dto);}      