package com.hand.hqm.hqm_pqc_inspection_c.controllers;

import org.springframework.stereotype.Controller;
import com.hand.hap.system.controllers.BaseController;
import com.hand.hap.core.IRequest;
import com.hand.hap.system.dto.ResponseData;
import com.hand.hqm.hqm_fqc_inspection_l.dto.FqcInspectionL;
import com.hand.hqm.hqm_iqc_inspection_h.dto.IqcInspectionH;
import com.hand.hqm.hqm_iqc_inspection_l.dto.IqcInspectionL;
import com.hand.hqm.hqm_pqc_inspection_c.dto.PqcInspectionC;
import com.hand.hqm.hqm_pqc_inspection_c.service.IPqcInspectionCService;
import com.hand.hqm.hqm_pqc_inspection_d.dto.PqcInspectionD;
import com.hand.hqm.hqm_pqc_inspection_d.mapper.PqcInspectionDMapper;
import com.hand.hqm.hqm_pqc_inspection_l.dto.PqcInspectionL;
import com.hand.hqm.hqm_pqc_inspection_l.mapper.PqcInspectionLMapper;

import org.apache.commons.lang3.StringUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import javax.servlet.http.HttpServletRequest;
import org.springframework.validation.BindingResult;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class PqcInspectionCController extends BaseController {

	@Autowired
	private IPqcInspectionCService service;
	@Autowired
	private PqcInspectionDMapper pqcInspectionDMapper;
	@Autowired
	private PqcInspectionLMapper pqcInspectionLMapper;
	
	/**
	 * 基础查询
	 * @description
	 * @author tianmin.wang
	 * @date 2019年11月22日 
	 * @param dto
	 * @param page
	 * @param pageSize
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/hqm/pqc/inspection/c/query")
	@ResponseBody
	public ResponseData query(PqcInspectionC dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
			@RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
		IRequest requestContext = createRequestContext(request);
		List<PqcInspectionC> iqcInspectionLList = new ArrayList<PqcInspectionC>();
		iqcInspectionLList = service.myselect(requestContext, dto, page, pageSize);
		if ("2".equals(getLine(dto))) {
			iqcInspectionLList = iqcInspectionLList.stream()
					.sorted(Comparator.comparing(PqcInspectionC::getAttributeInspectionRes)).collect(Collectors.toList());
		}
		for (PqcInspectionC each : iqcInspectionLList) {
			PqcInspectionD search = new PqcInspectionD();
			search.setConnectId(each.getConnectId());
			List<PqcInspectionD> iqcInspectionDList = new ArrayList<PqcInspectionD>();
			iqcInspectionDList = pqcInspectionDMapper.select(search);
			String inspectionHistory = "";
			for (PqcInspectionD iqcInspectionD : iqcInspectionDList) {
				inspectionHistory += StringUtils.isEmpty(iqcInspectionD.getData()) ? ""
						: iqcInspectionD.getData() + ",";
			}
			if (inspectionHistory.length() > 0) {
				inspectionHistory = inspectionHistory.substring(0, inspectionHistory.length() - 1);
			}
			each.setInspectionHistory(inspectionHistory);
		}
		return new ResponseData(iqcInspectionLList);
	}
	
	/**
	 * 获取L表
	 * @description
	 * @author tianmin.wang
	 * @date 2019年11月22日 
	 * @param dto
	 * @return
	 */
	private String getLine(PqcInspectionC dto) {
		// TODO Auto-generated method stub
		PqcInspectionL sear = new PqcInspectionL();
		sear.setLineId(dto.getLineId());
		sear = pqcInspectionLMapper.selectByPrimaryKey(sear);
		return sear.getInspectionStatus();
	}

	/**
	 * 保存
	 * @description
	 * @author tianmin.wang
	 * @date 2019年11月22日 
	 * @param dto
	 * @param result
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/hqm/pqc/inspection/c/submit")
	@ResponseBody
	public ResponseData update(@RequestBody List<PqcInspectionC> dto, BindingResult result,
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
	 * 删除
	 * @description
	 * @author tianmin.wang
	 * @date 2019年11月22日 
	 * @param request
	 * @param dto
	 * @return
	 */
	@RequestMapping(value = "/hqm/pqc/inspection/c/remove")
	@ResponseBody
	public ResponseData delete(HttpServletRequest request, @RequestBody List<PqcInspectionC> dto) {
		service.batchDelete(dto);
		return new ResponseData();
	}

	
	/**
	 * 保存明细
	 * @description
	 * @author tianmin.wang
	 * @date 2019年11月22日 
	 * @param dto
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/hqm/pqc/inspection/c/savedetail")
	@ResponseBody
	public ResponseData detailSave(@RequestBody List<PqcInspectionC> dto, HttpServletRequest request) {

		IRequest requestCtx = createRequestContext(request);
		ResponseData responseData = new ResponseData();
		try {
			List<PqcInspectionC> rows = service.detailSave(requestCtx, request, dto);
			responseData.setRows(rows);
		} catch (Exception e) {
			responseData.setSuccess(false);
			responseData.setMessage(e.getMessage());
		}
		return responseData;
	}

	/**
	 * 查询所有明细
	 * @description
	 * @author tianmin.wang
	 * @date 2019年11月22日 
	 * @param dtoh
	 * @param page
	 * @param pageSize
	 * @param request
	 * @return
	 * @throws Throwable
	 */
	@RequestMapping(value = "/hqm/pqc/inspection/c/getalldetail")
	@ResponseBody
	public ResponseData getAllDetail(PqcInspectionL dtoh, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
			@RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) throws Throwable {
		IRequest requestCtx = createRequestContext(request);
		return new ResponseData(service.getDetail(requestCtx, dtoh));
	}

	/**
	 * 保存所有明细
	 * @description
	 * @author tianmin.wang
	 * @date 2019年11月22日 
	 * @param dto
	 * @param result
	 * @param request
	 * @return
	 * @throws Throwable
	 */
	@RequestMapping(value = "/hqm/pqc/inspection/c/savealldetail")
	@ResponseBody
	public ResponseData saveDetail(@RequestBody List<PqcInspectionC> dto, BindingResult result,
			HttpServletRequest request) throws Throwable {
		IRequest requestCtx = createRequestContext(request);
		service.saveDetail(requestCtx, dto);
		return new ResponseData();
	}

	/**
	 * 提交操作
	 * @description
	 * @author tianmin.wang
	 * @date 2019年11月22日 
	 * @param dto
	 * @param result
	 * @param request
	 * @return
	 * @throws Throwable
	 */
	@RequestMapping(value = "/hqm/pqc/inspection/c/commitalldetail")
	@ResponseBody
	public ResponseData commitDetail(@RequestBody List<PqcInspectionC> dto, BindingResult result,
			HttpServletRequest request) throws Throwable {
		IRequest requestCtx = createRequestContext(request);
		service.commitDetail(requestCtx, dto);
		return new ResponseData();
	}

	@RequestMapping(value = "/hqm/pqc/inspection/c/selectfornon")
	@ResponseBody
	public ResponseData selectfornon(PqcInspectionC dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
			@RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
		IRequest requestContext = createRequestContext(request);
		List<PqcInspectionC> iqcInspectionLList = new ArrayList<PqcInspectionC>();
		iqcInspectionLList = service.selectfornon(requestContext, dto, page, pageSize);
		for (PqcInspectionC each : iqcInspectionLList) {
			PqcInspectionD search = new PqcInspectionD();
			search.setConnectId(each.getConnectId());
			List<PqcInspectionD> iqcInspectionDList = new ArrayList<PqcInspectionD>();
			iqcInspectionDList = pqcInspectionDMapper.select(search);
			String inspectionHistory = "";
			for (PqcInspectionD iqcInspectionD : iqcInspectionDList) {
				inspectionHistory += iqcInspectionD.getData() + ",";
			}
			if (inspectionHistory.length() > 0) {
				inspectionHistory = inspectionHistory.substring(0, inspectionHistory.length() - 1);
			}
			each.setInspectionHistory(inspectionHistory);
		}
		return new ResponseData(iqcInspectionLList);
	}
}