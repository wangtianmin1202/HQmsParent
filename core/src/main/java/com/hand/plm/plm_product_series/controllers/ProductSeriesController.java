package com.hand.plm.plm_product_series.controllers;

import org.springframework.stereotype.Controller;
import com.hand.hap.system.controllers.BaseController;
import com.hand.hap.core.IRequest;
import com.hand.hap.system.dto.ResponseData;
import com.hand.plm.plm_product_series.dto.ProductSeries;
import com.hand.plm.plm_product_series.service.IProductSeriesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import javax.servlet.http.HttpServletRequest;
import org.springframework.validation.BindingResult;
import java.util.List;

    @Controller
    public class ProductSeriesController extends BaseController{

    @Autowired
    private IProductSeriesService service;


    @RequestMapping(value = "/plm/product/series/query")
    @ResponseBody
    public ResponseData query(ProductSeries dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
        @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
        IRequest requestContext = createRequestContext(request);
        return new ResponseData(service.select(requestContext,dto,page,pageSize));
    }

    @RequestMapping(value = "/plm/product/series/submit")
    @ResponseBody
    public ResponseData update(@RequestBody List<ProductSeries> dto, BindingResult result, HttpServletRequest request){
        getValidator().validate(dto, result);
        if (result.hasErrors()) {
        ResponseData responseData = new ResponseData(false);
        responseData.setMessage(getErrorMessage(result, request));
        return responseData;
        }
        IRequest requestCtx = createRequestContext(request);
        return new ResponseData(service.batchUpdate(requestCtx, dto));
    }

    @RequestMapping(value = "/plm/product/series/remove")
    @ResponseBody
    public ResponseData delete(HttpServletRequest request,@RequestBody List<ProductSeries> dto){
        service.batchDelete(dto);
        return new ResponseData();
    }
    }