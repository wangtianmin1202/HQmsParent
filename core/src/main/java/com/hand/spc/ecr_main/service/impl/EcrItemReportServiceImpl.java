package com.hand.spc.ecr_main.service.impl;

import com.github.pagehelper.PageHelper;
import com.hand.hap.core.IRequest;
import com.hand.hap.system.service.impl.BaseServiceImpl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hand.spc.ecr_main.dto.EcrDetail;
import com.hand.spc.ecr_main.dto.EcrInfluencedmaterial;
import com.hand.spc.ecr_main.dto.EcrItemReport;
import com.hand.spc.ecr_main.dto.EcrItemReportDetail;
import com.hand.spc.ecr_main.dto.EcrItemResult;
import com.hand.spc.ecr_main.dto.EcrItemSku;
import com.hand.spc.ecr_main.dto.EcrMain;
import com.hand.spc.ecr_main.mapper.EcrDetailMapper;
import com.hand.spc.ecr_main.mapper.EcrInfluencedmaterialMapper;
import com.hand.spc.ecr_main.mapper.EcrItemReportDetailMapper;
import com.hand.spc.ecr_main.mapper.EcrItemReportMapper;
import com.hand.spc.ecr_main.mapper.EcrItemResultMapper;
import com.hand.spc.ecr_main.mapper.EcrSolutionSkuMapper;
import com.hand.spc.ecr_main.service.IEcrDetailService;
import com.hand.spc.ecr_main.service.IEcrInfluencedmaterialService;
import com.hand.spc.ecr_main.service.IEcrItemReportDetailService;
import com.hand.spc.ecr_main.service.IEcrItemReportService;
import com.hand.spc.ecr_main.view.EcrItemReportDetailV0;
import com.hand.spc.ecr_main.view.EcrItemReportDetailV1;
import com.hand.spc.ecr_main.view.EcrItemReportV0;
import com.hand.spc.ecr_main.view.EcrItemReportV1;
import com.hand.spc.ecr_main.view.EcrItemReportV2;
import com.hand.spc.ecr_main.view.EcrItemReportV3;

import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(rollbackFor = Exception.class)
public class EcrItemReportServiceImpl extends BaseServiceImpl<EcrItemReport> implements IEcrItemReportService{
	
	@Autowired
	private EcrItemReportMapper ecrItemReportMapper;
	
	@Autowired
	private EcrItemReportDetailMapper ecrItemReportDetailMapper;	

	@Autowired
	private IEcrItemReportDetailService ecrItemReportDetailService;	
	
	@Autowired
	private EcrInfluencedmaterialMapper ecrInfluencedmaterialMapper;
	
	@Autowired
	private EcrDetailMapper ecrDetailMapper;
	
	@Autowired
	private IEcrDetailService ecrDetailService;
	
	@Autowired
	private IEcrInfluencedmaterialService ecrInfluencedmaterialService;
	
	@Autowired
	private EcrItemResultMapper ecrItemResultMapper;
	
	@Autowired
	private EcrSolutionSkuMapper ecrSolutionSkuMapper;
	
	public List<EcrItemReportV0> reportQuery(EcrItemReport dto,int page,int pageSize){
		PageHelper.startPage(page, pageSize);
		List<EcrItemReportV0> eirvs=ecrItemReportMapper.getReportItems(dto.getEcrno());
		for(EcrItemReportV0 eir:eirvs ) {
			EcrItemReport ecrItemReport=new EcrItemReport();
			ecrItemReport.setEcrno(dto.getEcrno());
			ecrItemReport.setItemId(eir.getItemId());
			List<EcrItemReport> eirs=ecrItemReportMapper.select(ecrItemReport);
			List<EcrItemReportV1> eirvs1=new ArrayList();
			for(EcrItemReport ei:eirs) {	
				EcrItemReportV1 ev=new EcrItemReportV1();
				Float a=Float.valueOf("0");
				if(ei.getMesOnhand()!=null) {
				a+=ei.getMesOnhand();
				}
				if(ei.getWmsOnhand()!=null) {
					a+=ei.getWmsOnhand();
					}
				if(ei.getPoQty()!=null) {
					a+=ei.getPoQty();
					}
				if(ei.getSupplierOnhand()!=null) {
					a+=ei.getSupplierOnhand();
					}
				if(ei.getSpecialOnhand()!=null) {
					a+=ei.getSpecialOnhand();
					}
				if(ei.getCalculateOnhand()!=null) {
					a+=ei.getCalculateOnhand();
					} 
				ev.setOnhandQty(a);
				ev.setSumQty(ei.getPlanQty());
				if(ei.getPlanQty()-a>0) {
					ev.setBuyerQty(ei.getPlanQty()-a );
				}
				else {
						ev.setBuyerQty(Float.valueOf("0"));
					}
				ev.setPlanDate(ei.getTaskDate());
				ev.setReportId(ei.getReportId() );
				eirvs1.add(ev);
			}
			eir.setEcrItemReportV1s(eirvs1);
		}
		return eirvs;
	}
	public List<EcrItemReportDetailV0> reportDetailQuery(EcrItemReport dto,int page,int pageSize){		
		List<EcrItemReportDetailV0> eirds=new ArrayList();
		EcrItemReportDetail erd=new EcrItemReportDetail();
		erd.setReportId(dto.getReportId());
		List<EcrItemReportDetail> erds=ecrItemReportDetailMapper.select(erd);
		Map<String, List<EcrItemReportDetail>> tmp= erds.stream().sorted(Comparator.comparing(EcrItemReportDetail::getMonth)).collect(Collectors.groupingBy(EcrItemReportDetail::getSkuCode));
		for(List<EcrItemReportDetail> entry:tmp.values()) {
			EcrItemReportDetailV0 eidv=new EcrItemReportDetailV0();
			eidv.setSkuCode(entry.get(0).getSkuCode());
			eidv.setSkuId(entry.get(0).getSkuId().toString());
			eidv.setBomUse(entry.get(0).getBomUse().toString());
			List<EcrItemReportDetailV1> eirdv1s=new ArrayList();
			for(EcrItemReportDetail ed:entry) {
				EcrItemReportDetailV1 eirdv1=new EcrItemReportDetailV1();
				eirdv1.setLeftQty(ed.getLeftQty().toString());
				eirdv1.setCompleteQty(ed.getCompleteQty().toString());
				eirdv1.setDemandQty(ed.getDemandQty().toString());
				eirdv1.setMonth(ed.getMonth());
				eirdv1s.add(eirdv1);
			}
			eidv.setEcrItemReportDetailV1s(eirdv1s);
			eirds.add(eidv);
		} 
		return eirds;
	}
	
	public   EcrItemReportV2 getEcrOnhand(String supplierId,String itemId) {
		EcrItemReportV2 ecrItemReportV2=ecrItemReportMapper.getSupplierQty(itemId, supplierId,"AA");
		ecrItemReportV2.setPoQty(ecrItemReportMapper.getPoOnlineQty(itemId, supplierId));
		EcrItemReportV2 ecrItemReport=ecrItemReportMapper.getBomQtys(itemId, supplierId,"AA");
		ecrItemReportV2.setCalculateOnhand(ecrItemReport.getCalculateOnhand());
		ecrItemReportV2.setSpecialOnhand(ecrItemReport.getSpecialOnhand());
		return ecrItemReportV2;		
	}
	public void checkEcrProcess(IRequest iRequest, String ecrno) {
		/*1。先获取所有受影响料号
		 *2.获取所有供应商  计算5个供应商库存
		 *3.根据每个受影响料号找到关联的SKU 计算他们的BOM用量
		 *4.遍历每个SKU获取SKU的生产量 完工数量 记录明细（按月分组）
		 *5.反写总量给头表
		 * */
		EcrItemReport eir=new EcrItemReport();
		EcrInfluencedmaterial efm=new EcrInfluencedmaterial();
		efm.setEcrno(ecrno);
		List<EcrInfluencedmaterial> efms= ecrInfluencedmaterialMapper.select(efm);
		//遍历所有物料 进行物料--供应商库存数据查询
		for(EcrInfluencedmaterial ef:efms) {
			EcrDetail ecrDetail=new EcrDetail();
			ecrDetail.setEcrItemKid(ef.getKid());
			List<EcrDetail> eds= ecrDetailMapper.select(ecrDetail);
			Float poQty=Float.valueOf("0");
			Float calculateOnhand=Float.valueOf("0");
			Float supplierOnhand=Float.valueOf("0");
			Float supplierWipOnhand=Float.valueOf("0");
			Float specialOnhand=Float.valueOf("0");
			for(EcrDetail ed:eds) {
				//获取每个供应商下物料的五个库存数量
				EcrItemReportV2 ecrItemReportV2=self().getEcrOnhand(ed.getSupplierId(), ef.getMaterialId().toString());
				if(ecrItemReportV2.getCalculateOnhand()!=null) {
					ed.setCalculateOnhand(ecrItemReportV2.getCalculateOnhand());
					calculateOnhand+=ecrItemReportV2.getCalculateOnhand();
				}
				if(ecrItemReportV2.getPoQty()!=null) {
					ed.setPoQty(ecrItemReportV2.getPoQty());
					poQty+=ecrItemReportV2.getPoQty();
				}
				if(ecrItemReportV2.getSupplierOnhand()!=null) {
					ed.setSupplierOnhand(ecrItemReportV2.getSupplierOnhand());
					supplierOnhand+=ecrItemReportV2.getSupplierOnhand();
				}
				if(ecrItemReportV2.getSpecialOnhand()!=null) {
					ed.setSpecialOnhand(ecrItemReportV2.getSpecialOnhand());
					specialOnhand+=ecrItemReportV2.getCalculateOnhand();
				}
				if(ecrItemReportV2.getSupplierWipOnhand()!=null) {
					ed.setSpecialWipOnhand(ecrItemReportV2.getSupplierWipOnhand());
					supplierWipOnhand+=ecrItemReportV2.getSupplierWipOnhand();
				}
				eir.setBuyer(ed.getBuyer());
				//更新数据
				ecrDetailService.updateByPrimaryKey(iRequest, ed);
			}
			//将求和的数据回写到物料属性和对应的报表数据上
			ef.setPoQty(poQty.toString());
			ef.setCalculateOnhand(calculateOnhand.toString());
			ef.setSpecialOnhand(specialOnhand.toString());
			ef.setSupplierOnhand(supplierOnhand.toString());
			ef.setSpecialWipOnhand(supplierWipOnhand);
			ecrInfluencedmaterialService.updateByPrimaryKey(iRequest, ef);
			
			eir.setPoQty(poQty);
			eir.setCalculateOnhand(calculateOnhand);
			eir.setSpecialOnhand(specialOnhand);
			eir.setSupplierOnhand(supplierOnhand);
			eir.setEcrno(ecrno);
			eir.setItemId(ef.getMaterialId().toString());
			eir.setSpecialWipOnhand(supplierWipOnhand);
			//插入数据
			eir=self().insert(iRequest, eir);
			//获取物料总需求  即到切换时间的预测数量
			//获取计划切换时间
			EcrItemResult ecrItemResult =new EcrItemResult();
			ecrItemResult.setEcrno(ecrno);
			ecrItemResult.setStatust("A");			
			List<EcrItemResult> ecrItemResults=   ecrItemResultMapper.select(ecrItemResult);
			if(ecrItemResults.size()>0){
				Date date=ecrItemResults.get(0).getAdviceChageTime();				
				//获取当前物料受影响料号的SKU
				List<EcrItemReportDetail> eirds=ecrItemReportMapper.getBomSkuItems(ecrno, ef.getMaterialId().toString()); 	
				//遍历所有SKU 计算当前比例
				//声明一个总数量 最后回写到头表中
				Float sumQty=Float.valueOf(0);
				for(EcrItemReportDetail detail:eirds) {
					//获取每个月的预测需求数据进行行明细数据
					List<EcrItemReportV3> eir3s= ecrItemReportMapper.getDemandQty(ef.getMaterialId().toString(), date, "AA");
					//获取当月完工数据
					//获取当月完工数
					Float completeQty=ecrSolutionSkuMapper.getCompletedMonthQty(ef.getMaterialId().toString(), "AA");
					//获取当月到切换时间的预测数量
					for(EcrItemReportV3 eir3:eir3s) {
						EcrItemReportDetail eird=new EcrItemReportDetail();
						BigDecimal result=new BigDecimal(completeQty.toString());
						Float a=eir3.getDemandQty().subtract(result).floatValue();
						eird.setDemandQty(eir3.getDemandQty().floatValue());
						eird.setCompleteQty(completeQty);
						eird.setLeftQty(a);
						eird.setSkuCode(detail.getSkuCode());
						eird.setSkuId(detail.getSkuId());
						completeQty=Float.valueOf(0);
						eird.setBomUse(detail.getBomUse());
						eird.setReportId(eir.getReportId());
						Calendar cl=Calendar.getInstance();
						cl.setTime(eir3.getDemandDate());
						int month=cl.MONTH;
						eird.setMonth(String.valueOf(cl.YEAR)+"-"+String.valueOf(month+1)+"月");
						
						ecrItemReportDetailService.insert(iRequest, eird);						
					}
					//计算总需求数量
					sumQty+=eir3s.stream().map(x -> new BigDecimal(x.getDemandQty().toString())).reduce(BigDecimal.ZERO,BigDecimal::add).floatValue();
					
				}
				 Float supOnhand= poQty+calculateOnhand+specialOnhand+supplierOnhand;		
				 eir.setPlanQty(sumQty);
				 eir.setBuyerQty(sumQty-supOnhand);
				 eir.setTaskDate(new Date());
				//总需求数量-库存数量=需采购数量
				 eir.setObjectVersionNumber(null);
				self().updateByPrimaryKey(iRequest, eir);
				 																	
			}
		}
	}
}