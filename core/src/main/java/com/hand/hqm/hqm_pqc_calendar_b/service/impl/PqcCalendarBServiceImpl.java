package com.hand.hqm.hqm_pqc_calendar_b.service.impl;

import com.hand.hap.system.service.impl.BaseServiceImpl;
import org.springframework.stereotype.Service;
import com.hand.hqm.hqm_pqc_calendar_b.dto.PqcCalendarB;
import com.hand.hqm.hqm_pqc_calendar_b.service.IPqcCalendarBService;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(rollbackFor = Exception.class)
public class PqcCalendarBServiceImpl extends BaseServiceImpl<PqcCalendarB> implements IPqcCalendarBService{

}