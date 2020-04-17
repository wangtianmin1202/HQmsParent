package com.hand.hqm.hqm_control_plan.mapper;

import java.util.List;

import com.hand.hap.mybatis.common.Mapper;
import com.hand.hqm.hqm_control_plan.dto.ControlPlanFeaturesTree;

public interface ControlPlanFeaturesTreeMapper extends Mapper<ControlPlanFeaturesTree> {


    /**
     * @Author han.zhang
     * @Description 查询根对象
     * @Date 14:08 2019/8/19
     * @Param []
     * @param dto
     */
    List<ControlPlanFeaturesTree> selectParentFeatures(ControlPlanFeaturesTree dto);
    
    /**
     * @Author han.zhang
     * @Description 根据父级查询子集
     * @Date 14:08 2019/8/19
     * @Param []
     * @param dto
     */
    List<ControlPlanFeaturesTree> selectFeaturesByParent(ControlPlanFeaturesTree dto);
        
    int checkProcess(ControlPlanFeaturesTree record);
    
    int checkFeatures(ControlPlanFeaturesTree record);

	/**
	 * @Description:根据子集查询父级
	 * @param dto
	 * @return
	 */
	List<ControlPlanFeaturesTree> selectFeaturesByChild(ControlPlanFeaturesTree dto);
}