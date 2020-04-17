package com.hand.npi.npi_technology.service;

import java.util.List;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.ContextLoader;

import com.hand.npi.npi_technology.dto.TechnologySpec;
import com.hand.npi.npi_technology.dto.TechnologySpecHis;
import com.hand.npi.npi_technology.mapper.TechnologySpecHisMapper;
import com.hand.npi.npi_technology.mapper.TechnologySpecMapper;
import com.hand.hap.activiti.custom.IActivitiBean;

@Transactional(rollbackFor = Exception.class)
public class TechnologySpecExamine implements JavaDelegate,IActivitiBean {

	@Override
	public void execute(DelegateExecution execution) {
		TechnologySpecHisMapper technologySpecHisMapper = ContextLoader.getCurrentWebApplicationContext()
				.getBean(TechnologySpecHisMapper.class);
		TechnologySpecMapper technologySpecMapper = ContextLoader.getCurrentWebApplicationContext()
				.getBean(TechnologySpecMapper.class);
		
		float specId = Float.valueOf(execution.getProcessInstanceBusinessKey());
		
		TechnologySpecHis technologySpecHis = new TechnologySpecHis();
		technologySpecHis.setSpecId(specId);
		List<TechnologySpecHis> hisList = technologySpecHisMapper.selectByLastUpdateDate(technologySpecHis);
		if(hisList != null && hisList.size() > 0) {//变更审核
			TechnologySpec technologySpec = new TechnologySpec();
			technologySpec.setSpecId(specId);
			List<TechnologySpec> specList = technologySpecMapper.select(technologySpec);
			
			//更新当前最新版本
			technologySpec = specList.get(0);
			technologySpec.setSpecVersion(hisList.get(0).getSpecVersion());
			technologySpec.setStandActionId(hisList.get(0).getStandActionId());
			technologySpec.setStandardWorkingHours(hisList.get(0).getStandardWorkingHours());
			technologySpec.setSpecRemark(hisList.get(0).getSpecRemark());
			technologySpec.setStatus("3");
			technologySpecMapper.updateByPrimaryKeySelective(technologySpec);
			
			//更新历史版本
			TechnologySpecHis technologySpecHisUpdate = hisList.get(0);
			technologySpecHisUpdate.setSpecVersion(specList.get(0).getSpecVersion());
			technologySpecHisUpdate.setStandActionId(specList.get(0).getStandActionId());
			technologySpecHisUpdate.setStandardWorkingHours(specList.get(0).getStandardWorkingHours());
			technologySpecHisUpdate.setSpecRemark(specList.get(0).getSpecRemark());
			technologySpecHisMapper.updateByPrimaryKeySelective(technologySpecHisUpdate);
			
			//批量更新当前最新版本
			TechnologySpecHis technologySpecHisUpdates = new TechnologySpecHis();
			technologySpecHisUpdates.setSpecId(specId);
			List<TechnologySpecHis> hisUpdatesList = technologySpecHisMapper.select(technologySpecHisUpdates);
			if(hisUpdatesList != null && hisUpdatesList.size() > 0) {
				for(TechnologySpecHis dto:hisUpdatesList) {
					technologySpecHisMapper.updateByPrimaryKeySelective(dto);
				}
			}
		} else if(hisList == null || hisList.size() == 0) {//初次审核
			TechnologySpec technologySpec = new TechnologySpec();
			technologySpec.setSpecId(specId);
			technologySpec.setSpecVersion("-");
			technologySpec.setStatus("3");
			technologySpecMapper.updateByPrimaryKeySelective(technologySpec);
		}
		execution.setVariable("amineResult", Boolean.TRUE); 
	}

	@Override
	public String getBeanName() {
		return "checkAmine";
	}
}
