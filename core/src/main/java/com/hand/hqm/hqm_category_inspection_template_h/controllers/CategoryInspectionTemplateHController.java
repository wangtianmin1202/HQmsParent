package com.hand.hqm.hqm_category_inspection_template_h.controllers;

import org.springframework.stereotype.Controller;
import com.hand.hap.system.controllers.BaseController;
import com.hand.hap.core.IRequest;
import com.hand.hap.system.dto.ResponseData;
import com.hand.hqm.hqm_category_inspection_template_h.dto.CategoryInspectionTemplateH;
import com.hand.hqm.hqm_category_inspection_template_h.service.ICategoryInspectionTemplateHService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import javax.servlet.http.HttpServletRequest;
import org.springframework.validation.BindingResult;
import java.util.List;

    @Controller
    public class CategoryInspectionTemplateHController extends BaseController{

    @Autowired
    private ICategoryInspectionTemplateHService service;


    @RequestMapping(value = "/hqm/category/inspection/template/h/query")
    @ResponseBody
    public ResponseData query(CategoryInspectionTemplateH dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
        @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
        IRequest requestContext = createRequestContext(request);
        return new ResponseData(service.select(requestContext,dto,page,pageSize));
    }

    @RequestMapping(value = "/hqm/category/inspection/template/h/submit")
    @ResponseBody
    public ResponseData update(@RequestBody List<CategoryInspectionTemplateH> dto, BindingResult result, HttpServletRequest request){
        getValidator().validate(dto, result);
        if (result.hasErrors()) {
        ResponseData responseData = new ResponseData(false);
        responseData.setMessage(getErrorMessage(result, request));
        return responseData;
        }
        IRequest requestCtx = createRequestContext(request);
        return new ResponseData(service.batchUpdate(requestCtx, dto));
    }

    @RequestMapping(value = "/hqm/category/inspection/template/h/remove")
    @ResponseBody
    public ResponseData delete(HttpServletRequest request,@RequestBody List<CategoryInspectionTemplateH> dto){
        service.batchDelete(dto);
        return new ResponseData();
    }
    @RequestMapping(value = "/hqm/iqc/inspection/template/h/excelimport/category/upload")
    @ResponseBody
    public ResponseData excelUpload(CategoryInspectionTemplateH dto, @RequestParam String mainType, HttpServletRequest request) {
    	try {
    		service.excelUpload(request,mainType);
    	}catch(Exception e) {
    		ResponseData res = new ResponseData(false);
    		res.setMessage(e.getMessage());
    		return res;
    	}
        return new ResponseData();
    }
    
    }