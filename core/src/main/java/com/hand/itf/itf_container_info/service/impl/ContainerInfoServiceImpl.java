package com.hand.itf.itf_container_info.service.impl;

import com.hand.hap.system.service.impl.BaseServiceImpl;
import org.springframework.stereotype.Service;
import com.hand.itf.itf_container_info.dto.ContainerInfo;
import com.hand.itf.itf_container_info.service.IContainerInfoService;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(rollbackFor = Exception.class)
public class ContainerInfoServiceImpl extends BaseServiceImpl<ContainerInfo> implements IContainerInfoService{

}