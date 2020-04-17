package com.hand.spc.pspc_ce_group.mapper;

import com.hand.hap.mybatis.common.Mapper;
import com.hand.spc.pspc_ce_group.dto.CeGroup;

import java.util.List;

public interface CeGroupMapper extends Mapper<CeGroup> {

    /**
     *
     * @Description 模糊查询控制要素组
     *
     * @author yuchao.wang
     * @date 2019/8/21 22:12
     * @param dto
     * @return java.util.List<com.hand.spc.pspc_ce_group.dto.CeGroup>
     *
     */
    List<CeGroup> selectCeGroup(CeGroup dto);
}