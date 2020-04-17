package com.hand.hqm.hqm_pqc_calendar_d.controllers;

import org.springframework.stereotype.Controller;
import com.hand.hap.system.controllers.BaseController;
import com.hand.hap.core.IRequest;
import com.hand.hap.system.dto.ResponseData;
import com.hand.hqm.hqm_pqc_calendar.dto.PqcCalendar;
import com.hand.hqm.hqm_pqc_calendar_d.dto.PqcCalendarD;
import com.hand.hqm.hqm_pqc_calendar_d.service.IPqcCalendarDService;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import javax.servlet.http.HttpServletRequest;
import org.springframework.validation.BindingResult;

import java.text.ParseException;
import java.util.List;

    @Controller
    public class PqcCalendarDController extends BaseController{

    @Autowired
    private IPqcCalendarDService service;


    @RequestMapping(value = "/hqm/pqc/calendar/d/query")
    @ResponseBody
    public ResponseData query(PqcCalendar dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
			@RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request)
			throws ParseException {
		IRequest requestContext = createRequestContext(request);
		if (dto.getPlantId() == null || StringUtils.isEmpty(dto.getYearMonth())) {
			ResponseData responseData = new ResponseData(false);
			responseData.setMessage("必输项未输入");
			return responseData;
		}
		return new ResponseData(service.reSelect(requestContext, dto, page, pageSize));
	}

    @RequestMapping(value = "/hqm/pqc/calendar/d/submit")
    @ResponseBody
    public ResponseData update(@RequestBody List<PqcCalendar> dto, BindingResult result, HttpServletRequest request){
        getValidator().validate(dto, result);
        if (result.hasErrors()) {
        ResponseData responseData = new ResponseData(false);
        responseData.setMessage(getErrorMessage(result, request));
        return responseData;
        }
        IRequest requestCtx = createRequestContext(request);
        return new ResponseData(service.reBatchUpdate(requestCtx, dto));
    }

    @RequestMapping(value = "/hqm/pqc/calendar/d/remove")
    @ResponseBody
    public ResponseData delete(HttpServletRequest request,@RequestBody List<PqcCalendarD> dto){
        service.batchDelete(dto);
        return new ResponseData();
    }
    }