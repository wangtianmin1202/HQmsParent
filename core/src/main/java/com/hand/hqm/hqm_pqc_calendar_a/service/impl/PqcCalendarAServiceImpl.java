package com.hand.hqm.hqm_pqc_calendar_a.service.impl;

import com.hand.hap.system.service.impl.BaseServiceImpl;
import org.springframework.stereotype.Service;
import com.hand.hqm.hqm_pqc_calendar_a.dto.PqcCalendarA;
import com.hand.hqm.hqm_pqc_calendar_a.service.IPqcCalendarAService;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(rollbackFor = Exception.class)
public class PqcCalendarAServiceImpl extends BaseServiceImpl<PqcCalendarA> implements IPqcCalendarAService{

}