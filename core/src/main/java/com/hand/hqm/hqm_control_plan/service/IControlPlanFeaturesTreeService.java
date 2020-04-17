package com.hand.hqm.hqm_control_plan.service;

import java.util.List;

import com.hand.hap.core.IRequest;
import com.hand.hap.core.ProxySelf;
import com.hand.hap.system.dto.ResponseData;
import com.hand.hap.system.service.IBaseService;
import com.hand.hqm.hqm_control_plan.dto.ControlPlanFeaturesMenuItem;
import com.hand.hqm.hqm_control_plan.dto.ControlPlanFeaturesTree;

public interface IControlPlanFeaturesTreeService extends IBaseService<ControlPlanFeaturesTree>, ProxySelf<IControlPlanFeaturesTreeService> {

    /**
     * @Author han.zhang
     * @Description 查询附着对象层级维护 树形图数据
     * @Date 11:40 2019/8/19
     * @Param [requestContext, dto]
     */
    List<ControlPlanFeaturesMenuItem> queryTreeData(IRequest requestContext, ControlPlanFeaturesTree dto);

    /**
     * @Author han.zhang
     * @Description 更新或者保存附着对象
     * @Date 19:40 2019/8/19
     * @Param [requestCtx, dto]
     */
    ResponseData updateOrAdd(IRequest requestCtx, ControlPlanFeaturesTree dto);

    void deleteRow(IRequest requestCtx,ControlPlanFeaturesTree dto);
    /**
     * 更新
     * @param requestContext
     * @param dto
     * @return
     */
    List<ControlPlanFeaturesTree> changeDatas(IRequest requestContext, List<ControlPlanFeaturesTree> dto);

	/**
	 * @Description:修改还原
	 * @param requestCtx
	 * @param dto
	 */
	void restoreRow(IRequest requestCtx, ControlPlanFeaturesTree dto);
}