package com.hand.plm.product_func_attr_basic.controllers;

import com.hand.plm.product_func_attr_basic.view.UpdateDetailVO;
import org.springframework.stereotype.Controller;
import com.hand.hap.system.controllers.BaseController;
import com.hand.hap.core.IRequest;
import com.hand.hap.system.dto.ResponseData;
import com.hand.plm.product_func_attr_basic.dto.ProductFuncAttrDetail;
import com.hand.plm.product_func_attr_basic.service.IProductFuncAttrDetailService;
import com.hand.plm.product_func_attr_basic.view.ProductFuncAttrDetailVO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

import org.springframework.validation.BindingResult;
import java.util.List;

@Controller
public class ProductFuncAttrDetailController extends BaseController {

    @Autowired
    private IProductFuncAttrDetailService service;

    @RequestMapping(value = "/plm/product/func/attr/detail/queryDetail")
    @ResponseBody
    public ResponseData queryDetail(ProductFuncAttrDetailVO dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
                                    @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
        IRequest requestContext = createRequestContext(request);
        return new ResponseData(service.queryDetail(requestContext, dto, page, pageSize));
    }

    @RequestMapping(value = "/plm/product/func/attr/detail/query")
    @ResponseBody
    public ResponseData query(ProductFuncAttrDetail dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
                              @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
        IRequest requestContext = createRequestContext(request);
        return new ResponseData(service.select(requestContext, dto, page, pageSize));
    }

    @RequestMapping(value = "/plm/product/func/attr/detail/submit")
    @ResponseBody
    public ResponseData update(@RequestBody List<ProductFuncAttrDetail> dto, BindingResult result,
                               HttpServletRequest request) {
        getValidator().validate(dto, result);
        if (result.hasErrors()) {
            ResponseData responseData = new ResponseData(false);
            responseData.setMessage(getErrorMessage(result, request));
            return responseData;
        }
        IRequest requestCtx = createRequestContext(request);
        return new ResponseData(service.batchUpdate(requestCtx, dto));
    }

    @RequestMapping(value = "/plm/product/func/attr/detail/remove")
    @ResponseBody
    public ResponseData delete(HttpServletRequest request, @RequestBody List<ProductFuncAttrDetail> dto) {
        service.batchDelete(dto);
        return new ResponseData();
    }


    @RequestMapping(value = "/plm/product/func/attr/detail/queryUpdateDetail")
    @ResponseBody
    public ResponseData queryUpdateDetail(UpdateDetailVO dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
                                          @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
        IRequest requestContext = createRequestContext(request);
        return new ResponseData(service.queryUpdateDetail(requestContext, dto, page, pageSize));
    }
}