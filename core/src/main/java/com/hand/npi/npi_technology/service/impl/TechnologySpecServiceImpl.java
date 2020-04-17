package com.hand.npi.npi_technology.service.impl;

import com.github.pagehelper.PageHelper;
import com.google.common.base.Strings;
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

import com.hand.npi.npi_technology.dto.TechnologySparePartDetails;
import com.hand.npi.npi_technology.dto.TechnologySpec;
import com.hand.npi.npi_technology.dto.TechnologySpecDetail;
import com.hand.npi.npi_technology.dto.TechnologySpecHis;
import com.hand.npi.npi_technology.dto.TechnologySpecMatDetail;
import com.hand.npi.npi_technology.mapper.TechnologySparePartDetailsMapper;
import com.hand.npi.npi_technology.mapper.TechnologySpecDetailMapper;
import com.hand.npi.npi_technology.mapper.TechnologySpecHisMapper;
import com.hand.npi.npi_technology.mapper.TechnologySpecMapper;
import com.hand.npi.npi_technology.mapper.TechnologySpecMatDetailMapper;
import com.hand.npi.npi_technology.service.ITechnologySpecService;
import com.hand.npi.npi_technology.utils.GetNextVersion;
import com.hand.wfl.util.DropDownListDto;

import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(rollbackFor = Exception.class)
public class TechnologySpecServiceImpl extends BaseServiceImpl<TechnologySpec> implements ITechnologySpecService{
	
	@Autowired
	TechnologySpecMapper technologySpecMapper;
	@Autowired
	TechnologySpecMatDetailMapper technologySpecMatDetailMapper;
	@Autowired
	TechnologySpecDetailMapper technologySpecDetailMapper;
	@Autowired
	TechnologySpecHisMapper technologySpecHisMapper;
	@Autowired
	private IActivitiService activitiService;
	@Autowired
	TechnologySparePartDetailsMapper technologySparePartDetailsMapper;

	@Override
	public ResponseData addNewTechnologySpec(TechnologySpec dto, IRequest requestCtx, HttpServletRequest request) {
		if ("add".equals(dto.get__status())) {
			/*if(StringUtils.isNotEmpty(dto.getSpecRequirement())){
				dto.getSpecRequirement().replace("\n", "###");
			}*/
			
			Date date = new Date();
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMM");
			String time = dateFormat.format(date);
			String specNumber = "P-SPEC-" + time;
			specNumber = getSpecNumber(specNumber);
			/*dto.setSpecNumber(specNumber);*/
			
			insertSelective(requestCtx, dto);
		} else if ("update".equals(dto.get__status())) {
			updateByPrimaryKey(requestCtx, dto);
		}
		return new ResponseData(true);
	}
	
	public String getSpecNumber(String specNumber) {
		TechnologySpec  technologySpec = new TechnologySpec();
/*		technologySpec.setSpecNumber(specNumber);
*/		List<TechnologySpec> technologySpecList = technologySpecMapper.selectMaxNumber(technologySpec);
		if(technologySpecList.isEmpty()) {
			specNumber = specNumber +"0001";
		}else {
			/*int intNumber = Integer.parseInt(technologySpecList.get(0).getSpecNumber().substring(13));*/
			int intNumber = Integer.parseInt("0");
			intNumber++;
			String stringNumber = String.valueOf(intNumber);
			for(int i =0;i<3;i++) {
				stringNumber = stringNumber.length() < 4 ? "0" + stringNumber : stringNumber;
			}
			specNumber = specNumber + stringNumber;
		}
		
		return specNumber;
	}

	@Override
	public List<DropDownListDto> queryStandardActionName(String standardActionName) {
		return technologySpecMapper.queryStandardActionName(standardActionName);
	}
	
	/**
	 * @author likai 2020.03.20
	 * @description 测试动作下拉列表查询
	 * @param standardActionName
	 * @return List<DropDownListDto>
	 */
	@Override
	public List<DropDownListDto> queryTestActionName(String standardActionName) {
		return technologySpecMapper.queryTestActionName(standardActionName);
	}
	
	@Override
	public List<DropDownListDto> queryAuxiliaryActionName(String standardActionName) {
		return technologySpecMapper.queryAuxiliaryActionName(standardActionName);
	}

	@Override
	public List<TechnologySpec> queryTechnologySpecList(TechnologySpec dto,int page,int pageSize, IRequest requestCtx,
			HttpServletRequest request) {
		PageHelper.startPage(page, pageSize);
		return technologySpecMapper.queryTechnologySpecList(dto);
	}

	@Override
	public List<DropDownListDto> queryMaterielName(String materielName) {
		return technologySpecMapper.queryMaterielName(materielName);
	}

	@Override
	public List<DropDownListDto> queryCharacteristicName(String characteristicName) {
		return technologySpecMapper.queryCharacteristicName(characteristicName);
	}

	//2020年2月27日14:39:33 
	@Override
	public ResponseData addData(List<TechnologySpec> dtos, IRequest requestCtx, HttpServletRequest request) {
		ResponseData responseData=new ResponseData();
		//新建标准动作  首先保存头 然后拿到主键之后 保存两个行表
		for (TechnologySpec dto : dtos) {
			dto.setSpecVersion("V1");
			dto.setSpecActionType("1");
			dto.setStatus("1");
			technologySpecMapper.insertSelective(dto);
			Float specId = dto.getSpecId();
			List<TechnologySpecMatDetail> matList = dto.getMatList();
			if (matList !=null) {
				for (TechnologySpecMatDetail matDto : matList) {
					matDto.setSpecId(specId);
					matDto.setSpecVersion(dto.getSpecVersion());
					technologySpecMatDetailMapper.insertSelective(matDto);
				}
			}
			List<TechnologySpecDetail> detailList = dto.getDetailList();
			if (detailList !=null) {
				for (TechnologySpecDetail detDto : detailList) {
					Date date = new Date();
					SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMM");
					String time = dateFormat.format(date);
					String codeSeq = technologySpecDetailMapper.getCodeSeq();
					String specNumber = "P-SPEC-" + time+codeSeq;
					detDto.setSpecNumber(specNumber);
					detDto.setSpecId(specId);
					detDto.setSpecVersion(dto.getSpecVersion());
					technologySpecDetailMapper.insertSelective(detDto);
				}
			}
		}
		responseData.setRows(dtos);
		return responseData;
	}
	
	/**
	 * @description 保存测试动作要求
	 * @author likai 2020.03.20
	 * @param dtos
	 * @param requestCtx
	 * @param request
	 * @return
	 */
	@Override
	public ResponseData addTestData(List<TechnologySpec> dtos, IRequest requestCtx, HttpServletRequest request) {
		ResponseData responseData=new ResponseData();
		//新建标准动作  首先保存头 然后拿到主键之后 保存两个行表
		for (TechnologySpec dto : dtos) {
			dto.setSpecVersion("V1");
			dto.setSpecActionType("2");
			dto.setStatus("1");
			technologySpecMapper.insertSelective(dto);
			Float specId = dto.getSpecId();
			TechnologySpecMatDetail matDto = new TechnologySpecMatDetail();
			if(dto.getSparePartId() != null) {
				//2020年4月2日15:51:09 这里修改为关联系列表
				/*TechnologySparePartDetails technologySparePartDetails = new TechnologySparePartDetails();
				technologySparePartDetails.setDetailsCode(dto.getSparePartId());
				List<TechnologySparePartDetails> list = technologySparePartDetailsMapper.select(technologySparePartDetails);
				if(list != null && list.size() > 0) {
					matDto.setMaterielAttributeNumber(String.valueOf(list.get(0).getSparePartDetailsId()));
				}*/
				matDto.setMaterielAttributeNumber(dto.getSparePartId());
				matDto.setSpecId(specId);
				matDto.setSpecVersion(dto.getSpecVersion());
				technologySpecMatDetailMapper.insertSelective(matDto);
			}
			List<TechnologySpecDetail> detailList = dto.getDetailList();
			if (detailList !=null) {
				for (TechnologySpecDetail detDto : detailList) {
					Date date = new Date();
					SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMM");
					String time = dateFormat.format(date);
					String codeSeq = technologySpecDetailMapper.getCodeSeq();
					String specNumber = "P-SPEC-" + time+codeSeq;
					detDto.setSpecNumber(specNumber);
					detDto.setSpecId(specId);
					detDto.setSpecVersion(dto.getSpecVersion());
					technologySpecDetailMapper.insertSelective(detDto);
				}
			}
		}
		responseData.setRows(dtos);
		return responseData;
	}

	@Override
	public List<TechnologySpec> querySpecData(TechnologySpec dto, int page, int pageSize, IRequest requestCtx,
			HttpServletRequest request) {
		PageHelper.startPage(page, pageSize);
		return technologySpecMapper.querySpecData(dto);
	}

	@Override
	public List<TechnologySpec> submitData(IRequest request, List<TechnologySpec> list) {
		for (TechnologySpec dto : list) {
			technologySpecMapper.updateByPrimaryKeySelective(dto);
			
			TechnologySpecMatDetail technologySpecMatDetail = new TechnologySpecMatDetail();
			technologySpecMatDetail.setSpecId(dto.getSpecId());
			technologySpecMatDetail.setSpecVersion(dto.getSpecVersion());
			technologySpecMatDetailMapper.delete(technologySpecMatDetail);
			
			//更新Mat行信息
			List<TechnologySpecMatDetail> matList = dto.getMatList();
			if (matList !=null) {
				for(int i = matList.size() - 1; i > -1; i --) {
					matList.get(i).setSpecId(dto.getSpecId());
					matList.get(i).setSpecVersion(dto.getSpecVersion());
					technologySpecMatDetailMapper.insertSelective(matList.get(i));
				}
			}
			
			TechnologySpecDetail technologySpecDetail = new TechnologySpecDetail();
			technologySpecDetail.setSpecId(dto.getSpecId());
			technologySpecDetail.setSpecVersion(dto.getSpecVersion());
			technologySpecDetailMapper.delete(technologySpecDetail);
			
			//更新Detail行信息
			List<TechnologySpecDetail> detailList = dto.getDetailList();
			if (detailList !=null) {
				for(int i = detailList.size() - 1; i > -1; i --) {
					if(detailList.get(i).getSpecNumber() == null) {
						Date date = new Date();
						SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMM");
						String time = dateFormat.format(date);
						String codeSeq = technologySpecDetailMapper.getCodeSeq();
						String specNumber = "P-SPEC-" + time+codeSeq;
						detailList.get(i).setSpecNumber(specNumber);
					}
					detailList.get(i).setSpecId(dto.getSpecId());
					detailList.get(i).setSpecVersion(dto.getSpecVersion());
					technologySpecDetailMapper.insertSelective(detailList.get(i));
				}
			}
		}
		return list;
	}
	
	/**
	 * @description 修改测试动作后执行保存
	 * @author likai 2020.03.20
	 * @param request
	 * @param list
	 * @return
	 */
	@Override
	public List<TechnologySpec> submitTestData(IRequest request, List<TechnologySpec> list) {
		for (TechnologySpec dto : list) {
			technologySpecMapper.updateByPrimaryKeySelective(dto);
			
			TechnologySpecMatDetail technologySpecMatDetail = new TechnologySpecMatDetail();
			technologySpecMatDetail.setSpecId(dto.getSpecId());
			technologySpecMatDetail.setSpecVersion(dto.getSpecVersion());
			technologySpecMatDetailMapper.delete(technologySpecMatDetail);
			//更新Mat行信息
			TechnologySpecMatDetail matDto = new TechnologySpecMatDetail();
			if(dto.getSparePartId() != null) {
				matDto.setMaterielAttributeNumber(dto.getSparePartId());
				matDto.setSpecId(dto.getSpecId());
				technologySpecMatDetailMapper.insertSelective(matDto);
			}
			
			TechnologySpecDetail technologySpecDetail = new TechnologySpecDetail();
			technologySpecDetail.setSpecId(dto.getSpecId());
			technologySpecDetail.setSpecVersion(dto.getSpecVersion());
			technologySpecDetailMapper.delete(technologySpecDetail);
			//新增Detail行信息
			List<TechnologySpecDetail> detailList = dto.getDetailList();
			if (detailList !=null) {
				for(int i = detailList.size() - 1; i > -1; i --) {
					if(detailList.get(i).getSpecNumber() == null) {
						Date date = new Date();
						SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMM");
						String time = dateFormat.format(date);
						String codeSeq = technologySpecDetailMapper.getCodeSeq();
						String specNumber = "P-SPEC-" + time+codeSeq;
						detailList.get(i).setSpecNumber(specNumber);
					}
					detailList.get(i).setSpecId(dto.getSpecId());
					detailList.get(i).setSpecVersion(dto.getSpecVersion());
					technologySpecDetailMapper.insertSelective(detailList.get(i));
				}
			}
		}
		return list;
	}
	
	
	@Override
	public int delData(List<TechnologySpec> list) {
		//删除头表和行表数据
		TechnologySpecMatDetail matDetail=new TechnologySpecMatDetail();
		TechnologySpecDetail detail=new TechnologySpecDetail();
		for (TechnologySpec dto : list) {
			technologySpecMapper.delete(dto);
			matDetail.setSpecId(dto.getSpecId());
			detail.setSpecId(dto.getSpecId());
			technologySpecMatDetailMapper.delete(matDetail);
			technologySpecDetailMapper.delete(detail);
		}
		return 0;
	}

	@Override
	public List<TechnologySpec> queryHisData(TechnologySpec dto, int page, int pageSize, IRequest requestCtx,
			HttpServletRequest request) {
		// 查看历史记录 找到这条记录的所有父代
		PageHelper.startPage(page, pageSize);
		return technologySpecMapper.queryHisData(dto);
	}

	@Override
	public List<TechnologySpec> querySpecInfo(TechnologySpec dto, int page, int pageSize, IRequest requestCtx,
			HttpServletRequest request) {
		PageHelper.startPage(page, pageSize);
		return technologySpecMapper.querySpecInfo(dto);
	}
	
	/**
	 * @description 执行审批，启动流程
	 * @author likai 2020.03.21
	 * @param dtos
	 * @param requestCtx
	 * @return
	 */
	@Override
	public void audit(List<TechnologySpec> dtos, IRequest requestCtx) {
		for (TechnologySpec dto : dtos) {
			//启动审核流程
			
			ProcessInstanceCreateRequest instanceCreateRequest = new ProcessInstanceCreateRequest();
			instanceCreateRequest.setBusinessKey(String.valueOf(dto.getSpecId()));
			instanceCreateRequest.setProcessDefinitionKey("technologySpecAudit");
			
			//更新审批状态
			dto.setStatus("2");
			technologySpecMapper.updateByPrimaryKeySelective(dto);
			
			List<RestVariable> restVariableList = new ArrayList<RestVariable>();
			RestVariable restVariableStartMan = new RestVariable();
			RestVariable restVariable = new RestVariable();
			RestVariable restVariableType = new RestVariable();
			
			restVariableStartMan.setName("startCode");
			restVariableStartMan.setValue(requestCtx.getEmployeeCode());
			restVariableList.add(restVariableStartMan);
			
			if("1".equals(dto.getSpecActionType())) {
				restVariableType.setName("actionType");
				restVariableType.setValue("1");
				restVariableList.add(restVariableType);
			} else if("2".equals(dto.getSpecActionType())) {
				restVariableType.setName("actionType");
				restVariableType.setValue("2");
				restVariableList.add(restVariableType);
			}
			
			TechnologySpecHis technologySpecHis = new TechnologySpecHis();
			List<TechnologySpecHis> hisList = technologySpecHisMapper.select(technologySpecHis);
			if(hisList != null && hisList.size() > 0) {
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
	 * @description 删除数据
	 * @author likai 2020.04.01
	 * @param dtos
	 * @param requestCtx
	 * @return
	 */
	@Override
	public void deleteData(List<TechnologySpec> dtos, IRequest requestCtx) {
		for (TechnologySpec dto : dtos) {
			TechnologySpecMatDetail technologySpecMatDetail = new TechnologySpecMatDetail();
			technologySpecMatDetail.setSpecId(dto.getSpecId());
			technologySpecMatDetail.setSpecVersion(dto.getSpecVersion());
			technologySpecMatDetailMapper.delete(technologySpecMatDetail);
			
			TechnologySpecDetail technologySpecDetail = new TechnologySpecDetail();
			technologySpecDetail.setSpecId(dto.getSpecId());
			technologySpecDetail.setSpecVersion(dto.getSpecVersion());
			technologySpecDetailMapper.delete(technologySpecDetail);
			
			technologySpecMapper.delete(dto);
		}
	}
	
}