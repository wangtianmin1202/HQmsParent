package com.hand.hqm.hqm_online_sku_rule.controllers;

import org.springframework.stereotype.Controller;
import com.hand.hap.system.controllers.BaseController;
import com.hand.hap.core.IRequest;
import com.hand.hap.system.dto.ResponseData;
import com.hand.hqm.hqm_online_sku_rule.dto.OnlineSkuRule;
import com.hand.hqm.hqm_online_sku_rule.service.IOnlineSkuRuleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import javax.servlet.http.HttpServletRequest;
import org.springframework.validation.BindingResult;
import java.util.List;

    @Controller
    public class OnlineSkuRuleController extends BaseController{

    @Autowired
    private IOnlineSkuRuleService service;


    @RequestMapping(value = "/hqm/online/sku/rule/query")
    @ResponseBody
    public ResponseData query(OnlineSkuRule dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
        @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
        IRequest requestContext = createRequestContext(request);
        return new ResponseData(service.reSelect(requestContext,dto,page,pageSize));
    }

    @RequestMapping(value = "/hqm/online/sku/rule/submit")
    @ResponseBody
    public ResponseData update(@RequestBody List<OnlineSkuRule> dto, BindingResult result, HttpServletRequest request){
        getValidator().validate(dto, result);
        if (result.hasErrors()) {
        ResponseData responseData = new ResponseData(false);
        responseData.setMessage(getErrorMessage(result, request));
        return responseData;
        }
        IRequest requestCtx = createRequestContext(request);
        return new ResponseData(service.reBatchUpdate(requestCtx, dto));
    }

    @RequestMapping(value = "/hqm/online/sku/rule/remove")
    @ResponseBody
    public ResponseData delete(HttpServletRequest request,@RequestBody List<OnlineSkuRule> dto){
        service.batchDelete(dto);
        return new ResponseData();
    }
    }