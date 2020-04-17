package com.hand.npi.npi_technology.controllers;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.hand.hap.core.IRequest;
import com.hand.hap.system.controllers.BaseController;
import com.hand.hap.system.dto.ResponseData;
import com.hand.npi.npi_technology.dto.TechnologySparePartDetails;
import com.hand.npi.npi_technology.service.ITechnologySparePartDetailsService;

    @Controller
    public class TechnologySparePartDetailsController extends BaseController{

    @Autowired
    private ITechnologySparePartDetailsService service;


    @RequestMapping(value = "/npi/technology/spare/part/details/query")
    @ResponseBody
    public ResponseData query(TechnologySparePartDetails dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
        @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
        IRequest requestContext = createRequestContext(request);
        return new ResponseData(service.select(requestContext,dto,page,pageSize));
    }

    @RequestMapping(value = "/npi/technology/spare/part/details/submit")
    @ResponseBody
    public ResponseData update(@RequestBody List<TechnologySparePartDetails> dto, BindingResult result, HttpServletRequest request){
        getValidator().validate(dto, result);
        if (result.hasErrors()) {
        ResponseData responseData = new ResponseData(false);
        responseData.setMessage(getErrorMessage(result, request));
        return responseData;
        }
        IRequest requestCtx = createRequestContext(request);
        return new ResponseData(service.batchUpdate(requestCtx, dto));
    }

    @RequestMapping(value = "/npi/technology/spare/part/details/remove")
    @ResponseBody
    public ResponseData delete(HttpServletRequest request,@RequestBody List<TechnologySparePartDetails> dto){
        service.batchDelete(dto);
        return new ResponseData();
    }
    @RequestMapping(value = "/npi/technology/spare/part/details/deleteById")
    @ResponseBody
    public ResponseData deleteById(HttpServletRequest request,String sparePartDetailsId){
    	TechnologySparePartDetails de=new TechnologySparePartDetails();
    	de.setSparePartDetailsId(Float.valueOf(sparePartDetailsId));
    	service.deleteByPrimaryKey(de);
    	return new ResponseData();
    }
    
    /**
     * 
     * @Description:零件主查询
     * @param dto
     * @param page
     * @param pageSize
     * @param request
     * @return
     */
    @RequestMapping(value = "/npi/technology/spare/part/details/queryByCondition")
    @ResponseBody
    public ResponseData queryByCondition(TechnologySparePartDetails dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
        @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
        IRequest requestContext = createRequestContext(request);
        return new ResponseData(service.queryByCondition(requestContext,dto,page,pageSize));
    }
    
    /**
     * 
     * @Description:添加和编辑零件信息
     * @param dto
     * @param result
     * @param request
     * @return
     */
    @RequestMapping(value = "/npi/technology/spare/part/details/add")
    @ResponseBody
    public ResponseData add(@RequestBody TechnologySparePartDetails dto, BindingResult result, HttpServletRequest request){
        getValidator().validate(dto, result);
        if (result.hasErrors()) {
        ResponseData responseData = new ResponseData(false);
        responseData.setMessage(getErrorMessage(result, request));
        return responseData;
        }
        IRequest requestCtx = createRequestContext(request);
        return new ResponseData(service.add(requestCtx, dto));
    }
    }