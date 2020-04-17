package com.hand.hqm.hqm_measure_tool.service.impl;

import com.github.pagehelper.PageHelper;
import com.hand.hap.core.IRequest;
import com.hand.hap.system.dto.CodeValue;
import com.hand.hap.system.mapper.CodeValueMapper;
import com.hand.hap.system.service.impl.BaseServiceImpl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import org.liquibase.groovy.liquibase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hand.hqm.hqm_measure_tool.controllers.enums.MeasureToolEnums;
import com.hand.hqm.hqm_measure_tool.dto.MeasureTool;
import com.hand.hqm.hqm_measure_tool.dto.MeasureToolVO;
import com.hand.hqm.hqm_measure_tool.mapper.MeasureToolMapper;
import com.hand.hqm.hqm_measure_tool.service.IMeasureToolService;
import com.hand.hqm.hqm_measure_tool_his.dto.MeasureToolHis;
import com.hand.hqm.hqm_measure_tool_his.service.IMeasureToolHisService;
import com.hand.hqm.hqm_measure_tool_msa.dto.MeasureToolMsa;
import com.hand.hqm.hqm_measure_tool_msa.service.IMeasureToolMsaService;
import com.hand.hqm.hqm_measure_tool_msa_his.dto.MeasureToolMsaHis;
import com.hand.hqm.hqm_measure_tool_msa_his.service.IMeasureToolMsaHisService;
import com.hand.hqm.hqm_msa_plan.dto.MsaPlan;
import com.hand.hqm.hqm_msa_plan.mapper.MsaPlanMapper;
import com.hand.hqm.hqm_msa_plan.service.IMsaPlanService;
import com.hand.hqm.hqm_msa_plan_line.dto.MsaPlanLine;
import com.hand.hqm.hqm_msa_plan_line.service.IMsaPlanLineService;
import com.hand.utils.ObjectUtils.CopyObject;
import com.thoughtworks.xstream.mapper.Mapper.Null;

import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(rollbackFor = Exception.class)
public class MeasureToolServiceImpl extends BaseServiceImpl<MeasureTool> implements IMeasureToolService {

	@Autowired
	private MeasureToolMapper mapper;
	@Autowired
	private IMeasureToolMsaService measureToolMsaService;
	@Autowired
	private IMeasureToolMsaHisService measureToolMsaHisService;
	@Autowired
	private IMeasureToolHisService measureToolHisService;
	@Autowired
	private MsaPlanMapper msaPlanMapper;
	@Autowired
	private IMsaPlanService msaPlanService;
	@Autowired
	private IMsaPlanLineService msaPlanLineService;
	@Autowired
	private CodeValueMapper codeValueMapper;

	@Override
	public List<MeasureTool> query(IRequest requestContext, MeasureTool measureTool, int page, int pageSize) {
		PageHelper.startPage(page, pageSize);
		return mapper.query(measureTool);
	}

	@Override
	public void saveData(IRequest requestContext, MeasureTool measureTool) {
		MeasureTool meatool = new MeasureTool();
		meatool.setMeasureToolNum(measureTool.getMeasureToolNum());
		meatool = mapper.selectOne(meatool);
		if (meatool != null) {
			throw new RuntimeException("设备编号:" + measureTool.getMeasureToolNum() + "已存在");
		}
		//List<String> msaContentList = measureTool.getMsaContentList();

		Calendar cal = getCalendar(measureTool.getCheckCycle(), new Date());
		measureTool.setNextCheckDate(cal.getTime());

		if ("Y".equals(measureTool.getMsaFlag())) {
			Calendar calMsa = getCalendar(measureTool.getMsaCycle(), new Date());
			measureTool.setNextMsaDate(cal.getTime());
		}
		// 新增头
		measureTool = self().insertSelective(requestContext, measureTool);
//		Float msaPlanId = null;
//		if ("Y".equals(measureTool.getMsaFlag())) {
//			// 创建MSA分析计划
//			List<CodeValue> codeValueTime = codeValueMapper.selectCodeValuesByCodeName("HQM_MSA_TIME");
//			if (codeValueTime != null && codeValueTime.size() > 0) {
//				// 获取分析人
//				MeasureTool tool = new MeasureTool();
//				tool.setMeasureToolId(measureTool.getMeasureToolId());
//				List<MeasureTool> list = mapper.MeasureToolLov(tool);
//
//				if (list != null && list.size() == 1 && list.get(0).getAnalystId() != null) {
//					// 创建MSA分析计划头
//					MsaPlan msaPlan = createMsaPlan(measureTool, codeValueTime.get(0).getValue(),
//							list.get(0).getAnalystId());
//					msaPlanId = msaPlan.getMsaPlanId();
//				} else{
//					throw new RuntimeException("创建MSA分析计划时，分析人获取失败，请先维护关键岗位与分析人关系");
//				}
//			}
//		}
//		if (msaContentList != null && msaContentList.size() > 0) {
//			for (String msaContent : msaContentList) {
//				msaContent = msaContent.replaceAll("\"", "");
//				MeasureToolMsa measureToolMsa = new MeasureToolMsa();
//				measureToolMsa.setMeasureToolId(measureTool.getMeasureToolId());
//				measureToolMsa.setMsaContent(msaContent);
//				// 新增行
//				measureToolMsaService.insertSelective(requestContext, measureToolMsa);
//				if (msaPlanId != null) {
//					// 创建MSA分析计划行
//					createMsaPlanLine(msaContent, msaPlanId);
//				}
//			}
//		}
	}

	@Override
	public List<MeasureTool> returnData(IRequest requestContext, List<MeasureTool> dto) {

		for (MeasureTool measureTool : dto) {
			measureTool.setMeasureToolPositionStatus("I");
			measureTool.setReturnDate(new Date());
			// 更新
			mapper.updatemeasureToolPositionStatus(measureTool);

			MeasureTool sureTool = new MeasureTool();
			sureTool.setMeasureToolId(measureTool.getMeasureToolId());
			sureTool = mapper.selectByPrimaryKey(sureTool);
			sureTool.setHisType("1");
			// 记历史
			saveMeasureToolHis(requestContext, sureTool);
		}

		return dto;
	}

	@Override
	public List<MeasureTool> scrap(IRequest requestContext, List<MeasureTool> dto) {
		String scrapReason = dto.get(0).getScrapReason();
		for (MeasureTool measureTool : dto) {
			measureTool.setMeasureToolStatus("3");
			measureTool.setScrapReason(scrapReason);
			measureTool.setHisType("4");
			measureTool = self().updateByPrimaryKeySelective(requestContext, measureTool);
			// 记历史
			saveMeasureToolHis(requestContext, measureTool);
		}
		return dto;
	}

	@Override
	public List<MeasureTool> checkResult(IRequest requestContext, List<MeasureTool> dto) {
		String checkResult = dto.get(0).getCheckResult();
		for (MeasureTool measureTool : dto) {

			String start = measureTool.getCheckCycle().substring(0, measureTool.getCheckCycle().length() - 1);
			String end = measureTool.getCheckCycle().substring(measureTool.getCheckCycle().length() - 1);

			Calendar cal = Calendar.getInstance();
			cal.setTime(new Date());
			if ("M".equals(end)) {
				cal.add(Calendar.MONTH, Integer.parseInt(start.trim()));
			} else if ("Y".equals(end)) {
				cal.add(Calendar.YEAR, Integer.parseInt(start.trim()));
			}

			measureTool.setNextCheckDate(cal.getTime());
			measureTool.setLastCheckDate(new Date());
			measureTool.setCheckResult(checkResult);
			measureTool = self().updateByPrimaryKeySelective(requestContext, measureTool);
			// 记历史
			saveMeasureToolHis(requestContext, measureTool);
		}
		return dto;
	}

	@Override
	public List<MeasureTool> batchSave(IRequest requestContext, List<MeasureTool> dto) {
		List<MeasureTool> measureToolList = self().batchUpdate(requestContext, dto);
		for (MeasureTool measureTool : measureToolList) {
			// 记历史
			saveMeasureToolHis(requestContext, measureTool);
		}
		return measureToolList;
	}

	/**
	 * 记历史
	 * 
	 * @param requestContext
	 * @param measureTool
	 * @return
	 */
	private MeasureToolHis saveMeasureToolHis(IRequest requestContext, MeasureTool measureTool) {
		String hisType = measureTool.getHisType();
		// 记头历史
		MeasureToolHis measureToolHis = new MeasureToolHis();
		measureTool = mapper.selectByPrimaryKey(measureTool);
		CopyObject.copyByName(measureTool, measureToolHis);
		measureToolHis.setHisType(hisType);
		measureToolHis = measureToolHisService.insertSelective(requestContext, measureToolHis);

		MeasureToolMsa measureToolMsa = new MeasureToolMsa();
		measureToolMsa.setMeasureToolId(measureTool.getMeasureToolId());
		List<MeasureToolMsa> measureToolMasList = measureToolMsaService.select(requestContext, measureToolMsa, 0, 0);
		if (measureToolMasList != null && measureToolMasList.size() > 0) {
			for (MeasureToolMsa sureToolMsa : measureToolMasList) {
				// 行历史
				MeasureToolMsaHis measureToolMsaHis = new MeasureToolMsaHis();
				CopyObject.copyByName(sureToolMsa, measureToolMsaHis);
				measureToolMsaHis.setMeasureToolHisId(measureToolHis.getMeasureToolHisId());
				measureToolMsaHisService.insertSelective(requestContext, measureToolMsaHis);
			}
		}
		return measureToolHis;
	}

	@Override
	public List<MeasureTool> jobSelect(MeasureTool measureTool) {
		// TODO Auto-generated method stub
		return mapper.jobSelect(measureTool);
	}

	@Override
	public List<MeasureTool> MeasureToolLov(MeasureTool measureTool) {
		// TODO Auto-generated method stub
		return mapper.MeasureToolLov(measureTool);
	}

	/**
	 * 生成计划头信息
	 * 
	 * @param tool
	 * @param value
	 * @param analystId
	 * @return
	 */
	private MsaPlan createMsaPlan(MeasureTool tool, String value, Float analystId) {
		Calendar calendar = new GregorianCalendar();
		calendar.setTime(new Date());
		calendar.add(Calendar.DAY_OF_MONTH, Integer.parseInt(value)); // 把日期往后增加一年，整数往后推，负数往前移
		Date estimatedFinishTime = calendar.getTime();

		// 生成流水号
		int num = msaPlanMapper.queryMaxNum();
		num++;
		// 序列号
		String numStr = String.format("%05d", num);
		SimpleDateFormat sdf = new SimpleDateFormat("yyMMdd");
		String planNum = "M" + sdf.format(new Date()) + numStr;

		// 生成计划头信息
		MsaPlan msaPlan = new MsaPlan();
		msaPlan.setMsaPlanNum(planNum);
		msaPlan.setMsaType(tool.getMsaType());
		msaPlan.setMeasureToolNum(tool.getMeasureToolNum());
		msaPlan.setMeasurePlanStatus("1");
		msaPlan.setEstimatedFinishTime(estimatedFinishTime);
		msaPlan.setPlantId(tool.getPlantId());
		msaPlan.setAnalyzedBy(analystId);
		msaPlan = msaPlanService.insertSelective(null, msaPlan);

		return msaPlan;
	}

	/**
	 * 生成计划行信息
	 * 
	 * @param toolMsa
	 * @param msaPlanId
	 */
	private void createMsaPlanLine(String msaContent, Float msaPlanId) {
		MsaPlanLine msaPlanLine = new MsaPlanLine();
		msaPlanLine.setMsaContent(msaContent);
		msaPlanLine.setMeasurePlanId(msaPlanId);
		msaPlanLineService.insertSelective(null, msaPlanLine);
	}

	private Calendar getCalendar(String cycle, Date date) {
		String start = cycle.substring(0, cycle.length() - 1);
		String end = cycle.substring(cycle.length() - 1);

		Calendar cal = Calendar.getInstance();
		cal.setTime(date);

		if ("M".equals(end)) {
			cal.add(Calendar.MONTH, Integer.parseInt(start.trim()));
		} else if ("Y".equals(end)) {
			cal.add(Calendar.YEAR, Integer.parseInt(start.trim()));
		}
		return cal;
	}

	@Override
	public List<MeasureTool> queryUnit(MeasureTool measureTool) {
		// TODO Auto-generated method stub
		return mapper.queryUnit(measureTool);
	}
	@Override
	public List<MeasureTool> statisticsSelect(IRequest requestContext, MeasureTool dto, int page, int pageSize) {
		PageHelper.startPage(page, pageSize);
		return mapper.statisticsSelect(dto);
	}
	@Override
	public void forUse(IRequest requestContext,List<MeasureTool> dto) {
		// TODO Auto-generated method stub
		Float principal = dto.get(0).getPrincipal();
		Float useDepartment = dto.get(0).getUseDepartment();
		Float borrowedSupplier = dto.get(0).getBorrowedSupplier();
		for(MeasureTool measureTool : dto) {			
			measureTool = mapper.selectByPrimaryKey(measureTool);
			if(measureTool != null) {
				measureTool.setPrincipal(principal);
				measureTool.setUseDepartment(useDepartment);
				measureTool.setBorrowedSupplier(borrowedSupplier);
				if(measureTool.getFirstUseDate() == null) {
					measureTool.setFirstUseDate(new Date());
				}
				measureTool.setOutboundDate(new Date());
				
				if (useDepartment != null ) {
					measureTool.setMeasureToolPositionStatus("O");
				} else if (borrowedSupplier != null) {
					measureTool.setMeasureToolPositionStatus("2");
				}
				measureTool = self().updateByPrimaryKeySelective(requestContext, measureTool);
				measureTool.setHisType("2");
				//记历史
				saveMeasureToolHis(requestContext,measureTool);
			}
		}
	}

	@Override
	public void repair(IRequest requestContext, MeasureTool dto) {
		// TODO Auto-generated method stub
		dto.setMeasureToolStatus("4");
		self().updateByPrimaryKeySelective(requestContext, dto);
		dto.setHisType("5");
		//记历史
		saveMeasureToolHis(requestContext,dto);
	}

	@Override
	public void updateData(IRequest requestContext, MeasureTool measureTool) {
		List<String> msaContentList = new ArrayList();
		if(measureTool.getMsaContentList()!=null && measureTool.getMsaContentList().size()!=0) {			
			msaContentList = measureTool.getMsaContentList();
		}

//		Calendar cal = getCalendar(measureTool.getCheckCycle(), new Date());
//		measureTool.setNextCheckDate(cal.getTime());
//
//		if ("Y".equals(measureTool.getMsaFlag())) {
//			Calendar calMsa = getCalendar(measureTool.getMsaCycle(), new Date());
//			measureTool.setNextMsaDate(cal.getTime());
//		}
		// 更新头
		measureTool = self().updateByPrimaryKey(requestContext, measureTool);
		MeasureToolMsa toolMsa = new MeasureToolMsa();
		toolMsa.setMeasureToolId(measureTool.getMeasureToolId());
		List<MeasureToolMsa> measureToolMsaList = measureToolMsaService.select(requestContext, toolMsa, 0, 0);
			
		List<String> contentList = new ArrayList(msaContentList);
		List<MeasureToolMsa> toolMsaList = new ArrayList(measureToolMsaList);
		if (measureToolMsaList != null && measureToolMsaList.size() > 0) {
			for(MeasureToolMsa msa : measureToolMsaList) {
				for(String msaContent : msaContentList) {
					String msaContentStr = msaContent.replaceAll("\"", "");
					if(msaContentStr.equals(msa.getMsaContent())) {
						contentList.remove(msaContent);
						toolMsaList.remove(msa);
					}
				}
			}
		}
		
		if (contentList != null ) {
			for (String msaContent : contentList) {
				msaContent = msaContent.replaceAll("\"", "");
				MeasureToolMsa measureToolMsa = new MeasureToolMsa();
				measureToolMsa.setMeasureToolId(measureTool.getMeasureToolId());
				measureToolMsa.setMsaContent(msaContent);
				// 新增行
				measureToolMsaService.insertSelective(requestContext, measureToolMsa);
			}
		}
		if (toolMsaList.size() > 0) {
			for(MeasureToolMsa msa : toolMsaList) {
				measureToolMsaService.deleteByPrimaryKey(msa);
			}
		}
		//记历史
		measureTool = mapper.selectByPrimaryKey(measureTool);
		measureTool.setHisType("7");
		saveMeasureToolHis(requestContext,measureTool);
	}

	@Override
	public List<MeasureTool> metCheckPlanJob(MeasureTool measureTool) {
		// TODO Auto-generated method stub
		return mapper.metCheckPlanJob(measureTool);
	}

	@Override
	public void saveMsa(IRequest requestContext, MeasureTool measureTool) {
		// TODO Auto-generated method stub
		measureTool.setMeasureToolStatus("2");
		measureTool.setMsaStatus("1");
		List<String> msaContentList = measureTool.getMsaContentList();
		
		measureTool = self().updateByPrimaryKeySelective(requestContext, measureTool);
		
		measureTool = mapper.selectByPrimaryKey(measureTool);
		
		Float msaPlanId = null;
		if ("Y".equals(measureTool.getMsaFlag())) {
			// 创建MSA分析计划
			List<CodeValue> codeValueTime = codeValueMapper.selectCodeValuesByCodeName("HQM_MSA_TIME");
			if (codeValueTime != null && codeValueTime.size() > 0) {
				// 获取分析人
				MeasureTool tool = new MeasureTool();
				tool.setMeasureToolId(measureTool.getMeasureToolId());
				List<MeasureTool> list = mapper.MeasureToolLov(tool);

				if (list != null && list.size() == 1 && list.get(0).getAnalystId() != null) {
					// 创建MSA分析计划头
					MsaPlan msaPlan = createMsaPlan(measureTool, codeValueTime.get(0).getValue(),
							list.get(0).getAnalystId());
					msaPlanId = msaPlan.getMsaPlanId();
				} else{
					throw new RuntimeException("创建MSA分析计划时，分析人获取失败，请先维护关键岗位与分析人关系");
				}
			}
		}
		if (msaContentList != null && msaContentList.size() > 0) {
			for (String msaContent : msaContentList) {
				msaContent = msaContent.replaceAll("\"", "");
				MeasureToolMsa measureToolMsa = new MeasureToolMsa();
				measureToolMsa.setMeasureToolId(measureTool.getMeasureToolId());
				measureToolMsa.setMsaContent(msaContent);
				// 新增行
				measureToolMsaService.insertSelective(requestContext, measureToolMsa);
				if (msaPlanId != null) {
					// 创建MSA分析计划行
					createMsaPlanLine(msaContent, msaPlanId);
				}
			}
		}
		
		measureTool.setHisType("8");
		//记历史
		saveMeasureToolHis(requestContext,measureTool);
	}

	@Override
	public List<MeasureTool> queryByCode(IRequest requestContext, MeasureTool measureTool, int page, int pageSize) {
		// TODO Auto-generated method stub
		PageHelper.startPage(page, pageSize);
		List<MeasureTool> list = new ArrayList<>();
		switch (MeasureToolEnums.getByValue(measureTool.getDataFlag())) {
			//领用
			case NECKBAND :
				list = mapper.queryNeckband(measureTool);
				break;
			//报废
			case SCRAP :
				list = mapper.queryScrap(measureTool);
				break;
			//领用
			case MAINTAIN :
				list = mapper.queryMaintain(measureTool);
		}
		return list;
	}

	@Override
	public List<MeasureToolVO> queryReport(IRequest requestContext, MeasureTool measureTool) {
		// TODO Auto-generated method stub
		return mapper.queryReport(measureTool);
	}

	@Override
	public List<MeasureTool> queryByToolType(IRequest requestContext, MeasureTool measureTool, int page, int pageSize) {
		// TODO Auto-generated method stub
		PageHelper.startPage(page, pageSize);
		return mapper.queryByToolType(measureTool);
	}
	@Override
	public List<MeasureTool> queryMsaGridReport(IRequest requestContext, MeasureTool measureTool, int page, int pageSize) {
		// TODO Auto-generated method stub
		PageHelper.startPage(page, pageSize);
		return mapper.queryMsaGridReport(measureTool);
	}

	@Override
	public List<MeasureToolVO> queryMsaReport(IRequest requestContext, MeasureTool measureTool) {
		// TODO Auto-generated method stub
		return mapper.queryMsaReport(measureTool);
	}
}