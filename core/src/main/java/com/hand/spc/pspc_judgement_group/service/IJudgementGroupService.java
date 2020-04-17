package com.hand.spc.pspc_judgement_group.service;

import com.hand.hap.core.IRequest;
import com.hand.hap.core.ProxySelf;
import com.hand.hap.system.dto.ResponseData;
import com.hand.hap.system.service.IBaseService;
import com.hand.spc.pspc_judgement_group.dto.JudgementGroup;

import java.util.List;

public interface IJudgementGroupService extends IBaseService<JudgementGroup>, ProxySelf<IJudgementGroupService>{


    /**
    * @Description 根据传进来的拷贝id去保存对应行表信息
    * @author hch
    * @date 2019/8/14 16:34
    * @Param [requestCtx, dto]
    * @return com.hand.hap.system.dto.ResponseData
    * @version 1.0
    */
    ResponseData copyData(IRequest requestCtx,List<JudgementGroup> dto);

    /**
    * @Description 判异规则组维护前台查询
    * @author hch
    * @date 2019/8/28 15:34
    * @Param [requestContext, dto, page, pageSize]
    * @return java.util.List<com.hand.spc.pspc_judgement_group.dto.JudgementGroup>
    * @version 1.0
    */
    List<JudgementGroup> MySelect(IRequest requestContext, JudgementGroup dto, int page, int pageSize);
}