package com.hand.hqm.hqm_fmea_staff.service.impl;

import com.github.pagehelper.PageHelper;
import com.hand.hap.core.IRequest;
import com.hand.hap.system.dto.ResponseData;
import com.hand.hap.system.service.impl.BaseServiceImpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hand.hqm.file_permission.dto.Permission;
import com.hand.hqm.hqm_fmea.dto.Fmea;
import com.hand.hqm.hqm_fmea_staff.dto.FmeaStaff;
import com.hand.hqm.hqm_fmea_staff.mapper.FmeaStaffMapper;
import com.hand.hqm.hqm_fmea_staff.service.IFmeaStaffService;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(rollbackFor = Exception.class)
public class FmeaStaffServiceImpl extends BaseServiceImpl<FmeaStaff> implements IFmeaStaffService{
	@Autowired
	 FmeaStaffMapper  fmeaStaffMapper;
	/**
     * 页面查询
     * @param dto 查询内容
     * @param page 页码
     * @param pageSize 页大小
     * @param request 请求
     * @return 结果集
     */
	 @Override
	    public List<FmeaStaff> myselect(IRequest requestContext, FmeaStaff dto, int page, int pageSize) {
	        PageHelper.startPage(page, pageSize);
	        return fmeaStaffMapper.myselect(dto);
		 }
	 /**
	     * 新建员工
	     * @description
	     * @param dto
	     * @param request
	     * @return
	     */
		@Override
		public ResponseData  addNewInspection (IRequest requestCtx,List<FmeaStaff> dto) {
			ResponseData responseData = new ResponseData();
			for(FmeaStaff row:dto)
			{	
		     FmeaStaff fmeaStaff = new FmeaStaff();
		     fmeaStaff.setFmeaId(row.getFmeaId());
		     fmeaStaff.setStaffId(row.getStaffId());
		     int i = fmeaStaffMapper.selectCount(fmeaStaff);
		     if(i>0)
		     {
		    	    responseData.setSuccess(false);
					responseData.setMessage("该员工已存在!");
					return responseData; 
		     }
		     self().insertSelective(requestCtx, row);
		      
			 return responseData;
			}
			return null;
		}
}