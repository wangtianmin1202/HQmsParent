package com.hand.npi.npi_technology.service;

import java.util.List;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.ContextLoader;

import com.hand.hap.activiti.custom.IActivitiBean;
import com.hand.npi.npi_technology.dto.InvalidPattern;
import com.hand.npi.npi_technology.dto.InvalidPatternHis;
import com.hand.npi.npi_technology.mapper.InvalidPatternHisMapper;
import com.hand.npi.npi_technology.mapper.InvalidPatternMapper;

@Transactional(rollbackFor = Exception.class)
public class PFMEAExamine implements JavaDelegate,IActivitiBean {

	@Override
	public void execute(DelegateExecution execution) {
		InvalidPatternHisMapper invalidPatternHisMapper = ContextLoader.getCurrentWebApplicationContext()
				.getBean(InvalidPatternHisMapper.class);
		InvalidPatternMapper invalidPatternMapper = ContextLoader.getCurrentWebApplicationContext()
				.getBean(InvalidPatternMapper.class);
		
		String invalidPatternNumber = execution.getProcessInstanceBusinessKey();
		
		InvalidPatternHis invalidPatternHis = new InvalidPatternHis();
		invalidPatternHis.setInvalidPatternNumber(invalidPatternNumber);
		List<InvalidPatternHis> iphList = invalidPatternHisMapper.select(invalidPatternHis);
		if(iphList != null && iphList.size() > 0) {//变更审核
			InvalidPattern invalidPattern = new InvalidPattern();
			invalidPattern.setInvalidPatternNumber(invalidPatternNumber);
			List<InvalidPattern> iplist = invalidPatternMapper.select(invalidPattern);
			
			//更新当前最新版本
			invalidPattern = iplist.get(0);
			invalidPattern.setPfmeaVersion(iphList.get(0).getPfmeaVersion());
			invalidPattern.setInvalidPatternName(iphList.get(0).getInvalidPatternName());
			invalidPattern.setInvalidPatternConsequence(iphList.get(0).getInvalidPatternConsequence());
			invalidPattern.setSev(iphList.get(0).getSev());
			invalidPattern.setApproveStatus("3");
			invalidPatternMapper.updateByPrimaryKeySelective(invalidPattern);
			
			//更新历史版本
			InvalidPatternHis invalidPatternHisUpdate = iphList.get(0);
			invalidPatternHisUpdate.setPfmeaVersion(iplist.get(0).getPfmeaVersion());
			invalidPatternHisUpdate.setInvalidPatternName(iplist.get(0).getInvalidPatternName());
			invalidPatternHisUpdate.setInvalidPatternConsequence(iplist.get(0).getInvalidPatternConsequence());
			invalidPatternHisUpdate.setSev(iplist.get(0).getSev());
			invalidPatternHisMapper.updateByPrimaryKeySelective(invalidPatternHisUpdate);
			
			//批量更新当前最新版本
			InvalidPatternHis invalidPatternHisUpdates = new InvalidPatternHis();
			invalidPatternHisUpdates.setInvalidPatternNumber(invalidPatternNumber);
			List<InvalidPatternHis> updateLists = invalidPatternHisMapper.select(invalidPatternHisUpdates);
			if(updateLists != null && updateLists.size() > 0) {
				for(InvalidPatternHis dto:updateLists) {
					dto.setPfmeaVersion(iphList.get(0).getPfmeaVersion());
					invalidPatternHisMapper.updateByPrimaryKeySelective(dto);
				}
			}
			
		}else if(iphList == null || iphList.size() == 0) {//初次审核
			InvalidPattern invalidPattern = new InvalidPattern();
			invalidPattern.setInvalidPatternNumber(invalidPatternNumber);
			List<InvalidPattern> list = invalidPatternMapper.select(invalidPattern);
			list.get(0).setApproveStatus("3");
			list.get(0).setPfmeaVersion("-");
			invalidPatternMapper.updateByPrimaryKeySelective(list.get(0));
		}
		execution.setVariable("amineResult", Boolean.TRUE); 
	}

	@Override
	public String getBeanName() {
		return "checkAmine";
	}
}
