package com.hand.npi.npi_technology.service.impl;

import com.github.pagehelper.PageHelper;
import com.hand.hap.activiti.service.IActivitiService;
import com.hand.hap.core.IRequest;
import com.hand.hap.system.dto.ResponseData;
import com.hand.hap.system.service.impl.BaseServiceImpl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.activiti.rest.service.api.engine.variable.RestVariable;
import org.activiti.rest.service.api.runtime.process.ProcessInstanceCreateRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hand.npi.npi_technology.dto.InvalidCause;
import com.hand.npi.npi_technology.dto.InvalidPattern;
import com.hand.npi.npi_technology.dto.InvalidPatternHis;
import com.hand.npi.npi_technology.dto.PreventionMeasure;
import com.hand.npi.npi_technology.dto.TechnologySpec;
import com.hand.npi.npi_technology.dto.TechnologySpecDetail;
import com.hand.npi.npi_technology.dto.TechnologySpecMatDetail;
import com.hand.npi.npi_technology.mapper.InvalidCauseMapper;
import com.hand.npi.npi_technology.mapper.InvalidPatternHisMapper;
import com.hand.npi.npi_technology.mapper.InvalidPatternMapper;
import com.hand.npi.npi_technology.mapper.PreventionMeasureMapper;
import com.hand.npi.npi_technology.mapper.TechnologySpecMapper;
import com.hand.npi.npi_technology.service.IInvalidCauseService;
import com.hand.npi.npi_technology.service.IInvalidPatternService;
import com.hand.npi.npi_technology.service.IPreventionMeasureService;
import com.hand.npi.npi_technology.utils.GetNextVersion;

import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(rollbackFor = Exception.class)
public class InvalidPatternServiceImpl extends BaseServiceImpl<InvalidPattern> implements IInvalidPatternService {

	@Autowired
	InvalidPatternMapper invalidPatternMapper;
	@Autowired
	InvalidCauseMapper invalidCauseMapper;
	@Autowired
	PreventionMeasureMapper preventionMeasureMapper;

	@Autowired
	IPreventionMeasureService iPreventionMeasureService;

	@Autowired
	IInvalidCauseService iInvalidCauseService;
	
	@Autowired
	InvalidPatternHisMapper invalidPatternHisMapper;
	
	@Autowired
	private IActivitiService activitiService;

	@Override
	public ResponseData addNewInvalidPattern(InvalidPattern dto, IRequest requestCtx, HttpServletRequest request) {
		if ("add".equals(dto.get__status())) {
			Date date = new Date();
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMM");
			String time = dateFormat.format(date);
			String invalidPatternNumber = "FM" + time;
			invalidPatternNumber = getInvalidPatternNumber(invalidPatternNumber);
			dto.setInvalidPatternNumber(invalidPatternNumber);

			insertSelective(requestCtx, dto);
		} else if ("update".equals(dto.get__status())) {
			updateByPrimaryKey(requestCtx, dto);
		}
		return new ResponseData(true);
	}

	public String getInvalidPatternNumber(String invalidPatternNumber) {
		InvalidPattern invalidPattern = new InvalidPattern();
		invalidPattern.setInvalidPatternNumber(invalidPatternNumber);
		List<InvalidPattern> invalidPatternList = invalidPatternMapper.selectMaxNumber(invalidPattern);
		if (invalidPatternList.isEmpty()) {
			invalidPatternNumber = invalidPatternNumber + "0001";
		} else {
			int intNumber = Integer.parseInt(invalidPatternList.get(0).getInvalidPatternNumber().substring(8));
			intNumber++;
			String stringNumber = String.valueOf(intNumber);
			for (int i = 0; i < 3; i++) {
				stringNumber = stringNumber.length() < 4 ? "0" + stringNumber : stringNumber;
			}
			invalidPatternNumber = invalidPatternNumber + stringNumber;
		}

		return invalidPatternNumber;
	}

	@Override
	public void deleteRow(InvalidPattern dto) throws Exception {
		List<InvalidPattern> invalidPatternList = invalidPatternMapper.selectPatternChild(dto);
		for (int i = 0; i < invalidPatternList.size(); i++) {
			if (invalidPatternList.get(i).getPreventionMeasureId() != null) {
				PreventionMeasure preventionMeasure = new PreventionMeasure();
				preventionMeasure.setPreventionId(invalidPatternList.get(i).getPreventionMeasureId());
				iPreventionMeasureService.deleteByPrimaryKey(preventionMeasure);
			}
			if (invalidPatternList.get(i).getInvalidCauseId() != null) {
				InvalidCause invalidCause = new InvalidCause();
				invalidCause.setInvalidCauseId(invalidPatternList.get(i).getInvalidCauseId());
				iInvalidCauseService.deleteByPrimaryKey(invalidCause);
			}
		}
		self().deleteByPrimaryKey(dto);
	}

	@Override
	public ResponseData addInvalidPatterns(List<InvalidPattern> dtos, IRequest requestCtx, HttpServletRequest request) {
		for (InvalidPattern dto : dtos) {
			Date date = new Date();
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMM");
			String time = dateFormat.format(date);
			String invalidPatternNumber = "FM" + time;
			invalidPatternNumber = getInvalidPatternNumber(invalidPatternNumber);
			dto.setInvalidPatternNumber(invalidPatternNumber);
			// 设置版本号
			dto.setPfmeaVersion("V1");
			dto.setApproveStatus("1");
			insertSelective(requestCtx, dto);
			// 开始添加失效原因
			List<InvalidCause> causeList = dto.getCauseList();
			for (InvalidCause invalidCause : causeList) {
				invalidCause.setInvalidPatternId(dto.getInvalidPatternId());
				String invalidCauseNumber = "FC" + time;
				invalidCauseNumber = iInvalidCauseService.getInvalidCauseNumber(invalidCauseNumber);
				invalidCause.setInvalidCauseNumber(invalidCauseNumber);
				invalidCauseMapper.insertSelective(invalidCause);
				List<PreventionMeasure> meaList = invalidCause.getMeaList();
				// 开始添加预防措施
				for (PreventionMeasure measure : meaList) {
					measure.setInvalidCauseId(invalidCause.getInvalidCauseId());
					String preventionMeasureNumber = "PM" + time;
					preventionMeasureNumber = iPreventionMeasureService
							.getPreventionMeasureNumber(preventionMeasureNumber);
					measure.setPreventionMeasureNumber(preventionMeasureNumber);
					preventionMeasureMapper.insertSelective(measure);
				}
			}
		}
		ResponseData responseData = new ResponseData(true);
		responseData.setRows(dtos);
		return responseData;
	}

	@Override
	public List<InvalidPattern> editInvalidPatterns(List<InvalidPattern> list, IRequest requestCtx,
			HttpServletRequest request) {
		Date date = new Date();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMM");
		String time = dateFormat.format(date);
		for (InvalidPattern dto : list) {
			invalidPatternMapper.updateByPrimaryKeySelective(dto);
			
			InvalidCause icd = new InvalidCause();
			icd.setInvalidPatternId(dto.getInvalidPatternId());
			invalidCauseMapper.delete(icd);
			// 开始添加失效原因
			List<InvalidCause> causeList = dto.getCauseList();
			if (causeList == null) {
				continue;
			}
			for (InvalidCause invalidCause : causeList) {
				invalidCause.setInvalidPatternId(dto.getInvalidPatternId());
				String invalidCauseNumber = "FC" + time;
				invalidCauseNumber = iInvalidCauseService.getInvalidCauseNumber(invalidCauseNumber);
				invalidCause.setInvalidCauseNumber(invalidCauseNumber);
				invalidCauseMapper.insertSelective(invalidCause);
				List<PreventionMeasure> meaList = invalidCause.getMeaList();
				
				PreventionMeasure pmd = new PreventionMeasure();
				pmd.setInvalidCauseId(invalidCause.getInvalidCauseId());
				preventionMeasureMapper.delete(pmd);
				// 开始添加预防措施
				if (meaList == null) {
					continue;
				}
				for (PreventionMeasure measure : meaList) {
					measure.setInvalidCauseId(invalidCause.getInvalidCauseId());
					String preventionMeasureNumber = "PM" + time;
					preventionMeasureNumber = iPreventionMeasureService
							.getPreventionMeasureNumber(preventionMeasureNumber);
					measure.setPreventionMeasureNumber(preventionMeasureNumber);
					preventionMeasureMapper.insertSelective(measure);
				}
			}
		}
		return list;
	}

	@Override
	public List<InvalidPattern> queryData(IRequest request, InvalidPattern condition, int pageNum, int pageSize) {
		PageHelper.startPage(pageNum, pageSize);
		return invalidPatternMapper.queryNewVersionData(condition);
	}

	@Override
	public List<InvalidPattern> queryHisData(InvalidPattern dto, int page, int pageSize, IRequest requestCtx,
			HttpServletRequest request) {
		PageHelper.startPage(page, pageSize);
		return invalidPatternMapper.queryHisData(dto);
	}

	@Override
	public void deleteDatas(List<InvalidPattern> dtos) {
		// 删除时级联删除两张行表的数据
		InvalidCause invalidCause=new InvalidCause();
		PreventionMeasure preventionMeasure=new PreventionMeasure();
		for (InvalidPattern dto : dtos) {
			invalidCause.setInvalidPatternId(dto.getInvalidPatternId());
			List<InvalidCause> select = invalidCauseMapper.select(invalidCause);
			for (InvalidCause dtoInv : select) {
				Float id = dtoInv.getInvalidCauseId();
				preventionMeasure.setInvalidCauseId(id);
				preventionMeasureMapper.delete(preventionMeasure);
			}
			invalidCauseMapper.delete(invalidCause);
			//invalidPatternMapper.deleteByPrimaryKey(dto.getId());
			invalidPatternMapper.delete(dto);
		}
	}
	
	/**
	 * @description 执行审批，启动流程
	 * @author likai 2020.03.26
	 * @param dtos
	 * @param requestCtx
	 * @return
	 */
	@Override
	public void audit(List<InvalidPattern> dtos, IRequest requestCtx) {
		for(InvalidPattern dto:dtos) {
			//启动审核流程
			ProcessInstanceCreateRequest instanceCreateRequest = new ProcessInstanceCreateRequest();
			instanceCreateRequest.setBusinessKey(dto.getInvalidPatternNumber());
			instanceCreateRequest.setProcessDefinitionKey("pfmeaAudit");
			
			//更新审批状态
			dto.setApproveStatus("2");
			invalidPatternMapper.updateByPrimaryKeySelective(dto);
			
			List<RestVariable> restVariableList = new ArrayList<RestVariable>();
			RestVariable restVariableStartMan = new RestVariable();
			RestVariable restVariable = new RestVariable();
			
			restVariableStartMan.setName("startCode");
			restVariableStartMan.setValue(requestCtx.getEmployeeCode());
			restVariableList.add(restVariableStartMan);
			
			InvalidPatternHis invalidPatternHis = new InvalidPatternHis();
			invalidPatternHis.setInvalidPatternNumber(dto.getInvalidPatternNumber());
			List<InvalidPatternHis> invalidPatternHisList = invalidPatternHisMapper.select(invalidPatternHis);
			if(invalidPatternHisList != null && invalidPatternHisList.size() > 0) {
				restVariable.setName("workFlowType");
				restVariable.setValue("update");
				restVariableList.add(restVariable);
			} else {
				restVariable.setName("workFlowType");
				restVariable.setValue("add");
				restVariableList.add(restVariable);
			}
			instanceCreateRequest.setVariables(restVariableList);
			activitiService.startProcess(requestCtx, instanceCreateRequest);
		}
	}
	
	/**
	 * @author likai 2020.03.26
	 * @description 发起历史变更 新增或修改
	 * @param request
	 * @param list
	 * @return
	 */
	@Override
	public ResponseData addOrEditData(IRequest request, List<InvalidPattern> list) {
		ResponseData responseData=new ResponseData();
		
		Date date = new Date();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMM");
		String time = dateFormat.format(date);
		for(InvalidPattern dto:list) {
			InvalidPattern invalidPattern = new InvalidPattern();
			invalidPattern.setInvalidPatternNumber(dto.getInvalidPatternNumber());
			List<InvalidPattern> invalidPatternList = invalidPatternMapper.select(invalidPattern);
			String status = "";
			if(invalidPatternList != null && invalidPatternList.size() > 0) {
				status = invalidPatternList.get(0).getApproveStatus();
			}
			
			if("1".equals(status)) {//变更修改
				InvalidPatternHis invalidPatternHis = new InvalidPatternHis();
				invalidPatternHis.setInvalidPatternNumber(dto.getInvalidPatternNumber());
				invalidPatternHis.setPfmeaVersion(dto.getPfmeaVersion());
				List<InvalidPatternHis> listFind = invalidPatternHisMapper.select(invalidPatternHis);
				invalidPatternHis = listFind.get(0);
				invalidPatternHis.setInvalidPatternName(dto.getInvalidPatternName());
				invalidPatternHis.setInvalidPatternConsequence(dto.getInvalidPatternConsequence());
				invalidPatternHis.setSev(dto.getSev());
				invalidPatternHisMapper.updateByPrimaryKeySelective(invalidPatternHis);
				
				InvalidCause icd = new InvalidCause();
				icd.setInvalidPatternId(dto.getInvalidPatternId());
				invalidCauseMapper.delete(icd);
				
				List<InvalidCause> inList = dto.getCauseList();
				if(inList != null) {
					for (int i = inList.size() - 1; i > -1; i --) {
						inList.get(i).setInvalidPatternId(invalidPatternHis.getInvalidPatternId());
						String invalidCauseNumber = "FC" + time;
						invalidCauseNumber = iInvalidCauseService.getInvalidCauseNumber(invalidCauseNumber);
						inList.get(i).setInvalidCauseNumber(invalidCauseNumber);
						invalidCauseMapper.insertSelective(inList.get(i));
						
						PreventionMeasure pmd = new PreventionMeasure();
						pmd.setInvalidCauseId(inList.get(i).getInvalidCauseId());
						preventionMeasureMapper.delete(pmd);
						
						List<PreventionMeasure> pmList = inList.get(i).getMeaList();
						if(pmList != null) {
							for (int j = pmList.size() - 1; j > -1; j --) {
								pmList.get(j).setInvalidPatternId(inList.get(i).getInvalidCauseId());
								String preventionMeasureNumber = "PM" + time;
								preventionMeasureNumber = iPreventionMeasureService
										.getPreventionMeasureNumber(preventionMeasureNumber);
								pmList.get(j).setPreventionMeasureNumber(preventionMeasureNumber);
								preventionMeasureMapper.insertSelective(pmList.get(j));
							}
						}
					}
				}
			} else if("3".equals(status)) {//新增变更
				InvalidPatternHis invalidPatternHis = new InvalidPatternHis();
				
				String invalidPatternNumber = "FM" + time;
				invalidPatternNumber = getInvalidPatternNumber(invalidPatternNumber);
				invalidPatternHis.setInvalidPatternNumber(invalidPatternNumber);
				// 设置版本号
				String versionCode = GetNextVersion.getVersionCode(dto.getPfmeaVersion());
				invalidPatternHis.setPfmeaVersion(versionCode);
				invalidPatternHis.setInvalidPatternName(dto.getInvalidPatternName());
				invalidPatternHis.setInvalidPatternConsequence(dto.getInvalidPatternConsequence());
				invalidPatternHis.setSev(dto.getSev());
				invalidPatternHisMapper.insertSelective(invalidPatternHis);
				
				List<InvalidCause> inList = dto.getCauseList();
				if(inList != null) {
					for (int i = inList.size() - 1; i > -1; i --) {
						inList.get(i).setInvalidPatternId(invalidPatternHis.getInvalidPatternId());
						String invalidCauseNumber = "FC" + time;
						invalidCauseNumber = iInvalidCauseService.getInvalidCauseNumber(invalidCauseNumber);
						inList.get(i).setInvalidCauseNumber(invalidCauseNumber);
						invalidCauseMapper.insertSelective(inList.get(i));
						
						List<PreventionMeasure> pmList = inList.get(i).getMeaList();
						if(pmList != null) {
							for (int j = pmList.size() - 1; j > -1; j --) {
								pmList.get(j).setInvalidPatternId(inList.get(i).getInvalidCauseId());
								String preventionMeasureNumber = "PM" + time;
								preventionMeasureNumber = iPreventionMeasureService
										.getPreventionMeasureNumber(preventionMeasureNumber);
								pmList.get(j).setPreventionMeasureNumber(preventionMeasureNumber);
								preventionMeasureMapper.insertSelective(pmList.get(j));
							}
						}
					}
				}
				InvalidPattern ipUpS = new InvalidPattern();
				ipUpS.setInvalidPatternNumber(dto.getInvalidPatternNumber());
				List<InvalidPattern> listUpS = invalidPatternMapper.select(ipUpS);
				listUpS.get(0).setApproveStatus("1");
				invalidPatternMapper.updateByPrimaryKeySelective(listUpS.get(0));
			}else {
				responseData.setSuccess(false);
				responseData.setMessage("动作要求数据找不到！");
			}
		}
		
		responseData.setRows(list);
		return responseData;
	}
}