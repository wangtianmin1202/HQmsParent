package com.hand.srm.srm_itf_invoke_outbound.controllers;

import org.springframework.stereotype.Controller;
import com.hand.hap.system.controllers.BaseController;
import com.hand.hap.core.IRequest;
import com.hand.hap.system.dto.ResponseData;
import com.hand.srm.srm_itf_invoke_outbound.dto.ItfInvokeOutbound;
import com.hand.srm.srm_itf_invoke_outbound.service.IItfInvokeOutboundService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import javax.servlet.http.HttpServletRequest;
import org.springframework.validation.BindingResult;
import java.util.List;

    @Controller
    public class ItfInvokeOutboundController extends BaseController{

    @Autowired
    private IItfInvokeOutboundService service;


    @RequestMapping(value = "/srm/itf/invoke/outbound/query")
    @ResponseBody
    public ResponseData query(ItfInvokeOutbound dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
        @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
        IRequest requestContext = createRequestContext(request);
        return new ResponseData(service.select(requestContext,dto,page,pageSize));
    }

    @RequestMapping(value = "/srm/itf/invoke/outbound/submit")
    @ResponseBody
    public ResponseData update(@RequestBody List<ItfInvokeOutbound> dto, BindingResult result, HttpServletRequest request){
        getValidator().validate(dto, result);
        if (result.hasErrors()) {
        ResponseData responseData = new ResponseData(false);
        responseData.setMessage(getErrorMessage(result, request));
        return responseData;
        }
        IRequest requestCtx = createRequestContext(request);
        return new ResponseData(service.batchUpdate(requestCtx, dto));
    }

    @RequestMapping(value = "/srm/itf/invoke/outbound/remove")
    @ResponseBody
    public ResponseData delete(HttpServletRequest request,@RequestBody List<ItfInvokeOutbound> dto){
        service.batchDelete(dto);
        return new ResponseData();
    }
    }