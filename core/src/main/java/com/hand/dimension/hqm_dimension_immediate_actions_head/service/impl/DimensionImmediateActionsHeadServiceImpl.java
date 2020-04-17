package com.hand.dimension.hqm_dimension_immediate_actions_head.service.impl;

import com.hand.hap.core.IRequest;
import com.hand.hap.core.impl.ServiceRequest;
import com.hand.hap.system.dto.BaseDTO;
import com.hand.hap.system.dto.DTOStatus;
import com.hand.hap.system.dto.ResponseData;
import com.hand.hap.system.service.ICodeService;
import com.hand.hap.system.service.impl.BaseServiceImpl;
import com.hand.hap.util.SoapPostUtil;
import com.hand.hqm.hqm_mes_ng_recorde.dto.MesNgRecorde;
import com.hand.hqm.hqm_mes_ng_recorde.mapper.MesNgRecordeMapper;
import com.hand.hqm.hqm_qc_task.service.impl.FqcTaskServiceImpl;
import com.hand.sys.sys_if_invoke_outbound.dto.IfInvokeOutbound;
import com.hand.sys.sys_if_invoke_outbound.mapper.IfInvokeOutboundMapper;
import com.thoughtworks.xstream.mapper.Mapper.Null;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.hand.dimension.hqm_dimension_immediate_actions_head.dto.DimensionImmediateActionsHead;
import com.hand.dimension.hqm_dimension_immediate_actions_head.idto.WorkStationAbnormalFeedback;
import com.hand.dimension.hqm_dimension_immediate_actions_head.mapper.DimensionImmediateActionsHeadMapper;
import com.hand.dimension.hqm_dimension_immediate_actions_head.service.IDimensionImmediateActionsHeadService;
import com.hand.dimension.hqm_dimension_immediate_actions_line.dto.DimensionImmediateActionsLine;
import com.hand.dimension.hqm_dimension_immediate_actions_line.mapper.DimensionImmediateActionsLineMapper;
import com.hand.dimension.hqm_dimension_improving_actions.dto.DimensionImprovingActions;
import com.hand.dimension.hqm_dimension_order.dto.DimensionOrder;
import com.hand.dimension.hqm_dimension_order.mapper.DimensionOrderMapper;
import com.hand.dimension.hqm_dimension_order.service.IDimensionOrderService;
import com.hand.dimension.hqm_dimension_root_cause.dto.DimensionRootCause;
import com.hand.dimension.hqm_dimension_team_assembled.dto.DimensionTeamAssembled;
import com.hand.dimension.hqm_dimension_team_assembled.mapper.DimensionTeamAssembledMapper;

import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(rollbackFor = Exception.class)
public class DimensionImmediateActionsHeadServiceImpl extends BaseServiceImpl<DimensionImmediateActionsHead>
		implements IDimensionImmediateActionsHeadService {
	@Autowired
	DimensionImmediateActionsHeadMapper mapper;
	@Autowired
	IDimensionOrderService iDimensionOrderService;
	@Autowired
	DimensionTeamAssembledMapper dimensionTeamAssembledMapper;
	@Autowired
	DimensionOrderMapper dimensionOrderMapper;
	@Autowired
	MesNgRecordeMapper mesNgRecordeMapper;
	@Autowired
	DimensionImmediateActionsLineMapper dimensionImmediateActionsLineMapper;
	@Autowired
	ICodeService iCodeService;
	@Autowired
	IfInvokeOutboundMapper ifInvokeOutboundMapper;
	@Resource(name = "taskExecutor")
	TaskExecutor taskExecutor;
	Logger logger = LoggerFactory.getLogger(DimensionImmediateActionsHeadServiceImpl.class);

	@Override
	public List<DimensionImmediateActionsHead> saveOne(IRequest requestContext, DimensionImmediateActionsHead dto) {
		List<DimensionImmediateActionsHead> returnList = new ArrayList<>();
		self().insertSelective(requestContext, dto);

		returnList.add(dto);
		return returnList;
	}

	@Override
	public List<DimensionImmediateActionsHead> deleteOne(IRequest requestContext, DimensionImmediateActionsHead dto) {
		self().deleteByPrimaryKey(dto);
		return new ArrayList<>();
	}

	@Override
	public ResponseData commit(IRequest request, DimensionImmediateActionsHead dto) throws Exception {
		ResponseData responseData = new ResponseData();
		iDimensionOrderService.changeStep(request, dto.getOrderId(), 3, 4);
		// wtm-20200224 临时措施项提交后。若8D报告单号在HQM_MES_NG_recorde表中的8D_number存 则产生此接口信息
		// mes接口调用
		pushToMes(dto.getOrderId());
		return responseData;
	}

	/**
	 * 
	 * @description 工位异常信息反馈 mes 接口方法
	 * @author tianmin.wang
	 * @date 2020年2月24日
	 */
	public void pushToMes(Float orderId) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		DimensionOrder dOrder = new DimensionOrder();
		dOrder.setOrderId(orderId);
		dOrder = dimensionOrderMapper.selectByPrimaryKey(dOrder);
		MesNgRecorde ngR = new MesNgRecorde();
		ngR.setNumber8d(dOrder.getOrderCode());
		List<MesNgRecorde> ngList = mesNgRecordeMapper.select(ngR);
		if (ngList == null || ngList.size() == 0)
			return;
		DimensionImmediateActionsHead hSearch = new DimensionImmediateActionsHead();
		hSearch.setOrderId(orderId);
		List<DimensionImmediateActionsHead> hResult = mapper.select(hSearch);

		DimensionImmediateActionsLine lSearch = new DimensionImmediateActionsLine();
		lSearch.setHeadActionId(hResult.get(0).getHeadActionId());
		List<DimensionImmediateActionsLine> lResult = dimensionImmediateActionsLineMapper.reSelect(lSearch);
		ObjectMapper obj = new ObjectMapper();
		for (DimensionImmediateActionsLine line : lResult) {
			taskExecutor.execute(() -> {
				IfInvokeOutbound iio = new IfInvokeOutbound();
				try {
					WorkStationAbnormalFeedback wsaf = new WorkStationAbnormalFeedback();
					wsaf.taskType = ngList.get(0).getTaskType();
					wsaf.taskNo = ngList.get(0).getTaskNumber();
					wsaf.measures = hResult.get(0).getImmediateAction();
					wsaf.personLiable = line.getUserName();
					wsaf.startDate = sdf.format(line.getPlanTime());
					String param = obj.writeValueAsString(wsaf);
					ServiceRequest sr = new ServiceRequest();
					sr.setLocale("zh_CN");
					String uri = iCodeService.getCodeValueByMeaning(sr, "HAP.SYSTEM", "MES_WS_URI");// 获取调用地址
					SoapPostUtil.Response re = SoapPostUtil.ticketSrmToMes(uri, "workStationAbnormalFeedback", param,
							iio);
					ifInvokeOutboundMapper.insertSelective(iio);
					logger.info(SoapPostUtil.getStringFromResponse(re));
				} catch (Exception e) {
					iio.setResponseContent(e.getMessage());
					iio.setResponseCode("E");
					ifInvokeOutboundMapper.insertSelective(iio);
					logger.warn(e.getMessage());
				}
			});
		}
	}

	@Override
	public List<DimensionImmediateActionsHead> reBatchUpdate(IRequest request, List<DimensionImmediateActionsHead> list)
			throws Exception {
		for (DimensionImmediateActionsHead dto : list) {
			Float headActionId = null;
			Float principalId = -1.0F;
			headActionId = dto.getHeadActionId();
			Date finishTime = dto.getActualFinishiTime();
			if (finishTime != null) {
				dto.setActionStatus("1");
			}
			if (headActionId != null) {
				// 查询出原有的成员ID
				DimensionImmediateActionsHead actionsHead = mapper.selectByPrimaryKey(headActionId);
				// 原成员ID
				principalId = actionsHead.getPrincipalId();
			}
			// 根据角色及O单ID查询成员集合
			DimensionTeamAssembled dimensionTeamAssembled = new DimensionTeamAssembled();
			dimensionTeamAssembled.setOrderId(dto.getOrderId());
			dimensionTeamAssembled.setMemberRole("4");
			List<DimensionTeamAssembled> assembleds = dimensionTeamAssembledMapper.select(dimensionTeamAssembled);
			// 根据OrderId,MemberRole,MemberId查询团队表定位数据
			dimensionTeamAssembled.setMemberId(principalId);
			List<DimensionTeamAssembled> teamAssembleds = dimensionTeamAssembledMapper.select(dimensionTeamAssembled);

			List<Float> memberIdList = assembleds.stream().map(DimensionTeamAssembled::getMemberId).distinct()
					.collect(Collectors.toList());
			if (teamAssembleds != null && teamAssembleds.size() > 0) {
				// 修改
				DimensionTeamAssembled teamAssembled = teamAssembleds.get(0);
				// 团队表不包含新选中成员
				if (!memberIdList.contains(dto.getPrincipalId())) {
					teamAssembled.setMemberId(dto.getPrincipalId());
					dimensionTeamAssembledMapper.updateByPrimaryKeySelective(teamAssembled);
				}
				// 修改之后查询原始表中是否存在修改之前的成员
				// 查看原始表中成员总数量
				Integer totalNum = dimensionTeamAssembledMapper.queryMemberTotalNum(dto.getOrderId(), principalId);

				if (totalNum == 1) {
					// 删除团队表中数据
					dimensionTeamAssembledMapper.delete(dimensionTeamAssembled);
				}
				if (totalNum > 1) {
					// 多条数据对应一条团队数据,修改一条，新增一条
					dimensionTeamAssembled.setMemberId(principalId);
					int count = dimensionTeamAssembledMapper.selectCount(dimensionTeamAssembled);
					if (count == 0) {
						dimensionTeamAssembledMapper.insertSelective(dimensionTeamAssembled);
					}
				}
			} else {
				// 新增
				dimensionTeamAssembled.setMemberId(dto.getPrincipalId());
				int count = dimensionTeamAssembledMapper.selectCount(dimensionTeamAssembled);
				if (count == 0) {
					dimensionTeamAssembledMapper.insertSelective(dimensionTeamAssembled);
				}
			}
		}
		self().batchUpdate(request, list);
		iDimensionOrderService.changeStepStatus(request, list.get(0).getOrderId(), 3);
		return list;
	}

	@Override
	public List<DimensionImmediateActionsHead> reSelect(IRequest requestContext, DimensionImmediateActionsHead dto,
			int page, int pageSize) {
		return mapper.reSelect(dto);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.hand.dimension.hqm_dimension_immediate_actions_head.service.
	 * IDimensionImmediateActionsHeadService#batchUpdateById(java.util.List)
	 */
	@Override
	public void batchUpdateById(List<DimensionImmediateActionsHead> dto) {
		// TODO Auto-generated method stub
		dto.forEach(e -> {
			e.setEnableFlag("N");
			mapper.updateByPrimaryKeySelective(e);
			// 查看原始表中成员总数量
			Integer totalNum = dimensionTeamAssembledMapper.queryMemberTotalNum(e.getOrderId(), e.getPrincipalId());
			if (totalNum == 0) {
				DimensionTeamAssembled dimensionTeamAssembled = new DimensionTeamAssembled();
				dimensionTeamAssembled.setOrderId(e.getOrderId());
				dimensionTeamAssembled.setMemberId(e.getPrincipalId());
				dimensionTeamAssembled.setMemberRole("4");
				// 删除团队表中数据
				dimensionTeamAssembledMapper.delete(dimensionTeamAssembled);
			}
		});
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.hand.dimension.hqm_dimension_immediate_actions_head.service.
	 * IDimensionImmediateActionsHeadService#reSelectDelete(com.hand.hap.core.
	 * IRequest, com.hand.dimension.hqm_dimension_immediate_actions_head.dto.
	 * DimensionImmediateActionsHead, int, int)
	 */
	@Override
	public List<DimensionImmediateActionsHead> reSelectDelete(IRequest requestContext,
			DimensionImmediateActionsHead dto, int page, int pageSize) {
		// TODO Auto-generated method stub
		PageHelper.startPage(page, pageSize);
		return mapper.reSelectDelete(dto);
	}

}