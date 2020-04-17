package com.hand.hqm.hqm_pqc_calendar.mapper;

import java.util.List;

import com.hand.hap.mybatis.common.Mapper;
import com.hand.hqm.hqm_pqc_calendar.dto.PqcCalendar;

public interface PqcCalendarMapper extends Mapper<PqcCalendar>{

	/**
	 * 
	 * @description 判断传入的年份和月份的数据是否已经生成和存在 求总条数
	 * @author tianmin.wang
	 * @date 2019年11月22日 
	 * @param dto
	 * @return
	 */
	Float currentMonthHave(PqcCalendar dto);

	/**
	 * @description 基础查询
	 * @author tianmin.wang
	 * @date 2019年11月22日 
	 * @param dto
	 * @return
	 */
	List<PqcCalendar> reSelect(PqcCalendar dto);

	/**
	 * @description 检验日历通知查询
	 * @author tianmin.wang
	 * @date 2019年11月26日 
	 * @param dto
	 * @return
	 */
	List<PqcCalendar> forkQuery(PqcCalendar dto);
}