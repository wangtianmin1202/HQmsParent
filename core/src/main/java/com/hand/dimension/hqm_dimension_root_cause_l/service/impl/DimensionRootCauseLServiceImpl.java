package com.hand.dimension.hqm_dimension_root_cause_l.service.impl;

import com.hand.hap.core.IRequest;
import com.hand.hap.system.service.impl.BaseServiceImpl;

import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.hand.dimension.hqm_dimension_improving_actions.dto.DimensionImprovingActions;
import com.hand.dimension.hqm_dimension_root_cause.dto.DimensionRootCause;
import com.hand.dimension.hqm_dimension_root_cause.mapper.DimensionRootCauseMapper;
import com.hand.dimension.hqm_dimension_root_cause_l.dto.DimensionRootCauseL;
import com.hand.dimension.hqm_dimension_root_cause_l.mapper.DimensionRootCauseLMapper;
import com.hand.dimension.hqm_dimension_root_cause_l.service.IDimensionRootCauseLService;
import com.hand.dimension.hqm_dimension_team_assembled.dto.DimensionTeamAssembled;
import com.hand.dimension.hqm_dimension_team_assembled.mapper.DimensionTeamAssembledMapper;

import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(rollbackFor = Exception.class)
public class DimensionRootCauseLServiceImpl extends BaseServiceImpl<DimensionRootCauseL>
		implements IDimensionRootCauseLService {
	@Autowired
	DimensionTeamAssembledMapper dimensionTeamAssembledMapper;
	@Autowired
	DimensionRootCauseLMapper mapper;
	@Autowired
	DimensionRootCauseMapper dimensionRootCauseMapper;

	@Override
	public List<DimensionRootCauseL> reSelect(IRequest requestContext, DimensionRootCauseL dto, int page,
			int pageSize) {
		// TODO Auto-generated method stub
		PageHelper.startPage(page, pageSize);
		return mapper.reSelect(dto);
	}

	@Override
	public List<DimensionRootCauseL> reBatchUpdate(IRequest requestCtx, List<DimensionRootCauseL> dto)
			throws Exception {
		for (DimensionRootCauseL c : dto) {
			if (c.getPrincipalId() != null) {
				DimensionRootCause dimensionRootCause = new DimensionRootCause();
				dimensionRootCause.setRcauseId(c.getRcauseId());
				dimensionRootCause = dimensionRootCauseMapper.selectByPrimaryKey(dimensionRootCause);
				
				Float headActionId = null;
				Float principalId = -1.0F;
				headActionId = c.getLineId();
				if (headActionId != null) {
					//查询出原有的成员ID
					DimensionRootCauseL actionsHead = mapper.selectByPrimaryKey(headActionId);
					//原成员ID
					principalId = actionsHead.getPrincipalId();
				}
				//根据角色及O单ID查询成员集合
				DimensionTeamAssembled dimensionTeamAssembled =new DimensionTeamAssembled();
				dimensionTeamAssembled.setOrderId(dimensionRootCause.getOrderId());
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
					Integer totalNum = dimensionTeamAssembledMapper.queryMemberTotalNum(dimensionRootCause.getOrderId(), principalId);
					
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
		}
		self().batchUpdate(requestCtx, dto);
		return dto;
	}
	/* (non-Javadoc)
	 * @see com.hand.dimension.hqm_dimension_root_cause_l.service.IDimensionRootCauseLService#batchUpdateById(javax.servlet.http.HttpServletRequest, java.util.List)
	 */
	@Override
	public void batchUpdateById(HttpServletRequest request, List<DimensionRootCauseL> dto) {
		// TODO Auto-generated method stub
		dto.forEach(e -> {
			e.setEnableFlag("N");
			mapper.updateByPrimaryKeySelective(e);
			//查询出OrderID
			DimensionRootCause dimensionRootCause = new DimensionRootCause();
			dimensionRootCause.setRcauseId(e.getRcauseId());
			dimensionRootCause = dimensionRootCauseMapper.selectByPrimaryKey(dimensionRootCause);
			//查看原始表中成员总数量
			Integer totalNum = dimensionTeamAssembledMapper.queryMemberTotalNum(dimensionRootCause.getOrderId(), e.getPrincipalId());
			if (totalNum == 0) {
				DimensionTeamAssembled dimensionTeamAssembled = new DimensionTeamAssembled();
				dimensionTeamAssembled.setOrderId(dimensionRootCause.getOrderId());
				dimensionTeamAssembled.setMemberId(e.getPrincipalId());
				dimensionTeamAssembled.setMemberRole("4");
				//删除团队表中数据
				dimensionTeamAssembledMapper.delete(dimensionTeamAssembled);
			}
		});
	}
}