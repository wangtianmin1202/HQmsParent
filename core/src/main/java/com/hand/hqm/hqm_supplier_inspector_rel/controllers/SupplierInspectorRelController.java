package com.hand.hqm.hqm_supplier_inspector_rel.controllers;

import org.springframework.stereotype.Controller;
import com.hand.hap.system.controllers.BaseController;
import com.hand.hap.core.IRequest;
import com.hand.hap.system.dto.ResponseData;
import com.hand.hcs.hcs_supply_demand.dto.SupplyDemand;
import com.hand.hqm.hqm_supplier_inspector_rel.dto.SupplierInspectorRel;
import com.hand.hqm.hqm_supplier_inspector_rel.service.ISupplierInspectorRelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import org.springframework.validation.BindingResult;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
public class SupplierInspectorRelController extends BaseController {

	@Autowired
	private ISupplierInspectorRelService service;

	/**
	 * 
	 * @description 基础查询入口
	 * @author tianmin.wang
	 * @date 2019年12月16日
	 * @param dto
	 * @param page
	 * @param pageSize
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/hqm/supplier/inspector/rel/query")
	@ResponseBody
	public ResponseData query(SupplierInspectorRel dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
			@RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
		IRequest requestContext = createRequestContext(request);
		return new ResponseData(service.reSelect(requestContext, dto, page, pageSize));
	}

	/**
	 * 
	 * @description 数据保存入口
	 * @author tianmin.wang
	 * @date 2019年12月16日
	 * @param dto
	 * @param result
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/hqm/supplier/inspector/rel/submit")
	@ResponseBody
	public ResponseData update(@RequestBody List<SupplierInspectorRel> dto, BindingResult result,
			HttpServletRequest request) {
		getValidator().validate(dto, result);
		if (result.hasErrors()) {
			ResponseData responseData = new ResponseData(false);
			responseData.setMessage(getErrorMessage(result, request));
			return responseData;
		}
		IRequest requestCtx = createRequestContext(request);
		return new ResponseData(service.reBatchUpdate(requestCtx, dto));
	}

	/**
	 * 
	 * @description 删除入口
	 * @author tianmin.wang
	 * @date 2019年12月16日
	 * @param request
	 * @param dto
	 * @return
	 */
	@RequestMapping(value = "/hqm/supplier/inspector/rel/remove")
	@ResponseBody
	public ResponseData delete(HttpServletRequest request, @RequestBody List<SupplierInspectorRel> dto) {
		service.batchDelete(dto);
		return new ResponseData();
	}

	/**
	 * 
	 * @description excel数据导入
	 * @author tianmin.wang
	 * @date 2019年12月16日
	 * @param request
	 * @return
	 * @throws Throwable
	 */
	@RequestMapping(value = "/hqm/supplier/inspector/rel/excel/upload")
	@ResponseBody
	public ResponseData excelUpload(HttpServletRequest request) throws Throwable {
		ResponseData responseData = new ResponseData();
		MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
		IRequest requestCtx = createRequestContext(request);
		// 获取文件map集合
		Map<String, MultipartFile> fileMap = multipartRequest.getFileMap();
		List<SupplierInspectorRel> returnList = new ArrayList<SupplierInspectorRel>();
		for (Map.Entry<String, MultipartFile> entry : fileMap.entrySet()) {
			MultipartFile forModel = entry.getValue();
			try {
				returnList.addAll(service.excelImport(requestCtx, forModel));
			} catch (RuntimeException e) {
				responseData = new ResponseData(false);
				responseData.setMessage(e.getMessage());
				return responseData;
			}
		}
		responseData.setRows(returnList);
		return responseData;
	}

	/**
	 * 
	 * @description 批量更新
	 * @author tianmin.wang
	 * @date 2019年12月16日
	 * @param dto
	 * @param page
	 * @param pageSize
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/hqm/supplier/inspector/rel/batch/save")
	@ResponseBody
	public ResponseData batchSave(SupplierInspectorRel dto, HttpServletRequest request) {
		IRequest requestContext = createRequestContext(request);
		try {
			service.batchSave(requestContext, dto);
		} catch (RuntimeException e) {
			ResponseData result = new ResponseData(false);
			result.setMessage(e.getMessage());
			return result;
		} catch (Exception e) {
			throw e;
		}
		return new ResponseData();
	}
}