package com.hand.npi.npi_technology.controllers;

import java.util.List;
import java.util.Map;

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
import com.hand.npi.npi_technology.dto.TechnologySparePart;
import com.hand.npi.npi_technology.service.ITechnologySparePartService;

    @Controller
    public class TechnologySparePartController extends BaseController{

    @Autowired
    private ITechnologySparePartService service;


    @RequestMapping(value = "/npi/technology/spare/part/query")
    @ResponseBody
    public ResponseData query(TechnologySparePart dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
        @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
        IRequest requestContext = createRequestContext(request);
        return new ResponseData(service.select(requestContext,dto,page,pageSize));
    }

    @RequestMapping(value = "/npi/technology/spare/part/submit")
    @ResponseBody
    public ResponseData update(@RequestBody List<TechnologySparePart> dto, BindingResult result, HttpServletRequest request){
        getValidator().validate(dto, result);
        if (result.hasErrors()) {
        ResponseData responseData = new ResponseData(false);
        responseData.setMessage(getErrorMessage(result, request));
        return responseData;
        }
        IRequest requestCtx = createRequestContext(request);
        return new ResponseData(service.batchUpdate(requestCtx, dto));
    }

    @RequestMapping(value = "/npi/technology/spare/part/remove")
    @ResponseBody
    public ResponseData delete(HttpServletRequest request,@RequestBody List<TechnologySparePart> dto){
        service.batchDelete(dto);
        return new ResponseData();
    }
    @RequestMapping(value = "/npi/technology/spare/part/deleteById")
    @ResponseBody
    public ResponseData delSparePart(HttpServletRequest request,TechnologySparePart dto){
    	IRequest requestCtx = createRequestContext(request);
    	Map<String, Object> deleteSparePartById = service.deleteSparePartById(requestCtx,dto);
    	if (!(boolean)deleteSparePartById.get("isSuccess")) {
    		ResponseData responseData = new ResponseData(false);
    		responseData.setMessage(String.valueOf(deleteSparePartById.get("message")));
    		return responseData;
		}
    	return new ResponseData();
    }
    
    /**
     * 
     * @Description:主界面查询，树状结构
     * @param dto
     * @param request
     * @return
     */
    @RequestMapping(value = "/npi/technology/spare/part/queryTreeData")
    @ResponseBody
    public ResponseData queryTreeData(TechnologySparePart dto, HttpServletRequest request) {
        IRequest requestContext = createRequestContext(request);
        return new ResponseData(service.queryTreeData(requestContext,dto));
    }
    
    /**
     * 
     * @Description:新增和编辑组件
     * @param dto
     * @param result
     * @param request
     * @return
     */
    @RequestMapping(value = "/npi/technology/spare/part/add")
    @ResponseBody
    public ResponseData add(@RequestBody TechnologySparePart dto, BindingResult result, HttpServletRequest request){
        getValidator().validate(dto, result);
        if (result.hasErrors()) {
        ResponseData responseData = new ResponseData(false);
        responseData.setMessage(getErrorMessage(result, request));
        return responseData;
        }
        IRequest requestCtx = createRequestContext(request);
        List<TechnologySparePart> add = service.add(requestCtx, dto);
        if(add.size()==0) {
        	ResponseData responseData = new ResponseData(false);
        	responseData.setMessage("组件分类编码不能重复");
        	return responseData;
        }
        return new ResponseData();
    }
    }