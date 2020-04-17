package com.hand.hqm.hqm_pqc_calendar.service.impl;

import com.github.pagehelper.PageHelper;
import com.hand.hap.core.IRequest;
import com.hand.hap.core.annotation.StdWho;
import com.hand.hap.system.dto.BaseDTO;
import com.hand.hap.system.dto.DTOStatus;
import com.hand.hap.system.service.IBaseService;
import com.hand.hap.system.service.impl.BaseServiceImpl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.aop.framework.AopContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.hand.hqm.hqm_pqc_calendar.dto.PqcCalendar;
import com.hand.hqm.hqm_pqc_calendar.mapper.PqcCalendarMapper;
import com.hand.hqm.hqm_pqc_calendar.service.IPqcCalendarService;
import com.hand.hqm.hqm_pqc_calendar_a.dto.PqcCalendarA;
import com.hand.hqm.hqm_pqc_calendar_a.mapper.PqcCalendarAMapper;
import com.hand.hqm.hqm_pqc_calendar_a.service.IPqcCalendarAService;
import com.hand.hqm.hqm_pqc_calendar_b.dto.PqcCalendarB;
import com.hand.hqm.hqm_pqc_calendar_b.mapper.PqcCalendarBMapper;
import com.hand.hqm.hqm_pqc_calendar_b.service.IPqcCalendarBService;
import com.hand.hqm.hqm_pqc_calendar_c.dto.PqcCalendarC;
import com.hand.hqm.hqm_pqc_calendar_c.mapper.PqcCalendarCMapper;
import com.hand.hqm.hqm_pqc_calendar_c.service.IPqcCalendarCService;
import com.hand.hqm.hqm_pqc_calendar_d.dto.PqcCalendarD;
import com.hand.hqm.hqm_pqc_calendar_d.mapper.PqcCalendarDMapper;
import com.hand.hqm.hqm_pqc_calendar_d.service.IPqcCalendarDService;

import jodd.util.StringUtil;

import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(rollbackFor = Exception.class)
public class PqcCalendarServiceImpl extends BaseServiceImpl<PqcCalendar> implements IPqcCalendarService {

	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
	SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
	@Autowired
	PqcCalendarMapper mapper;
	@Autowired
	PqcCalendarAMapper pqcCalendarAMapper;
	@Autowired
	PqcCalendarBMapper pqcCalendarBMapper;
	@Autowired
	PqcCalendarCMapper pqcCalendarCMapper;
	@Autowired
	PqcCalendarDMapper pqcCalendarDMapper;
	@Autowired
	IPqcCalendarAService iPqcCalendarAService;
	@Autowired
	IPqcCalendarBService iPqcCalendarBService;
	@Autowired
	IPqcCalendarCService iPqcCalendarCService;
	@Autowired
	IPqcCalendarDService iPqcCalendarDService;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.hand.hqm.hqm_pqc_calendar.service.IPqcCalendarService#reSelect(com.hand.
	 * hap.core.IRequest, com.hand.hqm.hqm_pqc_calendar.dto.PqcCalendar, int, int)
	 */
	@Override
	public List<PqcCalendar> reSelect(IRequest requestContext, PqcCalendar dto, int page, int pageSize)
			throws ParseException {
		if (mapper.currentMonthHave(dto) < 1) {// 判断当前月份不存在 开始生成数据
			getDayListOfMonth(sdf.parse(dto.getYearMonth())).stream().forEach(date -> {
				PqcCalendar insertor = new PqcCalendar();
				insertor.setPlanDate(date);
				insertor.setPlantId(dto.getPlantId());
				self().insertSelective(requestContext, insertor);
			});

		}
		return mapper.reSelect(dto);
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.hand.hqm.hqm_pqc_calendar.service.IPqcCalendarService#reBatchUpdate(com.
	 * hand.hap.core.IRequest, java.util.List)
	 */
	@Override
	public List<PqcCalendar> reBatchUpdate(IRequest request, List<PqcCalendar> list) {
		for (PqcCalendar t : list) {
			switch (((BaseDTO) t).get__status()) {
			case DTOStatus.UPDATE:
				updateUnionA(request, t);
				updateUnionB(request, t);
				updateUnionC(request, t);
				break;
			default:
				break;
			}
		}
		return list;
	}

	/**
	 * 
	 * @description 这里更新了三个表的人员字段A表
	 * @author tianmin.wang
	 * @date 2019年11月22日
	 * @param request
	 * @param dto
	 */
	public void updateUnionA(IRequest request, PqcCalendar dto) {
		PqcCalendarA as = new PqcCalendarA();
		as.setCalendarId(dto.getCalendarId());
		List<PqcCalendarA> ares = pqcCalendarAMapper.select(as);
		if (ares == null || ares.size() == 0) {
			PqcCalendarA ainsert = new PqcCalendarA();
			ainsert.setCalendarId(dto.getCalendarId());
			ainsert.setLayeredPerson(dto.getLayeredUserId());
			if (dto.getLayeredUserId() != null) {
				ainsert.setLayeredStatus("A");
				dto.setLayeredStatus("A");
			}
			iPqcCalendarAService.insertSelective(request, ainsert);
		} else {
			if (dto.getLayeredUserId() == null) {
				return;
			}
			PqcCalendarA aup = new PqcCalendarA();
			aup.setCalendarAId(ares.get(0).getCalendarAId());
			aup.setLayeredPerson(dto.getLayeredUserId());
			if (StringUtil.isEmpty(dto.getLayeredStatus())) {
				aup.setLayeredStatus("A");
				dto.setLayeredStatus("A");
			}
			iPqcCalendarAService.updateByPrimaryKeySelective(request, aup);
		}
	}

	/**
	 * 
	 * @description 这里更新了三个表的人员字段B表
	 * @author tianmin.wang
	 * @date 2019年11月22日
	 * @param request
	 * @param dto
	 */
	public void updateUnionB(IRequest request, PqcCalendar dto) {
		PqcCalendarB bs = new PqcCalendarB();
		bs.setCalendarId(dto.getCalendarId());
		List<PqcCalendarB> bres = pqcCalendarBMapper.select(bs);
		if (bres == null || bres.size() == 0) {
			PqcCalendarB binsert = new PqcCalendarB();
			binsert.setCalendarId(dto.getCalendarId());
			binsert.setSafetyPerson(dto.getSafetyUserId());
			if (dto.getSafetyUserId() != null) {
				binsert.setSafetyStatus("A");
				dto.setSafetyStatus("A");
			}
			iPqcCalendarBService.insertSelective(request, binsert);
		} else {
			if (dto.getSafetyUserId() == null) {
				return;
			}
			PqcCalendarB bup = new PqcCalendarB();
			bup.setCalendarBId(bres.get(0).getCalendarBId());
			bup.setSafetyPerson(dto.getSafetyUserId());
			if (StringUtil.isEmpty(dto.getSafetyStatus())) {
				bup.setSafetyStatus("A");
				dto.setSafetyStatus("A");
			}
			iPqcCalendarBService.updateByPrimaryKeySelective(request, bup);
		}
	}

	/**
	 * 
	 * @description 这里更新了三个表的人员字段C表
	 * @author tianmin.wang
	 * @date 2019年11月22日
	 * @param request
	 * @param dto
	 */
	public void updateUnionC(IRequest request, PqcCalendar dto) {

		PqcCalendarC cs = new PqcCalendarC();
		cs.setCalendarId(dto.getCalendarId());
		List<PqcCalendarC> cres = pqcCalendarCMapper.select(cs);
		if (cres == null || cres.size() == 0) {
			PqcCalendarC cinsert = new PqcCalendarC();
			cinsert.setCalendarId(dto.getCalendarId());
			cinsert.setPwaPerson(dto.getPwaUserId());
			if (dto.getPwaUserId() != null) {
				cinsert.setPwaStatus("A");
				dto.setPwaStatus("A");
			}
			iPqcCalendarCService.insertSelective(request, cinsert);
		} else {
			if (dto.getPwaUserId() == null) {
				return;
			}
			PqcCalendarC cup = new PqcCalendarC();
			cup.setCalendarCId(cres.get(0).getCalendarCId());
			cup.setPwaPerson(dto.getPwaUserId());
			if (StringUtil.isEmpty(dto.getPwaStatus())) {
				cup.setPwaStatus("A");
				dto.setPwaStatus("A");
			}
			iPqcCalendarCService.updateByPrimaryKeySelective(request, cup);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.hand.hqm.hqm_pqc_calendar.service.IPqcCalendarService#batchClose(java.
	 * util.List)
	 */
	@Override
	public void batchClose(IRequest requestCtx, List<PqcCalendar> list) throws RuntimeException {
		for (PqcCalendar dto : list) {
			closeUnionA(requestCtx, dto);
			closeUnionB(requestCtx, dto);
			closeUnionC(requestCtx, dto);
			closeUnionD(requestCtx, dto);
		}
	}
	/**
	 * @description C表的状态修改为C关闭
	 * @author tianmin.wang
	 * @date 2019年11月25日
	 * @param requestCtx
	 * @param dto
	 */
	private void closeUnionD(IRequest requestCtx, PqcCalendar dto) {
		PqcCalendarD as = new PqcCalendarD();
		as.setCalendarId(dto.getCalendarId());
		List<PqcCalendarD> cres = pqcCalendarDMapper.select(as);
		cres.stream().forEach(p -> {
			p.setLayeredStatus("C");
			iPqcCalendarDService.updateByPrimaryKeySelective(requestCtx, p);
		});
	}
	/**
	 * @description C表的状态修改为C关闭
	 * @author tianmin.wang
	 * @date 2019年11月25日
	 * @param requestCtx
	 * @param dto
	 */
	private void closeUnionC(IRequest requestCtx, PqcCalendar dto) {
		PqcCalendarC as = new PqcCalendarC();
		as.setCalendarId(dto.getCalendarId());
		List<PqcCalendarC> cres = pqcCalendarCMapper.select(as);
		cres.stream().forEach(p -> {
			p.setPwaStatus("C");
			iPqcCalendarCService.updateByPrimaryKeySelective(requestCtx, p);
		});
	}

	/**
	 * @description B表的状态修改为C关闭
	 * @author tianmin.wang
	 * @date 2019年11月25日
	 * @param requestCtx
	 * @param dto
	 */
	private void closeUnionB(IRequest requestCtx, PqcCalendar dto) {
		PqcCalendarB as = new PqcCalendarB();
		as.setCalendarId(dto.getCalendarId());
		List<PqcCalendarB> bres = pqcCalendarBMapper.select(as);
		bres.stream().forEach(p -> {
			p.setSafetyStatus("C");
			iPqcCalendarBService.updateByPrimaryKeySelective(requestCtx, p);
		});
	}

	/**
	 * @description A表的状态修改为C关闭
	 * @author tianmin.wang
	 * @date 2019年11月25日
	 * @param requestCtx
	 * @param dto
	 */
	private void closeUnionA(IRequest requestCtx, PqcCalendar dto) {
		PqcCalendarA as = new PqcCalendarA();
		as.setCalendarId(dto.getCalendarId());
		List<PqcCalendarA> ares = pqcCalendarAMapper.select(as);
		ares.stream().forEach(p -> {
			p.setLayeredStatus("C");
			iPqcCalendarAService.updateByPrimaryKeySelective(requestCtx, p);
		});
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.hand.hqm.hqm_pqc_calendar.service.IPqcCalendarService#forkQuery(com.hand.
	 * hap.core.IRequest, java.util.List, int, int)
	 */
	@Override
	public List<PqcCalendar> forkQuery(IRequest requestContext, PqcCalendar dto, int page, int pageSize) {
		PageHelper.startPage(page, pageSize);
		return mapper.forkQuery(dto);
	}

}