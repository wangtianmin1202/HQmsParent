package com.hand.hqm.hqm_iqc_inspection_template_h.controllers;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import com.hand.hap.core.IRequest;
import com.hand.hap.system.controllers.BaseController;
import com.hand.hap.system.dto.ResponseData;
import com.hand.hap.system.service.IPromptService;
import com.hand.hap.util.SystemApiMethod;
import com.hand.hqm.hqm_iqc_inspection_template_h.dto.IqcInspectionTemplateH;
import com.hand.hqm.hqm_iqc_inspection_template_h.service.IIqcInspectionTemplateHService;
import com.hand.hqm.hqm_iqc_inspection_template_l.dto.IqcInspectionTemplateL;
import com.hand.hqm.hqm_iqc_inspection_template_l.mapper.IqcInspectionTemplateLMapper;
import com.hand.hqm.hqm_iqc_inspection_template_l.service.IIqcInspectionTemplateLService;

/*
 * created by tianmin.wang on 2019/7/9.
 */
@Controller
public class IqcInspectionTemplateHController extends BaseController {

	@Autowired
	private IIqcInspectionTemplateHService service;
	@Autowired
	private IIqcInspectionTemplateLService iIqcInspectionTemplateLService;
	@Autowired
	IqcInspectionTemplateLMapper iqcInspectionTemplateLMapper;
	@Autowired
	private IPromptService iPromptService;

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
	@RequestMapping(value = "/hqm/iqc/inspection/template/h/query")
	@ResponseBody
	public ResponseData query(IqcInspectionTemplateH dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
			@RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
		IRequest requestContext = createRequestContext(request);
		return new ResponseData(service.myselect(requestContext, dto, page, pageSize));
	}

	/**
	 * 
	 * @description 复制按钮的查询
	 * @author tianmin.wang
	 * @date 2019年11月22日
	 * @param dto
	 * @param page
	 * @param pageSize
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/hqm/iqc/inspection/template/h/selectforCopy")
	@ResponseBody
	public ResponseData selectforCopy(IqcInspectionTemplateH dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
			@RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
		IRequest requestContext = createRequestContext(request);
		return new ResponseData(service.selectforCopy(requestContext, dto, page, pageSize));
	}

	/**
	 * 
	 * @description 保存
	 * @author tianmin.wang
	 * @date 2019年11月22日
	 * @param dto
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/hqm/iqc/inspection/template/h/save")
	@ResponseBody
	public ResponseData save(IqcInspectionTemplateH dto, HttpServletRequest request) {
		IRequest requestContext = createRequestContext(request);
		if (!StringUtils.isEmpty(dto.getStatus()) && !"1".equals(dto.getStatus())) {
			ResponseData responseData = new ResponseData(false);
			responseData.setMessage(SystemApiMethod.getPromptDescription(requestContext, iPromptService,
					"error.iqc.template.save.error1001"));// 模板状态不是编辑中状态 不允许编辑
			return responseData;
		}
		// 校验同工厂，物料，来源类型下有效性为是的检验单模板只能存在一个 add by jy 20190920
		IqcInspectionTemplateH iqcInspectionTemplateH = new IqcInspectionTemplateH();
		List<IqcInspectionTemplateH> iqcInspectionTemplateHList = new ArrayList<IqcInspectionTemplateH>();
		iqcInspectionTemplateH.setPlantId(dto.getPlantId());
		iqcInspectionTemplateH.setItemId(dto.getItemId());
		iqcInspectionTemplateH.setSourceType(dto.getSourceType());
		iqcInspectionTemplateH.setItemEdition(dto.getItemEdition());
		iqcInspectionTemplateHList = service.myselect(requestContext, iqcInspectionTemplateH, 0, 0);

		for (int i = 0; i < iqcInspectionTemplateHList.size(); i++) {
			if (iqcInspectionTemplateHList.get(i).getPlantCode().equals(dto.getPlantCode())
					&& iqcInspectionTemplateHList.get(i).getItemCode().equals(dto.getItemCode())
					&& iqcInspectionTemplateHList.get(i).getSourceType().equals(dto.getSourceType())
					&& dto.getEnableFlag().equals("Y")
					&& iqcInspectionTemplateHList.get(i).getHeaderId().intValue() != dto.getHeaderId().intValue()) {
				ResponseData responseData = new ResponseData(false);
				responseData.setMessage("同工厂，物料，来源类型下有效性为是的检验单模板只能存在一个");
				return responseData;
			}

		}
		return new ResponseData(service.reSave(requestContext, dto));
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
	@RequestMapping(value = "/hqm/iqc/inspection/template/h/submit")
	@ResponseBody
	public ResponseData update(@RequestBody List<IqcInspectionTemplateH> dto, BindingResult result,
			HttpServletRequest request) {
		getValidator().validate(dto, result);
		if (result.hasErrors()) {
			ResponseData responseData = new ResponseData(false);
			responseData.setMessage(getErrorMessage(result, request));
			return responseData;
		}
		IRequest requestCtx = createRequestContext(request);

		try {
			return new ResponseData(service.versionNumberBatchUpdate(requestCtx, dto));
		} catch (Exception e) {
			ResponseData responseData = new ResponseData(false);
			if (e.getMessage().contains("ibatis")) {
				responseData.setMessage("违反唯一性");
			} else {
				responseData.setMessage(e.getMessage());
			}
			return responseData;
		}
	}

	/*
	 * @param null
	 * 
	 * @return
	 * 
	 * @author tianmin.wang
	 * 
	 * @date 2019/7/17
	 * 
	 * @description 发布功能，修改status字段
	 **/
	@RequestMapping(value = "/hqm/iqc/inspection/template/h/issue")
	@ResponseBody
	public ResponseData operationIssue(@RequestBody List<IqcInspectionTemplateH> dto, HttpServletRequest request) {
		IRequest requestCtx = createRequestContext(request);
		ResponseData responseData = new ResponseData();
		try {
			service.updateStatus(requestCtx, dto);
		} catch (Exception e) {
			responseData.setSuccess(false);
			responseData.setMessage(e.getMessage());
		}
		return responseData;
	}

	/**
	 * 
	 * @description 发布
	 * @author tianmin.wang
	 * @date 2019年11月22日
	 * @param dto
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/hqm/iqc/inspection/template/h/l/issue")
	@ResponseBody
	public ResponseData operationIssue_l(@RequestBody IqcInspectionTemplateH dto, HttpServletRequest request) {
		IRequest requestCtx = createRequestContext(request);
		ResponseData responseData = new ResponseData();
		try {
			service.updateStatus_l(requestCtx, dto);
		} catch (Exception e) {
			responseData.setSuccess(false);
			responseData.setMessage(e.getMessage());
		}
		return responseData;
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
	@RequestMapping(value = "/hqm/iqc/inspection/template/h/remove")
	@ResponseBody
	public ResponseData delete(HttpServletRequest request, @RequestBody List<IqcInspectionTemplateH> dto) {
		service.batchDelete(dto);
		dto.forEach(dao -> {
			IqcInspectionTemplateL temlSearch = new IqcInspectionTemplateL();
			temlSearch.setHeaderId(dao.getHeaderId());
			iIqcInspectionTemplateLService.batchDelete(iqcInspectionTemplateLMapper.select(temlSearch));
			// iIqcInspectionTemplateLService
		});
		return new ResponseData();
	}

	/**
	 * 
	 * @description excel导入
	 * @author tianmin.wang
	 * @date 2019年11月22日
	 * @param dto
	 * @param request
	 * @param response
	 * @throws Throwable
	 */
	@RequestMapping(value = "/hqm/iqc/inspection/template/h/exportexcel")
	@ResponseBody
	public void exportExcel(@RequestParam String dto, HttpServletRequest request, HttpServletResponse response)
			throws Throwable {
		ObjectMapper om = new ObjectMapper();
		IqcInspectionTemplateH iqcInspectionTemplateH = om.readValue(dto, IqcInspectionTemplateH.class);
		IRequest requestContext = createRequestContext(request);
		service.exportExcel(iqcInspectionTemplateH, requestContext, request, response);
	}

	/**
	 * 
	 * @description 提交
	 * @author tianmin.wang
	 * @date 2020年1月3日
	 * @param dto
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/hqm/iqc/inspection/template/h/commit")
	@ResponseBody
	public ResponseData commit(@RequestBody List<IqcInspectionTemplateH> dto, HttpServletRequest request) {
		IRequest requestCtx = createRequestContext(request);
		ResponseData responseData = new ResponseData();
		try {
			service.commit(requestCtx, dto);
		} catch (Exception e) {
			responseData.setSuccess(false);
			responseData.setMessage(e.getMessage());
		}
		return responseData;
	}

	/**
	 * 
	 * @description 审核
	 * @author tianmin.wang
	 * @date 2020年1月3日
	 * @param dto
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/hqm/iqc/inspection/template/h/audit")
	@ResponseBody
	public ResponseData audit(@RequestBody List<IqcInspectionTemplateH> dto, HttpServletRequest request) {
		IRequest requestCtx = createRequestContext(request);
		ResponseData responseData = new ResponseData();
		try {
			service.audit(requestCtx, dto);
		} catch (Exception e) {
			responseData.setSuccess(false);
			responseData.setMessage(e.getMessage());
		}
		return responseData;
	}

	/**
	 * 
	 * @description 退回
	 * @author tianmin.wang
	 * @date 2020年1月3日
	 * @param dto
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/hqm/iqc/inspection/template/h/reback")
	@ResponseBody
	public ResponseData reback(@RequestBody List<IqcInspectionTemplateH> dto, HttpServletRequest request) {
		IRequest requestCtx = createRequestContext(request);
		ResponseData responseData = new ResponseData();
		try {
			service.reback(requestCtx, dto);
		} catch (Exception e) {
			responseData.setSuccess(false);
			responseData.setMessage(e.getMessage());
		}
		return responseData;
	}

	/**
	 * 
	 * @description VTP模板创建查询
	 * @author tianmin.wang
	 * @date 2020年2月21日
	 * @param dto
	 * @param page
	 * @param pageSize
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/hqm/iqc/inspection/template/h/vtp/query")
	@ResponseBody
	public ResponseData vtpQuery(IqcInspectionTemplateH dto, HttpServletRequest request) {
		ResponseData res = new ResponseData();
		try {
			res.setRows(service.vtpQuery(dto, request));
		} catch (Exception e) {
			res.setSuccess(false);
			res.setMessage(e.getMessage());
		}
		return res;
	}

	@RequestMapping(value = "/hqm/iqc/inspection/template/h/vtp/create/save")
	@ResponseBody
	public ResponseData vtpCreateSave(@RequestBody List<IqcInspectionTemplateL> dto, HttpServletRequest request) {
		ResponseData res = new ResponseData();
		try {
			service.vtpCreateSave(dto, request);
		} catch (Exception e) {
			res.setSuccess(false);
			res.setMessage(e.getMessage());
		}
		return res;
	}

	/**
	 * excel数据导入 IQC
	 * 
	 * @author kai.li
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/hqm/iqc/inspection/template/h/excelimport")
	@ResponseBody
	public ResponseData excelImport(HttpServletRequest request) {
		IRequest requestContext = createRequestContext(request);
		ResponseData responseData = new ResponseData();

		MultipartResolver resolver = new CommonsMultipartResolver(request.getSession().getServletContext());
		MultipartHttpServletRequest multipartRequest = resolver.resolveMultipart(request);

		// 获取文件map集合
		Map<String, MultipartFile> fileMap = multipartRequest.getFileMap();
		for (Map.Entry<String, MultipartFile> entry : fileMap.entrySet()) {
			MultipartFile forModel = entry.getValue();
			try {
				String mainType = "IQC";
				responseData = service.inputDataFromExcel(request, requestContext, forModel.getInputStream(), mainType);
			} catch (Exception e1) {
				// TODO 解析异常
				responseData.setMessage(e1.getMessage());
				responseData.setSuccess(false);
				return responseData;
			}
		}
		return responseData;
	}

	/**
	 * excel数据导入 FQC
	 * 
	 * @author kai.li
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/hqm/iqc/inspection/template/h/excelimportFqc")
	@ResponseBody
	public ResponseData excelImportFqc(HttpServletRequest request) {
		IRequest requestContext = createRequestContext(request);
		ResponseData responseData = new ResponseData();

		MultipartResolver resolver = new CommonsMultipartResolver(request.getSession().getServletContext());
		MultipartHttpServletRequest multipartRequest = resolver.resolveMultipart(request);

		// 获取文件map集合
		Map<String, MultipartFile> fileMap = multipartRequest.getFileMap();
		for (Map.Entry<String, MultipartFile> entry : fileMap.entrySet()) {
			MultipartFile forModel = entry.getValue();
			try {
				String mainType = "FQC";
				responseData = service.inputDataFromExcel(request, requestContext, forModel.getInputStream(), mainType);
			} catch (Exception e1) {
				// TODO 解析异常
				responseData.setMessage(e1.getMessage());
				responseData.setSuccess(false);
				return responseData;
			}
		}
		return responseData;
	}

	/**
	 * 审批时展示历史变更记录
	 * 
	 * @author kai.li
	 * @param request
	 * @param response
	 * @return
	 * @throws ParseException
	 */
	@RequestMapping("/hqm/iqc/inspection/template/h/getHisContent")
	@ResponseBody
	public ResponseData getHisContent(HttpServletRequest request) {
		IRequest requestContext = createRequestContext(request);
		return new ResponseData(service.getHisContent(requestContext, Float.parseFloat(request.getParameter("headId")),
				request.getParameter("startTime")));
	}

}