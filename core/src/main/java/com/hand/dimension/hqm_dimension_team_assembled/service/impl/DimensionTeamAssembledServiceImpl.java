package com.hand.dimension.hqm_dimension_team_assembled.service.impl;

import com.hand.hap.core.IRequest;
import com.hand.hap.mybatis.common.Mapper;
import com.hand.hap.system.dto.BaseDTO;
import com.hand.hap.system.dto.DTOStatus;
import com.hand.hap.system.dto.ResponseData;
import com.hand.hap.system.service.IBaseService;
import com.hand.hap.system.service.impl.BaseServiceImpl;
import com.hand.hqm.hqm_sample_account_after.dto.SampleAccountAfter;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.springframework.aop.framework.AopContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.hand.dimension.hqm_dimension_order.dto.DimensionOrder;
import com.hand.dimension.hqm_dimension_order.service.IDimensionOrderService;
import com.hand.dimension.hqm_dimension_step.dto.DimensionStep;
import com.hand.dimension.hqm_dimension_step.mapper.DimensionStepMapper;
import com.hand.dimension.hqm_dimension_step.service.IDimensionStepService;
import com.hand.dimension.hqm_dimension_team_assembled.dto.DimensionTeamAssembled;
import com.hand.dimension.hqm_dimension_team_assembled.mapper.DimensionTeamAssembledMapper;
import com.hand.dimension.hqm_dimension_team_assembled.service.IDimensionTeamAssembledService;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(rollbackFor = Exception.class)
public class DimensionTeamAssembledServiceImpl extends BaseServiceImpl<DimensionTeamAssembled> implements IDimensionTeamAssembledService{
	@Autowired
    DimensionTeamAssembledMapper mapper;
	@Autowired
	IDimensionOrderService iDimensionOrderService;
	@Autowired
	IDimensionStepService iDimensionStepService;
	@Autowired
	DimensionStepMapper dimensionStepMapper;
	@Override
	public List<DimensionTeamAssembled> myselect(IRequest requestContext, DimensionTeamAssembled dto, int page,
			int pageSize) {
		// TODO Auto-generated method stub
		PageHelper.startPage(page, pageSize);
		return mapper.myselect(dto);
	}
	@Override
	public List<DimensionTeamAssembled> selectMulLov(IRequest requestContext, DimensionTeamAssembled dto, int page,
			int pageSize) {
		// TODO Auto-generated method stub
		PageHelper.startPage(page, pageSize);
		return mapper.mulLovSelect(dto);
	}
	@Override
	public List<DimensionTeamAssembled> batchUpdateRe(IRequest requestCtx, List<DimensionTeamAssembled> list) throws Exception {
		// TODO Auto-generated method stub
        self().batchUpdate(requestCtx, list);
        DimensionTeamAssembled search = new DimensionTeamAssembled();
        search.setOrderId(list.get(0).getOrderId());
        search.setMemberRole("L");
        List<DimensionTeamAssembled> filterList = mapper.select(search);
        if(filterList.size()>1) {
        	throw new Exception("只能存在一个组长");
        }
        //更新8D头的信息
        DimensionOrder updater = new DimensionOrder();
        updater.setOrderId(list.get(0).getOrderId());
        updater.setOrderStatus("1");
        iDimensionOrderService.updateByPrimaryKeySelective(requestCtx, updater);
        iDimensionOrderService.changeStepStatus(requestCtx, list.get(0).getOrderId(), 1);
        return list;
	}
	@Override
	public ResponseData commit(IRequest request,List<DimensionTeamAssembled> dto) throws Exception {
		// TODO 提交
		ResponseData responseData = new ResponseData();
		iDimensionOrderService.changeStep(request,dto.get(0).getOrderId(),1,2);
		return responseData;
	}
	
	@Override
    public ResponseData saveMenber(DimensionTeamAssembled dto,IRequest requestCtx, HttpServletRequest request) {  
		dto.setMemberId(Float.valueOf("1"));
		self().insert(requestCtx, dto);   
	    return new ResponseData();
     }  

}