package com.hand.spc.ecr_main.controllers;

import org.springframework.stereotype.Controller;
import com.hand.hap.system.controllers.BaseController;
import com.hand.hap.core.IRequest;
import com.hand.hap.system.dto.ResponseData;
import com.hand.spc.ecr_main.dto.EcrItemSku;
import com.hand.spc.ecr_main.dto.EcrMain;
import com.hand.spc.ecr_main.service.IEcrItemSkuService;
import com.hand.spc.ecr_main.view.EcrItemSkuV2;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import javax.servlet.http.HttpServletRequest;
import org.springframework.validation.BindingResult;
import java.util.List;

    @Controller
    public class EcrItemSkuController extends BaseController{

    @Autowired
    private IEcrItemSkuService service;


    @RequestMapping(value = "/hpm/ecr/item/sku/query")
    @ResponseBody
    public ResponseData query(EcrItemSku dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
        @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
        IRequest requestContext = createRequestContext(request);
        return new ResponseData(service.select(requestContext,dto,page,pageSize));
    }

    
    
    
    @RequestMapping(value = "/hpm/ecr/item/sku/submit")
    @ResponseBody
    public ResponseData update(@RequestBody List<EcrItemSku> dto, BindingResult result, HttpServletRequest request){
        getValidator().validate(dto, result);
        if (result.hasErrors()) {
        ResponseData responseData = new ResponseData(false);
        responseData.setMessage(getErrorMessage(result, request));
        return responseData;
        }
        IRequest requestCtx = createRequestContext(request);
        return new ResponseData(service.batchUpdate(requestCtx, dto));
    }

    @RequestMapping(value = "/hpm/ecr/item/sku/remove")
    @ResponseBody
    public ResponseData delete(HttpServletRequest request,@RequestBody List<EcrItemSku> dto){
        service.batchDelete(dto);
        return new ResponseData();
    }
    }