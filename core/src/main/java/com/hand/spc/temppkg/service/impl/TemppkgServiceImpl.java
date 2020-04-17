package com.hand.spc.temppkg.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hand.hap.system.service.impl.BaseServiceImpl;
import com.hand.spc.temppkg.dto.Temppkgdto;
import com.hand.spc.temppkg.service.ITemppkgService;

@Service
@Transactional(rollbackFor = Exception.class)
public class TemppkgServiceImpl extends BaseServiceImpl<Temppkgdto> implements ITemppkgService{

}