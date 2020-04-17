package com.hand.spc.hqm_d_order.controllers;

import org.springframework.stereotype.Controller;
import com.hand.hap.system.controllers.BaseController;
import com.hand.hap.core.IRequest;
import com.hand.hap.system.dto.ResponseData;
import com.hand.spc.hqm_d_order.dto.EightDOrder;
import com.hand.spc.hqm_d_order.service.IEightDOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import javax.servlet.http.HttpServletRequest;
import org.springframework.validation.BindingResult;
import java.util.List;

    @Controller
    public class EightDOrderController extends BaseController{

    @Autowired
    private IEightDOrderService service;


    @RequestMapping(value = "/spc/hqm/8d/order/query")
    @ResponseBody
    public ResponseData query(EightDOrder dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
        @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
        IRequest requestContext = createRequestContext(request);
        return new ResponseData(service.select(requestContext,dto,page,pageSize));
    }

    @RequestMapping(value = "/spc/hqm/8d/order/submit")
    @ResponseBody
    public ResponseData update(@RequestBody List<EightDOrder> dto, BindingResult result, HttpServletRequest request){
        getValidator().validate(dto, result);
        if (result.hasErrors()) {
        ResponseData responseData = new ResponseData(false);
        responseData.setMessage(getErrorMessage(result, request));
        return responseData;
        }
        IRequest requestCtx = createRequestContext(request);
        return new ResponseData(service.batchUpdate(requestCtx, dto));
    }

    @RequestMapping(value = "/spc/hqm/8d/order/remove")
    @ResponseBody
    public ResponseData delete(HttpServletRequest request,@RequestBody List<EightDOrder> dto){
        service.batchDelete(dto);
        return new ResponseData();
    }
    }