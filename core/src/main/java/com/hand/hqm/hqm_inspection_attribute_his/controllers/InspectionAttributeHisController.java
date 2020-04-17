package com.hand.hqm.hqm_inspection_attribute_his.controllers;

import org.springframework.stereotype.Controller;
import com.hand.hap.system.controllers.BaseController;
import com.hand.hap.core.IRequest;
import com.hand.hap.system.dto.ResponseData;
import com.hand.hqm.hqm_inspection_attribute_his.dto.InspectionAttributeHis;
import com.hand.hqm.hqm_inspection_attribute_his.service.IInspectionAttributeHisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import javax.servlet.http.HttpServletRequest;
import org.springframework.validation.BindingResult;
import java.util.List;

    @Controller
    public class InspectionAttributeHisController extends BaseController{

    @Autowired
    private IInspectionAttributeHisService service;


    @RequestMapping(value = "/hqm/inspection/attribute/his/query")
    @ResponseBody
    public ResponseData query(InspectionAttributeHis dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
        @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
        IRequest requestContext = createRequestContext(request);
        return new ResponseData(service.select(requestContext,dto,page,pageSize));
    }

    @RequestMapping(value = "/hqm/inspection/attribute/his/submit")
    @ResponseBody
    public ResponseData update(@RequestBody List<InspectionAttributeHis> dto, BindingResult result, HttpServletRequest request){
        getValidator().validate(dto, result);
        if (result.hasErrors()) {
        ResponseData responseData = new ResponseData(false);
        responseData.setMessage(getErrorMessage(result, request));
        return responseData;
        }
        IRequest requestCtx = createRequestContext(request);
        return new ResponseData(service.batchUpdate(requestCtx, dto));
    }

    @RequestMapping(value = "/hqm/inspection/attribute/his/remove")
    @ResponseBody
    public ResponseData delete(HttpServletRequest request,@RequestBody List<InspectionAttributeHis> dto){
        service.batchDelete(dto);
        return new ResponseData();
    }
    }