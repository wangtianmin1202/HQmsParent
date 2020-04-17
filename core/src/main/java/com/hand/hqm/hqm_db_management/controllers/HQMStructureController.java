package com.hand.hqm.hqm_db_management.controllers;

import org.springframework.stereotype.Controller;
import com.hand.hap.system.controllers.BaseController;
import com.hand.hap.core.IRequest;
import com.hand.hap.system.dto.ResponseData;
import com.hand.hqm.hqm_db_management.dto.HQMStructure;
import com.hand.hqm.hqm_db_management.service.IHQMStructureService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import javax.servlet.http.HttpServletRequest;
import org.springframework.validation.BindingResult;
import java.util.List;

    @Controller
    public class HQMStructureController extends BaseController{

    @Autowired
    private IHQMStructureService service;


    @RequestMapping(value = "/hqm/structure/query")
    @ResponseBody
    public ResponseData query(HQMStructure dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
        @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
        IRequest requestContext = createRequestContext(request);
        return new ResponseData(service.select(requestContext,dto,page,pageSize));
    }

    @RequestMapping(value = "/hqm/structure/submit")
    @ResponseBody
    public ResponseData update(@RequestBody List<HQMStructure> dto, BindingResult result, HttpServletRequest request){
        getValidator().validate(dto, result);
        if (result.hasErrors()) {
        ResponseData responseData = new ResponseData(false);
        responseData.setMessage(getErrorMessage(result, request));
        return responseData;
        }
        IRequest requestCtx = createRequestContext(request);
        return new ResponseData(service.batchUpdate(requestCtx, dto));
    }

    @RequestMapping(value = "/hqm/structure/remove")
    @ResponseBody
    public ResponseData delete(HttpServletRequest request,@RequestBody List<HQMStructure> dto){
        service.batchDelete(dto);
        return new ResponseData();
    }
    }