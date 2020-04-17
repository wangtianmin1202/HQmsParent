package com.hand.itf.itf_material.service.impl;

import com.hand.hap.system.service.impl.BaseServiceImpl;
import org.springframework.stereotype.Service;
import com.hand.itf.itf_material.dto.Material;
import com.hand.itf.itf_material.service.IMaterialService;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(rollbackFor = Exception.class)
public class MaterialServiceImpl extends BaseServiceImpl<Material> implements IMaterialService{

}