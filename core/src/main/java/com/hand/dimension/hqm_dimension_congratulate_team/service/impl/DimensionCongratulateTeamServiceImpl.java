package com.hand.dimension.hqm_dimension_congratulate_team.service.impl;

import com.hand.hap.core.IRequest;
import com.hand.hap.system.dto.ResponseData;
import com.hand.hap.system.service.impl.BaseServiceImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.hand.dimension.hqm_dimension_congratulate_team.dto.DimensionCongratulateTeam;
import com.hand.dimension.hqm_dimension_congratulate_team.mapper.DimensionCongratulateTeamMapper;
import com.hand.dimension.hqm_dimension_congratulate_team.service.IDimensionCongratulateTeamService;
import com.hand.dimension.hqm_dimension_immediate_actions_head.dto.DimensionImmediateActionsHead;
import com.hand.dimension.hqm_dimension_immediate_actions_head.mapper.DimensionImmediateActionsHeadMapper;
import com.hand.dimension.hqm_dimension_immediate_actions_line.dto.DimensionImmediateActionsLine;
import com.hand.dimension.hqm_dimension_immediate_actions_line.mapper.DimensionImmediateActionsLineMapper;
import com.hand.dimension.hqm_dimension_improving_actions.dto.DimensionImprovingActions;
import com.hand.dimension.hqm_dimension_improving_actions.mapper.DimensionImprovingActionsMapper;
import com.hand.dimension.hqm_dimension_initiated_actions.dto.DimensionInitiatedActions;
import com.hand.dimension.hqm_dimension_initiated_actions.mapper.DimensionInitiatedActionsMapper;
import com.hand.dimension.hqm_dimension_order.service.IDimensionOrderService;
import com.hand.dimension.hqm_dimension_prevention_actions.dto.DimensionPreventionActions;
import com.hand.dimension.hqm_dimension_prevention_actions.mapper.DimensionPreventionActionsMapper;
import com.hand.dimension.hqm_dimension_team_assembled.dto.DimensionTeamAssembled;
import com.hand.dimension.hqm_dimension_team_assembled.mapper.DimensionTeamAssembledMapper;

import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(rollbackFor = Exception.class)
public class DimensionCongratulateTeamServiceImpl extends BaseServiceImpl<DimensionCongratulateTeam>
		implements IDimensionCongratulateTeamService {
	@Autowired
	DimensionCongratulateTeamMapper mapper;
	@Autowired
	DimensionTeamAssembledMapper dimensionTeamAssembledMapper;
	@Autowired
	IDimensionOrderService iDimensionOrderService;
	@Autowired
	DimensionImmediateActionsHeadMapper dimensionImmediateActionsHeadMapper;
	@Autowired
	DimensionImmediateActionsLineMapper dimensionImmediateActionsLineMapper;
	@Autowired
	DimensionImprovingActionsMapper dimensionImprovingActionsMapper;
	@Autowired
	DimensionInitiatedActionsMapper dimensionInitiatedActionsMapper;
	@Autowired
	DimensionPreventionActionsMapper dimensionPreventionActionsMapper;

	/* (non-Javadoc)
	 * @see com.hand.dimension.hqm_dimension_congratulate_team.service.IDimensionCongratulateTeamService
	 */
	@Override
	public ResponseData commit(IRequest requestContext, DimensionCongratulateTeam dto) throws Exception {
		ResponseData responseData = new ResponseData();
		iDimensionOrderService.changeStep(requestContext, dto.getOrderId(), 8, -1);
		return responseData;
	}

	/**
	 * 
	 * @description 删除此8d下的所有团队庆祝信息
	 * @author tianmin.wang
	 * @date 2019年11月21日 
	 * @param orderId
	 * @return
	 */
	public int deleteHaved(Float orderId) {
		DimensionCongratulateTeam search = new DimensionCongratulateTeam();
		search.setOrderId(orderId);
		List<DimensionCongratulateTeam> resultList = mapper.select(search);
		return self().batchDelete(resultList);
	}
	/* (non-Javadoc)
	 * @see com.hand.dimension.hqm_dimension_congratulate_team.service.IDimensionCongratulateTeamService
	 */
	@Override
	public int createByTeam(IRequest requestContext, Float orderId) throws Exception {
		deleteHaved(orderId);
		int createCount = 0;
		DimensionTeamAssembled searchTeamAssembled = new DimensionTeamAssembled();
		searchTeamAssembled.setOrderId(orderId);
		List<DimensionTeamAssembled> teamAssembleds = dimensionTeamAssembledMapper.select(searchTeamAssembled);
		if (teamAssembleds == null || teamAssembleds.size() == 0) {
			throw new Exception("团队未组建");
		}
		for (DimensionTeamAssembled teamAssembledLoop : teamAssembleds) {
			DimensionCongratulateTeam addCongratulateTeamModel = new DimensionCongratulateTeam();
			addCongratulateTeamModel.setOrderId(orderId);
			addCongratulateTeamModel.setMemberId(teamAssembledLoop.getMemberId());
			// 临时措施数量+
			addCongratulateTeamModel
					.setImmediateActionQty(this.getImmediateActionQty(orderId, teamAssembledLoop.getMemberId()));
			// 长期措施数量
			List<DimensionImprovingActions> improvingActions = this.getPcActionQty(orderId,
					teamAssembledLoop.getMemberId());
			addCongratulateTeamModel.setPcActionQty(Float.valueOf(String.valueOf(improvingActions.size())));
			// 被执行 长期措施数量+
			List<DimensionInitiatedActions> initiatedActions = this.getEpcActionQty(improvingActions);
			addCongratulateTeamModel.setEpcActionQty(Float.valueOf(String.valueOf(initiatedActions.size())));
			// 有效 长期措施数量
			addCongratulateTeamModel.setEffectivePcActionQty(this.getEffectivePcActionQty(initiatedActions));
			// 预防措施数量
			List<DimensionPreventionActions> dimensionPreventionActions = this.getPreventionActionQty(orderId,
					teamAssembledLoop.getMemberId());
			addCongratulateTeamModel
					.setPreventionActionQty(Float.valueOf(String.valueOf(dimensionPreventionActions.size())));
			// 被下达 预防措施数量+
			addCongratulateTeamModel.setRpaQty(this.getRpaQty(dimensionPreventionActions));
			// 执行措施总数量
			addCongratulateTeamModel
					.setSumActionQty(this.getSumActionQty(addCongratulateTeamModel.getImmediateActionQty(),
							addCongratulateTeamModel.getEpcActionQty(), addCongratulateTeamModel.getRpaQty()));
			self().insertSelective(requestContext, addCongratulateTeamModel);
			createCount++;
		}
		iDimensionOrderService.changeStepStatus(requestContext, orderId, 8);
		return createCount;
	}

	/**
	 * 
	 * @description 计算 ImmediateActionQty
	 * @author tianmin.wang
	 * @date 2019年11月21日 
	 * @param orderId
	 * @param userId
	 * @return
	 */
	public float getImmediateActionQty(Float orderId, Float userId) {
		float count = 0;
		DimensionImmediateActionsHead searchHead = new DimensionImmediateActionsHead();
		searchHead.setOrderId(orderId);

		List<DimensionImmediateActionsHead> headList = dimensionImmediateActionsHeadMapper.select(searchHead);
		if (headList == null)
			return 0;

		for (DimensionImmediateActionsHead head : headList) {
			DimensionImmediateActionsLine searchLine = new DimensionImmediateActionsLine();
			searchLine.setHeadActionId(head.getHeadActionId());
			searchLine.setUserId(userId);
			List<DimensionImmediateActionsLine> lineList = dimensionImmediateActionsLineMapper.select(searchLine);
			count += lineList.size();
		}
		return count;
	}

	/**
	 * 
	 * @description 获取长期措施数量
	 * @author tianmin.wang
	 * @date 2019年11月21日 
	 * @param orderId
	 * @param userId
	 * @return
	 */
	public List<DimensionImprovingActions> getPcActionQty(Float orderId, Float userId) {
		DimensionImprovingActions search = new DimensionImprovingActions();
		search.setOrderId(orderId);
		search.setUserId(userId);
		List<DimensionImprovingActions> result = dimensionImprovingActionsMapper.select(search);
		return result==null?result:(new ArrayList<DimensionImprovingActions>());
	}

	/**
	 * 
	 * @description 获取 被执行 长期措施数量
	 * @author tianmin.wang
	 * @date 2019年11月21日 
	 * @param list
	 * @return
	 */
	public List<DimensionInitiatedActions> getEpcActionQty(List<DimensionImprovingActions> list) {
		List<DimensionInitiatedActions> result = new ArrayList<>();
		for (DimensionImprovingActions inac : list) {
			DimensionInitiatedActions search = new DimensionInitiatedActions();
			search.setOrderId(inac.getOrderId());
			search.setPcActionId(inac.getActionId());
			List<DimensionInitiatedActions> serRes = dimensionInitiatedActionsMapper.select(search);
			if (serRes != null) {
				result.addAll(serRes);
			}
		}
		return result;
	}

	/**
	 * 
	 * @description 入参中有效数据的数目
	 * @author tianmin.wang
	 * @date 2019年11月21日 
	 * @param initiatedActions
	 * @return
	 */
	public float getEffectivePcActionQty(List<DimensionInitiatedActions> initiatedActions) {
		float count = 0;
		count = initiatedActions.stream().filter(dao -> dao.getEnableFlag().equals("Y")).count();
		return count;
	}

	/**
	 * 
	 * @description 通过用户和8d 主键id 获取关联的 DimensionPreventionActions所有数据
	 * @author tianmin.wang
	 * @date 2019年11月21日 
	 * @param orderId
	 * @param userId
	 * @return
	 */
	public List<DimensionPreventionActions> getPreventionActionQty(Float orderId, Float userId) {
		DimensionPreventionActions search = new DimensionPreventionActions();
		search.setOrderId(orderId);
		search.setUserId(userId);
		return dimensionPreventionActionsMapper.select(search);
	}

	/**
	 * 
	 * @description 获取 list中 actionStatus==1 的数量
	 * @author tianmin.wang
	 * @date 2019年11月21日 
	 * @param list
	 * @return
	 */
	public float getRpaQty(List<DimensionPreventionActions> list) {
		float count = 0;
		count = list.stream().filter(dao -> "1".equals(dao.getActionStatus())).count();
		return count;
	}

	/**
	 * 
	 * @description 求和
	 * @author tianmin.wang
	 * @date 2019年11月21日 
	 * @param pa1
	 * @param pa2
	 * @param pa3
	 * @return
	 */
	public float getSumActionQty(Float pa1, Float pa2, Float pa3) {
		return pa1 + pa2 + pa3;
	}
	
	
	/* (non-Javadoc)
	 * @see com.hand.dimension.hqm_dimension_congratulate_team.service.IDimensionCongratulateTeamService
	 */
	@Override
	public List<DimensionCongratulateTeam> reSelect(IRequest requestContext, DimensionCongratulateTeam dto, int page,
			int pageSize) {
//		PageHelper.startPage(page, pageSize);
		List<DimensionCongratulateTeam> list = new ArrayList<>();
		DimensionCongratulateTeam team_a = new DimensionCongratulateTeam();
		team_a.setOtherContributions("原因分析");
		DimensionCongratulateTeam team_b = new DimensionCongratulateTeam();
		team_b.setOtherContributions("长期措施");
		DimensionCongratulateTeam team_c = new DimensionCongratulateTeam();
		team_c.setOtherContributions("短期措施");
		DimensionCongratulateTeam team_d = new DimensionCongratulateTeam();
		team_d.setOtherContributions("预防措施");
		list.add(team_a);
		list.add(team_b);
		list.add(team_c);
		list.add(team_d);
		return list;
	}

	
}