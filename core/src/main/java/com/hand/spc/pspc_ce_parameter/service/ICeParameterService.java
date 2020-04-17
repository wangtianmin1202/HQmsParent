package com.hand.spc.pspc_ce_parameter.service;

import com.hand.hap.core.IRequest;
import com.hand.hap.core.ProxySelf;
import com.hand.hap.system.dto.ResponseData;
import com.hand.hap.system.service.IBaseService;
import com.hand.spc.pspc_ce_parameter.dto.CeParameter;

import java.util.List;

public interface ICeParameterService extends IBaseService<CeParameter>, ProxySelf<ICeParameterService>{

    ResponseData selectCeParameter(IRequest requestContext, CeParameter dto, int page, int pageSize);

    /**
     * @Author han.zhang
     * @Description 新增或者更新控制要素
     * @Date 10:33 2019/8/8
     * @Param [requestContext, dto]
     */
    ResponseData saveAndUpdateClassify(IRequest requestContext, CeParameter dto);

    /**
     *
     * @Description 根据条件查询控制要素
     *
     * @author yuchao.wang
     * @date 2019/8/9 17:05
     * @param requestContext
     * @param dto
     * @param page
     * @param pageSize
     * @return java.util.List<com.hand.spc.pspc_ce_parameter.dto.CeParameter>
     *
     */
    List<CeParameter> selectCeParameterByGroupId(IRequest requestContext, CeParameter dto, int page, int pageSize);

    /**
     *
     * @Description 删除控制要素
     *
     * @author yuchao.wang
     * @date 2019/8/10 15:10
     * @param dto
     * @return com.hand.hap.system.dto.ResponseData
     *
     */
    ResponseData deleteCeParameter(List<CeParameter> dto) throws Exception;

    /**
     *
     * @Description 模糊查询控制要素
     *
     * @author yuchao.wang
     * @date 2019/8/21 22:41
     * @param requestContext
     * @param dto
     * @param page
     * @param pageSize
     * @return java.util.List<com.hand.spc.pspc_ce_parameter.dto.CeParameter>
     *
     */
    List<CeParameter> selectCeParameterSelective(IRequest requestContext, CeParameter dto, int page, int pageSize);
}