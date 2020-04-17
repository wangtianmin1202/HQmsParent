package com.hand.hqm.hqm_item_control_his.controllers;

import org.springframework.stereotype.Controller;
import com.hand.hap.system.controllers.BaseController;
import com.hand.hap.core.IRequest;
import com.hand.hap.system.dto.ResponseData;
import com.hand.hqm.hqm_item_control_his.dto.ItemControlHis;
import com.hand.hqm.hqm_item_control_his.service.IItemControlHisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import javax.servlet.http.HttpServletRequest;
import org.springframework.validation.BindingResult;
import java.util.List;

    @Controller
    public class ItemControlHisController extends BaseController{

    @Autowired
    private IItemControlHisService service;


    @RequestMapping(value = "/hqm/item/control/his/query")
    @ResponseBody
    public ResponseData query(ItemControlHis dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
        @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
        IRequest requestContext = createRequestContext(request);
        return new ResponseData(service.select(requestContext,dto,page,pageSize));
    }

    @RequestMapping(value = "/hqm/item/control/his/submit")
    @ResponseBody
    public ResponseData update(@RequestBody List<ItemControlHis> dto, BindingResult result, HttpServletRequest request){
        getValidator().validate(dto, result);
        if (result.hasErrors()) {
        ResponseData responseData = new ResponseData(false);
        responseData.setMessage(getErrorMessage(result, request));
        return responseData;
        }
        IRequest requestCtx = createRequestContext(request);
        return new ResponseData(service.batchUpdate(requestCtx, dto));
    }

    @RequestMapping(value = "/hqm/item/control/his/remove")
    @ResponseBody
    public ResponseData delete(HttpServletRequest request,@RequestBody List<ItemControlHis> dto){
        service.batchDelete(dto);
        return new ResponseData();
    }
    }