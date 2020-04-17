package com.hand.hqm.hqm_fmea.service.impl;

import com.github.pagehelper.PageHelper;
import com.hand.hap.core.IRequest;
import com.hand.hap.system.dto.ResponseData;
import com.hand.hap.system.service.impl.BaseServiceImpl;

import static org.junit.Assert.assertNotNull;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.TreeSet;
import java.util.stream.Collectors;

import org.antlr.grammar.v3.ANTLRv3Parser.action_return;
import org.apache.poi.hssf.record.PageBreakRecord.Break;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hand.hqm.hqm_control_plan.dto.ControlPlan;
import com.hand.hqm.hqm_control_plan.dto.ControlPlanFeatures;
import com.hand.hqm.hqm_control_plan.mapper.ControlPlanFeaturesMapper;
import com.hand.hqm.hqm_control_plan.mapper.ControlPlanMapper;
import com.hand.hqm.hqm_control_plan.service.IControlPlanService;
import com.hand.hqm.hqm_fmea.dto.Fmea;
import com.hand.hqm.hqm_fmea.mapper.FmeaMapper;
import com.hand.hqm.hqm_fmea.service.IFmeaService;
import com.hand.hqm.hqm_fmea_staff.dto.FmeaStaff;
import com.hand.hqm.hqm_fmea_staff.mapper.FmeaStaffMapper;
import com.hand.hqm.hqm_fmea_staff.service.IFmeaStaffService;
import com.hand.hqm.hqm_fmea_version.dto.FmeaVersion;
import com.hand.hqm.hqm_fmea_version.mapper.FmeaVersionMapper;
import com.hand.hqm.hqm_nonconforming_order.dto.NonconformingOrder;
import com.hand.hqm.hqm_pfmea_detail.dto.HqmpdbMenuItemP;
import com.hand.hqm.hqm_pfmea_detail.dto.PfmeaDetail;
import com.hand.hqm.hqm_pfmea_detail.mapper.PfmeaDetailMapper;
import com.hand.hqm.hqm_pfmea_detail.service.IPfmeaDetailService;
import com.hand.hqm.hqm_pfmea_level.dto.HqmLevelMenuItem;
import com.hand.hqm.hqm_pfmea_level.dto.PfmeaLevel;
import com.hand.hqm.hqm_pfmea_level.mapper.PfmeaLevelMapper;
import com.hand.hqm.hqm_pfmea_level.service.IPfmeaLevelService;
import com.hand.hqm.hqm_sam_use_order_h.dto.SamUseOrderH;
import com.mchange.v1.lang.GentleThread;
import com.mysql.jdbc.log.Log;
import com.thoughtworks.xstream.mapper.Mapper.Null;

import groovy.ui.Console;
import oracle.net.aso.e;
import oracle.net.aso.g;
import oracle.net.aso.h;

import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(rollbackFor = Exception.class)
public class FmeaServiceImpl extends BaseServiceImpl<Fmea> implements IFmeaService{
	 @Autowired
	 FmeaMapper fmeaMapper;
	 @Autowired
	 IFmeaService FmeaService;
	 @Autowired
	 IFmeaStaffService FmeaStaffService;
	 @Autowired
	 FmeaStaffMapper  fmeaStaffMapper;
	 @Autowired
      PfmeaLevelMapper  pfmeaLevelMapper;
	 @Autowired
	 FmeaVersionMapper fmeaVersionMapper;
	 @Autowired
	 PfmeaDetailMapper pfmeaDetailMapper;
	 /**
	     * 保存DFMEA 行信息
	     * @param dto list 操作数据集
	     * @param request 请求
	     * @return 操作结果
	     */
	 @Override
		public ResponseData dsave(IRequest requestContext, List<Fmea> dto) {	
		 ResponseData responseData = new ResponseData();
		 for(Fmea fmea : dto)
		 {
			 String code = null;
			 String name = null;
			 Fmea head = new Fmea();
			 Fmea head_get = new Fmea();
			 Fmea head_check = new Fmea();
//			 head.setFmeaName(fmea.getFmeaName() );		 
//			 head.setFmeaCode(fmea.getFmeaCode());			
			 head.setAnalysisObject(fmea.getAnalysisObject());
			 head.setChargeId(fmea.getChargeId());
			 head.setLevelId(fmea.getLevelId());	
			 head.setFmeaType("D");
			 
			 
 //确认当前level层级
			 
			 PfmeaLevel pfmeaLevel =new PfmeaLevel();
			 pfmeaLevel.setLevelId(fmea.getLevelId());
			 pfmeaLevel= pfmeaLevelMapper.selectByPrimaryKey(pfmeaLevel);
			
			 if(pfmeaLevel.getFmeaLevel()==1L)
			 {
				 PfmeaLevel pfmeaLevel_2 =new PfmeaLevel();
				 pfmeaLevel_2.setLevelId(fmea.getLevelId());
				 pfmeaLevel_2 =pfmeaLevelMapper.selectByPrimaryKey(pfmeaLevel_2);
				 name = pfmeaLevel_2.getDescription();
				 code =pfmeaLevel_2.getLevelCode();
				 
			 }
			 else if(pfmeaLevel.getFmeaLevel()==2L)
			 {
				 PfmeaLevel pfmeaLevel_2 =new PfmeaLevel();
				 PfmeaLevel pfmeaLevel_3 =new PfmeaLevel();
				 pfmeaLevel_2.setLevelId(fmea.getLevelId());
				 pfmeaLevel_2 =pfmeaLevelMapper.selectByPrimaryKey(pfmeaLevel_2);
				 pfmeaLevel_3.setLevelId(pfmeaLevel_2.getParentLevelId());
				 pfmeaLevel_3 =pfmeaLevelMapper.selectByPrimaryKey(pfmeaLevel_3);
				 name = pfmeaLevel_2.getDescription();
				 code = pfmeaLevel_3.getLevelCode()+"-"+pfmeaLevel_2.getLevelCode();
			 }
			 else if(pfmeaLevel.getFmeaLevel()==3L)
			 {
				 PfmeaLevel pfmeaLevel_2 =new PfmeaLevel();
				 PfmeaLevel pfmeaLevel_3 =new PfmeaLevel();
				 PfmeaLevel pfmeaLevel_4 =new PfmeaLevel();
				 pfmeaLevel_2.setLevelId(fmea.getLevelId());
				 pfmeaLevel_2 =pfmeaLevelMapper.selectByPrimaryKey(pfmeaLevel_2);
				 pfmeaLevel_3.setLevelId(pfmeaLevel_2.getParentLevelId());
				 pfmeaLevel_3 =pfmeaLevelMapper.selectByPrimaryKey(pfmeaLevel_3);
				 pfmeaLevel_4.setLevelId(pfmeaLevel_3.getParentLevelId());
				 pfmeaLevel_4 =pfmeaLevelMapper.selectByPrimaryKey(pfmeaLevel_4);
				 name = pfmeaLevel_2.getDescription();
				 code =pfmeaLevel_4.getLevelCode()+"-"+pfmeaLevel_3.getLevelCode()+"-"+pfmeaLevel_2.getLevelCode();
			 }
			 else if(pfmeaLevel.getFmeaLevel()==4L)
			 {
				 PfmeaLevel pfmeaLevel_2 =new PfmeaLevel();
				 PfmeaLevel pfmeaLevel_3 =new PfmeaLevel();
				 PfmeaLevel pfmeaLevel_4 =new PfmeaLevel();
				 PfmeaLevel pfmeaLevel_5 =new PfmeaLevel();
				 pfmeaLevel_2.setLevelId(fmea.getLevelId());
				 pfmeaLevel_2 =pfmeaLevelMapper.selectByPrimaryKey(pfmeaLevel_2);
				 pfmeaLevel_3.setLevelId(pfmeaLevel_2.getParentLevelId());
				 pfmeaLevel_3 =pfmeaLevelMapper.selectByPrimaryKey(pfmeaLevel_3);
				 pfmeaLevel_4.setLevelId(pfmeaLevel_3.getParentLevelId());
				 pfmeaLevel_4 =pfmeaLevelMapper.selectByPrimaryKey(pfmeaLevel_4);
				 pfmeaLevel_5.setLevelId(pfmeaLevel_4.getParentLevelId());
				 pfmeaLevel_5 =pfmeaLevelMapper.selectByPrimaryKey(pfmeaLevel_5);
				 name = pfmeaLevel_2.getDescription();
				 code =pfmeaLevel_5.getLevelCode()+"-"+pfmeaLevel_4.getLevelCode()+"-"+pfmeaLevel_3.getLevelCode()+"-"+pfmeaLevel_2.getLevelCode();
			 }
			 else if(pfmeaLevel.getFmeaLevel()==5L)
			 {
				 PfmeaLevel pfmeaLevel_2 =new PfmeaLevel();
				 PfmeaLevel pfmeaLevel_3 =new PfmeaLevel();
				 PfmeaLevel pfmeaLevel_4 =new PfmeaLevel();
				 PfmeaLevel pfmeaLevel_5 =new PfmeaLevel();
				 PfmeaLevel pfmeaLevel_6 =new PfmeaLevel();
				 pfmeaLevel_2.setLevelId(fmea.getLevelId());
				 pfmeaLevel_2 =pfmeaLevelMapper.selectByPrimaryKey(pfmeaLevel_2);
				 pfmeaLevel_3.setLevelId(pfmeaLevel_2.getParentLevelId());
				 pfmeaLevel_3 =pfmeaLevelMapper.selectByPrimaryKey(pfmeaLevel_3);
				 pfmeaLevel_4.setLevelId(pfmeaLevel_3.getParentLevelId());
				 pfmeaLevel_4 =pfmeaLevelMapper.selectByPrimaryKey(pfmeaLevel_4);
				 pfmeaLevel_5.setLevelId(pfmeaLevel_4.getParentLevelId());
				 pfmeaLevel_5 =pfmeaLevelMapper.selectByPrimaryKey(pfmeaLevel_5);
				 pfmeaLevel_6.setLevelId(pfmeaLevel_5.getParentLevelId());
				 pfmeaLevel_6 =pfmeaLevelMapper.selectByPrimaryKey(pfmeaLevel_6);
				 name = pfmeaLevel_2.getDescription();
				 code =pfmeaLevel_6.getLevelCode()+"-"+pfmeaLevel_5.getLevelCode()+"-"+pfmeaLevel_4.getLevelCode()+"-"+pfmeaLevel_3.getLevelCode()+"-"+pfmeaLevel_2.getLevelCode();
			 }
			 //校验必输项
			 if(null==head.getChargeId() )
			 {
				 responseData.setSuccess(false);
				 responseData.setMessage("责任人必输！");
				 return responseData;
			 }
			 head_check.setLevelId(fmea.getLevelId());
//			 head_check.setFmeaName(fmea.getFmeaName());
			 head_check= fmeaMapper.selectOne(head_check);
			
			 if(fmea.getKid()==null)
			 {   
				 //校验项目名称
				 if(head_check!=null)
				 {
					 responseData.setSuccess(false);
					 responseData.setMessage("项目名称已存在！不可重复维护");
					 return responseData;
				 }
			//新增时自动生成一个流水项目编号
				 head.setCurrentVersion(Float.parseFloat("10"));
				 DateFormat format1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");                   
				 String inspectionNum; 
				 Date date = new Date();
			     SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
				 String time = dateFormat.format(date);
				 String inspectionNumStart =  "DF" + time.substring(2)  ;	
				 inspectionNum = getInspectionNumberD(inspectionNumStart);
				 head.setFmeaName(name);
				 head.setFmeaCode(code);
				 head_get = FmeaService.insertSelective(requestContext, head);	
				 
				 
				 // 版本表插值
				 
				    FmeaVersion  fmeaVersion =new FmeaVersion();
					fmeaVersion.setFmeaId(head_get.getKid());  
					fmeaVersion.setFmeaCode(head_get.getFmeaCode());
					fmeaVersion.setFmeaVersion(head_get.getCurrentVersion());
					fmeaVersionMapper.insertSelective(fmeaVersion);
			 }
			 else
			 {
				 head.setCurrentVersion(fmea.getCurrentVersion());
				 head.setKid(fmea.getKid());
				 head_get=  FmeaService.updateByPrimaryKeySelective(requestContext, head);
			 }
			 
			
					 //参与人员表插值
					 FmeaStaff staff =new  FmeaStaff();
					 FmeaStaff staff_get =new  FmeaStaff();
					 staff.setFmeaId(head_get.getKid());
					 staff.setStaffId(head_get.getChargeId());
					 staff_get= fmeaStaffMapper.selectOne(staff);
					 if(staff_get==null)
					 {
						 fmeaStaffMapper.insertSelective(staff);
					 }
					 else
					 {
						 fmeaStaffMapper.updateByPrimaryKeySelective(staff_get);
					 }
				 }
	
		    List<Fmea> list = fmeaMapper.dmyselect(null);
		    responseData.setRows(list);
			return responseData;
		}
	 /**
	     * 保存PFMEA 行信息
	     * @param dto list 操作数据集
	     * @param request 请求
	     * @return 操作结果
	     */
	 @Override
		public ResponseData psave(IRequest requestContext, List<Fmea> dto) {	
		 ResponseData responseData = new ResponseData();
		
		 for(Fmea fmea : dto)
		 {
			 String code = null;
			 String name = null;
			 Fmea head = new Fmea();
			 Fmea head_get = new Fmea();
			 Fmea head_check = new Fmea();
//			 head.setFmeaName(fmea.getFmeaName() );		 
//			 head.setFmeaCode(fmea.getFmeaCode());			
			 head.setAnalysisObject(fmea.getAnalysisObject());
			 head.setChargeId(fmea.getChargeId());
			 head.setLevelId(fmea.getLevelId());			 
			 head.setFmeaType("P");
			 //确认当前level层级
			 
			 PfmeaLevel pfmeaLevel =new PfmeaLevel();
			 pfmeaLevel.setLevelId(fmea.getLevelId());
			 pfmeaLevel= pfmeaLevelMapper.selectByPrimaryKey(pfmeaLevel);
			
			 if(pfmeaLevel.getFmeaLevel()==1L)
			 {
				 PfmeaLevel pfmeaLevel_2 =new PfmeaLevel();
				 pfmeaLevel_2.setLevelId(fmea.getLevelId());
				 pfmeaLevel_2 =pfmeaLevelMapper.selectByPrimaryKey(pfmeaLevel_2);
				 name = pfmeaLevel_2.getDescription();
				 code =pfmeaLevel_2.getLevelCode();
			 }
			 else if(pfmeaLevel.getFmeaLevel()==2L)
			 {
				 PfmeaLevel pfmeaLevel_2 =new PfmeaLevel();
				 PfmeaLevel pfmeaLevel_3 =new PfmeaLevel();
				 pfmeaLevel_2.setLevelId(fmea.getLevelId());
				 pfmeaLevel_2 =pfmeaLevelMapper.selectByPrimaryKey(pfmeaLevel_2);
				 pfmeaLevel_3.setLevelId(pfmeaLevel_2.getParentLevelId());
				 pfmeaLevel_3 =pfmeaLevelMapper.selectByPrimaryKey(pfmeaLevel_3);
				 name = pfmeaLevel_2.getDescription();
				 code =pfmeaLevel_3.getLevelCode()+"-"+pfmeaLevel_2.getLevelCode();
			 }
			 else if(pfmeaLevel.getFmeaLevel()==3L)
			 {
				 PfmeaLevel pfmeaLevel_2 =new PfmeaLevel();
				 PfmeaLevel pfmeaLevel_3 =new PfmeaLevel();
				 PfmeaLevel pfmeaLevel_4 =new PfmeaLevel();
				 pfmeaLevel_2.setLevelId(fmea.getLevelId());
				 pfmeaLevel_2 =pfmeaLevelMapper.selectByPrimaryKey(pfmeaLevel_2);
				 pfmeaLevel_3.setLevelId(pfmeaLevel_2.getParentLevelId());
				 pfmeaLevel_3 =pfmeaLevelMapper.selectByPrimaryKey(pfmeaLevel_3);
				 pfmeaLevel_4.setLevelId(pfmeaLevel_3.getParentLevelId());
				 pfmeaLevel_4 =pfmeaLevelMapper.selectByPrimaryKey(pfmeaLevel_4);
				 name = pfmeaLevel_2.getDescription();
				 code =pfmeaLevel_4.getLevelCode()+"-"+pfmeaLevel_3.getLevelCode()+"-"+pfmeaLevel_2.getLevelCode();
			 }
			 else if(pfmeaLevel.getFmeaLevel()==4L)
			 {
				 PfmeaLevel pfmeaLevel_2 =new PfmeaLevel();
				 PfmeaLevel pfmeaLevel_3 =new PfmeaLevel();
				 PfmeaLevel pfmeaLevel_4 =new PfmeaLevel();
				 PfmeaLevel pfmeaLevel_5 =new PfmeaLevel();
				 pfmeaLevel_2.setLevelId(fmea.getLevelId());
				 pfmeaLevel_2 =pfmeaLevelMapper.selectByPrimaryKey(pfmeaLevel_2);
				 pfmeaLevel_3.setLevelId(pfmeaLevel_2.getParentLevelId());
				 pfmeaLevel_3 =pfmeaLevelMapper.selectByPrimaryKey(pfmeaLevel_3);
				 pfmeaLevel_4.setLevelId(pfmeaLevel_3.getParentLevelId());
				 pfmeaLevel_4 =pfmeaLevelMapper.selectByPrimaryKey(pfmeaLevel_4);
				 pfmeaLevel_5.setLevelId(pfmeaLevel_4.getParentLevelId());
				 pfmeaLevel_5 =pfmeaLevelMapper.selectByPrimaryKey(pfmeaLevel_5);
				 name = pfmeaLevel_2.getDescription();
				 code =pfmeaLevel_5.getLevelCode()+"-"+pfmeaLevel_4.getLevelCode()+"-"+pfmeaLevel_3.getLevelCode()+"-"+pfmeaLevel_2.getLevelCode();
			 }
			    		 
			 //校验必输项
			 if(null==head.getChargeId())
			 {
				 responseData.setSuccess(false);
				 responseData.setMessage("责任人必输！");
				 return responseData;
			 }
//			 head_check.setFmeaName(fmea.getFmeaName());
			 head_check.setLevelId(fmea.getLevelId());;
			 head_check= fmeaMapper.selectOne(head_check);
			
			 if(fmea.getKid()==null)
			 {   
				 //校验项目名称
				 if(head_check!=null)
				 {
					 responseData.setSuccess(false);
					 responseData.setMessage("项目名称已存在！不可重复维护");
					 return responseData;
				 }
			//新增时自动生成一个流水项目编号
				 head.setCurrentVersion(Float.parseFloat("10"));
				 DateFormat format1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");                   
				 String inspectionNum; 
				 Date date = new Date();
			     SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
				 String time = dateFormat.format(date);
				 String inspectionNumStart =  "PF" + time.substring(2)  ;	
				 inspectionNum = getInspectionNumberP(inspectionNumStart);
				 head.setFmeaCode(code);
				 head.setFmeaName(name);
				 head_get = FmeaService.insertSelective(requestContext, head);	
				 
				 
				 // 版本表插值
				 
				    FmeaVersion fmeaVersion =new FmeaVersion();
					fmeaVersion.setFmeaId(head_get.getKid());  
					fmeaVersion.setFmeaCode(head_get.getFmeaCode());
					fmeaVersion.setFmeaVersion(head_get.getCurrentVersion());
					fmeaVersionMapper.insertSelective(fmeaVersion);
			 }
			 else
			 {
				 head.setCurrentVersion(fmea.getCurrentVersion());
				 head.setKid(fmea.getKid());
				 head_get=  FmeaService.updateByPrimaryKeySelective(requestContext, head);
			 }
			 
			
				
				
			 if(fmea.getStaffId()==null)
			 {}
			 else
			 {
				 String str  = fmea.getStaffId();
				 String[] ary = str.split(",");
				 for (String staffId :ary)
				 {
					 //参与人员表插值
					 FmeaStaff staff =new  FmeaStaff();
					 FmeaStaff staff_get =new  FmeaStaff();
					 staff.setFmeaId(head_get.getKid());
					 staff.setStaffId(Float.parseFloat(staffId));
					 staff_get= fmeaStaffMapper.selectOne(staff);
					 if(staff_get==null)
					 {
						 fmeaStaffMapper.insertSelective(staff);
					 }
					 else
					 {
						 fmeaStaffMapper.updateByPrimaryKeySelective(staff_get);
					 }
				 }
			 }
			 
			  
			 
		 }
		    List<Fmea> list = fmeaMapper.pmyselect(null);
		    responseData.setRows(list);
			return responseData;
		}
	 public String getInspectionNumberD(String inspectionNumStart) {
			Integer count = 1;
			Fmea  sr = new Fmea();
			sr.setFmeaCode(inspectionNumStart);
			List<Fmea> list = new ArrayList<Fmea>();
			list = fmeaMapper.selectMaxNumberD(sr);
			if (list != null && list.size() > 0) {
				String NoNum = list.get(0).getFmeaCode();
				String number = NoNum.substring(NoNum.length() - 5);// 流水
				count = Integer.valueOf(number) + 1;
			}
			String str = String.format("%05d", count);
			return inspectionNumStart + str;// 最终检验单号
		}
	 
	 public String getInspectionNumberP(String inspectionNumStart) {
			Integer count = 1;
			Fmea  sr = new Fmea();
			sr.setFmeaCode(inspectionNumStart);
			List<Fmea> list = new ArrayList<Fmea>();
			list = fmeaMapper.selectMaxNumberP(sr);
			if (list != null && list.size() > 0) {
				String NoNum = list.get(0).getFmeaCode();
				String number = NoNum.substring(NoNum.length() - 5);// 流水
				count = Integer.valueOf(number) + 1;
			}
			String str = String.format("%05d", count);
			return inspectionNumStart + str;// 最终检验单号
		}
      	 
	 /**
	     * DFMEA页面查询
	     * @param dto 查询内容
	     * @param page 页码
	     * @param pageSize 页大小
	     * @param request 请求
	     * @return 结果集
	     */
	 @Override
	    public List<Fmea> dmyselect(IRequest requestContext, Fmea dto, int page, int pageSize) {
	        PageHelper.startPage(page, pageSize);
	      //确认当前level层级
			 List<Long> arg =new ArrayList<>();
			 List<Fmea> res =new ArrayList<>();
			 PfmeaLevel pfmeaLevel =new PfmeaLevel();
			 if(dto.getLevelId()==null)
			 {		 
				 res.addAll(fmeaMapper.dmyselect(dto)); 
			 }
			 else {
			 pfmeaLevel.setLevelId(dto.getLevelId());		 
			 pfmeaLevel= pfmeaLevelMapper.selectByPrimaryKey(pfmeaLevel);
			 res.addAll(fmeaMapper.dmyselect(dto));
			 if(pfmeaLevel.getFmeaLevel()==1L)
			 {
				 //第二层所有子
				 PfmeaLevel pfmeaLevel_2 =new PfmeaLevel();
				 pfmeaLevel_2.setParentLevelId(pfmeaLevel.getLevelId());
				 List<PfmeaLevel> l2 = pfmeaLevelMapper.select(pfmeaLevel_2);
				 if(l2.size()>0)
				 {
				 for(PfmeaLevel p2: l2)
				 {
					 Fmea f2 =new Fmea();
					 f2.setLevelId(p2.getLevelId());
					 res.addAll(fmeaMapper.dmyselect(f2));
					 PfmeaLevel pfmeaLevel_3 =new PfmeaLevel();
					 pfmeaLevel_3.setParentLevelId(p2.getLevelId());
					 List<PfmeaLevel> l3 = pfmeaLevelMapper.select(pfmeaLevel_3);
					 //第三层所有子
					 if(l3.size()>0)
					 {
					 for(PfmeaLevel p3 :l3)
					 {
						 Fmea f3 =new Fmea();
						 f3.setLevelId(p3.getLevelId());
						 res.addAll(fmeaMapper.dmyselect(f3));
						 PfmeaLevel pfmeaLevel_4 =new PfmeaLevel();
						 pfmeaLevel_4.setParentLevelId(p3.getLevelId());
						 List<PfmeaLevel> l4 = pfmeaLevelMapper.select(pfmeaLevel_4);
						 if(l4.size()>0)
						 {
						 for(PfmeaLevel p4 :l4)
						 {   
							 Fmea f4 =new Fmea();
							 f4.setLevelId(p4.getLevelId());
							 res.addAll(fmeaMapper.dmyselect(f4));
							 PfmeaLevel pfmeaLevel_5 =new PfmeaLevel();
							 pfmeaLevel_5.setParentLevelId(p4.getLevelId());
							 List<PfmeaLevel> l5 = pfmeaLevelMapper.select(pfmeaLevel_5);
							 for(PfmeaLevel p5 :l5)
							 {  
								 Fmea f5 =new Fmea();
								 f5.setLevelId(p5.getLevelId());
								 res.addAll(fmeaMapper.dmyselect(f5));
								 
							 }
							 
						 }	
						 }
					 }
					 }
				 }
				 }
				 
			 }
			 else if(pfmeaLevel.getFmeaLevel()==2L)
			 {
				 PfmeaLevel pfmeaLevel_2 =new PfmeaLevel();
				 pfmeaLevel_2.setParentLevelId(pfmeaLevel.getLevelId());
				 List<PfmeaLevel> l2 = pfmeaLevelMapper.select(pfmeaLevel_2);
				 for(PfmeaLevel p2: l2)
				 {
					 Fmea f2 =new Fmea();
					 f2.setLevelId(p2.getLevelId());
					 res.addAll(fmeaMapper.dmyselect(f2));
					 PfmeaLevel pfmeaLevel_3 =new PfmeaLevel();
					 pfmeaLevel_3.setParentLevelId(p2.getLevelId());
					 List<PfmeaLevel> l3 = pfmeaLevelMapper.select(pfmeaLevel_3);
					 for(PfmeaLevel p4 :l3)
					 {   
						 Fmea f4 =new Fmea();
						 f4.setLevelId(p4.getLevelId());
						 res.addAll(fmeaMapper.dmyselect(f4));
						 PfmeaLevel pfmeaLevel_5 =new PfmeaLevel();
						 pfmeaLevel_5.setParentLevelId(p4.getLevelId());
						 List<PfmeaLevel> l5 = pfmeaLevelMapper.select(pfmeaLevel_5);
						 for(PfmeaLevel p5 :l5)
						 {  
							 Fmea f5 =new Fmea();
							 f5.setLevelId(p5.getLevelId());
							 res.addAll(fmeaMapper.dmyselect(f5));
							 
						 }
					 }	
				 }
			 }
			 else if(pfmeaLevel.getFmeaLevel()==3L)
			 {
				 PfmeaLevel pfmeaLevel_2 =new PfmeaLevel();
				 pfmeaLevel_2.setParentLevelId(pfmeaLevel.getLevelId());
				 List<PfmeaLevel> l2 = pfmeaLevelMapper.select(pfmeaLevel_2);
				 for(PfmeaLevel p4: l2)
				 {
					 Fmea f4 =new Fmea();
					 f4.setLevelId(p4.getLevelId());
					 res.addAll(fmeaMapper.dmyselect(f4));
					 PfmeaLevel pfmeaLevel_5 =new PfmeaLevel();
					 pfmeaLevel_5.setParentLevelId(p4.getLevelId());
					 List<PfmeaLevel> l5 = pfmeaLevelMapper.select(pfmeaLevel_5);
					 for(PfmeaLevel p5 :l5)
					 {  
						 Fmea f5 =new Fmea();
						 f5.setLevelId(p5.getLevelId());
						 res.addAll(fmeaMapper.dmyselect(f5));
						 
					 }
				 }
				
			 } else if(pfmeaLevel.getFmeaLevel()==4L)
			 {
				 PfmeaLevel pfmeaLevel_2 =new PfmeaLevel();
				 pfmeaLevel_2.setParentLevelId(pfmeaLevel.getLevelId());
				 List<PfmeaLevel> l2 = pfmeaLevelMapper.select(pfmeaLevel_2);
				 for(PfmeaLevel p4: l2)
				 {
					 Fmea f4 =new Fmea();
					 f4.setLevelId(p4.getLevelId());
					 res.addAll(fmeaMapper.dmyselect(f4));
				 }
			 }
			 }
	        return res;   
		 }
	 /**
	     * PFMEA页面查询
	     * @param dto 查询内容
	     * @param page 页码
	     * @param pageSize 页大小
	     * @param request 请求
	     * @return 结果集
	     */
	 @SuppressWarnings("unlikely-arg-type")
	@Override
	    public List<Fmea> pmyselect(IRequest requestContext, Fmea dto, int page, int pageSize) {
	        PageHelper.startPage(page, pageSize);
	        //确认当前level层级
			 List<Long> arg =new ArrayList<>();
			 List<Fmea> res =new ArrayList<>();
			 PfmeaLevel pfmeaLevel =new PfmeaLevel();
			 if(dto.getLevelId() == null)
			 {		 
				 res.addAll(fmeaMapper.pmyselect(dto)); 
			 }
			 else {
			 pfmeaLevel.setLevelId(dto.getLevelId());		 
			 pfmeaLevel= pfmeaLevelMapper.selectByPrimaryKey(pfmeaLevel);
			 res.addAll(fmeaMapper.pmyselect(dto));
			 if(pfmeaLevel.getFmeaLevel()==1L)
			 {
				 //第二层所有子
				 PfmeaLevel pfmeaLevel_2 =new PfmeaLevel();
				 pfmeaLevel_2.setParentLevelId(pfmeaLevel.getLevelId());
				 List<PfmeaLevel> l2 = pfmeaLevelMapper.select(pfmeaLevel_2);
				 if(l2.size()>0)
				 {
				 for(PfmeaLevel p2: l2)
				 {
					 Fmea f2 =new Fmea();
					 f2.setLevelId(p2.getLevelId());
					 res.addAll(fmeaMapper.pmyselect(f2));
					 PfmeaLevel pfmeaLevel_3 =new PfmeaLevel();
					 pfmeaLevel_3.setParentLevelId(p2.getLevelId());
					 List<PfmeaLevel> l3 = pfmeaLevelMapper.select(pfmeaLevel_3);
					 //第三层所有子
					 if(l3.size()>0)
					 {
					 for(PfmeaLevel p3 :l3)
					 {
						 Fmea f3 =new Fmea();
						 f3.setLevelId(p3.getLevelId());
						 res.addAll(fmeaMapper.pmyselect(f3));
						 PfmeaLevel pfmeaLevel_4 =new PfmeaLevel();
						 pfmeaLevel_4.setParentLevelId(p3.getLevelId());
						 List<PfmeaLevel> l4 = pfmeaLevelMapper.select(pfmeaLevel_4);
						 if(l4.size()>0)
						 {
						 for(PfmeaLevel p4 :l4)
						 {   
							 Fmea f4 =new Fmea();
							 f4.setLevelId(p4.getLevelId());
							 res.addAll(fmeaMapper.pmyselect(f4));
						 }	
						 }
					 }
					 }
				 }
				 }
				 
			 }
			 else if(pfmeaLevel.getFmeaLevel()==2L)
			 {
				 PfmeaLevel pfmeaLevel_2 =new PfmeaLevel();
				 pfmeaLevel_2.setParentLevelId(pfmeaLevel.getLevelId());
				 List<PfmeaLevel> l2 = pfmeaLevelMapper.select(pfmeaLevel_2);
				 for(PfmeaLevel p2: l2)
				 {
					 Fmea f2 =new Fmea();
					 f2.setLevelId(p2.getLevelId());
					 res.addAll(fmeaMapper.pmyselect(f2));
					 PfmeaLevel pfmeaLevel_3 =new PfmeaLevel();
					 pfmeaLevel_3.setParentLevelId(p2.getLevelId());
					 List<PfmeaLevel> l3 = pfmeaLevelMapper.select(pfmeaLevel_3);
					 for(PfmeaLevel p4 :l3)
					 {   
						 Fmea f4 =new Fmea();
						 f4.setLevelId(p4.getLevelId());
						 res.addAll(fmeaMapper.pmyselect(f4));
					 }	
				 }
			 }
			 else if(pfmeaLevel.getFmeaLevel()==3L)
			 {
				 PfmeaLevel pfmeaLevel_2 =new PfmeaLevel();
				 pfmeaLevel_2.setParentLevelId(pfmeaLevel.getLevelId());
				 List<PfmeaLevel> l2 = pfmeaLevelMapper.select(pfmeaLevel_2);
				 for(PfmeaLevel p4: l2)
				 {
					 Fmea f4 =new Fmea();
					 f4.setLevelId(p4.getLevelId());
					 res.addAll(fmeaMapper.pmyselect(f4)); 
				 }
				
			 }
			 
			 }
	        return res;   
		
	 }
	 /**
	     * PFMEA页面版本查询
	     * @param dto 查询内容
	     * @param page 页码
	     * @param pageSize 页大小
	     * @param request 请求
	     * @return 结果集
	     */
	 @Override
	    public List<Fmea> PselectV(IRequest requestContext, Fmea dto, int page, int pageSize) {
	        PageHelper.startPage(page, pageSize);
	        return fmeaMapper.PselectV(dto);
		 }
	 
	 /**
	     * DFMEA页面版本查询
	     * @param dto 查询内容
	     * @param page 页码
	     * @param pageSize 页大小
	     * @param request 请求
	     * @return 结果集
	     */
	 @Override
	    public List<Fmea> DselectV(IRequest requestContext, Fmea dto, int page, int pageSize) {
	        PageHelper.startPage(page, pageSize);
	        return fmeaMapper.DselectV(dto);
		 }
	 
	@Autowired
	private ControlPlanMapper controlPlanMapper; 
	
	@Autowired
	private ControlPlanFeaturesMapper controlPlanFeaturesMapper; 
	
	@Autowired
    private IPfmeaDetailService pfmeaDetailService;
	
	/**
     * 新增控制计划数据
     * @param dto  操作数据集
     * @param request 请求
     * @return 操作结果
     */
	@Override
	public ResponseData addContralPlan(IRequest requestCtx, Fmea dto) {
		//当前时间
		Date now = new Date();
		// TODO 生成控制计划
		ResponseData responseData = new ResponseData();
		//根据levelId查询
		Long levelId = dto.getLevelId();
		PfmeaLevel pfmeaLevel = pfmeaLevelMapper.selectByPrimaryKey(levelId);
		pfmeaLevel.setLevelType("P");
		//查询Fmea
		List<Fmea> list = this.pmyselect(requestCtx,dto,0,0);
		//根据levelId判断是否存在控制计划
		List<ControlPlan> cps = controlPlanMapper.queryControlPlanByLevelId(levelId);
		//Fmea为空，删除控制计划；不为空，继续
		if (list != null && list.size() > 0) {
			//得出所有类型并去重
			List<String> types = list.stream().map(Fmea :: getLevelPfmeaType).distinct().collect(Collectors.toList());
			//修改
			if (cps != null && cps.size() > 0) {
				//删除所有的控制计划
				cps.forEach(cp -> {
					controlPlanMapper.delete(cp);
				});
				//新增
				this.insertControlPlanAndControlPlanFeatures(requestCtx, now, levelId, pfmeaLevel, list, types);
			} else {
				//新增
				this.insertControlPlanAndControlPlanFeatures(requestCtx, now, levelId, pfmeaLevel, list, types);
			}
		} else {
			//无Fmea删除控制计划
			ControlPlan controlPlan = new ControlPlan();
			controlPlan.setLevelId(levelId);
			controlPlanMapper.delete(controlPlan);
		}
		return responseData;
	}
	/**
	 * @Description: 插入控制计划表和明细表
	 * @param requestCtx
	 * @param now
	 * @param levelId
	 * @param pfmeaLevel
	 * @param list
	 * @param types
	 */
	private void insertControlPlanAndControlPlanFeatures(IRequest requestCtx, Date now, Long levelId,
			PfmeaLevel pfmeaLevel, List<Fmea> list, List<String> types) {
		types.forEach(acFun -> {
			//新增
			ControlPlan controlPlan = new ControlPlan();
			//控制计划类型
			controlPlan.setControlPlanType(acFun);
			//控制计划编号
			controlPlan.setControlPlanCode(pfmeaLevel.getLevelCode());
			//控制对象
			controlPlan.setControlObject(pfmeaLevel.getDescription());
			//编制时间
			controlPlan.setCreationDate(now);
			//开发代号ID
			controlPlan.setLevelId(levelId);
			//插入控制计划表
			controlPlanMapper.insertSelective(controlPlan);
			list.forEach(action -> {
				//查询Fmea明细
				PfmeaDetail pfmeaDetail = new PfmeaDetail();
				pfmeaDetail.setFmeaId(action.getKid());
				List<HqmpdbMenuItemP> queryTreeDatas = pfmeaDetailService.queryTreeData(requestCtx,  pfmeaDetail);	
				if (controlPlan.getControlPlanType().equals(action.getLevelPfmeaType())) {
					queryTreeDatas.forEach(f -> {
						ControlPlanFeatures controlPlanFeature = new ControlPlanFeatures();
						//控制计划ID
						controlPlanFeature.setControlPlanId(controlPlan.getControlPlanId());
						controlPlanFeature.setCreationDate(now);
						//第一层
						if (f.getRanks() == 1L) {
							controlPlanFeature.setBranchId(f.getBranchId());
							controlPlanFeature.setFeaturesName(f.getBranchName());
							controlPlanFeature.setParentFeaturesId(null);
							controlPlanFeature.setRanks(1L);
							controlPlanFeaturesMapper.insertSelective(controlPlanFeature);
							List<HqmpdbMenuItemP> children = f.getChildren();	
							if (children != null && children.size() > 0) {
								children.forEach(g -> {
									//第二层
									if (g.getRanks() == 2L) {
										ControlPlanFeatures controlPlanFeature_a = new ControlPlanFeatures();
										//控制计划ID
										controlPlanFeature_a.setControlPlanId(controlPlan.getControlPlanId());
										controlPlanFeature_a.setCreationDate(now);
										controlPlanFeature_a.setFeature(g.getClaim());
										controlPlanFeature_a.setBranchId(g.getBranchId());
										controlPlanFeature_a.setFeaturesName(g.getBranchName());
										controlPlanFeature_a.setParentFeaturesId(controlPlanFeature.getFeaturesId());
										controlPlanFeature_a.setRanks(2L);
										//特征类型,默认取1
										controlPlanFeature_a.setFeaturesType("1");
										//特殊特性分类
										controlPlanFeature_a.setSpecialCharacterType(g.getSpecialCharacteristicType());
										//规范
										String aString = g.getPreventiveMeasure() != null ? g.getPreventiveMeasure() +";": "";
										String bString = g.getMeasureResult() != null ? g.getMeasureResult() : "";
										controlPlanFeature_a.setStandrad(aString+bString);
										//检测设备
										controlPlanFeature_a.setDetectionEquipment(g.getDetectionMeasure());
										controlPlanFeaturesMapper.insertSelective(controlPlanFeature_a);
									}
								});
							}
						}
					});
				}
			});
		});
	}	
}