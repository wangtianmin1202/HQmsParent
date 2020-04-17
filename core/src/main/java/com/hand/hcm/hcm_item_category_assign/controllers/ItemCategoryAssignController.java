package com.hand.hcm.hcm_item_category_assign.controllers;

import org.springframework.stereotype.Controller;
import com.hand.hap.system.controllers.BaseController;
import com.hand.hap.core.IRequest;
import com.hand.hap.system.dto.ResponseData;
import com.hand.hcm.hcm_item_category_assign.dto.ItemCategoryAssign;
import com.hand.hcm.hcm_item_category_assign.service.IItemCategoryAssignService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import javax.servlet.http.HttpServletRequest;
import org.springframework.validation.BindingResult;
import java.util.List;

    @Controller
    public class ItemCategoryAssignController extends BaseController{

    @Autowired
    private IItemCategoryAssignService service;


    @RequestMapping(value = "/hcm/item/category/assign/query")
    @ResponseBody
    public ResponseData query(ItemCategoryAssign dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
        @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
        IRequest requestContext = createRequestContext(request);
        return new ResponseData(service.select(requestContext,dto,page,pageSize));
    }

    @RequestMapping(value = "/hcm/item/category/assign/submit")
    @ResponseBody
    public ResponseData update(@RequestBody List<ItemCategoryAssign> dto, BindingResult result, HttpServletRequest request){
        getValidator().validate(dto, result);
        if (result.hasErrors()) {
        ResponseData responseData = new ResponseData(false);
        responseData.setMessage(getErrorMessage(result, request));
        return responseData;
        }
        IRequest requestCtx = createRequestContext(request);
        return new ResponseData(service.batchUpdate(requestCtx, dto));
    }

    @RequestMapping(value = "/hcm/item/category/assign/remove")
    @ResponseBody
    public ResponseData delete(HttpServletRequest request,@RequestBody List<ItemCategoryAssign> dto){
        service.batchDelete(dto);
        return new ResponseData();
    }
    }