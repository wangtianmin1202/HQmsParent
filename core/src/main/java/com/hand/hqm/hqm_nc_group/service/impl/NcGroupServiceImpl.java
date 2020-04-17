package com.hand.hqm.hqm_nc_group.service.impl;

import com.hand.hap.system.service.impl.BaseServiceImpl;
import org.springframework.stereotype.Service;
import com.hand.hqm.hqm_nc_group.dto.NcGroup;
import com.hand.hqm.hqm_nc_group.service.INcGroupService;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(rollbackFor = Exception.class)
public class NcGroupServiceImpl extends BaseServiceImpl<NcGroup> implements INcGroupService{

}