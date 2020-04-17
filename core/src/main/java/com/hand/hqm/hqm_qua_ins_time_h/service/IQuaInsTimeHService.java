package com.hand.hqm.hqm_qua_ins_time_h.service;

import java.util.List;

import com.hand.hap.core.IRequest;
import com.hand.hap.core.ProxySelf;
import com.hand.hap.system.service.IBaseService;
import com.hand.hqm.hqm_qua_ins_time_h.dto.QuaInsTimeH;
import com.hand.hqm.hqm_standard_op_ins_h.dto.StandardOpInspectionH;

public interface IQuaInsTimeHService extends IBaseService<QuaInsTimeH>, ProxySelf<IQuaInsTimeHService>{
	
	/**
     * 头表数据查询
     * @param dto 查询内容
     * @param page 页码
     * @param pageSize 页大小
     * @param request 请求
     * @return 结果集
     */
	List<QuaInsTimeH>  myselect(IRequest requestContext,QuaInsTimeH dto,int page, int pageSize); 
}