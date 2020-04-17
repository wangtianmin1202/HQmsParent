package com.hand.hqm.hqm_mes_ng_recorde_line.service.impl;

import com.hand.hap.system.service.impl.BaseServiceImpl;
import org.springframework.stereotype.Service;
import com.hand.hqm.hqm_mes_ng_recorde_line.dto.MesNgRecordeLine;
import com.hand.hqm.hqm_mes_ng_recorde_line.service.IMesNgRecordeLineService;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(rollbackFor = Exception.class)
public class MesNgRecordeLineServiceImpl extends BaseServiceImpl<MesNgRecordeLine> implements IMesNgRecordeLineService{

}