package com.hand.hqm.hqm_msa_check_plan.controllers;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.hand.hap.core.IRequest;
import com.hand.hap.system.controllers.BaseController;
import com.hand.hap.system.dto.ResponseData;
import com.hand.hqm.hqm_measure_tool.dto.MeasureTool;
import com.hand.hqm.hqm_msa_check_plan.dto.MetCheckPlanLine;
import com.hand.hqm.hqm_msa_check_plan.service.IMetCheckPlanLineService;

@Controller
public class MetCheckPlanLineController extends BaseController {

	@Autowired
	private IMetCheckPlanLineService service;
	/**
     * 页面查询
     * @param dto 查询内容
     * @param page 页码
     * @param pageSize 页大小
     * @param request 请求
     * @return 结果集
     */
	@RequestMapping(value = "/hqm/met/check/plan/line/query")
	@ResponseBody
	public ResponseData query(MetCheckPlanLine dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
			@RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
		//IRequest requestContext = createRequestContext(request);
		return new ResponseData(service.selectCheckPlanLine(dto));
	}
	/**
     * 页面查询
     * @param dto 查询内容
     * @param page 页码
     * @param pageSize 页大小
     * @param request 请求
     * @return 结果集
     */
	@RequestMapping(value = "/hqm/met/check/plan/line/select")
	@ResponseBody
	public ResponseData select(MetCheckPlanLine dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
			@RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
		IRequest requestContext = createRequestContext(request);
		return new ResponseData(service.query(requestContext,dto,page,pageSize));
	}
	/**
     * 提交
     * @param dto 操作数据集
     * @param result 结果参数
     * @param request 请求
     * @return 操作结果
     */
	@RequestMapping(value = "/hqm/met/check/plan/line/submit")
	@ResponseBody
	public ResponseData update(@RequestBody List<MetCheckPlanLine> dto, BindingResult result,
			HttpServletRequest request) {
		IRequest requestCtx = createRequestContext(request);
		getValidator().validate(dto, result);
		if (result.hasErrors()) {
			ResponseData responseData = new ResponseData(false);
			responseData.setMessage(getErrorMessage(result, request));
			return responseData;
		}

		return service.update(dto, requestCtx);
	}
	/**
     * 删除
     * @param request
     * @param dto
     * @return
     */
	@RequestMapping(value = "/hqm/met/check/plan/line/remove")
	@ResponseBody
	public ResponseData delete(HttpServletRequest request, @RequestBody List<MetCheckPlanLine> dto) {
		service.batchDelete(dto);
		return new ResponseData();
	}
	/**
	 * 上传
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/hqm/met/check/plan/line/fileupload/upload")
	@ResponseBody
	public ResponseData fileUpload(HttpServletRequest request) {

		ResponseData responseData = new ResponseData();

		IRequest requestCtx = createRequestContext(request);
		try {
			responseData = service.fileUpload(requestCtx, request);
		} catch (IllegalStateException | IOException e) {
			// TODO Auto-generated catch block
			responseData.setMessage(e.getMessage());
			responseData.setSuccess(false);
		}

		return responseData;

	}
	/**
     * 编辑
     * @param request
     * @param dto
     * @return
     */
    @RequestMapping(value = "/hqm/met/check/plan/line/updateData")
    @ResponseBody
    public ResponseData updateData(HttpServletRequest request,MetCheckPlanLine dto){
    	IRequest requestContext = createRequestContext(request);
        ResponseData responseData = new ResponseData();
        try {
        	service.updateData(requestContext,dto);
        }catch(RuntimeException e) {
        	e.printStackTrace();
        	responseData.setMessage(e.getMessage());
        	responseData.setSuccess(false);
        }
        return responseData;
    }
}