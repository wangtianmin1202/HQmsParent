package com.hand.spc.pspc_classify.controllers;

import org.springframework.stereotype.Controller;
import com.hand.hap.system.controllers.BaseController;
import com.hand.hap.core.IRequest;
import com.hand.hap.system.dto.ResponseData;
import com.hand.spc.pspc_classify.dto.Classify;
import com.hand.spc.pspc_classify.service.IClassifyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import javax.servlet.http.HttpServletRequest;
import org.springframework.validation.BindingResult;
import java.util.List;

    @Controller
    public class ClassifyController extends BaseController{

    @Autowired
    private IClassifyService service;


    @RequestMapping(value = "/pspc/classify/query")
    @ResponseBody
    public ResponseData query(Classify dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
        @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
        IRequest requestContext = createRequestContext(request);
        return new ResponseData(service.select(requestContext,dto,page,pageSize));
    }

    /**
     * @Author han.zhang
     * @Description  分类组下的分类项查询
     * @Date 9:48 2019/8/7
     * @Param [dto, page, pageSize, request]
     */
    @RequestMapping(value = "/pspc/classify/query/classify")
    @ResponseBody
    public ResponseData queryClassify(Classify dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
                              @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
        IRequest requestContext = createRequestContext(request);
        ResponseData responseData = new ResponseData();
        try{
            responseData = service.selectClassify(requestContext,dto,page,pageSize);
        }catch (Exception e){
            e.printStackTrace();
            responseData.setSuccess(false);
            responseData.setMessage(e.toString());
        }
        return responseData;
    }

    @RequestMapping(value = "/pspc/classify/submit")
    @ResponseBody
    public ResponseData update(@RequestBody List<Classify> dto, BindingResult result, HttpServletRequest request){
        getValidator().validate(dto, result);
        if (result.hasErrors()) {
        ResponseData responseData = new ResponseData(false);
        responseData.setMessage(getErrorMessage(result, request));
        return responseData;
        }
        IRequest requestCtx = createRequestContext(request);
        return new ResponseData(service.batchUpdate(requestCtx, dto));
    }

    @RequestMapping(value = "/pspc/classify/remove")
    @ResponseBody
    public ResponseData delete(HttpServletRequest request,@RequestBody List<Classify> dto){
        service.batchDelete(dto);
        return new ResponseData();
    }

    /**
     * @Author han.zhang
     * @Description 分类项新增或者更新
     * @Date 20:35 2019/8/7
     * @Param [dto, page, pageSize, request]
     */
    @RequestMapping(value = "/pspc/classify/add/update/classify")
    @ResponseBody
    public ResponseData saveAndUpdateClassify(Classify dto,HttpServletRequest request) {
        IRequest requestContext = createRequestContext(request);
        ResponseData responseData = new ResponseData();
        try{
            responseData = service.saveAndUpdateClassify(requestContext,dto);
        }catch (Exception e){
            e.printStackTrace();
            responseData.setSuccess(false);
            responseData.setMessage(e.toString());
        }
        return responseData;
    }

    @RequestMapping(value = "/pspc/classify/add/query/classify/by/parameter/id")
    @ResponseBody
    public ResponseData queryByParameterId(Classify dto,HttpServletRequest request) {
        IRequest requestContext = createRequestContext(request);
        ResponseData responseData = new ResponseData();
        try{
            responseData = service.getClassifyByParameterId(requestContext,dto);
        }catch (Exception e){
            e.printStackTrace();
            responseData.setSuccess(false);
            responseData.setMessage(e.toString());
        }
        return responseData;
    }
}