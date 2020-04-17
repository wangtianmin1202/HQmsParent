package com.hand.spc.ecr_main.service;

import java.util.List;

import com.hand.hap.core.IRequest;
import com.hand.hap.core.ProxySelf;
import com.hand.hap.system.dto.ResponseData;
import com.hand.hap.system.service.IBaseService;
import com.hand.spc.ecr_main.dto.EcrItemSku;
import com.hand.spc.ecr_main.dto.EcrMain;
import com.hand.spc.ecr_main.view.EcrItemSkuV0;
import com.hand.spc.ecr_main.view.EcrItemSkuV1;
import com.hand.spc.ecr_main.view.EcrItemSkuV2;

public interface IEcrItemSkuService extends IBaseService<EcrItemSku>, ProxySelf<IEcrItemSkuService>{

}