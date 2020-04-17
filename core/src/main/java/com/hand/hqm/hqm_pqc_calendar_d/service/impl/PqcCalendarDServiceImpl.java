package com.hand.hqm.hqm_pqc_calendar_d.service.impl;

import com.hand.hap.core.IRequest;
import com.hand.hap.system.dto.BaseDTO;
import com.hand.hap.system.dto.DTOStatus;
import com.hand.hap.system.service.impl.BaseServiceImpl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hand.hqm.hqm_pqc_calendar.dto.PqcCalendar;
import com.hand.hqm.hqm_pqc_calendar.mapper.PqcCalendarMapper;
import com.hand.hqm.hqm_pqc_calendar.service.IPqcCalendarService;
import com.hand.hqm.hqm_pqc_calendar_d.dto.PqcCalendarD;
import com.hand.hqm.hqm_pqc_calendar_d.mapper.PqcCalendarDMapper;
import com.hand.hqm.hqm_pqc_calendar_d.service.IPqcCalendarDService;

import jodd.util.StringUtil;

import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(rollbackFor = Exception.class)
public class PqcCalendarDServiceImpl extends BaseServiceImpl<PqcCalendarD> implements IPqcCalendarDService{

	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
	SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
	@Autowired
	PqcCalendarDMapper mapper;
	@Autowired
	PqcCalendarMapper pqcCalendarMapper;
	@Autowired
	IPqcCalendarService iPqcCalendarService;
	/* (non-Javadoc)
	 * @see com.hand.hqm.hqm_pqc_calendar_d.service.IPqcCalendarDService#reSelect(com.hand.hap.core.IRequest, com.hand.hqm.hqm_pqc_calendar_d.dto.PqcCalendarD, int, int)
	 */
	@Override
	public List<PqcCalendar> reSelect(IRequest requestContext, PqcCalendar dto, int page, int pageSize) throws ParseException {
		if (pqcCalendarMapper.currentMonthHave(dto) < 1) {// 判断当前月份不存在 开始生成数据
			getDayListOfMonth(sdf.parse(dto.getYearMonth())).stream().forEach(date -> {
				PqcCalendar insertor = new PqcCalendar();
				insertor.setPlanDate(date);
				insertor.setPlantId(dto.getPlantId());
				iPqcCalendarService.insertSelective(requestContext, insertor);
			});
		}
		return pqcCalendarMapper.reSelect(dto);
	}
	/**
	 * 
	 * @description 日期对应月份的所有日历天
	 * @author tianmin.wang
	 * @date 2019年11月22日
	 * @param date 月份
	 * @return
	 * @throws ParseException
	 */
	public List<Date> getDayListOfMonth(Date date) throws ParseException {
		List<Date> list = new ArrayList<Date>();
		Calendar aCalendar = Calendar.getInstance();
		aCalendar.setTime(date);
		int year = aCalendar.get(Calendar.YEAR);// 年份
		int month = aCalendar.get(Calendar.MONTH) + 1;// 月份
		int day = aCalendar.getActualMaximum(Calendar.DATE);
		for (int i = 1; i <= day; i++) {
			String aDate = String.valueOf(year) + "-" + String.format("%02d", month) + "-" + String.format("%02d", i);
			list.add(sdf1.parse(aDate));
		}
		return list;
	}
	
	
	@Override
	public List<PqcCalendar> reBatchUpdate(IRequest request, List<PqcCalendar> list) {
		for (PqcCalendar t : list) {
			switch (((BaseDTO) t).get__status()) {
			case DTOStatus.UPDATE:
				updateUnionD(request, t);
				break;
			default:
				break;
			}
		}
		return list;
	}
	/**
	 * @description
	 * @author tianmin.wang
	 * @date 2020年2月18日 
	 * @param request
	 * @param t
	 */
	private void updateUnionD(IRequest request, PqcCalendar dto) {
		PqcCalendarD as = new PqcCalendarD();
		as.setCalendarId(dto.getCalendarId());
		List<PqcCalendarD> ares = mapper.select(as);
		if (ares == null || ares.size() == 0) {
			PqcCalendarD ainsert = new PqcCalendarD();
			ainsert.setCalendarId(dto.getCalendarId());
			ainsert.setLayeredPerson(dto.getPersonUserId());
			ainsert.setInspectionContext(dto.getInspectionContext());
			ainsert.setInspectionResult(dto.getInspectionResult());
			if (dto.getPersonUserId() != null) {
				ainsert.setLayeredStatus("A");
				dto.setPersonLayeredStatus("A");
			}
			self().insertSelective(request, ainsert);
		} else {
			if (dto.getPersonUserId() == null) {
				return;
			}
			PqcCalendarD aup = new PqcCalendarD();
			aup.setCalendarDId(ares.get(0).getCalendarDId());
			aup.setLayeredPerson(dto.getPersonUserId());
			aup.setInspectionContext(dto.getInspectionContext());
			aup.setInspectionResult(dto.getInspectionResult());
			if (StringUtil.isEmpty(dto.getPersonLayeredStatus())) {
				aup.setLayeredStatus("A");
				dto.setPersonLayeredStatus("A");
			}
			self().updateByPrimaryKeySelective(request, aup);
		}
		
	}
}