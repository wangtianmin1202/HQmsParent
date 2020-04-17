package com.hand.spc.pspc_count_sample_data_extend.controllers;

import com.hand.spc.pspc_count_sample_data.dto.CountSampleData;
import org.springframework.stereotype.Controller;
import com.hand.hap.system.controllers.BaseController;
import com.hand.hap.core.IRequest;
import com.hand.hap.system.dto.ResponseData;
import com.hand.spc.pspc_count_sample_data_extend.dto.CountSampleDataExtend;
import com.hand.spc.pspc_count_sample_data_extend.service.ICountSampleDataExtendService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import javax.servlet.http.HttpServletRequest;
import org.springframework.validation.BindingResult;
import java.util.List;

    @Controller
    public class CountSampleDataExtendController extends BaseController{

    @Autowired
    private ICountSampleDataExtendService service;


    @RequestMapping(value = "/pspc/count/sample/data/extend/query")
    @ResponseBody
    public ResponseData query(CountSampleDataExtend dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
        @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
        IRequest requestContext = createRequestContext(request);
        return new ResponseData(service.select(requestContext,dto,page,pageSize));
    }

    /**
     * @Author han.zhang
     * @Description
     * @Date 13:51 2019/8/14
     * @Param [dto, page, pageSize, request]
     */
    @RequestMapping(value = "/pspc/count/sample/data/extend/query/extend/columns")
    @ResponseBody
    public ResponseData queryExtendColumns(CountSampleData dto, HttpServletRequest request) {
        IRequest requestContext = createRequestContext(request);
        return new ResponseData(service.queryExtendColumns(requestContext,dto));
    }

    @RequestMapping(value = "/pspc/count/sample/data/extend/submit")
    @ResponseBody
    public ResponseData update(@RequestBody List<CountSampleDataExtend> dto, BindingResult result, HttpServletRequest request){
        getValidator().validate(dto, result);
        if (result.hasErrors()) {
        ResponseData responseData = new ResponseData(false);
        responseData.setMessage(getErrorMessage(result, request));
        return responseData;
        }
        IRequest requestCtx = createRequestContext(request);
        return new ResponseData(service.batchUpdate(requestCtx, dto));
    }

    @RequestMapping(value = "/pspc/count/sample/data/extend/remove")
    @ResponseBody
    public ResponseData delete(HttpServletRequest request,@RequestBody List<CountSampleDataExtend> dto){
        service.batchDelete(dto);
        return new ResponseData();
    }
    }