package com.hand.hcs.hcs_po_headers.service.impl;

import com.github.pagehelper.PageHelper;
import com.hand.hap.account.dto.User;
import com.hand.hap.account.service.IUserService;
import com.hand.hap.core.IRequest;
import com.hand.hap.system.service.impl.BaseServiceImpl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import org.drools.core.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.hand.hcs.hcs_po_headers.dto.PoHeaders;
import com.hand.hcs.hcs_po_headers.mapper.PoHeadersMapper;
import com.hand.hcs.hcs_po_headers.service.IPoHeadersService;
import com.hand.hcs.hcs_po_line_locations.dto.PoLineLocations;
import com.hand.hcs.hcs_po_line_locations.service.IPoLineLocationsService;
import com.hand.hcs.hcs_po_lines.dto.PoLines;
import com.hand.hcs.hcs_po_lines.service.IPoLinesService;

import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(rollbackFor = Exception.class)
public class PoHeadersServiceImpl extends BaseServiceImpl<PoHeaders> implements IPoHeadersService{
	@Autowired
	private PoHeadersMapper poHeadersMapper;
	@Autowired
	private IUserService userService;
	@Autowired
	private IPoLinesService poLinesService;
	@Autowired
	private IPoLineLocationsService poLineLocationService;
	
	/**
	 * 查询 业务实体、库存组织、供应商、供应商地点
	 * @param poHeaders
	 * @return
	 */
	@Override
	public List<PoHeaders> queryUtil(IRequest requestContext, PoHeaders poHeaders) {
		User user = new User();
		user.setUserId(requestContext.getUserId());
		user = userService.selectByPrimaryKey(requestContext, user);
		List<PoHeaders> headersList = new ArrayList();
		if(user == null) {
			throw new RuntimeException("未找到用户");
		}else {
			if("S".equals(user.getUserType())) {
				headersList = poHeadersMapper.queryUtilS(poHeaders);
			}else {
				headersList = poHeadersMapper.queryUtilNoS(poHeaders);
			}
		}
		return headersList;
	}

	/**
	 *  采购订单头查询
	 * @param requestContext
	 * @param poHeaders
	 * @param page
	 * @param pageSize
	 * @return
	 */
	@Override
	public List<PoHeaders> query(IRequest requestContext, PoHeaders poHeaders, int page, int pageSize) {
		PageHelper.startPage(page, pageSize);
		return poHeadersMapper.query(poHeaders);
	}
	@Override
	public List<PoHeaders> confirm(IRequest requestContext, List<PoHeaders> headList) {
		for(PoHeaders head : headList) {
			PoHeaders poHead = new PoHeaders();
			poHead.setPoHeaderId(head.getPoHeaderId());
			poHead.setPoStatus("A");
			poHead.setConfirmTime(new Date());
			poHead.setConfirmFlag("Y");
			//更新订单头
			self().updateByPrimaryKeySelective(requestContext, poHead);
			
			PoLineLocations poLineLocation = new PoLineLocations();
			poLineLocation.setPoHeaderId(head.getPoHeaderId());
			List<PoLineLocations> poLineLocationsList = poLineLocationService.select(requestContext, poLineLocation, 0, 0);
			
			poLineLocationsList.forEach(data -> {
				data.setShipmentStatus("A");
				data.setConfirmTime(new Date());
				data.setConfirmFlag("Y");
				data.setConfirmBy((float)requestContext.getUserId());
				//更新发运行
				poLineLocationService.updateByPrimaryKeySelective(requestContext, data);
			});
		}
		
		return headList;
	}

	@Override
	public List<PoHeaders> checkLine(IRequest requestContext, List<PoHeaders> headList) {
		for(PoHeaders head : headList) {
			PoLineLocations poLineLocation = new PoLineLocations();
			poLineLocation.setPoHeaderId(head.getPoHeaderId());
			List<PoLineLocations> poLineLocationsList = poLineLocationService.select(requestContext, poLineLocation, 0, 0);
			long lineLocation = poLineLocationsList.stream().filter(data 
					-> "P".equals(data.getShipmentStatus()) && data.getPromisedDate() == null).count();
			if(lineLocation != 0) {
				throw new RuntimeException();
			}
		}
		return headList;
	}

}