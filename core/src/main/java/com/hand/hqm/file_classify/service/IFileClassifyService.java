package com.hand.hqm.file_classify.service;

import java.util.List;

import com.hand.hap.core.IRequest;
import com.hand.hap.core.ProxySelf;
import com.hand.hap.system.dto.ResponseData;
import com.hand.hap.system.service.IBaseService;
import com.hand.hqm.file_classify.dto.Classify;
import com.hand.hqm.file_classify.dto.MenuItem;
import com.hand.hqm.file_manager.dto.Manager;
import com.hand.hqm.hqm_dfmea_detail.dto.DfmeaDetail;
import com.hand.hqm.hqm_dfmea_detail.dto.HqmpdbMenuItem;

public interface IFileClassifyService extends IBaseService<Classify>, ProxySelf<IFileClassifyService>{

	 /**
     * @Author ruifu.jiang
     * @Description 查询附着对象层级维护 树形图数据
     * @Date 11:40 2019/8/26
     * @Param [requestContext, dto]
     */
    List<MenuItem> queryTreeData(IRequest requestContext, Classify dto);
    
    /**
     * @Author ruifu.jiang
     * @Description 更新或者保存附着对象
     * @Date 19:40 2019/8/26
     * @Param [requestCtx, dto]
     */
    ResponseData updateOrAdd(IRequest requestCtx, Classify dto);  
    /**
	 * @Author han.zhang
	 * @Description 查询第一层级数据
	 * @Date 18:10 2019/8/19
	 * @Param [dto, result, request]
	 */
    List<Classify>  myselect1(IRequest requestContext,Classify dto,int page, int pageSize); 
    /**
	 * @Author han.zhang
	 * @Description 查询第二层级数据
	 * @Date 18:10 2019/8/19
	 * @Param [dto, result, request]
	 */
    List<Classify>  myselect2(IRequest requestContext,Classify dto,int page, int pageSize); 
    /**
	 * @Author han.zhang
	 * @Description 查询第三层级数据
	 * @Date 18:10 2019/8/19
	 * @Param [dto, result, request]
	 */
    List<Classify>  myselect3(IRequest requestContext,Classify dto,int page, int pageSize); 
}