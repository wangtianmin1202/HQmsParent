package com.hand.hqm.hqm_db_management.mapper;

import java.util.List;

import com.hand.hap.mybatis.common.Mapper;
import com.hand.hqm.hqm_db_management.dto.HQMInvalidTree;
import com.hand.hqm.hqm_dfmea_detail.dto.DfmeaDetail;

public interface HqmInvalidTreeMapper extends Mapper<HQMInvalidTree> {


    /**
     * @Author han.zhang
     * @Description 查询根对象
     * @Date 14:08 2019/8/19
     * @Param []
     * @param dto
     */
    List<HQMInvalidTree> selectParentInvalid(HQMInvalidTree dto);
 
    
    /**
     * @Author han.zhang
     * @Description 根据父级查询子集
     * @Date 14:08 2019/8/19
     * @Param []
     * @param dto
     */
    List<HQMInvalidTree> selectInvalidByParent(HQMInvalidTree dto);
    
    List<HQMInvalidTree> selectfromdata_r1(HQMInvalidTree dto);
    List<HQMInvalidTree> selectfromdata_r2r3(HQMInvalidTree dto);
    
    
    int checkStructure(HQMInvalidTree record);
    
    Long getparentIdfromthd(HQMInvalidTree dto);
    
    int checkFunctionAndInvalid(HQMInvalidTree record);
}