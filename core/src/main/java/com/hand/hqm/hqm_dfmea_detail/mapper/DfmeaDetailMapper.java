package com.hand.hqm.hqm_dfmea_detail.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.hand.hap.mybatis.common.Mapper;
import com.hand.hqm.hqm_control_plan.dto.ControlPlanFeatures;
import com.hand.hqm.hqm_dfmea_detail.dto.DfmeaDetail;
import com.hand.hqm.hqm_fmea.dto.Fmea;

public interface DfmeaDetailMapper extends Mapper<DfmeaDetail>{

	
	 /**
     * @Author han.zhang
     * @Description 查询根对象
     * @Date 14:08 2019/8/19
     * @Param []
     * @param dto
     */
    List<DfmeaDetail> selectParentInvalid(DfmeaDetail dto);
    
    /**
     * @Author han.zhang
     * @Description 根据父级查询子集
     * @Date 14:08 2019/8/19
     * @Param []
     * @param dto
     */
    List<DfmeaDetail> selectInvalidByParent(DfmeaDetail dto);
    
    int checkStructure(DfmeaDetail record);
    
    int checkFunctionAndInvalid(DfmeaDetail record);
    
    Long getparentIdsec(DfmeaDetail dto);
    
    Long getparentIdthd(DfmeaDetail dto);
    
    Long getparentIdfromthd(DfmeaDetail dto);
    
    
    List<DfmeaDetail> myselect(DfmeaDetail dto);
    
    List<DfmeaDetail> queryprintData(DfmeaDetail dto);
    
    List<DfmeaDetail> commitSelect(DfmeaDetail dto);
    
    List<DfmeaDetail> Selectbyparentbranch(DfmeaDetail dto);
    
    DfmeaDetail queryHeaderData(Float kid);

	List<DfmeaDetail> queryCondition(DfmeaDetail dto);

	/**
	 * @Description:修改
	 * @param dto
	 */
	void updateDfmeaDetail(DfmeaDetail dto);
}