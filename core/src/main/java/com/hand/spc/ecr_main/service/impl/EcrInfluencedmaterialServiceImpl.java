package com.hand.spc.ecr_main.service.impl;

import com.hand.hap.core.IRequest;
import com.hand.hap.system.dto.ResponseData;
import com.hand.hap.system.service.impl.BaseServiceImpl;
import com.hand.hcm.hcm_item_b.dto.ItemB;
import com.hand.hcm.hcm_item_b.mapper.ItemBMapper;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.hand.spc.ecr_main.dto.EcrInfluencedmaterial;
import com.hand.spc.ecr_main.dto.EcrItemResult;
import com.hand.spc.ecr_main.dto.EcrMain;
import com.hand.spc.ecr_main.mapper.EcrInfluencedmaterialMapper;
import com.hand.spc.ecr_main.mapper.EcrItemResultMapper;
import com.hand.spc.ecr_main.mapper.EcrMainMapper;
import com.hand.spc.ecr_main.mapper.EcrSolutionSkuMapper;
import com.hand.spc.ecr_main.service.IEcrInfluencedmaterialService;
import com.hand.spc.ecr_main.service.IEcrItemResultService;
import com.hand.spc.ecr_main.service.IEcrProcessService;
import com.hand.spc.ecr_main.service.IEcrSolutionSkuService;
import com.hand.spc.ecr_main.view.EcrApsTmpV0;
import com.hand.spc.ecr_main.view.EcrApsV0;
import com.hand.spc.ecr_main.view.EcrItemResultTmpV0;
import com.hand.spc.ecr_main.view.EcrMainV0;
import com.hand.spc.ecr_main.view.EcrMaterialResultV0;
import com.hand.spc.ecr_main.view.EcrMaterialV0;

import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(rollbackFor = Exception.class)
public class EcrInfluencedmaterialServiceImpl extends BaseServiceImpl<EcrInfluencedmaterial> implements IEcrInfluencedmaterialService{

	@Autowired
	private EcrInfluencedmaterialMapper ecrInfluencedmaterialMapper;
	
	@Autowired
	private IEcrItemResultService ecrItemResultService;
	
	@Autowired
	private EcrItemResultMapper ecrItemResultMapper;
	
	@Autowired
	private ItemBMapper itemBMapper;
	
	@Autowired
	private EcrMainMapper ecrMainMapper;
	
	@Autowired
	private IEcrSolutionSkuService ecrSolutionSkuService;
	
	@Autowired
	private EcrSolutionSkuMapper ecrSolutionSkuMapper;
	
	@Autowired
	private IEcrProcessService  ecrProcessService;
	
	public List<EcrMaterialV0> basequery(EcrInfluencedmaterial dto) {
		//一共就两个层级所以直接按照分组形式写入 2020年2月27日17:03:56 add by silin.zhang 
	/*	List<EcrMaterialV0> ecrMaterialV0s1=new ArrayList<>();
		List<EcrMaterialV0> ecrMaterialV0s= ecrInfluencedmaterialMapper.basequery(dto);
		Map<String, List<EcrMaterialV0>> tmp= ecrMaterialV0s.stream().collect(Collectors.groupingBy(EcrMaterialV0::getItemCode));
		int i=0;
		for(List<EcrMaterialV0> entry:tmp.values()) {
			EcrMaterialV0 lv1=new EcrMaterialV0();
			lv1.setItemCode(entry.get(0).getItemCode());
			lv1.setLevelId("1");
			lv1.setItemId(entry.get(0).getItemId());
			lv1.setId(Long.valueOf(entry.get(0).getItemId()));
			lv1.setItemDescription(entry.get(0).getItemDescription());
			
			for(EcrMaterialV0 ecrMaterialV0:entry) {

				ecrMaterialV0.setLevelId("2");
				ecrMaterialV0.setItemCode(ecrMaterialV0.getProductType());
				ecrMaterialV0.setItemDescription(ecrMaterialV0.getProducts());
				ecrMaterialV0.setId((long)i);
				ecrMaterialV0.setParent(lv1);
				i++;
			}
			//写入第一层物料
			lv1.setChildren(entry);
			ecrMaterialV0s1.add(lv1);
		}*/
		
		return ecrInfluencedmaterialMapper.basequery(dto);
	}
	
	public List<EcrMaterialV0> detailProductquery(EcrInfluencedmaterial dto) {
		return ecrInfluencedmaterialMapper.detailProductquery(dto);
		
	}
	/*
	 * 主负责人维护进入界面的数据获取
	 */
	public List<EcrMainV0> dutySingleOrder(IRequest iRequest,EcrMain dto){
		return ecrInfluencedmaterialMapper.dutySingleOrder( dto);
	}
	
	public List<EcrMaterialResultV0> getItemResult(IRequest iRequest,EcrMain dto){
		List<EcrMaterialResultV0> ecrMaterialResultV0s=new ArrayList();
		EcrItemResult ecrItemResult=new EcrItemResult();
		ecrItemResult.setEcrno(dto.getEcrno());
		ecrItemResult.setStatust(dto.getStatus());
		List<EcrItemResult> ecrItemResults=new ArrayList();
		ecrItemResults=ecrItemResultMapper.select(ecrItemResult);
		Map<Long, List<EcrItemResult>> tmp= ecrItemResults.stream().sorted(Comparator.comparing(EcrItemResult::getItemId)).collect(Collectors.groupingBy(EcrItemResult::getGroupId));
		
		for(List<EcrItemResult> entry:tmp.values()) {
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			EcrMaterialResultV0 ecrMaterialResultV0=new EcrMaterialResultV0();
			if(entry.get(0).getGroupId()!=null) {
				ecrMaterialResultV0.setGroupId( entry.get(0).getGroupId() );
			}
			
			if(entry.get(0).getCycleDays()!=null) {
				ecrMaterialResultV0.setCycleDays( entry.get(0).getCycleDays() );
			}
			if(entry.get(0).getStatust()!=null) {
				ecrMaterialResultV0.setStatus( entry.get(0).getStatust());
			}
			if(entry.get(0).getScrap()!=null) {
				
			}
			
			//获取结果数据 将物料分组处理，前台进行动态加载
			List<String> itemCode=new ArrayList();
			List<Float> qtys=new ArrayList();
			Date lastDate=new Date();
			Float scraps=Float.valueOf("0");
			for(EcrItemResult ecrMaterialV0:entry) {
				
				ItemB itemb=new ItemB();
				itemb.setItemId((float)ecrMaterialV0.getItemId() );
				List<ItemB> itembs=	itemBMapper.select(itemb);
				if(itembs.size()>0) {
					itemCode.add(itembs.get(0).getItemCode());	
				}
				if(ecrMaterialV0.getQty()!=null)
					qtys.add(ecrMaterialV0.getQty() );
				ecrMaterialResultV0.setQtys(qtys);
				ecrMaterialResultV0.setItemCodes(itemCode);
				if(ecrMaterialV0.getScrap()!=null) {
					scraps+= ecrMaterialV0.getScrap();
				}
				if(ecrMaterialV0.getAdviceChageTime()!=null) {
					if(ecrMaterialV0.getAdviceChageTime().compareTo(lastDate)>0) {
						lastDate=ecrMaterialV0.getAdviceChageTime();
					}
				
				}
			}
			ecrMaterialResultV0.setAdviceChangeTime(lastDate);
			ecrMaterialResultV0.setScrap(scraps);
			//写入第一层物料
			ecrMaterialResultV0s.add(ecrMaterialResultV0);
		}
		//根据ecr编码获取数据
		return ecrMaterialResultV0s;
	}
	
	private  Float   getOnhands(List<EcrMaterialV0> dtos,Long itemId) {
		for(EcrMaterialV0 ecrMaterialV0:dtos) {
			if(Long.valueOf(ecrMaterialV0.getItemId()).equals(itemId)) {
				return Float.valueOf(ecrMaterialV0.getOnhandQty());
			}
		}
		return null;
	}
	
	/*
	 * 将对应计划数据存入临时表进行物料管控计算
	 */
	public List<EcrApsV0> apsTmpData(List<String> itemIds){
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM");
		List<EcrApsV0> tmp=new ArrayList();
		for(String itemId:itemIds) {
			List<EcrApsTmpV0> pl=	ecrSolutionSkuMapper.getCompletedQtys("2335", "");
			List<EcrApsTmpV0> fc=	ecrSolutionSkuMapper.getForecastQtys("2335", "");
			for(EcrApsTmpV0 et:fc) {
				boolean flag=false;
				EcrApsV0 ea=new EcrApsV0();
				ea.setItemId(Long.valueOf(itemId));
				ea.setDemandDate(dateFormat.format(et.getMonth()));
				for(EcrApsTmpV0 p:pl) {
					
					if(p.getMonth().compareTo(et.getMonth())==0) {
						 flag=true;
						ea.setQty( et.getQty()-p.getQty());
						
						break;
					}
				}
				if(!flag) {
					ea.setQty( et.getQty());
				}
				tmp.add(ea);
			}
			
		}
			return tmp;
	}
	public void updateState(IRequest iRequest,EcrItemResult dto) {
		  ecrItemResultMapper.updateState(dto);
	}
	/*
	 * 物料管控保存数据自动判断 如果物料清单下所有的库存状态都是已完成 则进行计算， 否则保存数据
	 */ 
	public ResponseData saveItemControl(IRequest iRequest,List<EcrMaterialV0> dtos) {
		ResponseData responseData=new ResponseData();
		String ecrno="";		
		List<String> itemIds=new ArrayList();
		//判断是否需要进行自动运算
		boolean isall=false;
		//2020年4月1日17:44:12 update by 数据 
		int days=0;
		if(dtos.size()>0) {

			EcrInfluencedmaterial ecrInfluencedmaterial=new EcrInfluencedmaterial();
			ecrInfluencedmaterial.setKid(Long.valueOf(dtos.get(0).getKid()));
			ecrInfluencedmaterial=ecrInfluencedmaterialMapper.selectByPrimaryKey(ecrInfluencedmaterial);
			ecrno=ecrInfluencedmaterial.getEcrno();
			
			EcrInfluencedmaterial ecrInfluencedmaterials=new EcrInfluencedmaterial();
			ecrInfluencedmaterials.setEcrno(ecrno);
			ecrInfluencedmaterials.setOnhandStatus("A");
			int ct =ecrInfluencedmaterialMapper.selectCount(ecrInfluencedmaterials);
			if(ct==dtos.size()) {
				isall=true;
			}
		}
		for(EcrMaterialV0 ecrMaterialV0:dtos) {
			 
			EcrInfluencedmaterial ecrInfluencedmaterial=new EcrInfluencedmaterial();
			ecrInfluencedmaterial.setKid(Long.valueOf(ecrMaterialV0.getKid()));
			ecrInfluencedmaterial=ecrInfluencedmaterialMapper.selectByPrimaryKey(ecrInfluencedmaterial);
			ecrInfluencedmaterial.setOnhandStatus(ecrMaterialV0.getOnhandStatus());
			
			if(ecrInfluencedmaterial.getBuyerCycle()!=null&&Integer.valueOf( ecrInfluencedmaterial.getBuyerCycle())>days) {
				days=Integer.valueOf( ecrInfluencedmaterial.getBuyerCycle());
			}
			itemIds.add(ecrInfluencedmaterial.getMaterialId().toString());
			self().updateByPrimaryKey(iRequest, ecrInfluencedmaterial);
		}
		//自动计算的逻辑 目前还未提供出来如果全部成功 就在这里编写自动计算的逻辑
		
		
		if(isall) {
			//获取ecr申请预测完成时间
			EcrMain ecrMain=new  EcrMain();
			ecrMain.setEcrno(ecrno);
			List<EcrMain> ecrMains=ecrMainMapper.select(ecrMain);
			Date planFinishedDate=new Date();
			if(ecrMains.size()>0) {
				//获取预测完成时间
				Calendar cl=Calendar.getInstance();
				cl.setTime(ecrMains.get(0).getPlanFinishedDate());
				cl.add(Calendar.DAY_OF_YEAR,days );
				planFinishedDate=ecrMains.get(0).getPlanFinishedDate();				
			}
			//获取APS计划所需物料数量和完工时间					
			List<EcrApsV0> ecrApsV0s=self().apsTmpData(itemIds);
			List<EcrItemResultTmpV0> ecrItemResultTmpV0s=new ArrayList<>();
			Map<Long, List<EcrApsV0>> tmp=ecrApsV0s.stream().collect(Collectors.groupingBy(EcrApsV0::getItemId));		
			float planQty=0;
			for(List<EcrApsV0> entry:tmp.values()) {
				planQty=0;
				//获取供应商库存数量
				Float onhandQty= getOnhands(dtos,entry.get(0).getItemId()); 
				for(EcrApsV0 ecrApsV0:entry) {
					planQty+=ecrApsV0.getQty();
					//每个月都记录一下数据 按照物料维度记录
					EcrItemResultTmpV0 ecrItemResultTmpV0=new EcrItemResultTmpV0();					 
						onhandQty=onhandQty-  ecrApsV0.getQty();
						ecrItemResultTmpV0.setQty(onhandQty);
						ecrItemResultTmpV0.setMonth(ecrApsV0.getDemandDate());
						ecrItemResultTmpV0.setItemId(entry.get(0).getItemId());
						ecrItemResultTmpV0s.add(ecrItemResultTmpV0);
					 
				}
			}
			Long groupId=Long.valueOf(1);
			//临时数据记录后 要按照每个物料维护进行方案分配
			Map<Long, List<EcrItemResultTmpV0>>tmp1=	ecrItemResultTmpV0s.stream().sorted(Comparator.comparing(EcrItemResultTmpV0::getMonth)).collect(Collectors.groupingBy(EcrItemResultTmpV0::getItemId));	
			//出两套方案 已物料为主会有补充和报废两种区分两种 故需要两个方案
			EcrItemResult ecrItemResultPlan =new EcrItemResult ();
			for(List<EcrItemResultTmpV0> entry:tmp1.values()) {
				planQty=0;
				EcrItemResult ecrItemResult =new EcrItemResult ();
				EcrItemResult ecrItemResult1 =new EcrItemResult ();
				ecrItemResult.setGroupId(groupId);
				ecrItemResult1.setGroupId(groupId+1);
				SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM");
				try {				
					//遍历所有差值 获取两个方案 一个为需要补齐方案 另一个为报废方案 
					for(EcrItemResultTmpV0 ecrItemResultTmpV0:entry) {
						//中间根据建议完成时间生成对应方案 如果月份和当前月份保持一致 记录一笔方案  暂定此方案为最佳方案
						if(dateFormat.format(planFinishedDate).equals(ecrItemResultTmpV0.getMonth())) {
							ecrItemResultPlan.setQty(Float.valueOf(ecrItemResultTmpV0.getQty()));
							ecrItemResultPlan.setAdviceChageTime(dateFormat.parse( ecrItemResultTmpV0.getMonth()));
							ecrItemResultPlan.setScrap(ecrItemResultTmpV0.getQty()*-1);
							ecrItemResultPlan.setEcrno(ecrno);
							ecrItemResultPlan.setItemId(ecrItemResultTmpV0.getItemId());
							ecrItemResultPlan.setStatust("B");
							//稍后补充
							ecrItemResultPlan.setCycleDays((long)days);	
							ecrItemResultPlan.setGroupId(Long.valueOf("-1"));
							//直接插入数据
							ecrItemResultService.insert(iRequest, ecrItemResultPlan);
						}
						if(Float.valueOf(ecrItemResultTmpV0.getQty())>0) {
							ecrItemResult.setQty(Float.valueOf(ecrItemResultTmpV0.getQty()));
							ecrItemResult.setAdviceChageTime(dateFormat.parse( ecrItemResultTmpV0.getMonth()));
							ecrItemResult.setScrap(Float.valueOf(ecrItemResultTmpV0.getQty()*-1));
							ecrItemResult.setEcrno(ecrno);
							ecrItemResult.setItemId(ecrItemResultTmpV0.getItemId());
							ecrItemResult.setGroupId(groupId);
							ecrItemResult.setStatust("B");
							//稍后补充
							ecrItemResult.setCycleDays((long)days);						
						}
						else if(Float.valueOf(ecrItemResultTmpV0.getQty())<0) {
							ecrItemResult1.setQty(Float.valueOf(ecrItemResultTmpV0.getQty()));
							ecrItemResult1.setAdviceChageTime(dateFormat.parse( ecrItemResultTmpV0.getMonth()	));
							ecrItemResult1.setScrap(Float.valueOf(ecrItemResultTmpV0.getQty()*-1));
							ecrItemResult1.setEcrno(ecrno);
							ecrItemResult1.setItemId(ecrItemResultTmpV0.getItemId());
							ecrItemResult1.setGroupId(groupId+1);
							ecrItemResult1.setStatust("B");
							//稍后补充
							ecrItemResult.setCycleDays((long)days);						
							break;
						}
					}
				}
				
				catch( Exception ex) {					
				}
				if(ecrItemResult.getEcrno()!=null) {
					ecrItemResultService.insert(iRequest, ecrItemResult);
				}
				if(ecrItemResult1.getEcrno()!=null) {
					ecrItemResultService.insert(iRequest, ecrItemResult1);
				}
				//获取好改物料信息  接下来匹配各个物料数据
				for(List<EcrItemResultTmpV0> entry1:tmp1.values()) {	
					if(entry1.get(0).getItemId().equals(entry.get(0).getItemId())) {
						continue;
					}
					EcrItemResult ecrItemResultSub =new EcrItemResult ();
					EcrItemResult ecrItemResultSub1 =new EcrItemResult ();
					//遍历其他物料的信息 获取月份 获得对应数据
					for(EcrItemResultTmpV0 ecrItemResultTmpV0:entry1) {
						try {
							Date date=dateFormat.parse( ecrItemResultTmpV0.getMonth());
							if(date.equals(ecrItemResult.getAdviceChageTime())) {
								ecrItemResultSub.setQty(Float.valueOf(ecrItemResultTmpV0.getQty()));
								ecrItemResultSub.setAdviceChageTime(dateFormat.parse( ecrItemResultTmpV0.getMonth()));
								ecrItemResultSub.setScrap(Float.valueOf(ecrItemResultTmpV0.getQty()*-1));
								ecrItemResultSub.setEcrno(ecrno);
								ecrItemResultSub.setItemId(ecrItemResultTmpV0.getItemId());
								//稍后补充
								ecrItemResultSub.setCycleDays((long)days);
								ecrItemResultSub.setGroupId(groupId);
								ecrItemResultSub.setStatust("B");
								ecrItemResultService.insert(iRequest, ecrItemResultSub);			
							}
							else if(date.equals(ecrItemResult1.getAdviceChageTime())) {
								ecrItemResultSub1.setQty(Float.valueOf(ecrItemResultTmpV0.getQty()));
								ecrItemResultSub1.setAdviceChageTime(dateFormat.parse( ecrItemResultTmpV0.getMonth()));
								ecrItemResultSub1.setScrap(Float.valueOf(ecrItemResultTmpV0.getQty()*-1));
								ecrItemResultSub1.setEcrno(ecrno);
								ecrItemResultSub1.setItemId(ecrItemResultTmpV0.getItemId());
								//稍后补充
								ecrItemResultSub1.setCycleDays((long)days);
								ecrItemResultSub1.setGroupId(groupId+1);
								ecrItemResultSub1.setStatust("B");
								ecrItemResultService.insert(iRequest, ecrItemResultSub1);	
								
								break;
							}
						}												
						catch( Exception ex) {					
						}
					}
				}
				groupId+=2;
			}		
			
			//计算完毕后 将结果查询出来 进行最佳方案确认
			EcrMain dto=new EcrMain();
			dto.setEcrno(ecrno);
			List<EcrMain> dots=	ecrMainMapper.select(dto);
			if(dots.size()>0) {
				List<EcrMaterialResultV0> ecrMaterialResultV0s=self().getItemResult(iRequest, dto);
				if(ecrMaterialResultV0s.size()>0) {
					Long bestId=Long.valueOf("-1");
					//高风险按照时间最短切换时间最短进行选择  中低风险按照损耗费用最少进行选择
					if(dots.get(0).getRiskdegree().equals("A")) {
						Date tmpDate=ecrMaterialResultV0s.get(0).getAdviceChangeTime();
						for(EcrMaterialResultV0 ct:ecrMaterialResultV0s) {
							if(tmpDate.compareTo(ct.getAdviceChangeTime())>0){
								bestId=ct.getGroupId();
								tmpDate=ct.getAdviceChangeTime();
							}
						}						
					}
					else {
						Float scraps=ecrMaterialResultV0s.get(0).getScrap();
						for(EcrMaterialResultV0 ct:ecrMaterialResultV0s) {
							if(scraps>ct.getScrap()){
								bestId=ct.getGroupId();
								scraps=ct.getScrap();
							}
						}
					}
					//更新这个这个方案是最佳方案
					EcrItemResult bestResult=new EcrItemResult();
					bestResult.setStatust("A");
					bestResult.setEcrno(ecrno);
					bestResult.setGroupId(bestId);
					bestResult.setLastUpdatedBy(iRequest.getUserId());	
					
					self().updateState(iRequest, bestResult);
				}
			}
			//发起工作流
			EcrMain em=new EcrMain();		
			em.setEcrno(ecrno);
			List<EcrMain> ems=ecrMainMapper.select(em);
			em=ems.get(0);
			em.setProcessEmployeeCode(em.getMainDuty());
			ecrProcessService.startMatScrap(iRequest, em);
		}			
	
		responseData.setSuccess(true);
		responseData.setMessage("成功");
		return responseData;
	}
	
	
}