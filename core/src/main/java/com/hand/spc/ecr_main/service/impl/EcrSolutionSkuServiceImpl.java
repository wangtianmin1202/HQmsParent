package com.hand.spc.ecr_main.service.impl;

import com.github.pagehelper.PageHelper;
import com.hand.spc.ecr_main.view.EcrResultMsgV0;
import com.hand.spc.ecr_main.service.IEcrMainService;
import com.hand.spc.ecr_main.service.IEcrProcessService;
import com.hand.spc.ecr_main.service.IEcrRfqService;
import com.hand.spc.ecr_main.service.IEcrSolutionMainService;
import com.hand.spc.ecr_main.service.IEcrSolutionService;
import com.hand.spc.ecr_main.service.IEcrSolutionSkuRelationService;
import com.hand.hap.core.IRequest;
import com.hand.hap.system.dto.CodeValue;
import com.hand.hap.system.dto.ResponseData;
import com.hand.hap.system.service.ICodeService;
import com.hand.hap.system.service.impl.BaseServiceImpl;
import com.hand.hcm.hcm_item_b.dto.ItemB;
import com.hand.hcm.hcm_item_b.mapper.ItemBMapper;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hand.spc.ecr_main.dto.EcrSolution;
import com.hand.spc.ecr_main.dto.EcrSolutionMain;
import com.hand.spc.ecr_main.dto.EcrSolutionSku;
import com.hand.spc.ecr_main.dto.EcrSolutionSkuRelation;
import com.hand.spc.ecr_main.dto.EcrTechnicalFileHeader;
import com.hand.spc.ecr_main.dto.EcrTechnicalFileLine;
import com.hand.spc.ecr_main.dto.EcrInfluencedmaterial;
import com.hand.spc.ecr_main.dto.EcrItemResult;
import com.hand.spc.ecr_main.dto.EcrMain;
import com.hand.spc.ecr_main.mapper.EcrSolutionMainMapper;
import com.hand.spc.ecr_main.mapper.EcrSolutionMapper;
import com.hand.spc.ecr_main.mapper.EcrSolutionSkuMapper;
import com.hand.spc.ecr_main.mapper.EcrSolutionSkuRelationMapper;
import com.hand.spc.ecr_main.mapper.EcrMainMapper;
import com.hand.spc.ecr_main.service.IEcrSolutionSkuService;
import com.hand.spc.ecr_main.service.IEcrTechnicalFileHeaderService;
import com.hand.spc.ecr_main.service.IEcrTechnicalFileLineService;
import com.hand.spc.ecr_main.view.EcrItemResultTmpV0;
import com.hand.spc.ecr_main.view.EcrSolutionSkuV0;
import com.hand.spc.ecr_main.view.EcrSolutionSkuV1;
import com.hand.spc.ecr_main.view.EcrSolutionSkuV2;
import com.hand.spc.ecr_main.view.EcrSolutionSkuV4;
import com.hand.spc.ecr_main.view.EcrTechnicalFileLineV0;

import oracle.jdbc.driver.DMSFactory;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(rollbackFor = Exception.class)
public class EcrSolutionSkuServiceImpl extends BaseServiceImpl<EcrSolutionSku> implements IEcrSolutionSkuService{

	@Autowired
	private EcrSolutionSkuMapper ecrSolutionSkuMapper;
	
	@Autowired
	private EcrSolutionMapper ecrSolutionMapper;
	@Autowired
	private EcrSolutionMainMapper ecrSolutionMainMapper;
	
	@Autowired
	private IEcrSolutionMainService ecrSolutionMainService;
	
	@Autowired
	private IEcrProcessService ecrProcessService;
	
	@Autowired
	private EcrMainMapper ecrMainMapper;
	
	@Autowired
	private IEcrMainService ecrMainService;
	
	@Autowired
	private ICodeService codeService; 
	
	@Autowired
	private IEcrTechnicalFileHeaderService ecrTechnicalFileHeaderService ;
	
	@Autowired
	private IEcrTechnicalFileLineService ecrTechnicalFileLineService;
	
	@Autowired
	private IEcrSolutionService ecrSolutionService;
	
	@Autowired
	private ItemBMapper itemBMapper;
	
	@Autowired
	private IEcrSolutionSkuRelationService ecrSolutionSkuRelationService;
	
	@Autowired
	private EcrSolutionSkuRelationMapper ecrSolutionSkuRelationMapper;
	
	@Autowired
	private IEcrRfqService ecrRfqService;
	
	public void updateEcrYearQty(IRequest iRequest, EcrSolutionSku dto) {
		ecrSolutionSkuMapper.updateEcrYearQty(dto);
	}
	
	/*
	 * 获取预测数量
	 
	public String getPlanQty(String itemId) {
		 
		Long fqty=ecrSolutionSkuMapper.getForecastQty( "2335", "");
		//获取当月完工数据
		Long mqty=ecrSolutionSkuMapper.getCompletedMonthQty("2335", "");
		BigDecimal a=new BigDecimal(fqty.toString());
		BigDecimal b=new BigDecimal(mqty.toString());
		return a.add(b).toString();
	}*/
	private void checkUpdate(IRequest iRequest,EcrMain dto) {
		EcrSolutionSku ecrSolutionSku=new EcrSolutionSku();
		ecrSolutionSku.setEcrno(dto.getEcrno());
		List<EcrSolutionSku> ecrSolutionSkus=ecrSolutionSkuMapper.select(ecrSolutionSku);
		
	//	Map<Long, List<EcrSolutionSku>>tmp1=	ecrSolutionSkus.stream().sorted(Comparator.comparing(EcrSolutionSku::getItemId)).collect(Collectors.groupingBy(EcrSolutionSku::getSkuId));	
		//出两套方案 已物料为主会有补充和报废两种区分两种 故需要两个方案
		EcrItemResult ecrItemResultPlan =new EcrItemResult ();
		for( EcrSolutionSku  entry:ecrSolutionSkus) { 
				if(entry.getYearPlanQty()==null||entry.getYearPlanQty()==0) {
					//这里 先获取对应SKU的物料id 然后进行数量计算 暂无逻辑  后续添加					  
						//获取预测数据
						Float fqty=ecrSolutionSkuMapper.getForecastQty( "2335", "");
						//获取当月完工数据
						Float mqty=ecrSolutionSkuMapper.getCompletedMonthQty("2335", "");
						//获取全年完工数据
						Float qty=ecrSolutionSkuMapper.getCompletedQty( "2335", "");						
						fqty+=qty-mqty;
						EcrSolutionSku ecrSolutionSku2=new EcrSolutionSku();
						ecrSolutionSku2.setYearPlanQty(fqty); 
						//将QTY回写
						ecrSolutionSku2.setSkuId( entry.getSkuId());
						ecrSolutionSku2.setEcrno(dto.getEcrno());
						ecrSolutionSku2.setLastUpdatedBy(iRequest.getUserId());
						self().updateEcrYearQty(iRequest,ecrSolutionSku2); 
				}			
		}
	}
	public List<EcrSolutionSkuV0> getHeadSku(IRequest iRequest, EcrMain dto, int page, int pageSize){
		//查询之前拽一下数据
		checkUpdate(iRequest,dto);
		PageHelper.startPage(page, pageSize);
		List<EcrSolutionSkuV0> ecrSolutionSkuV0s= ecrSolutionSkuMapper.getHeadSql(dto.getEcrno());
		for(EcrSolutionSkuV0 ecrSolutionSkuV0:ecrSolutionSkuV0s) {
			BigDecimal a=new BigDecimal(ecrSolutionSkuV0.getSumItemCost());
			BigDecimal b=new BigDecimal(ecrSolutionSkuV0.getSumYears());
			BigDecimal c=new BigDecimal(ecrSolutionSkuV0.getSumWorkCost());
			a=a.multiply(b);
			a=a.add(c);
			String sum=a.toString();
			ecrSolutionSkuV0.setSumCost(sum );
		}
		return ecrSolutionSkuV0s;
	}
	
	public List<EcrSolutionSkuV1> getLineSku(IRequest iRequest, EcrSolutionSku dto, int page, int pageSize){
		PageHelper.startPage(page, pageSize);
		return ecrSolutionSkuMapper.getLineSql(dto.getItemSkuKid().toString());
	}
	/*
	 * 根据ecr编码和物料id 修改skuItem数据
	 */
	public void updateByEcrnoAndItemId(EcrSolutionSku dto) {
		ecrSolutionSkuMapper.updateByEcrnoAndItemId( dto);
	}
	
	/*
	 * 根据ecr编码和物料id 修改skuItem数据
	 */
	public void updateByEcrnoAndSkuId(EcrSolutionSku dto) {
		ecrSolutionSkuMapper.updateByEcrnoAndSkuId(dto);
	}
	/*
	 * 前台数据保存逻辑
	 */
	public ResponseData saveResult(IRequest iRequest,EcrSolutionSkuV2 dto) {
		//查询改善方案表 如果没有记录 记录一笔数据 并设置初始状态为已录入A
		EcrSolutionMain main=new EcrSolutionMain();
		main.setEcrno(dto.getHeadList().getEcrno());
		int ct=ecrSolutionMainMapper.selectCount(main);
		//每一次进行保存 需要修改ecr申请单中预测完成时间
		EcrMain mian=new EcrMain();
		mian.setEcrno(dto.getHeadList().getEcrno());
		List<EcrMain> msa=ecrMainMapper.select(mian);
		msa.get(0).setPlanFinishedDate(ecrMainService.getChangeFinishedDate(iRequest, dto.getHeadList().getIssueType(), msa.get(0).getCreationDate(), msa.get(0).getRiskdegree()) );
		ecrMainService.updateByPrimaryKey(iRequest, msa.get(0));
		if(ct==0) {
			main.setIssueMsg(dto.getHeadList().getIssueMsg());
			main.setIssueType(dto.getHeadList().getIssueType());
			main.setStatus("B");
			ecrSolutionMainService.insert(iRequest, main);
		}
		else {
			//更新类型和描述、
			List<EcrSolutionMain>  ms=ecrSolutionMainMapper.select(main);
			ms.get(0).setIssueMsg(dto.getHeadList().getIssueMsg());
			ms.get(0).setIssueType(dto.getHeadList().getIssueType());
			ms.get(0).setStatus("B"); 
			ecrSolutionMainService.updateByPrimaryKey(iRequest, ms.get(0));
		}
		//先遍历行数据  进行数据存储 最后将头的工时成本变化记录到系统中
		for(EcrSolutionSkuV1 EcrSolutionSkuV1:dto.getLineList()) {
			//如果是空的 那么肯定是新增物料逻辑，将对应数据记录到行表中
			if(EcrSolutionSkuV1.getRelationId()==null||EcrSolutionSkuV1.getRelationId().isEmpty()) {
				//数据录入 将新增的物料进行录入
				/*EcrSolutionSku EcrSolutionSku=new EcrSolutionSku();
				EcrSolutionSku.setItemId(Long.valueOf(EcrSolutionSkuV1.getItemId()));
				EcrSolutionSku.setItemChangeType(EcrSolutionSkuV1.getItemChangeType());
				EcrSolutionSku.setBomUse(Float.valueOf(EcrSolutionSkuV1.getBomUse()));
				EcrSolutionSku.setEcrUse(Float.valueOf(EcrSolutionSkuV1.getEcrUse()));
				EcrSolutionSku.setSubItemFlag(EcrSolutionSkuV1.getSubItemFlag());
				EcrSolutionSku.setSubItem(EcrSolutionSkuV1.getSubItem());
				self().insertSelective(iRequest, EcrSolutionSku);*/			
				//修正版 向影响料号和SKU行表插入对应数据
			 
				EcrSolutionSkuRelation essr=new EcrSolutionSkuRelation();
				essr.setItemEcrId(Long.valueOf(EcrSolutionSkuV1.getItemId()));
				essr.setItemSkuId(Long.valueOf(EcrSolutionSkuV1.getItemSkuKid()));
				essr.setItemChangeType(EcrSolutionSkuV1.getItemChangeType());
				essr.setBomUse(Float.valueOf(EcrSolutionSkuV1.getBomUse()));
				essr.setEcrUse(Float.valueOf(EcrSolutionSkuV1.getEcrUse()));
				essr.setSubItemFlag(EcrSolutionSkuV1.getSubItemFlag());
				essr.setSubItem(EcrSolutionSkuV1.getSubItem());
				ecrSolutionSkuRelationService.insert(iRequest, essr);
			}
			else {
				//数据修改
				/*EcrSolutionSku EcrSolutionSku=new EcrSolutionSku();
				EcrSolutionSku.setItemSkuKid(Long.valueOf(EcrSolutionSkuV1.getItemSkuKid() ));
				EcrSolutionSku=self().selectByPrimaryKey(iRequest, EcrSolutionSku);
				EcrSolutionSku.setItemChangeType(EcrSolutionSkuV1.getItemChangeType());
				EcrSolutionSku.setBomUse(Float.valueOf(EcrSolutionSkuV1.getBomUse()));
				EcrSolutionSku.setEcrUse(Float.valueOf(EcrSolutionSkuV1.getEcrUse()));
				EcrSolutionSku.setSubItemFlag(EcrSolutionSkuV1.getSubItemFlag());
				EcrSolutionSku.setSubItem(EcrSolutionSkuV1.getSubItem());
				EcrSolutionSku.setItemSkuKid(Long.valueOf(EcrSolutionSkuV1.getItemSkuKid()));
				EcrSolutionSku.setLastUpdatedBy(iRequest.getUserId());
				 self().updateByEcrnoAndItemId(EcrSolutionSku);*/				 
				 
				 EcrSolutionSkuRelation essr=new EcrSolutionSkuRelation();
				 essr.setRelationId(Long.valueOf(EcrSolutionSkuV1.getRelationId()));
				 essr.setItemEcrId(Long.valueOf(EcrSolutionSkuV1.getItemId()));
				 essr.setItemSkuId(Long.valueOf(EcrSolutionSkuV1.getItemSkuKid()));
				 essr.setItemChangeType(EcrSolutionSkuV1.getItemChangeType());
				 essr.setBomUse(Float.valueOf(EcrSolutionSkuV1.getBomUse()));
				 essr.setEcrUse(Float.valueOf(EcrSolutionSkuV1.getEcrUse()));
				 essr.setSubItemFlag(EcrSolutionSkuV1.getSubItemFlag());
				 essr.setSubItem(EcrSolutionSkuV1.getSubItem());
				 ecrSolutionSkuRelationService.updateByPrimaryKey(iRequest, essr);
			}
		}
		//将头上数据进行修正
		if(dto.getHeadList().getSkuId()!=null) {
			EcrSolutionSku EcrSolutionSku=new EcrSolutionSku();
			EcrSolutionSku.setItemSkuKid(Long.valueOf(dto.getHeadList().getItemSkuId()));
			EcrSolutionSku=self().selectByPrimaryKey(iRequest, EcrSolutionSku);
			EcrSolutionSku.setWorkCost(Long.valueOf( dto.getHeadList().getSumWorkCost()));
			//EcrSolutionSku.setLastUpdatedBy(iRequest.getUserId());
			self().updateByPrimaryKey(iRequest, EcrSolutionSku);
		}
		ResponseData responseData=new ResponseData();
		responseData.setSuccess(true);		
		responseData.setMessage("成功");
		return responseData;
	}
	
	/**
	 *   方案提交后修改状态 触发审批流
	 */
	public ResponseData commitSolution(IRequest iRequest,EcrSolutionMain dto) {
		//判断状态如果不是B 则不能提交  因为还未对方案进行改动，如果不改动 先保存现有方案 再提交
		if(dto.getStatus()!=null&&dto.getStatus().equals("B")) {
			//更新状态为C
			EcrSolutionMain ec=new EcrSolutionMain();
			ec.setSolutionId(dto.getSolutionId());
			ec=ecrSolutionMainMapper.selectByPrimaryKey(ec);
			ec.setStatus("C");
			ecrSolutionMainService.updateByPrimaryKey(iRequest, ec);
			//提交审批流之前 将技术文件基础数据记录到对应数据表中
			
			self().saveTechData(iRequest,dto);
			//提交审批流
			EcrMain ecr=new EcrMain();
			ecr.setEcrno(dto.getEcrno());
			List<EcrMain> ecrs=ecrMainMapper.select(ecr);
			ecr.setProcessEmployeeCode(iRequest.getEmployeeCode());
			ecr.setKid(ecrs.get(0).getKid());
			ecrProcessService.startSolution(iRequest, ecr);
		}
		else {
			throw new RuntimeException("请确认单据状态，先保存方案再进行提交");
		}
		
		ResponseData responseData=new ResponseData();
		responseData.setSuccess(true);		
		responseData.setMessage("成功");
		return responseData;
	}
	
	

	/*
	 * 生成对应技术文件数据（计数任务）
	 * 
	 */
	public EcrResultMsgV0 saveTechData(IRequest iRequest,EcrSolutionMain dto) {		
		EcrResultMsgV0 ecrResultMsgV0=new EcrResultMsgV0();
		ecrResultMsgV0.setMsg("");
		ecrResultMsgV0.setStatus("S");
		EcrMain  em=new EcrMain();
		em.setEcrno(dto.getEcrno());
		List<EcrMain> ems= ecrMainMapper.select(em);
		if(ems.size()>0) {
			em=ems.get(0);
		}
		//获取所有解决方案中sku数据
		EcrSolutionSku ecrSolutionSku=new EcrSolutionSku();
		ecrSolutionSku.setEcrno(dto.getEcrno());
		List<EcrSolutionSku> ecrSolutionSkus=ecrSolutionSkuMapper.select( ecrSolutionSku);
		//获取快码 将数据插入
		List<CodeValue> codevalues=	codeService.selectCodeValuesByCodeName(iRequest, "HPM_TECH_TITLE");
		//循环快码将数据录入
		for(CodeValue cv:codevalues) {
			EcrTechnicalFileHeader ecrTechnicalFileHeader=new EcrTechnicalFileHeader();
			ecrTechnicalFileHeader.setTaskno(cv.getValue());
			ecrTechnicalFileHeader.setEcrno(dto.getEcrno());			
			ecrTechnicalFileHeader.setFileType(cv.getMeaning());
			//先将几个类型的主负责人带出
			if(cv.getMeaning().equals("EBOM")||cv.getMeaning().equals("图纸")) {
				ecrTechnicalFileHeader.setDutyby(em.getMainDuty());
			}
			//插入头表数据
			ecrTechnicalFileHeader=ecrTechnicalFileHeaderService.insert(iRequest, ecrTechnicalFileHeader);
			//数据记录后 发送不同类型任务到工作流
			
			ecrProcessService.startTask(iRequest, cv.getMeaning(), iRequest.getEmployeeCode(), ecrTechnicalFileHeader.getTechnicalId().longValue(), dto.getEcrno());
			
			//根据tag类型 将 SKU/物料/SKU物料插入我们的表中 
			//SKU获取编码逻辑欠缺 
			//将SKU插入行表	
			if(cv.getTag()!=null) {
				if(cv.getTag().equals("A")) {
					//Map<Long, List<EcrSolutionSku>> tmp= ecrSolutionSkus.stream().sorted(Comparator.comparing(EcrSolutionSku::getSkuId)).collect(Collectors.groupingBy(EcrSolutionSku::getSkuId));
					//按组循环 记录SKU数据
					for(EcrSolutionSku entry:ecrSolutionSkus) {
						EcrTechnicalFileLine ecrTechnicalFileLine=new EcrTechnicalFileLine();
						ecrTechnicalFileLine.setTechnicalId(ecrTechnicalFileHeader.getTechnicalId());
						ecrTechnicalFileLine.setItemId(entry.getSkuId());	
						
						//记录物料  更改信息  查询行表 
						ItemB itemB=new ItemB();
						itemB.setItemId((float)entry.getSkuId());
						itemB=ecrSolutionSkuMapper.getSingle(itemB);
						ecrTechnicalFileLine.setItemCode(itemB.getItemCode());						
						ecrTechnicalFileLineService.insert(iRequest, ecrTechnicalFileLine);
						
						EcrTechnicalFileLineV0 etfl1=new EcrTechnicalFileLineV0();
						etfl1.setEcrno(entry.getEcrno());
						etfl1.setFileType(cv.getMeaning());
						etfl1.setKid(ecrTechnicalFileLine.getKid());
						etfl1.setItemCode(itemB.getItemCode());
						ecrMainService.allocationTechBy(iRequest, etfl1);
					}
				}
				//将物料记录行表
				else if(cv.getTag().equals("B")) {
					
					for(EcrSolutionSku ess  :ecrSolutionSkus) {
						EcrSolutionSkuRelation essr=new EcrSolutionSkuRelation();
						essr.setItemSkuId(ess.getItemSkuKid());
						List<EcrSolutionSkuRelation> essrs=ecrSolutionSkuRelationMapper.select(essr);
						for(EcrSolutionSkuRelation esr:essrs) {
							EcrTechnicalFileLine ecrTechnicalFileLine=new EcrTechnicalFileLine();
							ecrTechnicalFileLine.setTechnicalId(ecrTechnicalFileHeader.getTechnicalId());
							ecrTechnicalFileLine.setItemId(esr.getItemEcrId());
							ItemB itemB=new ItemB();
							itemB.setItemId((float)esr.getItemEcrId());
							itemB=ecrSolutionSkuMapper.getSingle(itemB);
							ecrTechnicalFileLine.setItemCode(itemB.getItemCode());
							ecrTechnicalFileLineService.insert(iRequest, ecrTechnicalFileLine);
							
	
							EcrTechnicalFileLineV0 etfl1=new EcrTechnicalFileLineV0();
							etfl1.setEcrno(ess.getEcrno());
							etfl1.setFileType(cv.getMeaning());
							etfl1.setKid(ecrTechnicalFileLine.getKid());
							etfl1.setItemCode(itemB.getItemCode());
							ecrMainService.allocationTechBy(iRequest, etfl1);
						}
					}
				}
				//将SKU和物料都记录
				else if(cv.getTag().equals("C")) {
					//Map<Long, List<EcrSolutionSku>> tmp= ecrSolutionSkus.stream().sorted(Comparator.comparing(EcrSolutionSku::getSkuId)).collect(Collectors.groupingBy(EcrSolutionSku::getSkuId));
					//按组循环 记录SKU数据
					for( EcrSolutionSku entry:ecrSolutionSkus) {
						EcrTechnicalFileLine ecrTechnicalFileLine=new EcrTechnicalFileLine();
						ecrTechnicalFileLine.setTechnicalId(ecrTechnicalFileHeader.getTechnicalId());
						ecrTechnicalFileLine.setItemId(entry.getSkuId());
						ItemB itemBS=new ItemB();
						itemBS.setItemId((float)entry.getSkuId() );
						itemBS=ecrSolutionSkuMapper.getSingle(itemBS);
						ecrTechnicalFileLine.setItemCode(itemBS.getItemCode());
						ecrTechnicalFileLineService.insert(iRequest, ecrTechnicalFileLine);
						EcrSolutionSkuRelation essr=new EcrSolutionSkuRelation();
						essr.setItemSkuId(entry.getItemSkuKid());
						List<EcrSolutionSkuRelation> essrs=ecrSolutionSkuRelationMapper.select(essr);
						for(EcrSolutionSkuRelation esr:essrs) {
							    ecrTechnicalFileLine=new EcrTechnicalFileLine();
								ecrTechnicalFileLine.setTechnicalId(ecrTechnicalFileHeader.getTechnicalId());
								ecrTechnicalFileLine.setItemId(esr.getItemEcrId() );
								ItemB itemB=new ItemB();
								itemB.setItemId((float)esr.getItemEcrId() );
								itemB=ecrSolutionSkuMapper.getSingle(itemB);
								ecrTechnicalFileLine.setItemCode(itemB.getItemCode());
								ecrTechnicalFileLineService.insert(iRequest, ecrTechnicalFileLine);
	
								EcrTechnicalFileLineV0 etfl1=new EcrTechnicalFileLineV0();
								etfl1.setEcrno(entry.getEcrno());
								etfl1.setFileType(cv.getMeaning());
								etfl1.setKid(ecrTechnicalFileLine.getKid());
								etfl1.setItemCode(itemB.getItemCode());
								ecrMainService.allocationTechBy(iRequest, etfl1);
						}
					}
				}
			}
		}
		return ecrResultMsgV0;
	}
	
	
	/**
	 * 改善方案物料选择下拉菜单
	 * @param dto
	 * @return
	 */
	public List<EcrSolutionSkuV1> getSolutionList(EcrMain dto){
		return ecrSolutionSkuMapper.getSolutionList(dto.getEcrno());
	}
	
	/**
	 * 选择物料后下拉的明细
	 * @param dto
	 * @return
	 */
	public List<EcrSolutionSkuV1> getDetailRfq(EcrInfluencedmaterial dto){
		//PageHelper.startPage(page, pageSize);
		return ecrSolutionSkuMapper.getDetailRfq(dto.getMaterialId().toString(),dto.getEcrno());
	}
	
	public List<EcrSolutionSkuV1> getRfqCommitList(EcrMain dto){
		return ecrSolutionSkuMapper.getRfqCommitList(dto.getEcrno());
	}
	
	/**
	 * 选择物料自动提交工作流
	 * @param iRequest
	 * @param dto
	 * @return
	 */
	public ResponseData rfqCommitProcess(IRequest iRequest,EcrSolutionSkuV4 dto) {
		
		ResponseData rd=new ResponseData();		
		rd.setSuccess(true);
		rd.setMessage("成功");
		ecrRfqService.startProcessRfq(iRequest, dto.getEcrno(), dto.getItemIds());
		return rd;
	}
}