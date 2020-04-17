package com.hand.hqm.hqm_pfmea_level.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.hand.hap.core.IRequest;
import com.hand.hap.core.ProxySelf;
import com.hand.hap.system.dto.ResponseData;
import com.hand.hap.system.service.IBaseService;
import com.hand.hqm.hqm_dfmea_detail.dto.DfmeaDetail;
import com.hand.hqm.hqm_pfmea_level.dto.HqmLevelMenuItem;
import com.hand.hqm.hqm_pfmea_level.dto.PfmeaLevel;

public interface IPfmeaLevelService extends IBaseService<PfmeaLevel>, ProxySelf<IPfmeaLevelService>{

	 /**
     * @Author ruifu.jiang
     * @Description 查询附着对象层级维护 树形图数据
     * @Date 11:40 2019/8/26
     * @Param [requestContext, dto]
     */
    List<HqmLevelMenuItem> queryTreeData(IRequest requestContext, PfmeaLevel dto);
    /**
     * 新增保存(P)
     * @param dto 层级查询
     * @param request 请求
     * @return 结果集
     */
    ResponseData updateOrAdd(IRequest requestCtx, PfmeaLevel dto);
    /**
     * 新增保存(D)
     * @param dto 层级查询
     * @param request 请求
     * @return 结果集
     */
    ResponseData updateOrAdd_D(IRequest requestCtx, PfmeaLevel dto);
    /**
     * 提交
     * @param dto 层级查询
     * @param request 请求
     * @param requestCtx 请求
     * @return 结果集
     */
    ResponseData publish(PfmeaLevel dto,IRequest requestCtx, HttpServletRequest request);
    /**
     * 删除
     * @param dto 层级查询
     * @return 结果集
     */
    void deleteRow(PfmeaLevel dto);
}