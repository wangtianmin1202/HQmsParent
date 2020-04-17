package com.hand.spc.pspc_judgement_group.service;

import com.hand.hap.core.IRequest;
import com.hand.hap.core.ProxySelf;
import com.hand.hap.system.dto.ResponseData;
import com.hand.hap.system.service.IBaseService;
import com.hand.spc.pspc_judgement_group.dto.Judgement;

import java.util.List;

public interface IJudgementService extends IBaseService<Judgement>, ProxySelf<IJudgementService>{

    List<Judgement> selectData(IRequest requestContext, Judgement dto, int page, int pageSize);

    ResponseData validateMustInput(IRequest requestCtx, List<Judgement> dto);
}