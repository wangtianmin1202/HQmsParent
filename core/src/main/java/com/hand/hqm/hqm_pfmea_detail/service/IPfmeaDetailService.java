package com.hand.hqm.hqm_pfmea_detail.service;

import java.util.List;

import com.hand.hap.core.IRequest;
import com.hand.hap.core.ProxySelf;
import com.hand.hap.system.dto.ResponseData;
import com.hand.hap.system.service.IBaseService;
import com.hand.hqm.hqm_pfmea_detail.dto.HqmpdbMenuItemP;
import com.hand.hqm.hqm_pfmea_detail.dto.PfmeaDetail;

public interface IPfmeaDetailService extends IBaseService<PfmeaDetail>, ProxySelf<IPfmeaDetailService>{
	 /**
     * @Author ruifu.jiang
     * @Description 查询附着对象层级维护 树形图数据
     * @Date 11:40 2019/8/26
     * @Param [requestContext, dto]
     */
    List<HqmpdbMenuItemP> queryTreeData(IRequest requestContext, PfmeaDetail dto);
    
    
   

    /**
     * @Author ruifu.jiang
     * @Description 更新或者保存附着对象
     * @Date 19:40 2019/8/26
     * @Param [requestCtx, dto]
     */
    ResponseData updateOrAdd(IRequest requestCtx, PfmeaDetail dto);

    void deleteRow(PfmeaDetail dto);
    /**
     * @Author ruifu.jiang
     * @Description 提交
     * @Date 19:40 2019/8/26
     * @Param [requestCtx, dto]
     */
    ResponseData confirm(IRequest requestContext, List<Float> headList);
    /**
     * @Author ruifu.jiang
     * @Description 获取页面信息
     * @Date 19:40 2019/8/26
     * @Param [requestCtx, dto]
     */
    ResponseData getdata(IRequest requestContext, List<Long> headList);
    /**
     * @Author ruifu.jiang
     * @Description 页面查询
     * @Date 19:40 2019/8/26
     * @Param [requestCtx, dto,page,pagesize]
     */
    List<PfmeaDetail>  myselect(IRequest requestContext,PfmeaDetail dto,int page, int pageSize); 
    /**
     * @Author ruifu.jiang
     * @Description 获取打印数据
     * @Date 19:40 2019/8/26
     * @Param [requestCtx, dto,page,pagesize]
     */
    List<PfmeaDetail>  queryprintData(IRequest requestContext,PfmeaDetail dto,int page, int pageSize); 
    /**
     * @Author ruifu.jiang
     * @Description 查询查询数据
     * @Date 19:40 2019/8/26
     * @Param [kid]
     */
    PfmeaDetail queryHeaderData(Float kid);


    /**
     * @Author ruifu.jiang
     * @Description 获取Pfmea明细信息
     * @Date 19:40 2019/8/26
     * @Param [requestCtx, dto,page,pagesize]
     */

	List<PfmeaDetail> selectCondition(IRequest requestContext, PfmeaDetail dto, int page, int pageSize);
}