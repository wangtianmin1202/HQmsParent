package com.hand.hqm.hqm_platform_program.controllers;

import org.springframework.stereotype.Controller;
import com.hand.hap.system.controllers.BaseController;
import com.hand.hap.core.IRequest;
import com.hand.hap.system.dto.ResponseData;
import com.hand.hqm.hqm_platform_program.dto.PlatformProgram;
import com.hand.hqm.hqm_platform_program.service.IPlatformProgramService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import javax.servlet.http.HttpServletRequest;
import org.springframework.validation.BindingResult;
import java.util.List;

    @Controller
    public class PlatformProgramController extends BaseController{

    @Autowired
    private IPlatformProgramService service;


    @RequestMapping(value = "/hqm/platform/program/query")
    @ResponseBody
    public ResponseData query(PlatformProgram dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
        @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
        IRequest requestContext = createRequestContext(request);
        return new ResponseData(service.reSelect(requestContext,dto,page,pageSize));
    }

    @RequestMapping(value = "/hqm/platform/program/submit")
    @ResponseBody
    public ResponseData update(@RequestBody List<PlatformProgram> dto, BindingResult result, HttpServletRequest request){
        getValidator().validate(dto, result);
        if (result.hasErrors()) {
        ResponseData responseData = new ResponseData(false);
        responseData.setMessage(getErrorMessage(result, request));
        return responseData;
        }
        IRequest requestCtx = createRequestContext(request);
        return new ResponseData(service.reBatchUpdate(requestCtx, dto));
    }

    @RequestMapping(value = "/hqm/platform/program/remove")
    @ResponseBody
    public ResponseData delete(HttpServletRequest request,@RequestBody List<PlatformProgram> dto){
        service.batchDelete(dto);
        return new ResponseData();
    }
    }