package com.hand.hqm.hqm_inspection_group_h.service.impl;

import com.github.pagehelper.PageHelper;
import com.hand.hap.core.IRequest;
import com.hand.hap.system.dto.ResponseData;
import com.hand.hap.system.service.IPromptService;
import com.hand.hap.system.service.impl.BaseServiceImpl;
import com.hand.hap.util.SystemApiMethod;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hand.hqm.hqm_inspection_group_h.dto.InspectionGroupH;
import com.hand.hqm.hqm_inspection_group_h.mapper.InspectionGroupHMapper;
import com.hand.hqm.hqm_inspection_group_h.service.IInspectionGroupHService;
import com.hand.hqm.hqm_inspection_group_l.dto.InspectionGroupL;
import com.hand.hqm.hqm_inspection_group_l.mapper.InspectionGroupLMapper;
import com.hand.hqm.hqm_iqc_inspection_template_h.dto.IqcInspectionTemplateH;
import com.hand.hqm.hqm_iqc_inspection_template_h.mapper.IqcInspectionTemplateHMapper;
import com.hand.hqm.hqm_iqc_inspection_template_h.service.IIqcInspectionTemplateHService;
import com.hand.hqm.hqm_iqc_inspection_template_l.dto.IqcInspectionTemplateL;
import com.hand.hqm.hqm_iqc_inspection_template_l.mapper.IqcInspectionTemplateLMapper;
import com.hand.hqm.hqm_iqc_inspection_template_l.service.IIqcInspectionTemplateLService;

import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(rollbackFor = Exception.class)
public class InspectionGroupHServiceImpl extends BaseServiceImpl<InspectionGroupH> implements IInspectionGroupHService{
	
	 @Autowired
	 InspectionGroupHMapper inspectionGroupHMapper;
	 @Autowired
	 IqcInspectionTemplateHMapper iqcInspectionTemplateHMapper;
	 @Autowired
	 IIqcInspectionTemplateHService IqcInspectionTemplateHService;
	 @Autowired 
	 IqcInspectionTemplateLMapper  iqcInspectionTemplateLMapper;
	 @Autowired
	 IIqcInspectionTemplateLService IqcInspectionTemplateLService;
	 @Autowired
	 InspectionGroupLMapper   inspectionGroupLMapper;
	 @Autowired
	 IPromptService iPromptService;
	 @Override
	    public List<InspectionGroupH> myselect(IRequest requestContext, InspectionGroupH dto, int page, int pageSize) {
	        PageHelper.startPage(page, pageSize);
	        return inspectionGroupHMapper.myselect(dto);
		 } 
	 

	 
	 @Override
	    public List<InspectionGroupH> itemselect(IRequest requestContext, InspectionGroupH dto, int page, int pageSize) {
	        PageHelper.startPage(page, pageSize);
	        return inspectionGroupHMapper.itemselect(dto);
		 } 
	 
	 /*
	  * 分配
	  * by jrf 9/23
	  * 
	  * */
	 @Override
		public ResponseData distribute(InspectionGroupH dto, IRequest requestCtx, HttpServletRequest request) {
			ResponseData responseData = new ResponseData();
			
			InspectionGroupH inspectionGroupH =new InspectionGroupH();
			IqcInspectionTemplateH iqcInspectionTemplateH = new IqcInspectionTemplateH();
			//校验抽样方式
			InspectionGroupL inspectionGroupl =new InspectionGroupL();
			inspectionGroupl.setGroupId(dto.getGroupId());
			int n =inspectionGroupLMapper.selectcount(inspectionGroupl);
			if(n>0)
			{
				responseData.setSuccess(false);
//				responseData.setMessage(SystemApiMethod.getPromptDescription(request,iPromptService,"error.hqm_group_distribute_01"));
				responseData.setMessage(SystemApiMethod.getPromptDescription(request,iPromptService,"error.hqm_group_distribute_01"));
				return responseData;
			}			
			//获取物料的数组
			String str = dto.getItemId();
	        String[] ids=str.split(",");
			//获取来源类型的数组
	        String str2 = dto.getAtrributeMain();
	        String[] mainType=str2.split(",");
			/*分四种情况*/
	        /*1.覆盖式完全同步，status = 'A' 若物料已有检验单模板，则删除已有模板， 将检验组的检验项目信息传进去*/
	        if(dto.getStyle().equals("A"))
	        {   
	        //循环每个物料
	           for(int i = 0 ;i<ids.length;i++ )
	           {   
	        	   inspectionGroupH.setGroupId(dto.getGroupId());   
	        	   inspectionGroupH=inspectionGroupHMapper.selectByPrimaryKey(inspectionGroupH);
	        	   iqcInspectionTemplateH.setItemId(Float.valueOf(ids[i]));
	        	   iqcInspectionTemplateH.setPlantId(inspectionGroupH.getPlantId());
	        	  for(int j = 0 ;j<mainType.length;j++)
	        	  {	  
	        	   iqcInspectionTemplateH.setSourceType(mainType[j]);
	        	   List<IqcInspectionTemplateH> lA =iqcInspectionTemplateHMapper.select(iqcInspectionTemplateH);
	        	   //若检验单头表存在该物料，那么进行覆盖更新      行表先删 后增 
	        	   if(lA.size()==1)
	        	   {
	        		   IqcInspectionTemplateH iA =new IqcInspectionTemplateH();
	        		   iA.setHeaderId(lA.get(0).getHeaderId());
	        		   iA.setPlantId(lA.get(0).getPlantId());
	        		   iA.setItemId(lA.get(0).getItemId());
	        		   iA.setItemCategory(lA.get(0).getItemCategory());
	        		   iA.setVersionNum(String.valueOf(Float.valueOf(lA.get(0).getVersionNum())+1));
	        		   iA.setDrawingNumber(lA.get(0).getDrawingNumber());
	        		   iA.setEnableFlag(lA.get(0).getEnableFlag());
	        		   iA.setHistoryNum(lA.get(0).getHistoryNum());
	        		   iA.setStatus("1");
	        		   iA.setSourceType(mainType[j]);
	        		   iA.setTimeLimit(dto.getTimeLimit());
	        		   iA.setMainType(dto.getType());
	        		   IqcInspectionTemplateHService.updateByPrimaryKeySelective(requestCtx,iA);
	        		   
	        		   //删除行表
	        		   IqcInspectionTemplateL iqcInspectionTemplateL =new IqcInspectionTemplateL();
	        		   iqcInspectionTemplateL.setHeaderId(iA.getHeaderId());
	        		   iqcInspectionTemplateLMapper.delete(iqcInspectionTemplateL);
	        		   
	        		   //新增行
	        		   InspectionGroupL inspectionGroupL =new InspectionGroupL();
	        		   inspectionGroupL.setGroupId(dto.getGroupId());
	        		   List<InspectionGroupL> lisrgl  =inspectionGroupLMapper.select(inspectionGroupL);
	        		   if(lisrgl.size()>0)
	        		   {
	        			   for(int z = 0 ; z < lisrgl.size(); z++)
	        			   {
	        				   iqcInspectionTemplateL.setHeaderId(iA.getHeaderId());
	        				   iqcInspectionTemplateL.setAttributeId(lisrgl.get(z).getAttributeId());
	        				   iqcInspectionTemplateL.setPrecision(lisrgl.get(z).getPrecision());
	        				   iqcInspectionTemplateL.setStandradFrom(lisrgl.get(z).getStandradFrom());
	        				   iqcInspectionTemplateL.setStandradTo(lisrgl.get(z).getStandradTo());
	        				   iqcInspectionTemplateL.setStandradUom(lisrgl.get(z).getStandradUom());
	        				   iqcInspectionTemplateL.setTextStandrad(lisrgl.get(z).getTextStandrad());
	        				   iqcInspectionTemplateL.setInspectionTool(lisrgl.get(z).getInspectionTool());
	        				   iqcInspectionTemplateL.setInspectionMethod(lisrgl.get(z).getInspectionMethod());
	        				   iqcInspectionTemplateL.setFillInType(lisrgl.get(z).getFillInType());
	        				   iqcInspectionTemplateL.setSampleWayId(lisrgl.get(z).getSampleWayId());
	        				   iqcInspectionTemplateL.setEnableType("D");
	        				   if(lisrgl.get(z).getEnableTime()==null)
	        				   {iqcInspectionTemplateL.setEnableTime(new Date());
	        					   }
	        				   else
	        				   {
	        				   iqcInspectionTemplateL.setEnableTime(lisrgl.get(z).getEnableTime());
	        				   }
	        				   iqcInspectionTemplateL.setDisableTime(lisrgl.get(z).getDisableTime());
	        				   iqcInspectionTemplateL.setEnableFlag(lisrgl.get(z).getEnableFlag());
	        				   iqcInspectionTemplateL.setIsSync("Y");
	        				   IqcInspectionTemplateLService.insertSelective(requestCtx, iqcInspectionTemplateL);
	        			   }
	        		   }
	        		   
	        		   
	        	   }
	        	   //新增(直接新增行)
	        	   else
	        	   {
	        		   IqcInspectionTemplateH iA =new IqcInspectionTemplateH();
	        		   iA.setPlantId(inspectionGroupH.getPlantId());
	        		   iA.setItemId(Float.valueOf(ids[i]));
	        		   iA.setVersionNum(String.valueOf(1));
	        		   iA.setEnableFlag("Y");
	        		   iA.setStatus("1");
	        		   iA.setSourceType(mainType[j]);
	        		   iA.setTimeLimit(dto.getTimeLimit());
	        		   iA.setMainType(dto.getType());
	        		   IqcInspectionTemplateHService.insertSelective(requestCtx, iA);
	        	    
	        		   //新增行
	        		   IqcInspectionTemplateL iqcInspectionTemplateL =new IqcInspectionTemplateL();
	        		   InspectionGroupL inspectionGroupL =new InspectionGroupL();
	        		   inspectionGroupL.setGroupId(dto.getGroupId());
	        		   List<InspectionGroupL> lisrgl  =inspectionGroupLMapper.select(inspectionGroupL);
	        		   if(lisrgl.size()>0)
	        		   {
	        			   for(int z = 0 ; z < lisrgl.size(); z++)
	        			   {
	        				   iqcInspectionTemplateL.setHeaderId(iA.getHeaderId());
	        				   iqcInspectionTemplateL.setAttributeId(lisrgl.get(z).getAttributeId());
	        				   iqcInspectionTemplateL.setPrecision(lisrgl.get(z).getPrecision());
	        				   iqcInspectionTemplateL.setStandradFrom(lisrgl.get(z).getStandradFrom());
	        				   iqcInspectionTemplateL.setStandradTo(lisrgl.get(z).getStandradTo());
	        				   iqcInspectionTemplateL.setStandradUom(lisrgl.get(z).getStandradUom());
	        				   iqcInspectionTemplateL.setTextStandrad(lisrgl.get(z).getTextStandrad());
	        				   iqcInspectionTemplateL.setInspectionTool(lisrgl.get(z).getInspectionTool());
	        				   iqcInspectionTemplateL.setInspectionMethod(lisrgl.get(z).getInspectionMethod());
	        				   iqcInspectionTemplateL.setFillInType(lisrgl.get(z).getFillInType());
	        				   iqcInspectionTemplateL.setSampleWayId(lisrgl.get(z).getSampleWayId());
	        				   iqcInspectionTemplateL.setEnableType("D");
	        				   if(lisrgl.get(z).getEnableTime()==null)
	        				   {iqcInspectionTemplateL.setEnableTime(new Date());
	        					   }
	        				   else
	        				   {
	        				   iqcInspectionTemplateL.setEnableTime(lisrgl.get(z).getEnableTime());
	        				   }
	        				   iqcInspectionTemplateL.setDisableTime(lisrgl.get(z).getDisableTime());
	        				   iqcInspectionTemplateL.setEnableFlag(lisrgl.get(z).getEnableFlag());
	        				   iqcInspectionTemplateL.setIsSync("Y");
	        				   IqcInspectionTemplateLService.insertSelective(requestCtx, iqcInspectionTemplateL);
	        			   }
	        		   }	        		   
	        	     }      	   
	        	   } 
	        	 }
               }
	        else if(dto.getStyle().equals("B"))
	        /*2.覆盖式完全同步，status = 'B'  保留已有项完全同步：若物料已有检验单模板，则保留检验单模板里的检验项目，将新的检验项目分配进检验单模板行；若新的和已有数据有重复项，则用新的替换旧的；*/
	        {
	        	//循环每个物料
		           for(int i = 0 ;i<ids.length;i++ )
		           {   
		        	   inspectionGroupH.setGroupId(dto.getGroupId());   
		        	   inspectionGroupH=inspectionGroupHMapper.selectByPrimaryKey(inspectionGroupH);
		        	   iqcInspectionTemplateH.setItemId(Float.valueOf(ids[i]));
		        	   iqcInspectionTemplateH.setPlantId(inspectionGroupH.getPlantId());
		        	  for(int j = 0 ;j<mainType.length;j++)
		        	  {	  
		        	   iqcInspectionTemplateH.setSourceType(mainType[j]);
		        	   List<IqcInspectionTemplateH> lA =iqcInspectionTemplateHMapper.select(iqcInspectionTemplateH);
		        	   //若检验单头表存在该物料，那么进行覆盖更新      行表根据attribute决定新增或更新 
		        	   if(lA.size()==1)
		        	   {
		        		   IqcInspectionTemplateH iA =new IqcInspectionTemplateH();
		        		   iA.setHeaderId(lA.get(0).getHeaderId());
		        		   iA.setPlantId(lA.get(0).getPlantId());
		        		   iA.setItemId(lA.get(0).getItemId());
		        		   iA.setItemCategory(lA.get(0).getItemCategory());
		        		   iA.setVersionNum(String.valueOf(Float.valueOf(lA.get(0).getVersionNum())+1));
		        		   iA.setDrawingNumber(lA.get(0).getDrawingNumber());
		        		   iA.setEnableFlag(lA.get(0).getEnableFlag());
		        		   iA.setHistoryNum(lA.get(0).getHistoryNum());
		        		   iA.setStatus("1");
		        		   iA.setSourceType(mainType[j]);
		        		   iA.setTimeLimit(dto.getTimeLimit());
		        		   iA.setMainType(dto.getType());
		        		   IqcInspectionTemplateHService.updateByPrimaryKeySelective(requestCtx,iA);
		        		   
		        		   IqcInspectionTemplateL iqcInspectionTemplateL =new IqcInspectionTemplateL();
		        	 
		        		   //新增行
		        		   InspectionGroupL inspectionGroupL =new InspectionGroupL();
		        		   inspectionGroupL.setGroupId(dto.getGroupId());
		        		   List<InspectionGroupL> lisrgl  =inspectionGroupLMapper.select(inspectionGroupL);
		        		   if(lisrgl.size()>0)
		        		   {
		        			   for(int z = 0 ; z < lisrgl.size(); z++)
		        			   {
		        				   IqcInspectionTemplateL lll = new IqcInspectionTemplateL();
		        				   lll.setAttributeId(lisrgl.get(z).getAttributeId());
		        				   lll.setHeaderId(iA.getHeaderId());
		        				   List<IqcInspectionTemplateL> listitl =iqcInspectionTemplateLMapper.select(lll);
		        				   //若存在 attributeid相同的数据，更新之
		        				   if(listitl.size()==1)
		        				   {
		        					   iqcInspectionTemplateL =iqcInspectionTemplateLMapper.selectOne(lll);
			        				   iqcInspectionTemplateL.setAttributeId(lisrgl.get(z).getAttributeId());
			        				   iqcInspectionTemplateL.setPrecision(lisrgl.get(z).getPrecision());
			        				   iqcInspectionTemplateL.setStandradFrom(lisrgl.get(z).getStandradFrom());
			        				   iqcInspectionTemplateL.setStandradTo(lisrgl.get(z).getStandradTo());
			        				   iqcInspectionTemplateL.setStandradUom(lisrgl.get(z).getStandradUom());
			        				   iqcInspectionTemplateL.setTextStandrad(lisrgl.get(z).getTextStandrad());
			        				   iqcInspectionTemplateL.setInspectionTool(lisrgl.get(z).getInspectionTool());
			        				   iqcInspectionTemplateL.setInspectionMethod(lisrgl.get(z).getInspectionMethod());
			        				   iqcInspectionTemplateL.setFillInType(lisrgl.get(z).getFillInType());
			        				   iqcInspectionTemplateL.setSampleWayId(lisrgl.get(z).getSampleWayId());
			        				   iqcInspectionTemplateL.setEnableType("D");
			        				   iqcInspectionTemplateL.setEnableTime(lisrgl.get(z).getEnableTime());
			        				   iqcInspectionTemplateL.setDisableTime(lisrgl.get(z).getDisableTime());
			        				   iqcInspectionTemplateL.setEnableFlag(lisrgl.get(z).getEnableFlag()); 
			        				   iqcInspectionTemplateL.setIsSync("Y");
			        				   IqcInspectionTemplateLService.updateByPrimaryKeySelective(requestCtx, iqcInspectionTemplateL);
		        				   }
		        				   else {
				        				   iqcInspectionTemplateL.setHeaderId(iA.getHeaderId());
				        				   iqcInspectionTemplateL.setAttributeId(lisrgl.get(z).getAttributeId());
				        				   iqcInspectionTemplateL.setPrecision(lisrgl.get(z).getPrecision());
				        				   iqcInspectionTemplateL.setStandradFrom(lisrgl.get(z).getStandradFrom());
				        				   iqcInspectionTemplateL.setStandradTo(lisrgl.get(z).getStandradTo());
				        				   iqcInspectionTemplateL.setStandradUom(lisrgl.get(z).getStandradUom());
				        				   iqcInspectionTemplateL.setTextStandrad(lisrgl.get(z).getTextStandrad());
				        				   iqcInspectionTemplateL.setInspectionTool(lisrgl.get(z).getInspectionTool());
				        				   iqcInspectionTemplateL.setInspectionMethod(lisrgl.get(z).getInspectionMethod());
				        				   iqcInspectionTemplateL.setFillInType(lisrgl.get(z).getFillInType());
				        				   iqcInspectionTemplateL.setSampleWayId(lisrgl.get(z).getSampleWayId());
				        				   iqcInspectionTemplateL.setEnableType("D");
				        				   iqcInspectionTemplateL.setEnableTime(lisrgl.get(z).getEnableTime());
				        				   iqcInspectionTemplateL.setDisableTime(lisrgl.get(z).getDisableTime());
				        				   iqcInspectionTemplateL.setEnableFlag(lisrgl.get(z).getEnableFlag()); 
				        				   iqcInspectionTemplateL.setIsSync("Y");
		        				           IqcInspectionTemplateLService.insertSelective(requestCtx, iqcInspectionTemplateL);
		        				   }
		        			   }
		        		   }
		        		   
		        		   
		        	   }
		        	   //新增(直接新增行)
		        	   else
		        	   {
		        		   IqcInspectionTemplateH iA =new IqcInspectionTemplateH();
		        		   iA.setPlantId(inspectionGroupH.getPlantId());
		        		   iA.setItemId(Float.valueOf(ids[i]));
		        		   iA.setVersionNum(String.valueOf(1));
		        		   iA.setEnableFlag("Y");
		        		   iA.setStatus("1");
		        		   iA.setSourceType(mainType[j]);
		        		   iA.setTimeLimit(dto.getTimeLimit());
		        		   iA.setMainType(dto.getType());
		        		   IqcInspectionTemplateHService.insertSelective(requestCtx, iA);
		        	    
		        		   //新增行
		        		   IqcInspectionTemplateL iqcInspectionTemplateL =new IqcInspectionTemplateL();
		        		   InspectionGroupL inspectionGroupL =new InspectionGroupL();
		        		   inspectionGroupL.setGroupId(dto.getGroupId());
		        		   List<InspectionGroupL> lisrgl  =inspectionGroupLMapper.select(inspectionGroupL);
		        		   if(lisrgl.size()>0)
		        		   {
		        			   for(int z = 0 ; z < lisrgl.size(); z++)
		        			   {
		        				   iqcInspectionTemplateL.setHeaderId(iA.getHeaderId());
		        				   iqcInspectionTemplateL.setAttributeId(lisrgl.get(z).getAttributeId());
		        				   iqcInspectionTemplateL.setPrecision(lisrgl.get(z).getPrecision());
		        				   iqcInspectionTemplateL.setStandradFrom(lisrgl.get(z).getStandradFrom());
		        				   iqcInspectionTemplateL.setStandradTo(lisrgl.get(z).getStandradTo());
		        				   iqcInspectionTemplateL.setStandradUom(lisrgl.get(z).getStandradUom());
		        				   iqcInspectionTemplateL.setTextStandrad(lisrgl.get(z).getTextStandrad());
		        				   iqcInspectionTemplateL.setInspectionTool(lisrgl.get(z).getInspectionTool());
		        				   iqcInspectionTemplateL.setInspectionMethod(lisrgl.get(z).getInspectionMethod());
		        				   iqcInspectionTemplateL.setFillInType(lisrgl.get(z).getFillInType());
		        				   iqcInspectionTemplateL.setSampleWayId(lisrgl.get(z).getSampleWayId());
		        				   iqcInspectionTemplateL.setEnableType("D");
		        				   iqcInspectionTemplateL.setEnableTime(lisrgl.get(z).getEnableTime());
		        				   iqcInspectionTemplateL.setDisableTime(lisrgl.get(z).getDisableTime());
		        				   iqcInspectionTemplateL.setEnableFlag(lisrgl.get(z).getEnableFlag());
		        				   iqcInspectionTemplateL.setIsSync("Y");
		        				   IqcInspectionTemplateLService.insertSelective(requestCtx, iqcInspectionTemplateL);
		        			   }
		        		   }	        		   
		        	     }      	   
		        	   } 
		        	 }
	        	
	        }
	        
	        else if(dto.getStyle().equals("C"))
		        /*3.覆盖式完全同步，status = 'C'   覆盖式无属性同步：若物料已有检验单模板，则删除已有检验单模板，检检验组的部分信息传过去*/
	        {
	        	for(int i = 0 ;i<ids.length;i++ )
		           {   
		        	   inspectionGroupH.setGroupId(dto.getGroupId());   
		        	   inspectionGroupH= inspectionGroupHMapper.selectByPrimaryKey(inspectionGroupH);
		        	   iqcInspectionTemplateH.setItemId(Float.valueOf(ids[i]));
		        	   iqcInspectionTemplateH.setPlantId(inspectionGroupH.getPlantId());
		        	  for(int j = 0 ;j<mainType.length;j++)
		        	  {	  
		        	   iqcInspectionTemplateH.setSourceType(mainType[j]);
		        	   List<IqcInspectionTemplateH> lA =iqcInspectionTemplateHMapper.select(iqcInspectionTemplateH);
		        	   //若检验单头表存在该物料，那么进行覆盖更新      行表先删 后增 
		        	   if(lA.size()==1)
		        	   {
		        		   IqcInspectionTemplateH iA =new IqcInspectionTemplateH();
		        		   iA.setHeaderId(lA.get(0).getHeaderId());
		        		   iA.setPlantId(lA.get(0).getPlantId());
		        		   iA.setItemId(lA.get(0).getItemId());
		        		   iA.setItemCategory(lA.get(0).getItemCategory());
		        		   iA.setVersionNum(String.valueOf(Float.valueOf(lA.get(0).getVersionNum())+1));
		        		   iA.setDrawingNumber(lA.get(0).getDrawingNumber());
		        		   iA.setEnableFlag(lA.get(0).getEnableFlag());
		        		   iA.setHistoryNum(lA.get(0).getHistoryNum());
		        		   iA.setStatus("1");
		        		   iA.setSourceType(mainType[j]);
		        		   iA.setTimeLimit(dto.getTimeLimit());
		        		   iA.setMainType(dto.getType());
		        		   IqcInspectionTemplateHService.updateByPrimaryKeySelective(requestCtx,iA);
		        		   
		        		   //删除行表
		        		   IqcInspectionTemplateL iqcInspectionTemplateL =new IqcInspectionTemplateL();
		        		   iqcInspectionTemplateL.setHeaderId(iA.getHeaderId());
		        		   iqcInspectionTemplateLMapper.deleteByExample(iqcInspectionTemplateL);
		        		   
		        		   //新增行
		        		   InspectionGroupL inspectionGroupL =new InspectionGroupL();
		        		   inspectionGroupL.setGroupId(dto.getGroupId());
		        		   List<InspectionGroupL> lisrgl  =inspectionGroupLMapper.select(inspectionGroupL);
		        		   if(lisrgl.size()>0)
		        		   {
		        			   for(int z = 0 ; z < lisrgl.size(); z++)
		        			   {
		        				   iqcInspectionTemplateL.setHeaderId(iA.getHeaderId());
		        				   iqcInspectionTemplateL.setAttributeId(lisrgl.get(z).getAttributeId());
		        				   iqcInspectionTemplateL.setEnableTime(lisrgl.get(z).getEnableTime());
		        				   iqcInspectionTemplateL.setEnableType("D");
		        				   iqcInspectionTemplateL.setIsSync("Y");
		        				   IqcInspectionTemplateLService.insertSelective(requestCtx, iqcInspectionTemplateL);
		        			   }
		        		   }
		        		   
		        		   
		        	   }
		        	   //新增(直接新增行)
		        	   else
		        	   {
		        		   IqcInspectionTemplateH iA =new IqcInspectionTemplateH();
		        		   iA.setPlantId(inspectionGroupH.getPlantId());
		        		   iA.setItemId(Float.valueOf(ids[i]));
		        		   iA.setVersionNum(String.valueOf(1));
		        		   iA.setEnableFlag("Y");
		        		   iA.setStatus("1");
		        		   iA.setSourceType(mainType[j]);
		        		   iA.setTimeLimit(dto.getTimeLimit());
		        		   iA.setMainType(dto.getType());
		        		   IqcInspectionTemplateHService.insertSelective(requestCtx, iA);
		        	    
		        		   //新增行
		        		   IqcInspectionTemplateL iqcInspectionTemplateL =new IqcInspectionTemplateL();
		        		   InspectionGroupL inspectionGroupL =new InspectionGroupL();
		        		   inspectionGroupL.setGroupId(dto.getGroupId());
		        		   List<InspectionGroupL> lisrgl  =inspectionGroupLMapper.select(inspectionGroupL);
		        		   if(lisrgl.size()>0)
		        		   {
		        			   for(int z = 0 ; z < lisrgl.size(); z++)
		        			   {
		        				   iqcInspectionTemplateL.setHeaderId(iA.getHeaderId());
		        				   iqcInspectionTemplateL.setAttributeId(lisrgl.get(z).getAttributeId());;
		        				   iqcInspectionTemplateL.setEnableType("D");
		        				   iqcInspectionTemplateL.setEnableTime(lisrgl.get(z).getEnableTime());
		        				   iqcInspectionTemplateL.setIsSync("Y");
		        				 
		        				   IqcInspectionTemplateLService.insertSelective(requestCtx, iqcInspectionTemplateL);
		        			   }
		        		   }	        		   
		        	     }      	   
		        	   } 
		        	 }
	        	
	        }
	        
	        else if(dto.getStyle().equals("D"))
		        /*4.覆盖式完全同步，status = 'C'   保留已有项无属性同步：若物料已有检验单模板，则保留检验单模板里的检验项目，将新的检验项目分配进检验单模板行；若新的和已有数据有重复项，则用新的替换旧的；*/
	        {
	        	 for(int i = 0 ;i<ids.length;i++ )
		           {   
		        	   inspectionGroupH.setGroupId(dto.getGroupId());   
		        	   inspectionGroupH=inspectionGroupHMapper.selectByPrimaryKey(inspectionGroupH);
		        	   iqcInspectionTemplateH.setItemId(Float.valueOf(ids[i]));
		        	   iqcInspectionTemplateH.setPlantId(inspectionGroupH.getPlantId());
		        	  for(int j = 0 ;j<mainType.length;j++)
		        	  {	  
		        	   iqcInspectionTemplateH.setSourceType(mainType[j]);
		        	   List<IqcInspectionTemplateH> lA =iqcInspectionTemplateHMapper.select(iqcInspectionTemplateH);
		        	   //若检验单头表存在该物料，那么进行覆盖更新      行表根据attribute决定新增或更新 
		        	   if(lA.size()==1)
		        	   {
		        		   IqcInspectionTemplateH iA =new IqcInspectionTemplateH();
		        		   iA.setHeaderId(lA.get(0).getHeaderId());
		        		   iA.setPlantId(lA.get(0).getPlantId());
		        		   iA.setItemId(lA.get(0).getItemId());
		        		   iA.setItemCategory(lA.get(0).getItemCategory());
		        		   iA.setVersionNum(String.valueOf(Float.valueOf(lA.get(0).getVersionNum())+1));
		        		   iA.setDrawingNumber(lA.get(0).getDrawingNumber());
		        		   iA.setEnableFlag(lA.get(0).getEnableFlag());
		        		   iA.setHistoryNum(lA.get(0).getHistoryNum());
		        		   iA.setStatus("1");
		        		   iA.setSourceType(mainType[j]);
		        		   iA.setTimeLimit(dto.getTimeLimit());
		        		   iA.setMainType(dto.getType());
		        		   IqcInspectionTemplateHService.updateByPrimaryKeySelective(requestCtx,iA);
		        		   
		        		   IqcInspectionTemplateL iqcInspectionTemplateL =new IqcInspectionTemplateL();
		        	 
		        		   //新增行
		        		   InspectionGroupL inspectionGroupL =new InspectionGroupL();
		        		   inspectionGroupL.setGroupId(dto.getGroupId());
		        		   List<InspectionGroupL> lisrgl  =inspectionGroupLMapper.select(inspectionGroupL);
		        		   if(lisrgl.size()>0)
		        		   {
		        			   for(int z = 0 ; z < lisrgl.size(); z++)
		        			   {
		        				   IqcInspectionTemplateL lll = new IqcInspectionTemplateL();
		        				   lll.setAttributeId(lisrgl.get(z).getAttributeId());
		        				   lll.setHeaderId(iA.getHeaderId());
		        				   List<IqcInspectionTemplateL> listitl =iqcInspectionTemplateLMapper.select(lll);
		        				   //若存在 attributeid相同的数据，更新之
		        				   if(listitl.size()==1)
		        				   {
		        					   iqcInspectionTemplateL =iqcInspectionTemplateLMapper.selectOne(lll);
			        				   iqcInspectionTemplateL.setAttributeId(lisrgl.get(z).getAttributeId());
			        				   iqcInspectionTemplateL.setEnableType("D");
			        				   iqcInspectionTemplateL.setEnableTime(lisrgl.get(z).getEnableTime()); 	
			        				   iqcInspectionTemplateL.setIsSync("Y");
			        				   IqcInspectionTemplateLService.updateByPrimaryKeySelective(requestCtx, iqcInspectionTemplateL);
		        				   }
		        				   else {
				        				   iqcInspectionTemplateL.setHeaderId(iA.getHeaderId());
				        				   iqcInspectionTemplateL.setAttributeId(lisrgl.get(z).getAttributeId());
				        				   iqcInspectionTemplateL.setEnableType("D");
				        				   iqcInspectionTemplateL.setEnableTime(lisrgl.get(z).getEnableTime()); 
				        				   iqcInspectionTemplateL.setIsSync("Y");
		        				           IqcInspectionTemplateLService.insertSelective(requestCtx, iqcInspectionTemplateL);
		        				   }
		        			   }
		        		   }
		        		   
		        		   
		        	   }
		        	   //新增(直接新增行)
		        	   else
		        	   {
		        		   IqcInspectionTemplateH iA =new IqcInspectionTemplateH();
		        		   iA.setPlantId(inspectionGroupH.getPlantId());
		        		   iA.setItemId(Float.valueOf(ids[i]));
		        		   iA.setVersionNum(String.valueOf(1));
		        		   iA.setEnableFlag("Y");
		        		   iA.setStatus("1");
		        		   iA.setSourceType(mainType[j]);
		        		   iA.setTimeLimit(dto.getTimeLimit());
		        		   iA.setMainType(dto.getType());
		        		   IqcInspectionTemplateHService.insertSelective(requestCtx, iA);
		        	    
		        		   //新增行
		        		   IqcInspectionTemplateL iqcInspectionTemplateL =new IqcInspectionTemplateL();
		        		   InspectionGroupL inspectionGroupL =new InspectionGroupL();
		        		   inspectionGroupL.setGroupId(dto.getGroupId());
		        		   List<InspectionGroupL> lisrgl  =inspectionGroupLMapper.select(inspectionGroupL);
		        		   if(lisrgl.size()>0)
		        		   {
		        			   for(int z = 0 ; z < lisrgl.size(); z++)
		        			   {
		        				   iqcInspectionTemplateL.setHeaderId(iA.getHeaderId());
		        				   iqcInspectionTemplateL.setAttributeId(lisrgl.get(z).getAttributeId());
		        				   iqcInspectionTemplateL.setEnableType(lisrgl.get(z).getEnableType());
		        				   iqcInspectionTemplateL.setEnableTime(lisrgl.get(z).getEnableTime());
		        				   iqcInspectionTemplateL.setIsSync("Y");
		        				 
		        				   IqcInspectionTemplateLService.insertSelective(requestCtx, iqcInspectionTemplateL);
		        			   }
		        		   }	        		   
		        	     }      	   
		        	   } 
		        	 }
	        	
	        	
	        }
			return responseData;
	 }
}