package com.hand.spc.ecr_main.service.impl;

import com.github.pagehelper.PageHelper;
import com.hand.hap.activiti.service.IActivitiService;
import com.hand.hap.core.IRequest;
import com.hand.hap.hr.dto.Employee;
import com.hand.hap.hr.mapper.EmployeeMapper;
import com.hand.hap.mail.service.IMessageService;
import com.hand.hap.system.dto.CodeValue;
import com.hand.hap.system.dto.ResponseData;
import com.hand.hap.system.service.ICodeService;
import com.hand.hap.system.service.impl.BaseServiceImpl;
import com.hand.hqm.hqm_nonconforming_order.dto.NonconformingOrder;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.activiti.rest.service.api.engine.variable.RestVariable;
import org.activiti.rest.service.api.runtime.process.ProcessInstanceCreateRequest;
import org.activiti.rest.service.api.runtime.process.ProcessInstanceResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hand.spc.ecr_main.dto.EcrInfluencedmaterial;
import com.hand.spc.ecr_main.dto.EcrItemSku;
import com.hand.spc.ecr_main.dto.EcrMain;
import com.hand.spc.ecr_main.dto.EcrSample;
import com.hand.spc.ecr_main.dto.EcrSolution;
import com.hand.spc.ecr_main.dto.EcrSolutionMain;
import com.hand.spc.ecr_main.dto.EcrSolutionSku;
import com.hand.spc.ecr_main.dto.EcrSolutionSkuRelation;
import com.hand.spc.ecr_main.dto.EcrTaskUser;
import com.hand.spc.ecr_main.dto.EcrTechnicalFileLine;
import com.hand.spc.ecr_main.dto.ItemSkuRelaition;
import com.hand.spc.ecr_main.mapper.EcrInfluencedmaterialMapper;
import com.hand.spc.ecr_main.mapper.EcrMainMapper;
import com.hand.spc.ecr_main.mapper.EcrTaskUserMapper;
import com.hand.spc.ecr_main.service.IEcrInfluencedmaterialService;
import com.hand.spc.ecr_main.service.IEcrItemSkuService;
import com.hand.spc.ecr_main.service.IEcrMainService;
import com.hand.spc.ecr_main.service.IEcrProjectTrackingService;
import com.hand.spc.ecr_main.service.IEcrSampleService;
import com.hand.spc.ecr_main.service.IEcrSolutionMainService;
import com.hand.spc.ecr_main.service.IEcrSolutionService;
import com.hand.spc.ecr_main.service.IEcrSolutionSkuRelationService;
import com.hand.spc.ecr_main.service.IEcrSolutionSkuService;
import com.hand.spc.ecr_main.service.IEcrTaskUserService;
import com.hand.spc.ecr_main.service.IEcrTechnicalFileLineService;
import com.hand.spc.ecr_main.service.IItemSkuRelaitionService;
import com.hand.spc.ecr_main.view.EcrCreateTmpV0;
import com.hand.spc.ecr_main.view.EcrMainV0;
import com.hand.spc.ecr_main.view.EcrMainV1;
import com.hand.spc.ecr_main.view.EcrMainV2;
import com.hand.spc.ecr_main.view.EcrMaterialV0;
import com.hand.spc.ecr_main.view.EcrTechnicalFileLineV0;
import com.hand.spc.utils.MailUtil;

import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(rollbackFor = Exception.class)
public class EcrMainServiceImpl extends BaseServiceImpl<EcrMain> implements IEcrMainService{
	@Autowired
	private IEcrInfluencedmaterialService ecrInfluencedmaterialService;
		
	@Autowired
	private EcrInfluencedmaterialMapper ecrInfluencedmaterialMapper;
	
	@Autowired
	private IEcrSampleService ecrSampleService;
	
	@Autowired
	private IMessageService messageService;
	
	@Autowired
	private EcrMainMapper ecrMainMapper;
	
	@Autowired
	private ICodeService codeService; 
	
	@Autowired
	private IActivitiService activitiService;
	
	@Autowired
	private IEcrSolutionService ecrSolutionService;
	
	@Autowired
	private IEcrItemSkuService ecrItemSkuService;
	
	
	@Autowired
	private IEcrSolutionSkuService ecrSolutionSkuService;
	
	@Autowired
	private IItemSkuRelaitionService itemSkuRelaitionService;
	
	@Autowired
	private IEcrSolutionSkuRelationService ecrSolutionSkuRelationService;
	
	@Autowired
	private IEcrTaskUserService ecrTaskUserService;
	
	@Autowired
	private  EcrTaskUserMapper ecrTaskUserMapper;
	
	@Autowired
	private  EmployeeMapper employeeMapper;
	
	@Autowired
	private IEcrTechnicalFileLineService ecrTechnicalFileLineService;
	
	@Autowired
	private IEcrProjectTrackingService ecrProjectTrackingService;
	
	
	@Autowired
	private IEcrSolutionMainService ecrSolutionMainService;
	
	public String getNumber(String ruleCode) {
		String number=ecrMainMapper.getNumer(ruleCode);
		
		String str = String.format("%04d",Integer.valueOf(number)+1);
		return ruleCode + str;// 最终检验单号
	}

	public List<EcrMainV0> selectSingleOrder(IRequest iRequest,EcrMain dto) {
		List<EcrMainV0> atr=new ArrayList<EcrMainV0>();
		atr.add(ecrMainMapper.singleOrder(dto.getEcrno(),dto.getIndividuationSql()));
		return atr;
		
	}
	
	
	public List<EcrMainV1> baseQuery(IRequest iRequest,EcrMainV1 dto, int page, int pageSize){
		
		PageHelper.startPage(page, pageSize);
		if (dto != null) {
			// 风险程度多选框值转换成 list
			String riskdegree = dto.getRiskdegree();
			if(riskdegree != null && !"".equals(riskdegree)) {
				String[] riskdegrees = riskdegree.split(";");
				dto.setRiskdegreeList(Arrays.asList(riskdegrees));
			}
			if(!validate(dto.getRelevanceFirst()) || !validate(dto.getRelevanceSecond())) {
				throw new RuntimeException("关联词错误");
			}
		}
		List<EcrMainV1> list = ecrMainMapper.baseQuery(dto);
		return list;
	}
	
	/**
	 * 判断关联词是否规范
	 * @param relevance
	 * @return
	 */
	private boolean validate(String relevance) {
		boolean rs = false;
		String[] arr = {"and", "or", "AND", "OR"} ;
		List<String> list = Arrays.asList(arr);

		if(relevance == null || "".equals(relevance)) {
			rs = true;
		} else {
			if(list.contains(relevance)) {
				rs = true;
			}
		}
		return rs;
	}

	public Date getChangeFinishedDate(IRequest iRequest,String type,Date oldFinishedDate,String risk) {
		CodeValue codevalue=	codeService.getCodeValue(iRequest, "HQM_8D_PROBLEM_TYPE", type);
		//根据风险程度 计算预计完成时间
				//如果是高风险   直接周期增加30天
				SimpleDateFormat planDate = new SimpleDateFormat("yyyy-MM-dd");
				if(risk.equals("A")) {			
					Calendar calendar= Calendar.getInstance();
					calendar.setTime(new Date());
					calendar.add(Calendar.DAY_OF_YEAR, 30);
					 return calendar.getTime() ;
				}
				//如果是中低风险 根绝快码中标准值 进行时间叠加
				else {
					
					Calendar calendar= Calendar.getInstance();
					calendar.setTime(new Date());
					calendar.add(Calendar.DAY_OF_YEAR, Integer.valueOf( codevalue.getTag()));
					return calendar.getTime() ;
				}
	}
	
	 private EcrCreateTmpV0 getTmp(List<EcrCreateTmpV0> dtos,String skuCode) {
		for(EcrCreateTmpV0 tmp:dtos) {
			if(tmp.getSkuCode().equals(skuCode)) {
				return tmp;
			}
		}
		return null;
	}
	public ResponseData createOrder(IRequest iRequest,EcrMainV0 dto) {
		ResponseData responseData=new ResponseData();
		EcrMain ecrMain=new EcrMain();
		ecrMain.setHappenddatetime(new Date());
		ecrMain.setIssue(dto.getIssue());
		ecrMain.setIssuesourceno(dto.getIssueFrom());
		ecrMain.setRiskdegree(dto.getRisk());
		ecrMain.setUfr(dto.getUfr());
		ecrMain.setIssueType(dto.getIssueType());
		ecrMain.setEcrType(dto.getEcrType());
		String ecrno="";
		Date date=new Date();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy");
		String time = dateFormat.format(date);
		ecrno = "ECR" + time;
		ecrno=getNumber(ecrno);
		ecrMain.setEcrno(ecrno);
		//对应生成改善方案主数据
		EcrSolutionMain esm=new EcrSolutionMain();
		esm.setEcrno(ecrno);
		ecrSolutionMainService.insertSelective(iRequest, esm);
		
		String codeValues= codeService.getCodeMeaningByValue(iRequest, "HQM_8D_PROBLEM_LEVEL", dto.getRisk());
		 
			ecrMain.setPlanFinishedDate(self().getChangeFinishedDate(iRequest,dto.getIssueType(),date,dto.getRisk())); 
			if(dto.getMaterialIds()!=null&&dto.getMaterialIds().size()>0) {
				ecrMain.setMatneedcontrol("Y");
			}
			else {				
				ecrMain.setMatneedcontrol("N");
			}
		self().insert(iRequest, ecrMain);
		if(dto.getMaterialIds()!=null&&dto.getMaterialIds().size()>0) {
			ecrMain.setMatneedcontrol("Y");
			EcrMainV2 ecrMainV2=null;
			//add by silin.zhang 增加职位负责人逻辑  如果所有物料对应主职位的负责人只有一个时 默认写入该员工 否则跳过
			int ct=ecrMainMapper.countUser(dto.getMaterialIds());
			//如果查询数量为1 证明物料集合的主负责人只有一个  直接将该人和对应的职位类别集写入数据表中
				if(ct==1) {
				List<EcrMainV2> ecrMainV2s=ecrMainMapper.getPosition(dto.getMaterialIds());
				 ecrMainV2=ecrMainV2s.get(0);
				 ecrMainV2.setEcrno(ecrno);
				 //dto.getFinishDate()
				 MailUtil.sendMail(ecrMainV2.getEmail(), "由 你负责的ECR--"+ecrno+"要求在"+ dateFormat.format(ecrMain.getPlanFinishedDate())+"前完成,风险程度"+codeValues, ecrno+"主负责人已分配给你，请在ECR界面查看");				
					self().saveDutys(ecrMainV2);
			}
				//记录已经创建过的SKU记录 不需要再访问数据库进行ct查询
				List<EcrCreateTmpV0> skuList=new ArrayList();
			for(int i=0;i<dto.getMaterialIds().size();i++) {				
				EcrInfluencedmaterial  ecrInfluencedmaterial=new EcrInfluencedmaterial();
				EcrSolution  ecrSolution=new EcrSolution();
				//如果EcrMainV2不为空  那么将对应的position信息写入
				if(ecrMainV2!=null) {
					ecrInfluencedmaterial.setCategoryFirst(ecrMainV2.getCategoryFirst());
					ecrInfluencedmaterial.setCategorySecond(ecrMainV2.getCategorySecond());
					ecrInfluencedmaterial.setCategoryThird(ecrMainV2.getCategoryThird());
					ecrInfluencedmaterial.setMainDuty(ecrMainV2.getMainDuty());
					ecrInfluencedmaterial.setSubDuty(ecrMainV2.getSubDuty());
					ecrInfluencedmaterial.setMainPosition(ecrMainV2.getMainPosition());
					ecrInfluencedmaterial.setSubPosition(ecrMainV2.getSubPosition());
					//ecrInfluencedmaterial.setMaterialId(Long.valueOf(dto.getMaterialIds().get(i)));


					
					ecrSolution.setCategoryFirst(ecrMainV2.getCategoryFirst());
					ecrSolution.setCategorySecond(ecrMainV2.getCategorySecond());
					ecrSolution.setCategoryThird(ecrMainV2.getCategoryThird());
					ecrSolution.setMainDuty(ecrMainV2.getMainDuty());
					ecrSolution.setSubDuty(ecrMainV2.getSubDuty());
					ecrSolution.setMainPosition(ecrMainV2.getMainPosition());
					ecrSolution.setSubPosition(ecrMainV2.getSubPosition());
					//ecrSolution.setMaterialId(Long.valueOf(dto.getMaterialIds().get(i)));
					
				}
				
				
				ecrInfluencedmaterial.setOnhandStatus("B");				
				ecrInfluencedmaterial.setMaterialId(Long.valueOf(dto.getMaterialIds().get(i)));
				ecrInfluencedmaterial.setEcrno(ecrno);
				ecrInfluencedmaterial.setBuyer(ecrMainMapper.getBuyer(dto.getMaterialIds().get(i)));
				//获取物料采购周期
				 Long days=  ecrMainMapper.getItemLeadTime(dto.getMaterialIds().get(i));
				 ecrInfluencedmaterial.setBuyerCycle(days.toString());
				ecrSolution.setOnhandStatus("B");
				ecrSolution.setMaterialId(Long.valueOf(dto.getMaterialIds().get(i)));
				ecrSolution.setEcrno(ecrno);
				ecrInfluencedmaterialService.insert(iRequest, ecrInfluencedmaterial);
				ecrSolutionService.insert(iRequest, ecrSolution);
				//
				
				
				
				//获取SKU信息进行数据录入
				List<EcrMaterialV0> emvs= ecrInfluencedmaterialMapper.detailProductquery(ecrInfluencedmaterial);
				
				for(EcrMaterialV0 tmp:emvs) {
					//不存在则创建数据  否则不创建
					EcrCreateTmpV0 ecr=getTmp(skuList,tmp.getSkucode());
					if(ecr==null) {
						ecr=new EcrCreateTmpV0();
						EcrItemSku eis=new EcrItemSku();
						EcrSolutionSku ess=new EcrSolutionSku();
											 
						//改善方案会变动记录历史等原因  这边会复制一份原单的数据到改善方案中施行
						eis.setSkuId(Long.valueOf(tmp.getSkuId()));
						eis.setSkuCode(tmp.getSkucode());
						eis.setSkuDescription(tmp.getSkuDescription());
					//	eis.setItemId(Long.valueOf(dto.getMaterialIds().get(i)));
						eis.setProducts(tmp.getProducts());
						eis.setProductType(tmp.getProductType());
						eis.setBomUse(Float.valueOf(tmp.getBaseQty()));
						eis.setEcrno(ecrno);
						//判断是否为空 如果不存在 就
						if(tmp.getStdCost()!=null) {
							eis.setStdCost(Float.valueOf(tmp.getStdCost()));
						}
						else {
							throw new RuntimeException("标准费用异常，请检查");
						}
						if(tmp.getStdPriceUnit()!=null) {
							eis.setStdPriceUnit(Float.valueOf(tmp.getStdPriceUnit()));
						}
						else {
							throw new RuntimeException("费用单位异常异常，请检查");
						}
						if(tmp.getStdCostCurrency()!=null) {
							eis.setStdCostCurrency(tmp.getStdCostCurrency());
						}
						
						
						ess.setSkuId(Long.valueOf(tmp.getSkuId()));
						ess.setSkuCode(tmp.getSkucode());
						ess.setSkuDescription(tmp.getSkuDescription());
						//ess.setItemId(Long.valueOf(dto.getMaterialIds().get(i)));
						ess.setProducts(tmp.getProducts());
						ess.setProductType(tmp.getProductType());
						ess.setBomUse(Float.valueOf(tmp.getBaseQty()));
						ess.setEcrno(ecrno);
						ess.setStdCost(Float.valueOf(tmp.getStdCost()));
						ess.setStdPriceUnit(Float.valueOf(tmp.getStdPriceUnit()));
						ess.setStdCostCurrency(tmp.getStdCostCurrency());
						
						eis=ecrItemSkuService.insert(iRequest, eis);
						ess=ecrSolutionSkuService.insert(iRequest, ess);
						ecr.setSkuCode(tmp.getSkucode());
						ecr.setItemSkuId(eis.getItemSkuKid().toString());
						ecr.setItemSkuSolutionId(ess.getItemSkuKid().toString());
						ecr.setBomUse(ess.getBomUse());
						skuList.add(ecr);
					}
					//创建影响料号和对应SKU关系
					ItemSkuRelaition isr=new ItemSkuRelaition();
					EcrSolutionSkuRelation essr=new EcrSolutionSkuRelation();
					isr.setItemSkuId(Long.valueOf(ecr.getItemSkuId()));
					isr.setItemEcrId(ecrInfluencedmaterial.getMaterialId());
					isr.setBomUse(ecr.getBomUse());
					itemSkuRelaitionService.insert(iRequest, isr);
					
					essr.setItemSkuId(Long.valueOf(ecr.getItemSkuSolutionId()) );
					essr.setBomUse(ecr.getBomUse());
					essr.setItemEcrId(ecrSolution.getMaterialId());
					ecrSolutionSkuRelationService.insert(iRequest, essr);
				}
			
			
				
			}
			
		}
		 
		
		for(String sampleId:dto.getSampleIds()) {
			EcrSample ecrSample=new EcrSample();
			ecrSample.setEcrno(ecrno);
			ecrSample.setSampleId(Float.valueOf(sampleId));
			ecrSampleService.insert(iRequest, ecrSample);
		}			
		//增加进度跟踪数据		
		ecrProjectTrackingService.insertInfo(iRequest, ecrno); 			 
		responseData.setSuccess(true);
		responseData.setMessage(ecrno);
		return responseData;
	}
	
	public void saveDutys(EcrMainV2 dto) {
		ecrMainMapper.saveMainDuty(dto);
		ecrMainMapper.saveMainDutyHead(dto);
	}
	public ResponseData saveMainDuty(IRequest iRequest,EcrMainV2 dto) {			
		ResponseData responseData=new ResponseData();
		dto.setUpdateBy(iRequest.getUserId().toString());
		//后面可能会加上一些校验 这里先记录一下
		if(dto==null||dto.getEcrno()==null) {
					throw new RuntimeException("请检查记录是否有误");			
		}		
		
		EcrMainV0 ecr= ecrMainMapper.singleOrder(dto.getEcrno(),"");
		String codeValues= codeService.getCodeMeaningByValue(iRequest, "HQM_8D_PROBLEM_LEVEL", ecr.getRisk());
		//更新ecr物料中该单号下得所有物料的主负责人  辅助人员等信息
		MailUtil.sendMail(dto.getEmail(), "由 你负责的ECR--"+dto.getEcrno()+"要求在"+ecr.getFinishDate()+"前完成,风险程度"+codeValues, dto.getEcrno()+"主负责人已分配给你，请在ECR界面查看");
		self().saveDutys(dto);
		responseData.setMessage("提交成功");
		responseData.setSuccess(true);
		return responseData;
		
	}
	
	
	public EcrMain startProcess(IRequest request, EcrMain dto) {
		
		String processKey = "wuliaoguanlibiao";
		ProcessInstanceCreateRequest create = new ProcessInstanceCreateRequest();
		create.setBusinessKey(String.valueOf(dto.getKid()));   // id
		request.setEmployeeCode(dto.getProcessEmployeeCode()); // 申请人
		create.setProcessDefinitionKey(processKey);
		
		List<RestVariable> variables  = new ArrayList<>();
		RestVariable va = new RestVariable();
		va.setName("email");
		va.setType("string");
		va.setValue("");
		variables.add(va);
		RestVariable va2 = new RestVariable();
		va2.setName("ecrno");
		va2.setType("string");
		va2.setValue(dto.getEcrno());
		variables.add(va2);
		create.setVariables(variables);
		
		ProcessInstanceResponse response = activitiService.startProcess(request, create);
		String processId = response.getId();
		System.out.println(processId);
		// 设置状态：审批中
		dto.setProcessStatus("A");
		// 设置流程 ID
		dto.setProcessId(Long.valueOf(processId));
		// 设置流程启动时间
		dto.setProcessStartDate(new Date());
		return dto;
	}

	
	public void allocationTechBy(IRequest iRequest,EcrTechnicalFileLineV0 dto) {
		EcrTechnicalFileLine etf=new EcrTechnicalFileLine();
		etf.setKid(dto.getKid());
		etf=ecrTechnicalFileLineService.selectByPrimaryKey(iRequest, etf);
		//根据传入的任务类型 查看是否存在任务类型的数据 如果存在 直接取员工进行修改		
		EcrTaskUser etu=new EcrTaskUser();
		etu.setRuleCode(dto.getFileType());
		List<EcrTaskUser> etus=ecrTaskUserMapper.select(etu);
		//如果存在数据 那么证明任务编码时code类型或者默认类型 需要直接写入
		if(etus.size()>0) {
			//判断类型
			if(etus.get(0).getRuleType().equals("CODE")) {
				//根据employee id获取负责人
				Employee em=new Employee();
				em.setEmployeeId( etus.get(0).getEmployeeId());
				em=employeeMapper.selectByPrimaryKey(em);
				etf.setDutyby(em.getName());
				ecrTechnicalFileLineService.updateByPrimaryKey(iRequest, etf);	
			}
			//否则直接使用默认负责人
			else {
				EcrMain em=new EcrMain();
				em.setEcrno(dto.getEcrno());
				List<EcrMain> ems=ecrMainMapper.select(em);
				etf.setDutyby(ems.get(0).getMainDuty());
				ecrTechnicalFileLineService.updateByPrimaryKey(iRequest, etf);	
			}
			
		}
		//否则取对应的SKU编码
		else {
			etu.setRuleCode(dto.getItemCode());
			etus=ecrTaskUserMapper.select(etu);
			if(etus.size()>0) {
				Employee em=new Employee();
				em.setEmployeeId( etus.get(0).getEmployeeId());
				em=employeeMapper.selectByPrimaryKey(em);
				etf.setDutyby(em.getName());
				ecrTechnicalFileLineService.updateByPrimaryKey(iRequest, etf);	
			}
		}
	}
}
