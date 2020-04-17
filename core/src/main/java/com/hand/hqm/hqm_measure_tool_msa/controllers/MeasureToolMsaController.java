package com.hand.hqm.hqm_measure_tool_msa.controllers;

import org.springframework.stereotype.Controller;
import com.hand.hap.system.controllers.BaseController;
import com.hand.hap.core.IRequest;
import com.hand.hap.system.dto.ResponseData;
import com.hand.hqm.hqm_measure_tool_msa.dto.MeasureToolMsa;
import com.hand.hqm.hqm_measure_tool_msa.service.IMeasureToolMsaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import javax.servlet.http.HttpServletRequest;
import org.springframework.validation.BindingResult;
import java.util.List;

@Controller
public class MeasureToolMsaController extends BaseController {

	@Autowired
	private IMeasureToolMsaService service;
	/**
     * 页面查询
     * @param dto 查询内容
     * @param page 页码
     * @param pageSize 页大小
     * @param request 请求
     * @return 结果集
     */
	@RequestMapping(value = "/hqm/measure/tool/msa/query")
	@ResponseBody
	public ResponseData query(MeasureToolMsa dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
			@RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
		IRequest requestContext = createRequestContext(request);
		return new ResponseData(service.select(requestContext, dto, page, pageSize));
	}
	/**
     * 提交
     * @param dto 操作数据集
     * @param result 结果参数
     * @param request 请求
     * @return 操作结果
     */
	@RequestMapping(value = "/hqm/measure/tool/msa/submit")
	@ResponseBody
	public ResponseData update(@RequestBody List<MeasureToolMsa> dto, BindingResult result,
			HttpServletRequest request) {
		getValidator().validate(dto, result);
		if (result.hasErrors()) {
			ResponseData responseData = new ResponseData(false);
			responseData.setMessage(getErrorMessage(result, request));
			return responseData;
		}
		IRequest requestCtx = createRequestContext(request);
		for (MeasureToolMsa measureToolMsa : dto) {
			MeasureToolMsa toolMsa = new MeasureToolMsa();
			measureToolMsa.setMsaContent(measureToolMsa.getMsaContent());
			measureToolMsa.setMeasureToolId(measureToolMsa.getMeasureToolId());

			List<MeasureToolMsa> measureToolMsaList = service.select(requestCtx, measureToolMsa, 0, 0);
			if (measureToolMsaList != null && measureToolMsaList.size() > 0) {
				ResponseData responseData = new ResponseData(false);
				responseData.setMessage("分析内容已存在");
				return responseData;
			}
		}
		return new ResponseData(service.batchUpdate(requestCtx, dto));
	}
	/**
     * 删除
     * @param request
     * @param dto
     * @return
     */
	@RequestMapping(value = "/hqm/measure/tool/msa/remove")
	@ResponseBody
	public ResponseData delete(HttpServletRequest request, @RequestBody List<MeasureToolMsa> dto) {
		service.batchDelete(dto);
		return new ResponseData();
	}
}