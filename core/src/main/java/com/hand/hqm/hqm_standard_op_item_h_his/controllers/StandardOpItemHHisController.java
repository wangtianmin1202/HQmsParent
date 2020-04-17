package com.hand.hqm.hqm_standard_op_item_h_his.controllers;

import org.springframework.stereotype.Controller;
import com.hand.hap.system.controllers.BaseController;
import com.hand.hap.core.IRequest;
import com.hand.hap.system.dto.ResponseData;
import com.hand.hqm.hqm_standard_op_item_h_his.dto.StandardOpItemHHis;
import com.hand.hqm.hqm_standard_op_item_h_his.service.IStandardOpItemHHisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import javax.servlet.http.HttpServletRequest;
import org.springframework.validation.BindingResult;
import java.util.List;

    @Controller
    public class StandardOpItemHHisController extends BaseController{

    @Autowired
    private IStandardOpItemHHisService service;


    @RequestMapping(value = "/hqm/standard/op/item/h/his/query")
    @ResponseBody
    public ResponseData query(StandardOpItemHHis dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
        @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
        IRequest requestContext = createRequestContext(request);
        return new ResponseData(service.select(requestContext,dto,page,pageSize));
    }

    @RequestMapping(value = "/hqm/standard/op/item/h/his/submit")
    @ResponseBody
    public ResponseData update(@RequestBody List<StandardOpItemHHis> dto, BindingResult result, HttpServletRequest request){
        getValidator().validate(dto, result);
        if (result.hasErrors()) {
        ResponseData responseData = new ResponseData(false);
        responseData.setMessage(getErrorMessage(result, request));
        return responseData;
        }
        IRequest requestCtx = createRequestContext(request);
        return new ResponseData(service.batchUpdate(requestCtx, dto));
    }

    @RequestMapping(value = "/hqm/standard/op/item/h/his/remove")
    @ResponseBody
    public ResponseData delete(HttpServletRequest request,@RequestBody List<StandardOpItemHHis> dto){
        service.batchDelete(dto);
        return new ResponseData();
    }
    }