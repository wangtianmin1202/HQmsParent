package com.hand.dimension.hqm_dimension_step.controllers;

import org.springframework.stereotype.Controller;
import com.hand.hap.system.controllers.BaseController;
import com.hand.hap.core.IRequest;
import com.hand.hap.system.dto.ResponseData;
import com.hand.dimension.hqm_dimension_step.dto.DimensionStep;
import com.hand.dimension.hqm_dimension_step.service.IDimensionStepService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import javax.servlet.http.HttpServletRequest;
import org.springframework.validation.BindingResult;
import java.util.List;

    @Controller
    public class DimensionStepController extends BaseController{

    @Autowired
    private IDimensionStepService service;


    /**
     * 
     * @description 基础查询入口
     * @author tianmin.wang
     * @date 2019年11月21日 
     * @param dto
     * @param page
     * @param pageSize
     * @param request
     * @return
     */
    @RequestMapping(value = "/hqm/8d/step/query")
    @ResponseBody
    public ResponseData query(DimensionStep dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
        @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
        IRequest requestContext = createRequestContext(request);
        return new ResponseData(service.select(requestContext,dto,page,pageSize));
    }

    /**
     * 
     * @description 数据保存
     * @author tianmin.wang
     * @date 2019年11月21日 
     * @param dto
     * @param result
     * @param request
     * @return
     */
    @RequestMapping(value = "/hqm/8d/step/submit")
    @ResponseBody
    public ResponseData update(@RequestBody List<DimensionStep> dto, BindingResult result, HttpServletRequest request){
        getValidator().validate(dto, result);
        if (result.hasErrors()) {
        ResponseData responseData = new ResponseData(false);
        responseData.setMessage(getErrorMessage(result, request));
        return responseData;
        }
        IRequest requestCtx = createRequestContext(request);
        return new ResponseData(service.batchUpdate(requestCtx, dto));
    }

    /**
     * 
     * @description 删除
     * @author tianmin.wang
     * @date 2019年11月21日 
     * @param request
     * @param dto
     * @return
     */
    @RequestMapping(value = "/hqm/8d/step/remove")
    @ResponseBody
    public ResponseData delete(HttpServletRequest request,@RequestBody List<DimensionStep> dto){
        service.batchDelete(dto);
        return new ResponseData();
    }
    }