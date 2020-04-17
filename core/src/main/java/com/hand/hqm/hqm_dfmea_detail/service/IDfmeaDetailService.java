package com.hand.hqm.hqm_dfmea_detail.service;

import java.util.Collection;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.multipart.MultipartFile;

import com.hand.hap.core.IRequest;
import com.hand.hap.core.ProxySelf;
import com.hand.hap.system.dto.ResponseData;
import com.hand.hap.system.service.IBaseService;
import com.hand.hcs.hcs_supply_demand.dto.SupplyDemand;
import com.hand.hqm.hqm_control_plan.dto.ControlPlanFeatures;
import com.hand.hqm.hqm_dfmea_detail.dto.DfmeaDetail;
import com.hand.hqm.hqm_dfmea_detail.dto.HqmpdbMenuItem;
import com.hand.hqm.hqm_fmea.dto.Fmea;
import com.hand.hqm.hqm_sample.dto.Sample;
public interface IDfmeaDetailService extends IBaseService<DfmeaDetail>, ProxySelf<IDfmeaDetailService>{

	
    /**
     * @Author ruifu.jiang
     * @Description 查询附着对象层级维护 树形图数据
     * @Date 11:40 2019/8/26
     * @Param [requestContext, dto]
     */
    List<HqmpdbMenuItem> queryTreeData(IRequest requestContext, DfmeaDetail dto);
    /**
     * @Author ruifu.jiang
     * @Description 更新或者保存附着对象
     * @Date 19:40 2019/8/26
     * @Param [requestCtx, dto]
     */
    ResponseData updateOrAdd(IRequest requestCtx, DfmeaDetail dto);
    /**
     * @Author ruifu.jiang
     * @Description 删除
     * @Date 19:40 2019/8/26
     * @Param [requestCtx, dto]
     */
    void deleteRow(DfmeaDetail dto);
    /**
     * @Author ruifu.jiang
     * @Description 提交
     * @Date 19:40 2019/8/26
     * @Param [requestCtx, dto]
     */
    ResponseData confirm(IRequest requestContext, List<Float> headList);
    /**
     * @Author ruifu.jiang
     * @Description 获取页面数据
     * @Date 19:40 2019/8/26
     * @Param [requestCtx, DFMEA明细数据集合]
     */
    ResponseData getdata(IRequest requestContext, List<Long> headList);
    /**
     * @Author ruifu.jiang
     * @Description 查询数据
     * @Date 19:40 2019/8/26
     * @Param [requestCtx, dto，page，pageSize]
     */
    List<DfmeaDetail>  myselect(IRequest requestContext,DfmeaDetail dto,int page, int pageSize); 
    /**
     * @Author ruifu.jiang
     * @Description 查询打印数据
     * @Date 19:40 2019/8/26
     * @Param [requestCtx, dto，page，pageSize]
     */
    List<DfmeaDetail>  queryprintData(IRequest requestContext,DfmeaDetail dto,int page, int pageSize); 
    /**
     * @Author ruifu.jiang
     * @Description 查询头表数据
     * @Date 19:40 2019/8/26
     * @Param [requestCtx, dto，page，pageSize]
     */
    DfmeaDetail queryHeaderData(Float kid);
    /**
     * @Author ruifu.jiang
     * @Description 查询数据
     * @Date 19:40 2019/8/26
     * @Param [requestCtx, dto，page，pageSize]
     */
	List<DfmeaDetail> queryCondition(IRequest requestContext, DfmeaDetail dto, int page, int pageSize);
	/**
	 * @description
	 * @author tianmin.wang
	 * @date 2019年12月9日 
	 * @param request
	 * @param requestCtx
	 * @param forModel
	 * @return
	 * @throws Exception 
	 */
	List<DfmeaDetail> detailExcelImport(HttpServletRequest request, IRequest requestCtx,
			MultipartFile forModel) throws Exception;
	/**
	 * @description
	 * @author tianmin.wang
	 * @date 2019年12月9日 
	 * @param valueOf
	 * @param request
	 * @param response
	 * @throws Exception 
	 */
	void exportExcel(Float fmeaId, HttpServletRequest request, HttpServletResponse response) throws Exception;
}
