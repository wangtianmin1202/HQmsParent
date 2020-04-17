package com.hand.hqm.hqm_pqc_warning_record.service.impl;

import com.hand.hap.system.service.impl.BaseServiceImpl;
import org.springframework.stereotype.Service;
import com.hand.hqm.hqm_pqc_warning_record.dto.PqcWarningRecord;
import com.hand.hqm.hqm_pqc_warning_record.service.IPqcWarningRecordService;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(rollbackFor = Exception.class)
public class PqcWarningRecordServiceImpl extends BaseServiceImpl<PqcWarningRecord> implements IPqcWarningRecordService{

}