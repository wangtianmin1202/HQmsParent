package com.hand.dimension.hqm_dimension_prevention_actions.service.impl;

import com.hand.hap.core.IRequest;
import com.hand.hap.system.dto.ResponseData;
import com.hand.hap.system.service.impl.BaseServiceImpl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.hand.dimension.hqm_dimension_congratulate_team.service.IDimensionCongratulateTeamService;
import com.hand.dimension.hqm_dimension_improving_actions.dto.DimensionImprovingActions;
import com.hand.dimension.hqm_dimension_order.service.IDimensionOrderService;
import com.hand.dimension.hqm_dimension_prevention_actions.dto.DimensionPreventionActions;
import com.hand.dimension.hqm_dimension_prevention_actions.mapper.DimensionPreventionActionsMapper;
import com.hand.dimension.hqm_dimension_prevention_actions.service.IDimensionPreventionActionsService;
import com.hand.dimension.hqm_dimension_team_assembled.dto.DimensionTeamAssembled;
import com.hand.dimension.hqm_dimension_team_assembled.mapper.DimensionTeamAssembledMapper;

import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(rollbackFor = Exception.class)
public class DimensionPreventionActionsServiceImpl extends BaseServiceImpl<DimensionPreventionActions> implements IDimensionPreventionActionsService{
	
	@Autowired
	DimensionPreventionActionsMapper mapper;
	@Autowired
	IDimensionOrderService iDimensionOrderService;
	@Autowired
	IDimensionCongratulateTeamService iDimensionCongratulateTeamService;
	@Autowired
    DimensionTeamAssembledMapper dimensionTeamAssembledMapper;
	@Override
	public List<DimensionPreventionActions> reSelect(IRequest requestContext, DimensionPreventionActions dto, int pageNum,
			int pageSize) {
		PageHelper.startPage(pageNum, pageSize);
		return mapper.reSelect(dto);
	}
	@Override
	public ResponseData commit(IRequest requestContext, DimensionPreventionActions dto) throws Exception {
		ResponseData responseData = new ResponseData();
		iDimensionOrderService.changeStep(requestContext,dto.getOrderId(),7,8);
		//创建生成第八步数据
		//iDimensionCongratulateTeamService.createByTeam(requestContext, dto.getOrderId());
		return responseData;
	}
	@Override
	public List<DimensionPreventionActions> batchIssue(IRequest requestCtx, List<DimensionPreventionActions> dto) {
		List<DimensionPreventionActions> result = new ArrayList<>();
		for(DimensionPreventionActions da : dto) {
			DimensionPreventionActions updator = new DimensionPreventionActions();
			updator.setActionId(da.getActionId());
			updator.setActualReleaseTime(new Date());
			updator.setActionStatus("1");
			self().updateByPrimaryKeySelective(requestCtx, updator);
		}
		return result;
	}
	@Override
	public List<DimensionPreventionActions> reBatchUpdate(IRequest requestCtx, List<DimensionPreventionActions> dto) throws Exception {
		for(DimensionPreventionActions c :dto)
		{
			Float headActionId = null;
			Float principalId = -1.0F;
			headActionId = c.getActionId();
			Date finishTime = c.getActualFinishiTime();
			if (finishTime != null) {
				c.setActionStatus("1");
			} 
			if (headActionId != null) {
				//查询出原有的成员ID
				DimensionPreventionActions actionsHead = mapper.selectByPrimaryKey(headActionId);
				//原成员ID
				principalId = actionsHead.getPrincipalId();
			}
			//根据角色及O单ID查询成员集合
			DimensionTeamAssembled dimensionTeamAssembled =new DimensionTeamAssembled();
			dimensionTeamAssembled.setOrderId(c.getOrderId());
			dimensionTeamAssembled.setMemberRole("4");	
			List<DimensionTeamAssembled> assembleds = dimensionTeamAssembledMapper.select(dimensionTeamAssembled);
			//根据OrderId,MemberRole,MemberId查询团队表定位数据
			dimensionTeamAssembled.setMemberId(principalId);
			List<DimensionTeamAssembled> list = dimensionTeamAssembledMapper.select(dimensionTeamAssembled);
			
			List<Float> memberIdList =  assembleds.stream().map(DimensionTeamAssembled :: getMemberId).distinct().collect(Collectors.toList());
			if (list != null && list.size() > 0) {
				//修改
				DimensionTeamAssembled teamAssembled = list.get(0);
				//团队表不包含新选中成员
				if (!memberIdList.contains(c.getPrincipalId())) {
					teamAssembled.setMemberId(c.getPrincipalId());
					dimensionTeamAssembledMapper.updateByPrimaryKeySelective(teamAssembled);
				}
				//修改之后查询原始表中是否存在修改之前的成员
				//查看原始表中成员总数量
				Integer totalNum = dimensionTeamAssembledMapper.queryMemberTotalNum(c.getOrderId(), principalId);
				if (totalNum == 1) {
					//删除团队表中数据
					dimensionTeamAssembledMapper.delete(dimensionTeamAssembled);
				}
				if (totalNum > 1) {
					//多条数据对应一条团队数据,修改一条，新增一条
					dimensionTeamAssembled.setMemberId(principalId);
					int count = dimensionTeamAssembledMapper.selectCount(dimensionTeamAssembled);
					if (count == 0) {
						dimensionTeamAssembledMapper.insertSelective(dimensionTeamAssembled);					
					}
				}
			} else {
				//新增
				dimensionTeamAssembled.setMemberId(c.getPrincipalId());
				int count = dimensionTeamAssembledMapper.selectCount(dimensionTeamAssembled);
				if (count == 0) {
					dimensionTeamAssembledMapper.insertSelective(dimensionTeamAssembled);					
				}	
			}
		}
		self().batchUpdate(requestCtx, dto);
		iDimensionOrderService.changeStepStatus(requestCtx, dto.get(0).getOrderId(), 7);
		return dto;
	}
	/* (non-Javadoc)
	 * @see com.hand.dimension.hqm_dimension_prevention_actions.service.IDimensionPreventionActionsService#batchUpdateById(java.util.List)
	 */
	@Override
	public void batchUpdateById(List<DimensionPreventionActions> dto) {
		// TODO Auto-generated method stub
		dto.forEach(e -> {
			e.setEnableFlag("N");
			mapper.updateByPrimaryKeySelective(e);
			//查看原始表中成员总数量
			Integer totalNum = dimensionTeamAssembledMapper.queryMemberTotalNum(e.getOrderId(), e.getPrincipalId());
			if (totalNum == 0) {
				DimensionTeamAssembled dimensionTeamAssembled = new DimensionTeamAssembled();
				dimensionTeamAssembled.setOrderId(e.getOrderId());
				dimensionTeamAssembled.setMemberId(e.getPrincipalId());
				dimensionTeamAssembled.setMemberRole("4");
				//删除团队表中数据
				dimensionTeamAssembledMapper.delete(dimensionTeamAssembled);
			}
		});
	}
	/* (non-Javadoc)
	 * @see com.hand.dimension.hqm_dimension_prevention_actions.service.IDimensionPreventionActionsService#selectDelete(com.hand.hap.core.IRequest, com.hand.dimension.hqm_dimension_prevention_actions.dto.DimensionPreventionActions, int, int)
	 */
	@Override
	public List<DimensionPreventionActions> selectDelete(IRequest requestContext, DimensionPreventionActions dto,
			int page, int pageSize) {
		PageHelper.startPage(page, pageSize);
		return mapper.reSelectDelete(dto);
	}
}