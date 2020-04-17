package com.hand.hap.system.controllers;

import org.springframework.stereotype.Controller;
import com.hand.hap.system.controllers.BaseController;
import com.hand.hap.core.IRequest;
import com.hand.hap.system.dto.ResponseData;
import com.hand.hap.system.dto.UserLoginFail;
import com.hand.hap.system.service.IUserLoginFailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import javax.servlet.http.HttpServletRequest;
import org.springframework.validation.BindingResult;
import java.util.List;

    @Controller
    public class UserLoginFailController extends BaseController{

    @Autowired
    private IUserLoginFailService service;


    @RequestMapping(value = "/sys/user/login/fail/query")
    @ResponseBody
    public ResponseData query(UserLoginFail dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
        @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
        IRequest requestContext = createRequestContext(request);
        return new ResponseData(service.select(requestContext,dto,page,pageSize));
    }

    @RequestMapping(value = "/sys/user/login/fail/submit")
    @ResponseBody
    public ResponseData update(@RequestBody List<UserLoginFail> dto, BindingResult result, HttpServletRequest request){
        getValidator().validate(dto, result);
        if (result.hasErrors()) {
        ResponseData responseData = new ResponseData(false);
        responseData.setMessage(getErrorMessage(result, request));
        return responseData;
        }
        IRequest requestCtx = createRequestContext(request);
        return new ResponseData(service.batchUpdate(requestCtx, dto));
    }

    @RequestMapping(value = "/sys/user/login/fail/remove")
    @ResponseBody
    public ResponseData delete(HttpServletRequest request,@RequestBody List<UserLoginFail> dto){
        service.batchDelete(dto);
        return new ResponseData();
    }
    }