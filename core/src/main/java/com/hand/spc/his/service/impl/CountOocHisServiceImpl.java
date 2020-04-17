package com.hand.spc.his.service.impl;

import com.hand.hap.system.service.impl.BaseServiceImpl;
import org.springframework.stereotype.Service;
import com.hand.spc.his.dto.CountOocHis;
import com.hand.spc.his.service.ICountOocHisService;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(rollbackFor = Exception.class)
public class CountOocHisServiceImpl extends BaseServiceImpl<CountOocHis> implements ICountOocHisService{

}