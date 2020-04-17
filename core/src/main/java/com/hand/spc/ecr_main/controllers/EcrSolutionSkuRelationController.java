package com.hand.spc.ecr_main.controllers;

import org.springframework.stereotype.Controller;
import com.hand.hap.system.controllers.BaseController;
import com.hand.hap.core.IRequest;
import com.hand.hap.system.dto.ResponseData;
import com.hand.spc.ecr_main.dto.EcrSolutionSkuRelation;
import com.hand.spc.ecr_main.service.IEcrSolutionSkuRelationService;
import com.hand.spc.ecr_main.view.EcrSolutionSkuV3;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import javax.servlet.http.HttpServletRequest;
import org.springframework.validation.BindingResult;
import java.util.List;

    @Controller
    public class EcrSolutionSkuRelationController extends BaseController{

    @Autowired
    private IEcrSolutionSkuRelationService service;


    @RequestMapping(value = "/hpm/ecr/solution/sku/relation/query")
    @ResponseBody
    public ResponseData query(EcrSolutionSkuRelation dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
        @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
        IRequest requestContext = createRequestContext(request);
        return new ResponseData(service.select(requestContext,dto,page,pageSize));
    }

    @RequestMapping(value = "/hpm/ecr/solution/sku/relation/submit")
    @ResponseBody
    public ResponseData update(@RequestBody List<EcrSolutionSkuRelation> dto, BindingResult result, HttpServletRequest request){
        getValidator().validate(dto, result);
        if (result.hasErrors()) {
        ResponseData responseData = new ResponseData(false);
        responseData.setMessage(getErrorMessage(result, request));
        return responseData;
        }
        IRequest requestCtx = createRequestContext(request);
        return new ResponseData(service.batchUpdate(requestCtx, dto));
    }

    @RequestMapping(value = "/hpm/ecr/solution/sku/relation/remove")
    @ResponseBody
    public ResponseData delete(HttpServletRequest request,@RequestBody List<EcrSolutionSkuRelation> dto){
        service.batchDelete(dto);
        return new ResponseData();
    }
    @RequestMapping(value = "/hpm/ecr/solution/sku/relation/sp")
    @ResponseBody
    public ResponseData querySp(@RequestBody EcrSolutionSkuV3 dto , HttpServletRequest request) {
        IRequest requestContext = createRequestContext(request);
        return new ResponseData(service.getSpItem(dto));
    }

    }