package com.hand.itf.itf_supplier_info.controllers;

import org.springframework.stereotype.Controller;
import com.hand.hap.system.controllers.BaseController;
import com.hand.hap.core.IRequest;
import com.hand.hap.system.dto.ResponseData;
import com.hand.itf.itf_supplier_info.dto.SupplierInfo;
import com.hand.itf.itf_supplier_info.service.ISupplierInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import javax.servlet.http.HttpServletRequest;
import org.springframework.validation.BindingResult;
import java.util.List;

    @Controller
    public class SupplierInfoController extends BaseController{

    @Autowired
    private ISupplierInfoService service;


    @RequestMapping(value = "/itf/supplier/info/query")
    @ResponseBody
    public ResponseData query(SupplierInfo dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
        @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
        IRequest requestContext = createRequestContext(request);
        return new ResponseData(service.select(requestContext,dto,page,pageSize));
    }

    @RequestMapping(value = "/itf/supplier/info/submit")
    @ResponseBody
    public ResponseData update(@RequestBody List<SupplierInfo> dto, BindingResult result, HttpServletRequest request){
        getValidator().validate(dto, result);
        if (result.hasErrors()) {
        ResponseData responseData = new ResponseData(false);
        responseData.setMessage(getErrorMessage(result, request));
        return responseData;
        }
        IRequest requestCtx = createRequestContext(request);
        return new ResponseData(service.batchUpdate(requestCtx, dto));
    }

    @RequestMapping(value = "/itf/supplier/info/remove")
    @ResponseBody
    public ResponseData delete(HttpServletRequest request,@RequestBody List<SupplierInfo> dto){
        service.batchDelete(dto);
        return new ResponseData();
    }
    }