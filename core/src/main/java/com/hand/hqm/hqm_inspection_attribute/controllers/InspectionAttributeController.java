package com.hand.hqm.hqm_inspection_attribute.controllers;

import org.springframework.stereotype.Controller;
import com.hand.hap.system.controllers.BaseController;
import com.hand.hap.core.IRequest;
import com.hand.hap.system.dto.ResponseData;
import com.hand.hqm.hqm_inspection_attribute.dto.InspectionAttribute;
import com.hand.hqm.hqm_inspection_attribute.service.IInspectionAttributeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.validation.BindingResult;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
public class InspectionAttributeController extends BaseController {

	@Autowired
	private IInspectionAttributeService service;

	
	/**
	 * 
	 * @description 基础查询
	 * @author tianmin.wang
	 * @date 2019年11月22日 
	 * @param dto
	 * @param page
	 * @param pageSize
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/hqm/inspection/attribute/query")
	@ResponseBody
	public ResponseData query(InspectionAttribute dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
			@RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
		IRequest requestContext = createRequestContext(request);
		return new ResponseData(service.select(requestContext, dto, page, pageSize));
	}

	/**
	 * 
	 * @description 基础查询
	 * @author tianmin.wang
	 * @date 2019年11月22日 
	 * @param dto
	 * @param page
	 * @param pageSize
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/hqm/inspection/attribute/select")
	@ResponseBody
	public ResponseData select(InspectionAttribute dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
			@RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
		IRequest requestContext = createRequestContext(request);
		return new ResponseData(service.reSelect(requestContext, dto, page, pageSize));
	}

	/**
	 * 
	 * @description 保存
	 * @author tianmin.wang
	 * @date 2019年11月22日 
	 * @param dto
	 * @param result
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/hqm/inspection/attribute/submit")
	@ResponseBody
	public ResponseData update(@RequestBody List<InspectionAttribute> dto, BindingResult result,
			HttpServletRequest request) {
		getValidator().validate(dto, result);
		if (result.hasErrors()) {
			ResponseData responseData = new ResponseData(false);
			responseData.setMessage(getErrorMessage(result, request));
			return responseData;
		}
//        for(InspectionAttribute inspectionAttribute : dto) {
//        	if("Y".equals(inspectionAttribute.getEnableFlag())) {
//        		//有效数据中唯一
//        		InspectionAttribute search = new InspectionAttribute();
//        		search.setEnableFlag("Y");
//        		search.setSampleProcedureType(inspectionAttribute.getSampleProcedureType());
//        		search.setInspectionAttribute(inspectionAttribute.getInspectionAttribute());
//        		search.setSourceType(inspectionAttribute.getSourceType());
//        		List<InspectionAttribute> resultList = service.select(null, search, 1, 1);
//        		if(resultList!=null && resultList.size()>0) {
//        			ResponseData responseData = new ResponseData(false);
//        	        responseData.setMessage("抽样方案:"+inspectionAttribute.getSampleProcedureType()
//        	        +"/检验项:"+inspectionAttribute.getInspectionAttribute()
//        	        +"/来源类型:"+inspectionAttribute.getSourceType()
//        	        +"/数据已存在");
//        	        return responseData;
//        		}
//        	}
//        }
		IRequest requestCtx = createRequestContext(request);
		if (dto == null || dto.size() == 0) {
			ResponseData responseData = new ResponseData();
			responseData.setMessage("FLAG");
			return responseData;
		}
		return new ResponseData(service.reBatchUpdate(requestCtx, dto));
	}

	/**
	 * 
	 * @description 删除
	 * @author tianmin.wang
	 * @date 2019年11月22日 
	 * @param request
	 * @param dto
	 * @return
	 */
	@RequestMapping(value = "/hqm/inspection/attribute/remove")
	@ResponseBody
	public ResponseData delete(HttpServletRequest request, @RequestBody List<InspectionAttribute> dto) {
		service.batchDelete(dto);
		return new ResponseData();
	}

	
	/**
	 * 
	 * @description excel导入
	 * @author tianmin.wang
	 * @date 2019年11月22日 
	 * @param request
	 * @return
	 * @throws Throwable
	 */
	@RequestMapping(value = "/hqm/inspection/attribute/excelimport/upload")
	@ResponseBody
	public ResponseData excelImport(HttpServletRequest request) throws Throwable {
		ResponseData responseData = new ResponseData();
		MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
		IRequest requestCtx = createRequestContext(request);
		// 获取文件map集合
		Map<String, MultipartFile> fileMap = multipartRequest.getFileMap();
		List<InspectionAttribute> returnList = new ArrayList<InspectionAttribute>();
		for (Map.Entry<String, MultipartFile> entry : fileMap.entrySet()) {
			MultipartFile forModel = entry.getValue();
			returnList.addAll(service.inputDataFromExcel(requestCtx, forModel.getInputStream()));
		}
		responseData.setRows(returnList);
		return responseData;
	}

	/**
	 * 
	 * @description 文件上传
	 * @author tianmin.wang
	 * @date 2019年11月22日 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/hqm/inspection/attribute/filesave")
	@ResponseBody
	public ResponseData fileSave(HttpServletRequest request) {
		ResponseData responseData = new ResponseData();
		List<String> rows = new ArrayList<String>();
		IRequest requestCtx = createRequestContext(request);
		MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;

		// 获取文件map集合
		Map<String, MultipartFile> fileMap = multipartRequest.getFileMap();
		String path = "/u01/hap/resource/inspection_attribute/img/";
		File dir = new File(path);
		if (!dir.exists()) {
			dir.mkdirs();
		}
		List<InspectionAttribute> returnList = new ArrayList<InspectionAttribute>();
		for (Map.Entry<String, MultipartFile> entry : fileMap.entrySet()) {
			rows.add(entry.getValue().getName());
			MultipartFile forModel = entry.getValue();
			File file = new File(path, forModel.getOriginalFilename());
			try {
				returnList.addAll(service.inputDataFromExcel(requestCtx, forModel.getInputStream()));
			} catch (Exception e1) {
				// TODO 解析异常
				responseData.setMessage(e1.getMessage());
				responseData.setSuccess(false);
				return responseData;
			}
			if (file.exists()) {
				file.delete();
			}
			try {
				forModel.transferTo(file);
			} catch (IllegalStateException | IOException e) {
				// TODO 文件保存异常
				responseData.setMessage(e.getMessage());
				responseData.setSuccess(false);
				return responseData;
			}

		}
		responseData.setRows(rows);
		return responseData;
	}
}