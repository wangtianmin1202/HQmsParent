package com.hand.spc.ecr_main.service.impl;

import com.github.pagehelper.PageHelper;
import com.hand.hap.core.IRequest;
import com.hand.hap.system.dto.ResponseData;
import com.hand.hap.system.service.impl.BaseServiceImpl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hand.spc.ecr_main.dto.EcrInfluencedmaterial;
import com.hand.spc.ecr_main.dto.EcrItemSku;
import com.hand.spc.ecr_main.dto.EcrMain;
import com.hand.spc.ecr_main.mapper.EcrInfluencedmaterialMapper;
import com.hand.spc.ecr_main.mapper.EcrItemSkuMapper;
import com.hand.spc.ecr_main.service.IEcrItemSkuService;
import com.hand.spc.ecr_main.view.EcrItemSkuV0;
import com.hand.spc.ecr_main.view.EcrItemSkuV1;
import com.hand.spc.ecr_main.view.EcrItemSkuV2;

import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(rollbackFor = Exception.class)
public class EcrItemSkuServiceImpl extends BaseServiceImpl<EcrItemSku> implements IEcrItemSkuService{
	
	
	
	
}