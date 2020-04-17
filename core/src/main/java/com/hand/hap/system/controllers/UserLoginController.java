package com.hand.hap.system.controllers;

import org.springframework.stereotype.Controller;

import com.hand.hap.core.IRequest;
import com.hand.hap.system.dto.ResponseData;
import com.hand.hap.system.dto.UserLogin;
import com.hand.hap.system.service.IUserLoginService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import javax.servlet.http.HttpServletRequest;
import org.springframework.validation.BindingResult;
import java.util.List;

    @Controller
    public class UserLoginController extends BaseController{

    @Autowired
    private IUserLoginService service;


    @RequestMapping(value = "/sys/user/login/query")
    @ResponseBody
    public ResponseData query(UserLogin dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
        @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
        IRequest requestContext = createRequestContext(request);
        return new ResponseData(service.query(requestContext,dto,page,pageSize));
    }

    @RequestMapping(value = "/sys/user/login/submit")
    @ResponseBody
    public ResponseData update(@RequestBody List<UserLogin> dto, BindingResult result, HttpServletRequest request){
        getValidator().validate(dto, result);
        if (result.hasErrors()) {
        ResponseData responseData = new ResponseData(false);
        responseData.setMessage(getErrorMessage(result, request));
        return responseData;
        }
        IRequest requestCtx = createRequestContext(request);
        return new ResponseData(service.batchUpdate(requestCtx, dto));
    }

    @RequestMapping(value = "/sys/user/login/remove")
    @ResponseBody
    public ResponseData delete(HttpServletRequest request,@RequestBody List<UserLogin> dto){
        service.batchDelete(dto);
        return new ResponseData();
    }
    }