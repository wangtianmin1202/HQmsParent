package com.hand.hqm.hqm_pqc_calendar.service;

import java.text.ParseException;
import java.util.List;

import com.hand.hap.core.IRequest;
import com.hand.hap.core.ProxySelf;
import com.hand.hap.system.service.IBaseService;
import com.hand.hqm.hqm_pqc_calendar.dto.PqcCalendar;

public interface IPqcCalendarService extends IBaseService<PqcCalendar>, ProxySelf<IPqcCalendarService>{

	/**
	 * @description 重写的查询 月份没有数据的进行一次生成
	 * @author tianmin.wang
	 * @date 2019年11月22日 
	 * @param requestContext
	 * @param dto
	 * @param page
	 * @param pageSize
	 * @return
	 * @throws ParseException 
	 */
	List<PqcCalendar> reSelect(IRequest requestContext, PqcCalendar dto, int page, int pageSize) throws ParseException;

	/**
	 * @description 更新数据 关联的三个表一起更新
	 * @author tianmin.wang
	 * @date 2019年11月22日 
	 * @param requestCtx
	 * @param dto
	 * @return
	 */
	List<PqcCalendar> reBatchUpdate(IRequest requestCtx, List<PqcCalendar> dto);

	/**
	 * @description 批量关闭
	 * @author tianmin.wang
	 * @date 2019年11月25日 
	 * @param dto
	 */
	void batchClose(IRequest requestCtx,List<PqcCalendar> dto);

	/**
	 * @description 检验日历通知查询
	 * @author tianmin.wang
	 * @date 2019年11月26日 
	 * @param requestContext
	 * @param dto
	 * @param page
	 * @param pageSize
	 * @return
	 */
	List<PqcCalendar> forkQuery(IRequest requestContext, PqcCalendar dto, int page, int pageSize);

}