package com.hand.spc.test.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
import com.hand.hap.system.dto.Code;
import com.hand.hap.system.dto.CodeValue;
import com.hand.hap.system.dto.ResponseData;
import com.hand.hap.system.mapper.CodeMapper;
import com.hand.hap.system.mapper.CodeValueMapper;
import com.hand.hap.system.service.ICodeService;
import com.hand.hap.system.service.ILovService;
import com.hand.spc.pspc_classify.dto.Classify;
import com.hand.spc.pspc_classify.service.IClassifyService;
import com.hand.spc.repository.mapper.GetCodeValuesMapper;

@Controller
public class PspcTestController extends BaseController {

	@Autowired
	private IClassifyService service;

	@Autowired
	private ILovService lovService;

	@Autowired
	private ICodeService codeService;

	@Autowired
	private CodeMapper codeMapper;

	@Autowired
	private CodeValueMapper codeValueMapper;

	@Autowired
	private GetCodeValuesMapper getCodeValuesMapper;

	@SuppressWarnings({ "unchecked", "unused" })
	@RequestMapping(value = "/pspc/test/query")
	@ResponseBody
	public ResponseData query(Classify dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
			@RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
		IRequest requestContext = createRequestContext(request);

		// Lov取值
		/*
		 * Plant plant = new Plant(); List<Plant> lovs = new ArrayList<Plant>(); lovs =
		 * (List<Plant>) lovService.selectDatas(null, "LOV_PLANT", plant, 0, 0);
		 * 
		 * for (Plant plant1 : lovs) { System.out.println(plant1);
		 * System.out.println(plant1.getPlantCode());
		 * System.out.println(plant1.getDescriptions()); }
		 */

		// 值集

		// List<CodeValue> codeValues = new ArrayList<CodeValue>();
		// codeValues=codeService.selectCodeValuesByCodeName(requestContext,
		// "PSPC.MESSAGE.TYPE");

		Code code = new Code();
		List<Code> codes = new ArrayList<Code>();
		code.setCode("PSPC.MESSAGE.TYPE");
		code = codeMapper.selectOne(code);

		CodeValue codeValue = new CodeValue();
		List<CodeValue> codeValues = new ArrayList<CodeValue>();
		codeValue.setCodeId(code.getCodeId());
		// codeValues=codeValueMapper.select(codeValue);

		codeValues = getCodeValuesMapper.getCodeValues("PSPC.MESSAGE.TYPE");

		for (CodeValue codeValue1 : codeValues) {
			System.out.println(codeValue1.getValue());
			System.out.println(codeValue1.getMeaning());

		}

		for (int i = 0; i < 3; i++) {
			System.out.println(codeValues.stream().filter(codeValue1 -> "MESSAGE_TYPE_2".equals(codeValue1.getValue())).collect(Collectors.toList()).get(0).getMeaning());
		}

		return new ResponseData(service.select(requestContext, dto, page, pageSize));
	}

	/**
	 * @Author han.zhang
	 * @Description 分类组下的分类项查询
	 * @Date 9:48 2019/8/7
	 * @Param [dto, page, pageSize, request]
	 */
	@RequestMapping(value = "/pspc/test/query/classify")
	@ResponseBody
	public ResponseData queryClassify(Classify dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
			@RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
		IRequest requestContext = createRequestContext(request);
		return new ResponseData(service.select(requestContext, dto, page, pageSize));
	}

	@RequestMapping(value = "/pspc/test/submit")
	@ResponseBody
	public ResponseData update(@RequestBody List<Classify> dto, BindingResult result, HttpServletRequest request) {
		getValidator().validate(dto, result);
		if (result.hasErrors()) {
			ResponseData responseData = new ResponseData(false);
			responseData.setMessage(getErrorMessage(result, request));
			return responseData;
		}
		IRequest requestCtx = createRequestContext(request);
		return new ResponseData(service.batchUpdate(requestCtx, dto));
	}

	@RequestMapping(value = "/pspc/test/remove")
	@ResponseBody
	public ResponseData delete(HttpServletRequest request, @RequestBody List<Classify> dto) {
		service.batchDelete(dto);
		return new ResponseData();
	}
}