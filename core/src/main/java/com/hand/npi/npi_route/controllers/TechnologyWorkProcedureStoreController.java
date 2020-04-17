package com.hand.npi.npi_route.controllers;

import org.springframework.stereotype.Controller;
import com.hand.hap.system.controllers.BaseController;
import com.hand.hap.core.IRequest;
import com.hand.hap.system.dto.ResponseData;
import com.hand.npi.npi_route.dto.TechnologyWorkProcedureStore;
import com.hand.npi.npi_route.service.ITechnologyWorkProcedureStoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import javax.servlet.http.HttpServletRequest;
import org.springframework.validation.BindingResult;
import java.util.List;

    @Controller
    public class TechnologyWorkProcedureStoreController extends BaseController{

    @Autowired
    private ITechnologyWorkProcedureStoreService service;


    @RequestMapping(value = "/npi/technology/work/procedure/store/query")
    @ResponseBody
    public ResponseData query(TechnologyWorkProcedureStore dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
        @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
        IRequest requestContext = createRequestContext(request);
        return new ResponseData(service.select(requestContext,dto,page,pageSize));
    }

    @RequestMapping(value = "/npi/technology/work/procedure/store/submit")
    @ResponseBody
    public ResponseData update(@RequestBody List<TechnologyWorkProcedureStore> dto, BindingResult result, HttpServletRequest request){
        getValidator().validate(dto, result);
        if (result.hasErrors()) {
        ResponseData responseData = new ResponseData(false);
        responseData.setMessage(getErrorMessage(result, request));
        return responseData;
        }
        IRequest requestCtx = createRequestContext(request);
        return new ResponseData(service.batchUpdate(requestCtx, dto));
    }

    @RequestMapping(value = "/npi/technology/work/procedure/store/remove")
    @ResponseBody
    public ResponseData delete(HttpServletRequest request,@RequestBody List<TechnologyWorkProcedureStore> dto){
        service.batchDelete(dto);
        return new ResponseData();
    }
    }