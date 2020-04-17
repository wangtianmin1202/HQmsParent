package com.hand.plm.product_func_attr_basic.controllers;

import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.hand.hap.core.IRequest;
import com.hand.hap.system.controllers.BaseController;
import com.hand.hap.system.dto.ResponseData;
import com.hand.plm.product_func_attr_basic.dto.ProductFuncAttrBasic;
import com.hand.plm.product_func_attr_basic.service.IProductFuncAttrBasicService;
import com.hand.plm.product_func_attr_basic.view.TreeVO;

@Controller
public class ProductFuncAttrBasicController extends BaseController {
	@Autowired
	private IProductFuncAttrBasicService service;

	/**
	 * 查询产品树
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/plm/product/func/attr/queryTreeDatas")
	@ResponseBody
	public ResponseData queryTreeDatas(HttpServletRequest request) {
		IRequest requestContext = createRequestContext(request);
		return new ResponseData(service.selectTreeDatas(requestContext));
	}

	/**
	 * 根据参数查询产品树结
	 * 
	 * @param request
	 * @param param   查询参数
	 * @return
	 */
	@RequestMapping(value = "/plm/product/func/attr/queryTreeDatas/byParm")
	@ResponseBody
	public ResponseData selectTreeDatasByParms(HttpServletRequest request, @RequestParam("param") String param) {
		IRequest iRequest = createRequestContext(request);
		return new ResponseData(service.selectTreeDatasByParms(iRequest, param));
	}

	/**
	 * 下拉框
	 * 
	 * @param request
	 * @param dto
	 * @return
	 */
	@RequestMapping(value = "/plm/product/func/attr/query/levelNum")
	@ResponseBody
	public ResponseData queryLevelNum(HttpServletRequest request, ProductFuncAttrBasic dto) {
		System.out.println(dto.getLevelNum());
		IRequest iRequest = createRequestContext(request);
		return new ResponseData(service.queryLevelNum(iRequest, dto));
	}

	@RequestMapping(value = "/plm/product/func/attr/query/addTree")
	@ResponseBody
	public ResponseData addTree(HttpServletRequest request, @RequestBody TreeVO treeVO) {
		IRequest iRequest = createRequestContext(request);
		ResponseData responseData = new ResponseData();
		try {
			TreeVO newTree=service.addTree(iRequest, treeVO);
			responseData.setSuccess(true);
			responseData.setMessage("成功");
			responseData.setRows(Arrays.asList(newTree));
		} catch (RuntimeException ex) {
			responseData.setSuccess(false);
			responseData.setMessage(ex.getMessage());
		}
		return responseData;
	}

	@RequestMapping(value = "/plm/product/func/attr/query/renameTree")
	@ResponseBody
	public ResponseData renameTree(HttpServletRequest request, @RequestBody TreeVO treeVO) {
		IRequest iRequest = createRequestContext(request);
		ResponseData responseData = new ResponseData();
		try {
			service.renameTree(iRequest, treeVO);
			responseData.setSuccess(true);
			responseData.setMessage("成功");
		} catch (RuntimeException ex) {
			responseData.setSuccess(false);
			responseData.setMessage(ex.getMessage());
		}
		return responseData;
	}

	@RequestMapping(value = "/plm/product/func/attr/query/removeTree")
	@ResponseBody
	public ResponseData removeTree(HttpServletRequest request, @RequestBody TreeVO treeVO) {
		IRequest iRequest = createRequestContext(request);
		ResponseData responseData = new ResponseData();
		try {
			service.removeTree(iRequest, treeVO);
			responseData.setSuccess(true);
			responseData.setMessage("成功");
		} catch (RuntimeException ex) {
			responseData.setSuccess(false);
			responseData.setMessage(ex.getMessage());
		}
		return responseData;
	}
}
