package com.hand.hcm.hcm_position.controllers;

import org.springframework.stereotype.Controller;
import com.hand.hap.system.controllers.BaseController;
import com.hand.hap.core.IRequest;
import com.hand.hap.system.dto.ResponseData;
import com.hand.hcm.hcm_position.dto.PositionCategory;
import com.hand.hcm.hcm_position.service.IPositionCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import javax.servlet.http.HttpServletRequest;
import org.springframework.validation.BindingResult;
import java.util.List;

    @Controller
    public class PositionCategoryController extends BaseController{

    @Autowired
    private IPositionCategoryService service;


    @RequestMapping(value = "/hcm/position/category/query")
    @ResponseBody
    public ResponseData query(PositionCategory dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
        @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
        IRequest requestContext = createRequestContext(request);
        return new ResponseData(service.select(requestContext,dto,page,pageSize));
    }

    @RequestMapping(value = "/hcm/position/category/submit")
    @ResponseBody
    public ResponseData update(@RequestBody List<PositionCategory> dto, BindingResult result, HttpServletRequest request){
        getValidator().validate(dto, result);
        if (result.hasErrors()) {
        ResponseData responseData = new ResponseData(false);
        responseData.setMessage(getErrorMessage(result, request));
        return responseData;
        }
        IRequest requestCtx = createRequestContext(request);
        return new ResponseData(service.batchUpdate(requestCtx, dto));
    }

    @RequestMapping(value = "/hcm/position/category/remove")
    @ResponseBody
    public ResponseData delete(HttpServletRequest request,@RequestBody List<PositionCategory> dto){
        service.batchDelete(dto);
        return new ResponseData();
    }
    }