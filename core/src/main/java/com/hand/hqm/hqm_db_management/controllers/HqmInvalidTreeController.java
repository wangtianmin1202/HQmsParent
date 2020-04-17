package com.hand.hqm.hqm_db_management.controllers;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.hand.hap.core.IRequest;
import com.hand.hap.system.controllers.BaseController;
import com.hand.hap.system.dto.ResponseData;
import com.hand.hqm.hqm_db_management.dto.HQMInvalidTree;
import com.hand.hqm.hqm_db_management.service.IHqmInvalidTreeService;

@Controller
public class HqmInvalidTreeController extends BaseController {
	
	@Autowired
	private IHqmInvalidTreeService service;

	/**
	 * @Author han.zhang
	 * @Description 查询附着对象层级维护 树形图数据
	 * @Date 11:39 2019/8/19
	 * @Param [dto, page, pageSize, request]
	 */
	@RequestMapping(value = "/hdm/invalid/query/tree/data")
	@ResponseBody
	public ResponseData queryTreeData(HQMInvalidTree dto, HttpServletRequest request) {
		IRequest requestContext = createRequestContext(request);
		if(!"".equals(dto.getStructureName())&&dto.getStructureName()!=null) {
			dto.setParentInvalidId(null);
			dto.setInvalidName(dto.getStructureName());
		}else {
			dto.setParentInvalidId(0L);
		}
		return new ResponseData(service.queryTreeData(requestContext, dto));
	}

	/**
	 * @Author han.zhang
	 * @Description 附着对象更新、新增
	 * @Date 18:10 2019/8/19
	 * @Param [dto, result, request]
	 */
	@RequestMapping(value = "/hdm/invalid/save/or/update")
	@ResponseBody
	public ResponseData updateOrSave(HQMInvalidTree dto, HttpServletRequest request) {
		ResponseData responseData = new ResponseData();
		IRequest requestCtx = createRequestContext(request);
		try {
			responseData = service.updateOrAdd(requestCtx, dto);
		} catch (Exception e) {
			e.printStackTrace();
			responseData.setMessage(e.toString());
			responseData.setSuccess(false);
		}
		return responseData;
	}

	/**
	 * @Author han.zhang
	 * @Description 根据主键删除附着对象
	 * @Date 10:51 2019/8/20
	 * @Param [dto, request]
	 */
	@RequestMapping(value = "/hdm/invalid/delete/row")
	@ResponseBody
	public ResponseData deleteRow(HQMInvalidTree dto, HttpServletRequest request) {
		ResponseData responseData = new ResponseData(true);
		try {
			service.deleteRow(dto);
		} catch (Exception e) {
			e.printStackTrace();
			responseData.setMessage(e.getMessage());
			responseData.setSuccess(false);
		}
		return responseData;
	}
}