package com.hand.hqm.hqm_iqc_inspection_h.controllers;

import org.springframework.stereotype.Controller;
import com.hand.hap.system.controllers.BaseController;
import com.hand.hap.core.IRequest;
import com.hand.hap.system.dto.ResponseData;
import com.hand.hap.util.SpringContextUtil;
import com.hand.hqm.hqm_iqc_inspection_h.dto.IqcInspectionH;
import com.hand.hqm.hqm_iqc_inspection_h.service.IIqcInspectionHService;
import com.hand.hqm.hqm_iqc_inspection_l.dto.IqcInspectionL;

import org.codehaus.jackson.type.TypeReference;
import org.codehaus.jackson.map.ObjectMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.RequestContextUtils;

import javax.servlet.http.HttpServletRequest;
import org.springframework.validation.BindingResult;

import java.util.ArrayList;
import java.util.List;

/*
 * created by tianmin.wang on 2019/7/19.
 */
@Controller
public class IqcInspectionHController extends BaseController {

	@Autowired
	private IIqcInspectionHService service;

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
	@RequestMapping(value = "/hqm/iqc/inspection/h/query")
	@ResponseBody
	public ResponseData query(IqcInspectionH dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
			@RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
		IRequest requestContext = createRequestContext(request);
		return new ResponseData(service.select(requestContext, dto, page, pageSize));
	}

	/**
	 * 检验单综合查询
	 * 
	 * @param dto
	 * @param page
	 * @param pageSize
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/hqm/iqc/inspection/h/qmsQuery")
	@ResponseBody
	public ResponseData qmsQuery(IqcInspectionH dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
			@RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
		IRequest requestContext = createRequestContext(request);
		return new ResponseData(service.qmsQueryAll(requestContext, dto, page, pageSize));
	}

	/**
	 * 
	 * @description 条件统计
	 * @date 2019年11月22日
	 * @param dto
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/hqm/iqc/inspection/h/limitquery")
	@ResponseBody
	public ResponseData queryLimit(IqcInspectionH dto, HttpServletRequest request) {
		IRequest requestContext = createRequestContext(request);
		return new ResponseData(service.getLimitCount(dto));
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
	@RequestMapping(value = "/hqm/iqc/inspection/h/submit")
	@ResponseBody
	public ResponseData update(@RequestBody List<IqcInspectionH> dto, BindingResult result,
			HttpServletRequest request) {
		getValidator().validate(dto, result);
		if (result.hasErrors()) {
			ResponseData responseData = new ResponseData(false);
			responseData.setMessage(getErrorMessage(result, request));
			return responseData;
		}
		IRequest requestCtx = createRequestContext(request);
		return new ResponseData(service.batchUpdate(requestCtx, dto));
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
	@RequestMapping(value = "/hqm/iqc/inspection/h/remove")
	@ResponseBody
	public ResponseData delete(HttpServletRequest request, @RequestBody List<IqcInspectionH> dto) {
		service.batchDelete(dto);
		return new ResponseData();
	}

	/**
	 * 
	 * @description 查询
	 * @author tianmin.wang
	 * @date 2019年11月22日
	 * @param dto
	 * @param page
	 * @param pageSize
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/hqm/iqc/inspection/h/select")
	@ResponseBody
	public ResponseData select(IqcInspectionH dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
			@RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
		IRequest requestContext = createRequestContext(request);
		return new ResponseData(service.selectByNumber(requestContext, dto, page, pageSize));
	}

	/**
	 * 
	 * @description 提交检验单 请求入口
	 * @author tianmin.wang
	 * @date 2019年11月22日
	 * @param dto
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/hqm/iqc/inspection/h/commit")
	@ResponseBody
	public ResponseData commit(IqcInspectionH dto, HttpServletRequest request) {
		ObjectMapper mapper = new ObjectMapper();// 定义 org.codehaus.jackson
		List<IqcInspectionL> lineList = new ArrayList<IqcInspectionL>();
		ResponseData responseData = new ResponseData();
		try {
			lineList = mapper.readValue(dto.getLineList(), new TypeReference<List<IqcInspectionL>>() {
			});
		} catch (Exception ex) {
			responseData.setSuccess(false);
			responseData.setMessage("JSON格式转换失败" + ex.getMessage());
		}

		IRequest requestContext = createRequestContext(request);
		try {
			responseData = service.commitInspection(dto, lineList, request, requestContext);
		} catch (Exception e) {
			responseData.setSuccess(false);
			responseData.setMessage(e.getMessage());
		}
		return responseData;
	}

	/**
	 * 
	 * @description 保存/暂挂 操作
	 * @author tianmin.wang
	 * @date 2019年11月22日
	 * @param dto
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/hqm/iqc/inspection/h/pause")
	@ResponseBody
	public ResponseData pause(IqcInspectionH dto, HttpServletRequest request) {
		ObjectMapper mapper = new ObjectMapper();// 定义 org.codehaus.jackson
		List<IqcInspectionL> lineList = new ArrayList<IqcInspectionL>();
		ResponseData responseData = new ResponseData();
		try {
			lineList = mapper.readValue(dto.getLineList(), new TypeReference<List<IqcInspectionL>>() {
			});
		} catch (Exception ex) {
			responseData.setSuccess(false);
			responseData.setMessage("JSON格式转换失败" + ex.getMessage());
			return responseData;
		}

		IRequest requestContext = createRequestContext(request);
		try {
			responseData = service.pauseInspection(dto, lineList, request);
		} catch (Exception e) {
			responseData.setSuccess(false);
			responseData.setMessage(e.getMessage());
		}
		return responseData;
	}

	/**
	 * tianmin.wang 审核
	 * 
	 * @param dto
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/hqm/iqc/inspection/h/audit")
	@ResponseBody
	public ResponseData audit(IqcInspectionH dto, HttpServletRequest request) {
//		ObjectMapper mapper = new ObjectMapper();// 定义 org.codehaus.jackson
		List<IqcInspectionL> lineList = new ArrayList<IqcInspectionL>();
		ResponseData responseData = new ResponseData();
//		try {
//			lineList = mapper.readValue(dto.getLineList(), new TypeReference<List<IqcInspectionL>>() {
//			});
//		} catch (Exception ex) {
//			responseData.setSuccess(false);
//			responseData.setMessage("JSON格式转换失败" + ex.getMessage());
//			return responseData;
//		}

		IRequest requestContext = createRequestContext(request);
		try {
			responseData = service.auditInspection(dto, lineList, request);
		} catch (Exception e) {
			responseData.setSuccess(false);
			responseData.setMessage(e.getMessage());
		}
		return responseData;
	}

	/**
	 * tianmin.wang 检验单管理-新建检验单
	 * 
	 * @param dto
	 * @param request
	 * @return 20190726
	 */
	@RequestMapping(value = "/hqm/iqc/inspection/h/addnew")
	@ResponseBody
	public ResponseData addNewInspection(IqcInspectionH dto, HttpServletRequest request) {
		ResponseData responseData = new ResponseData();

		IRequest requestContext = createRequestContext(request);

		try {
			responseData = service.addNewInspection(dto, requestContext, request);
			//创建成功后，发起IQC报验申请
			//service.samplingToWms(responseData);
		} catch (Exception e) {
			responseData.setSuccess(false);
			responseData.setMessage(e.getMessage());
		}
		return responseData;
	}

	/**
	 * 文件上传
	 * 
	 * @param file
	 * @param headerId
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/hqm/iqc/inspection/h/upload")
	@ResponseBody
	public ResponseData upload(@RequestParam("file") MultipartFile file, @RequestParam("headerId") String headerId,
			HttpServletRequest request) {

		return new ResponseData();
	}

	/**
	 * 不合格处理单关联用 add by Jiang Ruifu 2019/7/26
	 */
	@RequestMapping(value = "/hqm/iqc/inspection/h/selectforother")
	@ResponseBody
	public ResponseData selectforother(IqcInspectionH dto, HttpServletRequest request) {
		IRequest requestContext = createRequestContext(request);
		return new ResponseData(service.selectFornon(requestContext, dto));
	}

}
