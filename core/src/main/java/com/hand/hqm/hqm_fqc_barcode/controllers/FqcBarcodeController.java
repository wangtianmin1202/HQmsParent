package com.hand.hqm.hqm_fqc_barcode.controllers;

import org.springframework.stereotype.Controller;
import com.hand.hap.system.controllers.BaseController;
import com.hand.hap.core.IRequest;
import com.hand.hap.system.dto.ResponseData;
import com.hand.hqm.hqm_fqc_barcode.dto.FqcBarcode;
import com.hand.hqm.hqm_fqc_barcode.service.IFqcBarcodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import javax.servlet.http.HttpServletRequest;
import org.springframework.validation.BindingResult;
import java.util.List;

    @Controller
    public class FqcBarcodeController extends BaseController{

    @Autowired
    private IFqcBarcodeService service;


    @RequestMapping(value = "/hqm/fqc/barcode/query")
    @ResponseBody
    public ResponseData query(FqcBarcode dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
        @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
        IRequest requestContext = createRequestContext(request);
        return new ResponseData(service.select(requestContext,dto,page,pageSize));
    }

    @RequestMapping(value = "/hqm/fqc/barcode/submit")
    @ResponseBody
    public ResponseData update(@RequestBody List<FqcBarcode> dto, BindingResult result, HttpServletRequest request){
        getValidator().validate(dto, result);
        if (result.hasErrors()) {
        ResponseData responseData = new ResponseData(false);
        responseData.setMessage(getErrorMessage(result, request));
        return responseData;
        }
        IRequest requestCtx = createRequestContext(request);
        return new ResponseData(service.batchUpdate(requestCtx, dto));
    }

    @RequestMapping(value = "/hqm/fqc/barcode/remove")
    @ResponseBody
    public ResponseData delete(HttpServletRequest request,@RequestBody List<FqcBarcode> dto){
        service.batchDelete(dto);
        return new ResponseData();
    }
    }