package com.hand.hqm.hqm_switch_score.service.impl;

import com.github.pagehelper.PageHelper;
import com.hand.hap.core.IRequest;
import com.hand.hap.core.annotation.StdWho;
import com.hand.hap.system.dto.BaseDTO;
import com.hand.hap.system.dto.DTOStatus;
import com.hand.hap.system.service.IBaseService;
import com.hand.hap.system.service.IPromptService;
import com.hand.hap.system.service.impl.BaseServiceImpl;
import com.hand.hap.util.SystemApiMethod;

import java.util.List;

import org.springframework.aop.framework.AopContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.hand.hqm.hqm_switch_score.dto.SwitchScore;
import com.hand.hqm.hqm_switch_score.mapper.SwitchScoreMapper;
import com.hand.hqm.hqm_switch_score.service.ISwitchScoreService;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(rollbackFor = Exception.class)
public class SwitchScoreServiceImpl extends BaseServiceImpl<SwitchScore> implements ISwitchScoreService {

	@Autowired
	SwitchScoreMapper mapper;
	@Autowired
	IPromptService iPromptService;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.hand.hqm.hqm_switch_score.service.ISwitchScoreService#reSelect(com.hand.
	 * hap.core.IRequest, com.hand.hqm.hqm_switch_score.dto.SwitchScore, int, int)
	 */
	@Override
	public List<SwitchScore> reSelect(IRequest requestContext, SwitchScore dto, int page, int pageSize) {
		// TODO Auto-generated method stub
		PageHelper.startPage(page, pageSize);
		return mapper.reSelect(dto);
	}

	@Override
	public List<SwitchScore> batchUpdate(IRequest request, List<SwitchScore> list) {
		for (SwitchScore t : list) {
			switch (((BaseDTO) t).get__status()) {
			case DTOStatus.ADD:
				self().insertSelective(request, t);
				break;
			case DTOStatus.UPDATE:
				if (useSelectiveUpdate()) {
					self().updateByPrimaryKeySelective(request, t);
				} else {
					self().updateByPrimaryKey(request, t);
				}
				break;
			case DTOStatus.DELETE:
				self().deleteByPrimaryKey(t);
				break;
			default:
				break;
			}
		}
		list.forEach(p -> {
			SwitchScore ssSearch = new SwitchScore();
			ssSearch.setItemId(p.getItemId());
			ssSearch.setPlantId(p.getPlantId());
			List<SwitchScore> res = mapper.select(ssSearch);

			if (res != null && res.size() > 0
					&& res.stream().filter(switchScore -> switchScore.getSupplierId() == null).count() > 1) {
				throw new RuntimeException(
						SystemApiMethod.getPromptDescription(request, iPromptService, "switchscore.update.error1"));// 该物料已维护成品检验属性
			}
		});
		return list;
	}

}