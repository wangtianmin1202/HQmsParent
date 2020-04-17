package com.hand.hqm.hqm_msa_check_plan.service.impl;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.github.pagehelper.PageHelper;
import com.hand.hap.core.IRequest;
import com.hand.hap.system.dto.ResponseData;
import com.hand.hap.system.service.impl.BaseServiceImpl;
import com.hand.hap.util.SystemApiMethod;
import com.hand.hqm.hqm_measure_tool.dto.MeasureTool;
import com.hand.hqm.hqm_measure_tool.mapper.MeasureToolMapper;
import com.hand.hqm.hqm_measure_tool.service.IMeasureToolService;
import com.hand.hqm.hqm_measure_tool_his.dto.MeasureToolHis;
import com.hand.hqm.hqm_measure_tool_his.mapper.MeasureToolHisMapper;
import com.hand.hqm.hqm_measure_tool_his.service.IMeasureToolHisService;
import com.hand.hqm.hqm_measure_tool_msa.dto.MeasureToolMsa;
import com.hand.hqm.hqm_measure_tool_msa.service.IMeasureToolMsaService;
import com.hand.hqm.hqm_measure_tool_msa_his.dto.MeasureToolMsaHis;
import com.hand.hqm.hqm_measure_tool_msa_his.service.IMeasureToolMsaHisService;
import com.hand.hqm.hqm_msa_check_plan.dto.MetCheckPlanLine;
import com.hand.hqm.hqm_msa_check_plan.mapper.MetCheckPlanLineMapper;
import com.hand.hqm.hqm_msa_check_plan.service.IMetCheckPlanLineService;
import com.hand.utils.ObjectUtils.CopyObject;

@Service
@Transactional(rollbackFor = Exception.class)
public class MetCheckPlanLineServiceImpl extends BaseServiceImpl<MetCheckPlanLine> implements IMetCheckPlanLineService {

	@Autowired
	private MetCheckPlanLineMapper metCheckPlanLineMapper;
	
	@Autowired
    private IMeasureToolService measureService;
	
	@Autowired
	private MeasureToolMapper mapper;
	
	@Autowired
	private MeasureToolHisMapper hisMapper;
	
	@Autowired
	private IMeasureToolHisService measureToolHisService;
	
	@Autowired
	private IMeasureToolMsaService measureToolMsaService;
	
	@Autowired
	private IMeasureToolMsaHisService measureToolMsaHisService;

	@Override
	public List<MetCheckPlanLine> selectCheckPlanLine(MetCheckPlanLine metCheckPlanLine) {
		return metCheckPlanLineMapper.selectCheckPlanLine(metCheckPlanLine);
	}

	@Override
	public ResponseData fileUpload(IRequest requestCtx, HttpServletRequest request) throws IllegalStateException, IOException {
		// TODO 文件上传
		Float checkPlanLineId = (float) Float.valueOf(request.getParameter("checkPlanLineId")).intValue();
		ResponseData responseData = new ResponseData();
		List<String> rows = new ArrayList<String>();

		MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;

		// 获取文件map集合
		Map<String, MultipartFile> fileMap = multipartRequest.getFileMap();
		String rootPath = "/apps/hap/resource";
		String endPath = "/demension/met/checkplanline/file/";
		String path = rootPath + endPath;
		if (SystemApiMethod.getOsType().equals("window")) {
			rootPath = "C:/apps/hap/resource";
			endPath = "/demension/met/checkplanline/file/";
			path = rootPath + endPath;
		}
		File dir = new File(path);
		if (!dir.exists()) {
			dir.mkdirs();
		}
		for (Map.Entry<String, MultipartFile> entry : fileMap.entrySet()) {
			rows.add(entry.getValue().getOriginalFilename());
			MultipartFile forModel = entry.getValue();
			File file = new File(path, forModel.getOriginalFilename());
			MetCheckPlanLine metCheckPlanLine = new MetCheckPlanLine();
			metCheckPlanLine.setCheckPlanLineId(checkPlanLineId);
			List<MetCheckPlanLine> resultFileList = metCheckPlanLineMapper.select(metCheckPlanLine);	
			//此处执行逻辑为新上传的文件覆盖掉旧文件
			// 删除已存在并执行更改
			File haveFile = new File(rootPath + resultFileList.get(0).getAttachmentAddress());
			if (haveFile.exists()) {
				file.delete();
			}
			forModel.transferTo(file);
			// 执行更新操作
			MetCheckPlanLine update = new MetCheckPlanLine();
			update.setCheckPlanLineId(resultFileList.get(0).getCheckPlanLineId());
			update.setAttachmentAddress(endPath + entry.getValue().getOriginalFilename());
			metCheckPlanLineMapper.updateByPrimaryKeySelective(update);
			responseData.setMessage(entry.getValue().getOriginalFilename());
			break;
		}
		return responseData;
	}

	@Override
	public ResponseData update(List<MetCheckPlanLine> dto, IRequest requestCtx) {
		//List<MeasureTool> measureTools = new ArrayList<MeasureTool>();
		for (int i = 0; i < dto.size(); i++) {
			if ("NG".equals(dto.get(i).getCheckResult())) {// 检验结果不合格
				if ("".equals(dto.get(i).getDisqualificationReason()) || dto.get(i).getDisqualificationReason() == null) {
					ResponseData responseData = new ResponseData(false);
					responseData.setMessage("请填写不合格原因!");
					return responseData;
				}
				/*if ("".equals(dto.get(i).getAttachmentAddress()) || dto.get(i).getAttachmentAddress() == null) {
					ResponseData responseData = new ResponseData(false);
					responseData.setMessage("请上传不合格单!");
					return responseData;
				}*/
			}
			
			MeasureTool measureTool = new MeasureTool();
			MeasureTool measureTool2 = new MeasureTool();
			MeasureTool measureToolhis = new MeasureTool();
			measureTool.setMeasureToolId(dto.get(i).getMeasuretoolId());
			measureTool.setCheckResult(dto.get(i).getCheckResult());
			measureTool.setLastCheckDate(dto.get(i).getCheckDate());
			if("NG".equals(dto.get(i).getCheckResult())){
				measureTool.setMeasureToolStatus("4");
			}
			measureTool2 = mapper.selectByPrimaryKey(measureTool);
			Calendar cal = getCalendar(measureTool2.getCheckCycle(), dto.get(i).getCheckDate());
			measureTool.setNextCheckDate(cal.getTime());//更新下次校验日期
			measureTool.set__status("update");
			//measureTools.add(measureTool);
			measureService.updateByPrimaryKeySelective(requestCtx, measureTool);
			
			//记录量具台账历史
			measureToolhis = mapper.selectByPrimaryKey(measureTool);
			measureToolhis.setHisType("6");
			saveMeasureToolHis(requestCtx,measureToolhis);//记历史
		}
		
		//measureService.batchUpdate(requestCtx, measureTools);
		
		return new ResponseData(self().batchUpdate(requestCtx, dto));
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
	public void updateData(IRequest requestContext, MetCheckPlanLine dto) {
		self().updateByPrimaryKeySelective(requestContext, dto);
		MeasureToolHis measureToolHis = new MeasureToolHis();
		MetCheckPlanLine metCheckPlanLine = self().selectByPrimaryKey(requestContext, dto);
		
		measureToolHis.setRemark(dto.getDisqualificationReason());
		measureToolHis.setMeasureToolId(metCheckPlanLine.getMeasuretoolId());
		MeasureTool measureTool = mapper.selectByPrimaryKey(metCheckPlanLine.getMeasuretoolId());
		CopyObject.copyByName(measureTool, measureToolHis);
		measureToolHis.setRemark(dto.getDisqualificationReason());
		measureToolHis.setDescriptions(dto.getMeasuretoolName());
		measureToolHis.setLastCheckDate(dto.getCheckDate());
		measureToolHis.setCheckResult(dto.getCheckResult());
		measureToolHis.setHisType("6");
		String checkedBy = measureTool.getCheckedBy().toString();
		measureToolHis.setCheckedBy(checkedBy.substring(0,checkedBy.indexOf(".")));
//		String measuretoolType = "卡尺".equals(dto.getMeasuretoolName()) ? "1" : "天平".equals(dto.getMeasuretoolName()) ? "2" : "3";
//		measureToolHis.setMeasureToolType(measuretoolType);
		hisMapper.insertSelective(measureToolHis);
	}

	@Override
	public List<MetCheckPlanLine> query(IRequest requestContext, MetCheckPlanLine metCheckPlanLine, int page,
			int pageSize) {
		PageHelper.startPage(page, pageSize);
		return metCheckPlanLineMapper.query(metCheckPlanLine);
	}

}