package com.hand.hqm.hqm_pqc_calendar_d.service;

import java.text.ParseException;
import java.util.List;

import com.hand.hap.core.IRequest;
import com.hand.hap.core.ProxySelf;
import com.hand.hap.system.service.IBaseService;
import com.hand.hqm.hqm_pqc_calendar.dto.PqcCalendar;
import com.hand.hqm.hqm_pqc_calendar_d.dto.PqcCalendarD;

public interface IPqcCalendarDService extends IBaseService<PqcCalendarD>, ProxySelf<IPqcCalendarDService>{

	/**
	 * @description
	 * @author tianmin.wang
	 * @date 2020年2月18日 
	 * @param requestContext
	 * @param dto
	 * @param page
	 * @param pageSize
	 * @return
	 * @throws ParseException 
	 */
	List<PqcCalendar> reSelect(IRequest requestContext, PqcCalendar dto, int page, int pageSize) throws ParseException;

	/**
	 * @description
	 * @author tianmin.wang
	 * @date 2020年2月18日 
	 * @param request
	 * @param list
	 * @return
	 */
	List<PqcCalendar> reBatchUpdate(IRequest request, List<PqcCalendar> list);

}