package com.hand.hqm.hqm_db_p_management.controllers;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.hand.hap.core.IRequest;
import com.hand.hap.system.controllers.BaseController;
import com.hand.hap.system.dto.ResponseData;
import com.hand.hqm.hqm_db_p_management.dto.HQMPInvalidTree;
import com.hand.hqm.hqm_db_p_management.service.IHqmpInvalidTreeService;

@Controller
public class HqmpInvalidTreeController extends BaseController {
	
	@Autowired
	private IHqmpInvalidTreeService service;

	/**
	 * @Author han.zhang
	 * @Description 查询附着对象层级维护 树形图数据
	 * @Date 11:39 2019/8/19
	 * @Param [dto, page, pageSize, request]
	 */
	@RequestMapping(value = "/hdmp/invalid/query/tree/data")
	@ResponseBody
	public ResponseData queryTreeData(HQMPInvalidTree dto, HttpServletRequest request) {
		IRequest requestContext = createRequestContext(request);
		return new ResponseData(service.queryTreeData(requestContext, dto));
	}

	/**
	 * @Author han.zhang
	 * @Description 附着对象更新、新增
	 * @Date 18:10 2019/8/19
	 * @Param [dto, result, request]
	 */
	@RequestMapping(value = "/hdmp/invalid/save/or/update")
	@ResponseBody
	public ResponseData updateOrSave(HQMPInvalidTree dto, HttpServletRequest request) {
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
	@RequestMapping(value = "/hdmp/invalid/delete/row")
	@ResponseBody
	public ResponseData deleteRow(HQMPInvalidTree dto, HttpServletRequest request) {
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