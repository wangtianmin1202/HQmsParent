package com.hand.hcm.hcm_category_settings.controllers;

import org.springframework.stereotype.Controller;
import com.hand.hap.system.controllers.BaseController;
import com.hand.hap.core.IRequest;
import com.hand.hap.system.dto.ResponseData;
import com.hand.hcm.hcm_category_settings.dto.ItemCategorySettings;
import com.hand.hcm.hcm_category_settings.service.IItemCategorySettingsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

import org.springframework.validation.BindingResult;

import java.util.List;

@Controller
public class ItemCategorySettingsController extends BaseController {

    @Autowired
    private IItemCategorySettingsService service;


    @RequestMapping(value = "/hcm/item/category/settings/query")
    @ResponseBody
    public ResponseData query(ItemCategorySettings dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
                              @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
        IRequest requestContext = createRequestContext(request);
        return new ResponseData(service.select(requestContext, dto, page, pageSize));
    }

    @RequestMapping(value = "/hcm/item/category/settings/listQuery")
    @ResponseBody
    public ResponseData listQuery(ItemCategorySettings dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
                              @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
        IRequest requestContext = createRequestContext(request);
        return new ResponseData(service.listQuery(requestContext, dto, page, pageSize));
    }

    @RequestMapping(value = "/hcm/item/category/settings/submit")
    @ResponseBody
    public ResponseData update(@RequestBody List<ItemCategorySettings> dto, BindingResult result, HttpServletRequest request) {
        getValidator().validate(dto, result);
        if (result.hasErrors()) {
            ResponseData responseData = new ResponseData(false);
            responseData.setMessage(getErrorMessage(result, request));
            return responseData;
        }
        IRequest requestCtx = createRequestContext(request);
        return new ResponseData(service.batchUpdate(requestCtx, dto));
    }

    @RequestMapping(value = "/hcm/item/category/settings/remove")
    @ResponseBody
    public ResponseData delete(HttpServletRequest request, @RequestBody List<ItemCategorySettings> dto) {
        service.batchDelete(dto);
        return new ResponseData();
    }

    @RequestMapping(value = "/hcm/item/category/settings/add")
    @ResponseBody
    public ResponseData add(@RequestBody ItemCategorySettings dto,HttpServletRequest request) {
        IRequest requestCtx = createRequestContext(request);
        service.addOrUpdate(requestCtx,dto);
        return new ResponseData();
    }
}