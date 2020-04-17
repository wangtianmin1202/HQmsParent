package com.hand.spc.ecr_main.service.impl;

import com.github.pagehelper.PageHelper;
import com.hand.hap.core.IRequest;
import com.hand.hap.system.dto.CodeValue;
import com.hand.hap.system.service.ICodeService;
import com.hand.hap.system.service.impl.BaseServiceImpl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hand.spc.ecr_main.dto.EcrMain;
import com.hand.spc.ecr_main.dto.EcrPci;
import com.hand.spc.ecr_main.dto.EcrProjectTracking;
import com.hand.spc.ecr_main.dto.EcrQtp;
import com.hand.spc.ecr_main.dto.EcrSolutionSku;
import com.hand.spc.ecr_main.dto.EcrSolutionSkuRelation;
import com.hand.spc.ecr_main.dto.EcrTechnicalFileHeader;
import com.hand.spc.ecr_main.dto.EcrVtp;
import com.hand.spc.ecr_main.mapper.EcrMainMapper;
import com.hand.spc.ecr_main.mapper.EcrPciMapper;
import com.hand.spc.ecr_main.mapper.EcrProjectTrackingMapper;
import com.hand.spc.ecr_main.mapper.EcrQtpMapper;
import com.hand.spc.ecr_main.mapper.EcrSolutionSkuMapper;
import com.hand.spc.ecr_main.mapper.EcrSolutionSkuRelationMapper;
import com.hand.spc.ecr_main.mapper.EcrTechnicalFileHeaderMapper;
import com.hand.spc.ecr_main.mapper.EcrVtpMapper;
import com.hand.spc.ecr_main.service.IEcrProjectTrackingService;

import liquibase.integration.commandline.Main;

import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(rollbackFor = Exception.class)
public class EcrProjectTrackingServiceImpl extends BaseServiceImpl<EcrProjectTracking> implements IEcrProjectTrackingService{

	@Autowired
	private EcrProjectTrackingMapper mapper;
	
	@Autowired
	private EcrMainMapper mainMapper;
	
	@Autowired
	private ICodeService codeService; 
	
	@Autowired
	private EcrTechnicalFileHeaderMapper fileMapper;
	
	@Autowired
	private EcrQtpMapper qtpMapper; 
	
	@Autowired
	private EcrVtpMapper vtpMapper; 
	
	@Autowired
	private EcrPciMapper pciMapper; 
	
	@Autowired
	private EcrSolutionSkuMapper ecrSolutionSkuMapper;
	
	@Autowired
	private EcrSolutionSkuRelationMapper ecrSolutionSkuRelationMapper;
	

	Logger logger = LoggerFactory.getLogger(EcrProjectTrackingServiceImpl.class);
	
	@Override
	public List<EcrProjectTracking> selectGrid(IRequest requestContext, EcrProjectTracking dto, int page,
			int pageSize) {
		PageHelper.startPage(page, pageSize);
		return mapper.selectGrid(dto);
	}
	
	@Override
	public List<EcrProjectTracking> selectInfo(IRequest request, String ecrno) {
		logger.debug("------------------ECR项目跟踪查询 start-----------------------");
		if(!"".equals(ecrno) && null != ecrno) {
			List<EcrProjectTracking> list = mapper.selectInfo(ecrno);
			logger.debug("------------------ECR项目跟踪查询 end-----------------------");
			return list;
		}
		logger.debug("ecrno 编码为空");
		logger.debug("------------------ECR项目跟踪查询 end-----------------------");
		return new ArrayList<>();
	}

	@Override
	public List<EcrProjectTracking> insertInfo(IRequest request, String ecrno) {
		logger.debug("------------------ECR项目跟踪新增 start-----------------------");
		logger.debug("ecrno: " + ecrno);
		// 值：任务序号，含义：任务名称，描述：系数，标记：标准人天
		String codeName = "HPM_ERC_TRACKING";
		List<CodeValue> codes = codeService.getCodeValuesByCode(request, codeName);
		Map<Integer, EcrProjectTracking> map = new HashMap<>();
		// 值：任务序号，含义：任务名称，描述：系数，标记：标准人天
		codes.forEach(cv->{
			EcrProjectTracking dto = new EcrProjectTracking();
			dto.setEcrno(ecrno);
			dto.setTaskNumber(cv.getValue());
			dto.setTaskName(cv.getMeaning());
			dto.setStatus("new");
			dto.setTaskStandardDays(Float.valueOf(cv.getTag()));
			dto.setRatio(Integer.valueOf(cv.getDescription()));
			if("1".equals(cv.getValue())) {
				dto.setDueDate(new Date());
				mapper.insertSelective(dto);
			}
			logger.debug("codeName:" + codeName);
			logger.debug("value：" + cv.getValue() 
						+ ",meaning：" + cv.getMeaning()
						+ ",标记：" + cv.getTag() 
						+ ",描述：" + cv.getDescription());
			map.put(Integer.valueOf(cv.getValue()), dto);
		});
		// 计算要求完成日期
		for(int i=2; i<=codes.size(); i++) {
			// 上一个任务
			EcrProjectTracking ept1 = map.get(i-1);
			// 本任务
			EcrProjectTracking dto = map.get(i); 

			// 标准人天的系数
			Integer ratio = 1;
			if(dto.getRatio() != 0) {
				ratio = selectRatio(ecrno, i);
			}
			
			// 计算要求完成时间
			Calendar calendar= Calendar.getInstance();
			calendar.setTime(ept1.getDueDate());
			calendar.add(Calendar.DAY_OF_YEAR, (int) Math.ceil((dto.getTaskStandardDays()*ratio)));
			dto.setDueDate(calendar.getTime());
			logger.debug("任务编号：" + dto.getTaskNumber() 
						+ ",任务名称：" + dto.getTaskName() 
						+ ",任务标准人天：" + dto.getTaskStandardDays() 
						+ ",系数：" + ratio 
						+",要求完成时间：" + dto.getDueDate());
			mapper.insertSelective(dto);
		}
		logger.debug("------------------ECR项目跟踪新增 end-----------------------");
		return new ArrayList<>();
	}

	/**
	 * TODO 标准人天的系数
	 * @param ecrno
	 * @param i
	 * @return
	 */
	private Integer selectRatio(String ecrno, int i) {
		int ratio = 1;
		switch(i) {
			case 8:
				EcrTechnicalFileHeader etf = new EcrTechnicalFileHeader();
				etf.setEcrno(ecrno);
				List<EcrTechnicalFileHeader> etfs = fileMapper.select(etf);
				if(etfs != null) {
					ratio = etfs.size();
				}
				break;
			case 9:
				EcrQtp qtp = new EcrQtp();
				qtp.setEcrno(ecrno);
				List<EcrQtp> qtps = qtpMapper.select(qtp);
				if(qtps != null) {
					ratio = qtps.size();
				}
				break;
			case 10:  // VTP dto
				EcrVtp vtp = new EcrVtp();
				vtp.setEcrno(ecrno);
				List<EcrVtp> vtps = vtpMapper.select(vtp);
				if(vtps != null) {
					ratio = vtps.size();
				}
				break;
			case 11:  // PCI dto
				EcrPci pci = new EcrPci();
				pci.setEcrno(ecrno);
				List<EcrPci> pcis = pciMapper.select(pci);
				if(pcis != null) {
					ratio = pcis.size();
				}
				break;
			case 12:  // ECR生效发放
				break;
			case 13:  // 物料报废
				break;
			default:break;
		}
		logger.debug("查询到系数：" + ratio);
		return ratio;
	}

	@Override
	public List<EcrProjectTracking> selectInfluence(IRequest request, String skuId) {
		List<EcrProjectTracking> epts = mapper.selectInfluence(skuId);
		epts.forEach(dto ->{
			Double actPrimeCost = divide(dto.getActCost(),dto.getActPriceUnit());
			Double stdPrimeCost = divide(dto.getStdCost(),dto.getStdPriceUnit());
			dto.setActPrimeCost(actPrimeCost);
			dto.setStdPrimeCost(stdPrimeCost);
		});
		return epts;
	}

	/**
	 * -计算除法
	 * @param actCost
	 * @param actPriceUnit
	 * @return
	 */
	private Double divide(Double d1, Double d2) {
		if(d1==null || d1==0 || d2==null || d2==0) {
			return 0D;
		} else {
			BigDecimal co = new BigDecimal(d1);
			BigDecimal pu = new BigDecimal(d2);
			return co.divide(pu).doubleValue();
		}
	}
	
	/**
	 * -计算除法
	 * @param actCost
	 * @param actPriceUnit
	 * @return
	 */
	private Double divide(Double cost, Long priceUnit) {
		return divide(cost, Double.valueOf(priceUnit));
	}

	
	/**
	 * -计算乘法
	 * @param actCost
	 * @param actPriceUnit
	 * @return
	 */
	private Double multi(Double d1, Double d2) {
		if(d1==null || d1==0 || d2==null || d2==0) {
			return 0D;
		} else {
			BigDecimal co = new BigDecimal(d1);
			BigDecimal pu = new BigDecimal(d2);
			return co.multiply(pu).doubleValue();
		}
	}
	
	/**
	 * -计算乘法
	 * @param actCost
	 * @param actPriceUnit
	 * @return
	 */
	private Double multi(Double d1, Double d2, Double d3) {
		return multi(multi(d1,d2),d3);
	}
	
	@Override
	public void calculateActualCost(IRequest requestContext, String ecrno, String skuId) {
		// 1. 前一次的实际价格(查询是否存在前一次实际价格)
		//-- 1.1 查找到数据：取数据 ACT_COST 作为标准价格。
		Double stdCost = mapper.selectLastActualPrice(ecrno, skuId);
		if(stdCost == null) {
			//-- 1.2 未查找到数据：以下查询标准价格
			List<EcrProjectTracking> list = mapper.selectStdPrice(ecrno, skuId);
			if(list.isEmpty()) {
				logger.debug("============前一次实际价格不存在================");
				return;
			}
			EcrProjectTracking ept = list.get(0);
			stdCost = multi(divide(ept.getUnitPrice(), ept.getPriceUnit()),ept.getQuantity());
		}
		// 2. SKU物料成本变化 
		List<EcrProjectTracking> changes = mapper.selectCostChange(ecrno, skuId);
		if(changes.isEmpty()) {
			logger.debug("============SKU物料成本变化 不存在================");
			return;
		}
		Double changeCost = 0D;
		for(EcrProjectTracking dto:changes) {
			logger.info(dto.getItemPrice().toString());
			logger.info(dto.getItemQty().toString());
			logger.info(dto.getEcrUse().toString());
			logger.info(dto.getBomUse().toString());
			logger.info(dto.getYearPlanQty().toString());
			changeCost += multi(divide(dto.getItemPrice(),dto.getItemQty())
					,(dto.getEcrUse()-dto.getBomUse())
					, dto.getYearPlanQty());
		}
		// 3. SKU工时变化
		List<EcrProjectTracking> workTimes = mapper.selectWorkTimeChange(ecrno, skuId);
		if(workTimes.isEmpty()) {
			logger.debug("============SKU工时变化================");
			return;
		}
		Double workTime = workTimes.get(0).getWorkCost();
		// 4. stdPriceUnit
		Double stdPriceUnit = mapper.selectStdPriceUnit(ecrno, skuId);
		// 计算SKU实际成本 = 前一次的实际价格 + （SKU物料成本变化 + SKU工时变化) * stdPriceUnit
		Double actCost= stdCost + multi((changeCost + workTime),stdPriceUnit);
		// 保存
		mapper.updateCostItem(ecrno, skuId, actCost);
		mapper.updateCostSolution(ecrno, skuId, actCost);
	}

	@Override
	public Boolean qtp(IRequest request,String ecrno) {
		// FIXME　查询要求完成时间 dueDate,参数：ecrno,taskName="QTP"
		final Date askFinishedDate = selectDueDate(ecrno, "QTP");
		
		int ct =  mapper.selectFileItemId(ecrno);
		//其中三个任务全部完成  进行qtp生成
		if(ct==3) {
			List<String> itemIds=new ArrayList<>();
			EcrSolutionSku ess=new EcrSolutionSku();
			ess.setEcrno(ecrno);
			List<EcrSolutionSku> essList= ecrSolutionSkuMapper.select(ess);
			for(EcrSolutionSku es:essList) {
				itemIds.add(es.getSkuId().toString());
				EcrSolutionSkuRelation essr=new EcrSolutionSkuRelation();
				List<EcrSolutionSkuRelation> essrs=ecrSolutionSkuRelationMapper.select(essr);
				for(EcrSolutionSkuRelation er:essrs) {
					if(!itemIds.contains(er.getItemEcrId().toString())) {
						itemIds.add(er.getItemEcrId().toString());
					}
				}
			}			
			
			itemIds.forEach(itemId ->{
				List<EcrProjectTracking> list = mapper.selectSku(itemId);
				if(list.isEmpty()) {
					list = mapper.selectItem(itemId);
				}
				list.forEach(dto ->{
					Long qtpSeq = mapper.selectQtpSeq(dto.getEcrno(), dto.getItemId());
					if(qtpSeq == null) {
						qtpSeq = 1L;
					} else {
						qtpSeq++;
					}
					logger.info("============================qtpSeq:");
					logger.info(qtpSeq.toString());
					
					// 保存 QTP 数据
					EcrQtp qtp = new EcrQtp();
					qtp.setQtpSeq(qtpSeq);
					qtp.setEcrno(ecrno);
					qtp.setItemId(Long.valueOf(dto.getItemId()));
					qtp.setActFinishedDate(askFinishedDate);
					qtpMapper.insertSelective(qtp);
					
					// FIXME 更新负责人
					qtpMainDuty(request, ecrno, itemId);
				});
			});
			return true;
		}
		return false;
	}

	@Override
	public void qtpMainDuty(IRequest request, String ecrno, String itemId) {
		List<String> skuCodes = mapper.selectSkuCode(itemId);
		String mainDuty = null;
		// 判断 itemId 是否为 skuId
		List<EcrProjectTracking> list = mapper.selectSku(itemId);
		if(!list.isEmpty()) {
			// 查询品类
			String categroy = mapper.selectCategory(skuCodes.get(0));
			//品类代码值
			String codeName = "HPM_ECR_QTP_CATOGRY";
			List<CodeValue> codes = codeService.getCodeValuesByCode(request, codeName);
			for(CodeValue cv : codes){
				if(cv.getValue().equals(categroy)) {
					mainDuty = cv.getMeaning();
					break;
				}
			}
		}
		// 查询物料负责人
		if(mainDuty == null) {
			mainDuty = mapper.selectMainDuty(ecrno);
		}
		// 更新 QTP 负责人
		Long qtpSeq = mapper.selectQtpSeq(ecrno, itemId);
		if(qtpSeq == null) {
			qtpSeq = 1L;
		}
		mapper.updateQtpDutyby(ecrno,itemId,qtpSeq,mainDuty);
	}

	
	@Override
	public Boolean vtp(IRequest request,String ecrno) {
		//　查询要求完成时间 dueDate，参数：ecrno,taskName="VTP"
		final Date askFinishedDate = selectDueDate(ecrno, "VTP");
		
		Boolean update = false;
		String[] arr = {"PFMEA","控制计划","工装夹具","SIP","SOP"};
		List<String> fileTypes = Arrays.asList(arr);
		String s = mapper.selectFilesItemId(ecrno, fileTypes);
		logger.debug(s);
		int ct =  Integer.valueOf(s);
		//其中五个任务全部完成  进行vtp生成
		if(ct==5) {
			// 根据 ecrno 查询出所有的 skuId 和 skuCode
			EcrSolutionSku ess=new EcrSolutionSku();
			ess.setEcrno(ecrno);
			List<EcrSolutionSku> essList= ecrSolutionSkuMapper.select(ess);
			for(EcrSolutionSku es:essList) {
				// itemId
				String itemId = String.valueOf(es.getSkuId());
				// itemCode
				String itemCode = es.getSkuCode();
				// 查询 QTP 表状态为完成时，将数据保存到 VTP
				String qtpStatus = mapper.selectQtpStatus(ecrno, itemId);
				if("FINISHED".equals(qtpStatus)) {
					// 查询 VTP次序
					Long vtpSeq = mapper.selectVtpSeq(ecrno, itemCode);
					if(vtpSeq == null) {
						vtpSeq = 1L;
					} else {
						vtpSeq++;
					}
					//　保存到 VTP
					EcrVtp vtp = new EcrVtp();
					vtp.setEcrno(ecrno);
					vtp.setSkuCode(itemCode);
					vtp.setVtpSeq(vtpSeq);
					vtp.setFinishedDate(askFinishedDate);
					vtpMapper.insertSelective(vtp);
					// mapper.insertVtp(ecrno, itemCode, vtpSeq);
					// 更新负责人
					vtpMainDuty(request, ecrno, itemCode);
					update = true;
				}
			}	
		}
		return update;
	}

	@Override
	public void vtpMainDuty(IRequest request, String ecrno, String itemCode) {
		// 查询责任分配表
		List<String> list = mapper.selectVtpMainDuty(itemCode);
		if(list.isEmpty()) {
			logger.debug("=================未查询到 VTP 负责人=================");
			return;
		}
		String mainDuty = list.get(0);
		// 更新 VTP 负责人
		Long vtpSeq = mapper.selectVtpSeq(ecrno, itemCode);
		if(vtpSeq == null) {
			vtpSeq = 1L;
		}
		mapper.updateVtpDutyby(ecrno,itemCode,vtpSeq,mainDuty);
	}

	@Override
	public Boolean pci(IRequest requestContext, String ecrno) {
		// FIXME　查询要求完成时间 dueDate,参数：ecrno,taskName="PCI"
		final Date askFinishedDate = selectDueDate(ecrno, "PCI");
		
		// 查询初始化数据
		EcrMain ecr = new EcrMain();
		ecr.setEcrno(ecrno);
		List<EcrMain> list = mainMapper.select(ecr);
		if(list.isEmpty()) {
			return false;
		}
		// 保存到 PCI
		list.forEach(dto ->{
			EcrPci pci = new EcrPci();
			pci.setEcrno(ecrno);
			List<EcrPci> pcis = pciMapper.select(pci);
			Long pciSeq = 1L;
			for(EcrPci seq: pcis) {
				pciSeq = seq.getPciSeq() > pciSeq?seq.getPciSeq():pciSeq;
			}
			pci.setDutyby(dto.getMainDuty());
			pci.setPciSeq(pciSeq);
			pci.setAskFinishedDate(askFinishedDate);
			pciMapper.insert(pci);
		});
		return true;
	}

	/**
	 * 查询项目跟踪的要求完成时间
	 * @param ecrno
	 * @param type
	 * @return
	 */
	@Override
	public Date selectDueDate(String ecrno, String type) {
		Date dueDate = null;
		EcrProjectTracking ept = new EcrProjectTracking();
		ept.setEcrno(ecrno);
		ept.setTaskName(type);
		List<EcrProjectTracking> epts = mapper.select(ept);
		if(!epts.isEmpty() && epts.get(0) != null) {
			dueDate = epts.get(0).getDueDate();
		}
		return dueDate;
	}


}