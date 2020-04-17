package com.hand.hcs.hcs_produce.controllers;

import com.hand.hap.core.IRequest;
import com.hand.hap.system.controllers.BaseController;
import com.hand.hap.system.dto.ResponseData;
import com.hand.hcs.hcs_certificate_file_manage.dto.Certificate;
import com.hand.hcs.hcs_produce.dto.ItemStationProduce;
import com.hand.hcs.hcs_produce.service.IItemStationProduceService;
import com.hand.wfl.util.ActException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.xml.bind.ValidationException;

@Controller
public class ItemStationProduceController extends BaseController {

    @Autowired
    private IItemStationProduceService service;


    @RequestMapping(value = "/hcs/item/station/produce/query")
    @ResponseBody
    public ResponseData query(ItemStationProduce dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
                              @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
        IRequest requestContext = createRequestContext(request);
        return new ResponseData(service.select(requestContext, dto, page, pageSize));
    }

    @RequestMapping(value = "/hcs/item/station/produce/listQuery")
    @ResponseBody
    public ResponseData listQuery(ItemStationProduce dto, HttpServletRequest request) {
        IRequest requestContext = createRequestContext(request);
        return new ResponseData(service.listQuery(requestContext, dto));
    }

    @RequestMapping(value = "/hcs/item/station/produce/submit")
    @ResponseBody
    public ResponseData update(@RequestBody List<ItemStationProduce> dto, BindingResult result, HttpServletRequest request) {
        getValidator().validate(dto, result);
        if (result.hasErrors()) {
            ResponseData responseData = new ResponseData(false);
            responseData.setMessage(getErrorMessage(result, request));
            return responseData;
        }
        IRequest requestCtx = createRequestContext(request);
        return new ResponseData(service.batchUpdate(requestCtx, dto));
    }

    @RequestMapping(value = "/hcs/item/station/produce/remove")
    @ResponseBody
    public ResponseData delete(HttpServletRequest request, @RequestBody List<ItemStationProduce> dto) {
        service.batchDelete(dto);
        return new ResponseData();
    }

    @RequestMapping(value = "/hcs/item/station/produce/insert")
    @ResponseBody
    public ResponseData addData(@RequestBody List<ItemStationProduce> dtos, BindingResult result, HttpServletRequest request) {
        getValidator().validate(dtos, result);
        if (result.hasErrors()) {
            ResponseData responseData = new ResponseData(false);
            responseData.setMessage(getErrorMessage(result, request));
            return responseData;
        }
        IRequest requestCtx = createRequestContext(request);
        return service.insertMutiData(requestCtx, dtos);
    }

    @RequestMapping(value = "/hcs/item/station/produce/add")
    @ResponseBody
    public ResponseData add(ItemStationProduce dto,HttpServletRequest request) {
        IRequest requestCtx = createRequestContext(request);
        return service.add(requestCtx, dto);

    }

    @RequestMapping(value = "/hcs/produce/approve")
    @ResponseBody
    public ResponseData approve(HttpServletRequest request,@RequestBody ItemStationProduce dto) throws ActException, ValidationException {
        IRequest requestCtx = createRequestContext(request);
        return new ResponseData(service.approve(requestCtx,dto));
    }
}