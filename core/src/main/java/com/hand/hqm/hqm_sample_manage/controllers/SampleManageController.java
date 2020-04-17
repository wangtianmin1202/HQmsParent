package com.hand.hqm.hqm_sample_manage.controllers;

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
import com.hand.hqm.hqm_sample_manage.dto.SampleManage;
import com.hand.hqm.hqm_sample_manage.service.ISampleManageService;
import com.hand.utils.recording_changes_basic_data.dto.ChangesBasicData;
import com.hand.utils.recording_changes_basic_data.service.IChangesBasicDataService;

@Controller
public class SampleManageController extends BaseController {

	@Autowired
	private ISampleManageService service;
	
	@Autowired
    private IChangesBasicDataService iChangesBasicDataService;
	/**
     * 页面查询
     * @param dto 查询内容
     * @param page 页码
     * @param pageSize 页大小
     * @param request 请求
     * @return 结果集
     */
	@RequestMapping(value = "/hqm/sample/manage/query")
	@ResponseBody
	public ResponseData query(SampleManage dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
			@RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
		IRequest requestContext = createRequestContext(request);
		return new ResponseData(service.querySampleManage(dto));
	}
	/**
     * 提交
     * @param dto 操作数据集
     * @param result 结果参数
     * @param request 请求
     * @return 操作结果
     */
	@RequestMapping(value = "/hqm/sample/manage/submit")
	@ResponseBody
	public ResponseData update(@RequestBody List<SampleManage> dto, BindingResult result, HttpServletRequest request) {
		IRequest requestCtx = createRequestContext(request);
		getValidator().validate(dto, result);
		if (result.hasErrors()) {
			ResponseData responseData = new ResponseData(false);
			responseData.setMessage(getErrorMessage(result, request));
			return responseData;
		}
		for (int i = 0; i < dto.size(); i++) {
			if ("1".equals(dto.get(i).getSampleType())) {// 抽样类型：使用抽样方案
				if ("0".equals(dto.get(i).getSampleProcedureType())||"1".equals(dto.get(i).getSampleProcedureType())) {// 抽样标准： C=0 ; GB2828
					if ("".equals(dto.get(i).getAcceptanceQualityLimit())||dto.get(i).getAcceptanceQualityLimit()==null) {
						ResponseData responseData = new ResponseData(false);
						responseData.setMessage("请填写AQL值!");
						return responseData;
					}
				}
				if ("0".equals(dto.get(i).getSampleProcedureType())) {// 抽样标准： GB2828
					if ("".equals(dto.get(i).getInspectionLevels())||dto.get(i).getInspectionLevels()==null) {
						ResponseData responseData = new ResponseData(false);
						responseData.setMessage("请填写检验水平!");
						return responseData;
					}
				}
			}
			
			ChangesBasicData changesBasicData = new ChangesBasicData("HQM_SAMPLE_MANAGE",dto.get(i),requestCtx.getUserName(),dto.get(i).get__status());
	        iChangesBasicDataService.insertSelective(requestCtx,changesBasicData);
		}
		return new ResponseData(service.batchUpdate(requestCtx, dto));
	}
	/**
     * 删除
     * @param request
     * @param dto
     * @return
     */
	@RequestMapping(value = "/hqm/sample/manage/remove")
	@ResponseBody
	public ResponseData delete(HttpServletRequest request, @RequestBody List<SampleManage> dto) {
		IRequest requestCtx = createRequestContext(request);
		for (int i = 0; i < dto.size(); i++) {
			ChangesBasicData changesBasicData = new ChangesBasicData("HQM_SAMPLE_MANAGE",dto.get(i),requestCtx.getUserName(),dto.get(i).get__status());
	        iChangesBasicDataService.insertSelective(requestCtx,changesBasicData);
		}
		service.batchDelete(dto);
		return new ResponseData();
	}
}