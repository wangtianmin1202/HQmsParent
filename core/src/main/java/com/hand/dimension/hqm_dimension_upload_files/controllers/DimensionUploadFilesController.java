package com.hand.dimension.hqm_dimension_upload_files.controllers;

import org.springframework.stereotype.Controller;
import com.hand.hap.system.controllers.BaseController;
import com.hand.hap.core.IRequest;
import com.hand.hap.system.dto.ResponseData;
import com.hand.dimension.hqm_dimension_upload_files.dto.DimensionUploadFiles;
import com.hand.dimension.hqm_dimension_upload_files.service.IDimensionUploadFilesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import javax.servlet.http.HttpServletRequest;
import org.springframework.validation.BindingResult;
import java.util.List;

    @Controller
    public class DimensionUploadFilesController extends BaseController{

    @Autowired
    private IDimensionUploadFilesService service;


    /**
     * 
     * @description 查询所有8d功能上传的文件
     * @author tianmin.wang
     * @date 2019年11月22日 
     * @param dto
     * @param page
     * @param pageSize
     * @param request
     * @return
     */
    @RequestMapping(value = "/hqm/8d/upload/files/query")
    @ResponseBody
    public ResponseData query(DimensionUploadFiles dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
        @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
        IRequest requestContext = createRequestContext(request);
        return new ResponseData(service.select(requestContext,dto,page,pageSize));
    }
    /**
     * 
     * @description 查询所有8d功能上传的文件
     * @author tianmin.wang
     * @date 2019年11月22日 
     * @param dto
     * @param page
     * @param pageSize
     * @param request
     * @return
     */
    @RequestMapping(value = "/hqm/8d/upload/files/select")
    @ResponseBody
    public ResponseData select(DimensionUploadFiles dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
        @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
        IRequest requestContext = createRequestContext(request);
        return new ResponseData(service.reSelect(requestContext,dto,page,pageSize));
    }
    /**
     * 
     * @description 提交修改
     * @author tianmin.wang
     * @date 2019年11月22日 
     * @param dto
     * @param result
     * @param request
     * @return
     */
    @RequestMapping(value = "/hqm/8d/upload/files/submit")
    @ResponseBody
    public ResponseData update(@RequestBody List<DimensionUploadFiles> dto, BindingResult result, HttpServletRequest request){
        getValidator().validate(dto, result);
        if (result.hasErrors()) {
        ResponseData responseData = new ResponseData(false);
        responseData.setMessage(getErrorMessage(result, request));
        return responseData;
        }
        IRequest requestCtx = createRequestContext(request);
        return new ResponseData(service.batchUpdate(requestCtx, dto));
    }

    /**
     * 
     * @description 删除已重写 同时删除fileUrl在linux的映射文件
     * @author tianmin.wang
     * @date 2019年11月22日 
     * @param request
     * @param dto
     * @return
     */
    @RequestMapping(value = "/hqm/8d/upload/files/remove")
    @ResponseBody
    public ResponseData delete(HttpServletRequest request,@RequestBody List<DimensionUploadFiles> dto){
        service.reBatchDelete(dto);
        return new ResponseData();
    }
    
    /**
	 * 
	 * @description 长期措施 预防措施文件上传
	 * @author tianmin.wang
	 * @date 2019年11月21日 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/hqm/8d/improving/prevention/fileupload/upload")
	@ResponseBody
	public ResponseData fileUpload(HttpServletRequest request) {
		ResponseData responseData = new ResponseData();
		IRequest requestCtx = createRequestContext(request);
		responseData = service.fileUpload(requestCtx,request);
        return responseData;
	
	}
    
    }