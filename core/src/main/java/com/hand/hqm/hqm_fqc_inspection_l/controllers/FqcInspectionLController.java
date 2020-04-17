package com.hand.hqm.hqm_fqc_inspection_l.controllers;

import org.springframework.stereotype.Controller;
import com.hand.hap.system.controllers.BaseController;
import com.hand.hap.core.IRequest;
import com.hand.hap.system.dto.ResponseData;
import com.hand.hap.util.SystemApiMethod;
import com.hand.hqm.hqm_fqc_inspection_l.dto.FqcInspectionL;
import com.hand.hqm.hqm_fqc_inspection_l.service.IFqcInspectionLService;
import jodd.util.StringUtil;
import com.hand.hqm.hqm_fqc_inspection_d.dto.FqcInspectionD;
import com.hand.hqm.hqm_fqc_inspection_d.mapper.FqcInspectionDMapper;
import com.hand.hqm.hqm_fqc_inspection_h.dto.FqcInspectionH;
import com.hand.hqm.hqm_fqc_inspection_h.mapper.FqcInspectionHMapper;

import org.drools.core.util.StringUtils;
import org.junit.Assert;
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
public class FqcInspectionLController extends BaseController {

	@Autowired
	private IFqcInspectionLService service;
	@Autowired
	FqcInspectionDMapper fqcInspectionDMapper;
	@Autowired
	FqcInspectionHMapper fqcInspectionHMapper;

	/**
	 * 
	 * @description 查询 拼接D表数据
	 * @author tianmin.wang
	 * @date 2019年11月22日
	 * @param dto
	 * @param page
	 * @param pageSize
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/hqm/fqc/inspection/l/query")
	@ResponseBody
	public ResponseData query(FqcInspectionL dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
			@RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
		IRequest requestContext = createRequestContext(request);
		List<FqcInspectionL> fqcInspectionLList = new ArrayList<FqcInspectionL>();
		fqcInspectionLList = service.query(requestContext, dto, page, pageSize);
		if ("5".equals(getHeader(dto)) || "4".equals(getHeader(dto))) {
			fqcInspectionLList = fqcInspectionLList.stream().sorted(Comparator.comparing(
					p -> StringUtil.isEmpty(p.getAttributeInspectionRes()) ? "" : p.getAttributeInspectionRes()))
					.collect(Collectors.toList());
		}
		for (FqcInspectionL each : fqcInspectionLList) {
			FqcInspectionD search = new FqcInspectionD();
			search.setLineId(each.getLineId());
			List<FqcInspectionD> fqcInspectionDList = new ArrayList<FqcInspectionD>();
			fqcInspectionDList = fqcInspectionDMapper.select(search);
			String inspectionHistory = "";
			for (FqcInspectionD fqcInspectionD : fqcInspectionDList) {
				inspectionHistory += fqcInspectionD.getData() + ",";
			}
			if (inspectionHistory.length() > 0) {
				inspectionHistory = inspectionHistory.substring(0, inspectionHistory.length() - 1);
			}
			each.setInspectionHistory(inspectionHistory);
		}
		return new ResponseData(fqcInspectionLList);
	}

	@RequestMapping(value = "/hqm/fqc/inspection/l/attribute/query")
	@ResponseBody
	public ResponseData queryAttribute(FqcInspectionL dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
			@RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
		IRequest requestContext = createRequestContext(request);
		List<FqcInspectionL> iqcInspectionLList = new ArrayList<FqcInspectionL>();
		iqcInspectionLList = service.selectAttribute(requestContext, dto, page, pageSize);
		return new ResponseData(iqcInspectionLList);
	}

	/**
	 * 
	 * @description 获取H表数据
	 * @author tianmin.wang
	 * @date 2019年11月22日
	 * @param dto
	 * @return
	 */
	private String getHeader(FqcInspectionL dto) {
		// TODO Auto-generated method stub
		FqcInspectionH sear = new FqcInspectionH();
		sear.setHeaderId(dto.getHeaderId());
		sear = fqcInspectionHMapper.selectByPrimaryKey(sear);
		return sear.getInspectionStatus();
	}

	@RequestMapping(value = "/hqm/fqc/inspection/l/queryfornon")
	@ResponseBody
	public ResponseData queryfornon(FqcInspectionL dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
			@RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
		IRequest requestContext = createRequestContext(request);
		List<FqcInspectionL> fqcInspectionLList = new ArrayList<FqcInspectionL>();
		fqcInspectionLList = service.queryfornon(requestContext, dto, page, pageSize);
		for (FqcInspectionL each : fqcInspectionLList) {
			FqcInspectionD search = new FqcInspectionD();
			search.setLineId(each.getLineId());
			List<FqcInspectionD> fqcInspectionDList = new ArrayList<FqcInspectionD>();
			fqcInspectionDList = fqcInspectionDMapper.select(search);
			String inspectionHistory = "";
			for (FqcInspectionD fqcInspectionD : fqcInspectionDList) {
				inspectionHistory += fqcInspectionD.getData() + ",";
			}
			if (inspectionHistory.length() > 0) {
				inspectionHistory = inspectionHistory.substring(0, inspectionHistory.length() - 1);
			}
			each.setInspectionHistory(inspectionHistory);
		}
		return new ResponseData(fqcInspectionLList);
	}

	@RequestMapping(value = "/hqm/fqc/inspection/l/attribute/submit")
	@ResponseBody
	public ResponseData updateAttribute(@RequestBody List<FqcInspectionL> dto, BindingResult result,
			HttpServletRequest request) {
		getValidator().validate(dto, result);
		if (result.hasErrors()) {
			ResponseData responseData = new ResponseData(false);
			responseData.setMessage(getErrorMessage(result, request));
			return responseData;
		}
		for (FqcInspectionL dao : dto) {
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
	 * @description 数据保存
	 * @author tianmin.wang
	 * @date 2019年11月22日
	 * @param dto
	 * @param result
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/hqm/fqc/inspection/l/submit")
	@ResponseBody
	public ResponseData update(@RequestBody List<FqcInspectionL> dto, BindingResult result,
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
	@RequestMapping(value = "/hqm/fqc/inspection/l/remove")
	@ResponseBody
	public ResponseData delete(HttpServletRequest request, @RequestBody List<FqcInspectionL> dto) {
		service.batchDelete(dto);
		return new ResponseData();
	}

	@RequestMapping(value = "/hqm/fqc/inspection/l/attribute/remove")
	@ResponseBody
	public ResponseData deleteAttribute(HttpServletRequest request, @RequestBody List<FqcInspectionL> dto) {
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
	@RequestMapping(value = "/hqm/fqc/inspection/l/getalldetail")
	@ResponseBody
	public ResponseData getAllDetail(FqcInspectionH dtoh, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
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
	@RequestMapping(value = "/hqm/fqc/inspection/l/savealldetail")
	@ResponseBody
	public ResponseData saveDetail(@RequestBody List<FqcInspectionL> dto, BindingResult result,
			HttpServletRequest request) throws Throwable {
		IRequest requestCtx = createRequestContext(request);
		try {
			service.saveDetail(requestCtx, request, dto);
		} catch (RuntimeException e) {
			ResponseData re = new ResponseData(false);
			re.setMessage(e.getMessage());
			return re;
		} catch (Exception e1) {
			throw e1;
		}
		return new ResponseData();
	}

	/**
	 * 
	 * @description 提交所有明细
	 * @author tianmin.wang
	 * @date 2019年11月22日
	 * @param dto
	 * @param result
	 * @param request
	 * @return
	 * @throws Throwable
	 */
	@RequestMapping(value = "/hqm/fqc/inspection/l/commitalldetail")
	@ResponseBody
	public ResponseData commitDetail(@RequestBody List<FqcInspectionL> dto, BindingResult result,
			HttpServletRequest request) throws Throwable {
		IRequest requestCtx = createRequestContext(request);
		return service.commitDetail(requestCtx, request, dto);
	}
}