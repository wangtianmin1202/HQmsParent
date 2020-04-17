package com.hand.hcs.hcs_produce.controllers;

import com.hand.hap.core.IRequest;
import com.hand.hap.system.controllers.BaseController;
import com.hand.hap.system.dto.ResponseData;
import com.hand.hcs.hcs_produce.dto.ItemStationProduceLine;
import com.hand.hcs.hcs_produce.service.IItemStationProduceLineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.util.List;
import javax.servlet.http.HttpServletRequest;

@Controller
public class ItemStationProduceLineController extends BaseController {

    @Autowired
    private IItemStationProduceLineService service;


    @RequestMapping(value = "/hcs/item/station/produce/line/query")
    @ResponseBody
    public ResponseData query(ItemStationProduceLine dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
                              @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
        IRequest requestContext = createRequestContext(request);
        return new ResponseData(service.select(requestContext, dto, page, pageSize));
    }

    @RequestMapping(value = "/hcs/item/station/produce/line/listQuery")
    @ResponseBody
    public ResponseData listQuery(ItemStationProduceLine dto, HttpServletRequest request) {
        IRequest requestContext = createRequestContext(request);
        return new ResponseData(service.listQuery(requestContext, dto));
    }

    @RequestMapping(value = "/hcs/item/station/produce/line/submit")
    @ResponseBody
    public ResponseData update(@RequestBody List<ItemStationProduceLine> dto, BindingResult result, HttpServletRequest request) {
        getValidator().validate(dto, result);
        if (result.hasErrors()) {
            ResponseData responseData = new ResponseData(false);
            responseData.setMessage(getErrorMessage(result, request));
            return responseData;
        }
        IRequest requestCtx = createRequestContext(request);
        return new ResponseData(service.batchUpdate(requestCtx, dto));
    }

    @RequestMapping(value = "/hcs/item/station/produce/line/remove")
    @ResponseBody
    public ResponseData delete(HttpServletRequest request, @RequestBody List<ItemStationProduceLine> dto) {
        service.batchDelete(dto);
        return new ResponseData();
    }

    @RequestMapping(value = "/hcs/item/station/produce/line/add")
    @ResponseBody
    public ResponseData add( @RequestBody  ItemStationProduceLine dto,HttpServletRequest request) {
        IRequest requestCtx = createRequestContext(request);
        return service.add(requestCtx, dto);

    }

    @RequestMapping(value = "/hcs/item/station/produce/line/upload")
    @ResponseBody
    public ResponseData fileUpload(HttpServletRequest request) {
        ResponseData responseData = new ResponseData();
        IRequest requestCtx = createRequestContext(request);
        try {
            responseData = service.fileUpload(requestCtx, request);
        } catch (IllegalStateException | IOException e) {
            responseData.setMessage(e.getMessage());
            responseData.setSuccess(false);
        }
        return responseData;
    }

    @RequestMapping(value = "/hcs/item/station/produce/line/delfile")
    @ResponseBody
    public ResponseData updateDataAndDelFile(HttpServletRequest request, @RequestBody List<ItemStationProduceLine> dto) {
        IRequest requestCtx = createRequestContext(request);
        service.updateAndDelFile(requestCtx, dto);
        return new ResponseData();
    }
}