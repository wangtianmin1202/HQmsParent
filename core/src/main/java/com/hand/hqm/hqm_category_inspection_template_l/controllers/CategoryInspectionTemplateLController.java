package com.hand.hqm.hqm_category_inspection_template_l.controllers;

import org.springframework.stereotype.Controller;
import com.hand.hap.system.controllers.BaseController;
import com.hand.hap.core.IRequest;
import com.hand.hap.system.dto.ResponseData;
import com.hand.hqm.hqm_category_inspection_template_l.dto.CategoryInspectionTemplateL;
import com.hand.hqm.hqm_category_inspection_template_l.service.ICategoryInspectionTemplateLService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import javax.servlet.http.HttpServletRequest;
import org.springframework.validation.BindingResult;
import java.util.List;

    @Controller
    public class CategoryInspectionTemplateLController extends BaseController{

    @Autowired
    private ICategoryInspectionTemplateLService service;


    @RequestMapping(value = "/hqm/category/inspection/template/l/query")
    @ResponseBody
    public ResponseData query(CategoryInspectionTemplateL dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
        @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
        IRequest requestContext = createRequestContext(request);
        return new ResponseData(service.select(requestContext,dto,page,pageSize));
    }

    @RequestMapping(value = "/hqm/category/inspection/template/l/submit")
    @ResponseBody
    public ResponseData update(@RequestBody List<CategoryInspectionTemplateL> dto, BindingResult result, HttpServletRequest request){
        getValidator().validate(dto, result);
        if (result.hasErrors()) {
        ResponseData responseData = new ResponseData(false);
        responseData.setMessage(getErrorMessage(result, request));
        return responseData;
        }
        IRequest requestCtx = createRequestContext(request);
        return new ResponseData(service.batchUpdate(requestCtx, dto));
    }

    @RequestMapping(value = "/hqm/category/inspection/template/l/remove")
    @ResponseBody
    public ResponseData delete(HttpServletRequest request,@RequestBody List<CategoryInspectionTemplateL> dto){
        service.batchDelete(dto);
        return new ResponseData();
    }
    }