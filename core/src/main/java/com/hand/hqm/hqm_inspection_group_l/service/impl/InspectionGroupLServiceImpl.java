package com.hand.hqm.hqm_inspection_group_l.service.impl;

import com.github.pagehelper.PageHelper;
import com.hand.hap.core.IRequest;
import com.hand.hap.core.annotation.StdWho;
import com.hand.hap.system.dto.DTOStatus;
import com.hand.hap.system.dto.ResponseData;
import com.hand.hap.system.service.impl.BaseServiceImpl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.xpath.operations.Bool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hand.hqm.hqm_inspection_group_h.dto.InspectionGroupH;
import com.hand.hqm.hqm_inspection_group_h.mapper.InspectionGroupHMapper;
import com.hand.hqm.hqm_inspection_group_h.service.IInspectionGroupHService;
import com.hand.hqm.hqm_inspection_group_l.dto.InspectionGroupL;
import com.hand.hqm.hqm_inspection_group_l.mapper.InspectionGroupLMapper;
import com.hand.hqm.hqm_inspection_group_l.service.IInspectionGroupLService;
import com.hand.hqm.hqm_iqc_inspection_template_h.dto.IqcInspectionTemplateH;
import com.hand.hqm.hqm_iqc_inspection_template_l.dto.IqcInspectionTemplateL;

import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(rollbackFor = Exception.class)
public class InspectionGroupLServiceImpl extends BaseServiceImpl<InspectionGroupL> implements IInspectionGroupLService{
	@Autowired
	InspectionGroupLMapper  inspectionGroupLMapper;
	@Autowired
	InspectionGroupHMapper  inspectionGroupHMapper;
	@Autowired
	IInspectionGroupHService  inspectionGroupHService;
	
	@Override
	public ResponseData saveHead(IRequest requestContext, InspectionGroupL dto) {
		 //头操作
	     ResponseData responseData = new ResponseData();   
	     InspectionGroupH head = new InspectionGroupH();
	     InspectionGroupH head_check = new InspectionGroupH();
	     if(dto.getGroupId()==null)
	     {	 
	     //新增
	     head_check.setGroupCode(dto.getGroupCode());
	     //校验头唯一性
	     List<InspectionGroupH> l  = inspectionGroupHMapper.select(head_check);
	     if(l.size()==0)	 
	     {    
	     head.setPlantId(dto.getPlantId());
	     head.setGroupCode(dto.getGroupCode());
		 head.setDescription(dto.getDescription());
		 head.setEnableFlag("Y");
		 head.setRemark(dto.getHeadRemark());
		 
		 inspectionGroupHService.insertSelective(requestContext, head);
	     }
	     }
	     else
	     {
	    	 //修改
		     head.setGroupId(dto.getGroupId());
		     head.setPlantId(dto.getPlantId()); 
		     head.setGroupCode(dto.getGroupCode());
			 head.setDescription(dto.getDescription());
			 head.setEnableFlag("Y");
			 head.setRemark(dto.getHeadRemark());
	    	 
			 inspectionGroupHService.updateByPrimaryKey(requestContext, head); 
	     }
	     List<InspectionGroupH> lr = inspectionGroupHMapper.select(head);
			responseData.setRows(lr);
			responseData.setSuccess(true);
			responseData.setMessage("成功");
			return responseData;
	}
	
	@Override
	public List<InspectionGroupL> historynumberUpdate(IRequest request, @StdWho List<InspectionGroupL> list)		  
			throws Exception {	
		List<InspectionGroupL> returnList = new ArrayList<InspectionGroupL>();
		for (InspectionGroupL t : list) {
			switch (t.get__status()) {
			case DTOStatus.ADD:
				self().insertSelective(request, t);
				returnList.add(t);
				break;
			case DTOStatus.UPDATE:
				self().updateByPrimaryKey(request, t);
				returnList.add(t);
				break;
			case DTOStatus.DELETE:
				self().deleteByPrimaryKey(t);
				break;
			default:
				break;
			}
		}
			for (InspectionGroupL t : list) {
				InspectionGroupL sear = new InspectionGroupL();
				sear.setGroupId(t.getGroupId());
				sear.setAttributeId(t.getAttributeId());
				List<InspectionGroupL> resli = inspectionGroupLMapper.select(sear);
				if (resli != null && resli.size() > 1) {
					throw new Exception("存在重复数据");
				}
			}
		   return returnList;
        }	
	
	@Override
	public ResponseData updateLine(IRequest requestContext, List<InspectionGroupL> dto) {
		  //批量更新
	     ResponseData responseData = new ResponseData(); 
	     for(InspectionGroupL line : dto){
	    	 InspectionGroupL lineData1 = new InspectionGroupL();
				lineData1.setLineId(line.getLineId());
				lineData1.setPrecision(line.getPrecision());
				lineData1.setStandradFrom(line.getStandradFrom());
				lineData1.setStandradTo(line.getStandradFrom());
				lineData1.setStandradUom(line.getStandradUom());
				lineData1.setInspectionTool(line.getInspectionTool());
				lineData1.setInspectionMethod(line.getInspectionMethod());
				lineData1.setFillInType(line.getFillInType());
				lineData1.setSampleWayId(line.getSampleWayId());
				lineData1.setEnableType(line.getEnableType());
				lineData1.setEnableTime(line.getEnableTime());
				lineData1.setDisableTime(line.getDisableTime());
				self().updateByPrimaryKeySelective(requestContext, lineData1);
	    	 
	    	 
	     }
	 	 return responseData;
	}
	
	
	 @Override
	    public List<InspectionGroupL> myselect(IRequest requestContext, InspectionGroupL dto, int page, int pageSize) {
	        PageHelper.startPage(page, pageSize);
	        return inspectionGroupLMapper.myselect(dto);
		 } 
	 @Override
	    public List<InspectionGroupL> selectheadincopy(IRequest requestContext, InspectionGroupL dto, int page, int pageSize) {
	        PageHelper.startPage(page, pageSize);
	        return inspectionGroupLMapper.selectheadincopy(dto);
		 } 
	 @Override
	    public List<InspectionGroupL> selectTb(IRequest requestContext, InspectionGroupL dto, int page, int pageSize) {
	        PageHelper.startPage(page, pageSize);
	        return inspectionGroupLMapper.selectTb(dto);
		 } 
	 
	 @Override
	    public List<InspectionGroupL> selecthead(IRequest requestContext, InspectionGroupL dto, int page, int pageSize) {
	        PageHelper.startPage(page, pageSize);
	        return inspectionGroupLMapper.selecthead(dto);
		 } 
}