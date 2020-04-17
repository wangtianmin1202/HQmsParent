package com.hand.itf.itf_purchase_order_header.controllers;

import org.springframework.stereotype.Controller;
import com.hand.hap.system.controllers.BaseController;
import com.hand.hap.core.IRequest;
import com.hand.hap.system.dto.ResponseData;
import com.hand.itf.itf_purchase_order_header.dto.PurchaseOrderHeader;
import com.hand.itf.itf_purchase_order_header.service.IPurchaseOrderHeaderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import javax.servlet.http.HttpServletRequest;
import org.springframework.validation.BindingResult;
import java.util.List;

    @Controller
    public class PurchaseOrderHeaderController extends BaseController{

    @Autowired
    private IPurchaseOrderHeaderService service;


    @RequestMapping(value = "/itf/purchase/order/header/query")
    @ResponseBody
    public ResponseData query(PurchaseOrderHeader dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
        @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
        IRequest requestContext = createRequestContext(request);
        return new ResponseData(service.select(requestContext,dto,page,pageSize));
    }

    @RequestMapping(value = "/itf/purchase/order/header/submit")
    @ResponseBody
    public ResponseData update(@RequestBody List<PurchaseOrderHeader> dto, BindingResult result, HttpServletRequest request){
        getValidator().validate(dto, result);
        if (result.hasErrors()) {
        ResponseData responseData = new ResponseData(false);
        responseData.setMessage(getErrorMessage(result, request));
        return responseData;
        }
        IRequest requestCtx = createRequestContext(request);
        return new ResponseData(service.batchUpdate(requestCtx, dto));
    }

    @RequestMapping(value = "/itf/purchase/order/header/remove")
    @ResponseBody
    public ResponseData delete(HttpServletRequest request,@RequestBody List<PurchaseOrderHeader> dto){
        service.batchDelete(dto);
        return new ResponseData();
    }
    }