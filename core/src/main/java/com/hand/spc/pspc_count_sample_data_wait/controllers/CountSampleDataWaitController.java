package com.hand.spc.pspc_count_sample_data_wait.controllers;

import org.springframework.stereotype.Controller;
import com.hand.hap.system.controllers.BaseController;
import com.hand.hap.core.IRequest;
import com.hand.hap.system.dto.ResponseData;
import com.hand.spc.pspc_count_sample_data_wait.dto.CountSampleDataWait;
import com.hand.spc.pspc_count_sample_data_wait.service.ICountSampleDataWaitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import javax.servlet.http.HttpServletRequest;
import org.springframework.validation.BindingResult;
import java.util.List;

    @Controller
    public class CountSampleDataWaitController extends BaseController{

    @Autowired
    private ICountSampleDataWaitService service;


    @RequestMapping(value = "/pspc/count/sample/data/wait/query")
    @ResponseBody
    public ResponseData query(CountSampleDataWait dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
        @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
        IRequest requestContext = createRequestContext(request);
        return new ResponseData(service.select(requestContext,dto,page,pageSize));
    }

    /**
     * @Author han.zhang
     * @Description EXCEL导入
     * @Date 15:42 2019/8/15
     * @Param [request]
     */
    @RequestMapping(value = "/pspc/count/sample/data/wait/importExcel")
    @ResponseBody
    public ResponseData importFirstExcel(HttpServletRequest request,@RequestParam("ceParameterId") Long ceParameterId,
                                         @RequestParam("ceGroupId") Long ceGroupId,
                                         @RequestParam("attachmentGroupId") Long attachmentGroupId,
                                         @RequestParam("classifyExtendCount") Long classifyExtendCount,
                                         @RequestParam("attributeExtendCount") Long attributeExtendCount){
        IRequest requestContext = createRequestContext(request);
        ResponseData responseData = new ResponseData();
        try {
            responseData = service.importExcel(requestContext, request,ceGroupId,ceParameterId,attachmentGroupId,
                    classifyExtendCount,attributeExtendCount);
        }catch (Exception e){
            e.printStackTrace();
            responseData.setMessage(e.getMessage());
            responseData.setSuccess(false);
        }
        return responseData;
    }

    @RequestMapping(value = "/pspc/count/sample/data/wait/submit")
    @ResponseBody
    public ResponseData update(@RequestBody List<CountSampleDataWait> dto, BindingResult result, HttpServletRequest request){
        getValidator().validate(dto, result);
        if (result.hasErrors()) {
        ResponseData responseData = new ResponseData(false);
        responseData.setMessage(getErrorMessage(result, request));
        return responseData;
        }
        IRequest requestCtx = createRequestContext(request);
        return new ResponseData(service.batchUpdate(requestCtx, dto));
    }

    @RequestMapping(value = "/pspc/count/sample/data/wait/remove")
    @ResponseBody
    public ResponseData delete(HttpServletRequest request,@RequestBody List<CountSampleDataWait> dto){
        service.batchDelete(dto);
        return new ResponseData();
    }
    }