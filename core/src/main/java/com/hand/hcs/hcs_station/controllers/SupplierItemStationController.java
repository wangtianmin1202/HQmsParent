package com.hand.hcs.hcs_station.controllers;

import com.hand.hap.core.IRequest;
import com.hand.hap.system.controllers.BaseController;
import com.hand.hap.system.dto.ResponseData;
import com.hand.hcs.hcs_produce.dto.ItemStationProduce;
import com.hand.hcs.hcs_station.dto.SupplierItemStation;
import com.hand.hcs.hcs_station.service.ISupplierItemStationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import javax.servlet.http.HttpServletRequest;

@Controller
public class SupplierItemStationController extends BaseController {

    @Autowired
    private ISupplierItemStationService service;


    @RequestMapping(value = "/hcs/supplier/item/station/query")
    @ResponseBody
    public ResponseData query(SupplierItemStation dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
                              @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
        IRequest requestContext = createRequestContext(request);
        return new ResponseData(service.select(requestContext, dto, page, pageSize));
    }

    @RequestMapping(value = "/hcs/supplier/item/station/listQuery")
    @ResponseBody
    public ResponseData listQuery(SupplierItemStation dto,  HttpServletRequest request) {
        IRequest requestContext = createRequestContext(request);
        return new ResponseData(service.query(requestContext, dto));
    }

    @RequestMapping(value = "/hcs/supplier/item/station/submit")
    @ResponseBody
    public ResponseData update(@RequestBody List<SupplierItemStation> dto, BindingResult result, HttpServletRequest request) {
        getValidator().validate(dto, result);
        if (result.hasErrors()) {
            ResponseData responseData = new ResponseData(false);
            responseData.setMessage(getErrorMessage(result, request));
            return responseData;
        }
        IRequest requestCtx = createRequestContext(request);
        return new ResponseData(service.batchUpdate(requestCtx, dto));
    }

    @RequestMapping(value = "/hcs/supplier/item/station/remove")
    @ResponseBody
    public ResponseData delete(HttpServletRequest request, @RequestBody List<SupplierItemStation> dto) {
        service.batchDelete(dto);
        return new ResponseData();
    }

    @RequestMapping(value = "/hcs/supplier/item/station/add")
    @ResponseBody
    public ResponseData add(HttpServletRequest request, @RequestBody SupplierItemStation dto) {
        IRequest iRequest = createRequestContext(request);
        service.add(iRequest,dto);
        return new ResponseData();
    }

    @RequestMapping(value = "/hcs/supplier/item/station/insert")
    @ResponseBody
    public ResponseData addData(@RequestBody List<SupplierItemStation> dtos, BindingResult result, HttpServletRequest request) {
        getValidator().validate(dtos, result);
        if (result.hasErrors()) {
            ResponseData responseData = new ResponseData(false);
            responseData.setMessage(getErrorMessage(result, request));
            return responseData;
        }
        IRequest requestCtx = createRequestContext(request);
        return service.inserMuli(requestCtx, dtos);
    }

    @RequestMapping(value = "/hcs/supplier/item/station/enable")
    @ResponseBody
    public ResponseData enable(HttpServletRequest request, @RequestParam Float itemStationId,@RequestParam String flag) {
        IRequest iRequest = createRequestContext(request);
        return service.enable(iRequest,itemStationId,flag);
    }
}