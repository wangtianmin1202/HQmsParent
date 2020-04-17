package com.hand.hqm.hqm_pqc_calendar_c.service.impl;

import com.hand.hap.system.service.impl.BaseServiceImpl;
import org.springframework.stereotype.Service;
import com.hand.hqm.hqm_pqc_calendar_c.dto.PqcCalendarC;
import com.hand.hqm.hqm_pqc_calendar_c.service.IPqcCalendarCService;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(rollbackFor = Exception.class)
public class PqcCalendarCServiceImpl extends BaseServiceImpl<PqcCalendarC> implements IPqcCalendarCService{

}