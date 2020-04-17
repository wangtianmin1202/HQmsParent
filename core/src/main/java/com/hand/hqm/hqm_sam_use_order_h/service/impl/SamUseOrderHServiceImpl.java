package com.hand.hqm.hqm_sam_use_order_h.service.impl;

import com.github.pagehelper.PageHelper;
import com.hand.hap.core.IRequest;
import com.hand.hap.system.service.impl.BaseServiceImpl;
import com.hand.hcs.hcs_delivery_ticket.dto.DeliveryTicketH;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.hand.hqm.hqm_sam_use_order_h.dto.SamUseOrderH;
import com.hand.hqm.hqm_sam_use_order_h.mapper.SamUseOrderHMapper;
import com.hand.hqm.hqm_sam_use_order_h.service.ISamUseOrderHService;
import com.hand.hqm.hqm_sam_use_order_l.dto.SamUseOrderL;

import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(rollbackFor = Exception.class)
public class SamUseOrderHServiceImpl extends BaseServiceImpl<SamUseOrderH> implements ISamUseOrderHService{

	 @Autowired
	 SamUseOrderHMapper samUseOrderHMapper;
	 @Autowired
	 ISamUseOrderHService SamUseOrderHService;
	 /**
	     * 行表数据查询
	     * @param dto 查询内容
	     * @param page 页码
	     * @param pageSize 页大小
	     * @param request 请求
	     * @return 结果集
	     */
	 @Override
	    public List<SamUseOrderH> myselect(IRequest requestContext, SamUseOrderH dto, int page, int pageSize) {
	        PageHelper.startPage(page, pageSize);
	        return samUseOrderHMapper.myselect(dto);
		 }
	 
	 /**
	     * 根据主键查询头表数据
	     * @param dto 查询内容
	     * @param request 请求
	     * @return 结果集
	     */
	 @Override
		public List<SamUseOrderH> queryByTicketId(IRequest requestContext, SamUseOrderH samUseOrderH) {
			// TODO Auto-generated method stub
			return samUseOrderHMapper.queryByTicketId(samUseOrderH);
		}
	 /**
	     * 根据主键查询头表数据
	     * @param dto 查询内容
	     * @param request 请求
	     * @return 结果集
	     */
	 
	 @Override
		public List<SamUseOrderH> saveHead(IRequest requestContext, List<SamUseOrderH> dto) {		
		     			
		 SamUseOrderH head = new SamUseOrderH();
	     head.setUseOrderNumber(dto.get(0).getUseOrderNumber());
		 List<SamUseOrderH> headList = SamUseOrderHService.myselect(requestContext, head, 0, 0);
		if(headList.size()>0) {
			 head = headList.get(0);
			 head.setUseOrderStatus(dto.get(0).getUseOrderStatus());
			 head.setUseDepartment(dto.get(0).getUseDepartment());
			 head.setUseReason(dto.get(0).getUseReason());
			 head.setPlantId(dto.get(0).getPlantId());
			 head.setApplicant(dto.get(0).getApplicant());
			 head.setApplicationDate(dto.get(0).getApplicationDate());
			 head.setApprovalDate(new Date());
			 head.setRemark(dto.get(0).getRemark());
			 head.setPrintFlag(dto.get(0).getPrintFlag());
			 head.setApprover((float)requestContext.getUserId());
			//update
			 head = self().updateByPrimaryKeySelective(requestContext, head);
		    }else {
			//insert
			head =  self().insertSelective(requestContext, head);
		        }
			return dto;
		}
}