package com.hand.plm.plm_prod_design_standard_approve.service.impl;

import com.hand.hap.system.service.impl.BaseServiceImpl;
import org.springframework.stereotype.Service;
import com.hand.plm.plm_prod_design_standard_approve.dto.ProdDesignStandardApprove;
import com.hand.plm.plm_prod_design_standard_approve.service.IProdDesignStandardApproveService;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(rollbackFor = Exception.class)
public class ProdDesignStandardApproveServiceImpl extends BaseServiceImpl<ProdDesignStandardApprove> implements IProdDesignStandardApproveService{

}