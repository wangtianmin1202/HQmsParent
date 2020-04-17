package com.hand.hqm.file_type.controllers;

import org.springframework.stereotype.Controller;
import com.hand.hap.system.controllers.BaseController;
import com.hand.hap.core.IRequest;
import com.hand.hap.system.dto.ResponseData;
import com.hand.hqm.file_type.dto.FileType;
import com.hand.hqm.file_type.service.IFileTypeService;
import com.hand.wfl.util.DropDownListDto;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import javax.servlet.http.HttpServletRequest;
import org.springframework.validation.BindingResult;
import java.util.List;

    @Controller
    public class FileTypeController extends BaseController{

    @Autowired
    private IFileTypeService service;


    @RequestMapping(value = "/file/type/query")
    @ResponseBody
    public ResponseData query(FileType dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
        @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
        IRequest requestContext = createRequestContext(request);
        return new ResponseData(service.select(requestContext,dto,page,pageSize));
    }

    @RequestMapping(value = "/file/type/submit")
    @ResponseBody
    public ResponseData update(@RequestBody List<FileType> dto, BindingResult result, HttpServletRequest request){
        getValidator().validate(dto, result);
        if (result.hasErrors()) {
        ResponseData responseData = new ResponseData(false);
        responseData.setMessage(getErrorMessage(result, request));
        return responseData;
        }
        IRequest requestCtx = createRequestContext(request);
        return new ResponseData(service.batchUpdate(requestCtx, dto));
    }

    @RequestMapping(value = "/file/type/remove")
    @ResponseBody
    public ResponseData delete(HttpServletRequest request,@RequestBody List<FileType> dto){
        service.batchDelete(dto);
        return new ResponseData();
    }
    
    /**
     * 
     * @Description:界面主查询
     * @param dto
     * @param page
     * @param pageSize
     * @param request
     * @return
     */
    @RequestMapping(value = "/file/type/queryByCondition")
    @ResponseBody
    public ResponseData queryByCondition(FileType dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
        @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
        IRequest requestContext = createRequestContext(request);
        return new ResponseData(service.queryByCondition(requestContext,dto,page,pageSize));
    }
    
    @RequestMapping(value = "/file/type/add")
    @ResponseBody
    public ResponseData add(@RequestBody FileType dto, HttpServletRequest request){
        IRequest requestCtx = createRequestContext(request);
        return service.add(requestCtx, dto);
    }
    
    @RequestMapping(value = "/file/type/queryFileTypeName")
    @ResponseBody
    public List<DropDownListDto> queryFileTypeName(@RequestParam(required = false) String fileTypeName){
    	return service.queryFileTypeName(fileTypeName);
    }
    }