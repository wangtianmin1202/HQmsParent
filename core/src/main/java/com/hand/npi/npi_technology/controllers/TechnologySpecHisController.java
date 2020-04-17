package com.hand.npi.npi_technology.controllers;

import org.springframework.stereotype.Controller;
import com.hand.hap.system.controllers.BaseController;
import com.hand.hap.core.IRequest;
import com.hand.hap.system.dto.ResponseData;
import com.hand.npi.npi_technology.dto.TechnologySpecHis;
import com.hand.npi.npi_technology.service.ITechnologySpecHisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import javax.servlet.http.HttpServletRequest;
import org.springframework.validation.BindingResult;
import java.util.List;

    @Controller
    public class TechnologySpecHisController extends BaseController{

    @Autowired
    private ITechnologySpecHisService service;


    @RequestMapping(value = "/npi/technology/spec/his/query")
    @ResponseBody
    public ResponseData query(TechnologySpecHis dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
        @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
        IRequest requestContext = createRequestContext(request);
        return new ResponseData(service.select(requestContext,dto,page,pageSize));
    }

    @RequestMapping(value = "/npi/technology/spec/his/submit")
    @ResponseBody
    public ResponseData update(@RequestBody List<TechnologySpecHis> dto, BindingResult result, HttpServletRequest request){
        getValidator().validate(dto, result);
        if (result.hasErrors()) {
        ResponseData responseData = new ResponseData(false);
        responseData.setMessage(getErrorMessage(result, request));
        return responseData;
        }
        IRequest requestCtx = createRequestContext(request);
        return new ResponseData(service.batchUpdate(requestCtx, dto));
    }

    @RequestMapping(value = "/npi/technology/spec/his/remove")
    @ResponseBody
    public ResponseData delete(HttpServletRequest request,@RequestBody List<TechnologySpecHis> dto){
        service.batchDelete(dto);
        return new ResponseData();
    }
    
    /**
	 * @author likai 2020.03.21
	 * @description 查询组装动作历史数据
	 * @param dto
	 * @param page
	 * @param pageSize
	 * @param requestCtx
	 * @param request
	 * @return
	 */
    @RequestMapping(value = "/npi/technology/spec/his/selectByLastUpdateDate")
    @ResponseBody
    public ResponseData selectByLastUpdateDate(TechnologySpecHis dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
    		@RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
        IRequest requestContext = createRequestContext(request);
        return new ResponseData(service.selectByLastUpdateDate(dto, page, pageSize, requestContext, request));
    }
    
    /**
	 * @author likai 2020.03.23
	 * @description 发起历史变更 新增或修改
	 * @param request
	 * @param list
	 * @return
	 */
    @RequestMapping(value = "/npi/technology/spec/his/addOrEditData")
    @ResponseBody
    public ResponseData addOrEditData(@RequestBody List<TechnologySpecHis> dtos, BindingResult result, HttpServletRequest request){
    	getValidator().validate(dtos, result);
        if (result.hasErrors()) {
        ResponseData responseData = new ResponseData(false);
        responseData.setMessage(getErrorMessage(result, request));
        return responseData;
        }
        IRequest requestCtx = createRequestContext(request);
        return service.addOrEditData(requestCtx, dtos);
    }
    }