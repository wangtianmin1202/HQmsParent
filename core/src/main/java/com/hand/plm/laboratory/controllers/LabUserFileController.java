package com.hand.plm.laboratory.controllers;

import org.springframework.stereotype.Controller;
import com.hand.hap.system.controllers.BaseController;
import com.hand.hap.core.IRequest;
import com.hand.hap.system.dto.ResponseData;
import com.hand.plm.laboratory.dto.LabUserFile;
import com.hand.plm.laboratory.service.ILabUserFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import javax.servlet.http.HttpServletRequest;
import org.springframework.validation.BindingResult;
import java.util.List;

    @Controller
    public class LabUserFileController extends BaseController{

    @Autowired
    private ILabUserFileService service;


    @RequestMapping(value = "/plm/lab/user/file/query")
    @ResponseBody
    public ResponseData query(LabUserFile dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
        @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
        IRequest requestContext = createRequestContext(request);
        return new ResponseData(service.select(requestContext,dto,page,pageSize));
    }

    @RequestMapping(value = "/plm/lab/user/file/submit")
    @ResponseBody
    public ResponseData update(@RequestBody List<LabUserFile> dto, BindingResult result, HttpServletRequest request){
        getValidator().validate(dto, result);
        if (result.hasErrors()) {
        ResponseData responseData = new ResponseData(false);
        responseData.setMessage(getErrorMessage(result, request));
        return responseData;
        }
        IRequest requestCtx = createRequestContext(request);
        return new ResponseData(service.batchUpdate(requestCtx, dto));
    }

    @RequestMapping(value = "/plm/lab/user/file/remove")
    @ResponseBody
    public ResponseData delete(HttpServletRequest request,@RequestBody LabUserFile dto){
//        service.batchDelete(dto);
    	IRequest requestCtx = createRequestContext(request);
    	List<LabUserFile> dtoList = service.select(requestCtx, dto, 1, 100);
    	for(LabUserFile file:dtoList) {
    		
        	service.deleteByPrimaryKey(file);
        	
    	}
    	
        return service.deleteFile(dto.getFilePath());
    }
    
    /**
     * 
     * @Description:上传文件，只上传到服务器，不在前台展示
     * @param request
     * @return
     */
    @RequestMapping(value = "/plm/lab/user/file/upload")
	@ResponseBody
	public ResponseData activityUpload(HttpServletRequest request,@RequestParam Float labUserId) {
		IRequest requestCtx = createRequestContext(request);
        return service.fileUpload(requestCtx,request,labUserId);
	}
    
    
    }