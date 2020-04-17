package com.hand.hqm.hqm_fmea_staff.service;

import java.util.List;

import com.hand.hap.core.IRequest;
import com.hand.hap.core.ProxySelf;
import com.hand.hap.system.dto.ResponseData;
import com.hand.hap.system.service.IBaseService;
import com.hand.hqm.file_permission.dto.Permission;
import com.hand.hqm.hqm_fmea.dto.Fmea;
import com.hand.hqm.hqm_fmea_staff.dto.FmeaStaff;

public interface IFmeaStaffService extends IBaseService<FmeaStaff>, ProxySelf<IFmeaStaffService>{
	/**
     * 页面查询
     * @param dto 查询内容
     * @param page 页码
     * @param pageSize 页大小
     * @param request 请求
     * @return 结果集
     */
	List<FmeaStaff>  myselect(IRequest requestContext,FmeaStaff dto,int page, int pageSize);
	 /**
     * 新建
     * @description
     * @param dto
     * @param request
     * @return
     */
	ResponseData addNewInspection(IRequest requestContext, List<FmeaStaff> dto);
}