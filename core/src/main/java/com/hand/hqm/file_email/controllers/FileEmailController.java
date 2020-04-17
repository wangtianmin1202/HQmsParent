package com.hand.hqm.file_email.controllers;

import org.springframework.stereotype.Controller;
import com.hand.hap.system.controllers.BaseController;
import com.hand.hap.core.IRequest;
import com.hand.hap.system.dto.ResponseData;
import com.hand.hqm.file_email.dto.FileEmail;
import com.hand.hqm.file_email.service.IFileEmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import javax.servlet.http.HttpServletRequest;
import org.springframework.validation.BindingResult;
import java.util.List;

    @Controller
    public class FileEmailController extends BaseController{

    @Autowired
    private IFileEmailService service;


    @RequestMapping(value = "/file/email/query")
    @ResponseBody
    public ResponseData query(FileEmail dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
        @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
        IRequest requestContext = createRequestContext(request);
        return new ResponseData(service.select(requestContext,dto,page,pageSize));
    }

    @RequestMapping(value = "/file/email/submit")
    @ResponseBody
    public ResponseData update(@RequestBody List<FileEmail> dto, BindingResult result, HttpServletRequest request){
        getValidator().validate(dto, result);
        if (result.hasErrors()) {
        ResponseData responseData = new ResponseData(false);
        responseData.setMessage(getErrorMessage(result, request));
        return responseData;
        }
        IRequest requestCtx = createRequestContext(request);
        return new ResponseData(service.batchUpdate(requestCtx, dto));
    }

    @RequestMapping(value = "/file/email/remove")
    @ResponseBody
    public ResponseData delete(HttpServletRequest request,@RequestBody List<FileEmail> dto){
        service.batchDelete(dto);
        return new ResponseData();
    }
    
    /**
     * 
     * @Description:邮件维护界面主查询
     * @param dto
     * @param page
     * @param pageSize
     * @param request
     * @return
     */
    @RequestMapping(value = "/file/email/queryByCondition")
    @ResponseBody
    public ResponseData queryByCondition(FileEmail dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
        @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
        IRequest requestContext = createRequestContext(request);
        return new ResponseData(service.queryByCondition(requestContext,dto,page,pageSize));
    }
    }