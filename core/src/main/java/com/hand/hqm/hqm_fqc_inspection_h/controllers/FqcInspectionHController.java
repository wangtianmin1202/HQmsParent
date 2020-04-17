package com.hand.hqm.hqm_fqc_inspection_h.controllers;

import org.springframework.stereotype.Controller;
import com.hand.hap.system.controllers.BaseController;
import com.hand.hap.core.IRequest;
import com.hand.hap.system.dto.ResponseData;
import com.hand.hqm.hqm_fqc_inspection_h.dto.FqcInspectionH;
import com.hand.hqm.hqm_fqc_inspection_h.service.IFqcInspectionHService;
import com.hand.hqm.hqm_fqc_inspection_l.dto.FqcInspectionL;
import com.hand.hqm.hqm_iqc_inspection_h.dto.IqcInspectionH;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import org.springframework.validation.BindingResult;

import java.util.ArrayList;
import java.util.List;

@Controller
public class FqcInspectionHController extends BaseController {

	@Autowired
	private IFqcInspectionHService service;

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
	@RequestMapping(value = "/hqm/fqc/inspection/h/query", method = { RequestMethod.POST })
	@ResponseBody
	public ResponseData query(FqcInspectionH dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
			@RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
		IRequest requestContext = createRequestContext(request);
		return new ResponseData(service.select(requestContext, dto, page, pageSize));
	}

	/**
	 * 
	 * @description
	 * @date 2019年11月22日
	 * @param dto
	 * @param page
	 * @param pageSize
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/hqm/fqc/inspection/h/qmsQuery")
	@ResponseBody
	public ResponseData qmsQuery(FqcInspectionH dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
			@RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
		IRequest requestContext = createRequestContext(request);
		return new ResponseData(service.qmsQuery(requestContext, dto, page, pageSize));
	}

	/**
	 * 
	 * @description 提交保存
	 * @author tianmin.wang
	 * @date 2019年11月22日
	 * @param dto
	 * @param result
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/hqm/fqc/inspection/h/submit")
	@ResponseBody
	public ResponseData update(@RequestBody List<FqcInspectionH> dto, BindingResult result,
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
	@RequestMapping(value = "/hqm/fqc/inspection/h/remove")
	@ResponseBody
	public ResponseData delete(HttpServletRequest request, @RequestBody List<FqcInspectionH> dto) {
		service.batchDelete(dto);
		return new ResponseData();
	}

	/**
	 * 
	 * @description 查询
	 * @author tianmin.wang
	 * @date 2019年11月22日
	 * @param dto
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/hqm/fqc/inspection/h/select")
	@ResponseBody
	public ResponseData select(FqcInspectionH dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
			@RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
		IRequest requestContext = createRequestContext(request);
		return new ResponseData(service.reSelect(requestContext, dto, page, pageSize));
	}

	/**
	 * 
	 * @description 数据的 提交 操作请求入口
	 * @author tianmin.wang
	 * @date 2019年11月22日
	 * @param dto
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/hqm/fqc/inspection/h/commit")
	@ResponseBody
	public ResponseData commit(FqcInspectionH dto, HttpServletRequest request) {
		ObjectMapper mapper = new ObjectMapper();// 定义 org.codehaus.jackson
		List<FqcInspectionL> lineList = new ArrayList<FqcInspectionL>();
		ResponseData responseData = new ResponseData();
		try {
			lineList = mapper.readValue(dto.getLineList(), new TypeReference<List<FqcInspectionL>>() {
			});
		} catch (Exception ex) {
			responseData.setSuccess(false);
			responseData.setMessage("JSON格式转换失败" + ex.getMessage());
			return responseData;
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
	 * @description 暂挂/保存 操作
	 * @author tianmin.wang
	 * @date 2019年11月22日
	 * @param dto
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/hqm/fqc/inspection/h/pause")
	@ResponseBody
	public ResponseData pause(FqcInspectionH dto, HttpServletRequest request) {
		ObjectMapper mapper = new ObjectMapper();// 定义 org.codehaus.jackson
		List<FqcInspectionL> lineList = new ArrayList<FqcInspectionL>();
		ResponseData responseData = new ResponseData();
		try {
			lineList = mapper.readValue(dto.getLineList(), new TypeReference<List<FqcInspectionL>>() {
			});
		} catch (Exception ex) {
			responseData.setSuccess(false);
			responseData.setMessage("JSON格式转换失败" + ex.getMessage());
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
	 * tianmin.wang 20190730 审核
	 * 
	 * @param dto
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/hqm/fqc/inspection/h/audit")
	@ResponseBody
	public ResponseData audit(FqcInspectionH dto, HttpServletRequest request) {
//		ObjectMapper mapper = new ObjectMapper();// 定义 org.codehaus.jackson
		List<FqcInspectionL> lineList = new ArrayList<FqcInspectionL>();
		ResponseData responseData = new ResponseData();
//		try {
//			lineList = mapper.readValue(dto.getLineList(), new TypeReference<List<FqcInspectionL>>() {
//			});
//		} catch (Exception ex) {
//			responseData.setSuccess(false);
//			responseData.setMessage("JSON格式转换失败" + ex.getMessage());
//		}
		try {
			service.auditInspection(dto, lineList, request);
		} catch (Exception e) {
			responseData.setSuccess(false);
			responseData.setMessage(e.getMessage());
		}
		return responseData;
	}

	/**
	 * 
	 * @description 检验单管理-新建检验单
	 * @author tianmin.wang
	 * @date 2019年11月21日
	 * @param dto
	 * @param request
	 * @return
	 * @throws Throwable
	 */
	@RequestMapping(value = "/hqm/fqc/inspection/h/addnew")
	@ResponseBody
	public ResponseData addNewInspection(FqcInspectionH dto, HttpServletRequest request) throws Throwable {
		ResponseData responseData = new ResponseData();

		IRequest requestContext = createRequestContext(request);
		try {
			responseData = service.addNewInspection(dto, requestContext, request);
			// 检验单生成后，调用WMS接口反馈
			// service.samplingFeedback(responseData);
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
	@RequestMapping(value = "/hqm/fqc/inspection/h/upload")
	@ResponseBody
	public ResponseData upload(@RequestParam("file") MultipartFile file, @RequestParam("headerId") String headerId,
			HttpServletRequest request) {
		return new ResponseData();
	}

	/**
	 * 
	 * @description 加抽入口
	 * @author tianmin.wang
	 * @date 2019年11月21日
	 * @param dto
	 * @param request
	 * @return
	 * @throws Throwable
	 */
	@RequestMapping(value = "/hqm/fqc/inspection/h/addsample")
	@ResponseBody
	public ResponseData addSample(FqcInspectionH dto, HttpServletRequest request) throws Throwable {
		ResponseData responseData = new ResponseData();
		try {
			service.addSample(dto, request);
		} catch (Exception e) {
			responseData.setSuccess(false);
			responseData.setMessage(e.getMessage());
		}
		return responseData;
	}

	/**
	 * 
	 * @description 条件统计
	 * @date 2019年11月22日
	 * @param dto
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/hqm/fqc/inspection/h/limitquery")
	@ResponseBody
	public ResponseData queryLimit(FqcInspectionH dto, HttpServletRequest request) {
		return new ResponseData(service.getLimitCount(dto));
	}
}