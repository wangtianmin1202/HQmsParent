package com.hand.spc.pspc_ce_relationship.mapper;

import com.hand.hap.mybatis.common.Mapper;
import com.hand.spc.pspc_ce_relationship.dto.CeRelationship;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CeRelationshipMapper extends Mapper<CeRelationship> {

    /**
     *
     * @Description 根据控制要素组ID查询关系
     *
     * @author yuchao.wang
     * @date 2019/8/9 19:46
     * @param ceGroupId
     * @return java.util.List<com.hand.spc.pspc_ce_relationship.dto.CeRelationship>
     *
     */
    List<CeRelationship> selectRelationshipByGroupId(@Param("ceGroupId") Long ceGroupId);

    /**
     *
     * @Description 通过唯一性条件查询关系
     *
     * @author yuchao.wang
     * @date 2019/9/5 14:26
     * @param ceRelationship
     * @return com.hand.spc.pspc_ce_relationship.dto.CeRelationship
     *
     */
    CeRelationship selectRelationshipForUniqueCheck(CeRelationship ceRelationship);
}