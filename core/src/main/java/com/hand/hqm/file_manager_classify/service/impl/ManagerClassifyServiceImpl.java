package com.hand.hqm.file_manager_classify.service.impl;

import com.hand.hap.system.service.impl.BaseServiceImpl;
import org.springframework.stereotype.Service;
import com.hand.hqm.file_manager_classify.dto.ManagerClassify;
import com.hand.hqm.file_manager_classify.service.IManagerClassifyService;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(rollbackFor = Exception.class)
public class ManagerClassifyServiceImpl extends BaseServiceImpl<ManagerClassify> implements IManagerClassifyService{

}