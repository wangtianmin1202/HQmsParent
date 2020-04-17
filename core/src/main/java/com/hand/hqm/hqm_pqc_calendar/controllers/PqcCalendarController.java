package com.hand.hqm.hqm_pqc_calendar.controllers;

import org.springframework.stereotype.Controller;
import com.hand.hap.system.controllers.BaseController;
import com.hand.hap.core.IRequest;
import com.hand.hap.system.dto.ResponseData;
import com.hand.hqm.hqm_pqc_calendar.dto.PqcCalendar;
import com.hand.hqm.hqm_pqc_calendar.service.IPqcCalendarService;

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
public class PqcCalendarController extends BaseController {

	@Autowired
	private IPqcCalendarService service;

	/**
	 * 
	 * @description 查询入口
	 * @author tianmin.wang
	 * @date 2019年11月22日
	 * @param dto
	 * @param page
	 * @param pageSize
	 * @param request
	 * @return
	 * @throws ParseException
	 */
	@RequestMapping(value = "/hqm/pqc/calendar/query")
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

	/**
	 * 
	 * @description 数据更新保存
	 * @author tianmin.wang
	 * @date 2019年11月22日
	 * @param dto
	 * @param result
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/hqm/pqc/calendar/submit")
	@ResponseBody
	public ResponseData update(@RequestBody List<PqcCalendar> dto, BindingResult result, HttpServletRequest request) {
		getValidator().validate(dto, result);
		if (result.hasErrors()) {
			ResponseData responseData = new ResponseData(false);
			responseData.setMessage(getErrorMessage(result, request));
			return responseData;
		}
		IRequest requestCtx = createRequestContext(request);
		return new ResponseData(service.reBatchUpdate(requestCtx, dto));
	}

	/**
	 * 
	 * @description 删除
	 * @author tianmin.wang
	 * @date 2019年11月22日
	 * @param request
	 * @param dto
	 * @return
	 */
	@RequestMapping(value = "/hqm/pqc/calendar/remove")
	@ResponseBody
	public ResponseData delete(HttpServletRequest request, @RequestBody List<PqcCalendar> dto) {
		service.batchDelete(dto);
		return new ResponseData();
	}

	/**
	 * 
	 * @description 关闭
	 * @author tianmin.wang
	 * @date 2019年11月25日
	 * @param request
	 * @param dto
	 * @return
	 */
	@RequestMapping(value = "/hqm/pqc/calendar/close")
	@ResponseBody
	public ResponseData close(HttpServletRequest request, @RequestBody List<PqcCalendar> dto) {
		IRequest requestCtx = createRequestContext(request);
		try {
			service.batchClose(requestCtx, dto);
		} catch (RuntimeException e) {
			ResponseData res = new ResponseData(false);
			res.setMessage(e.getMessage());
			return res;
		}
		return new ResponseData();
	}

	/**
	 * 
	 * @description 检验日历通知的查询
	 * @author tianmin.wang
	 * @date 2019年11月25日
	 * @param request
	 * @param dto
	 * @return
	 */
	@RequestMapping(value = "/hqm/pqc/calendar/fork/query")
	@ResponseBody
	public ResponseData forkQuery(PqcCalendar dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
			@RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
		IRequest requestContext = createRequestContext(request);
		return new ResponseData(service.forkQuery(requestContext, dto, page, pageSize));
	}

}