package com.hand.spc.his.service.impl;

import com.hand.hap.system.service.impl.BaseServiceImpl;
import org.springframework.stereotype.Service;
import com.hand.spc.his.dto.OocHis;
import com.hand.spc.his.service.IOocHisService;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(rollbackFor = Exception.class)
public class OocHisServiceImpl extends BaseServiceImpl<OocHis> implements IOocHisService{

}