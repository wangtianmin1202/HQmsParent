package com.hand.hqm.hqm_sam_use_order_l.service.impl;

import com.github.pagehelper.PageHelper;
import com.hand.hap.core.IRequest;
import com.hand.hap.system.dto.ResponseData;
import com.hand.hap.system.service.impl.BaseServiceImpl;
import com.hand.hcs.hcs_delivery_ticket.mapper.DeliveryTicketLMapper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hand.hqm.hqm_sam_use_order_h.service.ISamUseOrderHService;
import com.hand.hqm.hqm_sam_use_order_h.service.impl.SamUseOrderHServiceImpl;
import com.hand.hqm.hqm_nonconforming_order.dto.NonconformingOrder;
import com.hand.hqm.hqm_sam_use_order_h.dto.SamUseOrderH;
import com.hand.hqm.hqm_sam_use_order_h.mapper.SamUseOrderHMapper;
import com.hand.hqm.hqm_sam_use_order_l.mapper.SamUseOrderLMapper;
import com.hand.hqm.hqm_sam_use_order_l.dto.SamUseOrderL;
import com.hand.hqm.hqm_sam_use_order_l.service.ISamUseOrderLService;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(rollbackFor = Exception.class)
public class SamUseOrderLServiceImpl extends BaseServiceImpl<SamUseOrderL> implements ISamUseOrderLService{
	 @Autowired
	 SamUseOrderLMapper samUseOrderLMapper;
	 @Autowired
	 SamUseOrderHMapper samUseOrderHMapper;
	 @Autowired
     private SamUseOrderLMapper mapper;
	 @Autowired
	 ISamUseOrderHService SamUseOrderHService;
	 /**
	     * 页面查询
	     * @param dto 查询内容
	     * @param page 页码
	     * @param pageSize 页大小
	     * @param request 请求
	     * @return 结果集
	     */
	 @Override
	    public List<SamUseOrderL> myselect(IRequest requestContext, SamUseOrderL dto, int page, int pageSize) {
	        PageHelper.startPage(page, pageSize);
	        return samUseOrderLMapper.myselect(dto);
		 }
	 /**
	     * 页面头行数据
	     * @param dto 
	     * @param request 请求
	     * @return 结果集
	     */
	 @Override
		public ResponseData saveHeadLine(IRequest requestContext, List<SamUseOrderL> dto) {
			//头操作
		      ResponseData responseData = new ResponseData();   
		     SamUseOrderH head = new SamUseOrderH();
		     SamUseOrderH head_receive =new SamUseOrderH();
		     head.setUseOrderStatus(dto.get(0).getUseOrderStatus());
		     head.setKid(dto.get(0).getKid());
			 head.setUseDepartment(dto.get(0).getUseDepartment());
			 head.setUseReason(dto.get(0).getUseReason());
			 head.setPlantId(dto.get(0).getPlantId());
			 head.setApplicant(dto.get(0).getApplicant());
			 head.setApplicationDate(dto.get(0).getApplicationDate());
			 head.setApprovalDate(dto.get(0).getApprovalDate());
			 head.setRemark(dto.get(0).getRemark());
			 head.setPrintFlag(dto.get(0).getPrintFlag());
			 head.setApprover(dto.get(0).getApprover());
		     head.setUseOrderNumber(dto.get(0).getUseOrderNumber());
		
		     //如果当前获取到的 单号为空 执行新增
		     if(dto.get(0).getUseOrderNumber()==null)
		     {
		    	    //生成单号
		    	    String inspectionNum;
					Date date = new Date();
					SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
					String time = dateFormat.format(date);
					String inspectionNumStart =  "A" + time.substring(2) + "-" ;
					// 流水一个检验单号			
					inspectionNum = getInspectionNumber(inspectionNumStart);
					head.setUseOrderNumber(inspectionNum);
		    	    head_receive = SamUseOrderHService.insertSelective(requestContext, head);
		     }
		     else
		     {
		    	    head_receive = SamUseOrderHService.updateByPrimaryKeySelective(requestContext, head);
		     }
			
			//行操作
			//如果行字段没有数据 就不保存行 只保存头
			if(dto.get(0).getSampleType()==null&&dto.get(0).getApplicationNum()==null&&dto.get(0).getIssueNum()==null)
			{
				
			}
			else
			{
			for(SamUseOrderL line : dto) {
				
				if((line.getLineId())==null)
				{
				SamUseOrderL lineData = new SamUseOrderL();
				SamUseOrderL lineData1 = new SamUseOrderL();
				lineData.setKid(head_receive.getKid());	
				lineData.setSampleType(line.getSampleType());
				lineData = mapper.selectOne(lineData);
				
				lineData1.setKid(head_receive.getKid());	
				lineData1.setSampleType(line.getSampleType());
				lineData1.setApplicationNum(line.getApplicationNum());
				lineData1.setIssueNum(line.getIssueNum());
				if(lineData == null) {
					//insert
					self().insertSelective(requestContext, lineData1);
				}else {
					//update
					responseData.setSuccess(false);
					responseData.setMessage("重复数据");
					return responseData;
				}
				}
				else
				{
					SamUseOrderL lineData1 = new SamUseOrderL();
					lineData1.setKid(head_receive.getKid());	
					lineData1.setSampleType(line.getSampleType());
					lineData1.setApplicationNum(line.getApplicationNum());
					lineData1.setIssueNum(line.getIssueNum());
					self().updateByPrimaryKeySelective(requestContext, lineData1);
				}
			}
			}
			dto.get(0).setKid(head_receive.getKid());
			dto.get(0).setUseOrderNumber(head_receive.getUseOrderNumber());
		
			responseData.setRows(dto);
			responseData.setSuccess(true);
			responseData.setMessage("成功");
			return responseData;
		}
	 /**
	     * 保存行数据
	     * @param dto 
	     * @param request 请求
	     * @return 结果集
	     */
	   @Override
		public List<SamUseOrderL> saveLine(IRequest requestContext, List<SamUseOrderL> dto) {		
		     			
			//行操作
		      
			for(SamUseOrderL line : dto) {					
				SamUseOrderL lineData = new SamUseOrderL();
				SamUseOrderL lineData1 = new SamUseOrderL();
				lineData.setKid(dto.get(0).getKid());	
				lineData.setSampleType(line.getSampleType());
				lineData.setApplicationNum(line.getApplicationNum());
				lineData.setIssueNum(line.getIssueNum());
				lineData = mapper.selectOne(lineData);
				
				lineData1.setKid(dto.get(0).getKid());	
				lineData1.setSampleType(line.getSampleType());
				lineData1.setApplicationNum(line.getApplicationNum());
				lineData1.setIssueNum(line.getIssueNum());
				if(lineData == null) {
					//insert
					self().insertSelective(requestContext, lineData1);
				}else {
					//update
					self().updateByPrimaryKeySelective(requestContext, lineData1);
				}
			}
			return dto;
		}
	   //获取流水
	   public String getInspectionNumber(String inspectionNumStart) {
			Integer count = 1;
			SamUseOrderH sr = new SamUseOrderH();
			sr.setUseOrderNumber(inspectionNumStart);
			List<SamUseOrderH> list = new ArrayList<SamUseOrderH>();
			list = samUseOrderHMapper.selectMaxNumber(sr);
			if (list != null && list.size() > 0) {
				String NoNum = list.get(0).getUseOrderNumber();
				String number = NoNum.substring(NoNum.length() - 5);// 流水
				count = Integer.valueOf(number) + 1;
			}
			String str = String.format("%05d", count);
			return inspectionNumStart + str;// 最终检验单号
		}
	   
	   @Override
		public ResponseData dealSaveLine(IRequest requestContext, List<SamUseOrderL> dto) {
		   ResponseData responseData = new ResponseData(); 
		   float anum =0;//申请数量
		   float snum =0;//实发数量
		   
		   float ApplicationNum=0;
		   float IssueNum=0;
		   float kid =dto.get(0).getKid();
			for(SamUseOrderL line : dto) {
				if(line.getApplicationNum()==null)
				{ApplicationNum=0;}
				else
				{ApplicationNum=line.getApplicationNum(); }
				if(line.getIssueNum()==null)
				{IssueNum=0;}
				else
				{IssueNum=line.getIssueNum();}
				
				anum =anum+ApplicationNum;
				snum=snum+IssueNum;
				 //更新行表 					
					mapper.updateByPrimaryKeySelective(line);				
			}
			//更新行表（跟新状态）
			if(anum>snum)
			{
				SamUseOrderH head = new SamUseOrderH();
				head = samUseOrderHMapper.selectByPrimaryKey(kid);
				head.setUseOrderStatus("E");
				SamUseOrderHService.updateByPrimaryKeySelective(requestContext, head);
			}
			else
			{
				SamUseOrderH head = new SamUseOrderH();
				head = samUseOrderHMapper.selectByPrimaryKey(kid);
				head.setUseOrderStatus("C");
				SamUseOrderHService.updateByPrimaryKeySelective(requestContext, head);
				
			}
			responseData.setSuccess(true);
			responseData.setMessage("成功");
			return responseData;
		}
}