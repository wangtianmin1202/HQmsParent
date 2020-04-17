package com.hand.hqm.hqm_iqc_inspection_l.controllers;

import org.springframework.stereotype.Controller;
import com.hand.hap.system.controllers.BaseController;
import com.hand.hap.core.IRequest;
import com.hand.hap.system.dto.ResponseData;
import com.hand.hap.util.SystemApiMethod;
import com.hand.hqm.hqm_fqc_inspection_l.dto.FqcInspectionL;
import com.hand.hqm.hqm_iqc_inspection_d.dto.IqcInspectionD;
import com.hand.hqm.hqm_iqc_inspection_d.mapper.IqcInspectionDMapper;
import com.hand.hqm.hqm_iqc_inspection_h.dto.IqcInspectionH;
import com.hand.hqm.hqm_iqc_inspection_h.mapper.IqcInspectionHMapper;
import com.hand.hqm.hqm_iqc_inspection_l.dto.IqcInspectionL;
import com.hand.hqm.hqm_iqc_inspection_l.service.IIqcInspectionLService;

import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import javax.servlet.http.HttpServletRequest;
import org.springframework.validation.BindingResult;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/*
 * created by tianmin.wang on 2019/7/9.
 */
@Controller
public class IqcInspectionLController extends BaseController {

	@Autowired
	private IIqcInspectionLService service;
	@Autowired
	IqcInspectionDMapper iqcInspectionDMapper;
	@Autowired
	IqcInspectionHMapper iqcInspectionHMapper;

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
	@RequestMapping(value = "/hqm/iqc/inspection/l/query")
	@ResponseBody
	public ResponseData query(IqcInspectionL dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
			@RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
		IRequest requestContext = createRequestContext(request);
		List<IqcInspectionL> iqcInspectionLList = new ArrayList<IqcInspectionL>();
		iqcInspectionLList = service.select(requestContext, dto, page, pageSize);
		for (IqcInspectionL each : iqcInspectionLList) {
			IqcInspectionD search = new IqcInspectionD();
			search.setLineId(each.getLineId());
			List<IqcInspectionD> iqcInspectionDList = new ArrayList<IqcInspectionD>();
			iqcInspectionDList = iqcInspectionDMapper.select(search);
			String inspectionHistory = "";
			for (IqcInspectionD iqcInspectionD : iqcInspectionDList) {
				inspectionHistory += (iqcInspectionD.getData() == null ? "" : iqcInspectionD.getData()) + ",";
			}
			if (inspectionHistory.length() > 0) {
				inspectionHistory = inspectionHistory.substring(0, inspectionHistory.length() - 1);
			}
			each.setInspectionHistory(inspectionHistory);
		}
		return new ResponseData(iqcInspectionLList);
	}

	@RequestMapping(value = "/hqm/iqc/inspection/l/attribute/query")
	@ResponseBody
	public ResponseData queryAttribute(IqcInspectionL dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
			@RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
		IRequest requestContext = createRequestContext(request);
		List<IqcInspectionL> iqcInspectionLList = new ArrayList<IqcInspectionL>();
		iqcInspectionLList = service.selectAttribute(requestContext, dto, page, pageSize);
		return new ResponseData(iqcInspectionLList);
	}

	/**
	 * 
	 * @description 头H表信息
	 * @author tianmin.wang
	 * @date 2019年11月22日
	 * @param dto
	 * @return
	 */
	private String getHeader(IqcInspectionL dto) {
		// TODO Auto-generated method stub
		IqcInspectionH sear = new IqcInspectionH();
		sear.setHeaderId(dto.getHeaderId());
		sear = iqcInspectionHMapper.selectByPrimaryKey(sear);
		return sear.getInspectionStatus();
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
	@RequestMapping(value = "/hqm/iqc/inspection/l/select")
	@ResponseBody
	public ResponseData select(IqcInspectionL dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
			@RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
		IRequest requestContext = createRequestContext(request);
		List<IqcInspectionL> iqcInspectionLList = new ArrayList<IqcInspectionL>();
		iqcInspectionLList = service.reSelect(requestContext, dto, page, pageSize);
		if ("5".equals(getHeader(dto)) || "4".equals(getHeader(dto))) {
			iqcInspectionLList = iqcInspectionLList.stream().sorted(Comparator.comparing((p1) -> {
				return StringUtils.isEmpty(p1.getAttributeInspectionRes()) ? "" : p1.getAttributeInspectionRes();
			})).collect(Collectors.toList());
		}
		for (IqcInspectionL each : iqcInspectionLList) {
			IqcInspectionD search = new IqcInspectionD();
			search.setLineId(each.getLineId());
			List<IqcInspectionD> iqcInspectionDList = new ArrayList<IqcInspectionD>();
			iqcInspectionDList = iqcInspectionDMapper.select(search);
			String inspectionHistory = "";
			for (IqcInspectionD iqcInspectionD : iqcInspectionDList) {
				inspectionHistory += (iqcInspectionD.getData() == null ? "" : iqcInspectionD.getData()) + ",";
			}
			if (inspectionHistory.length() > 0) {
				inspectionHistory = inspectionHistory.substring(0, inspectionHistory.length() - 1);
			}
			each.setInspectionHistory(inspectionHistory);
		}
		return new ResponseData(iqcInspectionLList);
	}

	@RequestMapping(value = "/hqm/iqc/inspection/l/selectFornon")
	@ResponseBody
	public ResponseData selectFornon(IqcInspectionL dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
			@RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
		IRequest requestContext = createRequestContext(request);
		List<IqcInspectionL> iqcInspectionLList = new ArrayList<IqcInspectionL>();
		iqcInspectionLList = service.reSelectFornon(requestContext, dto, page, pageSize);
		for (IqcInspectionL each : iqcInspectionLList) {
			IqcInspectionD search = new IqcInspectionD();
			search.setLineId(each.getLineId());
			List<IqcInspectionD> iqcInspectionDList = new ArrayList<IqcInspectionD>();
			iqcInspectionDList = iqcInspectionDMapper.select(search);
			String inspectionHistory = "";
			for (IqcInspectionD iqcInspectionD : iqcInspectionDList) {
				inspectionHistory += iqcInspectionD.getData() + ",";
			}
			if (inspectionHistory.length() > 0) {
				inspectionHistory = inspectionHistory.substring(0, inspectionHistory.length() - 1);
			}
			each.setInspectionHistory(inspectionHistory);
		}
		return new ResponseData(iqcInspectionLList);
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
	@RequestMapping(value = "/hqm/iqc/inspection/l/submit")
	@ResponseBody
	public ResponseData update(@RequestBody List<IqcInspectionL> dto, BindingResult result,
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

	@RequestMapping(value = "/hqm/iqc/inspection/l/attribute/submit")
	@ResponseBody
	public ResponseData updateAttribute(@RequestBody List<IqcInspectionL> dto, BindingResult result,
			HttpServletRequest request) {
		getValidator().validate(dto, result);
		if (result.hasErrors()) {
			ResponseData responseData = new ResponseData(false);
			responseData.setMessage(getErrorMessage(result, request));
			return responseData;
		}
		for (IqcInspectionL dao : dto) {
			if(dao.getSampleWayId() == null) {
				ResponseData responseData = new ResponseData(false);
				responseData.setMessage("抽样方式是必须的");
				return responseData;
			}
			if ("M".equals(dao.getStandardType())) {// 计量型的一些校验
				if (StringUtils.isEmpty(dao.getStandradFrom()) && StringUtils.isEmpty(dao.getStandradTo())) {
					ResponseData responseData = new ResponseData(false);
					responseData.setMessage("计量型请至少输入规格值上下限的一项");
					return responseData;
				}
				if (!StringUtils.isEmpty(dao.getStandradFrom()) && !StringUtils.isEmpty(dao.getStandradTo())) {
					if (SystemApiMethod.getPercision(dao.getStandradFrom()) != SystemApiMethod
							.getPercision(dao.getStandradTo())) {
						ResponseData responseData = new ResponseData(false);
						responseData.setMessage("上下限精度不一致");
						return responseData;
					}
				}
			}
		}
		IRequest requestCtx = createRequestContext(request);
		return new ResponseData(service.batchAttributeUpdate(requestCtx, dto));
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
	@RequestMapping(value = "/hqm/iqc/inspection/l/remove")
	@ResponseBody
	public ResponseData delete(HttpServletRequest request, @RequestBody List<IqcInspectionL> dto) {
		service.batchDelete(dto);
		return new ResponseData();
	}

	@RequestMapping(value = "/hqm/iqc/inspection/l/attribute/remove")
	@ResponseBody
	public ResponseData deleteAttribute(HttpServletRequest request, @RequestBody List<IqcInspectionL> dto) {
		service.batchAttributeDelete(dto);
		return new ResponseData();
	}

	/**
	 * 
	 * @description 获取所有明细
	 * @author tianmin.wang
	 * @date 2019年11月22日
	 * @param dtoh
	 * @param page
	 * @param pageSize
	 * @param request
	 * @return
	 * @throws Throwable
	 */
	@RequestMapping(value = "/hqm/iqc/inspection/l/getalldetail")
	@ResponseBody
	public ResponseData getAllDetail(IqcInspectionH dtoh, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
			@RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) throws Throwable {
		IRequest requestCtx = createRequestContext(request);
		return new ResponseData(service.getDetail(requestCtx, dtoh));
	}

	/**
	 * 
	 * @description 保存所有明细
	 * @author tianmin.wang
	 * @date 2019年11月22日
	 * @param dto
	 * @param result
	 * @param request
	 * @return
	 * @throws Throwable
	 */
	@RequestMapping(value = "/hqm/iqc/inspection/l/savealldetail")
	@ResponseBody
	public ResponseData saveDetail(@RequestBody List<IqcInspectionL> dto, BindingResult result,
			HttpServletRequest request) throws Throwable {
		IRequest requestCtx = createRequestContext(request);
		service.saveDetail(requestCtx, request, dto);
		return new ResponseData();
	}

	/**
	 * 
	 * @description 提交明细
	 * @author tianmin.wang
	 * @date 2019年11月22日
	 * @param dto
	 * @param result
	 * @param request
	 * @return
	 * @throws Throwable
	 */
	@RequestMapping(value = "/hqm/iqc/inspection/l/commitalldetail")
	@ResponseBody
	public ResponseData commitDetail(@RequestBody List<IqcInspectionL> dto, BindingResult result,
			HttpServletRequest request) throws Throwable {
		IRequest requestCtx = createRequestContext(request);
		return service.commitDetail(requestCtx, request, dto);
	}
}