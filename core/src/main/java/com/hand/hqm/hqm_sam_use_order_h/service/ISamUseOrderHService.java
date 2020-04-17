package com.hand.hqm.hqm_sam_use_order_h.service;

import java.util.List;

import com.hand.hap.core.IRequest;
import com.hand.hap.core.ProxySelf;
import com.hand.hap.system.service.IBaseService;
import com.hand.hcs.hcs_delivery_ticket.dto.DeliveryTicketH;
import com.hand.hqm.hqm_sam_use_order_h.dto.SamUseOrderH;
import com.hand.hqm.hqm_sam_use_order_l.dto.SamUseOrderL;

public interface ISamUseOrderHService extends IBaseService<SamUseOrderH>, ProxySelf<ISamUseOrderHService>{
	 /**
     * 行表数据查询
     * @param dto 查询内容
     * @param page 页码
     * @param pageSize 页大小
     * @param request 请求
     * @return 结果集
     */
	List<SamUseOrderH>  myselect(IRequest requestContext,SamUseOrderH dto,int page, int pageSize); 
	 /**
     * 根据主键查询头表数据
     * @param dto 查询内容
     * @param request 请求
     * @return 结果集
     */
    List<SamUseOrderH> queryByTicketId(IRequest requestContext,SamUseOrderH samUseOrderH);
    /**
     * 根据主键查询头表数据
     * @param dto 查询内容
     * @param request 请求
     * @return 结果集
     */
    List<SamUseOrderH> saveHead(IRequest requestContext, List<SamUseOrderH> dto);
}