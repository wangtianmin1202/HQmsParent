package com.hand.spc.pspc_ce_parameter.mapper;

import com.hand.hap.mybatis.common.Mapper;
import com.hand.spc.pspc_ce_parameter.dto.CeParameter;

import java.util.List;

public interface CeParameterMapper extends Mapper<CeParameter>{

    /**
     *
     * @Description 根据条件查询控制要素
     *
     * @author yuchao.wang
     * @date 2019/8/9 17:08
     * @param dto
     * @return java.util.List<com.hand.spc.pspc_ce_parameter.dto.CeParameter>
     *
     */
    List<CeParameter> selectCeParameterByGroupId(CeParameter dto);

    /**
     * @Author han.zhang
     * @Description 控制要lov查询
     * @Date 21:24 2019/8/12
     * @Param 可选参数 dto.ceGroupId，dto.ceParameter，dto.remark，dto.ceParameterName
     */
    List<CeParameter> slectCeParamterLov(CeParameter dto);

    /**
     *
     * @Description 模糊查询控制要素
     *
     * @author yuchao.wang
     * @date 2019/8/21 22:43
     * @param dto
     * @return java.util.List<com.hand.spc.pspc_ce_parameter.dto.CeParameter>
     *
     */
    List<CeParameter> selectCeParameterSelective(CeParameter dto);
}