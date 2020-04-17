package com.hand.plm.spec_product_detail.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.hand.hap.core.IRequest;
import com.hand.hap.core.ProxySelf;
import com.hand.hap.system.service.IBaseService;
import com.hand.plm.spec_product_detail.dto.SpecProductDetail;

public interface ISpecProductDetailService extends IBaseService<SpecProductDetail>, ProxySelf<ISpecProductDetailService>{
	/**
	 * 查询左边树状层级
	 * @param request
	 * @return
	 */
	List<SpecProductDetail> selectTreeDatas(IRequest request);
	
	/**
	 * 查询右边表格数据
	 * @param request
	 * @return
	 */
	List<SpecProductDetail> queryAll(IRequest request, SpecProductDetail condition, int pageNum, int pageSize);
	
	 /**
     *	 按条件导出数据
     * @param iRequest
     * @param condition
     * @param request
     * @param response
     */
    void  exportData(IRequest iRequest, SpecProductDetail condition, HttpServletRequest request, HttpServletResponse response);
    
    /**
     * 	批量保存
     */
    List<SpecProductDetail> batchUpdateDetail(IRequest iRequest, List<SpecProductDetail> details);
    
    /**
     * 批量删除
     */
    void updateSpecLineIdStatus(IRequest iRequest, List<SpecProductDetail> details);
    
    /**
     * 	查询下级
     */
    List<SpecProductDetail> selectTreeChild(IRequest iRequest, List<SpecProductDetail> details);
    
    /**
     * 	查询层级联动
     * @param iRequest
     * @param details
     * @return
     */
    List<SpecProductDetail> selectLevelInfo(IRequest iRequest, Long levelNum, Long parentSpecId);
    
    /**
     * 	查询草稿信息
     * @param request
     * @param changeType
     * @return
     */
    List<SpecProductDetail> queryAllPendding(IRequest request, String changeType);
    
    /**
     * 	查询首页父级数据
     * @param request
     * @param changeType
     * @return
     */
    List<SpecProductDetail> selectParentTree(IRequest request);
    
    /**
  	  *  查询废止数据
  	  * @return
  	  */
  	 Long hisCount(IRequest request);
  	 
     /**
      * 	批量变更数据
      */
     void batchUpdateSpecChange(IRequest iRequest, List<SpecProductDetail> details);
}



