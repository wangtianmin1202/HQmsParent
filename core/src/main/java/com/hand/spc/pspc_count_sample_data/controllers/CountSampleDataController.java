package com.hand.spc.pspc_count_sample_data.controllers;

import org.springframework.stereotype.Controller;
import com.hand.hap.system.controllers.BaseController;
import com.hand.hap.core.IRequest;
import com.hand.hap.system.dto.ResponseData;
import com.hand.spc.pspc_count_sample_data.dto.CountSampleData;
import com.hand.spc.pspc_count_sample_data.service.ICountSampleDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import javax.servlet.http.HttpServletRequest;
import org.springframework.validation.BindingResult;
import java.util.List;
import java.util.Map;

@Controller
    public class CountSampleDataController extends BaseController{

    @Autowired
    private ICountSampleDataService service;


    @RequestMapping(value = "/pspc/count/sample/data/query")
    @ResponseBody
    public ResponseData query(CountSampleData dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
        @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
        IRequest requestContext = createRequestContext(request);
        return new ResponseData(service.select(requestContext,dto,page,pageSize));
    }

    /**
     * @Author han.zhang
     * @Description 计数型数据维护查询
     * @Date 20:54 2019/8/12
     * @Param [dto, page, pageSize, request]
     */
    @RequestMapping(value = "/pspc/count/sample/data/query/count/data")
    @ResponseBody
    public ResponseData queryCountData(CountSampleData dto, HttpServletRequest request) {
        IRequest requestContext = createRequestContext(request);
        return new ResponseData(service.queryCountData(requestContext,dto,1,10));
    }

    @RequestMapping(value = "/pspc/count/sample/data/submit")
    @ResponseBody
    public ResponseData update(@RequestBody List<CountSampleData> dto, BindingResult result, HttpServletRequest request){
        getValidator().validate(dto, result);
        if (result.hasErrors()) {
        ResponseData responseData = new ResponseData(false);
        responseData.setMessage(getErrorMessage(result, request));
        return responseData;
        }
        IRequest requestCtx = createRequestContext(request);
        return new ResponseData(service.batchUpdate(requestCtx, dto));
    }

    @RequestMapping(value = "/pspc/count/sample/data/submit/count/sampl/data")
    @ResponseBody
    public ResponseData updateSampleData(@RequestBody List<Map<String,String>> dtos,HttpServletRequest request){
        IRequest requestCtx = createRequestContext(request);
        ResponseData responseData = new ResponseData();
        try {
            responseData = service.saveCountData(requestCtx,dtos);
        }catch (Exception e){
            e.printStackTrace();
            responseData.setMessage(e.getMessage());
            responseData.setSuccess(false);
        }
        return responseData;
    }

    @RequestMapping(value = "/pspc/count/sample/data/remove")
    @ResponseBody
    public ResponseData delete(HttpServletRequest request,@RequestBody List<CountSampleData> dto){
        ResponseData responseData = new ResponseData();
        try {
            responseData = service.deleteCountSamleDate(dto);
        }catch (Exception e){
            e.printStackTrace();
            responseData.setSuccess(false);
            responseData.setMessage(e.toString());
        }

        return responseData;
    }
    }