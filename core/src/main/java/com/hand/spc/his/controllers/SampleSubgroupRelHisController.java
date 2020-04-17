package com.hand.spc.his.controllers;

import org.springframework.stereotype.Controller;
import com.hand.hap.system.controllers.BaseController;
import com.hand.hap.core.IRequest;
import com.hand.hap.system.dto.ResponseData;
import com.hand.spc.his.dto.SampleSubgroupRelHis;
import com.hand.spc.his.service.ISampleSubgroupRelHisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import javax.servlet.http.HttpServletRequest;
import org.springframework.validation.BindingResult;
import java.util.List;

    @Controller
    public class SampleSubgroupRelHisController extends BaseController{

    @Autowired
    private ISampleSubgroupRelHisService service;


    @RequestMapping(value = "/pspc/sample/subgroup/rel/his/query")
    @ResponseBody
    public ResponseData query(SampleSubgroupRelHis dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
        @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
        IRequest requestContext = createRequestContext(request);
        return new ResponseData(service.select(requestContext,dto,page,pageSize));
    }

    @RequestMapping(value = "/pspc/sample/subgroup/rel/his/submit")
    @ResponseBody
    public ResponseData update(@RequestBody List<SampleSubgroupRelHis> dto, BindingResult result, HttpServletRequest request){
        getValidator().validate(dto, result);
        if (result.hasErrors()) {
        ResponseData responseData = new ResponseData(false);
        responseData.setMessage(getErrorMessage(result, request));
        return responseData;
        }
        IRequest requestCtx = createRequestContext(request);
        return new ResponseData(service.batchUpdate(requestCtx, dto));
    }

    @RequestMapping(value = "/pspc/sample/subgroup/rel/his/remove")
    @ResponseBody
    public ResponseData delete(HttpServletRequest request,@RequestBody List<SampleSubgroupRelHis> dto){
        service.batchDelete(dto);
        return new ResponseData();
    }
    }