package com.hand.hqm.hqm_db_p_management.mapper;

import java.util.List;

import com.hand.hap.mybatis.common.Mapper;
import com.hand.hqm.hqm_db_management.dto.HQMInvalidTree;
import com.hand.hqm.hqm_db_p_management.dto.HQMPInvalidTree;

public interface HqmpInvalidTreeMapper extends Mapper<HQMPInvalidTree> {


    /**
     * @Author han.zhang
     * @Description 查询根对象
     * @Date 14:08 2019/8/19
     * @Param []
     * @param dto
     */
    List<HQMPInvalidTree> selectParentInvalid(HQMPInvalidTree dto);
    
    /**
     * @Author han.zhang
     * @Description 根据父级查询子集
     * @Date 14:08 2019/8/19
     * @Param []
     * @param dto
     */
    List<HQMPInvalidTree> selectInvalidByParent(HQMPInvalidTree dto);
    
    int checkStructure(HQMPInvalidTree record);
    
    int checkFunctionAndInvalid(HQMPInvalidTree record);
    
    List<HQMPInvalidTree> selectfromdata_r1(HQMPInvalidTree dto);
    List<HQMPInvalidTree> selectfromdata_r2r3(HQMPInvalidTree dto);
    Long getparentIdfromthd(HQMPInvalidTree dto);
}