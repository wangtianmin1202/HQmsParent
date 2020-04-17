package com.hand.npi.npi_technology.controllers;

import org.springframework.stereotype.Controller;
import com.hand.hap.system.controllers.BaseController;
import com.hand.hap.core.IRequest;
import com.hand.hap.system.dto.ResponseData;
import com.hand.npi.npi_technology.dto.InvalidPatternHis;
import com.hand.npi.npi_technology.service.IInvalidPatternHisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import javax.servlet.http.HttpServletRequest;
import org.springframework.validation.BindingResult;
import java.util.List;

    @Controller
    public class InvalidPatternHisController extends BaseController{

    @Autowired
    private IInvalidPatternHisService service;


    @RequestMapping(value = "/npi/invalid/pattern/his/query")
    @ResponseBody
    public ResponseData query(InvalidPatternHis dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
        @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
        IRequest requestContext = createRequestContext(request);
        return new ResponseData(service.select(requestContext,dto,page,pageSize));
    }

    @RequestMapping(value = "/npi/invalid/pattern/his/submit")
    @ResponseBody
    public ResponseData update(@RequestBody List<InvalidPatternHis> dto, BindingResult result, HttpServletRequest request){
        getValidator().validate(dto, result);
        if (result.hasErrors()) {
        ResponseData responseData = new ResponseData(false);
        responseData.setMessage(getErrorMessage(result, request));
        return responseData;
        }
        IRequest requestCtx = createRequestContext(request);
        return new ResponseData(service.batchUpdate(requestCtx, dto));
    }

    @RequestMapping(value = "/npi/invalid/pattern/his/remove")
    @ResponseBody
    public ResponseData delete(HttpServletRequest request,@RequestBody List<InvalidPatternHis> dto){
        service.batchDelete(dto);
        return new ResponseData();
    }
    
    /**
	 * @author likai 2020.03.26
	 * @description 查询历史数据
	 * @param dto
	 * @param page
	 * @param pageSize
	 * @param requestCtx
	 * @param request
	 * @return
	 */
    @RequestMapping(value = "/npi/invalid/pattern/his/selectByLastUpdateDate")
    @ResponseBody
    public ResponseData selectByLastUpdateDate(InvalidPatternHis dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
    		@RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
        IRequest requestContext = createRequestContext(request);
        return new ResponseData(service.selectByLastUpdateDate(dto, page, pageSize, requestContext, request));
    }
    
    }