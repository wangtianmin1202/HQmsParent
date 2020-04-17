package com.hand.itf.itf_function_info.controllers;

import io.netty.util.internal.chmv8.ConcurrentHashMapV8;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import com.hand.hap.system.controllers.BaseController;
import com.hand.hap.core.IRequest;
import com.hand.hap.system.dto.ResponseData;
import com.hand.itf.itf_function_info.dto.FunctionInfo;
import com.hand.itf.itf_function_info.service.IFunctionInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

import org.springframework.validation.BindingResult;

import java.util.ArrayList;
import java.util.List;

@Controller
public class FunctionInfoController extends BaseController {

    @Autowired
    private IFunctionInfoService service;


    @RequestMapping(value = "/itf/function/info/query")
    @ResponseBody
    public ResponseData query(FunctionInfo dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
                              @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {  //@RequestParam String individuationSql
        IRequest requestContext = createRequestContext(request);
        List<FunctionInfo> functionInfoList = new ArrayList<>();
        if (StringUtils.isEmpty(dto.getIndividuationSql()) || dto.getIndividuationSql().equals("undefined")) {
            functionInfoList = service.select(requestContext, dto, page, pageSize);
        } else {
            functionInfoList = service.individuationQuery(requestContext, page, pageSize, dto.getIndividuationSql());
        }
        return new ResponseData(functionInfoList);
    }

    @RequestMapping(value = "/itf/function/info/submit")
    @ResponseBody
    public ResponseData update(@RequestBody List<FunctionInfo> dto, BindingResult result, HttpServletRequest request) {
        getValidator().validate(dto, result);
        if (result.hasErrors()) {
            ResponseData responseData = new ResponseData(false);
            responseData.setMessage(getErrorMessage(result, request));
            return responseData;
        }
        IRequest requestCtx = createRequestContext(request);
        return new ResponseData(service.batchUpdate(requestCtx, dto));
    }

    @RequestMapping(value = "/itf/function/info/remove")
    @ResponseBody
    public ResponseData delete(HttpServletRequest request, @RequestBody List<FunctionInfo> dto) {
        service.batchDelete(dto);
        return new ResponseData();
    }

    @RequestMapping(value = "/itf/function/info/individuation/query")
    @ResponseBody
    public ResponseData individuationQuery(HttpServletRequest request, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
                                           @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, String individuationSql) {
        IRequest requestContext = createRequestContext(request);
        List<FunctionInfo> functionInfoList = service.individuationQuery(requestContext, page, pageSize, individuationSql);
        return new ResponseData(functionInfoList);
    }
}